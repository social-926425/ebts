package com.ebts.generator.utils.exception;

/**
 * 自定义异常
 *
 * @author binlin
 */
public class GenCustomException extends RuntimeException {


    private Integer code;

    private String message;

    public GenCustomException(String message) {
        this.message = message;
    }

    public GenCustomException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public GenCustomException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
