package com.projetict207;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import javafx.application.Platform;

import java.util.List;

public class Form2 {

    private static ComboBox<DatabaseConnector.Filiere> filiereCombo;
    private static ComboBox<DatabaseConnector.Niveau> niveauCombo;
    private static ComboBox<DatabaseConnector.UE> ueCombo;
    private static ComboBox<DatabaseConnector.TypeEvaluation> typeEvalCombo;
    private static ComboBox<DatabaseConnector.Etudiant> etudiantCombo;
    private static TextField noteField;
    private static Label statusLabel;
    
    private static TableView<DatabaseConnector.NoteDetail> notesTable;
    private static final ObservableList<DatabaseConnector.NoteDetail> notesList = FXCollections.observableArrayList();

    public static Scene getScene() {
        DatabaseConnector.User user = App.getCurrentUser();
        if (user == null) {
            // Sécurité au cas où
            return new Scene(new StackPane(new Label("Session expirée")), 400, 300);
        }

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f4f7f6;");

        // --- HEADER ---
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));

        Label titre = new Label("Espace Enseignant : Saisie et Visualisation");
        titre.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label userInfo = new Label("Connecté en tant que : " + user.fullName);
        userInfo.setStyle("-fx-text-fill: #7f8c8d; -fx-font-style: italic;");
        
        header.getChildren().addAll(titre, spacer, userInfo);
        root.setTop(header);

        // --- SECTION GAUCHE : Formulaire de Saisie (Fusion Lead + Jemina) ---
        VBox leftSection = new VBox(15);
        leftSection.setPrefWidth(350);
        leftSection.setPadding(new Insets(10));
        leftSection.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #dcdde1; -fx-border-radius: 10;");

        Label formTitle = new Label("Nouvelle Note");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        filiereCombo = createComboBox("Filière", 200);
        filiereCombo.setOnAction(e -> loadNiveauxAndUEs());
        
        niveauCombo = createComboBox("Niveau", 200);
        niveauCombo.setOnAction(e -> loadUEsAndEtudiants());
        
        ueCombo = createComboBox("UE", 200);
        ueCombo.setOnAction(e -> refreshTable());
        
        typeEvalCombo = createComboBox("Type Eval.", 200);
        etudiantCombo = createComboBox("Étudiant", 200);
        
        noteField = new TextField();
        noteField.setPromptText("Note / 20");

        int row = 0;
        grid.add(new Label("Filière:"), 0, row); grid.add(filiereCombo, 1, row++);
        grid.add(new Label("Niveau:"), 0, row); grid.add(niveauCombo, 1, row++);
        grid.add(new Label("UE:"), 0, row);     grid.add(ueCombo, 1, row++);
        grid.add(new Label("Évaluation:"), 0, row); grid.add(typeEvalCombo, 1, row++);
        grid.add(new Label("Étudiant:"), 0, row); grid.add(etudiantCombo, 1, row++);
        grid.add(new Label("Note:"), 0, row);     grid.add(noteField, 1, row++);

        Button btnEnregistrer = new Button("Enregistrer la note");
        btnEnregistrer.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");
        btnEnregistrer.setMaxWidth(Double.MAX_VALUE);
        btnEnregistrer.setOnAction(e -> saveNote());

        statusLabel = new Label();
        statusLabel.setWrapText(true);

        leftSection.getChildren().addAll(formTitle, grid, btnEnregistrer, statusLabel);

        // --- SECTION CENTRE : Tableau des Notes (Elisée + Lead) ---
        VBox centerSection = new VBox(10);
        centerSection.setPadding(new Insets(0, 0, 0, 20));
        
        Label tableTitle = new Label("Notes enregistrées pour l'UE sélectionnée");
        tableTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        notesTable = new TableView<>();
        
        TableColumn<DatabaseConnector.NoteDetail, String> colMatricule = new TableColumn<>("Matricule");
        colMatricule.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().matricule));
        
        TableColumn<DatabaseConnector.NoteDetail, String> colNom = new TableColumn<>("Nom Complet");
        colNom.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEtudiantNomComplet()));
        
        TableColumn<DatabaseConnector.NoteDetail, String> colFiliere = new TableColumn<>("Filière");
        colFiliere.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().filiereCode));

        TableColumn<DatabaseConnector.NoteDetail, String> colNiveau = new TableColumn<>("Niveau");
        colNiveau.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().niveauCode));

        TableColumn<DatabaseConnector.NoteDetail, String> colUE = new TableColumn<>("Code UE");
        colUE.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().ueCode));
        
        TableColumn<DatabaseConnector.NoteDetail, String> colEval = new TableColumn<>("Type");
        colEval.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().typeEvalCode));
        
        TableColumn<DatabaseConnector.NoteDetail, String> colVal = new TableColumn<>("Note");
        colVal.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().valeur)));
        
        TableColumn<DatabaseConnector.NoteDetail, String> colStatut = new TableColumn<>("Statut");
        colStatut.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().statut));

        notesTable.getColumns().clear();
        notesTable.getColumns().addAll(colMatricule, colNom, colFiliere, colNiveau, colUE, colEval, colVal, colStatut);
        notesTable.setItems(notesList);
        notesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        centerSection.getChildren().addAll(tableTitle, notesTable);
        
        // Assemblage final
        HBox mainContent = new HBox(leftSection, centerSection);
        HBox.setHgrow(centerSection, Priority.ALWAYS);
        root.setCenter(mainContent);

        // --- FOOTER ---
        Button btnLogout = new Button("Se déconnecter");
        btnLogout.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white;");
        btnLogout.setOnAction(e -> {
            App.setCurrentUser(null);
            App.showForm1();
        });
        
        root.setBottom(new HBox(btnLogout));
        BorderPane.setMargin(root.getBottom(), new Insets(20, 0, 0, 0));

        // Chargement initial
        loadInitialData();

        return new Scene(root, 1100, 700);
    }

    private static <T> ComboBox<T> createComboBox(String prompt, double width) {
        ComboBox<T> combo = new ComboBox<>();
        combo.setPromptText(prompt);
        combo.setPrefWidth(width);
        
        // CellFactory pour afficher correctement les libellés des objets du Lead
        combo.setCellFactory(lv -> new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else if (item instanceof DatabaseConnector.Filiere) setText(((DatabaseConnector.Filiere)item).getLibelle());
                else if (item instanceof DatabaseConnector.Niveau) setText(((DatabaseConnector.Niveau)item).getLibelle());
                else if (item instanceof DatabaseConnector.UE) setText(((DatabaseConnector.UE)item).getLibelle());
                else if (item instanceof DatabaseConnector.TypeEvaluation) setText(((DatabaseConnector.TypeEvaluation)item).getLibelle());
                else if (item instanceof DatabaseConnector.Etudiant) setText(((DatabaseConnector.Etudiant)item).getDisplayName());
            }
        });
        // Bouton de sélection aussi
        combo.setButtonCell(combo.getCellFactory().call(null));
        
        return combo;
    }

    private static void loadInitialData() {
        filiereCombo.setItems(FXCollections.observableArrayList(DatabaseConnector.getFilieres()));
        typeEvalCombo.setItems(FXCollections.observableArrayList(DatabaseConnector.getTypesEvaluation()));
    }

    private static void loadNiveauxAndUEs() {
        DatabaseConnector.Filiere f = filiereCombo.getValue();
        if (f != null) {
            niveauCombo.setItems(FXCollections.observableArrayList(DatabaseConnector.getNiveaux()));
        }
    }

    private static void loadUEsAndEtudiants() {
        DatabaseConnector.Filiere f = filiereCombo.getValue();
        DatabaseConnector.Niveau n = niveauCombo.getValue();
        if (f != null && n != null) {
            ueCombo.setItems(FXCollections.observableArrayList(DatabaseConnector.getUnitesEnseignementByFiliereNiveau(f.id, n.id)));
            etudiantCombo.setItems(FXCollections.observableArrayList(DatabaseConnector.getEtudiantsByFiliereNiveau(f.id, n.id)));
        }
    }

    private static void refreshTable() {
        DatabaseConnector.Filiere f = filiereCombo.getValue();
        DatabaseConnector.Niveau n = niveauCombo.getValue();
        DatabaseConnector.UE ue = ueCombo.getValue();
        
        notesList.clear();
        Integer fid = (f != null) ? f.id : null;
        Integer nid = (n != null) ? n.id : null;
        Integer uid = (ue != null) ? ue.id : null;
        
        notesList.addAll(DatabaseConnector.getNotes(fid, nid, uid));
    }

    private static void saveNote() {
        try {
            DatabaseConnector.UE ue = ueCombo.getValue();
            DatabaseConnector.TypeEvaluation te = typeEvalCombo.getValue();
            DatabaseConnector.Etudiant et = etudiantCombo.getValue();
            double val = Double.parseDouble(noteField.getText());
            DatabaseConnector.User user = App.getCurrentUser();

            if (ue == null || te == null || et == null) {
                statusLabel.setText("Erreur : Remplissez tous les champs !");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            boolean ok = DatabaseConnector.saveNote(et.id, ue.id, te.id, user.id, val);
            if (ok) {
                statusLabel.setText("✓ Note enregistrée !");
                statusLabel.setStyle("-fx-text-fill: green;");
                noteField.clear();
                Platform.runLater(() -> refreshTable());
            } else {
                statusLabel.setText("Erreur lors de l'enregistrement en BD.");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Note invalide !");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }
}
