package com.akasoft.poneyrox.configuration;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 *  Source des données.
 */
@Configuration
public class DataSourceConfiguration extends BasicDataSource {
    /**
     *  Constructeur.
     */
    public DataSourceConfiguration() {
        super.setDriverClassName("org.postgresql.Driver");
        super.setUrl("jdbc:postgresql://localhost/btc");
        super.setUsername("kawa");
        super.setPassword("gdmfsob256!");
    }
}
