package com.akasoft.poneyrox.exceptions;

/**
 *  Erreur de données.
 *  Erreur consécutive à la levée d'une anomalie dans les données persistantes.
 */
public class DataException extends AbstractException {
    /**
     *  Constructeur.
     *  @param message Message d'erreur.
     *  @param format Arguments de formatage.
     */
    public DataException(String message, Object... format) {
        super(message, format);
    }
}
