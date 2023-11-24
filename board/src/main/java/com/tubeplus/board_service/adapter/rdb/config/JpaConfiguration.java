package com.tubeplus.board_service.adapter.rdb.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class JpaConfiguration {
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("dataSource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean entityManagerFactory
                = new LocalContainerEntityManagerFactoryBean();

        entityManagerFactory.setDataSource(dataSource);

        entityManagerFactory.setPackagesToScan(
                "com.tubeplus.board_service.adapter.rdb.board",
                "com.tubeplus.board_service.adapter.rdb.posting"
        );

        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter());

        entityManagerFactory.setPersistenceUnitName("entityManager");

        return entityManagerFactory;

    }

    private JpaVendorAdapter jpaVendorAdapter() {

        HibernateJpaVendorAdapter hibernateJpaVendorAdapter
                = new HibernateJpaVendorAdapter();

        hibernateJpaVendorAdapter.setGenerateDdl(true);

        hibernateJpaVendorAdapter.setShowSql(true);

        return hibernateJpaVendorAdapter;
    }

    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory")
                                                         LocalContainerEntityManagerFactoryBean entityManagerFactory) {

        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();

        jpaTransactionManager.setEntityManagerFactory(
                entityManagerFactory.getObject()
        );

        return jpaTransactionManager;
    }
}
