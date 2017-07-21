package com.akasoft.poneyrox.api.alphavantage;

import com.akasoft.poneyrox.exceptions.ImportException;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *  Accès à l'API AlphaVantage.
 *  Classe permettant la récupération des données depuis l'API AlphaVantage.
 */
@Controller
public class AlphaVantageAPI {
    /**
     *  Clef d'accès à l'API.
     */
    public static final String API_KEY = "JAQDZA4QAHI6QGZX";

    /**
     *  Retourne une série représentative d'une liste de taux intra-journaliers mis à disposition par l'API.
     *  @param symbol Symbole recherché.
     *  @param interval Intervalle de temps.
     *  @return Série représentative du taux recherché.
     *  @throws ImportException En cas d'erreur lors de l'import.
     */
    public TimeSeries getIntradaySeries(String symbol, AlphaVantageIntradayInterval interval) throws ImportException {
        /* Extraction de la période littérale */
        String period = "1min";
        switch (interval) {
            case MINUTES_1:
                period = "1min";
                break;
            case MINUTES_5:
                period = "5min";
                break;
            case MINUTES_15:
                period = "15min";
                break;
            case MINUTES_30:
                period = "30min";
                break;
            case MINUTES_60:
                period = "60min";
                break;
        }

        /* Création du chemin */
        String path = String.format(
                "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=%s&interval=%s&outputsize=full&apikey=%s",
                symbol,
                period,
                AlphaVantageAPI.API_KEY);

        /**
         *  Appel HTTP.
         */
        GetRequest request = Unirest.get(path);
        try {
            /* Récupération du flux JSON */
            JSONObject root = request.asJson().getBody().getObject();

            /* Récupération de l'objet porteur des taux */
            String dataKey = root.keySet().stream().filter(key -> !key.equals("Meta Data")).findFirst().get();
            JSONObject dataRoot = root.getJSONObject(dataKey);
            Iterator<String> dataDates = dataRoot.keys();

            /* Création de l'objet de formatage */
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            /* Création des taux */
            List<Tick> ticks = new ArrayList<>();
            while (dataDates.hasNext()) {
                /* Extraction de la date litérale */
                String date = dataDates.next();

                /* Extraction de la donnée brute */
                JSONObject data = dataRoot.getJSONObject(date);

                /* Création du taux */
                Tick tick = new Tick(
                        new DateTime(sdf.parse(date)),
                        data.getDouble("1. open"),
                        data.getDouble("2. high"),
                        data.getDouble("3. low"),
                        data.getDouble("4. close"),
                        data.getDouble("5. volume"));
                ticks.add(tick);
            }

            /* Renvoi */
            return new TimeSeries(
                    String.format("AlphaVantage-%s", symbol),
                    ticks);
        } catch (UnirestException ex) {
            throw new ImportException(ex, "Failed to process URL '%s' from AlphaVantage API", path);
        } catch (ParseException ex) {
            throw new ImportException(ex, "Failed to parse date on '%s'", path);
        }
    }
}
