package com.example.demo.serverImpl.Factory;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class MyFactoryProxy implements ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<Integer, FactoryService> map = new HashMap<>();

    public void getDemo(Integer type){
        FactoryService factoryService = getFactoryService(type);
        //调用子类方法
        factoryService.demo();
        //调用基类方法
        factoryService.test();
    }

    /**
     * 获取实现类
     * @param type
     * @return
     */
    private FactoryService getFactoryService(Integer type) {
        FactoryService factoryService = map.get(type);
        if (factoryService == null) {
            logger.warn("无效 Type= {}", type);
        }
        return factoryService;
    }

    /**
     * 添加工厂实现类
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        String[] beanNames = applicationContext.getBeanNamesForType(FactoryService.class);
        logger.info("添加授权服务工厂类，beanNames = {}", System.out.printf(beanNames.toString()));
        for (String beanName : beanNames) {
            FactoryService factoryService = applicationContext.getBean(beanName, FactoryService.class);
            map.put(factoryService.getType(), factoryService);
        }
        logger.info("添加授权服务工厂类，map = {}", System.out.printf(map.toString()));
    }
}
