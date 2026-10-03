package com.upf.bibliotheque.ui;

import com.upf.bibliotheque.model.Emprunt;
import com.upf.bibliotheque.model.Etudiant;
import com.upf.bibliotheque.service.Bibliotheque;

import java.util.ArrayList;
import java.util.List;

/** Sous-menu de gestion des étudiants. */
public class MenuEtudiants {
    private final Bibliotheque bibliotheque;

    public MenuEtudiants(Bibliotheque bibliotheque) {
        this.bibliotheque = bibliotheque;
    }

    public void afficher() {
        while (true) {
            Console.filAriane("Accueil", "Étudiants");
            Console.titre("🎓 GESTION DES ÉTUDIANTS");
            Console.menu("1. Lister les étudiants", "2. Ajouter un étudiant", "3. Modifier un étudiant",
                    "4. Consulter un étudiant et ses emprunts", "0. Retour à l'accueil");
            int choix = Console.lireEntier("Votre choix : ");
            if (choix == 0) return;
            try {
                switch (choix) {
                    case 1 -> lister();
                    case 2 -> ajouter();
                    case 3 -> modifier();
                    case 4 -> consulter();
                    default -> Console.erreur("Choix invalide.");
                }
            } catch (Exception exception) {
                Console.erreur(exception.getMessage() == null ? "L'opération n'a pas pu être effectuée." : exception.getMessage());
            }
            Console.pauseEtEffacer();
        }
    }

    private void lister() {
        List<Etudiant> etudiants = bibliotheque.getEtudiants();
        if (etudiants.isEmpty()) {
            Console.information("Aucun étudiant à afficher.");
            return;
        }
        List<String[]> lignes = new ArrayList<>();
        for (Etudiant etudiant : etudiants) {
            lignes.add(new String[]{String.valueOf(etudiant.getId()), etudiant.getNom(), etudiant.getPrenom(),
                    etudiant.getEmail(), String.valueOf(etudiant.getEmpruntsEnCours().size())});
        }
        Console.tableau(new String[]{"ID", "Nom", "Prénom", "E-mail", "Emprunts en cours"}, lignes);
    }

    private void ajouter() {
        int id = bibliotheque.getEtudiants().stream().mapToInt(Etudiant::getId).max().orElse(0) + 1;
        bibliotheque.ajouterEtudiant(saisirEtudiant(id));
        Console.succes("Étudiant ajouté avec l'identifiant " + id + ".");
    }

    private void modifier() throws Exception {
        int id = Console.lireEntierPositif("Identifiant de l'étudiant : ");
        Etudiant existant = bibliotheque.trouverEtudiantParId(id);
        System.out.println(Console.gris("Valeurs actuelles : " + existant.getPrenom() + " " + existant.getNom()));
        bibliotheque.modifierEtudiant(saisirEtudiant(id));
        Console.succes("Étudiant modifié.");
    }

    private void consulter() throws Exception {
        int id = Console.lireEntierPositif("Identifiant de l'étudiant : ");
        Etudiant etudiant = bibliotheque.trouverEtudiantParId(id);
        Console.titre(etudiant.consulterInformations());
        List<Emprunt> emprunts = etudiant.consulterEmprunts();
        if (emprunts.isEmpty()) {
            Console.information("Cet étudiant n'a encore aucun emprunt.");
        } else {
            List<String[]> lignes = new ArrayList<>();
            for (Emprunt emprunt : emprunts) {
                String statut = emprunt.estEnRetard() ? Console.rouge("EN RETARD") : emprunt.getStatut().name();
                lignes.add(new String[]{String.valueOf(emprunt.getId()), emprunt.getLivre().getTitre(),
                        emprunt.getDateEmprunt().toString(), emprunt.getDateRetourPrevue().toString(), statut});
            }
            Console.tableau(new String[]{"ID", "Livre", "Date d'emprunt", "Retour prévu", "Statut"}, lignes);
        }
    }

    private Etudiant saisirEtudiant(int id) {
        return new Etudiant(id, Console.lireTexte("Nom : "), Console.lireTexte("Prénom : "),
                Console.lireEmail("E-mail : "));
    }
}