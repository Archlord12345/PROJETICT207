# Projet JavaFX avec SQLite

Il s'agit d'une application JavaFX avec trois formulaires interconnectés (Form1, Form2, Form3) et une intégration de base de données SQLite.

## Prérequis

- Java 11 ou supérieur
- Maven 3.6 ou supérieur

## Comment lancer le projet

1. **Naviguer vers le répertoire du projet :**
   ```
   cd /home/ravel/Documents/CODES/PROJETICT207
   ```

2. **Compiler et exécuter l'application :**
   ```
   mvn clean install
   mvn javafx:run
   ```

   Alternativement, vous pouvez exécuter directement :
   ```
   mvn javafx:run
   ```

## Structure du projet

- `src/main/java/com/projetict207/` - Code source principal
  - `App.java` - Classe principale de l'application
  - `Form1.java`, `Form2.java`, `Form3.java` - Classes de formulaires
  - `DatabaseConnector.java` - Utilitaire de connexion à la base de données SQLite

- `pom.xml` - Configuration Maven avec les dépendances JavaFX et SQLite

## Fonctionnalités

- Trois formulaires : Form1 (départ, avec des boutons vers Form2 et Form3), Form2 (retour à Form1), Form3 (retour à Form1)
- Connexion à la base de données SQLite prête à l'emploi

## Base de données

L'application utilise SQLite. Le fichier de base de données `database.db` sera créé automatiquement à la racine du projet s'il n'existe pas. La classe `DatabaseConnector` inclut une méthode pour créer un exemple de table `users`.
