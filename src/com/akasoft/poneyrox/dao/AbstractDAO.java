package com.akasoft.poneyrox.dao;

import com.akasoft.poneyrox.configuration.SessionFactoryConfiguration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 *  DAO.
 *  Classe abstraite représentative d'un répertoire d'accès aux données.
 */
@Service
@Transactional
public abstract class AbstractDAO {
    /**
     *  Gestionnaire de sessions.
     */
    private SessionFactory factory;

    /**
     *  Constructeur.
     *  @param factory Gestionnaire de sessions.
     */
    public AbstractDAO(SessionFactory factory) {
        this.factory = factory;
    }

    /**
     *  Retourne la session courante.
     *  @return Session courante.
     */
    protected Session getSession() {
        return this.factory.getCurrentSession();
    }
}
