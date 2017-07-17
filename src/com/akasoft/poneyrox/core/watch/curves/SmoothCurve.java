package com.akasoft.poneyrox.core.watch.curves;

import com.akasoft.poneyrox.core.watch.cells.RawCell;
import com.akasoft.poneyrox.core.watch.cells.SmoothCell;
import com.akasoft.poneyrox.exceptions.CurveException;

import java.util.HashSet;
import java.util.Set;

/**
 *  Courbe lissée.
 *  Courbe représentative des taux relevés sur un marché donné impliquant un facteur de lissage
 *  exprimé en nombre de cellules.
 */
public class SmoothCurve extends AbstractCurve<SmoothCell> {
    /**
     *  Constructeur.
     *  @param market Marché rattaché.
     *  @param interval Intervalle des cellules en secondes.
     *  @param smooth Niveau de lissage.
     *  @param source Courbe d'origine.
     *  @throws CurveException En cas d'erreur lors de la génération de la courbe.
     */
    protected SmoothCurve(String market, long interval, int smooth, RawCurve source) throws CurveException {
        super(market, interval, smooth);

        /* Vérification du facteur de lissage */
        if (smooth < 2) {
            throw new CurveException("Invalid smooth rate %d", smooth);
        }

        /* Récupération des cellules */
        for (int position = 0; position < source.getCellsSize() - smooth + 1; position++) {
            /* Récupération de la cellule principal */
            RawCell main = source.getCell(position);

            /* Récupération des cellules ciblées */
            Set<RawCell> cells = new HashSet<>();
            for (int i = 0; i < smooth; i++) {
                cells.add(source.getCell(position + i));
            }

            /* Création de la cellule */
            new SmoothCell(main.getStart(), main.getEnd(), cells);
        }
    }

    /**
     *  Référence une cellule manquante.
     *  Dans le cas d'une courbe lissée, la fonction de ne doit en principe jamais etre appelée.
     *  @param place Position de la cellule manquante.
     *  @param righPosition Position de la pré-existante la plus proche à droite.
     *                      Si aucune cellule disponible à droite, égal à -1.
     *  @param rightCell Cellule pré-existante la plus proche à droite.
     *                   Nul si aucune cellule pré-existante.
     *  @param leftPosition Position de la cellule pré-existante la plus proche à gauche.
     *  @param leftCell Cellule pré-existante à gauche.
     *  @throws CurveException
     */
    @Override
    protected void addMissing(int place, int righPosition, SmoothCell rightCell, int leftPosition, SmoothCell leftCell) throws CurveException {
        throw new CurveException("Detected missing cell on smooth curve...");
    }
}
