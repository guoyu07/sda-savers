package com.sda.savers.web.exception;

/**
 * Created by lenovo on 2017/7/5.
 */
public class PriceException extends RuntimeException {

    public PriceException(String message) {
        super(message);
    }

    public PriceException(String message, Throwable cause) {
        super(message, cause);
    }
}
