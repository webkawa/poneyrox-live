package com.akasoft.poneyrox.core.watch.cells;

import com.akasoft.poneyrox.core.watch.curves.AbstractCurve;
import com.akasoft.poneyrox.core.watch.curves.RawCurve;
import com.akasoft.poneyrox.core.watch.rates.AbstractRateType;
import com.akasoft.poneyrox.core.watch.rates.RawRate;

/**
 *  Cellule brute.
 *  Classe représentative d'une cellule basée sur un taux brut.
 */
public class RawCell extends AbstractCell<RawRate> {
    /**
     *  Constructeur.
     *  @param owner Courbe propriétaire.
     *  @param start Date de départ.
     *  @param times Dates des taux relevés.
     *  @param bid Taux d'offre relevés.
     *  @param ask Taux de demande relevés.
     */
    public RawCell(RawCurve owner, long start, long[] times, double[] bid, double[] ask) {
        super(owner,
                start,
                RawCell.extractRate(AbstractRateType.BID, times, bid),
                RawCell.extractRate(AbstractRateType.ASK, times, ask));
    }

    /**
     *  Génère un taux brut à partir d'une liste de valeurs relevées.
     *  @param type Type de taux traité.
     *  @param times Dates des taux relevés.
     *  @param rates Liste des taux relevés.
     *  @return Taux correspondant.
     */
    private static RawRate extractRate(AbstractRateType type, long[] times, double[] rates) {
        /* TODO */
        return null;
    }
}
