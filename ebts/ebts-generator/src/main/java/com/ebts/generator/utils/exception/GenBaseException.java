package com.ebts.generator.utils.exception;

import com.ebts.generator.utils.GenMessageUtils;
import com.ebts.generator.utils.GenStringUtils;

/**
 * 基础异常
 *
 * @author binlin
 */
public class GenBaseException extends RuntimeException {


    /**
     * 所属模块
     */
    private String module;

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误码对应的参数
     */
    private Object[] args;

    /**
     * 错误消息
     */
    private String defaultMessage;

    public GenBaseException(String module, String code, Object[] args, String defaultMessage) {
        this.module = module;
        this.code = code;
        this.args = args;
        this.defaultMessage = defaultMessage;
    }

    public GenBaseException(String module, String code, Object[] args) {
        this(module, code, args, null);
    }

    public GenBaseException(String module, String defaultMessage) {
        this(module, null, null, defaultMessage);
    }

    public GenBaseException(String code, Object[] args) {
        this(null, code, args, null);
    }

    public GenBaseException(String defaultMessage) {
        this(null, null, null, defaultMessage);
    }

    @Override
    public String getMessage() {
        String message = null;
        if (!GenStringUtils.isEmpty(code)) {
            message = GenMessageUtils.message(code, args);
        }
        if (message == null) {
            message = defaultMessage;
        }
        return message;
    }

    public String getModule() {
        return module;
    }

    public String getCode() {
        return code;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
