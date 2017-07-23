package com.akasoft.poneyrox.core.projections;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;

/**
 *  Projection du prix de sortie.
 */
public class ClosePriceProjection extends AbstractProjection {
    /**
     *  Constructeur.
     *  @param deepth Profondeur de la projection.
     *  @param series Série évaluée.
     */
    public ClosePriceProjection(int deepth, TimeSeries series) {
        super(deepth, new ClosePriceIndicator(series));
    }
}
