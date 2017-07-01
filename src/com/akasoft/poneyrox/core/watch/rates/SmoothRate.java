package com.akasoft.poneyrox.core.watch.rates;

import com.akasoft.poneyrox.core.watch.clusters.Cluster;
import com.akasoft.poneyrox.core.watch.clusters.ClusterType;

import java.util.Set;

/**
 *  Taux lissé.
 *  Classe représentative d'un taux lissé.
 */
public class SmoothRate extends AbstractRate {
    /**
     *  Constructeur.
     *  @param type Type de taux créé.
     *  @param source Liste des taux employés.
     */
    protected SmoothRate(AbstractRateType type, Set<RawRate> source) {
        super(type,
                SmoothRate.extractCluster(ClusterType.MINIMUM, source),
                SmoothRate.extractCluster(ClusterType.AVERAGE, source),
                SmoothRate.extractCluster(ClusterType.MAXIMUM, source));
    }

    /**
     *  Extrait un des noeuds rattachés à un taux lissé.
     *  @param type Type de noeud extrait.
     *  @param source Liste des taux employés.
     *  @return Noeud généré.
     */
    private static Cluster extractCluster(ClusterType type, Set<RawRate> source) {
        /* TODO */
        return null;
    }
}
