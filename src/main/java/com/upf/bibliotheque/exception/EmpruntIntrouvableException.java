package com.upf.bibliotheque.exception;

/** Exception levée lorsqu'un emprunt demandé n'existe pas. */
public class EmpruntIntrouvableException extends Exception {
    public EmpruntIntrouvableException(String message) {
        super(message);
    }
}