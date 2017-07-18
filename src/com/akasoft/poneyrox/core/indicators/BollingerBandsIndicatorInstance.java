package com.akasoft.poneyrox.core.indicators;

import com.akasoft.poneyrox.core.charts.ChartInstance;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsLowerIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsMiddleIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsUpperIndicator;
import org.joda.time.DateTime;

/**
 *  Bandes de Bollinger.
 */
public class BollingerBandsIndicatorInstance extends AbstractIndicatorInstance {
    /**
     *  Indicateur bas.
     */
    private BollingerBandsLowerIndicator lower;

    /**
     *  Indicateur centre.
     */
    private BollingerBandsMiddleIndicator middle;

    /**
     *  Indicateur haut.
     */
    private BollingerBandsUpperIndicator upper;


    public BollingerBandsIndicatorInstance(ChartInstance owner) {
        super(owner);
        this.middle = new BollingerBandsMiddleIndicator(new ClosePriceIndicator(owner.getSeries()));
        this.lower = new BollingerBandsLowerIndicator(this.middle, new ClosePriceIndicator(owner.getSeries()));
        this.upper = new BollingerBandsUpperIndicator(this.middle, new ClosePriceIndicator(owner.getSeries()));
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    protected boolean hasArtifact(DateTime time) {
        return false;
    }

    @Override
    protected double[] getArtifact(DateTime time) {
        new BollingerBandsMiddleIndicator();

        return new double[0];
    }
}
