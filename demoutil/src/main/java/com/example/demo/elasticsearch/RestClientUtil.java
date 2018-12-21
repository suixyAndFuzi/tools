package com.example.demo.elasticsearch;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

/**
 * HTTP请求 es 直接使用 9200 restful 接口即可 ，若要使用 9300 java Api端口 需要继承ElasticsearchRepository 接口注入
 */

@Service
public class RestClientUtil {


    public final RestClient restClient = RestClient.builder(new HttpHost("10.1.1.94", 9200, "http")).build();

    public void search() {
        Map<String, String> params = Collections.emptyMap();

        //检索条件
        String queryString = "{\n" +
                "  \"query\": {\n" +
                "    \"match_phrase\": {\n" +
                "      \"auditDoctorId\": \"wangmm\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        HttpEntity entity = new NStringEntity(queryString, ContentType.APPLICATION_JSON);

        try {
            //索引 开头要是使用 / 则需要转译为 //
            Response response = restClient.performRequest("POST", "//sf_ipt_result_201812/_search", params, entity);
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println(responseBody);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
