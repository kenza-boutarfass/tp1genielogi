package com.upf.bibliotheque.exception;

/** Exception levée lorsqu'un étudiant demandé n'existe pas. */
public class EtudiantIntrouvableException extends Exception {
    public EtudiantIntrouvableException(String message) {
        super(message);
    }
}