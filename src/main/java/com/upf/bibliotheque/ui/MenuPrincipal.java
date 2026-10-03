package com.upf.bibliotheque.ui;

import com.upf.bibliotheque.model.Livre;
import com.upf.bibliotheque.service.Bibliotheque;

import java.util.ArrayList;
import java.util.List;

/** Menu d'accueil et navigation de l'application. */
public class MenuPrincipal {
    private final Bibliotheque bibliotheque;
    private final MenuLivres menuLivres;
    private final MenuEtudiants menuEtudiants;
    private final MenuEmprunts menuEmprunts;

    public MenuPrincipal(Bibliotheque bibliotheque) {
        this.bibliotheque = bibliotheque;
        menuLivres = new MenuLivres(bibliotheque);
        menuEtudiants = new MenuEtudiants(bibliotheque);
        menuEmprunts = new MenuEmprunts(bibliotheque);
    }

    public void afficher() {
        Console.banniere();
        while (true) {
            Console.filAriane("Accueil");
            afficherTableauDeBord();
            Console.menu("1. 📚 Livres", "2. 🎓 Étudiants", "3. 🔄 Emprunts",
                    "4. 🔍 Recherche de livre", "0. Quitter");
            int choix = Console.lireEntier("Votre choix : ");
            switch (choix) {
                case 1 -> menuLivres.afficher();
                case 2 -> menuEtudiants.afficher();
                case 3 -> menuEmprunts.afficher();
                case 4 -> rechercher();
                case 0 -> {
                    Console.titre("À BIENTÔT À LA BIBLIOTHÈQUE UPF");
                    Console.succes("Merci de votre visite. Au revoir !");
                    return;
                }
                default -> {
                    Console.erreur("Choix invalide.");
                    Console.pauseEtEffacer();
                }
            }
        }
    }

    private void afficherTableauDeBord() {
        long empruntsActifs = bibliotheque.afficherEmpruntsEnCours().size();
        Console.titre("TABLEAU DE BORD");
        System.out.printf("  📚 Livres : %s   🎓 Étudiants : %s   🔄 Emprunts en cours : %s%n%n",
                Console.cyan(String.valueOf(bibliotheque.getLivres().size())),
                Console.cyan(String.valueOf(bibliotheque.getEtudiants().size())),
                Console.jaune(String.valueOf(empruntsActifs)));
    }

    private void rechercher() {
        Console.filAriane("Accueil", "Recherche");
        Console.titre("🔍 RECHERCHE DE LIVRE");
        String terme = Console.lireTexte("Titre, auteur ou catégorie : ");
        List<Livre> livres = bibliotheque.rechercherLivre(terme);
        if (livres.isEmpty()) {
            Console.information("Aucun livre trouvé.");
        } else {
            List<String[]> lignes = new ArrayList<>();
            for (Livre livre : livres) {
                String disponibilite = livre.estDisponible() ? Console.vert("Disponible") : Console.rouge("Indisponible");
                lignes.add(new String[]{String.valueOf(livre.getId()), livre.getTitre(), livre.getAuteur(),
                        livre.getCategorie(), livre.getNombreExemplaires() + " (" + disponibilite + ")"});
            }
            Console.tableau(new String[]{"ID", "Titre", "Auteur", "Catégorie", "Exemplaires"}, lignes);
        }
        Console.pauseEtEffacer();
    }
}