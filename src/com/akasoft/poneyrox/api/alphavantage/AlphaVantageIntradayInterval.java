package com.akasoft.poneyrox.api.alphavantage;

/**
 *  Niveau de granularité passé à l'API AlphaVantage lors d'un appel intra-journalier.
 */
public enum AlphaVantageIntradayInterval {
    /**
     *  Découpage à la minute.
     */
    MINUTES_1,

    /**
     *  Découpage par bloc de 5 minutes.
     */
    MINUTES_5,

    /**
     *  Découpage par bloc de 15 minutes.
     */
    MINUTES_15,

    /**
     *  Découpage par bloc de 30 minutes.
     */
    MINUTES_30,

    /**
     *  Découpage par bloc de 60 minutes.
     */
    MINUTES_60
}
