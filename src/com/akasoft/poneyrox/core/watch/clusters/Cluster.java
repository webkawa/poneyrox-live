package com.akasoft.poneyrox.core.watch.clusters;

import java.util.Objects;

/**
 *  Noeud.
 *  Valeur unitaire rattaché à une typologie d'indicateur (minimum, moyenne, maximum) pour
 *  un taux (offre ou demande) sur une courbe donnée.
 */
public class Cluster {
    /**
     *  Type du noeud.
     */
    private final ClusterType type;

    /**
     *  Valeur du noeud.
     */
    private final double value;

    /**
     *  Valeur brute de croissance (ou décroissance) du noeud comparativement au précédent.
     */
    private double rawMargin;

    /**
     *  Pourcentage de croissance (ou décroissance) du noeud comparativement au précédent.
     */
    private double relativeMargin;

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
        this.rawMargin = 0;
        this.relativeMargin = 0;
        this.top = false;
        this.bottom = false;
    }

    /**
     *  Retourne le type du noeud.
     *  @return Type du noeud.
     */
    public ClusterType getType() {
        return this.type;
    }

    /**
     *  Retourne la valeur rattachée au noeud.
     *  @return Valeur rattachée.
     */
    public double getValue() {
        return this.value;
    }

    /**
     *  Retourne la marge avec le noeud précédent.
     *  @return Marge brute.
     */
    public double getRawMargin() {
        return this.rawMargin;
    }

    /**
     *  Retourne le pourcentage de marge avec le noeud précédent.
     *  @return Pourcentage de marge.
     */
    public double getRelativeMargin() {
        return this.relativeMargin;
    }

    /**
     *  Indique si le noeud constitue un sommet de la courbe.
     *  @return true si le noeud constitue un sommet.
     */
    public boolean isTop() {
        return this.top;
    }

    /**
     *  Indique si le noeud constitue un repli de la courbe.
     *  @return true si le noeud constitue un repli.
     */
    public boolean isBottom() {
        return this.bottom;
    }

    /**
     *  Définit la marge brute et relative comparativement au noeud précédent.
     *  @param raw Marge brute.
     */
    public void setMargin(double raw) {
        this.rawMargin = raw;
        this.relativeMargin = (this.rawMargin / this.value) * 100;
    }

    /**
     *  Retourne la clef de hachage rattachée au noeud.
     *  @return Clef de hachage.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                this.type,
                this.value);
    }
}
