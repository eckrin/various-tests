package com.eckrin.test.config;

import jakarta.persistence.EntityManagerFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
//@MapperScan(
//        basePackages = "com.eckrin.test",
//        sqlSessionFactoryRef = "sqlSessionFactory",
//        sqlSessionTemplateRef = "sqlSessionTemplate"
//)
@EnableJpaRepositories(
        basePackages = "com.eckrin.test"
)
public class DatasourceConfig {

    @Value("${mybatis.mapper-locations}")
    String mPath;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    // Database 정보 설정
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    // JPA 설정
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("dataSource") DataSource dataSource,
            Environment env) {
        Map<String, Object> properties = new HashMap<String, Object>();
        // hbmddl.auto와 ddl-auto 차이? https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#data.sql.jpa-and-spring-data.creating-and-dropping
        properties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty("spring.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect", env.getRequiredProperty("spring.jpa.database-platform"));

        return builder.dataSource(dataSource)
                .packages("com.eckrin.test")
                .properties(properties) // application.yml 미동작으로 직접 DatasourceConfig에 추가
//                .persistenceUnit("PERSISTENCE_SOCIAL_VR_MEMBER")
//                .mappingResources("META-INF/member-orm.xml")
                .build();
    }

    // Mybatis 설정
    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource DataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(DataSource);
        // mybatis 설정 파일 세팅
        sqlSessionFactoryBean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/mybatis-config.xml"));
        // mapper.xml 패키지 주소
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mybatis/mapper/**/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        // Mybatis transactional
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource());

        // JPA transactional
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);

        // Chaining transaction manager (mybatis + jpa)
        return new ChainedTransactionManager(jpaTransactionManager, dataSourceTransactionManager);
    }
}
