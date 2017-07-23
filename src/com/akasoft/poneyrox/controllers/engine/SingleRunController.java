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
        /* Calcul du nombre d'éléments par entrée */
        int size = strategy.inputColumns() * strategy.getMinibatchSize();
        int h1 = (int) Math.round(size * 0.9);
        int h2 = (int) Math.round(size * 0.8);
        int h3 = (int) Math.round(size * 0.7);
        int h4 = (int) Math.round(size * 0.6);

        /* Création de la machine */
        MultiLayerConfiguration mlc = new NeuralNetConfiguration.Builder()
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .iterations(1)
                .learningRate(0.01)
                //.rmsDecay(0.95)
                .seed(1234)
                .regularization(true)
                .l2(0.001)
                .weightInit(WeightInit.XAVIER)
                .updater(Updater.RMSPROP)
                .list()
                .layer(0, new GravesLSTM.Builder().nIn(strategy.inputColumns()).nOut(h1).activation(Activation.TANH).build())
                .layer(1, new GravesLSTM.Builder().nIn(h1).nOut(h2).activation(Activation.TANH).build())
                .layer(2, new GravesLSTM.Builder().nIn(h2).nOut(h3).activation(Activation.TANH).build())
                .layer(3, new GravesLSTM.Builder().nIn(h3).nOut(h4).activation(Activation.TANH).build())
                .layer(4, new RnnOutputLayer.Builder(LossFunctions.LossFunction.MCXENT).activation(Activation.SOFTMAX).nIn(h4).nOut(strategy.totalOutcomes()).build())
                .pretrain(false)
                .backprop(true)
                .build();
        MultiLayerNetwork mln = new MultiLayerNetwork(mlc);
        mln.init();
        mln.setListeners(new ScoreIterationListener(1));

        /* Entrainement */
        for (int i = 0; i < 5; i++) {
            while(strategy.hasNext()){
                /* Récupération du lot et division */
                DataSet next = strategy.next();
                SplitTestAndTrain split = next.splitTestAndTrain(0.90d);

                /* Entrainement */
                mln.fit(split.getTrain());

                /* Test */
                Evaluation eval = new Evaluation();
                INDArray predict = mln.output(split.getTest().getFeatures(), false);
                eval.evalTimeSeries(split.getTest().getLabels(), predict);


                /* Affichage */
                String output = String.format(
                        "%f %f %f %f %d/%d",
                        eval.accuracy(),
                        eval.precision(),
                        eval.recall(),
                        eval.f1(),
                        strategy.cursor(),
                        strategy.getSeries().getTickCount());
                System.out.println(output);
            }
            strategy.reset();
        }
    }
}
