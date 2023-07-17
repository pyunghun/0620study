package com.ohgiraffers.mybatisspring.configuration;

import com.ohgiraffers.mybatisspring.section01.factorybean.MenuMapper;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfiguration {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.jdbc-url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean(destroyMethod = "close")
    public HikariDataSource dataSource() {
//        DataSource : javax.sql 패키지에서 제공하는 인터페이스
//        BasicDataSource : commons-dbcp 모듈에서 제공하는 DataSource 구현체
//        HikariDataSource : hikari-cp 모듈에서 제공하는 DataSource 구현체
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        dataSource.setConnectionTimeout(30000); // 30초 이상의 시간이 지나면 오류 발생
        dataSource.setIdleTimeout(60000);
        dataSource.setMaxLifetime(1800000);

        dataSource.setMaximumPoolSize(50);

        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.addMapper(MenuMapper.class); // mapper 등록

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setConfiguration(configuration);

        return factoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate() throws Exception {

        return new SqlSessionTemplate(sqlSessionFactory());
    }
    // 데이터베이스 설정에 관련 된 마이바티스 설정
}
