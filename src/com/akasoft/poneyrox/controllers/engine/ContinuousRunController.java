package com.akasoft.poneyrox.controllers.engine;

import com.akasoft.poneyrox.api.alphavantage.AlphaVantageAPI;
import com.akasoft.poneyrox.api.alphavantage.AlphaVantageIntradayInterval;
import com.akasoft.poneyrox.core.strategies.SimpleForexStrategy;
import com.akasoft.poneyrox.exceptions.ImportException;
import eu.verdelhan.ta4j.TimeSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     *  Constructeur.
     *  @param alphaVantageAPI API AlphaVantage.
     *  @param singleRunController Controleur des lancements unitaires.
     */
    public ContinuousRunController(
            @Autowired AlphaVantageAPI alphaVantageAPI,
            @Autowired SingleRunController singleRunController) {
        this.singleRunController = singleRunController;
        this.alphaVantageAPI = alphaVantageAPI;
    }

    /**
     *  Exécution.
     *  @throws ImportException En cas d'erreur pendant l'import.
     */
    @RequestMapping("foo")
    public void execute() throws ImportException {
        /* Chargement des données */
        TimeSeries series = this.alphaVantageAPI.getIntradaySeries("EURUSD", AlphaVantageIntradayInterval.MINUTES_5);

        /* Création de la stratégie */
        SimpleForexStrategy strategy = new SimpleForexStrategy(series);
        strategy.addClosePriceProjection(1);
        strategy.addClosePriceProjection(2);
        strategy.addClosePriceProjection(5);

        /* Lancement */
        this.singleRunController.execute(strategy);
    }
}
