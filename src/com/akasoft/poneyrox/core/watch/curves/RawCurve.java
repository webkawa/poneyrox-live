package com.akasoft.poneyrox.core.watch.curves;

import com.akasoft.poneyrox.core.watch.cells.RawCell;
import com.akasoft.poneyrox.core.watch.clusters.ClusterType;
import com.akasoft.poneyrox.core.watch.rates.AbstractRateType;
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
     *  @param times Dates des relevés.
     *  @param bid Valeur des relevés sur la demande.
     *  @param ask Valeur des relevés sur l'offre.
     *  @throws CurveException En cas d'erreur lors de la génération de la courbe.
     */
    protected RawCurve(String market, long interval, long[] times, double[] bid, double[] ask) throws CurveException {
        super(market, interval, 1);

        /* Vérification des données brutes */
        if (times.length < 1) {
            throw new CurveException("Insufficient data for raw curve generation");
        }
        if (times.length != bid.length || times.length != ask.length) {
            throw new CurveException("Invalid data for raw curve generation");
        }

        /* Création des bornes associées à chaque cellule */
        long end = super.getDate();
        long start = end - super.getInterval() + 1;

        /* Création des cellules */
        for (int placed = 0, distance = 0; placed < times.length; distance++) {
            /* Calcul du nombre d'éléments dans la sous-liste */
            int size = 0;
            for (long time : times) {
                if (time > start && time <= end) {
                    size++;
                }
            }

            /* Vérification */
            if (size > 0) {
                /* Récupération des données */
                double[] subBid = new double[size];
                double[] subAsk = new double[size];

                /* Complétion */
                for (int i = 0, j = 0; i < times.length; i++) {
                    if (times[i] > start && times[i] <= end) {
                        subBid[j] = bid[i];
                        subAsk[j] = ask[i];
                        j++;
                    }
                }

                /* Création de la cellule */
                RawCell cell = new RawCell(start, end, subBid, subAsk);

                /* Ajout au contenu de la courbe */
                super.addCell(distance, cell);
            }

            /* Mise à jour des bornes */
            end -= super.getInterval();
            start -= super.getInterval();
            placed += size;
        }
    }

    /**
     *  Référence une cellule manquante.
     *  @param place Position de la cellule manquante.
     *  @param righPosition Position de la pré-existante la plus proche à droite.
     *                      Si aucune cellule disponible à droite, égal à -1.
     *  @param rightCell Cellule pré-existante la plus proche à droite.
     *  @param leftPosition Position de la cellule pré-existante la plus proche à gauche.
     *  @param leftCell Cellule pré-existante à gauche.
     *  @throws CurveException En cas d'erreur lors de la génération de la cellule.
     */
    @Override
    protected void addMissing(int place, int righPosition, RawCell rightCell, int leftPosition, RawCell leftCell) throws CurveException {
        /* Définition des dates de début et de fin */
        long end = super.getDate() - (super.getInterval() * place);
        long start = end - super.getInterval() + 1;

        /* Traitement d'une cellule en fin de course */
        if (leftPosition == -1) {
            /* Création de la cellule manquante */
            RawCell missing = new RawCell(
                    start,
                    end,
                    rightCell.getBid().getMinimum().getValue(),
                    rightCell.getBid().getAverage().getValue(),
                    rightCell.getBid().getMaximum().getValue(),
                    rightCell.getAsk().getMinimum().getValue(),
                    rightCell.getAsk().getAverage().getValue(),
                    rightCell.getAsk().getMaximum().getValue());

            /* Complétion */
            super.addCell(place, missing);
        } else {
            /* Calcul du ratio */
            double ratio = (leftPosition - place) / (leftPosition - righPosition);

            /* Création de la cellule manquante */
            RawCell missing = new RawCell(
                    start,
                    end,
                    this.computeMiddle(ratio, rightCell, leftCell, AbstractRateType.BID, ClusterType.MINIMUM),
                    this.computeMiddle(ratio, rightCell, leftCell, AbstractRateType.BID, ClusterType.AVERAGE),
                    this.computeMiddle(ratio, rightCell, leftCell, AbstractRateType.BID, ClusterType.MAXIMUM),
                    this.computeMiddle(ratio, rightCell, leftCell, AbstractRateType.ASK, ClusterType.MINIMUM),
                    this.computeMiddle(ratio, rightCell, leftCell, AbstractRateType.ASK, ClusterType.AVERAGE),
                    this.computeMiddle(ratio, rightCell, leftCell, AbstractRateType.ASK, ClusterType.MAXIMUM));

            /* Complétion */
            super.addCell(place, missing);
        }
    }

    /**
     *  Calcule la valeur rattachée, pour un type de taux et un type de noeud, à une cellule manquante
     *  au moyen d'un ratio calculé en fonction des distantes entre les cellules encadrantes et à partir
     *  des cellules elles-memes.
     *  @param ratio Ratio entre la cellule de droite et la cellule de gauche.
     *  @param rightCell Cellule de droite.
     *  @param leftCell Cellule de gauche.
     *  @param rateType Type de taux.
     *  @param clusterType Type de noeud.
     *  @return Valeur intermédiaire.
     *  @throws CurveException En cas d'erreur lors du calcul.
     */
    private double computeMiddle(double ratio, RawCell rightCell, RawCell leftCell, AbstractRateType rateType, ClusterType clusterType) throws CurveException {
        /* Calcul de la différence */
        double leftValue = leftCell.getRateByType(rateType).getClusterByType(clusterType).getValue();
        double rightValue = rightCell.getRateByType(rateType).getClusterByType(clusterType).getValue();
        double difference = leftValue - rightValue;

        /* Renvoi */
        return difference * ratio;
    }
}
