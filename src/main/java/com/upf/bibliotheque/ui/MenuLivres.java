package com.upf.bibliotheque.ui;

import com.upf.bibliotheque.model.Livre;
import com.upf.bibliotheque.service.Bibliotheque;

import java.util.ArrayList;
import java.util.List;

/** Sous-menu de gestion des livres. */
public class MenuLivres {
    private final Bibliotheque bibliotheque;

    public MenuLivres(Bibliotheque bibliotheque) {
        this.bibliotheque = bibliotheque;
    }

    public void afficher() {
        while (true) {
            Console.filAriane("Accueil", "Livres");
            Console.titre("📚 GESTION DES LIVRES");
            Console.menu("1. Lister tous les livres", "2. Rechercher (titre, auteur, catégorie)",
                    "3. Ajouter un livre", "4. Modifier un livre", "5. Supprimer un livre",
                    "6. Afficher les livres disponibles", "0. Retour à l'accueil");
            int choix = Console.lireEntier("Votre choix : ");
            if (choix == 0) return;
            try {
                switch (choix) {
                    case 1 -> lister(bibliotheque.getLivres());
                    case 2 -> rechercher();
                    case 3 -> ajouter();
                    case 4 -> modifier();
                    case 5 -> supprimer();
                    case 6 -> lister(bibliotheque.afficherLivresDisponibles());
                    default -> Console.erreur("Choix invalide.");
                }
            } catch (Exception exception) {
                Console.erreur(exception.getMessage() == null ? "L'opération n'a pas pu être effectuée." : exception.getMessage());
            }
            Console.pauseEtEffacer();
        }
    }

    private void rechercher() {
        String terme = Console.lireTexte("Titre, auteur ou catégorie : ");
        lister(bibliotheque.rechercherLivre(terme));
    }

    private void ajouter() {
        int id = bibliotheque.getLivres().stream().mapToInt(Livre::getId).max().orElse(0) + 1;
        bibliotheque.ajouterLivre(saisirLivre(id));
        Console.succes("Livre ajouté avec l'identifiant " + id + ".");
    }

    private void modifier() throws Exception {
        int id = Console.lireEntierPositif("Identifiant du livre : ");
        Livre livre = bibliotheque.trouverLivreParId(id);
        System.out.println(Console.gris("Valeurs actuelles : " + livre.getTitre() + " / " + livre.getAuteur()));
        Livre modifie = saisirLivre(id);
        bibliotheque.modifierLivre(modifie);
        Console.succes("Livre modifié.");
    }

    private void supprimer() throws Exception {
        int id = Console.lireEntierPositif("Identifiant du livre à supprimer : ");
        Livre livre = bibliotheque.trouverLivreParId(id);
        if (Console.confirmer("Supprimer « " + livre.getTitre() + " » ?")) {
            bibliotheque.supprimerLivre(id);
            Console.succes("Livre supprimé.");
        } else {
            Console.information("Suppression annulée.");
        }
    }

    private Livre saisirLivre(int id) {
        return new Livre(id, Console.lireTexte("Titre : "), Console.lireTexte("Auteur : "),
                Console.lireTexte("Catégorie : "), Console.lireEntierNonNegatif("Exemplaires : "));
    }

    private void lister(List<Livre> livres) {
        if (livres.isEmpty()) {
            Console.information("Aucun livre à afficher.");
            return;
        }
        List<String[]> lignes = new ArrayList<>();
        for (Livre livre : livres) {
            String disponibilite = livre.estDisponible() ? Console.vert("Disponible") : Console.rouge("Indisponible");
            lignes.add(new String[]{String.valueOf(livre.getId()), livre.getTitre(), livre.getAuteur(),
                    livre.getCategorie(), livre.getNombreExemplaires() + " (" + disponibilite + ")"});
        }
        Console.tableau(new String[]{"ID", "Titre", "Auteur", "Catégorie", "Exemplaires"}, lignes);
    }
}