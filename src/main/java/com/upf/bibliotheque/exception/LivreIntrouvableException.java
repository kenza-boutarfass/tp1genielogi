package com.upf.bibliotheque.exception;

/** Exception levée lorsqu'un livre demandé n'existe pas. */
public class LivreIntrouvableException extends Exception {
    public LivreIntrouvableException(String message) {
        super(message);
    }
}