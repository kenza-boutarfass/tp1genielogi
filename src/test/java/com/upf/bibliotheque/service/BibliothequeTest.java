package com.upf.bibliotheque.service;

import com.upf.bibliotheque.exception.LimiteEmpruntDepasseeException;
import com.upf.bibliotheque.exception.LivreIndisponibleException;
import com.upf.bibliotheque.model.Emprunt;
import com.upf.bibliotheque.model.Etudiant;
import com.upf.bibliotheque.model.Livre;
import com.upf.bibliotheque.model.StatutEmprunt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BibliothequeTest {
    private Bibliotheque bibliotheque;

    @BeforeEach
    void initialiser() {
        bibliotheque = new Bibliotheque();
        bibliotheque.ajouterEtudiant(new Etudiant(1, "Alami", "Sara", "sara@upf.ma"));
        bibliotheque.ajouterLivre(new Livre(1, "Java avancé", "A. Auteur", "Informatique", 2));
        bibliotheque.ajouterLivre(new Livre(2, "Structures de données", "B. Auteur", "Informatique", 1));
        bibliotheque.ajouterLivre(new Livre(3, "Histoire", "C. Auteur", "Sciences humaines", 1));
        bibliotheque.ajouterLivre(new Livre(4, "Poésie", "D. Auteur", "Littérature", 1));
    }

    @Test
    void empruntReussiMetAJourLesAssociationsEtLeStock() throws Exception {
        Emprunt emprunt = bibliotheque.emprunterLivre(1, 1);

        assertEquals(1, emprunt.getId());
        assertEquals(1, bibliotheque.trouverLivreParId(1).getNombreExemplaires());
        assertEquals(StatutEmprunt.EN_COURS, emprunt.getStatut());
        assertEquals(1, bibliotheque.trouverEtudiantParId(1).getEmprunts().size());
    }

    @Test
    void empruntDUnLivreIndisponibleEstRefuse() throws Exception {
        bibliotheque.trouverLivreParId(1).setNombreExemplaires(0);

        assertThrows(LivreIndisponibleException.class, () -> bibliotheque.emprunterLivre(1, 1));
        assertTrue(bibliotheque.consulterEmprunts().isEmpty());
        assertEquals(0, bibliotheque.trouverLivreParId(1).getNombreExemplaires());
    }

    @Test
    void retourTermineLEmpruntEtRestitueUnExemplaire() throws Exception {
        Emprunt emprunt = bibliotheque.emprunterLivre(1, 1);

        bibliotheque.retournerLivre(emprunt.getId());

        assertEquals(2, bibliotheque.trouverLivreParId(1).getNombreExemplaires());
        assertEquals(StatutEmprunt.RETOURNE, emprunt.getStatut());
        assertEquals(0, bibliotheque.afficherEmpruntsEnCours().size());
    }

    @Test
    void etudiantNePeutPasDepasserTroisEmpruntsEnCours() throws Exception {
        bibliotheque.emprunterLivre(1, 1);
        bibliotheque.emprunterLivre(1, 2);
        bibliotheque.emprunterLivre(1, 3);

        assertThrows(LimiteEmpruntDepasseeException.class, () -> bibliotheque.emprunterLivre(1, 4));
        assertEquals(3, bibliotheque.afficherEmpruntsEnCours().size());
        assertEquals(1, bibliotheque.trouverLivreParId(4).getNombreExemplaires());
    }

    @Test
    void rechercheLivreIgnoreLaCasseEtChercheDansLesChamps() {
        assertEquals(1, bibliotheque.rechercherLivre("JAVA").size());
        assertEquals("Java avancé", bibliotheque.rechercherLivre("a. auteur").get(0).getTitre());
        assertEquals(2, bibliotheque.rechercherLivre("informatique").size());
    }
}