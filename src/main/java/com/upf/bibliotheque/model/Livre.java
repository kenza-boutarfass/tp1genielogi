package com.upf.bibliotheque.model;

import com.upf.bibliotheque.exception.LivreIndisponibleException;

/** Représente un livre et son nombre d'exemplaires actuellement disponibles. */
public class Livre {
    private int id;
    private String titre;
    private String auteur;
    private String categorie;
    private int nombreExemplaires;

    /** Crée un livre avec ses exemplaires disponibles. */
    public Livre(int id, String titre, String auteur, String categorie, int nombreExemplaires) {
        setId(id);
        setTitre(titre);
        setAuteur(auteur);
        setCategorie(categorie);
        setNombreExemplaires(nombreExemplaires);
    }

    public int getId() { return id; }
    public void setId(int id) {
        if (id <= 0) throw new IllegalArgumentException("L'identifiant doit être positif.");
        this.id = id;
    }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = texteObligatoire(titre, "Le titre"); }
    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = texteObligatoire(auteur, "L'auteur"); }
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = texteObligatoire(categorie, "La catégorie"); }
    public int getNombreExemplaires() { return nombreExemplaires; }
    public void setNombreExemplaires(int nombreExemplaires) {
        if (nombreExemplaires < 0) throw new IllegalArgumentException("Le nombre d'exemplaires ne peut pas être négatif.");
        this.nombreExemplaires = nombreExemplaires;
    }

    /** Ajoute un exemplaire disponible. */
    public void ajouterExemplaire() { nombreExemplaires++; }

    /** Réserve un exemplaire disponible. */
    public void emprunter() throws LivreIndisponibleException {
        if (!estDisponible()) throw new LivreIndisponibleException("Le livre « " + titre + " » n'est pas disponible.");
        nombreExemplaires--;
    }

    /** Enregistre le retour d'un exemplaire. */
    public void retourner() { nombreExemplaires++; }

    /** Indique si au moins un exemplaire peut être emprunté. */
    public boolean estDisponible() { return nombreExemplaires > 0; }

    private String texteObligatoire(String valeur, String champ) {
        if (valeur == null || valeur.isBlank()) throw new IllegalArgumentException(champ + " est obligatoire.");
        return valeur.trim();
    }

    @Override
    public String toString() {
        return "Livre{id=" + id + ", titre='" + titre + "', auteur='" + auteur
                + "', categorie='" + categorie + "', exemplairesDisponibles=" + nombreExemplaires + "}";
    }
}