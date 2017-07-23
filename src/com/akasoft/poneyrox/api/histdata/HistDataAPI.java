package com.akasoft.poneyrox.api.histdata;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 *  API HistData.
 *  Controleur en charge de la lecture de données générique issues de l'API HistData.
 *  Voir : http://www.histdata.com/
 */
@Controller
public class HistDataAPI {
    /**
     *  Contexte de la page.
     */
    private final ServletContext context;

    /**
     *  Constructeur.
     *  @param context Contexte de la servlet.
     */
    public HistDataAPI(@Autowired ServletContext context) {
        this.context = context;
    }

    /**
     *  Chargement d'un fichier unitaire.
     *  @param path Chemin relatif du fichier.
     *  @return Série correspondante.
     *  @throws FileNotFoundException En cas de fichier introuvable.
     *  @throws ParseException En cas d'erreur de parsing.
     */
    public TimeSeries load(String path) throws FileNotFoundException, ParseException {
        /* Création des taux */
        List<Tick> ticks = new ArrayList<>();

        /* Création du parseur de date */
        DateFormat df = new SimpleDateFormat("YYYYMMdd HHmmss");

        /* Lecture du fichier */
        FileReader fr = new FileReader(this.context.getRealPath(path));
        BufferedReader br = new BufferedReader(fr);
        for (String[] split : br.lines().map(e -> e.split(";")).collect(Collectors.toList())) {
            Tick tick = new Tick(
                    new DateTime(df.parse(split[0])),
                    Decimal.valueOf(split[1]),
                    Decimal.valueOf(split[2]),
                    Decimal.valueOf(split[3]),
                    Decimal.valueOf(split[4]),
                    Decimal.valueOf(split[5]));
            ticks.add(tick);
        }

        /* Renvoi */
        return new TimeSeries(path, ticks);
    }
}
