package com.akasoft.poneyrox.core.watch.rates;

import com.akasoft.poneyrox.core.watch.clusters.Cluster;

/**
 *  Taux.
 *  Classe métier représentative d'un taux (offre ou demande) ciblé par une cellule de courbe.
 */
public class AbstractRate {
    /**
     *  Type de taux.
     */
    private AbstractRateType type;

    /**
     *  Noeud embarquant la valeur minimale.
     */
    private Cluster minimum;

    /**
     *  Noeud embarquant la valeur moyenne.
     */
    private Cluster average;

    /**
     *  Noeud embarquant la valeur maximum.
     */
    private Cluster maximum;

    /**
     *  Constructeur.
     *  @param type Type de taux créé.
     *  @param minimum Valeur minimale.
     *  @param average Valeur moyenne.
     *  @param maximum Valeur maximale.
     */
    protected AbstractRate(AbstractRateType type, Cluster minimum, Cluster average, Cluster maximum) {
        this.type = type;
        this.minimum = minimum;
        this.average = average;
        this.maximum = maximum;
    }
}
