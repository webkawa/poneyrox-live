package com.akasoft.poneyrox.controllers.engine;

import com.akasoft.poneyrox.api.alphavantage.AlphaVantageAPI;
import com.akasoft.poneyrox.api.alphavantage.AlphaVantageIntradayInterval;
import com.akasoft.poneyrox.api.histdata.HistDataAPI;
import com.akasoft.poneyrox.core.strategies.SimpleForexStrategy;
import com.akasoft.poneyrox.exceptions.ImportException;
import eu.verdelhan.ta4j.TimeSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.text.ParseException;

/**
 *  Controleur permettant le déclenchement de tests continus.
 */
@RestController
public class ContinuousRunController {
    /**
     *  Controleur des lancements unitaires.
     */
    private final SingleRunController singleRunController;

    /**
     *  API AlphaVantage.
     */
    private final AlphaVantageAPI alphaVantageAPI;

    /**
     *  API HistData.
     */
    private final HistDataAPI histDataAPI;

    /**
     *  Constructeur.
     *  @param alphaVantageAPI API AlphaVantage.
     *  @param histDataAPI API HistData.
     *  @param singleRunController Controleur des lancements unitaires.
     */
    public ContinuousRunController(
            @Autowired AlphaVantageAPI alphaVantageAPI,
            @Autowired HistDataAPI histDataAPI,
            @Autowired SingleRunController singleRunController) {
        this.singleRunController = singleRunController;
        this.alphaVantageAPI = alphaVantageAPI;
        this.histDataAPI = histDataAPI;
    }

    /**
     *  Exécution.
     *  @throws ImportException En cas d'erreur pendant l'import.
     *  @throws FileNotFoundException En cas de fichier introuvable.
     *  @throws ParseException En cas d'erreur de parsing.
     */
    @RequestMapping("foo")
    public void execute() throws ImportException, FileNotFoundException, ParseException {
        /* Chargement des données */
        TimeSeries series = this.histDataAPI.load("WEB-INF/histdata/DAT_ASCII_EURUSD_M1_2016.csv");

        /* Création de la stratégie */
        SimpleForexStrategy strategy = new SimpleForexStrategy(series);
        strategy.addPriceVariationProjection(1);
        //strategy.addPriceVariationProjection(2);
        //strategy.addPriceVariationProjection(3);
        strategy.normalize();

        /* Lancement */
        this.singleRunController.execute(strategy);
    }
}
