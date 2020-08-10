package com.example.demo.elasticsearch;

import com.example.demo.bean.MyDemoBean;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 若要使用 9300 java Api端口 需要继承ElasticsearchRepository 接口注入
 */
@Service
public class ElasticsearchClict {

    protected Client client;
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
        MyDemoBean myDemoBean = demoElasticsearchUtil.findById(4L).orElse(null);
        System.out.println(myDemoBean);
    }


    public void delete() {
        demoElasticsearchUtil.deleteById(4L);
    }

    public void update() {
        MyDemoBean myDemoBean = demoElasticsearchUtil.findById(4L).orElse(null);
        myDemoBean.setAge("99");
        MyDemoBean save = demoElasticsearchUtil.save(myDemoBean);
        System.out.println(save);
    }


    //查询api
    private void apiQuery() throws Exception {
        List<String> list = Arrays.asList();
        //查询ES must and 条件
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        for (String locationId : list) {
            //should 或条件
            query.should(QueryBuilders.wildcardQuery("Id", locationId + "*")).must();
        }
        BoolQueryBuilder allBuidler1 = QueryBuilders.boolQuery();
        //精确查询
        allBuidler1.must(QueryBuilders.termQuery("uid", 1));
        allBuidler1.must(QueryBuilders.termQuery("delete", false));
        //正则查询查询
        allBuidler1.must(QueryBuilders.wildcardQuery("block", "*" + "123" + "*"));
        //分词 短语查询
        allBuidler1.must(QueryBuilders.matchPhraseQuery("block", "123"));
        allBuidler1.must(query);
        SearchRequestBuilder srb = this.client.prepareSearch("index").setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(query);
        //分页
        srb = srb.setFrom(0).setSize(10);
        MultiSearchResponse.Item[] items = this.client.prepareMultiSearch().add(srb).execute().actionGet().getResponses();


        //组装返回json数据
        for (MultiSearchResponse.Item item : items) {
            if (item.isFailure()) {
                //失败了默认不过滤
                throw new Exception("es查询失败");
            }

            SearchHits searchHits = item.getResponse().getHits();
            if (null == searchHits || null == searchHits.getHits() || searchHits.getHits().length == 0) {
                continue;
            }

            for (SearchHit hit : searchHits.getHits()) {
                Map<String, Object> map = hit.getSource();
                hit.getId();
                map.get("Id");
            }
        }
    }

}


