package com.projetict207;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import javafx.stage.Stage;

import java.util.List;
import java.util.ArrayList;

public class Form3 {
    private static TableView<NoteRow> notesTable;
    private static ObservableList<NoteRow> notesData = FXCollections.observableArrayList();
    private static ComboBox<DatabaseConnector.Filiere> filiereFilterCombo;
    private static ComboBox<DatabaseConnector.Niveau> niveauFilterCombo;
    private static ComboBox<DatabaseConnector.UE> ueFilterCombo;
    private static Label totalLabel, valideesLabel, enAttenteLabel;

    public static Scene getScene() {
        DatabaseConnector.User user = App.getCurrentUser();

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color:#f5f5f5;");

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(20);

        Label title = new Label("Espace Jury");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Label userLabel = new Label("Bienvenue, " + user.fullName + " (" + user.role + ")");
        userLabel.setStyle("-fx-text-fill:#666;");

        Button logoutBtn = new Button("Déconnexion");
        logoutBtn.setStyle("-fx-background-color:#e74c3c; -fx-text-fill:white; -fx-padding:8 15;");
        logoutBtn.setOnAction(e -> {
            App.setCurrentUser(null);
            App.showForm1();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        header.getChildren().addAll(title, spacer, userLabel, logoutBtn);

        VBox filterSection = new VBox(10);
        filterSection.setPadding(new Insets(15));
        filterSection.setStyle("-fx-background-color:white; -fx-background-radius:10; -fx-effect:dropshadow(gaussian, #ccc, 5, 0, 0, 2);");

        HBox filterBox = new HBox(15);
        filterBox.setAlignment(Pos.CENTER_LEFT);

        Label filterTitle = new Label("Filtres:");
        filterTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label filiereLabel = new Label("Filière:");
        filiereFilterCombo = new ComboBox<>();
        filiereFilterCombo.setPromptText("Toutes les filières");
        filiereFilterCombo.setMinWidth(150);
        filiereFilterCombo.setCellFactory(new Callback<ListView<DatabaseConnector.Filiere>, ListCell<DatabaseConnector.Filiere>>() {
            @Override
            public ListCell<DatabaseConnector.Filiere> call(ListView<DatabaseConnector.Filiere> p) {
                return new ListCell<DatabaseConnector.Filiere>() {
                    @Override
                    protected void updateItem(DatabaseConnector.Filiere item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText("");
                        } else {
                            setText(item.getLibelle());
                        }
                    }
                };
            }
        });
        filiereFilterCombo.setOnAction(e -> loadNiveauxAndUEs());

        Label niveauLabel = new Label("Niveau:");
        niveauFilterCombo = new ComboBox<>();
        niveauFilterCombo.setPromptText("Tous les niveaux");
        niveauFilterCombo.setMinWidth(150);
        niveauFilterCombo.setCellFactory(new Callback<ListView<DatabaseConnector.Niveau>, ListCell<DatabaseConnector.Niveau>>() {
            @Override
            public ListCell<DatabaseConnector.Niveau> call(ListView<DatabaseConnector.Niveau> p) {
                return new ListCell<DatabaseConnector.Niveau>() {
                    @Override
                    protected void updateItem(DatabaseConnector.Niveau item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText("");
                        } else {
                            setText(item.getLibelle());
                        }
                    }
                };
            }
        });
        niveauFilterCombo.setOnAction(e -> loadNotes());

        Label ueLabel = new Label("UE:");
        ueFilterCombo = new ComboBox<>();
        ueFilterCombo.setPromptText("Toutes les UE");
        ueFilterCombo.setMinWidth(150);
        ueFilterCombo.setCellFactory(new Callback<ListView<DatabaseConnector.UE>, ListCell<DatabaseConnector.UE>>() {
            @Override
            public ListCell<DatabaseConnector.UE> call(ListView<DatabaseConnector.UE> p) {
                return new ListCell<DatabaseConnector.UE>() {
                    @Override
                    protected void updateItem(DatabaseConnector.UE item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText("");
                        } else {
                            setText(item.getLibelle());
                        }
                    }
                };
            }
        });
        ueFilterCombo.setOnAction(e -> loadNotes());

        Button refreshBtn = new Button("Actualiser");
        refreshBtn.setStyle("-fx-background-color:#3498db; -fx-text-fill:white; -fx-padding:8 15;");
        refreshBtn.setOnAction(e -> loadNotes());

        filterBox.getChildren().addAll(filterTitle, filiereLabel, filiereFilterCombo, niveauLabel, niveauFilterCombo, ueLabel, ueFilterCombo, refreshBtn);
        filterSection.getChildren().add(filterBox);

        VBox tableSection = new VBox(10);
        tableSection.setPadding(new Insets(15));
        tableSection.setStyle("-fx-background-color:white; -fx-background-radius:10; -fx-effect:dropshadow(gaussian, #ccc, 5, 0, 0, 2);");

        Label tableTitle = new Label("Gestion des Notes et Validations");
        tableTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        notesTable = new TableView<>();
        notesTable.setEditable(false);
        notesTable.setMinHeight(300);

        TableColumn<NoteRow, String> colMatricule = new TableColumn<>("Matricule");
        colMatricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        colMatricule.setMinWidth(100);

        TableColumn<NoteRow, String> colEtudiant = new TableColumn<>("Étudiant");
        colEtudiant.setCellValueFactory(new PropertyValueFactory<>("etudiantNom"));
        colEtudiant.setMinWidth(150);

        TableColumn<NoteRow, String> colFiliere = new TableColumn<>("Filière");
        colFiliere.setCellValueFactory(new PropertyValueFactory<>("filiereCode"));
        colFiliere.setMinWidth(80);

        TableColumn<NoteRow, String> colNiveau = new TableColumn<>("Niveau");
        colNiveau.setCellValueFactory(new PropertyValueFactory<>("niveauCode"));
        colNiveau.setMinWidth(70);

        TableColumn<NoteRow, String> colUE = new TableColumn<>("UE");
        colUE.setCellValueFactory(new PropertyValueFactory<>("ueCode"));
        colUE.setMinWidth(100);

        TableColumn<NoteRow, String> colType = new TableColumn<>("Type Évaluation");
        colType.setCellValueFactory(new PropertyValueFactory<>("typeEval"));
        colType.setMinWidth(100);

        TableColumn<NoteRow, Double> colNote = new TableColumn<>("Note");
        colNote.setCellValueFactory(new PropertyValueFactory<>("valeur"));
        colNote.setMinWidth(60);

        TableColumn<NoteRow, String> colStatut = new TableColumn<>("Statut");
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colStatut.setMinWidth(90);

        notesTable.getColumns().addAll(colMatricule, colEtudiant, colFiliere, colNiveau, colUE, colType, colNote, colStatut);
        notesTable.setItems(notesData);

        HBox actionBox = new HBox(10);
        actionBox.setAlignment(Pos.CENTER_LEFT);

        Button validerBtn = new Button("Valider Sélection");
        validerBtn.setStyle("-fx-background-color:#27ae60; -fx-text-fill:white; -fx-padding:10 20; -fx-font-weight:bold;");
        validerBtn.setOnAction(e -> validerNotes());

        Button pvBtn = new Button("Générer PV");
        pvBtn.setStyle("-fx-background-color:#8e44ad; -fx-text-fill:white; -fx-padding:10 20;");
        pvBtn.setOnAction(e -> genererPV());

        actionBox.getChildren().addAll(validerBtn, pvBtn);

        tableSection.getChildren().addAll(tableTitle, notesTable, actionBox);

        VBox statsSection = new VBox(10);
        statsSection.setPadding(new Insets(15));
        statsSection.setStyle("-fx-background-color:white; -fx-background-radius:10; -fx-effect:dropshadow(gaussian, #ccc, 5, 0, 0, 2);");

        Label statsTitle = new Label("Statistiques du Jury");
        statsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        HBox statsBox = new HBox(30);
        statsBox.setAlignment(Pos.CENTER_LEFT);

        totalLabel = new Label("Notes total: 0");
        valideesLabel = new Label("Validées: 0");
        enAttenteLabel = new Label("En attente: 0");

        totalLabel.setStyle("-fx-font-size:14; -fx-font-weight:bold;");
        valideesLabel.setStyle("-fx-font-size:14; -fx-text-fill:#27ae60;");
        enAttenteLabel.setStyle("-fx-font-size:14; -fx-text-fill:#e67e22;");

        statsBox.getChildren().addAll(totalLabel, valideesLabel, enAttenteLabel);
        statsSection.getChildren().addAll(statsTitle, statsBox);

        root.getChildren().addAll(header, filterSection, tableSection, statsSection);

        loadFilters();
        loadNotes();

        return new Scene(root, 1100, 700);
    }

    private static void loadFilters() {
        List<DatabaseConnector.Filiere> filieres = DatabaseConnector.getFilieres();
        filiereFilterCombo.setItems(FXCollections.observableArrayList(filieres));

        List<DatabaseConnector.Niveau> niveaux = DatabaseConnector.getNiveaux();
        niveauFilterCombo.setItems(FXCollections.observableArrayList(niveaux));

        List<DatabaseConnector.UE> ues = DatabaseConnector.getUnitesEnseignement();
        ueFilterCombo.setItems(FXCollections.observableArrayList(ues));
    }

    private static void loadNiveauxAndUEs() {
        DatabaseConnector.Filiere filiere = filiereFilterCombo.getValue();
        
        if (filiere != null) {
            List<DatabaseConnector.Niveau> niveaux = DatabaseConnector.getNiveaux();
            niveauFilterCombo.setItems(FXCollections.observableArrayList(niveaux));
            
            List<DatabaseConnector.UE> ues = DatabaseConnector.getUnitesEnseignementByFiliereNiveau(filiere.id, null);
            ueFilterCombo.setItems(FXCollections.observableArrayList(ues));
        } else {
            niveauFilterCombo.setItems(FXCollections.observableArrayList());
            ueFilterCombo.setItems(FXCollections.observableArrayList());
        }
        loadNotes();
    }

    private static void loadNotes() {
        notesData.clear();
        
        Integer filiereId = filiereFilterCombo.getValue() != null ? filiereFilterCombo.getValue().id : null;
        Integer niveauId = niveauFilterCombo.getValue() != null ? niveauFilterCombo.getValue().id : null;
        Integer ueId = ueFilterCombo.getValue() != null ? ueFilterCombo.getValue().id : null;
        
        List<DatabaseConnector.NoteDetail> notes = DatabaseConnector.getNotes(filiereId, niveauId, ueId);
        
        int total = 0, validees = 0;
        for (DatabaseConnector.NoteDetail n : notes) {
            notesData.add(new NoteRow(n.id, n.matricule, n.getEtudiantNomComplet(), n.filiereCode, n.niveauCode, n.ueCode, n.typeEvalCode, n.valeur, n.statut));
            total++;
            if ("VALIDEE".equals(n.statut)) validees++;
        }
        
        totalLabel.setText("Notes total: " + total);
        valideesLabel.setText("Validées: " + validees);
        enAttenteLabel.setText("En attente: " + (total - validees));
    }

    private static void validerNotes() {
        ObservableList<NoteRow> selected = notesTable.getSelectionModel().getSelectedItems();
        if (selected.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner des notes à valider");
            alert.showAndWait();
            return;
        }

        List<Integer> noteIds = new ArrayList<>();
        for (NoteRow row : selected) {
            noteIds.add(row.id);
        }

        DatabaseConnector.User user = App.getCurrentUser();
        boolean success = DatabaseConnector.validerNotes(noteIds, user.id);

        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validation");
            alert.setHeaderText(null);
            alert.setContentText(noteIds.size() + " note(s) validée(s) avec succès!");
            alert.showAndWait();
            loadNotes();
        }
    }

    private static void genererPV() {
        DatabaseConnector.UE ue = ueFilterCombo.getValue();
        if (ue == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Génération PV");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une UE pour générer le PV");
            alert.showAndWait();
            return;
        }

        DatabaseConnector.User user = App.getCurrentUser();
        DatabaseConnector.PVResult result = DatabaseConnector.generatePV(ue.id, 1, "2025-2026", user.id);

        if (result != null) {
            String message = "PV généré avec succès!\n\n" +
                "Fichier: " + result.fileName + "\n" +
                "Effectif total: " + result.effectifTotal + "\n" +
                "Capacité (CA): " + result.nbCa + " (" + String.format("%.1f", result.effectifTotal > 0 ? result.nbCa * 100.0 / result.effectifTotal : 0) + "%)\n" +
                "Capacité avec dette (CANT): " + result.nbCant + "\n" +
                "Non Capable (NC): " + result.nbNc + "\n" +
                "Éliminé (EL): " + result.nbEl;

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Génération PV");
            alert.setHeaderText("Procès-Verbal généré");
            alert.setContentText(message);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de la génération du PV");
            alert.showAndWait();
        }
    }

    public static class NoteRow {
        private final int id;
        private final String matricule;
        private final String etudiantNom;
        private final String filiereCode;
        private final String niveauCode;
        private final String ueCode;
        private final String typeEval;
        private final double valeur;
        private final String statut;

        public NoteRow(int id, String matricule, String etudiantNom, String filiereCode, String niveauCode, String ueCode, String typeEval, double valeur, String statut) {
            this.id = id;
            this.matricule = matricule;
            this.etudiantNom = etudiantNom;
            this.filiereCode = filiereCode != null ? filiereCode : "-";
            this.niveauCode = niveauCode != null ? niveauCode : "-";
            this.ueCode = ueCode;
            this.typeEval = typeEval;
            this.valeur = valeur;
            this.statut = statut;
        }

        public int getId() { return id; }
        public String getMatricule() { return matricule; }
        public String getEtudiantNom() { return etudiantNom; }
        public String getFiliereCode() { return filiereCode; }
        public String getNiveauCode() { return niveauCode; }
        public String getUeCode() { return ueCode; }
        public String getTypeEval() { return typeEval; }
        public Double getValeur() { return valeur; }
        public String getStatut() { return statut; }
    }
}
