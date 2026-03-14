package com.projetict207;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Form1 {

    private static TextField username;
    private static PasswordField password;
    private static Label errorLabel;

    public static Scene getScene() {
        
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #F0F9FF;"); 
        root.setPadding(new Insets(40));

        
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);

       
        Label logo = new Label("📚");
        logo.setStyle("-fx-font-size: 45; -fx-background-color: #2563EB; -fx-background-radius: 12; -fx-padding: 8;");
        logo.setTextFill(Color.WHITE);

        Label title = new Label("GESTION DES NOTES");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        title.setTextFill(Color.web("#0F172A"));

        Label university = new Label("Université de Yaoundé I");
        university.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        university.setTextFill(Color.web("#64748B"));

        Label desc = new Label("Système de saisie et validation des notes académiques");
        desc.setTextFill(Color.web("#475569"));
        desc.setPadding(new Insets(5, 0, 15, 0));

        header.getChildren().addAll(logo, title, university, desc);

        
        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(30));
        formBox.setMaxWidth(400);
        
        
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 15;");
        DropShadow shadow = new DropShadow(15, Color.color(0, 0, 0, 0.1));
        formBox.setEffect(shadow);

        Label loginTitle = new Label("Connexion");
        loginTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        loginTitle.setTextFill(Color.web("#1E293B"));

        Label subtitleLogin = new Label("Entrez vos identifiants pour accéder au système");
        subtitleLogin.setTextFill(Color.web("#64748B"));
        subtitleLogin.setPadding(new Insets(0, 0, 10, 0));

        VBox fields = new VBox(8);
        
        Label userLabel = new Label("Nom d'utilisateur");
        userLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        username = new TextField();
        username.setPromptText("jury1");
        styleInput(username);

        Label passLabel = new Label("Mot de passe");
        passLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        password = new PasswordField();
        password.setPromptText("••••");
        styleInput(password);

        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #EF4444; -fx-font-size: 12;");

        
        Button loginBtn = new Button("Se connecter");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setCursor(javafx.scene.Cursor.HAND);
        loginBtn.setStyle("-fx-background-color: #2563EB; -fx-text-fill: white; -fx-font-weight: bold; " +
                         "-fx-padding: 12; -fx-background-radius: 8; -fx-font-size: 14;");

        loginBtn.setOnAction(e -> authenticate());

        
        VBox testBox = new VBox(6);
        testBox.setPadding(new Insets(15));
        testBox.setStyle("-fx-background-color: #EFF6FF; -fx-border-color: #DBEAFE; " +
                        "-fx-background-radius: 10; -fx-border-radius: 10;");

        Label testTitle = new Label("Identifiants de test :");
        testTitle.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        testTitle.setTextFill(Color.web("#1E40AF"));

        HBox profLine = createInfoLine("Enseignant :", "prof / 1234");
        HBox juryLine = createInfoLine("Jury :", "jury / 1234");

        testBox.getChildren().addAll(testTitle, profLine, juryLine);

        formBox.getChildren().addAll(
                loginTitle, subtitleLogin, 
                userLabel, username, 
                passLabel, password, 
                errorLabel, loginBtn, testBox
        );

        
        Label footer = new Label("© 2026 Université de Yaoundé I - Tous droits réservés");
        footer.setTextFill(Color.web("#64748B"));
        footer.setFont(Font.font(12));

        root.getChildren().addAll(header, formBox, footer);

        return new Scene(root, 800, 700);
    }

    private static void styleInput(Control input) {
        input.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #E2E8F0; " +
                      "-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10;");
    }

    private static HBox createInfoLine(String label, String value) {
        HBox row = new HBox();
        Label lbl = new Label(label);
        lbl.setTextFill(Color.web("#2563EB"));
        lbl.setPrefWidth(100);
        Label val = new Label(value);
        val.setTextFill(Color.web("#1E40AF"));
        row.getChildren().addAll(lbl, val);
        return row;
    }

    private static void authenticate() {
        String user = username.getText().trim();
        String pass = password.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            errorLabel.setText("Veuillez entrer login et mot de passe");
            return;
        }

        // Appel à la base de données
        DatabaseConnector.User authenticatedUser = DatabaseConnector.authenticate(user, pass);

        if (authenticatedUser != null) {
            App.setCurrentUser(authenticatedUser);
            errorLabel.setText("");

            switch (authenticatedUser.role.toLowerCase()) {
                case "enseignant":
                    App.showForm2();
                    break;
                case "jury":
                    App.showForm3();
                    break;
                default:
                    errorLabel.setText("Rôle non reconnu");
            }
        } else {
            errorLabel.setText("Identifiants incorrects");
            password.clear();
        }
    }
}