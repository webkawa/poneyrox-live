package com.akasoft.poneyrox.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 *  Relevé.
 *  Entité représentative d'un cours relevé sur un ensemble de périodes.
 */
@Entity
@NamedQueries(
        @NamedQuery(
                name = "Chart.getByPeriodAndMarket",
                query = "SELECT chart " +
                        "FROM ChartEntity AS chart " +
                        "WHERE chart.period = :period " +
                        "AND chart.market = :market"
        )
)
public class ChartEntity extends AbstractEntity {
    /**
     *  Période de temps couverte par les relevés unitaires.
     *  Exprimé en millisecondes.
     */
    @Column(name = "chart_period")
    private long period;

    /**
     *  Marché rattaché.
     */
    @ManyToOne
    @JoinColumn(name = "chart_market")
    private MarketEntity market;

    /**
     *  Relevés unitaires OHLC.
     */
    @OneToMany(mappedBy = "chart")
    private Set<OHLCRateEntity> ohlc;

    /**
     *  Constructeur.
     */
    public ChartEntity() {
        this.ohlc = new HashSet<>();
    }

    /**
     *  Retourne la période de temps couverte par les relevés.
     *  @return Période de temps couverte.
     */
    public long getPeriod() {
        return this.period;
    }

    /**
     *  Retourne le marché propriétaire.
     *  @return Marché propriétaire.
     */
    public MarketEntity getMarket() {
        return this.market;
    }

    /**
     *  Retourne la liste des relevés OHLC.
     *  @return Liste des relevés.
     */
    public Set<OHLCRateEntity> getOHLC() {
        return this.ohlc;
    }

    /**
     *  Définit la période de temps couverte par les relevés.
     *  @param period Période de temps couverte par les relevés.
     */
    public void setPeriod(long period) {
        this.period = period;
    }

    /**
     *  Définit le marché rattaché.
     *  @param market Marché propriétaire.
     */
    public void setMarket(MarketEntity market) {
        this.market = market;
    }
}
