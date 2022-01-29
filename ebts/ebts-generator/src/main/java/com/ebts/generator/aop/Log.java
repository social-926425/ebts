package com.ebts.generator.aop;

import com.ebts.generator.enums.GenBusinessType;
import com.ebts.generator.enums.GenOperatorType;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 *
 * @author binlin
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 模块
     */
    public String title() default "";

    /**
     * 功能
     */
    public GenBusinessType businessType() default GenBusinessType.OTHER;

    /**
     * 操作人类别
     */
    public GenOperatorType operatorType() default GenOperatorType.MANAGE;

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;
}
