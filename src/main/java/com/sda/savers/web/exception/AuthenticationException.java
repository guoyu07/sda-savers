package com.sda.savers.web.exception;

/**
 * Created by lenovo on 2017/7/5.
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
