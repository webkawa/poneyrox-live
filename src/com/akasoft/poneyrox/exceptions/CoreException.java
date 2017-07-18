package com.akasoft.poneyrox.exceptions;

/**
 *  Erreur du noyau.
 *  Exception liée à une erreur interne aux objets métiers de l'application.
 */
public class CoreException extends AbstractException {
    /**
     *  Constructeur simple.
     *  @param message Message d'erreur.
     *  @param format Arguments de formatage.
     */
    public CoreException(String message, Object... format) {
        super(message, format);
    }

    /**
     *  Constructeur à charge.
     *  @param cause Cause de l'erreur.
     *  @param message Message d'erreur.
     *  @param format Arguments de formatage.
     */
    public CoreException(Throwable cause, String message, Object... format) {
        super(cause, message, format);
    }
}
