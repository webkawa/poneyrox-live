package com.akasoft.poneyrox.core.watch.curves;

import com.akasoft.poneyrox.core.watch.cells.SmoothCell;
import com.akasoft.poneyrox.exceptions.CurveException;

/**
 *  Courbe lissée.
 *  Courbe représentative des taux relevés sur un marché donné impliquant un facteur de lissage
 *  exprimé en nombre de cellules.
 */
public class SmoothCurve extends AbstractCurve<SmoothCell> {
    /**
     *  Constructeur.
     *  @param market Marché rattaché.
     *  @param interval Intervalle des cellules en secondes.
     *  @param smooth Niveau de lissage.
     *  @param source Courbe d'origine.
     *  @throws CurveException En cas d'erreur lors de la génération de la courbe.
     */
    protected SmoothCurve(String market, long interval, int smooth, RawCurve source) throws CurveException {
        super(market, interval, smooth);

        /* Vérification du facteur de lissage */
        if (smooth < 2) {
            throw new CurveException("Invalid smooth rate %d", smooth);
        }

        /* TODO */
    }
}
