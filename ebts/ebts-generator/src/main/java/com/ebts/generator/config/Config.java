package com.ebts.generator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author 18209
 * @Date 2021/2/23 15:48
 * @Version 1.0
 */

@Component
@ConfigurationProperties(prefix = "ebts")
public class Config {



    /**
     * 上传路径
     */
    private static String profile;


    public static String getProfile() {
        return profile;
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath() {
        return getProfile() + "/download/";
    }
}
