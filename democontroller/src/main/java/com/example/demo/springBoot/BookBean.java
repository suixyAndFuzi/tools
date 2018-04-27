package com.example.demo.springBoot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "book")
@PropertySource("classpath:book.properties")

@Data
public class BookBean {
    private String name;
    private String author;
    private String price;

}