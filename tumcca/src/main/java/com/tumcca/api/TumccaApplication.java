package com.tumcca.api;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Optional;
import com.tumcca.api.auth.TumccaBasicAuthenticator;
import com.tumcca.api.auth.TumccaOauthAuthenticator;
import com.tumcca.api.db.ErrorsDAO;
import com.tumcca.api.db.UsersDAO;
import com.tumcca.api.model.*;
import com.tumcca.api.resources.*;
import com.tumcca.api.service.AtmosphereClientService;
import com.tumcca.api.service.AuthService;
import com.tumcca.api.service.ElasticSearchService;
import com.tumcca.api.service.ServerClustersService;
import com.tumcca.api.util.PasswordHash;
import com.tumcca.api.util.WSUtil;
import com.xeiam.dropwizard.sundial.SundialBundle;
import com.xeiam.dropwizard.sundial.SundialConfiguration;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.ChainedAuthFactory;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.auth.oauth.OAuthFactory;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

import org.apache.commons.lang3.StringUtils;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereServlet;
import org.atmosphere.wasync.ClientFactory;
import org.atmosphere.wasync.RequestBuilder;
import org.atmosphere.wasync.impl.AtmosphereClient;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.elasticsearch.node.Node;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

public class TumccaApplication extends Application<TumccaConfiguration> {
    static final Logger LOGGER = LoggerFactory.getLogger(TumccaApplication.class);

    public static void main(String[] args) throws Exception {
        new TumccaApplication().run(args);
    }

    @Override
    public String getName() {
        return "tumcca";
    }

    @Override
    public void initialize(Bootstrap<TumccaConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

        bootstrap.addBundle(new MultiPartBundle());

        bootstrap.addBundle(new SwaggerBundle<TumccaConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(TumccaConfiguration configuration) {
                // this would be the preferred way to set up swagger, you can also construct the object here programtically if you want
                return configuration.swaggerBundleConfiguration;
            }
        });

        bootstrap.addBundle(new SundialBundle<TumccaConfiguration>() {
            @Override
            public SundialConfiguration getSundialConfiguration(TumccaConfiguration configuration) {
                return configuration.getSundialConfiguration();
            }
        });
    }

    @Override
    public void run(TumccaConfiguration configuration, Environment environment) throws Exception {
        // Enable CORS headers
        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin,Authorization,Content-Range,Content-Disposition,Content-Description");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        DataSourceFactory dataSourceFactory = configuration.getDataSourceFactory();

        final Client client = new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration())
                .build(getName());
        final String uploadPath = configuration.getUploadPath();
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        final DBIFactory factory = new DBIFactory();
        DBI dbi = factory.build(environment, dataSourceFactory, "mysql");

        final AuthService authService = new AuthService(dbi, configuration.getSessionTimeout());

        final MetricRegistry metricRegistry = new MetricRegistry();

//        CachingAuthenticator<BasicCredentials, Principals> cachingBasicAuthenticator =
//                new CachingAuthenticator<>(metricRegistry,
//                        new TumccaBasicAuthenticator(authService),
//                        CacheBuilderSpec.parse("maximumSize=1000, expireAfterAccess=5m"));
//
//        CachingAuthenticator<String, Principals> cachingOauthAuthenticator =
//                new CachingAuthenticator<>(metricRegistry,
//                        new TumccaOauthAuthenticator(authService),
//                        CacheBuilderSpec.parse("maximumSize=5000, expireAfterAccess=5m"));

        ChainedAuthFactory chainedFactory = new ChainedAuthFactory(
                new BasicAuthFactory<>(new TumccaBasicAuthenticator(authService),
                        "realm",
                        Principals.class),
                new OAuthFactory<>(new TumccaOauthAuthenticator(authService),
                        "realm",
                        Principals.class));

        environment.jersey().register(AuthFactory.binder(chainedFactory));

        final OSSConfig ossConfig = configuration.getOssConfig();
        final OSSClient ossClient = new OSSClient(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getSecretAccessKey());

        if (!ossClient.doesBucketExist(ossConfig.getTrashBucket())) {
            ossClient.createBucket(ossConfig.getTrashBucket());
            ossClient.setBucketAcl(ossConfig.getTrashBucket(), CannedAccessControlList.Private);
        }

        List<ErrorVO> errors;
        Map<Integer, ErrorVO> errorsMap;
        try (final ErrorsDAO errorsDAO = dbi.open(ErrorsDAO.class)) {
            errors = errorsDAO.findAll();
            errorsMap = errors.stream().collect(Collectors.toMap(ErrorVO::getCode, e -> e));
        }

        environment.lifecycle().manage(new ServerClustersService(dbi, configuration.getServerId()));
        final Node node = nodeBuilder().node();
        ElasticSearchService ecs = new ElasticSearchService(node);
        environment.lifecycle().manage(ecs);

        AtmosphereServlet servlet = new AtmosphereServlet();
        servlet.framework().addInitParameter("com.sun.jersey.config.property.packages", "com.tumcca.api.resources.atmosphere");
        servlet.framework().addInitParameter(ApplicationConfig.WEBSOCKET_CONTENT_TYPE, "application/json");
        servlet.framework().addInitParameter(ApplicationConfig.WEBSOCKET_SUPPORT, "true");
        servlet.framework().addInitParameter("org.atmosphere.cpr.broadcastFilterClasses", "com.tumcca.api.filter.MessageFilter");

        final String domain = configuration.getDomain();
        final String followUri = "/ws/follow";
        environment.servlets().addServlet("follow", servlet).addMapping(followUri + "/*");
        final AtmosphereClient atmosphereClient = ClientFactory.getDefault().newClient(AtmosphereClient.class);
        final Admin admin = configuration.getAdmin();

        try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
            if (usersDAO.countEmail(Optional.of(admin.getEmail())) == 0) {
                try {
                    usersDAO.begin();
                    final Long uid = usersDAO.insertUser(Optional.fromNullable(admin.getEmail()), Optional.fromNullable(admin.getMobile()), Optional.of(PasswordHash.hash(admin.getPassword())));
                    usersDAO.insertRole(Optional.of(uid), Optional.of(Roles.ADMIN.toString()));
                    usersDAO.commit();
                } catch (Exception e) {
                    usersDAO.rollback();
                    throw new WebApplicationException(e);
                }
            }
        }

        final Optional<Principals> principal = authService.signIn(admin.getEmail(), admin.getPassword());
        final String token = principal.get().getToken();
        final RequestBuilder followRequest = WSUtil.createFollowRequest(token, domain, followUri, atmosphereClient);

        final String unfollowUri = "/ws/unfollow";
        environment.servlets().addServlet("unfollow", servlet).addMapping(unfollowUri + "/*");
        final RequestBuilder unFollowRequest = WSUtil.createUnFollowRequest(token, domain, unfollowUri, atmosphereClient);

        environment.lifecycle().manage(new AtmosphereClientService(atmosphereClient, followRequest, unFollowRequest, dbi, configuration.getClientServiceDelay()));

        environment.getApplicationContext().addFilter(new FilterHolder(new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {
            }

            @Override
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
                final String authorization = httpRequest.getHeader("Authorization");
                final int bearLen = "Bearer ".length();
                final HttpServletResponse response = (HttpServletResponse) servletResponse;
                if (StringUtils.isBlank(authorization) || authorization.length() <= bearLen + 2) {
                    response.sendError(401);
                    return;
                }
                final String token = authorization.substring(bearLen);
                final Sessions session;
                try {
                    session = authService.getSession(token);
                } catch (Exception e) {
                    throw new WebApplicationException(e);
                }
                String email;
                try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
                    email = usersDAO.findUserVO(Optional.of(session.getUid())).getEmail();
                } catch (Exception e) {
                    throw new WebApplicationException(e);
                }
                if (!admin.getEmail().equals(email)) {
                    if (!authService.validateSession(session)) {
                        response.sendError(403);
                        return;
                    }
                }

                filterChain.doFilter(servletRequest, servletResponse);
            }

            @Override
            public void destroy() {
            }
        }), "/ws/*", EnumSet.of(DispatcherType.REQUEST));

        environment.getApplicationContext().setAttribute("dbi", dbi);
        environment.getApplicationContext().setAttribute("uploadPath", uploadPath);
        environment.getApplicationContext().setAttribute("cacheTimeout", configuration.getCacheTimeout());
        environment.jersey().register(new MiscResource(errors, errorsMap, dbi));
        environment.jersey().register(new TestResource(dbi, uploadPath, configuration.getOssConfig(), errorsMap));
        environment.jersey().register(new AuthResource(dbi, authService, errorsMap));
        environment.jersey().register(new MaterialResource(dbi, uploadPath, ossClient, ossConfig.getBucketName(), ossConfig.getTrashBucket(), errorsMap, configuration.getCacheTimeout(), configuration.getServerId()));
        environment.jersey().register(new LogResource(dbi, errorsMap));
        environment.jersey().register(new WorksResource(dbi, errorsMap, configuration.getServerId(), ecs, admin.getHomePageUid()));
        environment.jersey().register(new NotificationResource(dbi, errorsMap, atmosphereClient, followRequest, unFollowRequest));
        environment.jersey().register(new ProfileResource(dbi, errorsMap));
        //Add by Neil 
        environment.jersey().register(new AlbumResource(dbi, errorsMap));
        environment.jersey().register(new FollowResource(dbi, errorsMap));
    }
}
