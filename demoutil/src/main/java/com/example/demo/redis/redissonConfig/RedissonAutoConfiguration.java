package com.example.demo.redis.redissonConfig;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RedissonAutoConfiguration {

    @Autowired
    private RedissonProperties redssionProperties;

    /**
     * 单机模式自动装配
     *
     * @return
     */
   // @Bean
    public RedissonClient getRedisson() {
        Config config = new Config();
        //指定编码，默认编码为org.redisson.codec.JsonJacksonCodec
        //之前使用的spring-data-redis，用的客户端jedis，编码为org.springframework.data.redis.serializer.StringRedisSerializer
        //改用redisson后为了之间数据能兼容，这里修改编码为org.redisson.client.codec.StringCodec
        config.setCodec(new org.redisson.client.codec.StringCodec());
        //指定使用单节点部署方式
        config.useSingleServer()
                .setAddress(redssionProperties.getAddress())
                .setPassword(redssionProperties.getPassword())
                .setConnectionPoolSize(redssionProperties.getConnectionPoolSize())//设置对于master节点的连接池中连接数最大为500
                .setConnectTimeout(redssionProperties.getConnectTimeout())//同任何节点建立连接时的等待超时。时间单位是毫秒。
                .setTimeout(redssionProperties.getTimeout())//等待节点回复命令的时间。该时间从命令发送成功时开始计时。
                .setPingTimeout(redssionProperties.getConnectTimeout())
                .setReconnectionTimeout(redssionProperties.getTimeout())//当与某个节点的连接断开时，等待与其重新建立连接的时间间隔。时间单位是毫秒。
                .setConnectionMinimumIdleSize(redssionProperties.getConnectionMinimumIdleSize());

        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

}
