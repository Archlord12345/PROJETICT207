// package com.projetict207;

// import javafx.scene.Scene;
// import javafx.scene.control.Button;
// import javafx.scene.control.Label;
// import javafx.scene.layout.VBox;

// public class Form1 {
//     public static Scene getScene() {
//         VBox layout = new VBox(10);
//         layout.setPadding(new javafx.geometry.Insets(20));

//         Label label = new Label("This is Form1");
//         Button toForm2 = new Button("Go to Form2");
//         toForm2.setOnAction(e -> App.showForm2());

//         Button toForm3 = new Button("Go to Form3");
//         toForm3.setOnAction(e -> App.showForm3());

//         layout.getChildren().addAll(label, toForm2, toForm3);
//         return new Scene(layout, 320, 240);
//     }
// }

package com.projetict207;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Form1 {

    public static Scene getScene() {
        // --- CONTENEUR PRINCIPAL (Fond bleu très clair) ---
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #F0F9FF;"); // Light Blue background
        root.setPadding(new Insets(40));

        // --- EN-TÊTE (Logo et Titres) ---
        VBox header = new VBox(5);
        header.setAlignment(Pos.CENTER);
        
        // Icône (Utilise un Label avec emoji ou une image si tu as le fichier)
        Label logo = new Label("📚"); 
        logo.setStyle("-fx-font-size: 50; -fx-background-color: #2563EB; -fx-background-radius: 10; -fx-padding: 10;");
        logo.setTextFill(Color.WHITE);

        Label title = new Label("GESTION DES NOTES");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#0F172A"));

        Label university = new Label("Université de Yaoundé I");
        university.setTextFill(Color.web("#64748B"));

        Label description = new Label("Système de saisie et validation des notes académiques");
        description.setTextFill(Color.web("#475569"));
        description.setPadding(new Insets(10, 0, 20, 0));

        header.getChildren().addAll(logo, title, university, description);

        // --- CARTE DE CONNEXION (Le rectangle blanc) ---
        VBox card = new VBox(15);
        card.setMaxWidth(400);
        card.setStyle("-fx-background-color: white; " +
                     "-fx-background-radius: 15; " +
                     "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5); " +
                     "-fx-padding: 30;");

        Label connTitle = new Label("Connexion");
        connTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        
        Label connSubtitle = new Label("Entrez vos identifiants pour accéder au système");
        connSubtitle.setTextFill(Color.web("#64748B"));

        // Champs de saisie
        VBox fields = new VBox(10);
        
        Label lblUser = new Label("Nom d'utilisateur");
        lblUser.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        TextField userField = new TextField();
        userField.setPromptText("Ex: jury1");
        styleInput(userField);

        Label lblPass = new Label("Mot de passe");
        lblPass.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        PasswordField passField = new PasswordField();
        passField.setPromptText("••••");
        styleInput(passField);

        // Bouton Se Connecter
        Button btnLogin = new Button("Se connecter");
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        btnLogin.setStyle("-fx-background-color: #2563EB; -fx-text-fill: white; -fx-font-weight: bold; " +
                         "-fx-padding: 12; -fx-background-radius: 8; -fx-cursor: hand;");
        
        // Logique de connexion simple
        btnLogin.setOnAction(e -> {
            String user = userField.getText();
            if (user.equals("prof1")) App.showForm2(); // Vers Saisie
            else if (user.equals("jury1") || user.equals("admin")) App.showForm3(); // Vers PV
            else showAlert();
        });

        // --- ZONE INFOS DE TEST (Le petit bloc bleu en bas) ---
        VBox testInfo = new VBox(5);
        testInfo.setStyle("-fx-background-color: #EFF6FF; -fx-padding: 15; -fx-background-radius: 10; -fx-border-color: #DBEAFE; -fx-border-radius: 10;");
        
        Label lblTest = new Label("Identifiants de test :");
        lblTest.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        lblTest.setTextFill(Color.web("#1E40AF"));

        testInfo.getChildren().addAll(lblTest, 
            creerLigneInfo("Enseignant :", "prof1 / 1234"),
            creerLigneInfo("Jury :", "jury1 / 1234"),
            creerLigneInfo("Admin :", "admin / 1234")
        );

        fields.getChildren().addAll(lblUser, userField, lblPass, passField);
        card.getChildren().addAll(connTitle, connSubtitle, fields, btnLogin, testInfo);

        // --- FOOTER ---
        Label footer = new Label("© 2026 Université de Yaoundé I - Tous droits réservés");
        footer.setTextFill(Color.web("#64748B"));
        footer.setFont(Font.font(12));

        root.getChildren().addAll(header, card, footer);

        return new Scene(root, 1000, 800);
    }

    // Méthode utilitaire pour styliser les champs de texte
    private static void styleInput(Control input) {
        input.setStyle("-fx-background-color: #F1F5F9; -fx-border-color: #E2E8F0; " +
                      "-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10;");
    }

    // Méthode pour créer les lignes d'info de test
    private static HBox creerLigneInfo(String role, String pass) {
        HBox hbox = new HBox();
        Label r = new Label(role); r.setTextFill(Color.web("#2563EB")); r.setMinWidth(100);
        Label p = new Label(pass); p.setTextFill(Color.web("#1E40AF"));
        hbox.getChildren().addAll(r, p);
        return hbox;
    }

    private static void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Identifiants incorrects !");
        alert.showAndWait();
    }
}