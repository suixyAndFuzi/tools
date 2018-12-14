//package com.example.demo.interfaces;
//
//
//
//import com.example.demo.DemoApplication;
//import com.example.demo.api.MyDemoServer;
//import com.example.demo.bean.MyDemoBean;
//import com.example.demo.springBoot.BookBean;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = DemoApplication.class)
//@WebAppConfiguration
//public class DemoServicesTest {
//
//    @Autowired
//    private BookBean bookBean;
//
//    @Autowired
//    private MyDemoServer myDemoServer;
//
//    @Test
//    public  void  demo(){
//        MyDemoBean myDemoBean = new MyDemoBean();
//        myDemoBean.setName("123");
//        String result = myDemoServer.selectDemo(myDemoBean);
//        System.out.println("Hello Spring Boot! The BookName is "+bookBean.getName()+";and Book Author is "+bookBean.getAuthor()+";and Book price is "+bookBean.getPrice()+"result===="+result);
//    }
//}
