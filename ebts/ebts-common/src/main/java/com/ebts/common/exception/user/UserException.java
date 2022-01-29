package com.ebts.common.exception.user;

import com.ebts.common.exception.BaseException;

/**
 * 用户信息异常类
 *
 * @author binlin
 */
public class UserException extends BaseException {


    public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }
}
