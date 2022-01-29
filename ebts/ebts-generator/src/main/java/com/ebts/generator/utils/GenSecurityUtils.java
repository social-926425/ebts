package com.ebts.generator.utils;

import com.alibaba.fastjson.JSONObject;
import com.ebts.generator.utils.exception.GenCustomException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全服务工具类
 *
 * @author binlin
 */
public class GenSecurityUtils {

    public static Long getUserId() {
        try {
            Object object = getAuthentication().getPrincipal();
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(object);
            JSONObject userObject = (JSONObject) jsonObject.get("user");
            return (Long) userObject.get("userId");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new GenCustomException("获取用户账户异常", GenHttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}

