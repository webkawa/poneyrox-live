package com.akasoft.poneyrox.core.watch.curves;

import com.akasoft.poneyrox.core.watch.cells.AbstractCell;
import com.akasoft.poneyrox.entities.MarketEntity;
import com.akasoft.poneyrox.exceptions.CurveException;

import java.util.*;

/**
 *  Courbe.
 *  Classe descriptive d'une courbe brute ou lissée applicable à un marché donné.
 *  @param <TCell> Type de cellule portée.
 */
public abstract class AbstractCurve<TCell extends AbstractCell> {
    /**
     *  Date d'actualisation de la courbe.
     *  Exprimé en temps UNIX.
     */
    private final long date;

    /**
     *  Clef d'accès au marché rattaché.
     */
    private final String market;

    /**
     *  Intervalle des cellules.
     */
    private final long interval;

    /**
     *  Niveau de lissage.
     *  Egal à 1 pour une courbe brute et à une valeur supérieure à 1 pour une
     *  courbe lissée.
     */
    private final int smooth;

    /**
     *  Liste des cellules.
     *  Les cellules sont, par convention, classées en fonction de la distance les séparant de l'instant courant
     *  (0 pour la cellule la plus proche, 1 pour la précédente, etc.).
     */
    private final Map<Integer, TCell> cells;

    /**
     *  Constructeur.
     *  @param market Marché rattaché.
     *  @param interval Intervalle des cellules exprimé en secondes.
     *  @param smooth Niveau de lissage.
     *  @throws CurveException En cas d'erreur lors de la génération de la courbe.
     */
    protected AbstractCurve(String market, long interval, int smooth) throws CurveException {
        /* Vérification de l'intervalle */
        if (interval < 1) {
            throw new CurveException("Invalid curve interval %d", interval);
        }

        /* Affectation */
        this.date = Calendar.getInstance().getTimeInMillis() / 1000;
        this.market = market;
        this.interval = interval;
        this.smooth = smooth;
        this.cells = new HashMap<>();
    }

    /**
     *  Retourne la date d'actualisation de la courbe.
     *  @return Date d'actualisation.
     */
    public long getDate() {
        return this.date;
    }

    /**
     *  Retourne la clef d'accès au marché.
     *  @return Clef d'accès au marché.
     */
    public String getMarket() {
        return this.market;
    }

    /**
     *  Retourne l'intervalle des cellules.
     *  @return Intervalle des cellules.
     */
    public long getInterval() {
        return this.interval;
    }

    /**
     *  Retourne le niveau de lissage.
     *  @return Niveau de lissage.
     */
    public int getSmooth() {
        return this.smooth;
    }

    /**
     *  Retourne le nombre de cellules contenues dans la courbe.
     *  @return Nombre de cellules disponibles.
     */
    public int getCellsSize() {
        return this.cells.size();
    }

    /**
     *  Indique si la courbe contient un élément à une position donnée.
     *  @param position Position recherchée.
     *  @return true si la courbe contient un élément, false sinon.
     */
    public boolean hasCell(int position) {
        return this.cells.containsKey(position);
    }

    /**
     *  Retourne la cellule située à une position passée en paramètre.
     *  @param position Position recherchée.
     *  @return Cellule correspondante.
     */
    public TCell getCell(int position) {
        return this.cells.get(position);
    }

    /**
     *  Retourne la clef de hachage rattachée à la courbe.
     *  @return Clef de hachage.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                this.market,
                this.interval,
                this.smooth,
                this.cells);
    }

    /**
     *  Complète la courbe.
     *  @throws CurveException En cas d'erreur lors de la complétion de la courbe.
     */
    public void complete() throws CurveException {
        /* Détermination de la distance maximale */
        int distance = this.cells
                .keySet()
                .stream()
                .mapToInt(e -> e)
                .max()
                .getAsInt();

        /* Complétion des cellules manquantes.
         * Dans le cas d'une courbe lissée, aucune cellule ne doit manquer par défaut. La recherche des cellules
         * est toutefois réalisée par mesure de vérification. */
        for (int i = 0; i <= distance; i++) {
            /* Vérification d'existance */
            if (!this.cells.containsKey(i)) {
                /* Création des variables utiles */
                int leftPosition = i + 1;
                TCell leftCell = null;
                int rightPosition = i - 1;
                TCell rightCell = null;

                /* Recherche de la cellule à gauche */
                while (leftPosition <= distance && leftCell == null) {
                    if (this.cells.containsKey(leftPosition)) {
                        leftCell = this.cells.get(leftPosition);
                    } else {
                        leftPosition++;
                    }
                }

                /* Recherche de la cellule à droite */
                while (rightPosition >= -1 && rightCell == null) {
                    if (this.cells.containsKey(rightPosition)) {
                        rightCell = this.cells.get(rightPosition);
                    } else {
                        rightPosition--;
                    }
                }

                /* Création de la cellule manquante */
                this.addMissing(i, rightPosition, rightCell, leftPosition, leftCell);
            }
        }

        /* Compétion des marges */
        for (int i = distance - 1; i <= 0; i--) {
            this.cells.get(i).computeMargins(this.cells.get(i + 1));
        }

        /* Complétion des opposés et courbes d'avancement */
        List<AbstractCell> buffer = new ArrayList<>();
        buffer.add(this.cells.get(distance));
        for (int i = distance - 1; i <= 0; i--) {
            AbstractCell cell = this.cells.get(i);
            cell.computeOppositions(buffer);
            cell.computeForwards(buffer);
            buffer.add(cell);
        }
    }

    /**
     *  Ajoute une cellule à la liste.
     *  @param position Position de la cellule.
     *  @param cell Cellule.
     */
    protected void addCell(int position, TCell cell) {
        this.cells.put(position, cell);
    }

    /**
     *  Ajoute une cellule manquante lors de la phase de consolidation.
     *  @param place Position de la cellule manquante.
     *  @param leftPosition Position de la cellule pré-existante la plus proche à gauche.
     *  @param leftCell Cellule pré-existante à gauche.
     *  @param righPosition Position de la pré-existante la plus proche à droite.
     *                      Si aucune cellule disponible à droite, égal à -1.
     *  @param rightCell Cellule pré-existante la plus proche à droite.
     *                   Nul si aucune cellule pré-existante.
     *  @throws CurveException En cas d'erreur lors de la génération de la cellule.
     */
    protected abstract void addMissing(int place, int righPosition, TCell rightCell, int leftPosition, TCell leftCell) throws CurveException;
}
