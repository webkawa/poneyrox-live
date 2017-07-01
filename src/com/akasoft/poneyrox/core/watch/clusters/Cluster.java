package com.akasoft.poneyrox.core.watch.clusters;

/**
 *  Noeud.
 *  Valeur unitaire rattaché à une typologie d'indicateur (minimum, moyenne, maximum) pour
 *  un taux (offre ou demande) sur une courbe donnée.
 */
public class Cluster {
    /**
     *  Type du noeud.
     */
    private ClusterType type;

    /**
     *  Valeur du noeud.
     */
    private double value;

    /**
     *  Taux de croissance (ou de décroissance) comparativement à l'élément précédent.
     */
    private double margin;

    /**
     *  Indique si le noeud constitue un sommet sur la courbe.
     */
    private boolean top;

    /**
     *  Indique si le noeud constitue un repli sur la courbe.
     */
    private boolean bottom;

    /**
     *  Constructeur.
     *  Au départ et à défaut d'analyse comparative, le noeud est considéré comme ne constituant ni un sommet, ni
     *  un repli de la courbe, et possède une marge nulle.
     *  @param type Type du noeud.
     *  @param value Valeur affectée.
     */
    public Cluster(ClusterType type, double value) {
        this.type = type;
        this.value = value;
        this.margin = 0;
        this.top = false;
        this.bottom = false;
    }
}
