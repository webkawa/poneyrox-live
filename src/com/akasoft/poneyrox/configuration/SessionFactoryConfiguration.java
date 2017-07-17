package com.akasoft.poneyrox.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 *  Gestionnaire de sessions.
 */
@Configuration
public class SessionFactoryConfiguration extends LocalSessionFactoryBean {
    /**
     *  Constructeur.
     *  @param dataSource Source des données.
     */
    public SessionFactoryConfiguration(@Autowired DataSourceConfiguration dataSource) {
        /* Création des propriétés NHibernate */
        Properties properties = new Properties();
        properties.put("hibernate.default_schema", "public");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        properties.put("hibernate.show_sql", "false");
        properties.put("hibernate.hbm2ddl.auto", "create");

        /* Définition de l'objet */
        super.setDataSource(dataSource);
        super.setPackagesToScan("com.akasoft.poneyrox.entities");
        super.setHibernateProperties(properties);
    }
}
