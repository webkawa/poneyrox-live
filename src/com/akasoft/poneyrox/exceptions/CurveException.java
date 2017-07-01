package com.akasoft.poneyrox.exceptions;

/**
 *  Erreur de courbe.
 *  Exception représentative d'un problème levé durant le calcul d'une courbe.
 */
public class CurveException extends AbstractException {
    /**
     *  Constructeur simple.
     *  @param message Message d'erreur.
     *  @param params Liste des paramètres de formatage.
     */
    public CurveException(String message, Object... params) {
        super(message, params);
    }

    /**
     *  Constructeur à charge.
     *  @param cause Cause de l'erreur.
     *  @param message Message d'erreur.
     *  @param params Liste des paramètres de formatage.
     */
    public CurveException(Throwable cause, String message, Object... params) {
        super(cause, message, params);
    }
}
