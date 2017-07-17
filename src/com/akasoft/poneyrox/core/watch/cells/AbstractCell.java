package com.akasoft.poneyrox.core.watch.cells;

import com.akasoft.poneyrox.core.watch.curves.AbstractCurve;
import com.akasoft.poneyrox.core.watch.rates.AbstractRate;
import com.akasoft.poneyrox.core.watch.rates.AbstractRateType;
import com.akasoft.poneyrox.exceptions.CurveException;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *  Cellule.
 *  Classe représentative d'une division unitaire inscrite au sein d'une courbe.
 *  @param <TRate> Type de taux traité.
 */
public abstract class AbstractCell<TRate extends AbstractRate> {
    /**
     *  Date de départ.
     *  Exprimée en temps UNIX.
     */
    private final long start;

    /**
     *  Date intermédiaire.
     *  Point situé à mi-chemin entre la date de début et la date de fin. Exprimée en temps UNIX.
     */
    private final long middle;

    /**
     *  Date de fin.
     *  Exprimée en temps UNIX.
     */
    private final long end;

    /**
     *  Taux de l'offre.
     */
    private final TRate bid;

    /**
     *  Taux de la demande.
     */
    private final TRate ask;

    /**
     *  Constructeur.
     *  @param start Date de départ.
     *  @param end Date de fin.
     */
    protected AbstractCell(long start, long end, TRate bid, TRate ask) {
        this.start = start;
        this.middle = start + ((end - start) / 2);
        this.end = end;
        this.bid = bid;
        this.ask = ask;
    }

    /**
     *  Retourne la date de début.
     *  @return Date de début.
     */
    public long getStart() {
        return this.start;
    }

    /**
     *  Retourne la date intermédiaire.
     *  @return Date intermédiaire.
     */
    public long getMiddle() {
        return this.middle;
    }

    /**
     *  Retourne la date de fin.
     *  @return Date de fin.
     */
    public long getEnd() {
        return this.end;
    }

    /**
     *  Retourne le taux de l'offre.
     *  @return Taux de l'offre.
     */
    public TRate getBid() {
        return this.bid;
    }

    /**
     *  Retourne le taux de la demande.
     *  @return Taux de la demande.
     */
    public TRate getAsk() {
        return this.ask;
    }

    /**
     *  Retourne un taux par type.
     *  @param type Type recherché.
     *  @return Taux correspondant.
     *  @throws CurveException En cas de taux invalide.
     */
    public TRate getRateByType(AbstractRateType type) throws CurveException {
        switch (type) {
            case BID:
                return this.bid;
            case ASK:
                return this.ask;
        }
        throw new CurveException("Invalid rate type %s", type);
    }

    /**
     *  Réalise le calcul des marges en comparaison de la cellule précédente.
     *  @param previous Cellule précédente.
     */
    public void computeMargins(AbstractCell previous) {
        this.bid.computeMargins(previous.getBid());
        this.ask.computeMargins(previous.getAsk());
    }

    /**
     *  Réalise le calcul des oppositions en comparaison des cellules précédentes.
     *  @param previous Liste des cellules précédentes.
     */
    public void computeOppositions(List<AbstractCell> previous) {

    }

    /**
     *  Réalise le calcul des courbes d'avancement en comparaison des cellules précédentes.
     *  @param previous Liste des cellules précédentes.
     */
    public void computeForwards(List<AbstractCell> previous) {

    }

    /**
     *  Retourne la clef de hachage rattachée à la cellule.
     *  @return Clef de hachage.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                this.start,
                this.end,
                this.bid,
                this.ask);
    }
}
