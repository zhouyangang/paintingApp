package com.lee.culture.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @date 2017-07-12
 * @author KevinPei
 */

@Aspect   //定义一个切面
@Configuration
public class ApiLogAop {
	
	private static final Logger logger = LogManager.getLogger(ApiLogAop.class);

	private static final ObjectMapper mapper = new ObjectMapper();

	    // 定义切点Pointcut
	    @Pointcut("execution(public * com.lee.culture.demo.api..*.*(..))")
	    public void excudeService() {
	    }

	    @Around("excudeService()")
	    public Object doAround(ProceedingJoinPoint point) throws Throwable {
	        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
	        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
	        HttpServletRequest request = sra.getRequest();

	       /* String url = request.getRequestURL().toString();
	        String method = request.getMethod();
	        String uri = request.getRequestURI();
	        String queryString = request.getQueryString();*/
	        //logger.info("请求开始, 各个参数, url: {}, method: {}, uri: {}, params: {}", url, method, uri, queryString);

			String simpleTargetName = point.getTarget().getClass().getSimpleName();
			String methodName = point.getSignature().getName();

			MethodSignature methodSignature = (MethodSignature) point.getSignature();
			String[] parameterNames = methodSignature.getParameterNames();  //获取参数名称

			Object[] method_param = point.getArgs(); // 获取方法参数
			StringBuilder operatingcontent = new StringBuilder();
			operatingcontent.append("Invoke Method : " + simpleTargetName + "." + methodName);
			// 将API 入参读取并进行JSON化输出
			operatingcontent.append("  Request params :");
			for (int i = 0; i < method_param.length; i++) {
				operatingcontent.append("" + parameterNames[i] + " = ");
				if (method_param[i] != null &&
						!(method_param[i] instanceof HttpServletRequest) &&
						!(method_param[i] instanceof HttpServletResponse)) {
					try {
						operatingcontent.append(mapper.writeValueAsString(method_param[i]) + " ");
					} catch (JsonProcessingException e) {
						// 某些Spring 辅助参数如 BindingResult无法JSON
						// 忽略异常
						continue;
					}
				} else {
					operatingcontent.append("null\n");
				}
			}
			logger.info(operatingcontent.toString());
	        // result的值就是被拦截方法的返回值
	        Object result = point.proceed();
			try {
				logger.info("Response:" + mapper.writeValueAsString(result));
			} catch (JsonProcessingException e) {
				// 如果返回的而是 不能 Json序列化的对象，
				// 例如文件下载时的 Resource对象
				// 则 捕获之
			}
			return  result;
	    }
}
