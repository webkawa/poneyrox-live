package com.akasoft.poneyrox.core.watch.cells;

import com.akasoft.poneyrox.core.watch.rates.AbstractRateType;
import com.akasoft.poneyrox.core.watch.rates.RawRate;
import com.akasoft.poneyrox.exceptions.CurveException;

/**
 *  Cellule brute.
 *  Classe représentative d'une cellule basée sur un taux brut.
 */
public class RawCell extends AbstractCell<RawRate> {
    /**
     *  Constructeur.
     *  @param start Date de départ.
     *  @param end Date de fin.
     *  @param bid Taux d'offre relevés.
     *  @param ask Taux de demande relevés.
     *  @throws CurveException En cas d'erreur lors de la génération de la courbe.
     */
    public RawCell(long start, long end, double[] bid, double[] ask) throws CurveException {
        super(start,
                end,
                new RawRate(AbstractRateType.BID, bid),
                new RawRate(AbstractRateType.ASK, ask));
    }

    /**
     *  Constructeur empirique.
     *  Utilisé pour la complétion des cellules manquantes.
     *  @param start Date de départ.
     *  @param end Date de fin.
     *  @param minBid Offre minimum.
     *  @param avgBid Offre moyenne.
     *  @param maxBid Offre maximum.
     *  @param minAsk Demande minimum.
     *  @param avgAsk Demande moyenne.
     *  @param maxAsk Demande maximum.
     */
    public RawCell(long start, long end, double minBid, double avgBid, double maxBid, double minAsk, double avgAsk, double maxAsk) {
        super(start,
                end,
                new RawRate(AbstractRateType.BID, minBid, avgBid, maxBid),
                new RawRate(AbstractRateType.ASK, minAsk, avgAsk, maxAsk));
    }
}
