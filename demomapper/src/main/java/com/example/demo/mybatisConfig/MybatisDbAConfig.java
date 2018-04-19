package com.example.demo.mybatisConfig;


import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 最关键的地方就是这块了，一层一层注入,先创建DataSource，在创建SqlSessionFactory在创建事务，最后包装到SqlSessionTemplate中。其中需要制定分库的mapper文件地址，以及分库到层代码
 * 这块的注解就是指明了扫描dao层，并且给dao层注入指定的SqlSessionTemplate。所有@Bean都需要按照命名指定正确。
 */
@Configuration
@MapperScan(basePackages = {"com.example.demo.dao"}, sqlSessionFactoryRef = "sqlSessionFactory1")
public class MybatisDbAConfig {

    /**
     * 这个时候就要用到@Qualifier注解了，qualifier的意思是合格者，通过这个标示，
     * 表明了哪个实现类才是我们所需要的，我们修改调用代码，添加@Qualifier注解，需要注意的是@Qualifier的参数名称必须为我们之前定义
     */
    @Autowired
    @Qualifier("ds1")
    private DataSource ds1;


    /**
     * 要创建工厂 bean,放置下面的代码在 Spring 的 XML 配置文件中:
     *
     * 属性
     SqlSessionFactory 有一个单独的必须属性,就是 JDBC 的 DataSource。这可以是任意 的 DataSource,其配置应该和其它 Spring 数据库连接是一样的。
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory1() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        // 使用titan数据源, 连接titan库
        factoryBean.setDataSource(ds1);
        //添加XML目录
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/ds/**/*.xml"));
        return factoryBean.getObject();
    }

    /**
     * Spring中使用DataSourceTransactionManager进行事务管理的配置
     * @return
     */
    @Bean
    public DataSourceTransactionManager transactionManager1() {
        return new DataSourceTransactionManager(ds1);
    }


    /**
     * SqlSessionTemplate是MyBatis-Spring的核心。这个类负责管理MyBatis的SqlSession,调用MyBatis的SQL方法，翻译异常。
     * SqlSessionTemplate是线程安全的，可以被多个DAO所共享使用。
     当调用SQL方法时，包含从映射器getMapper()方法返回的方法，SqlSessionTemplate将会保证使用的SqlSession是和当前Spring的事务相关的。此外，它管理session的生命周期，包含必要的关闭，提交或回滚操作。
     SqlSessionTemplate实现了SqlSession，这就是说要对MyBatis的SqlSession进行简易替换。
     SqlSessionTemplate通常是被用来替代默认的MyBatis实现的DefaultSqlSession，因为它不能参与到Spring的事务中也不能被注入，因为它是线程不安全的。相同应用程序中两个类之间的转换可能会引起数据一致性的问题。
     SqlSessionTemplate对象可以使用SqlSessionFactory作为构造方法的参数来创建
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionTemplate sqlSessionTemplate1() throws Exception {
        // 使用上面配置的Factory
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory1());
        return template;
    }
}
