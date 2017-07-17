package com.akasoft.poneyrox.dao;

import com.akasoft.poneyrox.entities.ChartEntity;
import com.akasoft.poneyrox.entities.OHLCRateEntity;
import com.akasoft.poneyrox.exceptions.DataException;
import javafx.scene.chart.Chart;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  DAO des taux OHLC.
 */
@Repository
public class OHLCRateDAO extends AbstractDAO {
    /**
     *  Constructeur.
     *  @param factory Gestionnaire de sessions.
     */
    public OHLCRateDAO(@Autowired SessionFactory factory) {
        super(factory);
    }

    /**
     *  Intègre une liste de taux dans les relevés pré-existants.
     *  La fonction prend en paramètre la liste des dates de relevé ainsi que la liste des taux organisés dans l'ordre
     *  suivant : open, high, low, close
     *  @param times Liste des dates.
     *  @param rates Liste des taux.
     *  @param chart Relevé de rattachement.
     *  @return Liste des taux créés.
     *  @throws DataException En cas d'erreur lors du traitement.
     */
    public List<OHLCRateEntity> integrate(List<Long> times, List<Double[]> rates, ChartEntity chart) throws DataException {
        /* Vérification des entrées */
        if (times.size() != rates.size() || times.size() == 0) {
            throw new DataException("Invalid OHLC integration parameters");
        }

        /* Chargement des taux pré-existants */
        Map<Long, OHLCRateEntity> pre = this.getMapByChart(chart);
        List<OHLCRateEntity> result = new ArrayList<>();

        /* Récupération du point de comparaison */
        long offset = times.get(0);
        if (pre.size() > 0) {
            offset = pre.keySet().stream().mapToLong(r -> r).min().getAsLong();
        }

        /* Parcours */
        for (int i = 0; i < times.size(); i++) {
            /* Vérification de pré-existance */
            if (pre.containsKey(times.get(i))) {
                throw new DataException("OHLC rate already registered for market '%s' at time '%l'", chart.getMarket().getKey(), chart.getPeriod());
            }

            /* Vérification de décalage */
            if ((times.get(i) - offset) % chart.getPeriod() != 0) {
                throw new DataException("Invalid OHLC timing for market '%s' and time '%l'", chart.getMarket().getKey(), chart.getPeriod());
            }

            /* Intégration */
            OHLCRateEntity rate = new OHLCRateEntity();
            rate.setChart(chart);
            rate.setTime(times.get(i));
            rate.setOpen(rates.get(i)[0]);
            rate.setHigh(rates.get(i)[1]);
            rate.setLow(rates.get(i)[2]);
            rate.setClose(rates.get(i)[3]);

            super.getSession().persist(rate);

            pre.put(rate.getTime(), rate);
            result.add(rate);
        }

        /* Renvoi */
        return result;
    }

    /**
     *  Retourne la liste complète des taux OHLC rattachés à un relevé donné.
     *  @param chart Relevé recherché.
     *  @return Liste des taux OHLC.
     */
    public List<OHLCRateEntity> getByChart(ChartEntity chart) {
        return super.getSession()
                .getNamedQuery("OHLCRate.getByChart")
                .setParameter("chart", chart)
                .list();
    }

    /**
     *  Retourne la liste complète des taux OHLC rattachés à un relevé donné et organisés par date d'ouverture.
     *  @param chart Relevé recherché.
     *  @return Liste des taux classés par date de départ.
     */
    public Map<Long, OHLCRateEntity> getMapByChart(ChartEntity chart) {
        Map<Long, OHLCRateEntity> map = new HashMap<>();
        for (OHLCRateEntity rate : this.getByChart(chart)) {
            map.put(rate.getTime(), rate);
        }
        return map;
    }
}
