package com.akasoft.poneyrox.core.watch.rates;

import com.akasoft.poneyrox.core.watch.clusters.Cluster;
import com.akasoft.poneyrox.core.watch.clusters.ClusterType;
import com.akasoft.poneyrox.exceptions.CurveException;

import java.util.Arrays;

/**
 *  Taux brut.
 *  Classe représentative d'un taux brut inscrit au sein d'une cellule de courbe.
 */
public class RawRate extends AbstractRate {
    /**
     *  Constructeur.
     *  @param type Type de taux créé.
     *  @param rates Taux relevés.
     *  @throws CurveException En cas d'erreur lors de la génération.
     */
    public  RawRate(AbstractRateType type, double[] rates) throws CurveException {
        super(type,
                RawRate.extractCluster(ClusterType.MINIMUM, rates),
                RawRate.extractCluster(ClusterType.AVERAGE, rates),
                RawRate.extractCluster(ClusterType.MAXIMUM, rates));
    }

    /**
     *  Constructeur empirique.
     *  Utilisé pour la complétion des cellules manquantes.
     *  @param type Type de taux créé.
     *  @param minimum Taux minimum.
     *  @param average Taux moyen.
     *  @param maximum Taux maximum.
     */
    public RawRate(AbstractRateType type, double minimum, double average, double maximum) {
        super(type,
                new Cluster(ClusterType.MINIMUM, minimum),
                new Cluster(ClusterType.AVERAGE, average),
                new Cluster(ClusterType.MAXIMUM, maximum));
    }

    /**
     *  Extrait un des noeuds rattachés à un taux brut.
     *  @param type Type du noeud.
     *  @param rates Liste des taux relevés.
     *  @return Noeud correspondant.
     *  @throws CurveException En cas d'erreur lors de la génération.
     */
    private static Cluster extractCluster(ClusterType type, double[] rates) throws CurveException {
        /* Récupération de la valeur */
        double value = 0;
        switch (type) {
            case MINIMUM:
                value = Arrays.stream(rates).min().getAsDouble();
                break;
            case AVERAGE:
                value = Arrays.stream(rates).average().getAsDouble();
                break;
            case MAXIMUM:
                value = Arrays.stream(rates).max().getAsDouble();
                break;
            default:
                throw new CurveException("Invalid cluster type %s", type);
        }

        /* Création et renvoi du noeud */
        return new Cluster(type, value);
    }
}
