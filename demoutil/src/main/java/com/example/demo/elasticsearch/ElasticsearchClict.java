package com.example.demo.elasticsearch;

import com.example.demo.bean.MyDemoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElasticsearchClict {


    @Autowired
    private DemoElasticsearchUtil demoElasticsearchUtil;


    public void save() {
        MyDemoBean myDemoBean = new MyDemoBean();
        myDemoBean.setId(4L);
        myDemoBean.setName("xxxxx");
        MyDemoBean save = demoElasticsearchUtil.save(myDemoBean);
        System.out.println(save);
    }

    public void query() {
        MyDemoBean myDemoBean =demoElasticsearchUtil.findById(4L).orElse(null);
        System.out.println(myDemoBean);
    }


    public void delete() {
        demoElasticsearchUtil.deleteById(4L);
    }

    public void update() {
        MyDemoBean myDemoBean =demoElasticsearchUtil.findById(4L).orElse(null);
        myDemoBean.setAge("99");
        MyDemoBean save = demoElasticsearchUtil.save(myDemoBean);
        System.out.println(save);
    }

}
