package com.akasoft.poneyrox.core.watch.curves;

import com.akasoft.poneyrox.core.watch.cells.AbstractCell;
import com.akasoft.poneyrox.entities.MarketEntity;
import com.akasoft.poneyrox.exceptions.CurveException;

import java.util.*;

/**
 *  Courbe.
 *  Classe descriptive d'une courbe brute ou lissée applicable à un marché donné.
 *  @param <TCell> Type de cellule portée.
 */
public class AbstractCurve<TCell extends AbstractCell> {
    /**
     *  Clef d'accès au marché rattaché.
     */
    private String market;

    /**
     *  Intervalle des cellules.
     */
    private long interval;

    /**
     *  Niveau de lissage.
     *  Egal à 1 pour une courbe brute et à une valeur supérieure à 1 pour une
     *  courbe lissée.
     */
    private int smooth;

    /**
     *  Liste des cellules.
     *  Les cellules sont, par convention, classées par ordre chronologique et référencée par date de début.
     */
    private Map<Long, TCell> cells;

    /**
     *  Constructeur.
     *  @param market Marché rattaché.
     *  @param interval Intervalle des cellules exprimé en secondes.
     *  @param smooth Niveau de lissage.
     *  @throws CurveException En cas d'erreur lors de la génération de la courbe.
     */
    protected AbstractCurve(String market, long interval, int smooth) throws CurveException {
        /* Vérification de l'intervalle */
        if (interval < 1) {
            throw new CurveException("Invalid curve interval %d", interval);
        }

        /* Affectation */
        this.market = market;
        this.interval = interval;
        this.smooth = smooth;
        this.cells = new LinkedHashMap<>();
    }

    /**
     *  Retourne la clef d'accès au marché.
     *  @return Clef d'accès au marché.
     */
    public String getMarket() {
        return this.market;
    }

    /**
     *  Retourne l'intervalle des cellules.
     *  @return Intervalle des cellules.
     */
    public long getInterval() {
        return this.interval;
    }

    /**
     *  Retourne le niveau de lissage.
     *  @return Niveau de lissage.
     */
    public int getSmooth() {
        return this.smooth;
    }

    /**
     *  Retourne la clef de hachage rattachée à la courbe.
     *  @return Clef de hachage.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                this.market,
                this.interval,
                this.smooth);
    }
}
