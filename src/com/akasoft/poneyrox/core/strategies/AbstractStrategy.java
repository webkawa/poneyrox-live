package com.akasoft.poneyrox.core.strategies;

import com.akasoft.poneyrox.core.projections.AbstractProjection;
import com.akasoft.poneyrox.core.projections.ClosePriceProjection;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.TimeSeries;
import org.hibernate.criterion.Projection;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.DataSetPreProcessor;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *  Stratégie unitaire.
 */
public abstract class AbstractStrategy implements DataSetIterator {
    /**
     *  Série étudiée.
     */
    private final TimeSeries series;

    /**
     *  Liste des indicateurs d'entrée.
     *  Utilisée en variable d'entrée de la machine.
     */
    private final List<Indicator<Decimal>> inputs;

    /**
     *  Liste des indicateurs de sortie.
     */
    private final List<AbstractProjection> outputs;

    /**
     *  Taille des mini-lots.
     */
    private int miniBatchSize;

    /**
     *  Position du curseur.
     */
    private int cursor;

    /**
     *  Constructeur.
     *  @param series Série étudiée.
     */
    public AbstractStrategy(TimeSeries series) {
        this.series = series;
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
        this.miniBatchSize = 128;
        this.cursor = 0;
    }

    /**
     *  Retourne la série étudiée.
     */
    public TimeSeries getSeries() {
        return this.series;
    }

    /**
     *  Retourne la liste des indicateurs.
     *  @return Liste des indicateurs.
     */
    public List<Indicator<Decimal>> getInputs() {
        return this.inputs;
    }

    /**
     *  Retourne la liste des mini-lots.
     *  @return Liste des mini-lots.
     */
    public int getMiniBatchSize() {
        return this.miniBatchSize;
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
     *  Définit la taille des mini-lots.
     *  @param miniBatchSize Taille des mini-lots.
     */
    public void setMiniBatchSize(int miniBatchSize) {
        this.miniBatchSize = miniBatchSize;
    }

    /**
     *  Ajoute une projection sur le prix de sortie.
     *  @param deepth Profondeur de la projection.
     */
    public void addClosePriceProjection(int deepth) {
        this.outputs.add(new ClosePriceProjection(deepth, this.series));
    }

    /**
     *  Définit le pré-processeur.
     * @param dataSetPreProcessor Valeur affectée.
     */
    @Override
    public void setPreProcessor(DataSetPreProcessor dataSetPreProcessor) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     *  Indique si la stratégie contient un mini-lot supplémentaire.
     *  @return true si la stratégie contient un mini-lot supplémentaire.
     */
    @Override
    public boolean hasNext() {
        return this.series.getTickCount() - cursor > this.miniBatchSize;
    }

    /**
     *  Retourne le mini-lot suivant.
     *  @return Mini-lot suivant.
     */
    @Override
    public DataSet next() {
        return this.next(this.miniBatchSize);
    }

    /**
     *  Génère et retourne un lot de taille fixe.
     *  @param size Taille du lot.
     *  @return Lot correspondant.
     */
    @Override
    public DataSet next(int size) {
        /* Préparation des données */
        double[][] inputRaw = new double[this.miniBatchSize][];
        double[][] inputMask = new double[this.miniBatchSize][];
        double[][] outputRaw = new double[this.miniBatchSize][];
        double[][] outputMask = new double[this.miniBatchSize][];
        int fullsize  = this.inputs.size() + this.outputs.size();

        /* Parcours */
        for (int idx = 0, i = this.cursor; i < this.cursor + this.miniBatchSize; idx++, i++) {
            /* Gestion des entrées
               TODO : supprimer les entrées non-pertinentes */
            double[] inputRawBuff = new double[fullsize];
            double[] inputMaskBuff = new double[fullsize];
            for (int j = 0; j < this.inputs.size(); j++) {
                inputRawBuff[j] = this.inputs.get(j).getValue(i).toDouble();
                inputMaskBuff[j] = 1;
            }
            for (int j = this.inputs.size(); j < fullsize; j++) {
                inputRawBuff[j] = 0;
                inputMaskBuff[j] = 0;
            }
            inputRaw[idx] = inputRawBuff;
            inputMask[idx] = inputMaskBuff;

            /* Gestion des sorties */
            double[] outputRawBuff = new double[fullsize];
            double[] outputMaskBuff = new double[fullsize];
            for (int j = 0; j < this.inputs.size(); j++) {
                outputRawBuff[j] = 0;
                outputMaskBuff[j] = 0;
            }
            for (int j = 0, k = this.inputs.size(); j < this.outputs.size(); j++, k++) {
                AbstractProjection projection = this.outputs.get(j);
                if (i < this.miniBatchSize - projection.getDeepth()) {
                    outputRawBuff[k] = projection.getIndicator().getValue(i + projection.getDeepth()).toDouble();
                    outputMaskBuff[k] = 1;
                } else {
                    outputRawBuff[k] = 0;
                    outputMaskBuff[k] = 0;
                }
            }
            outputRaw[idx] = outputRawBuff;
            outputMask[idx] = outputMaskBuff;
        }
        this.cursor += this.miniBatchSize;

        /* Renvoi */
        INDArray inputRawArray = Nd4j.create(inputRaw);
        INDArray outputRawArray = Nd4j.create(outputRaw);
        INDArray inputMaskArray = Nd4j.create(inputMask);
        INDArray outputMaskArray = Nd4j.create(outputMask);
        return new DataSet(
                inputRawArray,
                outputRawArray);
        /*
        return new DataSet(
                inputRawArray,
                outputRawArray,
                inputMaskArray,
                outputMaskArray);
        */
    }

    /**
     *  Indique si l'itérateur peut etre redémarré.
     *  @return true si l'itérateur peut etre redémarré.
     */
    @Override
    public boolean resetSupported() {
        return true;
    }

    /**
     *  Redémarre l'itérateur.
     */
    @Override
    public void reset() {
        this.cursor = 0;
    }

    /**
     *  Retourne le nombre de mini-lots disponibles.
     *  @return Nombre de mini-lots disponibles.
     */
    @Override
    public int totalExamples() {
        return (int) Math.floor(this.series.getTickCount() / this.miniBatchSize);
    }

    /**
     *  Retourne le nombre d'exemples.
     *  @return Nombre d'exemples.
     */
    @Override
    public int numExamples() {
        return this.totalExamples();
    }

    /**
     *  Retourne le nombre de colonnes d'entrée.
     *  @return Nombre de colonnes d'entrée.
     */
    @Override
    public int inputColumns() {
        return this.inputs.size() + this.outputs.size();
    }

    /**
     *  Retourne le nombre de colonnes de sortie.
     *  @return Nombre de colonnes de sortie.
     */
    @Override
    public int totalOutcomes() {
        return this.inputs.size() + this.outputs.size();
    }

    /**
     *  Indique si le chargement peut avoir lieu de manière asynchrone.
     *  @return true si le chargement peut avoir lieu en asynchrone.
     */
    @Override
    public boolean asyncSupported() {
        return true;
    }

    /**
     *  Retourne la taille des lots.
     *  @return Taille des lots.
     */
    @Override
    public int batch() {
        return this.miniBatchSize;
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
     *  Ajoute un indicateur d'entrée à la liste.
     *  @param input Indicateur ajouté.
     */
    protected void addInput(Indicator<Decimal> input) {
        this.inputs.add(input);
    }
}
