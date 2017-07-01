package com.akasoft.poneyrox.core.watch.rates;

import com.akasoft.poneyrox.core.watch.clusters.Cluster;
import com.akasoft.poneyrox.core.watch.clusters.ClusterType;

/**
 *  Taux brut.
 *  Classe représentative d'un taux brut inscrit au sein d'une cellule de courbe.
 */
public class RawRate extends AbstractRate {
    /**
     *  Constructeur.
     *  @param type Type de taux créé.
     *  @param times Dates des relevés.
     *  @param rates Taux relevés.
     */
    public RawRate(AbstractRateType type, long[] times, double[] rates) {
        super(type,
                RawRate.extractCluster(ClusterType.MINIMUM, times, rates),
                RawRate.extractCluster(ClusterType.AVERAGE, times, rates),
                RawRate.extractCluster(ClusterType.MAXIMUM, times, rates));
    }

    /**
     *  Extrait un des noeuds rattachés à un taux brut.
     *  @param type Type du noeud.
     *  @param times Liste des dates de relevé.
     *  @param rates Liste des taux relevés.
     *  @return Noeud correspondant.
     */
    private static Cluster extractCluster(ClusterType type, long[] times, double[] rates) {
        /* TODO */
        return null;
    }
}
