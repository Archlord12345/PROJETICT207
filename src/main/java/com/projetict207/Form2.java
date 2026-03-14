package com.projetict207;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Form2 {

    // Liste partagée (Trecy / Elisée / Jemina)
    private static final ObservableList<Etudiant> listeEtudiants = FXCollections.observableArrayList();

    /**
     * MISSION POUR JEMINA (LOT B) ET ELISÉE (LOT C)
     * 
     * Jemina : Ton rôle est de créer les champs de saisie (Matricule, Nom, Prénom, Note, Filière, Niveau) 
     * et le bouton pour ajouter un étudiant.
     * 
     * Elisée : Ton rôle est de créer le tableau (TableView) pour afficher 
     * la liste des étudiants avec toutes leurs informations.
     */
    public static Scene getScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // --- ZONE POUR JEMINA (LOT B - Saisie) ---
        VBox inputArea = new VBox(10);
        inputArea.setPadding(new Insets(0, 0, 20, 0));
        inputArea.getChildren().add(new Label("SECTION JEMINA : Saisie des notes"));
        
        // Jemina, tu devras déclarer tes TextField ici et les ajouter à inputArea
        // Exemple : txtNom = new TextField(); inputArea.getChildren().add(txtNom);
        // Puis dans l'action du bouton : 
        // Etudiant e = new Etudiant(id, mat, nom, pren, note, fil, niv);
        // listeEtudiants.add(e);
        
        root.setTop(inputArea);


        // --- ZONE POUR ELISÉE (LOT C - Affichage) ---
        VBox tableArea = new VBox(10);
        Label tableTitle = new Label("SECTION ELISÉE : Tableau des notes");
        tableTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        tableArea.getChildren().add(tableTitle);

        TableView<Etudiant> monTableView = new TableView<>();

        // Elisée : Voici tes colonnes configurées proprement
        TableColumn<Etudiant, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Etudiant, String> colMatricule = new TableColumn<>("Matricule");
        colMatricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));

        TableColumn<Etudiant, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));

        TableColumn<Etudiant, String> colPrenom = new TableColumn<>("Prénom");
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));

        TableColumn<Etudiant, Double> colNote = new TableColumn<>("Note");
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));

        TableColumn<Etudiant, String> colFiliere = new TableColumn<>("Filière");
        colFiliere.setCellValueFactory(new PropertyValueFactory<>("filiere"));

        TableColumn<Etudiant, String> colNiveau = new TableColumn<>("Niveau");
        colNiveau.setCellValueFactory(new PropertyValueFactory<>("niveau"));

        monTableView.getColumns().addAll(colId, colMatricule, colNom, colPrenom, colNote, colFiliere, colNiveau);
        
        // Liaison avec la liste
        monTableView.setItems(listeEtudiants);
        monTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableArea.getChildren().add(monTableView);
        root.setCenter(tableArea);


        // Bouton de retour
        Button backToForm1 = new Button("Retour à l'accueil");
        backToForm1.setOnAction(e -> App.showForm1());
        backToForm1.setStyle("-fx-margin-top: 10px;");
        
        root.setBottom(backToForm1);

        return new Scene(root, 1000, 700);
    }
}
