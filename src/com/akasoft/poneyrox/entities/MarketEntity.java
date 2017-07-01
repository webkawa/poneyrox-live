package com.akasoft.poneyrox.entities;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

/**
 *  Marché.
 *  Entité représentative d'un marché analysable par l'application.
 */
@Entity
public class MarketEntity extends AbstractEntity {
    /**
     *  Libellé du marché.
     */
    private String label;

    /**
     *  Clef d'accès.
     */
    private String key;

    /**
     *  Liste des taux.
     */
    @OneToMany(mappedBy = "market")
    private Set<RateEntity> rates;

    /**
     *  Retourne le libellé du marché.
     *  @return Libellé du marché.
     */
    public String getLabel() {
        return this.label;
    }

    /**
     *  Retourne la clef d'accès au marché.
     *  @return Clef d'accès.
     */
    public String getKey() {
        return this.key;
    }

    /**
     *  Retourne la liste des taux rattachés.
     *  @return Liste des taux rattachés.
     */
    public Set<RateEntity> getRates() {
        return this.rates;
    }

    /**
     *  Définit le libellé du marché.
     *  @param label Libellé du marché.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     *  Définit la clef du marché.
     *  @param key Clef du marché.
     */
    public void setKey(String key) {
        this.key = key;
    }
}
