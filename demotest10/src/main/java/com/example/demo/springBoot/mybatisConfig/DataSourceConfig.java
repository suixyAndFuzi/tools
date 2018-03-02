package com.example.demo.springBoot.mybatisConfig;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {


    @Autowired
    private Environment env;


//    //DataSourceBuilder构建的仅仅是Spring JDBC的DataSource
//    @Bean(name = "ds1")
//    @ConfigurationProperties(prefix = "spring.datasource.titan-master") // application.properteis中对应属性的前缀
//    public DataSource dataSource1() {
//        return DataSourceBuilder.create().build();
//    }


    /**
     * 创建数据库连接池
     * destroy-method="close"的作用是当数据库连接不使用的时候,就把该连接重新放到数据池中,方便下次使用调用.
     *
     * @return
     */
    @Bean(name = "ds1", destroyMethod = "close")
    public DataSource dataSource1() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(env.getProperty("spring.datasource.db1.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.db1.username"));//用户名
        dataSource.setPassword(env.getProperty("spring.datasource.db1.password"));//密码
        dataSource.setDriverClassName(env.getProperty("spring.datasource.db1.driver-class-name"));
        dataSource.setInitialSize(2);//初始化时建立物理连接的个数
        dataSource.setMaxActive(20);//最大连接池数量
        dataSource.setMinIdle(0);//最小连接池数量
        dataSource.setMaxWait(60000);//获取连接时最大等待时间，单位毫秒。
        dataSource.setValidationQuery("SELECT 1");//用来检测连接是否有效的sql
        dataSource.setTestOnBorrow(false);//申请连接时执行validationQuery检测连接是否有效
        dataSource.setTestWhileIdle(true);//建议配置为true，不影响性能，并且保证安全性。
        dataSource.setPoolPreparedStatements(false);//是否缓存preparedStatement，也就是PSCache
        return dataSource;
    }


//    @Bean(name = "ds2")
//    @ConfigurationProperties(prefix = "spring.datasource.db2") // application.properteis中对应属性的前缀
//    public DataSource dataSource2() {
//        return DataSourceBuilder.create().build();
//    }

    @Bean(name = "ds2", destroyMethod = "close")
    public DataSource dataSource2() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(env.getProperty("spring.datasource.db2.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.db2.username"));//用户名
        dataSource.setPassword(env.getProperty("spring.datasource.db2.password"));//密码
        dataSource.setDriverClassName(env.getProperty("spring.datasource.db2.driver-class-name"));
        dataSource.setInitialSize(2);//初始化时建立物理连接的个数
        dataSource.setMaxActive(20);//最大连接池数量
        dataSource.setMinIdle(0);//最小连接池数量
        dataSource.setMaxWait(60000);//获取连接时最大等待时间，单位毫秒。
        dataSource.setValidationQuery("SELECT 1");//用来检测连接是否有效的sql
        dataSource.setTestOnBorrow(false);//申请连接时执行validationQuery检测连接是否有效
        dataSource.setTestWhileIdle(true);//建议配置为true，不影响性能，并且保证安全性。
        dataSource.setPoolPreparedStatements(false);//是否缓存preparedStatement，也就是PSCache
        return dataSource;
    }


}
