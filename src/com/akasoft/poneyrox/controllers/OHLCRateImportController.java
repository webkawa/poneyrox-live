package com.akasoft.poneyrox.controllers;

import com.akasoft.poneyrox.dao.ChartDAO;
import com.akasoft.poneyrox.dao.MarketDAO;
import com.akasoft.poneyrox.dao.OHLCRateDAO;
import com.akasoft.poneyrox.entities.ChartEntity;
import com.akasoft.poneyrox.entities.MarketEntity;
import com.akasoft.poneyrox.entities.OHLCRateEntity;
import com.akasoft.poneyrox.entities.RateEntity;
import com.akasoft.poneyrox.exceptions.DataException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.CharSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.error.Mark;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.stream.DoubleStream;

/**
 *  Importation des taux OHLC.
 *  Controleur dédié à l'importation d'une liste de taux OHLC à partir d'un ou plusieurs fichiers préalablement
 *  placés dans le dossier "WEB-INF/data/{market}/{period}" et formatés selon la norme générique du fournisseur
 *  http://www.histdata.com/.
 */
@RestController
public class OHLCRateImportController {
    /**
     *  Contexte du controleur.
     */
    private ServletContext context;

    /**
     *  DAO des marchés.
     */
    private MarketDAO marketDAO;

    /**
     *  DAO des relevés.
     */
    private ChartDAO chartDAO;

    /**
     *  DAO des taux OHLC.
     */
    private OHLCRateDAO OHLCRateDAO;

    /**
     *  Constructeur.
     *  @param context Contexte du controleur.
     *  @param marketDAO DAO des marchés.
     *  @param chartDAO DAO des relevés.
     *  @param OHLCRateDAO DAO des taux OHLC.
     */
    public OHLCRateImportController(
            @Autowired ServletContext context,
            @Autowired MarketDAO marketDAO,
            @Autowired ChartDAO chartDAO,
            @Autowired OHLCRateDAO OHLCRateDAO) {
        this.context = context;
        this.marketDAO = marketDAO;
        this.chartDAO = chartDAO;
        this.OHLCRateDAO = OHLCRateDAO;
    }

    /**
     *  Exécution.
     *  @param key Clef d'accès au marché ciblé.
     *  @param period Périodicité des relevés.
     *  @return Résultat des opérations.
     *  @throws DataException En cas d'erreur lors de l'import des données.
     */
    @RequestMapping("import/ohlc/{key}/{period}")
    public List<String> execute(@PathVariable String key, @PathVariable long period) throws DataException {
        /* Création du résultat */
        List<String> result = new ArrayList<>();

        /* Récupération du marché et du relevé */
        MarketEntity market = this.marketDAO.retrieveOrPersist(key.toUpperCase());
        ChartEntity chart = this.chartDAO.retrieveOrPersist(period, market);

        /* Préparation */
        List<Long> times = new ArrayList<Long>();
        List<Double[]> rates = new ArrayList<>();

        /* Parcours des fichiers */
        String relPath = String.format("/WEB-INF/data/ohlc/%s/%d", key, period);
        String absPath = this.context.getRealPath(relPath);
        for (File csv : new File(absPath).listFiles()) {
            try {
                /* Création du parser */
                CSVParser parser = CSVParser.parse(csv, Charset.forName("ASCII"), CSVFormat.newFormat(';'));

                /* Parcours */
                DateFormat format = new SimpleDateFormat("yyyyMMdd HHmmss");
                for (CSVRecord record : parser) {
                    /* Lecture de la date */
                    java.util.Date time = format.parse(record.get(0));
                    times.add(time.getTime());

                    /* Lecture des indicateurs */
                    Double[] rate = new Double[4];
                    rate[0] = Double.parseDouble(record.get(1));
                    rate[1] = Double.parseDouble(record.get(2));
                    rate[2] = Double.parseDouble(record.get(3));
                    rate[3] = Double.parseDouble(record.get(4));
                    rates.add(rate);
                }

                /* Renvoi */
                result.add(String.format("File '%s' integrated", csv.getAbsolutePath()));
            } catch (ParseException exception) {
                exception.printStackTrace();
                result.add(String.format("Failed to integrate file '%s' ; cause : %s", csv.getAbsolutePath(), exception.getMessage()));
            } catch (IOException exception) {
                result.add(String.format("File '%s' not readable", csv.getAbsolutePath()));
            }
        }

        /* Insertion */
        this.OHLCRateDAO.integrate(times, rates, chart);

        /* Renvoi */
        return result;
    }
}
