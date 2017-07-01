package com.akasoft.poneyrox.core.watch.curves;

import com.akasoft.poneyrox.core.watch.cells.RawCell;
import com.akasoft.poneyrox.exceptions.CurveException;

/**
 *  Courbe brute.
 *  Courbe représentative des taux exacts relevés sur un marché.
 */
public class RawCurve extends AbstractCurve<RawCell> {
    /**
     *  Constructeur.
     *  @param market Marché rattaché.
     *  @param interval Intervalle des cellules, en secondes.
     *  @param rates Niveau de l'offre et de la demande.
     *               Chaque élément représente un taux relevé, avec un dimension 0 la date du taux (exprimée en
     *               temps UNIX), en dimension 1 la valeur de la demande et en dimension 2 la valeur de l'offre.
     *  @throws CurveException En cas d'erreur lors de la génération de la courbe.
     */
    protected RawCurve(String market, long interval, double[][] rates) throws CurveException {
        super(market, interval, 1);

        /* Vérification des données brutes */
        if (rates.length < 1) {
            throw new CurveException("Insufficient data for raw curve generation");
        }

        /* TODO */
    }
}
