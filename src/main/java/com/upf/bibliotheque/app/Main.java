package com.upf.bibliotheque.app;

import com.upf.bibliotheque.exception.EtudiantIntrouvableException;
import com.upf.bibliotheque.exception.LimiteEmpruntDepasseeException;
import com.upf.bibliotheque.exception.LivreIndisponibleException;
import com.upf.bibliotheque.exception.LivreIntrouvableException;
import com.upf.bibliotheque.model.Emprunt;
import com.upf.bibliotheque.model.Etudiant;
import com.upf.bibliotheque.model.Livre;
import com.upf.bibliotheque.service.Bibliotheque;
import com.upf.bibliotheque.ui.Console;
import com.upf.bibliotheque.ui.MenuPrincipal;

import java.util.ArrayList;

/** Point d'entrée de l'application console de gestion de bibliothèque. */
public class Main {
    private Main() { }

    public static void main(String[] args) {
        Console.configurer(args);
        Bibliotheque bibliotheque = new Bibliotheque();
        initialiserDonnees(bibliotheque);
        afficherDemonstration(bibliotheque);
        new MenuPrincipal(bibliotheque).afficher();
    }

    private static void initialiserDonnees(Bibliotheque bibliotheque) {
        bibliotheque.ajouterLivre(new Livre(1, "Les Misérables", "Victor Hugo", "Roman", 1));
        bibliotheque.ajouterLivre(new Livre(2, "L'Étranger", "Albert Camus", "Roman", 3));
        bibliotheque.ajouterLivre(new Livre(3, "Clean Code", "Robert C. Martin", "Informatique", 2));
        bibliotheque.ajouterLivre(new Livre(4, "Le Petit Prince", "Antoine de Saint-Exupéry", "Conte", 4));
        bibliotheque.ajouterLivre(new Livre(5, "Introduction aux algorithmes", "Thomas H. Cormen", "Informatique", 2));
        bibliotheque.ajouterEtudiant(new Etudiant(1, "Alami", "Sara", "sara.alami@upf.ac.ma"));
        bibliotheque.ajouterEtudiant(new Etudiant(2, "Bennani", "Youssef", "youssef.bennani@upf.ac.ma"));
        bibliotheque.ajouterEtudiant(new Etudiant(3, "Idrissi", "Meryem", "meryem.idrissi@upf.ac.ma"));
    }

    private static void afficherDemonstration(Bibliotheque bibliotheque) {
        Console.titre("DÉMONSTRATION - BIBLIOTHÈQUE UPF GI4");
        System.out.println("Étape 1/6 - Livres disponibles :");
        afficherLivres(bibliotheque.afficherLivresDisponibles());
        try {
            Emprunt premier = bibliotheque.emprunterLivre(1, 1);
            System.out.println("Étape 2/6 - Emprunt effectué : " + premier.getId()
                    + " (« " + premier.getLivre().getTitre() + " »).");
            try {
                bibliotheque.emprunterLivre(2, 1);
            } catch (LivreIndisponibleException exception) {
                System.out.println("Étape 3/6 - Exception attendue : " + exception.getMessage());
            }
            bibliotheque.retournerLivre(premier.getId());
                System.out.println("Étape 4/6 - Retour effectué, exemplaires disponibles : "
                    + bibliotheque.trouverLivreParId(1).getNombreExemplaires());
                Emprunt second = bibliotheque.emprunterLivre(2, 2);
                System.out.println("Étape 5/6 - Nouvel emprunt enregistré : " + second.getId()
                    + " (« " + second.getLivre().getTitre() + " »).");
        } catch (EtudiantIntrouvableException | LivreIntrouvableException
                 | LivreIndisponibleException | LimiteEmpruntDepasseeException
                 | com.upf.bibliotheque.exception.EmpruntIntrouvableException exception) {
            System.out.println("Erreur pendant la démonstration : " + exception.getMessage());
        }
        System.out.println("Étape 6/6 - Emprunts en cours :");
        afficherEmprunts(bibliotheque.afficherEmpruntsEnCours());
    }

    private static void afficherLivres(java.util.List<Livre> livres) {
        if (livres.isEmpty()) {
            Console.information("Aucun livre disponible.");
            return;
        }
        ArrayList<String[]> lignes = new ArrayList<>();
        for (Livre livre : livres) {
            String statut = livre.estDisponible() ? Console.vert("Disponible") : Console.rouge("Indisponible");
            lignes.add(new String[]{String.valueOf(livre.getId()), livre.getTitre(), livre.getAuteur(),
                    livre.getCategorie(), livre.getNombreExemplaires() + " (" + statut + ")"});
        }
        Console.tableau(new String[]{"ID", "Titre", "Auteur", "Catégorie", "Exemplaires"}, lignes);
    }

    private static void afficherEmprunts(java.util.List<Emprunt> emprunts) {
        if (emprunts.isEmpty()) {
            Console.information("Aucun emprunt en cours.");
            return;
        }
        ArrayList<String[]> lignes = new ArrayList<>();
        for (Emprunt emprunt : emprunts) {
            String statut = emprunt.estEnRetard() ? Console.rouge("EN RETARD") : emprunt.getStatut().name();
            lignes.add(new String[]{String.valueOf(emprunt.getId()), emprunt.getLivre().getTitre(),
                    emprunt.getEtudiant().getPrenom() + " " + emprunt.getEtudiant().getNom(),
                    emprunt.getDateEmprunt().toString(), emprunt.getDateRetourPrevue().toString(), statut});
        }
        Console.tableau(new String[]{"ID", "Livre", "Étudiant", "Date d'emprunt", "Retour prévu", "Statut"}, lignes);
    }
}