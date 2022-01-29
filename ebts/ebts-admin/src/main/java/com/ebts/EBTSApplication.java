package com.ebts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author binlin
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@EnableSwagger2
public class EBTSApplication {
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(EBTSApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  EBTS启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
