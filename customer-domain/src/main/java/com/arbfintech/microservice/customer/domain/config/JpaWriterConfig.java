package com.arbfintech.microservice.customer.domain.config;


import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Wade He
 * @At 10/30/2019
 */

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef="entityManagerFactoryWriter",
        transactionManagerRef="transactionManagerWriter",
        basePackages= { "com.arbfintech.microservice.customer.domain.repository" }) //set repo location
public class JpaWriterConfig {

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    @Qualifier("pandaWriterDataSource")
    private DataSource writerDataSource;

    @Bean(name = "entityManagerWriter")
    @Primary
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryWriter(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactoryWriter")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryWriter (EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(writerDataSource)
                .properties(getVendorProperties(writerDataSource))
                .packages("com.arbfintech.microservice.customer.domain.entity") //entity location
                .persistenceUnit("WriterPersistenceUnit")
                .build();
    }

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }

    @Bean(name = "transactionManagerWriter")
    @Primary
    PlatformTransactionManager transactionManagerWriter(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryWriter(builder).getObject());
    }

}
