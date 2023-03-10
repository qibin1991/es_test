package com.valid;


import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.stream.Collectors;

/**
 * @ClassName GlobalExceptionHandlerAssert
 * @Description TODO
 * @Author QiBin
 * @Date 2022/6/23 10:42
 * @Version 1.0
 * @mark: show me the code , change the world
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandlerAssert {

    /**
     * 默认全局异常处理。
     *
     * @param e e
     * @return ResponseData
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseData<String> exception(Exception e) {
        log.error("兜底异常信息 ex={}", e.getMessage());
        return ResponseData.fail(ResponseCode.RC500.getCode(), e.getMessage());
    }

    /**
     * Assert异常
     */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseData<String> exception(IllegalArgumentException e) {
        return ResponseData.fail(ResponseCode.ILLEGAL_ARGUMENT.getCode(), e.getMessage());
    }


    /**
     * 抓取自定义异常  BaseException
     */
    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseData<String> exception(BaseException e) {
        return ResponseData.fail(e.getErrorCode(), e.getMessage());
    }


    /**
     * @param e
     * @return
     */
    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseData<String>> handleValidatedException(Exception e) {
        ResponseData<String> resp = null;

        if (e instanceof MethodArgumentNotValidException) {
            // BeanValidation exception
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            resp = ResponseData.fail(HttpStatus.BAD_REQUEST.value(),
                    ex.getBindingResult().getAllErrors().stream()
                            .map(ObjectError::getDefaultMessage)
                            .collect(Collectors.joining("; "))
            );
        } else if (e instanceof ConstraintViolationException) {
            // BeanValidation GET simple param
            ConstraintViolationException ex = (ConstraintViolationException) e;
            resp = ResponseData.fail(HttpStatus.BAD_REQUEST.value(),
                    ex.getConstraintViolations().stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining("; "))
            );
        } else if (e instanceof BindException) {
            // BeanValidation GET object param
            BindException ex = (BindException) e;
            resp = ResponseData.fail(HttpStatus.BAD_REQUEST.value(),
                    ex.getAllErrors().stream()
                            .map(ObjectError::getDefaultMessage)
                            .collect(Collectors.joining("; "))
            );
        }

        log.error("参数校验异常:{}", resp.getMessage());
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理请求参数格式错误 @RequestParam上validate失败后抛出的异常是javax.validation.ConstraintViolationException
     *
     * @param httpServletRequest httpServletRequest
     * @param e                  e
     * @return Result
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<ResponseData<String>> exceptionHandler(HttpServletRequest httpServletRequest, ConstraintViolationException e) {
        log.error("{}, 发生异常: {}", httpServletRequest.getRequestURI(), e);
        StringBuilder sb = new StringBuilder();
        e.getConstraintViolations().forEach(constraintViolation -> {
            sb.append(StrUtil.format("字段:{},不允许值:{},{} ", constraintViolation.getPropertyPath(),
                    constraintViolation.getInvalidValue(), constraintViolation.getMessage()));
        });
        return new ResponseEntity<>(ResponseData.fail(HttpStatus.BAD_REQUEST.value(),sb.toString().trim()), HttpStatus.BAD_REQUEST);
    }


}