package com.akasoft.poneyrox.entities;

import javax.persistence.*;

/**
 *  Marché.
 *  Entité représentative d'un marché unitaire.
 */
@Entity
@NamedQueries(
        @NamedQuery(
                name = "Market.getByKey",
                query = "SELECT market " +
                        "FROM MarketEntity AS market " +
                        "WHERE market.key = :key"
        )
)
public class MarketEntity extends AbstractEntity {
    /**
     *  Clef d'accès au marché.
     */
    @Column(name = "market_key")
    private String key;

    /**
     *  Retourne la clef d'accès.
     *  @return Clef d'accès au marché.
     */
    public String getKey() {
        return this.key;
    }

    /**
     *  Définit la clef d'accès.
     *  @param key Clef d'accès.
     */
    public void setKey(String key) {
        this.key = key;
    }
}
