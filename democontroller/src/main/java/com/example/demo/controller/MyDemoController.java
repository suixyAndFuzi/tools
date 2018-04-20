package com.example.demo.controller;

import com.example.demo.api.MyDemoServer;
import com.example.demo.bean.MyDemoBean;
import com.example.demo.springBoot.BookBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyDemoController {

    @Autowired
    private BookBean bookBean;

    @Autowired
    private MyDemoServer myDemoServer;

    @RequestMapping(value = "/demo",produces = "text/plain;charset=UTF-8" ,method = RequestMethod.GET)
    String demo(String name){
        MyDemoBean myDemoBean = new MyDemoBean();
        myDemoBean.setName(name);
        String result = myDemoServer.selectDemo(myDemoBean);
        return "Hello Spring Boot SUCCESS! The BookName is "+bookBean.getName()+";and Book Author is "+bookBean.getAuthor()+";and Book price is "+bookBean.getPrice()+"result===="+result;
    }
}
