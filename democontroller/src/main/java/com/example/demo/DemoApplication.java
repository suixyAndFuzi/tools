package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ServletComponentScan
public class DemoApplication {
    private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    public static void main(String[] args) {
        
        //增加这个配置 解决启动时 Elasticsearch 冲突报错 - - - - - - 暂时没找到好方法解决
        System.setProperty("es.set.netty.runtime.available.processors", "false");

        SpringApplication.run(DemoApplication.class, args);
        logger.info("service start");
    }

}
