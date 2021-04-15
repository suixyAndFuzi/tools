package com.example.demo.serverImpl.design.agency;

/**
 * 代理模式 被代理 实现类
 */
class Hello implements InterfacesOne {
    @Override
    public void sayHello(String str1, String str2) {
        System.out.println("Hello fuzi!" + str1 + str2);
    }
}

class Bye implements InterfacesTwo {
    @Override
    public void sayBye() {
        System.out.println("Bye fuzi!");
    }
}

