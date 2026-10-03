package com.upf.bibliotheque.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/** Outils communs d'affichage et de saisie pour l'interface console. */
public final class Console {
    private static final String RESET = "\u001B[0m";
    private static final Pattern CODES_ANSI = Pattern.compile("\u001B\\[[;\\d]*m");
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static Scanner scanner = new Scanner(System.in);
    private static boolean couleurs = !System.getenv().containsKey("NO_COLOR");

    private Console() { }

    public static void configurer(String[] args) {
        for (String argument : args) {
            if ("--sans-couleur".equals(argument)) couleurs = false;
        }
    }

    public static String cyan(String texte) { return colorer("36", texte); }
    public static String vert(String texte) { return colorer("32", texte); }
    public static String rouge(String texte) { return colorer("31", texte); }
    public static String jaune(String texte) { return colorer("33", texte); }
    public static String gris(String texte) { return colorer("90", texte); }

    private static String colorer(String code, String texte) {
        return couleurs ? "\u001B[" + code + "m" + texte + RESET : texte;
    }

    public static void filAriane(String... chemins) {
        List<String> elements = new ArrayList<>(List.of(chemins));
        System.out.println(gris(String.join("  ›  ", elements)));
        System.out.println(gris("─".repeat(72)));
    }

    public static void titre(String texte) {
        int largeur = Math.max(texte.codePointCount(0, texte.length()) + 4, 24);
        String ligne = "═".repeat(largeur);
        System.out.println(cyan("╔" + ligne + "╗"));
        System.out.println(cyan("║  " + texte + "  ║"));
        System.out.println(cyan("╚" + ligne + "╝"));
    }

    public static void banniere() {
        System.out.println(cyan("╔════════════════════════════════════════════════════════════════════╗"));
        System.out.println(cyan("║   ____  ___ ____ _     ___ ___ _____ _   _ _____ _   _ _____ ___  ║"));
        System.out.println(cyan("║  BBBB  IIII  BBBB  L     IIII  OOOO                               ║"));
        System.out.println(cyan("║  B   B  II   B   B L      II  O    O                              ║"));
        System.out.println(cyan("║  BBBB   II   BBBB  L      II  O    O                              ║"));
        System.out.println(cyan("║  BBBB  IIII  BBBB  LLLL  IIII  OOOO                               ║"));
        System.out.println(cyan("║                     BIBLIOTHÈQUE UPF                              ║"));
        System.out.println(cyan("╚════════════════════════════════════════════════════════════════════╝"));
    }

    public static void menu(String... options) {
        int largeur = 58;
        System.out.println(cyan("╔" + "═".repeat(largeur) + "╗"));
        for (String option : options) {
            System.out.printf("%s║%s %-" + (largeur - 1) + "s%s║%n", cyan(""), " ", option, cyan(""));
        }
        System.out.println(cyan("╚" + "═".repeat(largeur) + "╝"));
    }

    public static void tableau(String[] colonnes, List<String[]> lignes) {
        int[] largeurs = new int[colonnes.length];
        for (int index = 0; index < colonnes.length; index++) {
            largeurs[index] = longueurVisible(colonnes[index]);
        }
        for (String[] ligne : lignes) {
            for (int index = 0; index < colonnes.length; index++) {
                largeurs[index] = Math.min(42, Math.max(largeurs[index], longueurVisible(ligne[index])));
            }
        }
        StringBuilder bordure = new StringBuilder("+");
        for (int largeur : largeurs) bordure.append("-".repeat(largeur + 2)).append('+');
        System.out.println(gris(bordure.toString()));
        afficherLigne(colonnes, largeurs, true);
        System.out.println(gris(bordure.toString()));
        for (String[] ligne : lignes) afficherLigne(ligne, largeurs, false);
        System.out.println(gris(bordure.toString()));
    }

    private static void afficherLigne(String[] valeurs, int[] largeurs, boolean entete) {
        System.out.print("|");
        for (int index = 0; index < largeurs.length; index++) {
            String valeur = valeurs[index];
            int espaces = Math.max(0, largeurs[index] - longueurVisible(valeur));
            String contenu = " " + valeur + " ".repeat(espaces + 1);
            System.out.print((entete ? cyan(contenu) : contenu) + "|");
        }
        System.out.println();
    }

    private static int longueurVisible(String valeur) {
        return CODES_ANSI.matcher(valeur).replaceAll("").codePointCount(0,
                CODES_ANSI.matcher(valeur).replaceAll("").length());
    }

    public static String lireTexte(String invite) {
        while (true) {
            System.out.print(cyan(invite));
            String valeur = scanner.nextLine().trim();
            if (!valeur.isBlank()) return valeur;
            erreur("Ce champ ne peut pas être vide.");
        }
    }

    public static String lireEmail(String invite) {
        while (true) {
            String valeur = lireTexte(invite);
            if (EMAIL.matcher(valeur).matches()) return valeur;
            erreur("Veuillez saisir une adresse e-mail valide.");
        }
    }

    public static int lireEntier(String invite) {
        while (true) {
            System.out.print(cyan(invite));
            String valeur = scanner.nextLine().trim();
            try {
                return Integer.parseInt(valeur);
            } catch (NumberFormatException exception) {
                erreur("Veuillez saisir un nombre entier valide.");
            }
        }
    }

    public static int lireEntierPositif(String invite) {
        while (true) {
            int valeur = lireEntier(invite);
            if (valeur > 0) return valeur;
            erreur("La valeur doit être supérieure à zéro.");
        }
    }

    public static int lireEntierNonNegatif(String invite) {
        while (true) {
            int valeur = lireEntier(invite);
            if (valeur >= 0) return valeur;
            erreur("La valeur ne peut pas être négative.");
        }
    }

    public static boolean confirmer(String message) {
        while (true) {
            System.out.print(jaune(message + " (O/N) : "));
            String reponse = scanner.nextLine().trim();
            if (reponse.equalsIgnoreCase("o")) return true;
            if (reponse.equalsIgnoreCase("n")) return false;
            erreur("Répondez par O ou N.");
        }
    }

    public static void succes(String message) { System.out.println(vert("✅ " + message)); }
    public static void erreur(String message) { System.out.println(rouge("❌ " + message)); }
    public static void avertissement(String message) { System.out.println(jaune("⚠️ " + message)); }
    public static void information(String message) { System.out.println(gris(message)); }

    public static void pauseEtEffacer() {
        System.out.print(gris("\nAppuyez sur Entrée pour continuer..."));
        scanner.nextLine();
        System.out.print("\u001B[2J\u001B[H");
    }
}