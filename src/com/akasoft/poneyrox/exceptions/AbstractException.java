package com.akasoft.poneyrox.exceptions;

/**
 *  Erreur de l'application.
 *  Classe d'erreur abstraite regroupant les erreurs projetées par l'application.
 */
public abstract class AbstractException extends Exception {
    /**
     *  Constructeur simple.
     *  @param message Message d'erreur.
     *  @param params Liste des paramètres de formatage.
     */
    protected AbstractException(String message, Object... params) {
        super(String.format(message, params));
    }

    /**
     *  Constructeur à charge.
     *  @param cause Cause de l'erreur.
     *  @param message Message d'erreur.
     *  @param params Paramètres de formatage.
     */
    protected AbstractException(Throwable cause, String message, Object... params) {
        super(String.format(message, params), cause);
    }
}
