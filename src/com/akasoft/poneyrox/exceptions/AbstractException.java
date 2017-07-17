package com.akasoft.poneyrox.exceptions;

/**
 *  Exception.
 *  Classe abstraite descriptive d'une exception générée par le système.
 */
public abstract class AbstractException extends Exception {
    /**
     *  Constructeur simple.
     *  @param message Message d'erreur.
     *  @param format Arguments de formatage.
     */
    public AbstractException(String message, Object... format) {
        super(String.format(message, format));
    }

    /**
     *  Constructeur à charge.
     *  @param cause Cause de l'erreur.
     *  @param message Message d'erreur.
     *  @param format Arguments de formatage.
     */
    public AbstractException(Throwable cause, String message, Object... format) {
        super(String.format(message, format), cause);
    }
}
