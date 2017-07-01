package com.akasoft.poneyrox.core.watch.cells;

import com.akasoft.poneyrox.core.watch.curves.AbstractCurve;
import com.akasoft.poneyrox.core.watch.curves.SmoothCurve;
import com.akasoft.poneyrox.core.watch.rates.AbstractRateType;
import com.akasoft.poneyrox.core.watch.rates.SmoothRate;

import java.util.Set;

/**
 *  Cellule lissée.
 *  Classe représentative d'une cellule basée sur des taux lissés.
 */
public class SmoothCell extends AbstractCell<SmoothRate> {
    /**
     *  Constructeur.
     *  @param owner Courbe propriétaire.
     *  @param start Date de départ.
     *  @param source Liste des cellules utilisées pour la génération de la cellule lissée.
     */
    public SmoothCell(SmoothCurve owner, long start, Set<RawCell> source) {
        super(owner,
                start,
                SmoothCell.extractRate(AbstractRateType.BID, source),
                SmoothCell.extractRate(AbstractRateType.ASK, source));
    }

    /**
     *  Génère un taux lissé à partir d'une liste de cellules brutes.
     *  @param type Type de taux.
     *  @param source Liste de cellules brutes.
     *                Similaires au paramètre reçu par le constructeur.
     *  @return Taux correspondant.
     */
    private static SmoothRate extractRate(AbstractRateType type, Set<RawCell> source) {
        /* TODO */
        return null;
    }
}
