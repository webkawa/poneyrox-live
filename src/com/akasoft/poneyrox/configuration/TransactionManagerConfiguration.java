package com.akasoft.poneyrox.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *  Configuration du gestionnaire de transactions.
 */
@Configuration
@EnableTransactionManagement
public class TransactionManagerConfiguration extends HibernateTransactionManager {
    /**
     *  Constructeur.
     *  @param sessionFactory Gestionnaire de sessions.
     */
    public TransactionManagerConfiguration(@Autowired SessionFactoryConfiguration sessionFactory) {
        super.setSessionFactory(sessionFactory.getObject());
    }
}
