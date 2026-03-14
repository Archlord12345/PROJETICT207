package com.projetict207;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class Form2 {

    /**
     * MISSION POUR JEMINA (LOT B) ET ELISÉE (LOT C)
     * 
     * Jemina : Ton rôle est de créer les champs de saisie (Matricule, Nom, Prénom, Note, Filière, Niveau) 
     * et le bouton pour ajouter un étudiant.
     * 
     * Elisée : Ton rôle est de créer le tableau (TableView) pour afficher 
     * la liste des étudiants avec toutes leurs informations.
     * 
     * Trecy (LOT A) a déjà créé la classe Etudiant.java que vous allez utiliser.
     */
    public static Scene getScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // --- ZONE POUR JEMINA (LOT B - Saisie) ---
        // Jemina, crée un HBox ou GridPane ici avec des TextField et un Button "Ajouter"
        VBox inputArea = new VBox(10);
        inputArea.getChildren().add(new Label("SECTION JEMINA : Saisie des notes"));
        // inputArea.getChildren().addAll(txtNom, txtPrenom, txtNote, btnAjouter);
        
        root.setTop(inputArea);


        // --- ZONE POUR ELISÉE (LOT C - Affichage) ---
        // Elisée, crée un TableView<Etudiant> ici pour afficher la liste
        VBox tableArea = new VBox(10);
        tableArea.getChildren().add(new Label("SECTION ELISÉE : Tableau des notes"));
        // tableArea.getChildren().add(monTableView);

        root.setCenter(tableArea);


        // Bouton de retour (déjà présent)
        Button backToForm1 = new Button("Retour à l'accueil");
        backToForm1.setOnAction(e -> App.showForm1());
        root.setBottom(backToForm1);

        return new Scene(root, 800, 600);
    }
}
