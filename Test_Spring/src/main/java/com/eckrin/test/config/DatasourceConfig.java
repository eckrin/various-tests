package com.eckrin.test.config;

import jakarta.persistence.EntityManagerFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Isolation;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement // Spring의 선언적 트랜잭션(@Transactional) 처리 기능 활성화. 해당 어노테이션을 사용하면 DataSourceTransactionManager로 구성되기 때문에 @Scheduled가 동작하지 않는 이슈 발생하므로 트랜잭션 매니저를 JpaTransactionManage로 구현
@MapperScan(
        basePackages = "com.eckrin.test",
        sqlSessionFactoryRef = "sqlSessionFactory"
//        sqlSessionTemplateRef = "sqlSessionTemplate"
)
@EnableJpaRepositories(
        basePackages = "com.eckrin.test"
) // JpaRepository 사용하기 위한 인터페이스 경로 명시
public class DatasourceConfig {

    @Value("${mybatis.mapper-locations}")
    String mPath;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    /**
     * Connection Pool을 지원하는 인터페이스
     */
    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }


    /**
     * SqlSessionFactory를 찍어내는 역할
     * Datasource를 참고하여 MyBatis와 Mysql 서버 연동
     */
    @Primary
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

    /**
     * SqlSession을 구현하고 코드에서 SqlSession을 대체하는 역할을 한다.
     */
    @Primary
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * LocalContainerEntityManagerFactoryBean
     * EntityManager를 생성하는 팩토리
     * SessionFactoryBean과 동일한 역할, Datasource와 mapper를 스캔할 .xml 경로를 지정하듯이
     * datasource와 엔티티가 저장된 폴더 경로를 매핑해주면 된다.
     */
    @Primary
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
                .build();
    }


//    // JPA 설정
//    @Primary
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
//            EntityManagerFactoryBuilder builder,
//            @Qualifier("dataSource") DataSource dataSource,
//            Environment env) {
//        Map<String, Object> properties = new HashMap<String, Object>();
//        // hbmddl.auto와 ddl-auto 차이? https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#data.sql.jpa-and-spring-data.creating-and-dropping
//        properties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty("spring.jpa.hibernate.ddl-auto"));
//        properties.put("hibernate.dialect", env.getRequiredProperty("spring.jpa.database-platform"));
//
//        return builder.dataSource(dataSource)
//                .packages("com.eckrin.test")
//                .properties(properties) // application.yml 미동작으로 직접 DatasourceConfig에 추가
////                .persistenceUnit("PERSISTENCE_SOCIAL_VR_MEMBER")
////                .mappingResources("META-INF/member-orm.xml")
//                .build();
//    }

    /**
     * EntityManagerFactory를 전달받아 JPA에서 트랜잭션을 관리
     */
    @Primary
    @Bean
    public JpaTransactionManager transactionManager(@Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        // Mybatis transactional
//        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
//        dataSourceTransactionManager.setDataSource(dataSource());

        // JPA transactional
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory.getObject());


        // Chaining transaction manager (jpa + mybatis)
//        return new ChainedTransactionManager(jpaTransactionManager, dataSourceTransactionManager);
        return jpaTransactionManager;
    }
}
