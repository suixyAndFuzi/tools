package com.example.demo.serverImpl.design.Factory.impl;

import com.example.demo.serverImpl.design.Factory.AbstractFactoryService;
import org.springframework.stereotype.Service;

@Service
public class FactoryServiceOneImpl extends AbstractFactoryService {

    @Override
    public Integer getType() {
        return 1;
    }

    @Override
    public void demo() {
        System.out.println("FactoryServiceOneImpl");
    }
}
