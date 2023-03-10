package com.valid;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName ExtGlobalExceptionHandler
 * @Description TODO
 * @Author QiBin
 * @Date 2022/6/20 18:54
 * @Version 1.0
 **/

//@RestControllerAdvice
public class ExtGlobalExceptionHandler {

    /**
     * valid校验异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public Map<String,Object> exceptionHandler(HttpServletRequest req, ConstraintViolationException e) {
        StringBuffer sb = new StringBuffer("参数校验异常：");
        Throwable rootCause = e.getCause();
        Set<ConstraintViolation<?>> eList = e.getConstraintViolations();
        if(eList.size() > 0){
            eList.forEach((o)->{
                sb.append("参数");
                sb.append(o.getPropertyPath().toString().substring(o.getPropertyPath().toString().lastIndexOf(".")+1));
                sb.append(o.getMessage());
                sb.append(";");
            } );
        }else{
            sb.append(e.getMessage());
        }
        Map<String,Object> result = new HashMap<>();
        result.put("code",500);
        result.put("message",sb.toString());
        return result;
    }
}
