package com.upf.bibliotheque.service;

import com.upf.bibliotheque.exception.EmpruntIntrouvableException;
import com.upf.bibliotheque.exception.EtudiantIntrouvableException;
import com.upf.bibliotheque.exception.LimiteEmpruntDepasseeException;
import com.upf.bibliotheque.exception.LivreIndisponibleException;
import com.upf.bibliotheque.exception.LivreIntrouvableException;
import com.upf.bibliotheque.model.Emprunt;
import com.upf.bibliotheque.model.Etudiant;
import com.upf.bibliotheque.model.Livre;
import com.upf.bibliotheque.model.StatutEmprunt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/** Centralise la gestion en mémoire des livres, étudiants et emprunts. */
public class Bibliotheque {
    private static final int LIMITE_EMPRUNTS = 3;

    private final List<Livre> livres = new ArrayList<>();
    private final List<Etudiant> etudiants = new ArrayList<>();
    private final List<Emprunt> emprunts = new ArrayList<>();
    private int compteurIdEmprunt = 1;

    public List<Livre> getLivres() { return Collections.unmodifiableList(livres); }
    public List<Etudiant> getEtudiants() { return Collections.unmodifiableList(etudiants); }
    public List<Emprunt> getEmprunts() { return Collections.unmodifiableList(emprunts); }

    /** Ajoute un livre si son identifiant n'est pas déjà utilisé. */
    public void ajouterLivre(Livre livre) {
        if (livre == null) throw new IllegalArgumentException("Le livre est obligatoire.");
        if (trouverDansListe(livres, livre.getId()) != null) throw new IllegalArgumentException("Un livre possède déjà cet identifiant.");
        livres.add(livre);
    }

    /** Remplace les informations modifiables d'un livre existant. */
    public void modifierLivre(Livre livreModifie) throws LivreIntrouvableException {
        Livre livre = trouverLivreParId(livreModifie.getId());
        livre.setTitre(livreModifie.getTitre());
        livre.setAuteur(livreModifie.getAuteur());
        livre.setCategorie(livreModifie.getCategorie());
        livre.setNombreExemplaires(livreModifie.getNombreExemplaires());
    }

    /** Supprime un livre qui n'est pas actuellement emprunté. */
    public void supprimerLivre(int id) throws LivreIntrouvableException {
        Livre livre = trouverLivreParId(id);
        boolean empruntActif = emprunts.stream()
                .anyMatch(emprunt -> emprunt.getLivre().equals(livre) && emprunt.getStatut() != StatutEmprunt.RETOURNE);
        if (empruntActif) throw new IllegalStateException("Impossible de supprimer un livre actuellement emprunté.");
        livres.remove(livre);
    }

    /** Recherche les livres par titre, auteur ou catégorie sans tenir compte de la casse. */
    public List<Livre> rechercherLivre(String recherche) {
        if (recherche == null || recherche.isBlank()) return List.of();
        String terme = recherche.trim().toLowerCase(Locale.ROOT);
        return livres.stream()
                .filter(livre -> livre.getTitre().toLowerCase(Locale.ROOT).contains(terme)
                        || livre.getAuteur().toLowerCase(Locale.ROOT).contains(terme)
                        || livre.getCategorie().toLowerCase(Locale.ROOT).contains(terme))
                .toList();
    }

    /** Recherche un livre par son identifiant. */
    public Livre trouverLivreParId(int id) throws LivreIntrouvableException {
        return livres.stream().filter(livre -> livre.getId() == id).findFirst()
                .orElseThrow(() -> new LivreIntrouvableException("Livre introuvable (id " + id + ")."));
    }

    /** Ajoute un étudiant si son identifiant n'est pas déjà utilisé. */
    public void ajouterEtudiant(Etudiant etudiant) {
        if (etudiant == null) throw new IllegalArgumentException("L'étudiant est obligatoire.");
        if (trouverDansListe(etudiants, etudiant.getId()) != null) throw new IllegalArgumentException("Un étudiant possède déjà cet identifiant.");
        etudiants.add(etudiant);
    }

    /** Met à jour les informations d'un étudiant existant. */
    public void modifierEtudiant(Etudiant etudiantModifie) throws EtudiantIntrouvableException {
        Etudiant etudiant = trouverEtudiantParId(etudiantModifie.getId());
        etudiant.setNom(etudiantModifie.getNom());
        etudiant.setPrenom(etudiantModifie.getPrenom());
        etudiant.setEmail(etudiantModifie.getEmail());
    }

    /** Recherche un étudiant par son identifiant. */
    public Etudiant trouverEtudiantParId(int id) throws EtudiantIntrouvableException {
        return etudiants.stream().filter(etudiant -> etudiant.getId() == id).findFirst()
                .orElseThrow(() -> new EtudiantIntrouvableException("Étudiant introuvable (id " + id + ")."));
    }

    /** Enregistre un emprunt après vérification de l'étudiant, du livre et des limites. */
    public Emprunt emprunterLivre(int idEtudiant, int idLivre)
            throws EtudiantIntrouvableException, LivreIntrouvableException,
            LivreIndisponibleException, LimiteEmpruntDepasseeException {
        Etudiant etudiant = trouverEtudiantParId(idEtudiant);
        Livre livre = trouverLivreParId(idLivre);
        if (etudiant.getEmpruntsEnCours().size() >= LIMITE_EMPRUNTS) {
            throw new LimiteEmpruntDepasseeException("L'étudiant a déjà atteint la limite de " + LIMITE_EMPRUNTS + " emprunts en cours.");
        }
        livre.emprunter();
        Emprunt emprunt = new Emprunt(compteurIdEmprunt++, livre, etudiant);
        etudiant.ajouterEmprunt(emprunt);
        emprunts.add(emprunt);
        return emprunt;
    }

    /** Termine un emprunt et restitue l'exemplaire au stock disponible. */
    public void retournerLivre(int idEmprunt) throws EmpruntIntrouvableException {
        Emprunt emprunt = emprunts.stream().filter(item -> item.getId() == idEmprunt).findFirst()
                .orElseThrow(() -> new EmpruntIntrouvableException("Emprunt introuvable (id " + idEmprunt + ")."));
        if (emprunt.getStatut() == StatutEmprunt.RETOURNE) throw new IllegalStateException("Cet emprunt est déjà terminé.");
        emprunt.terminer();
        emprunt.getLivre().retourner();
    }

    /** Retourne les livres ayant au moins un exemplaire disponible. */
    public List<Livre> afficherLivresDisponibles() {
        return livres.stream().filter(Livre::estDisponible).toList();
    }

    /** Retourne tous les emprunts enregistrés. */
    public List<Emprunt> consulterEmprunts() { return getEmprunts(); }

    /** Retourne les emprunts non terminés, y compris ceux en retard. */
    public List<Emprunt> afficherEmpruntsEnCours() {
        return emprunts.stream().filter(emprunt -> emprunt.getStatut() != StatutEmprunt.RETOURNE).toList();
    }

    private <T> T trouverDansListe(List<T> elements, int id) {
        return elements.stream().filter(element -> {
            if (element instanceof Livre livre) return livre.getId() == id;
            if (element instanceof Etudiant etudiant) return etudiant.getId() == id;
            return false;
        }).findFirst().orElse(null);
    }
}