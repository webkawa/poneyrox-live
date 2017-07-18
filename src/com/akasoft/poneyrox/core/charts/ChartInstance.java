package com.akasoft.poneyrox.core.charts;

import com.akasoft.poneyrox.core.indicators.AbstractIndicatorInstance;
import com.akasoft.poneyrox.entities.ChartEntity;
import com.akasoft.poneyrox.entities.OHLCRateEntity;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Relevé.
 *  Objet métier représentatif d'un relevé de taux monté en mémoire et incluant plusieurs indicateurs.
 */
public class ChartInstance {
    /**
     *  Inscription en base.
     */
    private final ChartEntity entity;

    /**
     *  Liste des taux et indicateurs inclus.
     */
    private final Map<Long, RateInstance> rates;

    /**
     *  Séries consolidées pour la génération des indicateurs.
     */
    private final TimeSeries series;

    /**
     *  Liste des indicateurs disponibles.
     */
    private final List<AbstractIndicatorInstance> indicators;

    /**
     *  Constructeur.
     *  @param entity Inscription en base.
     */
    public ChartInstance(ChartEntity entity, List<OHLCRateEntity> source) {
        /* Paramètres */
        this.entity = entity;
        this.rates = new HashMap<>();

        /* Création de la série */
        List<Tick> ticks = new ArrayList<>();
        for (OHLCRateEntity rate : source) {
            Tick tick = new Tick(
                    new DateTime(rate.getTime()),
                    rate.getOpen(),
                    rate.getHigh(),
                    rate.getLow(),
                    rate.getClose(),
                    0);
            ticks.add(tick);
        }
        this.series = new TimeSeries("main", ticks);

        /* Création des indicateurs */
        this.indicators = new ArrayList<>();
    }

    /**
     *  Retourne le taux rattaché à une date d'ouverture passée en paramètre.
     *  @param time Date du taux.
     *  @return Taux correspondant.
     */
    public RateInstance getRate(Long time) {
        return this.rates.get(time);
    }

    /**
     *  Retourne la série rattachée au relevé.
     *  @return Série rattachée.
     */
    public TimeSeries getSeries() {
        return this.series;
    }

    /**
     *  Retourne les valeurs
     * @return
     */
    public ClosePriceIndicator getClosePriceIndicator() {
        return new ClosePriceIndicator(this.series);
    }

    /**
     *  Enregistre un taux non-consolidé dans la liste.
     *  @param time Date du taux.
     *  @param ohcl Taux OHCL.
     */
    public void putRate(Long time, OHLCRateEntity ohcl) {
        RateInstance rate = new RateInstance(this, time, ohcl);
        this.rates.put(time, rate);
    }


}
