package com.projetict207;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Form2 {

    // Liste partagée (Trecy / Elisée / Jemina)
    private static final ObservableList<Etudiant> listeEtudiants = FXCollections.observableArrayList();

    public static Scene getScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f4f7f6;");

        // --- SECTION JEMINA : Saisie des notes (LOT B) ---
        VBox inputArea = new VBox(15);
        inputArea.setPadding(new Insets(0, 0, 20, 0));
        
        Label titre = new Label("Espace Professeur : Saisie des Notes");
        titre.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        // On utilise un GridPane pour un alignement propre des champs de Jemina
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #dcdde1; -fx-border-radius: 10;");

        TextField txtMatricule = new TextField(); txtMatricule.setPromptText("Ex: MY123");
        TextField txtNom = new TextField(); txtNom.setPromptText("Nom de l'étudiant");
        TextField txtPrenom = new TextField(); txtPrenom.setPromptText("Prénom");
        TextField txtNote = new TextField(); txtNote.setPromptText("Note (ex: 15.5)");
        TextField txtFiliere = new TextField(); txtFiliere.setPromptText("Ex: ICT4D");
        TextField txtNiveau = new TextField(); txtNiveau.setPromptText("Ex: L2");

        grid.add(new Label("Matricule:"), 0, 0); grid.add(txtMatricule, 1, 0);
        grid.add(new Label("Nom:"), 0, 1);       grid.add(txtNom, 1, 1);
        grid.add(new Label("Prénom:"), 2, 0);    grid.add(txtPrenom, 3, 0);
        grid.add(new Label("Note:"), 2, 1);      grid.add(txtNote, 3, 1);
        grid.add(new Label("Filière:"), 4, 0);   grid.add(txtFiliere, 5, 0);
        grid.add(new Label("Niveau:"), 4, 1);    grid.add(txtNiveau, 5, 1);

        Button btnAjouter = new Button("Enregistrer l'étudiant");
        btnAjouter.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20;");

        btnAjouter.setOnAction(e -> {
            try {
                int id = listeEtudiants.size() + 1;
                String matricule = txtMatricule.getText();
                String nom = txtNom.getText();
                String prenom = txtPrenom.getText();
                double note = Double.parseDouble(txtNote.getText());
                String filiere = txtFiliere.getText();
                String niveau = txtNiveau.getText();

                Etudiant etudiant = new Etudiant(id, matricule, nom, prenom, note, filiere, niveau);
                listeEtudiants.add(etudiant);

                // Nettoyage des champs
                txtMatricule.clear(); txtNom.clear(); txtPrenom.clear();
                txtNote.clear(); txtFiliere.clear(); txtNiveau.clear();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez entrer une note valide !");
                alert.show();
            }
        });

        HBox wrapperBtn = new HBox(btnAjouter);
        wrapperBtn.setAlignment(Pos.CENTER_RIGHT);
        
        inputArea.getChildren().addAll(titre, grid, wrapperBtn);
        root.setTop(inputArea);


        // --- SECTION ELISÉE : Tableau des notes (LOT C) ---
        VBox tableArea = new VBox(10);
        Label tableTitle = new Label("Liste des Notes Enregistrées");
        tableTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #34495e;");
        tableArea.getChildren().add(tableTitle);

        TableView<Etudiant> monTableView = new TableView<>();

        TableColumn<Etudiant, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(50);

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
        monTableView.setItems(listeEtudiants);
        monTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableArea.getChildren().add(monTableView);
        root.setCenter(tableArea);


        // --- Footer ---
        Button backToForm1 = new Button("Déconnexion");
        backToForm1.setOnAction(e -> App.showForm1());
        backToForm1.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white;");
        
        HBox footer = new HBox(backToForm1);
        footer.setPadding(new Insets(20, 0, 0, 0));
        footer.setAlignment(Pos.CENTER_LEFT);
        
        root.setBottom(footer);

        return new Scene(root, 1000, 700);
    }
}
