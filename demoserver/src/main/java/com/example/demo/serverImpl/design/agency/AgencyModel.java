package com.example.demo.serverImpl.design.agency;

import com.example.demo.serverImpl.design.BaseDesign;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 设计模式-代理
 *
 * @author fuzi
 */
public class AgencyModel extends BaseDesign implements InvocationHandler {
    /**
     * 被代理类对象 入参
     */
    private Object object;

    public AgencyModel(Object object) {
        this.object = object;
    }

    /**
     * 动态代理 xxx类统一处理方法业务
     *
     * @param proxy
     * @param method 方法名
     * @param args   入参
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Before invoke " + method.getName());
        method.invoke(object, args);
        return null;
    }
}

/**
 * 代理生成工具类
 */
class CreateProxyUtil extends BaseDesign {
    /**
     * 生成 被代理对象 接口实例 新实现类对象
     *
     * @param proxiedObject 被代理类对象
     * @return
     */
    public Object createProxy(Object proxiedObject) {
        //获取接口
        Class<?>[] interfaces = proxiedObject.getClass().getInterfaces();
        //获取代理对象
        AgencyModel handler = new AgencyModel(proxiedObject);
        //返回字节码从新编译过的 新代理对象
        return Proxy.newProxyInstance(proxiedObject.getClass().getClassLoader(), interfaces, handler);
    }

    /**
     * 测试方法
     *
     * @param args
     */
    public static void main(String[] args) {
        //创建 代理生成工具类对象
        CreateProxyUtil createProxy = new CreateProxyUtil();
        //获取 代理类对象
        InterfacesOne one = (InterfacesOne) createProxy.createProxy(new Hello());
        InterfacesTwo two = (InterfacesTwo) createProxy.createProxy(new Bye());
        //执行 被代理方法
        one.sayHello("123", "456");
        two.sayBye();
    }
}



