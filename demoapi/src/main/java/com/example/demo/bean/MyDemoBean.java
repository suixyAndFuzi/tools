package com.example.demo.bean;

import lombok.Data;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = MyDemoBean.INDEX, type = MyDemoBean.TYPE)
public class MyDemoBean implements Serializable {

    public static final String INDEX = "mydemobean_index";
    public static final String TYPE = "mydemobean";

    @Id
    private Long id;

    private String name;

    private String age;
}
