package com.example.demo.serverImpl.design.Factory.impl;

import org.springframework.stereotype.Service;

import com.example.demo.serverImpl.design.Factory.AbstractFactoryService;

@Service
public class FactoryServiceTwoImpl extends AbstractFactoryService {

    @Override
    public Integer getType() {
        return 2;
    }

    @Override
    public void demo() {
        System.out.println("FactoryServiceTwoImpl");
    }
}
