package com.upf.bibliotheque.ui;

import com.upf.bibliotheque.model.Emprunt;
import com.upf.bibliotheque.service.Bibliotheque;

import java.util.ArrayList;
import java.util.List;

/** Sous-menu des emprunts, retours et retards. */
public class MenuEmprunts {
    private final Bibliotheque bibliotheque;

    public MenuEmprunts(Bibliotheque bibliotheque) {
        this.bibliotheque = bibliotheque;
    }

    public void afficher() {
        while (true) {
            Console.filAriane("Accueil", "Emprunts");
            Console.titre("🔄 GESTION DES EMPRUNTS");
            Console.menu("1. Emprunter un livre", "2. Retourner un livre", "3. Consulter tous les emprunts",
                    "4. Afficher les emprunts en cours", "5. Afficher les retards", "0. Retour à l'accueil");
            int choix = Console.lireEntier("Votre choix : ");
            if (choix == 0) return;
            try {
                switch (choix) {
                    case 1 -> emprunter();
                    case 2 -> retourner();
                    case 3 -> afficher(bibliotheque.consulterEmprunts());
                    case 4 -> afficher(bibliotheque.afficherEmpruntsEnCours());
                    case 5 -> afficher(bibliotheque.afficherEmpruntsEnCours().stream().filter(Emprunt::estEnRetard).toList());
                    default -> Console.erreur("Choix invalide.");
                }
            } catch (Exception exception) {
                Console.erreur(exception.getMessage() == null ? "L'opération n'a pas pu être effectuée." : exception.getMessage());
            }
            Console.pauseEtEffacer();
        }
    }

    private void emprunter() throws Exception {
        int idEtudiant = Console.lireEntierPositif("Identifiant étudiant : ");
        int idLivre = Console.lireEntierPositif("Identifiant livre : ");
        Emprunt emprunt = bibliotheque.emprunterLivre(idEtudiant, idLivre);
        Console.succes("Emprunt n°" + emprunt.getId() + " enregistré. Retour prévu le "
                + emprunt.getDateRetourPrevue() + ".");
    }

    private void retourner() throws Exception {
        int id = Console.lireEntierPositif("Identifiant de l'emprunt : ");
        bibliotheque.retournerLivre(id);
        Console.succes("Retour enregistré.");
    }

    private void afficher(List<Emprunt> emprunts) {
        if (emprunts.isEmpty()) {
            Console.information("Aucun emprunt à afficher.");
            return;
        }
        List<String[]> lignes = new ArrayList<>();
        for (Emprunt emprunt : emprunts) {
            String statut = emprunt.estEnRetard() ? Console.rouge("EN RETARD")
                    : emprunt.getStatut().name();
            lignes.add(new String[]{String.valueOf(emprunt.getId()), emprunt.getLivre().getTitre(),
                    emprunt.getEtudiant().getPrenom() + " " + emprunt.getEtudiant().getNom(),
                    emprunt.getDateEmprunt().toString(), emprunt.getDateRetourPrevue().toString(), statut});
        }
        Console.tableau(new String[]{"ID", "Livre", "Étudiant", "Date d'emprunt", "Retour prévu", "Statut"}, lignes);
    }
}