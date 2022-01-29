package com.ebts.common.exception.user;

/**
 * 验证码失效异常类
 *
 * @author binlin
 */
public class CaptchaExpireException extends UserException {


    public CaptchaExpireException() {
        super("user.jcaptcha.expire", null);
    }
}
