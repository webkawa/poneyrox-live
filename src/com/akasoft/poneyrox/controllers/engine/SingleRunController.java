package com.akasoft.poneyrox.controllers.engine;

import com.akasoft.poneyrox.core.strategies.AbstractStrategy;
import com.akasoft.poneyrox.core.strategies.SimpleForexStrategy;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Indicator;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.GravesLSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 *  Test unitaire.
 *  Controleur en charge d'un test unitaire et aléatoire réalisé par le biais d'une machine sur
 *  une série passée en paramètre.
 */
@Controller
public class SingleRunController {
    /**
     *  Constructeur.
     *  @param strategy Stratégie
     */
    public void execute(AbstractStrategy strategy) {

        /* Création de la machine */
        MultiLayerConfiguration mlc = new NeuralNetConfiguration.Builder()
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .iterations(3)
                .learningRate(0.1)
                .rmsDecay(0.95)
                .seed(1234)
                .regularization(true)
                .l2(0.001)
                .weightInit(WeightInit.XAVIER)
                .updater(Updater.RMSPROP)
                .list()
                .layer(0, new GravesLSTM.Builder().nIn(strategy.inputColumns()).nOut(256).activation(Activation.TANH).build())
                .layer(1, new GravesLSTM.Builder().nIn(256).nOut(256).activation(Activation.TANH).build())
                .layer(2, new GravesLSTM.Builder().nIn(256).nOut(256).activation(Activation.TANH).build())
                .layer(3, new RnnOutputLayer.Builder(LossFunctions.LossFunction.MCXENT).activation(Activation.SOFTMAX).nIn(256).nOut(strategy.totalOutcomes()).build())
                .pretrain(false)
                .backprop(true)
                .build();
        MultiLayerNetwork mln = new MultiLayerNetwork(mlc);
        mln.init();
        mln.setListeners(new ScoreIterationListener(1));

        //INDArray ind = strategy.next().getFeatureMatrix();
        //mln.output(ind, true);

        /* Entrainement */
        for (int i = 0; i < 4; i++) {
            while(strategy.hasNext()){
                mln.fit(strategy.next());
            }
            strategy.reset();
        }

        /* Test */
        while (strategy.hasNext()) {
            DataSet next = strategy.next();
            Evaluation eval = new Evaluation();
            INDArray predict = mln.output(next.getFeatureMatrix(), false);
            eval.eval(next.getLabels(), predict);

            int x = 2;
        }
    }
}
