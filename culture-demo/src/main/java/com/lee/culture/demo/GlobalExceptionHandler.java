package com.lee.culture.demo;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Iterator;

/**
 * Created by zhengjun.jing on 7/14/2017.
 */

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOG = LogManager.getLogger(GlobalExceptionHandler.class);


    /**
     * 处理ServiceException, 封装成WsResponse的统一格式
     *
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(value = ServiceException.class)
    @ResponseBody
    public WsResponse errorHandler(HttpServletRequest request, ServiceException exception) {
        LOG.error("error", exception);
        return WsResponse.failure(exception.getMessage());
    }

    /**
     * 处理JSR303 验证异常 exception,API入参和出参的数据验证时发生的异常
     *
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public WsResponse errorHandler(HttpServletRequest request, MethodArgumentNotValidException exception) {
        LOG.error("error", exception);
        StringBuilder errorMsg = new StringBuilder();
        for (ObjectError objectError : exception.getBindingResult().getAllErrors()) {
            if (objectError instanceof FieldError) {
                FieldError fieldError = (FieldError) objectError;
                errorMsg.append("[" + fieldError.getField() + " = " + fieldError.getRejectedValue() + "]" + fieldError.getDefaultMessage() +"; ");
            } else {
                errorMsg.append(objectError.toString() + "; ");
            }
        }
        return WsResponse.failure(errorMsg.toString());
    }

    /**
     * 处理 ConstraintViolationException的异常
     *
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public WsResponse errorHandler(HttpServletRequest request, ConstraintViolationException exception) {
        StringBuilder errorMsg = new StringBuilder();
        for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {

            Iterator<Path.Node> iterator = constraintViolation.getPropertyPath().iterator();
            String name = iterator.next().getName();
            while (iterator.hasNext()) {
                name = iterator.next().getName();
            }

            errorMsg.append("[" + name + " = " + constraintViolation.getInvalidValue() + "]" + constraintViolation.getMessage() +"; ");

        }
        LOG.error("error", exception);
        return WsResponse.failure(errorMsg.toString());
    }



    /**
     * 枚举类型不能匹配的/参数类型不匹配
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public WsResponse errorHandler(HttpServletRequest request, HttpMessageNotReadableException exception){
        LOG.error("error", exception);
        return WsResponse.failure(exception.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public WsResponse errorHandler(HttpServletRequest request, MethodArgumentTypeMismatchException exception){
        LOG.error("error", exception);
        return WsResponse.failure(exception.getMessage());
    }

    /**
     * 没有catch的到 exception统一处理，系统的最后防线
     *
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public WsResponse jsonErrorHandler(HttpServletRequest request, Exception exception) {
        LOG.error("error", exception);
        return WsResponse.failure(exception.getMessage());
    }

}
