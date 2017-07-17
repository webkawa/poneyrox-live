package com.akasoft.poneyrox.dao;

import com.akasoft.poneyrox.entities.MarketEntity;
import com.akasoft.poneyrox.exceptions.DataException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.List;

/**
 *  DAO des marchés.
 */
@Repository
public class MarketDAO extends AbstractDAO {
    /**
     *  Constructeur.
     *  @param factory Gestionnaire de sessions.
     */
    public MarketDAO(@Autowired SessionFactory factory) {
        super(factory);
    }

    /**
     *  Extrait ou inscrit un marché dans la base.
     *  @param key Clef d'accès au marché.
     *  @return Marché trouvé ou généré.
     *  @throws DataException En cas de découverte de plusieurs cours.
     */
    public MarketEntity retrieveOrPersist(String key) throws DataException {
        MarketEntity pre = this.getByKey(key);
        if (pre == null) {
            MarketEntity rate = new MarketEntity();
            rate.setKey(key);

            super.getSession().persist(rate);
            return rate;
        } else {
            return pre;
        }
    }

    /**
     *  Retourne un marché par clef d'appartenance.
     *  @param key Clef d'accès au marché.
     *  @return Marché trouvé ou nul si non-existant.
     *  @throws DataException En cas de découverte de plusieurs marchés.
     */
    public MarketEntity getByKey(String key) throws DataException {
        List<MarketEntity> markets = super.getSession()
                .getNamedQuery("Market.getByKey")
                .setParameter("key", key)
                .list();

        /* Aucun résultat */
        if (markets.size() == 0) {
            return null;
        }

        /* Résultat unique */
        if (markets.size() == 1) {
            return markets.get(0);
        }

        /* Résultat multiples */
        throw new DataException("Multiple matches found for rate '%s'", key);
    }
}
