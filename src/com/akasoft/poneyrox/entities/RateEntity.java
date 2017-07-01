package com.akasoft.poneyrox.entities;

import com.akasoft.poneyrox.exceptions.CurveException;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *  Taux.
 *  Entité représentative d'un taux relevé à une date donnée.
 */
@Entity
public class RateEntity extends AbstractEntity {
    /**
     *  Date du relevé.
     *  Exprimé en période UNIX.
     */
    private long time;

    /**
     *  Taux de l'offre.
     */
    private double bid;

    /**
     *  Taux de la demande.
     */
    private double ask;

    /**
     *  Marché de rattachement.
     */
    @ManyToOne
    private MarketEntity market;

    /**
     *  Retourne la date du relevé.
     *  @return Date du relevé.
     */
    public long getTime() {
        return this.time;
    }

    /**
     *  Retourne le taux de l'offre.
     *  @return Taux de l'offre.
     */
    public double getBid() {
        return this.bid;
    }

    /**
     *  Retourne le taux de la demande.
     *  @return Taux de la demande.
     */
    public double getAsk() {
        return this.ask;
    }

    /**
     *  Retourne le marché de rattachement.
     *  @return Marché de rattachement.
     */
    public MarketEntity getMarket() {
        return this.market;
    }

    /**
     *  Affecte la date du relevé.
     *  @param time Date du relevé.
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     *  Affecte le taux de la demande.
     *  @param bid Taux de la demande.
     */
    public void setBid(double bid) {
        this.bid = bid;
    }

    /**
     *  Affecte le taux de l'offre.
     *  @param ask Taux de l'offre.
     */
    public void setAsk(double ask) {
        this.ask = ask;
    }

    /**
     *  Affecte le marché de rattachement.
     *  @param market Marché de rattachement.
     */
    public void setMarket(MarketEntity market) {
        this.market = market;
    }
}
