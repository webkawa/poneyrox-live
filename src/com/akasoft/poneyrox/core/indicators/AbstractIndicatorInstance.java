package com.akasoft.poneyrox.core.indicators;

import com.akasoft.poneyrox.core.charts.ChartInstance;
import org.joda.time.DateTime;

/**
 *  Indicateur.
 *  Indicateur individuel susceptible d'etre appliqué à un relevé de taux et réutilisé dans le cadre
 *  du processus sélectif.
 */
public abstract class AbstractIndicatorInstance {
    /**
     *  Relevé porteur.
     */
    private ChartInstance owner;

    /**
     *  Constructeur.
     *  @param owner Relevé propriétaire.
     */
    public AbstractIndicatorInstance(ChartInstance owner) {
        this.owner = owner;
    }

    /**
     *  Retourne le relevé propriétaire.
     *  @return Relevé propriétaire.
     */
    public ChartInstance getOwner() {
        return this.owner;
    }

    /**
     *  Retourne une description textuelle de l'objet.
     *  @return Description textuelle.
     */
    @Override
    public abstract String toString();

    /**
     *  Indique si l'indicateur peut fournir un artefact à la date demandée.
     *  @param time Date demandée.
     *  @return true si l'indicateur peut fournir un artefact, false sinon.
     */
    protected abstract boolean hasArtifact(DateTime time);

    /**
     *  Retourne un artefact rattaché à l'indicateur pour une date donnée.
     *  @param time Date recherchée.
     *  @return Artefact correspondant.
     */
    protected abstract double[] getArtifact(DateTime time);
}
