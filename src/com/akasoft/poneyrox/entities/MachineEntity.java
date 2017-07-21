package com.akasoft.poneyrox.entities;

import javax.persistence.Entity;

/**
 *  Deep-learning-machine.
 *  Entité représentative d'une machine évaluée à une ou plusieurs reprises par le système.
 */
@Entity
public class MachineEntity extends AbstractEntity {
    /**
     *  Libellé.
     */
    private String label;

    /**
     *  Retourne le libellé de la machine.
     *  @return Libellé de la machine.
     */
    public String getLabel() {
        return this.label;
    }

    /**
     *  Affecte le libellé de la machine.
     *  @param label Libellé de la machine.
     */
    public void setLabel(String label) {
        this.label = label;
    }
}
