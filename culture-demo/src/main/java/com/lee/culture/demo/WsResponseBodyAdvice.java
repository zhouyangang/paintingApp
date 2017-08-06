package com.lee.culture.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Created by zhengjun.jing on 7/14/2017.
 */

@ControllerAdvice(basePackages = "com.lee.culture.demo")
public class WsResponseBodyAdvice implements ResponseBodyAdvice {

    private static final Logger LOG = LogManager.getLogger(WsResponseBodyAdvice.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    /**
     * 在Response返回之前执行的动作
     * @param o
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        //如果为null,直接return
        if(o == null){
            if (isJsonResponse(mediaType)) {
                return WsResponse.success();
            } else {
                return null;
            }
        }else {
            // TODO: 文件上传下载要做处理，不能封装，直接返回
            if (o instanceof Resource) {
                return o;
            } else if (o instanceof String) {
                // 按道理 只要API 返回值是 String / byte[]等
                // 不会由MappingJackson2HttpMessageConverter处理的返回值
                // 都有可能出错，抛出ClassCastException...
                // 目前API 出现的比较多的是String，所以只处理String情况
                // 如果 API返回的是 String，
                try {
                    if (WsResponse.isWsResponseJson((String) o)) {
                        // 已经是 WsResponse格式的 字符串不做统一格式封装
                        return o;
                    } else {
                        if (isJsonResponse(mediaType)) {
                            // 如果 produces = application/json格式时，
                            // String返回值 将被StringHttpMessageConvertor处理，
                            // 所以此时应该返回字符串
                            return mapper.writeValueAsString(WsResponse.success(o));
                        } else {
                            // 如果 produces = text/html时，
                            // String返回值 将被StringHttpMessageConverter处理，
                            // 所以此时应该返回WsResponse的json序列化后的字符串
                            // 如果此时还是返回WsResponse对象，会抛出ClassCastException
                            // 因为StringHttpMessageConverter会把WsResponse对象当做String处理
                            return mapper.writeValueAsString(WsResponse.success(o));
                        }
                    }
                } catch (JsonProcessingException e) {
                    LOG.warn("json serialize error", e);
                    // 因为 API返回值为String，
                    // 所以不会抛异常，此处在业务上 是进不来的...
                    return o;
                }
            } else {
                if(o instanceof WsResponse){
                    //如果已经封装成WsResponse,直接return
                    return o;
                }else{
                    //封装成WsResponse
                    return WsResponse.success(o);
                }
            }
        }
    }

    private boolean isJsonResponse(MediaType mediaType) {
        if (mediaType.equals(MediaType.APPLICATION_JSON) ||
                mediaType.equals(MediaType.APPLICATION_JSON_UTF8)) {
            return true;
        } else {
            return false;
        }
    }
}
