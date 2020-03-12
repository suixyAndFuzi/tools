package com.example.demo.elasticsearch

import org.elasticsearch.client.Client
import spock.lang.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * groovy 测试 + TestMe 组件 自动生成
 *
 * 如需关联数据库 许额外配置 @ContextConfiguration 注解
 */
class ElasticsearchClictTest extends Specification {
    @Mock
    Client client
    @Mock
    DemoElasticsearchUtil demoElasticsearchUtil
    @InjectMocks
    ElasticsearchClict elasticsearchClict

    def setup() {
        MockitoAnnotations.initMocks(this)
    }

    def "test save"() {
        when:
        elasticsearchClict.save()

        then:
        false//todo - validate something
    }

    def "test query"() {
        when:
        elasticsearchClict.query()

        then:
        false//todo - validate something
    }

    def "test delete"() {
        when:
        elasticsearchClict.delete()

        then:
        false//todo - validate something
    }

    def "test update"() {
        when:
        elasticsearchClict.update()

        then:
        false//todo - validate something
    }
}

