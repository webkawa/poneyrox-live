package com.akasoft.poneyrox.entities;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 *  Taux unitaire.
 *  Entité descriptive d'un taux unitaire relevé dans une série.
 */
@MappedSuperclass
public class RateEntity extends AbstractEntity {
    /**
     *  Date d'ouverture du relevée.
     *  Exprimée en temps UNIX.
     */
    @Column(name = "rate_time")
    private long time;

    /**
     *  Relevé porteur.
     */
    @ManyToOne
    @JoinColumn(name = "rate_chart")
    private ChartEntity chart;

    /**
     *  Retourne la date d'ouverture.
     *  @return Date d'ouverture.
     */
    public long getTime() {
        return this.time;
    }

    /**
     *  Retourne le relevé porteur.
     *  @return Relevé porteur.
     */
    public ChartEntity getChart() {
        return this.chart;
    }

    /**
     *  Définit la date d'ouverture.
     *  @param time Date d'ouverture.
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     *  Définit le relevé porteur.
     *  @param chart Relevé porteur.
     */
    public void setChart(ChartEntity chart) {
        this.chart = chart;
    }

}
