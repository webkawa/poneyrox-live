package com.akasoft.poneyrox.core.watch.cells;

import com.akasoft.poneyrox.core.watch.curves.AbstractCurve;
import com.akasoft.poneyrox.core.watch.rates.AbstractRate;

import java.util.Objects;

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
    private long start;

    /**
     *  Date intermédiaire.
     *  Point situé à mi-chemin entre la date de début et la date de fin. Exprimée en temps UNIX.
     */
    private long middle;

    /**
     *  Date de fin.
     *  Exprimée en temps UNIX.
     */
    private long end;

    /**
     *  Taux de l'offre.
     */
    private TRate bid;

    /**
     *  Taux de la demande.
     */
    private TRate ask;

    /**
     *  Constructeur.
     *  @param owner Courbe propriétaire.
     *  @param start Date de départ.
     */
    protected AbstractCell(AbstractCurve owner, long start, TRate bid, TRate ask) {
        this.start = start;
        this.middle = start + (owner.getInterval() / 2);
        this.end = start + owner.getInterval();
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
