package com.akasoft.poneyrox.entities;

import javax.persistence.*;

/**
 *  Taux OHLC.
 *  Entité représentative d'un taux unitaire de type OHLC (Open-High-Low-Close).
 */
@Entity
@NamedQueries(
        @NamedQuery(
                name = "OHLCRate.getByChart",
                query = "SELECT ohlc " +
                        "FROM OHLCRateEntity AS ohlc " +
                        "WHERE ohlc.chart = :chart " +
                        "ORDER BY ohlc.time ASC"
        )
)
public class OHLCRateEntity extends RateEntity {
    /**
     *  Taux à l'ouverture.
     */
    @Column(name = "ohlc_open")
    private double open;

    /**
     *  Taux haut.
     */
    @Column(name = "ohlc_high")
    private double high;

    /**
     *  Taux base.
     */
    @Column(name = "ohlc_low")
    private double low;

    /**
     *  Taux à la fermeture.
     */
    @Column(name = "ohlc_close")
    private double close;

    /**
     *  Retourne le taux à l'ouverture.
     *  @return Taux à l'ouverture.
     */
    public double getOpen() {
        return this.open;
    }

    /**
     *  Retourne le taux haut.
     *  @return Taux haut.
     */
    public double getHigh() {
        return this.high;
    }

    /**
     *  Retourne le taux bas.
     *  @return Taux bas.
     */
    public double getLow() {
        return this.low;
    }

    /**
     *  Retourne le taux à la fermeture.
     *  @return Taux à la fermeture.
     */
    public double getClose() {
        return this.close;
    }

    /**
     *  Définit le taux à l'ouverture.
     *  @param open Taux à l'ouverture.
     */
    public void setOpen(double open) {
        this.open = open;
    }

    /**
     *  Définit le taux haut.
     *  @param high Taux maximal.
     */
    public void setHigh(double high) {
        this.high = high;
    }

    /**
     *  Définit le taux minimal.
     *  @param low Taux minimal.
     */
    public void setLow(double low) {
        this.low = low;
    }

    /**
     *  Définit le taux à la fermeture.
     *  @param close Taux à la fermeture.
     */
    public void setClose(double close) {
        this.close = close;
    }
}
