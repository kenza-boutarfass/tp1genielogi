package com.upf.bibliotheque.exception;

/** Exception levée lorsqu'aucun exemplaire du livre n'est disponible. */
public class LivreIndisponibleException extends Exception {
    public LivreIndisponibleException(String message) {
        super(message);
    }
}