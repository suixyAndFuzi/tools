package com.example.demo.serverImpl;


import com.example.demo.api.MyDemoServer;
import com.example.demo.bean.MyDemoBean;
import com.example.demo.dao.MyDemoMapper;
import com.example.demo.daoFull.MyDemoFullMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyDemoServerImpl implements MyDemoServer {
    @Autowired
    private MyDemoMapper myDemoMapper;
    @Autowired
    private MyDemoFullMapper myDemoFullMapper;

    @Override
    public String selectDemo(MyDemoBean myDemoBean) {
        String r = myDemoMapper.selectByUser(myDemoBean.getName());
        System.out.println(r);
        String rFull = myDemoFullMapper.selectByUser(myDemoBean.getName());
        System.out.println(rFull);
        return  r;
    }


}
