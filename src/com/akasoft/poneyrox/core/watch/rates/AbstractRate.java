package com.akasoft.poneyrox.core.watch.rates;

import com.akasoft.poneyrox.core.watch.clusters.Cluster;
import com.akasoft.poneyrox.core.watch.clusters.ClusterType;
import com.akasoft.poneyrox.exceptions.CurveException;

import java.util.Objects;

/**
 *  Taux.
 *  Classe métier représentative d'un taux (offre ou demande) ciblé par une cellule de courbe.
 */
public class AbstractRate {
    /**
     *  Type de taux.
     */
    private final AbstractRateType type;

    /**
     *  Noeud embarquant la valeur minimale.
     */
    private final Cluster minimum;

    /**
     *  Noeud embarquant la valeur moyenne.
     */
    private final Cluster average;

    /**
     *  Noeud embarquant la valeur maximum.
     */
    private final Cluster maximum;

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

    /**
     *  Retourne le type de taux créé.
     *  @return Type de taux.
     */
    public AbstractRateType getType() {
        return this.type;
    }

    /**
     *  Retourne le noeud minimum.
     *  @return Noeud minimum.
     */
    public Cluster getMinimum() {
        return this.minimum;
    }

    /**
     *  Retourne le noeud moyen.
     *  @return Noeud moyen.
     */
    public Cluster getAverage() {
        return this.average;
    }

    /**
     *  Retourne le noeud maximum.
     *  @return Noeud maximum.
     */
    public Cluster getMaximum() {
        return this.maximum;
    }

    /**
     *  Retourne un noeud rattaché au taux par type.
     *  @param type Type de noeud.
     *  @return Noeud correspondant.
     *  @throws CurveException En cas de type invalide.
     */
    public Cluster getClusterByType(ClusterType type) throws CurveException {
        switch (type) {
            case MINIMUM:
                return this.minimum;
            case AVERAGE:
                return this.average;
            case MAXIMUM:
                return this.maximum;
        }
        throw new CurveException("Invalid cluster type %s", type);
    }

    /**
     *  Réalise le calcul des marges en fonction du taux précédent.
     *  @param previous Taux précédent.
     */
    public void computeMargins(AbstractRate previous) {
        this.minimum.setMargin(previous.getMinimum().getValue() - this.minimum.getValue());
        this.average.setMargin(previous.getAverage().getValue() - this.average.getValue());
        this.maximum.setMargin(previous.getMaximum().getValue() - this.maximum.getValue());
    }

    public void computeOppositions()

    /**
     *  Retourne la clef de hachage rattachée au taux.
     *  @return Clef de hachage.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                this.type,
                this.minimum,
                this.average,
                this.maximum);
    }
}
