/*
 * Copyright © 2015-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.zx.demo.thread.config.database;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * 扩展数据库.
 *
 * @author zhangxin
 * @since 0.0.1
 */
@Configuration
@MapperScan(basePackages = { "me.zx.demo.thread.mapper.extend"}, sqlSessionTemplateRef = "extendSqlSessionTemplate")
public class ExtendDatabaseConfig {

    /**
     * 默认的核心数据源.
     * @return DataSource
     */
    @Bean(name = "extendDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.extend")
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * MyBatis SQL 会话工厂.
     * @param dataSource dataSource
     * @return SqlSessionFactory
     * @throws Exception Exception
     */
    @Bean(name = "extendSqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("extendDataSource") final DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        //        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/main/*.xml"));
        return bean.getObject();
    }

    /**
     * MyBatis 事务管理.
     * @param dataSource dataSource
     * @return DataSourceTransactionManager
     */
    @Bean(name = "extendTransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("extendDataSource") final DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * SQL会话模板..
     * @param sqlSessionFactory sqlSessionFactory
     * @return SqlSessionTemplate
     * @throws Exception Exception
     */
    @Bean(name = "extendSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(
            @Qualifier("extendSqlSessionFactory") final SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
