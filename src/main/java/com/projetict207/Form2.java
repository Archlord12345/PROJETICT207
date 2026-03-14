package com.projetict207;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;

import java.util.List;

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

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color:#f5f5f5;");

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(20);

        Label title = new Label("Espace Enseignant");
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

        VBox formSection = new VBox(15);
        formSection.setPadding(new Insets(20));
        formSection.setStyle("-fx-background-color:white; -fx-background-radius:10; -fx-effect:dropshadow(gaussian, #ccc, 5, 0, 0, 2);");

        Label sectionTitle = new Label("Saisie des Notes");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(10);
        formGrid.setAlignment(Pos.CENTER_LEFT);

        Label filiereLabel = new Label("Filière:");
        filiereCombo = new ComboBox<>();
        filiereCombo.setPromptText("Sélectionner filière");
        filiereCombo.setMinWidth(250);
        filiereCombo.setCellFactory(new Callback<ListView<DatabaseConnector.Filiere>, ListCell<DatabaseConnector.Filiere>>() {
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
        filiereCombo.setOnAction(e -> loadNiveauxAndUEs());

        Label niveauLabel = new Label("Niveau:");
        niveauCombo = new ComboBox<>();
        niveauCombo.setPromptText("Sélectionner niveau");
        niveauCombo.setMinWidth(250);
        niveauCombo.setCellFactory(new Callback<ListView<DatabaseConnector.Niveau>, ListCell<DatabaseConnector.Niveau>>() {
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
        niveauCombo.setOnAction(e -> loadUEsForEnseignant());

        Label ueLabel = new Label("Unité d'Enseignement:");
        ueCombo = new ComboBox<>();
        ueCombo.setPromptText("Sélectionner UE");
        ueCombo.setMinWidth(250);
        ueCombo.setCellFactory(new Callback<ListView<DatabaseConnector.UE>, ListCell<DatabaseConnector.UE>>() {
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

        Label typeEvalLabel = new Label("Type d'évaluation:");
        typeEvalCombo = new ComboBox<>();
        typeEvalCombo.setPromptText("Sélectionner type");
        typeEvalCombo.setMinWidth(250);
        typeEvalCombo.setCellFactory(new Callback<ListView<DatabaseConnector.TypeEvaluation>, ListCell<DatabaseConnector.TypeEvaluation>>() {
            @Override
            public ListCell<DatabaseConnector.TypeEvaluation> call(ListView<DatabaseConnector.TypeEvaluation> p) {
                return new ListCell<DatabaseConnector.TypeEvaluation>() {
                    @Override
                    protected void updateItem(DatabaseConnector.TypeEvaluation item, boolean empty) {
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

        Label etudiantLabel = new Label("Étudiant:");
        etudiantCombo = new ComboBox<>();
        etudiantCombo.setPromptText("Sélectionner étudiant");
        etudiantCombo.setMinWidth(250);
        etudiantCombo.setCellFactory(new Callback<ListView<DatabaseConnector.Etudiant>, ListCell<DatabaseConnector.Etudiant>>() {
            @Override
            public ListCell<DatabaseConnector.Etudiant> call(ListView<DatabaseConnector.Etudiant> p) {
                return new ListCell<DatabaseConnector.Etudiant>() {
                    @Override
                    protected void updateItem(DatabaseConnector.Etudiant item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText("");
                        } else {
                            setText(item.getDisplayName());
                        }
                    }
                };
            }
        });

        Label noteLabel = new Label("Note (/20):");
        noteField = new TextField();
        noteField.setPromptText("Entrez la note");
        noteField.setMaxWidth(150);

        formGrid.add(filiereLabel, 0, 0);
        formGrid.add(filiereCombo, 1, 0);
        formGrid.add(niveauLabel, 0, 1);
        formGrid.add(niveauCombo, 1, 1);
        formGrid.add(ueLabel, 0, 2);
        formGrid.add(ueCombo, 1, 2);
        formGrid.add(typeEvalLabel, 0, 3);
        formGrid.add(typeEvalCombo, 1, 3);
        formGrid.add(etudiantLabel, 0, 4);
        formGrid.add(etudiantCombo, 1, 4);
        formGrid.add(noteLabel, 0, 5);
        formGrid.add(noteField, 1, 5);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        Button saveBtn = new Button("Enregistrer");
        saveBtn.setStyle("-fx-background-color:#27ae60; -fx-text-fill:white; -fx-padding:10 20; -fx-font-weight:bold;");
        saveBtn.setOnAction(e -> saveNote());

        Button clearBtn = new Button("Effacer");
        clearBtn.setStyle("-fx-background-color:#95a5a6; -fx-text-fill:white; -fx-padding:10 20;");
        clearBtn.setOnAction(e -> clearForm());

        statusLabel = new Label();
        statusLabel.setStyle("-fx-font-weight:bold;");

        buttonBox.getChildren().addAll(saveBtn, clearBtn, statusLabel);

        formGrid.add(buttonBox, 1, 6);

        formSection.getChildren().addAll(sectionTitle, formGrid);

        root.getChildren().addAll(header, formSection);

        loadFilieres();
        loadTypesEvaluation();

        return new Scene(root, 800, 500);
    }

    private static void loadFilieres() {
        List<DatabaseConnector.Filiere> filieres = DatabaseConnector.getFilieres();
        filiereCombo.setItems(FXCollections.observableArrayList(filieres));
    }

    private static void loadNiveauxAndUEs() {
        DatabaseConnector.Filiere filiere = filiereCombo.getValue();
        niveauCombo.setValue(null);
        ueCombo.setValue(null);
        
        if (filiere != null) {
            List<DatabaseConnector.Niveau> niveaux = DatabaseConnector.getNiveaux();
            niveauCombo.setItems(FXCollections.observableArrayList(niveaux));
        } else {
            niveauCombo.setItems(FXCollections.observableArrayList());
        }
        etudiantCombo.setItems(FXCollections.observableArrayList());
    }

    private static void loadUEsForEnseignant() {
        DatabaseConnector.Filiere filiere = filiereCombo.getValue();
        DatabaseConnector.Niveau niveau = niveauCombo.getValue();
        ueCombo.setValue(null);
        
        if (filiere != null && niveau != null) {
            List<DatabaseConnector.UE> ues = DatabaseConnector.getUnitesEnseignementByFiliereNiveau(filiere.id, niveau.id);
            ueCombo.setItems(FXCollections.observableArrayList(ues));
            loadEtudiants();
        } else {
            ueCombo.setItems(FXCollections.observableArrayList());
        }
    }

    private static void loadEtudiants() {
        DatabaseConnector.Filiere filiere = filiereCombo.getValue();
        DatabaseConnector.Niveau niveau = niveauCombo.getValue();
        
        if (filiere != null && niveau != null) {
            List<DatabaseConnector.Etudiant> etudiants = DatabaseConnector.getEtudiantsByFiliereNiveau(filiere.id, niveau.id);
            etudiantCombo.setItems(FXCollections.observableArrayList(etudiants));
        } else {
            etudiantCombo.setItems(FXCollections.observableArrayList());
        }
    }

    private static void loadTypesEvaluation() {
        List<DatabaseConnector.TypeEvaluation> types = DatabaseConnector.getTypesEvaluation();
        typeEvalCombo.setItems(FXCollections.observableArrayList(types));
    }

    private static void saveNote() {
        DatabaseConnector.UE ue = ueCombo.getValue();
        DatabaseConnector.TypeEvaluation typeEval = typeEvalCombo.getValue();
        DatabaseConnector.Etudiant etudiant = etudiantCombo.getValue();
        String noteStr = noteField.getText().trim();

        if (ue == null || typeEval == null || etudiant == null || noteStr.isEmpty()) {
            statusLabel.setText("Veuillez remplir tous les champs");
            statusLabel.setStyle("-fx-text-fill:red;");
            return;
        }

        try {
            double note = Double.parseDouble(noteStr);
            if (note < 0 || note > 20) {
                statusLabel.setText("La note doit être entre 0 et 20");
                statusLabel.setStyle("-fx-text-fill:red;");
                return;
            }

            DatabaseConnector.User user = App.getCurrentUser();
            boolean success = DatabaseConnector.saveNote(etudiant.id, ue.id, typeEval.id, user.id, note);

            if (success) {
                statusLabel.setText("Note enregistrée avec succès!");
                statusLabel.setStyle("-fx-text-fill:green;");
                clearForm();
            } else {
                statusLabel.setText("Erreur lors de l'enregistrement");
                statusLabel.setStyle("-fx-text-fill:red;");
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Note invalide");
            statusLabel.setStyle("-fx-text-fill:red;");
        }
    }

    private static void clearForm() {
        ueCombo.setValue(null);
        typeEvalCombo.setValue(null);
        etudiantCombo.setValue(null);
        noteField.clear();
    }
}
