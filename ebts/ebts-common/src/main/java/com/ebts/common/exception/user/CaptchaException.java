package com.ebts.common.exception.user;

/**
 * 验证码错误异常类
 *
 * @author binlin
 */
public class CaptchaException extends UserException {


    public CaptchaException() {
        super("user.jcaptcha.error", null);
    }
}
