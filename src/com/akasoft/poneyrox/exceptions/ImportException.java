package com.akasoft.poneyrox.exceptions;

/**
 *  Erreur d'import.
 *  Erreur liée à l'import de données dans le système.
 */
public class ImportException extends AbstractException {
    /**
     *  Constructeur simple.
     *  @param message Message d'erreur.
     *  @param format Arguments de formatage.
     */
    public ImportException(String message, Object... format) {
        super(message, format);
    }

    /**
     *  Constructeur à cause.
     *  @param cause Cause de l'erreur.
     *  @param message Message d'erreur.
     *  @param format Arguments de formatage.
     */
    public ImportException(Throwable cause, String message, Object... format) {
        super(cause, message, format);
    }
}
