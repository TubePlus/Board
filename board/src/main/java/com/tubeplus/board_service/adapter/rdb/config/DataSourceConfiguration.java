package com.tubeplus.board_service.adapter.rdb.config;


import com.google.common.collect.ImmutableMap;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.Map;

@Slf4j

@Configuration
public class DataSourceConfiguration {
    public static final String MASTER_DATASOURCE = "masterDataSource";
    public static final String SLAVE_DATASOURCE = "slaveDataSource";

    @Bean(MASTER_DATASOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.master.hikari")
    public DataSource masterDataSource() {
        log.info(String.format("\n\nmasterDataSource() called\n\n"));
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(SLAVE_DATASOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.slave.hikari")
    public DataSource slaveDataSource() {
        log.info(String.format("\n\nslaveDataSource() called\n\n"));
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean("routingDataSource")
    public DataSource routingDataSource(
            @Qualifier(MASTER_DATASOURCE) DataSource masterDataSource,
            @Qualifier(SLAVE_DATASOURCE) DataSource slaveDataSource) {

        log.info("\n\nroutingDataSource() called\n\n");
        RoutingDataSource routingDataSource = new RoutingDataSource();

        Map<Object, Object> datasourceMap = ImmutableMap.<Object, Object>builder()
                .put("master", masterDataSource)
                .put("slave", slaveDataSource)
                .build();

        routingDataSource.setTargetDataSources(datasourceMap);

        routingDataSource.setDefaultTargetDataSource(masterDataSource);

        return routingDataSource;
    }

    @Primary
    @Bean
    public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
        // 지연 연결 제공 -> 데이터베이스 연결의 지연 실행, 필요한 시점에서만 연결
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

}