package com.upf.bibliotheque.exception;

/** Exception levée lorsqu'un étudiant atteint la limite d'emprunts autorisée. */
public class LimiteEmpruntDepasseeException extends Exception {
    public LimiteEmpruntDepasseeException(String message) {
        super(message);
    }
}