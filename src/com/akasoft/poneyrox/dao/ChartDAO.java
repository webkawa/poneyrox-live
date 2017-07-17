package com.akasoft.poneyrox.dao;

import com.akasoft.poneyrox.entities.ChartEntity;
import com.akasoft.poneyrox.entities.MarketEntity;
import com.akasoft.poneyrox.exceptions.DataException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.List;

/**
 *  DAO des relevés.
 */
@Repository
public class ChartDAO extends AbstractDAO {
    /**
     *  Constructeur.
     *  @param factory Gestionnaire de sessions.
     */
    public ChartDAO(@Autowired SessionFactory factory) {
        super(factory);
    }

    /**
     *  Extrait ou inscrit un relevé dans la base.
     *  @param period Période traitée.
     *  @param market Marché de rattachement.
     *  @return Relevé trouvé ou généré.
     *  @throws DataException En cas de découverte de résultats multiples.
     */
    public ChartEntity retrieveOrPersist(long period, MarketEntity market) throws DataException {
        ChartEntity pre = this.getByPeriodAndRate(period, market);
        if (pre == null) {
            ChartEntity chart = new ChartEntity();
            chart.setPeriod(period);
            chart.setMarket(market);

            super.getSession().persist(chart);
            return chart;
        } else {
            return pre;
        }
    }

    /**
     *  Retourne un relevé par périodicité et marché d'appartenance.
     *  @param period Période traitée.
     *  @param market Marché d'appartenance.
     *  @return Relevé trouvé ou nul.
     *  @throws DataException En cas de résultats multiples.
     */
    public ChartEntity getByPeriodAndRate(long period, MarketEntity market) throws DataException {
        List<ChartEntity> charts = super.getSession()
                .getNamedQuery("Chart.getByPeriodAndMarket")
                .setParameter("period", period)
                .setParameter("market", market)
                .list();

        /* Aucun résultat */
        if (charts.size() == 0) {
            return null;
        }

        /* Résultat unique */
        if (charts.size() == 1) {
            return charts.get(0);
        }

        /* Résultats multiples */
        throw new DataException("Multiple charts found for market '%s' and period '%d'", market.getKey(), period);
    }
}
