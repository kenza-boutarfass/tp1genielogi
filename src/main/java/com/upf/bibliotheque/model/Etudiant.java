package com.upf.bibliotheque.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/** Représente un étudiant inscrit à la bibliothèque. */
public class Etudiant {
    private static final Pattern FORMAT_EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private int id;
    private String nom;
    private String prenom;
    private String email;
    private List<Emprunt> emprunts;

    /** Crée un étudiant sans emprunt. */
    public Etudiant(int id, String nom, String prenom, String email) {
        setId(id);
        setNom(nom);
        setPrenom(prenom);
        setEmail(email);
        this.emprunts = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) {
        if (id <= 0) throw new IllegalArgumentException("L'identifiant doit être positif.");
        this.id = id;
    }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = texteObligatoire(nom, "Le nom"); }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = texteObligatoire(prenom, "Le prénom"); }
    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || !FORMAT_EMAIL.matcher(email.trim()).matches()) {
            throw new IllegalArgumentException("L'adresse e-mail n'est pas valide.");
        }
        this.email = email.trim();
    }
    public List<Emprunt> getEmprunts() { return Collections.unmodifiableList(emprunts); }
    public void setEmprunts(List<Emprunt> emprunts) {
        this.emprunts = emprunts == null ? new ArrayList<>() : new ArrayList<>(emprunts);
    }

    /** Retourne les informations principales de l'étudiant. */
    public String consulterInformations() {
        return "Étudiant " + id + " : " + prenom + " " + nom + " (" + email + ")";
    }

    /** Retourne les emprunts enregistrés pour cet étudiant. */
    public List<Emprunt> consulterEmprunts() { return getEmprunts(); }

    /** Associe un nouvel emprunt à l'étudiant. */
    public void ajouterEmprunt(Emprunt emprunt) {
        if (emprunt == null) throw new IllegalArgumentException("L'emprunt est obligatoire.");
        emprunts.add(emprunt);
    }

    /** Retourne le livre correspondant à un emprunt en cours de cet étudiant. */
    public void retournerLivre(Livre livre) {
        Emprunt emprunt = emprunts.stream()
                .filter(item -> item.getLivre().equals(livre) && item.getStatut() != StatutEmprunt.RETOURNE)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Aucun emprunt en cours pour ce livre."));
        emprunt.terminer();
        livre.retourner();
    }

    /** Retourne les emprunts qui ne sont pas encore terminés. */
    public List<Emprunt> getEmpruntsEnCours() {
        return emprunts.stream().filter(emprunt -> emprunt.getStatut() != StatutEmprunt.RETOURNE).toList();
    }

    private String texteObligatoire(String valeur, String champ) {
        if (valeur == null || valeur.isBlank()) throw new IllegalArgumentException(champ + " est obligatoire.");
        return valeur.trim();
    }

    @Override
    public String toString() { return consulterInformations(); }
}