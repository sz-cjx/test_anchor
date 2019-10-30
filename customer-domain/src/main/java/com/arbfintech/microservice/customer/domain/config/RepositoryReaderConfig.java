package com.arbfintech.microservice.customer.domain.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

/**
 * @author Wade He
 * @At 10/30/2019
 */

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef="entityManagerFactoryReader",
        transactionManagerRef="transactionManagerReader",
        basePackages= { "com.arbfintech.microservice.customer.domain.repositoryReader" }) //set repo location
public class RepositoryReaderConfig {

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    @Qualifier("pandaReaderDataSource")
    private DataSource readerDataSource;

    @Bean(name = "entityManagerReader")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryReader(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactoryReader")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryReader(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(readerDataSource)
                .properties(getVendorProperties(readerDataSource))
                .packages("com.arbfintech.microservice.customer.domain.entity") //entity location
                .persistenceUnit("ReaderPersistenceUnit")
                .build();
    }

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }

    @Bean(name = "transactionManagerReader")
    PlatformTransactionManager transactionManagerReader(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryReader(builder).getObject());
    }

}
