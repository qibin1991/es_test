package com.mybatisPlus;

/**
 * @ClassName MybatisPlusConfig
 * @Description TODO
 * @Author QiBin
 * @Date 2022/8/29 15:10
 * @Version 1.0
 **/

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
public class MybatisPlusConfig {

//    /**
//     * 分页插件
//     */
//    @Bean
//    public PaginationInterceptor paginationInterceptor() {
//        return new PaginationInterceptor();
//    }

    /**
     * 注册插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件
        PaginationInnerInterceptor pageInterceptor = new PaginationInnerInterceptor();
        // 设置请求的页面大于最大页后操作，true调回到首页，false继续请求。默认false
        pageInterceptor.setOverflow(false);
        // 单页分页条数限制，默认无限制
//        pageInterceptor.setMaxLimit(500L);
        // 设置数据库类型
        pageInterceptor.setDbType(DbType.MYSQL);

        interceptor.addInnerInterceptor(pageInterceptor);
        return interceptor;
    }

    @Primary
    @Bean("sqlSessionFactory")
    public SqlSessionFactory db1SqlSessionFactory(DataSource dataSource) throws Exception {
        /**
         * 使用 mybatis plus 配置
         */
        MybatisSqlSessionFactoryBean b1 = new MybatisSqlSessionFactoryBean();
        System.out.println("dataSourceLyz" + dataSource.toString());
        b1.setDataSource(dataSource);
        b1.setPlugins(mybatisPlusInterceptor());
        b1.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        return b1.getObject();
    }


    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource datasource)
            throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(datasource);
        bean.setPlugins(mybatisPlusInterceptor());
        bean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        return bean.getObject();
    }

}
