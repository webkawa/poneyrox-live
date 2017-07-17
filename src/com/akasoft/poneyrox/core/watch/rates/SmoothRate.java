package com.akasoft.poneyrox.core.watch.rates;

import com.akasoft.poneyrox.core.watch.clusters.Cluster;
import com.akasoft.poneyrox.core.watch.clusters.ClusterType;
import com.akasoft.poneyrox.exceptions.CurveException;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

/**
 *  Taux lissé.
 *  Classe représentative d'un taux lissé.
 */
public class SmoothRate extends AbstractRate {
    /**
     *  Constructeur.
     *  @param type Type de taux créé.
     *  @param source Liste des taux employés.
     *  @throws CurveException En cas d'erreur lors de la génération.
     */
    public SmoothRate(AbstractRateType type, Set<RawRate> source) throws CurveException {
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
     *  @throws CurveException En cas d'erreur lors de la génération.
     */
    private static Cluster extractCluster(ClusterType type, Set<RawRate> source) throws CurveException {
        /* Récupération de la valeur */
        double value = 0;
        switch (type) {
            case MINIMUM:
                value = source.stream()
                        .mapToDouble(e -> e.getMinimum().getValue())
                        .average()
                        .getAsDouble();
                break;
            case AVERAGE:
                value = source.stream()
                        .mapToDouble(e -> e.getAverage().getValue())
                        .average()
                        .getAsDouble();
                break;
            case MAXIMUM:
                value = source.stream()
                        .mapToDouble(e -> e.getMaximum().getValue())
                        .average()
                        .getAsDouble();
                break;
            default:
                throw new CurveException("Invalid cluster type %s", type);
        }

        /* Création et renvoi */
        return new Cluster(type, value);
    }
}
