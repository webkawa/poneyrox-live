package com.akasoft.poneyrox.core.strategies;

import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.statistics.StandardDeviationIndicator;
import eu.verdelhan.ta4j.indicators.trackers.MACDIndicator;
import eu.verdelhan.ta4j.indicators.trackers.RSIIndicator;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandWidthIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsLowerIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsMiddleIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsUpperIndicator;

/**
 *  Stratégie simple d'exploitation du FOREX, basée sur les prix de cloture et composée des
 *  indicateurs suivants en entrée :
 *      - Bandes de Bollinger ;
 *      - MACD ;
 *      - Parabolic SAR ;
 *      - Stochastic ;
 *      - RSI.
 *  Voir : https://www.babypips.com/learn/forex/what-is-the-most-profitable-indicator
 */
public class SimpleForexStrategy extends AbstractStrategy {
    /**
     *  Constructeur.
     *  @param series Série étudiée.
     */
    public SimpleForexStrategy(TimeSeries series) {
        super(series);

        /* Récupération des prix de sortie */
        ClosePriceIndicator cpi = new ClosePriceIndicator(super.getSeries());
        super.addInput(cpi);

        /* Gestion des bandes de Bollinger
         * Voir : https://www.oanda.com/forex-trading/learn/technical-analysis-for-traders/bollinger-bands/parameters */
        SMAIndicator bollingerSMA = new SMAIndicator(cpi, 20);
        StandardDeviationIndicator bollingerSD = new StandardDeviationIndicator(cpi, 2);
        BollingerBandsMiddleIndicator bollingerMiddle = new BollingerBandsMiddleIndicator(bollingerSMA);
        BollingerBandsLowerIndicator bollingerLower = new BollingerBandsLowerIndicator(bollingerMiddle, bollingerSD);
        BollingerBandsUpperIndicator bollingerUpper = new BollingerBandsUpperIndicator(bollingerMiddle, bollingerSD);
        BollingerBandWidthIndicator bollinger = new BollingerBandWidthIndicator(bollingerUpper, bollingerMiddle, bollingerLower);
        super.addInput(bollinger);

        /* Gestion du MACD
         * Voir : http://www.investopedia.com/terms/m/macd.asp */
        MACDIndicator macd = new MACDIndicator(cpi, 12, 24);
        super.addInput(macd);

        /* Gestion du RSI
         * Voir : https://www.babypips.com/learn/forex/what-is-the-most-profitable-indicator */
        RSIIndicator rsi = new RSIIndicator(cpi, 9);
        super.addInput(rsi);
    }
}
