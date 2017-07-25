package com.akasoft.poneyrox.core.strategies;

import com.akasoft.poneyrox.core.projections.AbstractProjection;
import com.akasoft.poneyrox.core.projections.ClosePriceProjection;
import com.akasoft.poneyrox.core.projections.PriceVariationProjection;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.TimeSeries;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.DataSetPreProcessor;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerMinMaxScaler;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.factory.Nd4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  Stratégie unitaire.
 *  Implémente l'interface "DataSetIterator" descriptive d'une classe permettant dans une liste de mini-lots
 *  exploitables
 */
public abstract class AbstractStrategy implements DataSetIterator {
    /**
     *  Série étudiée.
     */
    private final TimeSeries series;

    /**
     *  Liste des indicateurs d'entrée.
     */
    private final List<Indicator<Decimal>> inputIndicators;

    /**
     *  Liste des indicateurs de sortie.
     */
    private final List<AbstractProjection> outputProjections;

    /**
     *  Taille des mini-lots.
     *  Nombre total de taux exploités dans chaque mini-lot retourné par l'itérateur.
     */
    private final int minibatchSize;

    /**
     *  Marge des mini-lots.
     *  Nombre de périodes évaluées pour chaque exemple compris dans un mini-lot.
     */
    private final int minibatchPadding;

    /**
     *  Point de départ du curseur.
     */
    private final int minibatchStart;

    /**
     *  Normaliseur de mise à l'échelle.
     */
    private final NormalizerMinMaxScaler normalizerMinMaxScalerInstance;

    /**
     *  Marqueur de normalisation par mise à l'échelle.
     */
    private boolean normalizerMinMaxScalerMarker;

    /**
     *  Normaliseur de standardisation.
     */
    private final NormalizerStandardize normalizerStandardizeInstance;

    /**
     *  Marqueur de normalisation par standardisation.
     */
    private boolean normalizerStandardizeMarker;

    /**
     *  Mix.
     *  Liste aléatoire des relevés pris en compte dans les mini-lots.
     */
    private final List<Integer> shuffle;

    /**
     *  Cache des indicateurs d'entrée.
     */
    private final List<Double[]> inputCache;

    /**
     *  Cache des indicateurs de sortie.
     */
    private final List<Double[]> outputCache;

    /**
     *  Curseur.
     *  Position courante du curseur dans la liste des mini-lots.
     */
    private int cursor;

    /**
     *  Constructeur.
     *  @param series Série évaluée.
     */
    public AbstractStrategy(TimeSeries series) {
        this.series = series;
        this.inputIndicators = new ArrayList<>();
        this.inputCache = new ArrayList<>();
        this.outputProjections = new ArrayList<>();
        this.outputCache = new ArrayList<>();
        this.minibatchSize = 32;
        this.minibatchPadding = 24;
        this.minibatchStart = this.minibatchPadding * 2;
        this.normalizerMinMaxScalerInstance = new NormalizerMinMaxScaler();
        this.normalizerMinMaxScalerMarker = false;
        this.normalizerStandardizeInstance = new NormalizerStandardize();
        this.normalizerStandardizeMarker = false;
        this.shuffle = new ArrayList<>();

        this.normalizerMinMaxScalerInstance.fitLabel(false);
        this.normalizerStandardizeInstance.fitLabel(false);
        this.reset();
    }

    /**
     *  Retourne la série évaluée.
     *  @return Série évaluée.
     */
    public TimeSeries getSeries() {
        return this.series;
    }

    /**
     *  Retourne la taille des mini-lots.
     *  @return Taille des mini-lots.
     */
    public int getMinibatchSize() {
        return this.minibatchSize;
    }

    /**
     *  Retourne la marge des mini-lots.
     *  @return Marge des mini-lots.
     */
    public int getMinibatchPadding() {
        return this.minibatchPadding;
    }

    /**
     *  Retourne la profondeur maximale des projections référencées dans la stratégie.
     *  @return Profondeur maximale.
     */
    public int getMinibatchDeepth() {
        int result = 0;
        for (AbstractProjection projection : this.outputProjections) {
            if (projection.getDeepth() > result) {
                result = projection.getDeepth();
            }
        }
        return result;
    }

    /**
     *  Retourne le nombre total de mini-lots disponibles dans l'itérateur.
     *  @return Nombre de mini-lots.
     */
    public int getMinibatchCount() {
        return (int) Math.floor(this.getExploitableTicks() / this.minibatchSize);
    }

    /**
     *  Retourne le nombre de relevé exploitables dans la série.
     *  @return Nombre de relevés exploitables.
     */
    public int getExploitableTicks() {
        return this.series.getTickCount() - this.minibatchStart - this.getMinibatchDeepth();
    }

    /**
     *  Ajoute une projection sur le prix de sortie.
     *  @param deepth Profondeur de la projection.
     */
    public void addClosePriceProjection(int deepth) {

        this.addOutput(new ClosePriceProjection(deepth, this.series));
    }

    /**
     *  Ajoute une projection sur la variation du prix.
     *  @param deepth Profondeur de la projection.
     */
    public void addPriceVariationProjection(int deepth) {
        this.addOutput(new PriceVariationProjection(deepth, this.series));
    }

    /**
     *  Réalise le placement des données dans le cache et met à niveau les statistiques
     *  du normalisateur.
     */
    public void normalize() {
        /* Cache */
        this.inputCache.clear();
        this.outputCache.clear();
        for (int i = 0; i < this.series.getTickCount(); i++) {
            /* Gestion des entrées */
            Double[] inputBuff = new Double[this.inputIndicators.size()];
            for (int j = 0; j < this.inputIndicators.size(); j++) {
                inputBuff[j] = this.inputIndicators.get(j).getValue(i).toDouble();
            }
            this.inputCache.add(inputBuff);

            /* Gestion des sorties */
            Double[] outputBuff = new Double[this.outputProjections.size()];
            for (int j = 0; j < this.outputProjections.size(); j++) {
                AbstractProjection projection = this.outputProjections.get(j);
                if (this.series.getTickCount() - projection.getDeepth() > i) {
                    outputBuff[j] = projection.getIndicator().getValue(i + projection.getDeepth()).toDouble();
                } else {
                    outputBuff[j] = Double.valueOf(0);
                }
            }
            this.outputCache.add(outputBuff);
        }

        /* Enrichissement du normaliseur par mise à l'échelle */
        this.normalizerMinMaxScalerMarker = false;
        this.normalizerMinMaxScalerInstance.fit(this);
        this.normalizerMinMaxScalerMarker = true;

        /* Enregistrement du normaliseur par standardisation */
        this.normalizerStandardizeMarker = false;
        this.normalizerStandardizeInstance.fit(this);
        this.normalizerStandardizeMarker = true;
    }

    /**
     *  Indique si l'itérateur contient des données exploitables.
     *  @return true si l'itérateur contient des données exploitables.
     */
    @Override
    public boolean hasNext() {
        return this.cursor < this.getExploitableTicks();
    }

    /**
     *  Retourne le prochain mini-lot disponible dans l'itérateur.
     *  @return Prochain mini-lot.
     */
    @Override
    public DataSet next() {
        return this.next(this.minibatchSize);
    }

    /**
     *  Retourne un mini-lot de données contenant un nombre d'échantillons passé en paramètre.
     *  @param size Nombre d'échantillons retenus.
     *  @return Mini-lot pret à l'utilisation.
     */
    @Override
    public DataSet next(int size) {
        /* Mix */
        if (this.shuffle.size() == 0) {
            for (int i = this.minibatchStart; i < this.minibatchStart + this.getExploitableTicks(); i++) {
                this.shuffle.add(i);
            }
            Collections.shuffle(this.shuffle);
        }

        /* Calcul de la taille réelle du lot */
        int realSize = Math.min(size, this.getExploitableTicks() - this.cursor);

        /* Création des tableaux de retour */
        INDArray input = Nd4j.create(new int[] {realSize, this.inputColumns(), this.minibatchPadding}, 'f');
        INDArray output = Nd4j.create(new int[] {realSize, this.totalOutcomes(), this.minibatchPadding}, 'f');

        /* Parcours */
        for (int i = 0; i < realSize; i++) {
            /* Récupération du pointeur */
            int pointer = this.shuffle.get(this.cursor - this.minibatchPadding);

            /* Entrées */
            for (int j = 0; j < this.inputColumns(); j++) {
                for (int k = 0; k < this.minibatchPadding; k++) {
                    /* Calcul de la valeur injectée */
                    double value = this.inputCache.get(pointer - (this.minibatchPadding - k))[j];

                    /* Ajout */
                    input.putScalar(new int[] {i, j, k}, value);
                }
            }

            /* Sorties */
            for (int j = 0; j < this.totalOutcomes(); j++) {
                for (int k = 0; k < this.minibatchPadding; k++) {
                    /* Récupération de la projection */
                    AbstractProjection projection = this.outputProjections.get(j);

                    /* Calcul de la valeur injectée */
                    double value = this.outputCache.get(pointer - (this.minibatchPadding - k))[j];

                    /* Ajout */
                    output.putScalar(new int[] {i, j, k}, value);
                }
            }

            /* Mise à jour du curseur */
            this.cursor++;
        }

        /* Création du lot */
        DataSet ds = new DataSet(input, output);
        if (this.normalizerMinMaxScalerMarker) {
            this.normalizerMinMaxScalerInstance.preProcess(ds);
            if (this.normalizerStandardizeMarker) {
                this.normalizerStandardizeInstance.preProcess(ds);
            }
        }

        /* Renvoi */
        return ds;
    }

    /**
     *  Retourne la liste des libellés.
     *  @return Liste des libellés.
     */
    @Override
    public List<String> getLabels() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     *  Retourne le pré-processeur.
     *  @return Pré-processeur.
     */
    @Override
    public DataSetPreProcessor getPreProcessor() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     *  Définit le pré-processeur.
     *  @param preProcessor Pré-processeur.
     */
    @Override
    public void setPreProcessor(DataSetPreProcessor preProcessor) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     *  Retourne le nombre total d'exemples disponibles dans l'itérateur.
     *  @return Nombre d'exemples disponibles.
     */
    @Override
    public int totalExamples() {
        return this.getMinibatchCount();
    }

    /**
     *  Retourne le nombre total d'exemples disponibles dans l'itérateur.
     *  Curieusement identique à "totalExamples()"...
     *  @return Nombre d'exemples disponibles.
     */
    public int numExamples() {
        return this.getMinibatchCount();
    }

    /**
     *  Retourne le nombre de colonnes en entrée.
     *  @return Nombre de colonnes en entrée.
     */
    @Override
    public int inputColumns() {
        return this.inputIndicators.size();
    }

    /**
     *  Retourne le nombre de colonnes en sortie.
     *  @return Nombre de colonnes en sortie.
     */
    @Override
    public int totalOutcomes() {
        return this.outputProjections.size();
    }

    /**
     *  Retourne la taille des mini-lots.
     *  @return Taille des mini-lots.
     */
    @Override
    public int batch() {
        return this.minibatchSize;
    }

    /**
     *  Retourne la position du curseur.
     *  @return Position du curseur.
     */
    @Override
    public int cursor() {
        return this.cursor;
    }

    /**
     *  Indique si l'itérateur supporte les traitements asynchrones.
     *  @return true si le pré-processeur supporte les traitements asynchrones.
     */
    @Override
    public boolean asyncSupported() {
        return true;
    }

    /**
     *  Indique si la réinitialisation du curseur est supportée.
     *  @return true si la réinitialisation est supportée, false sinon.
     */
    @Override
    public boolean resetSupported() {
        return true;
    }

    /**
     *  Réinitialise le curseur.
     */
    @Override
    public void reset() {
        this.cursor = this.minibatchStart;
        this.shuffle.clear();
    }

    /**
     *  Ajoute un indicateur d'entrée à la liste.
     *  @param input Indicateur ajouté.
     */
    protected void addInput(Indicator<Decimal> input) {
        this.inputIndicators.add(input);
        this.shuffle.clear();
    }

    /**
     *  Ajoute une projection à la liste.
     *  @param output Projection ajoutée.
     */
    protected void addOutput(AbstractProjection output) {
        this.outputProjections.add(output);
        this.shuffle.clear();
    }
}
