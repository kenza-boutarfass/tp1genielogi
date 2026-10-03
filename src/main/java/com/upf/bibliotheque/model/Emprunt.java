package com.upf.bibliotheque.model;

import java.time.LocalDate;
import java.util.Objects;

/** Représente l'emprunt d'un livre par un étudiant. */
public class Emprunt {
    private final int id;
    private final Livre livre;
    private final Etudiant etudiant;
    private final LocalDate dateEmprunt;
    private final LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;
    private StatutEmprunt statut;

    /** Crée un emprunt avec un retour prévu dans quatorze jours. */
    public Emprunt(int id, Livre livre, Etudiant etudiant) {
        this(id, livre, etudiant, LocalDate.now(), LocalDate.now().plusDays(14));
    }

    /** Crée un emprunt avec des dates explicites. */
    public Emprunt(int id, Livre livre, Etudiant etudiant, LocalDate dateEmprunt, LocalDate dateRetourPrevue) {
        if (id <= 0) throw new IllegalArgumentException("L'identifiant doit être positif.");
        this.id = id;
        this.livre = Objects.requireNonNull(livre, "Le livre est obligatoire.");
        this.etudiant = Objects.requireNonNull(etudiant, "L'étudiant est obligatoire.");
        this.dateEmprunt = Objects.requireNonNull(dateEmprunt, "La date d'emprunt est obligatoire.");
        this.dateRetourPrevue = Objects.requireNonNull(dateRetourPrevue, "La date prévue est obligatoire.");
        if (dateRetourPrevue.isBefore(dateEmprunt)) throw new IllegalArgumentException("La date prévue ne peut pas précéder l'emprunt.");
        this.statut = StatutEmprunt.EN_COURS;
    }

    public int getId() { return id; }
    public Livre getLivre() { return livre; }
    public Etudiant getEtudiant() { return etudiant; }
    public LocalDate getDateEmprunt() { return dateEmprunt; }
    public LocalDate getDateRetourPrevue() { return dateRetourPrevue; }
    public LocalDate getDateRetourEffective() { return dateRetourEffective; }
    public StatutEmprunt getStatut() {
        return statut == StatutEmprunt.EN_COURS && estEnRetard() ? StatutEmprunt.EN_RETARD : statut;
    }

    /** Termine l'emprunt et mémorise sa date de retour. */
    public void terminer() {
        if (statut == StatutEmprunt.RETOURNE) throw new IllegalStateException("Cet emprunt est déjà terminé.");
        dateRetourEffective = LocalDate.now();
        statut = StatutEmprunt.RETOURNE;
    }

    /** Indique si la date prévue est dépassée pour un emprunt non terminé. */
    public boolean estEnRetard() {
        return statut != StatutEmprunt.RETOURNE && LocalDate.now().isAfter(dateRetourPrevue);
    }

    @Override
    public String toString() {
        return "Emprunt{id=" + id + ", livre='" + livre.getTitre() + "', étudiant='"
                + etudiant.getPrenom() + " " + etudiant.getNom() + "', dateEmprunt=" + dateEmprunt
                + ", dateRetourPrevue=" + dateRetourPrevue + ", dateRetourEffective="
                + dateRetourEffective + ", statut=" + getStatut() + "}";
    }
}