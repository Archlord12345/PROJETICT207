// package com.projetict207;

// import javafx.scene.Scene;
// import javafx.scene.control.Button;
// import javafx.scene.control.Label;
// import javafx.scene.layout.VBox;

// public class Form2 {
//     public static Scene getScene() {
//         VBox layout = new VBox(10);
//         layout.setPadding(new javafx.geometry.Insets(20));

//         Label label = new Label("This is Form2");
//         Button backToForm1 = new Button("Back to Form1");
//         backToForm1.setOnAction(e -> App.showForm1());

//         layout.getChildren().addAll(label, backToForm1);
//         return new Scene(layout, 320, 240);
//     }
// }

package com.projetict207;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.sql.*;
import java.util.Optional;

public class Form2 {

    private static TableView<NoteEntry> table = new TableView<>();
    private static Label lblTotal = new Label("0");
    private static Label lblValide = new Label("0");
    private static Label lblAttente = new Label("0");

    public static Scene getScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F8FAFC;");

        // 1. Barre de navigation (Top)
        root.setTop(createNavBar());

        // 2. Contenu principal
        VBox mainContent = new VBox(25);
        mainContent.setPadding(new Insets(30));

        // En-tête de page
        VBox pageHeader = new VBox(5);
        Label mainTitle = new Label("Saisie des Notes");
        mainTitle.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        Label subTitle = new Label("Entrez les notes de vos étudiants par matière et type d'évaluation");
        subTitle.setTextFill(Color.web("#64748B"));
        pageHeader.getChildren().addAll(mainTitle, subTitle);

        // Zone centrale : Formulaire (Gauche) + Tableau (Droite)
        HBox middleLayout = new HBox(25);
        middleLayout.setAlignment(Pos.TOP_CENTER);

        // --- FORMULAIRE (GAVCHE) ---
        VBox formCard = new VBox(15);
        formCard.setMinWidth(320);
        formCard.setStyle("-fx-background-color: white; -fx-padding: 25; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);");

        Label formTitle = new Label("+ Nouvelle Note");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TextField txtMatricule = new TextField(); txtMatricule.setPromptText("Ex: 24H2001");
        ComboBox<String> comboUE = new ComboBox<>(FXCollections.observableArrayList("ICT201", "ICT203", "ICT205", "ICT207", "ICT213"));
        ComboBox<String> comboType = new ComboBox<>(FXCollections.observableArrayList("CC", "TP", "SN"));
        TextField txtNote = new TextField(); txtNote.setPromptText("0");

        Button btnAdd = new Button("Ajouter la note");
        btnAdd.setMaxWidth(Double.MAX_VALUE);
        btnAdd.setStyle("-fx-background-color: #2563EB; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12; -fx-background-radius: 8; -fx-cursor: hand;");

        formCard.getChildren().addAll(formTitle, new Label("Étudiant"), txtMatricule, new Label("Matière"), comboUE, new Label("Type d'évaluation"), comboType, new Label("Note (/20)"), txtNote, btnAdd);

        // --- TABLEAU (DROITE) ---
        VBox tableCard = new VBox(15);
        HBox.setHgrow(tableCard, Priority.ALWAYS);
        tableCard.setStyle("-fx-background-color: white; -fx-padding: 25; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);");
        
        Label tableTitle = new Label("Notes saisies");
        tableTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        configureTable();
        tableCard.getChildren().addAll(tableTitle, table);

        middleLayout.getChildren().addAll(formCard, tableCard);

        // --- STATISTIQUES (BAS) ---
        HBox statsLayout = new HBox(20);
        statsLayout.setAlignment(Pos.CENTER);
        statsLayout.getChildren().addAll(
            createStatCard("Notes saisies", lblTotal, "#2563EB"),
            createStatCard("Notes validées (>=10)", lblValide, "#10B981"),
            createStatCard("En attente de validation (<10)", lblAttente, "#3B82F6")
        );

        mainContent.getChildren().addAll(pageHeader, middleLayout, statsLayout);
        root.setCenter(mainContent);

        // Action d'ajout
        btnAdd.setOnAction(e -> {
            try {
                String mat = txtMatricule.getText();
                String ue = comboUE.getValue();
                String type = comboType.getValue();
                double noteVal = Double.parseDouble(txtNote.getText());
                
                sauvegarderNoteIndividuelle(mat, ue, type, noteVal);
                refreshData();
                txtNote.clear();
            } catch (Exception ex) {
                showError("Saisie invalide.");
            }
        });

        refreshData(); // Charger les données au démarrage
        return new Scene(root, 1200, 850);
    }

    private static void configureTable() {
        TableColumn<NoteEntry, String> colMat = new TableColumn<>("Étudiant");
        colMat.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().matricule));

        TableColumn<NoteEntry, String> colUE = new TableColumn<>("Matière");
        colUE.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().ue));

        TableColumn<NoteEntry, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().type));

        TableColumn<NoteEntry, String> colNote = new TableColumn<>("Note");
        colNote.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().note + "/20"));

        table.getColumns().setAll(colMat, colUE, colType, colNote);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(400);
    }

    private static void refreshData() {
        ObservableList<NoteEntry> data = FXCollections.observableArrayList();
        int total = 0, valide = 0, attente = 0;

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM notes_details")) {
            
            while (rs.next()) {
                double val = rs.getDouble("note");
                data.add(new NoteEntry(rs.getString("matricule"), rs.getString("ue"), rs.getString("type"), val));
                total++;
                if (val >= 10) valide++; else attente++;
            }
        } catch (SQLException e) {
            // Si la table n'existe pas encore (premier lancement), on la crée
            createTableDetails();
        }
        
        table.setItems(data);
        lblTotal.setText(String.valueOf(total));
        lblValide.setText(String.valueOf(valide));
        lblAttente.setText(String.valueOf(attente));
    }

    private static VBox createStatCard(String title, Label val, String color) {
        VBox card = new VBox(5);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(350);
        card.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);");
        
        val.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        val.setTextFill(Color.web(color));
        Label lblT = new Label(title);
        lblT.setTextFill(Color.web("#64748B"));
        
        card.getChildren().addAll(val, lblT);
        return card;
    }

    private static void sauvegarderNoteIndividuelle(String mat, String ue, String type, double note) {
        String sql = "INSERT OR REPLACE INTO notes_details(matricule, ue, type, note) VALUES(?,?,?,?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, mat);
            pstmt.setString(2, ue);
            pstmt.setString(3, type);
            pstmt.setDouble(4, note);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private static void createTableDetails() {
        try (Connection conn = DatabaseConnector.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS notes_details (matricule TEXT, ue TEXT, type TEXT, note REAL, PRIMARY KEY(matricule, ue, type))");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // --- REPRISE DE LA NAVBAR DU CODE PRÉCÉDENT ---
    private static HBox createNavBar() {
        HBox navbar = new HBox();
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setPadding(new Insets(10, 30, 10, 30));
        navbar.setStyle("-fx-background-color: #2563EB;");
        navbar.setPrefHeight(60);

        Label brand = new Label("📚 GESTION DES NOTES");
        brand.setTextFill(Color.WHITE);
        brand.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnLogout = new Button("↪ Déconnexion");
        btnLogout.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-color: white; -fx-border-radius: 5;");
        btnLogout.setOnAction(e -> handleLogout());

        navbar.getChildren().addAll(brand, spacer, btnLogout);
        return navbar;
    }

    private static void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Se déconnecter ?", ButtonType.YES, ButtonType.NO);
        if (alert.showAndWait().get() == ButtonType.YES) App.showForm1();
    }

    private static void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).show();
    }

    // Classe interne pour le modèle de données du tableau
    public static class NoteEntry {
        String matricule, ue, type;
        double note;
        public NoteEntry(String m, String u, String t, double n) {
            this.matricule = m; this.ue = u; this.type = t; this.note = n;
        }
    }
}