package com.tumcca.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import com.tumcca.api.model.Admin;
import com.tumcca.api.model.OSSConfig;
import com.xeiam.dropwizard.sundial.SundialConfiguration;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.DataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;

public class TumccaConfiguration extends Configuration {
    @NotEmpty
    String serverId;

    @NotEmpty
    String uploadPath;

    @NotEmpty
    String domain;

    @NotNull
    Long sessionTimeout;

    @NotNull
    Long cacheTimeout;

    @NotNull
    Admin admin;

    @NotNull
    Long clientServiceDelay;

    @NotNull
    OSSConfig ossConfig;

    @NotNull
    SwaggerBundleConfiguration swaggerBundleConfiguration;

    @Valid
    @NotNull
    SundialConfiguration sundialConfiguration = new SundialConfiguration();

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @NotNull
    private Map<String, Map<String, String>> viewRendererConfiguration = Collections.emptyMap();

    @Valid
    @NotNull
    private JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration();

    @JsonProperty
    public String getServerId() {
        return serverId;
    }

    @JsonProperty
    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    @JsonProperty
    public String getUploadPath() {
        return uploadPath;
    }

    @JsonProperty
    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    @JsonProperty
    public String getDomain() {
        return domain;
    }

    @JsonProperty
    public void setDomain(String domain) {
        this.domain = domain;
    }

    @JsonProperty
    public Long getSessionTimeout() {
        return sessionTimeout;
    }

    @JsonProperty
    public void setSessionTimeout(Long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    @JsonProperty
    public Long getCacheTimeout() {
        return cacheTimeout;
    }

    @JsonProperty
    public void setCacheTimeout(Long cacheTimeout) {
        this.cacheTimeout = cacheTimeout;
    }

    @JsonProperty("admin")
    public Admin getAdmin() {
        return admin;
    }

    @JsonProperty("admin")
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    @JsonProperty
    public Long getClientServiceDelay() {
        return clientServiceDelay;
    }

    @JsonProperty
    public void setClientServiceDelay(Long clientServiceDelay) {
        this.clientServiceDelay = clientServiceDelay;
    }

    @JsonProperty("oss")
    public OSSConfig getOssConfig() {
        return ossConfig;
    }

    @JsonProperty("oss")
    public void setOssConfig(OSSConfig ossConfig) {
        this.ossConfig = ossConfig;
    }

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration getSwaggerBundleConfiguration() {
        return swaggerBundleConfiguration;
    }

    @JsonProperty("swagger")
    public void setSwaggerBundleConfiguration(SwaggerBundleConfiguration swaggerBundleConfiguration) {
        this.swaggerBundleConfiguration = swaggerBundleConfiguration;
    }

    @JsonProperty("sundial")
    public SundialConfiguration getSundialConfiguration() {
        return sundialConfiguration;
    }

    @JsonProperty("sundial")
    public void setSundialConfiguration(SundialConfiguration sundialConfiguration) {
        this.sundialConfiguration = sundialConfiguration;
    }

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
    }

    @JsonProperty("viewRendererConfiguration")
    public Map<String, Map<String, String>> getViewRendererConfiguration() {
        return viewRendererConfiguration;
    }

    @JsonProperty("viewRendererConfiguration")
    public void setViewRendererConfiguration(Map<String, Map<String, String>> viewRendererConfiguration) {
        ImmutableMap.Builder<String, Map<String, String>> builder = ImmutableMap.builder();
        for (Map.Entry<String, Map<String, String>> entry : viewRendererConfiguration.entrySet()) {
            builder.put(entry.getKey(), ImmutableMap.copyOf(entry.getValue()));
        }
        this.viewRendererConfiguration = builder.build();
    }

    @JsonProperty("jerseyClient")
    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return jerseyClient;
    }

    @JsonProperty("jerseyClient")
    public void setJerseyClientConfiguration(JerseyClientConfiguration jerseyClient) {
        this.jerseyClient = jerseyClient;
    }
}
