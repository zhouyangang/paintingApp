package com.lee.culture.demo;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lee.culture.demo.api.BaseApi;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhengjun.jing on 7/24/2017.
 * 全局的Filter，用来验证Http request header是否包含token信息，如果没有，直接拦截出去
 * Carday IAM 上去之后，这个拦截器会做解析IAM提供的JWT的工作，然后获取userid
 */
@Component
//@WebFilter(urlPatterns = {"/services/*"}, filterName = "authorizationFilter", initParams = {@WebInitParam(name = "EXCLUDED_PAGES", value = "/services/v2/api-docs")})
public class AuthorizationInterceptor implements HandlerInterceptor {
    private static final Logger LOG = LogManager.getLogger(AuthorizationInterceptor.class);
    private static final String[] excludedPageArray = {
            "/api/v2/api-docs",
            "/api/sign-up",
            "/api/sign-login",
            "/v2/api-docs"
    };

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        HttpServletRequest req = httpServletRequest;


        String token = req.getHeader("Authorization");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        WsResponse<String> wsResponse;
        if (!ExcludedPage(req) && (null == token || token.isEmpty())) {
            String Msg = "Authorization没有认证通过!原因为：客户端请求参数中无Authorization信息";
            LOG.error(Msg);
            wsResponse = WsResponse.failure(Msg);
        } else {
            wsResponse = WsResponse.success();
        }

        if (wsResponse.getStatus().getCode() != MessageCode.COMMON_SUCCESS.getCode()) {
            LOG.info(wsResponse.toString());
            ObjectMapper mapper = new ObjectMapper();
            httpServletResponse.getWriter().write(mapper.writeValueAsString(wsResponse));
            return false;
        } else {
            //TODO: IAM 上去之后，要根据token的信息get userId的信息
            try {
                httpServletRequest.setAttribute("userId", token);
                BaseApi.setLoginUser(token == null ? 0 : Integer.valueOf(token));
                return true;
            } catch (NumberFormatException e) {
                LOG.error(e.getMessage());
                wsResponse = WsResponse.failure(e.getMessage());
                ObjectMapper mapper = new ObjectMapper();
                httpServletResponse.getWriter().write(mapper.writeValueAsString(wsResponse));
                e.printStackTrace();
                return false;
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //Nothing
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        BaseApi.removeLoginUser();
    }


    private boolean ExcludedPage(HttpServletRequest req) {
        boolean isExcludedPage = false;
        for (String page : excludedPageArray) {// 遍历例外url数组
            // 判断当前URL是否与例外页面相同
            boolean isSwaggerURL = req.getRequestURI().indexOf("swagger") > 0;
            if (isSwaggerURL || req.getRequestURI().contains(page)) {
                isExcludedPage = true;
                break;
            }
        }
        return isExcludedPage;
    }


//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        excludedPages = filterConfig.getInitParameter("EXCLUDED_PAGES");
//        if (null != excludedPages && excludedPages.length() != 0) { // 例外页面不为空
//            excludedPageArray = excludedPages.split(String.valueOf(';'));
//        }
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
////        HttpServletRequest req = (HttpServletRequest) servletRequest;
//
//
//        String token = req.getHeader("Authorization");
//        servletResponse.setCharacterEncoding("UTF-8");
//        servletResponse.setContentType("application/json; charset=utf-8");
//        WsResponse<String> wsResponse;
//        if (!ExcludedPage(req) && (null == token || token.isEmpty())) {
//            String Msg = "token没有认证通过!原因为：客户端请求参数中无token信息";
//            LOG.error(Msg);
//            wsResponse = WsResponse.failure(MessageCode.COMMON_NO_AUTHORIZED, Msg);
//        } else {
//            wsResponse = WsResponse.success();
//        }
//
//        if (wsResponse.getStatus().getCode() != MessageCode.COMMON_SUCCESS.getCode()) {
//            LOG.info(wsResponse.toString());
//            ObjectMapper mapper = new ObjectMapper();
//            servletResponse.getWriter().write(mapper.writeValueAsString(wsResponse));
//            return;
//        } else {
//            //TODO: IAM 上去之后，要根据token的信息get userId的信息
//            servletRequest.setAttribute("userId", token);
//            BaseApi.setLoginUser(Long.valueOf(token));
//            filterChain.doFilter(new WsRequest((HttpServletRequest) servletRequest), servletResponse);
//        }
}


//
//    @Override
//    public void destroy() {
//        BaseApi.removeLoginUser();
//        this.excludedPages = null;
//        this.excludedPageArray = null;
//    }
//}
