package com.valid;

/**
 * @ClassName BaseException
 * @Description TODO
 * @Author QiBin
 * @Date 2022/6/23 10:46
 * @Version 1.0
 **/

import lombok.Getter;
import lombok.Setter;

/**
 * Base exception is the parent of all exceptions
 *
 * @author artisan
 */

public abstract class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private int errorCode;

    public BaseException(String errorMessage) {
        super(errorMessage);
    }

    public BaseException(int errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public BaseException(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }

    public BaseException(int errorCode, String errorMessage, Throwable e) {
        super(errorMessage, e);
        this.errorCode = errorCode;
    }
}
