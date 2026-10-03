# Gestion de bibliothèque - UPF GI4

Application console Java 17 pour gérer en mémoire les livres, les étudiants et les emprunts d'une bibliothèque universitaire. Un étudiant peut avoir au maximum trois emprunts en cours. Chaque emprunt concerne un livre et un étudiant, avec une date de retour prévue quatorze jours après l'emprunt.

## Structure du projet

```text
src/
  main/java/com/upf/bibliotheque/
    app/Main.java
    exception/       Exceptions métier
    model/           Livre, Etudiant, Emprunt, StatutEmprunt
    service/         Bibliotheque
  test/java/com/upf/bibliotheque/service/BibliothequeTest.java
docs/                Diagrammes PlantUML
pom.xml
```

Les données sont conservées dans des `ArrayList` pendant l'exécution et ne sont pas enregistrées après la fermeture du programme.

## Prérequis

- JDK 17 ou version ultérieure
- Maven 3.8 ou version ultérieure

## Compilation et tests

Depuis le répertoire du projet :

```bash
mvn compile
mvn test
```

Pour lancer la démonstration puis le menu console :

```bash
mvn exec:java
```

## Exemple d'exécution

```text
============================================================
DÉMONSTRATION - BIBLIOTHÈQUE UPF GI4
============================================================
Livres disponibles :
Livre{id=1, titre='Les Misérables', auteur='Victor Hugo', categorie='Roman', exemplairesDisponibles=1}
...
Emprunt effectué : Emprunt{id=1, livre='Les Misérables', ... statut=EN_COURS}
Exception attendue : Le livre « Les Misérables » n'est pas disponible.
Retour effectué, exemplaires disponibles : 1
...
1. Gérer les livres
2. Gérer les étudiants
3. Emprunter un livre
4. Retourner un livre
5. Consulter les emprunts
0. Quitter
Votre choix :
```

## Interface console

Après la démonstration automatique en six étapes, le menu interactif donne accès aux livres, aux étudiants, aux emprunts et à la recherche. Les identifiants des nouveaux livres et étudiants sont attribués automatiquement.

```bash
mvn exec:java
```

Pour désactiver les couleurs ANSI, passez `--sans-couleur` ou définissez la variable d'environnement `NO_COLOR` :

```bash
mvn exec:java -Dexec.args="--sans-couleur"
```

Exemple d'écran d'accueil :

```text
╔════════════════════════════════════════════════════════════════════╗
║                     BIBLIOTHÈQUE UPF                              ║
╚════════════════════════════════════════════════════════════════════╝
Accueil › Tableau de bord
  📚 Livres : 5   🎓 Étudiants : 3   🔄 Emprunts en cours : 1
  1. 📚 Livres
  2. 🎓 Étudiants
  3. 🔄 Emprunts
  4. 🔍 Recherche de livre
  0. Quitter
Votre choix :
```

Sous Windows, il est préférable d'utiliser le terminal intégré de VS Code ou Windows Terminal pour profiter correctement des caractères Unicode et des couleurs ANSI.

Les diagrammes de cas d'utilisation, de classes, de séquence et d'activité sont dans `docs/`. Ils peuvent être visualisés avec PlantUML.