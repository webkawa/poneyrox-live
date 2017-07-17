package com.akasoft.poneyrox.core.watch.cells;

import com.akasoft.poneyrox.core.watch.curves.AbstractCurve;
import com.akasoft.poneyrox.core.watch.curves.SmoothCurve;
import com.akasoft.poneyrox.core.watch.rates.AbstractRateType;
import com.akasoft.poneyrox.core.watch.rates.SmoothRate;
import com.akasoft.poneyrox.exceptions.CurveException;

import java.util.Set;
import java.util.stream.Collectors;

/**
 *  Cellule lissée.
 *  Classe représentative d'une cellule basée sur des taux lissés.
 */
public class SmoothCell extends AbstractCell<SmoothRate> {
    /**
     *  Constructeur.
     *  @param start Date de départ.
     *  @param end Date de fin.
     *  @param source Liste des cellules utilisées pour la génération de la cellule lissée.
     *  @throws CurveException En cas d'erreur lors de la génération de la courbe.
     */
    public SmoothCell(long start, long end, Set<RawCell> source) throws CurveException {
        super(start,
                end,
                new SmoothRate(AbstractRateType.BID, source.stream().map(e -> e.getBid()).collect(Collectors.toSet())),
                new SmoothRate(AbstractRateType.ASK, source.stream().map(e -> e.getAsk()).collect(Collectors.toSet())));
    }
}
