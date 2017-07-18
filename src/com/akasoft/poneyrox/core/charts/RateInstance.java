package com.akasoft.poneyrox.core.charts;

import com.akasoft.poneyrox.entities.OHLCRateEntity;

/**
 *  Taux.
 *  Objet métier représentatif d'un taux unitaire inscrit dans un relevé.
 */
public class RateInstance {
    /**
     *  Relevé porteur.
     */
    private ChartInstance owner;

    /**
     *  Date du taux.
     */
    private long time;

    /**
     *  Relevé OHCL brut.
     */
    private OHLCRateEntity rawOHLC;

    /**
     *  Liste des indicateurs.
     *  Les indicateurs sont organisés dans le sens défini au niveau du relevé parent, chaque élément pouvant
     *  contenir un - ou plusieurs - sous-artefacts.
     */
    private double[][] indicators;

    /**
     *  Constructeur.
     *  @param owner Relevé porteur.
     *  @param time Date du taux.
     *  @param rawOHLC Relevé OHCL brut.
     */
    public RateInstance(ChartInstance owner, Long time, OHLCRateEntity rawOHLC) {
        this.owner = owner;
        this.time = time;
        this.rawOHLC = rawOHLC;
    }

    /**
     *  Retourne le relevé porteur.
     *  @return Relevé porteur.
     */
    public ChartInstance getOwner() {
        return this.owner;
    }

    /**
     *  Retourne la date du taux.
     *  @return Date du taux.
     */
    public long getTime() {
        return this.time;
    }

    /**
     *  Retourne le relevé OHCL brut.
     *  @return Relevé OHCL brut.
     */
    public OHLCRateEntity getRawOHLC() {
        return this.rawOHLC;
    }
}
