package com.akasoft.poneyrox.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 *  Entité.
 *  Classe abstraite représentative d'une entité persistente.
 */
@MappedSuperclass
public abstract class AbstractEntity {
    /**
     *  Identifiant.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     *  Retourne l'identifiant.
     *  @return Identifiant de l'entité.
     */
    public UUID getId() {
        return this.id;
    }
}
