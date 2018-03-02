package com.example.demo.bean;

import java.io.Serializable;

public class MyDemoBean  implements Serializable {
    private static final long serialVersionUID = 2596306289679515800L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
