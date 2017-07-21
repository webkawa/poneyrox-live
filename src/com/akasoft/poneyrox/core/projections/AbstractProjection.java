package com.akasoft.poneyrox.core.projections;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Indicator;

/**
 *  Projection.
 *  Valeur ciblée en réponse à un ensemble d'indicateurs d'entrée traités par une machine.
 */
public abstract class AbstractProjection {
    /**
     *  Profondeur.
     *  Exprimée en nombre de cellules projetées (minimum 1).
     */
    private final int deepth;

    /**
     *  Indicateur ciblé.
     */
    private final Indicator<Decimal> indicator;

    /**
     *  Constructeur.
     *  @param deepth Profondeur de la projection.
     *  @param indicator Indicateur ciblé.
     */
    public AbstractProjection(int deepth, Indicator<Decimal> indicator) {
        this.deepth = deepth;
        this.indicator = indicator;
    }

    /**
     *  Retourne la profondeur.
     *  @return Profondeur.
     */
    public int getDeepth() {
        return this.deepth;
    }

    /**
     *  Retourne l'indicateur ciblé.
     *  @return Indicateur ciblé.
     */
    public Indicator<Decimal> getIndicator() {
        return this.indicator;
    }
}
