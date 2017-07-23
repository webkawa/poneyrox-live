package com.akasoft.poneyrox.core.projections;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.simple.DifferenceIndicator;
import eu.verdelhan.ta4j.indicators.simple.PreviousPriceIndicator;
import eu.verdelhan.ta4j.indicators.simple.PriceVariationIndicator;

/**
 *  Projection de la variation du prix de fermeture.
 */
public class PriceVariationProjection extends AbstractProjection {
    /**
     *  Constructeur.
     *  @param deepth Profondeur de la projection.
     *  @param series Série évaluée.
     */
    public PriceVariationProjection(int deepth, TimeSeries series) {
        super(deepth, new PriceVariationIndicator(series));
    }
}
