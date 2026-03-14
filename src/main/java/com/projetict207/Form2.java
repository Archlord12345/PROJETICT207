package com.projetict207;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.collections.FXCollections;
import java.util.Optional;

public class Form2 {
    private static ComboBox<DatabaseConnector.Filiere> filiereCombo;
    private static ComboBox<DatabaseConnector.Niveau> niveauCombo;
    private static ComboBox<DatabaseConnector.UE> ueCombo;
    private static ComboBox<DatabaseConnector.TypeEvaluation> typeEvalCombo;
    private static ComboBox<DatabaseConnector.Etudiant> etudiantCombo;
    private static TextField noteField;
    private static Label statusLabel;

    public static Scene getScene() {
        DatabaseConnector.User user = App.getCurrentUser();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F1F5F9;"); 

        root.setTop(createNavBar(user));

        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(30));
        centerContent.setAlignment(Pos.TOP_CENTER);

        // Header
        VBox sectionHeader = new VBox(5);
        sectionHeader.setMaxWidth(600); 
        Label sectionTitle = new Label("Saisie des Notes");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        Label sectionSub = new Label("Veuillez sélectionner les critères pour enregistrer une note.");
        sectionSub.setTextFill(Color.web("#64748B"));
        sectionHeader.getChildren().addAll(sectionTitle, sectionSub);

        VBox formCard = new VBox(20);
        formCard.setMaxWidth(600);
        formCard.setPadding(new Insets(35));
        formCard.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(18);
        
    
        ColumnConstraints colLabels = new ColumnConstraints(120); 
        ColumnConstraints colFields = new ColumnConstraints();
        colFields.setHgrow(Priority.ALWAYS); 
        formGrid.getColumnConstraints().addAll(colLabels, colFields);

        // Composants
        filiereCombo = createStyledCombo("Sélectionner filière");
        filiereCombo.setOnAction(e -> loadNiveauxAndUEs());

        niveauCombo = createStyledCombo("Sélectionner niveau");
        niveauCombo.setOnAction(e -> loadUEsForEnseignant());

        ueCombo = createStyledCombo("Sélectionner UE");
        typeEvalCombo = createStyledCombo("Sélectionner type");
        etudiantCombo = createStyledCombo("Sélectionner étudiant");

        noteField = new TextField();
        noteField.setPromptText("Note / 20");
        styleInput(noteField);

        setupComboBoxFactories();

        // Ajout au Grid 
        addRow(formGrid, "Filière:", filiereCombo, 0);
        addRow(formGrid, "Niveau:", niveauCombo, 1);
        addRow(formGrid, "UE:", ueCombo, 2);
        addRow(formGrid, "Évaluation:", typeEvalCombo, 3);
        addRow(formGrid, "Étudiant:", etudiantCombo, 4);
        addRow(formGrid, "Note (/20):", noteField, 5);

        HBox actions = new HBox(15);
        actions.setAlignment(Pos.CENTER_RIGHT);
        actions.setPadding(new Insets(10, 0, 0, 0));

        Button clearBtn = new Button("Effacer");
        clearBtn.setStyle("-fx-background-color: #94A3B8; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 5; -fx-cursor: hand;");
        clearBtn.setOnAction(e -> clearForm());

        Button saveBtn = new Button("Enregistrer la Note");
        saveBtn.setStyle("-fx-background-color: #2563EB; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 25; -fx-background-radius: 5; -fx-cursor: hand;");
        saveBtn.setOnAction(e -> saveNote());

        statusLabel = new Label();
        statusLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 13));

        actions.getChildren().addAll(statusLabel, clearBtn, saveBtn);
        formCard.getChildren().addAll(formGrid, new Separator(), actions);

        centerContent.getChildren().addAll(sectionHeader, formCard);
        
        ScrollPane scrollPane = new ScrollPane(centerContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        root.setCenter(scrollPane);

        loadFilieres();
        loadTypesEvaluation();

        return new Scene(root, 1100, 750);
    }

    private static void setupComboBoxFactories() {
        
        filiereCombo.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(DatabaseConnector.Filiere item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getLibelle());
            }
        });
        filiereCombo.setButtonCell(filiereCombo.getCellFactory().call(null));

        niveauCombo.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(DatabaseConnector.Niveau item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getLibelle());
            }
        });
        niveauCombo.setButtonCell(niveauCombo.getCellFactory().call(null));

        ueCombo.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(DatabaseConnector.UE item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getLibelle());
            }
        });
        ueCombo.setButtonCell(ueCombo.getCellFactory().call(null));

        typeEvalCombo.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(DatabaseConnector.TypeEvaluation item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getLibelle());
            }
        });
        typeEvalCombo.setButtonCell(typeEvalCombo.getCellFactory().call(null));

        etudiantCombo.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(DatabaseConnector.Etudiant item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDisplayName());
            }
        });
        etudiantCombo.setButtonCell(etudiantCombo.getCellFactory().call(null));
    }

    private static void addRow(GridPane grid, String labelText, Control field, int row) {
        Label lbl = new Label(labelText);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        lbl.setTextFill(Color.web("#334155"));
        grid.add(lbl, 0, row);
        grid.add(field, 1, row);
        GridPane.setHgrow(field, Priority.ALWAYS); 
    }

    private static <T> ComboBox<T> createStyledCombo(String prompt) {
        ComboBox<T> combo = new ComboBox<>();
        combo.setPromptText(prompt);
        combo.setMaxWidth(Double.MAX_VALUE); 
        combo.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #E2E8F0; -fx-border-radius: 5;");
        return combo;
    }

    private static HBox createNavBar(DatabaseConnector.User user) {
        HBox navbar = new HBox();
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setPadding(new Insets(10, 30, 10, 30));
        navbar.setStyle("-fx-background-color: #2563EB;"); 
        navbar.setPrefHeight(65);
        Label logo = new Label("📚");
        logo.setFont(Font.font(22));
        Label brand = new Label("GESTION DES NOTES");
        brand.setTextFill(Color.WHITE);
        brand.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        HBox leftBox = new HBox(12, logo, brand);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        VBox userDetails = new VBox(-2);
        userDetails.setAlignment(Pos.CENTER_RIGHT);
        Label lblName = new Label(user.fullName);
        lblName.setTextFill(Color.WHITE);
        lblName.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label lblRole = new Label(user.role);
        lblRole.setTextFill(Color.web("#DBEAFE"));
        lblRole.setFont(Font.font("Arial", 11));
        userDetails.getChildren().addAll(lblName, lblRole);
        Button btnLogout = new Button("Déconnexion");
        btnLogout.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: white; -fx-border-radius: 5; -fx-cursor: hand; -fx-font-weight: bold;");
        btnLogout.setOnAction(e -> handleLogout());
        HBox rightBox = new HBox(20, userDetails, btnLogout);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        navbar.getChildren().addAll(leftBox, spacer, rightBox);
        return navbar;
    }

    private static void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Déconnexion");
        alert.setHeaderText("Souhaitez-vous vraiment vous déconnecter ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            App.setCurrentUser(null);
            App.showForm1();
        }
    }

    private static void styleInput(TextField tf) {
        tf.setMaxWidth(Double.MAX_VALUE);
        tf.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #E2E8F0; -fx-border-radius: 5; -fx-padding: 8;");
    }

    private static void loadFilieres() {
        filiereCombo.setItems(FXCollections.observableArrayList(DatabaseConnector.getFilieres()));
    }

    private static void loadNiveauxAndUEs() {
        DatabaseConnector.Filiere filiere = filiereCombo.getValue();
        niveauCombo.setValue(null);
        ueCombo.setValue(null);
        if (filiere != null) {
            niveauCombo.setItems(FXCollections.observableArrayList(DatabaseConnector.getNiveaux()));
        }
    }

    private static void loadUEsForEnseignant() {
        DatabaseConnector.Filiere filiere = filiereCombo.getValue();
        DatabaseConnector.Niveau niveau = niveauCombo.getValue();
        if (filiere != null && niveau != null) {
            ueCombo.setItems(FXCollections.observableArrayList(DatabaseConnector.getUnitesEnseignementByFiliereNiveau(filiere.id, niveau.id)));
            loadEtudiants();
        }
    }

    private static void loadEtudiants() {
        DatabaseConnector.Filiere filiere = filiereCombo.getValue();
        DatabaseConnector.Niveau niveau = niveauCombo.getValue();
        if (filiere != null && niveau != null) {
            etudiantCombo.setItems(FXCollections.observableArrayList(DatabaseConnector.getEtudiantsByFiliereNiveau(filiere.id, niveau.id)));
        }
    }

    private static void loadTypesEvaluation() {
        typeEvalCombo.setItems(FXCollections.observableArrayList(DatabaseConnector.getTypesEvaluation()));
    }

    private static void saveNote() {
        DatabaseConnector.UE ue = ueCombo.getValue();
        DatabaseConnector.TypeEvaluation typeEval = typeEvalCombo.getValue();
        DatabaseConnector.Etudiant etudiant = etudiantCombo.getValue();
        String noteStr = noteField.getText().trim();
        if (ue == null || typeEval == null || etudiant == null || noteStr.isEmpty()) {
            statusLabel.setText("⚠️ Champs manquants");
            statusLabel.setStyle("-fx-text-fill: #E11D48;");
            return;
        }
        try {
            double note = Double.parseDouble(noteStr);
            if (note < 0 || note > 20) {
                statusLabel.setText("❌ Note invalide (0-20)");
                statusLabel.setStyle("-fx-text-fill: #E11D48;");
                return;
            }
            boolean success = DatabaseConnector.saveNote(etudiant.id, ue.id, typeEval.id, App.getCurrentUser().id, note);
            if (success) {
                statusLabel.setText("✅ Note enregistrée !");
                statusLabel.setStyle("-fx-text-fill: #059669;");
                noteField.clear();
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("❌ Format invalide");
            statusLabel.setStyle("-fx-text-fill: #E11D48;");
        }
    }

    private static void clearForm() {
        filiereCombo.setValue(null);
        niveauCombo.setValue(null);
        ueCombo.setValue(null);
        typeEvalCombo.setValue(null);
        etudiantCombo.setValue(null);
        noteField.clear();
        statusLabel.setText("");
    }
}