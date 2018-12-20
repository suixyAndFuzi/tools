package com.example.demo.elasticsearch;

import com.example.demo.bean.MyDemoBean;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;


@Component
public interface DemoElasticsearchUtil extends ElasticsearchRepository<MyDemoBean,Long> {

}
