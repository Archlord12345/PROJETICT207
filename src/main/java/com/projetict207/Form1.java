package com.projetict207;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Form1 {

    private static TextField username;
    private static PasswordField password;
    private static Label errorLabel;

    public static Scene getScene() {

        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color:#dfe8ec;");

        Label title = new Label("GESTION DES NOTES");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));

        Label subtitle = new Label("Université de Yaoundé I");
        subtitle.setFont(Font.font(16));

        Label desc = new Label("Système de saisie et validation des notes académiques");

        VBox header = new VBox(5);
        header.setAlignment(Pos.CENTER);
        header.getChildren().addAll(title, subtitle, desc);

        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(25));
        formBox.setMaxWidth(400);

        formBox.setStyle("-fx-background-color:white;" +
                        "-fx-background-radius:10;" +
                        "-fx-border-radius:10;");

        Label loginTitle = new Label("Connexion");
        loginTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Label userLabel = new Label("Nom d'utilisateur");
        username = new TextField();
        username.setPromptText("Entrez votre identifiant");

        Label passLabel = new Label("Mot de passe");
        password = new PasswordField();

        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill:red; -fx-font-size:12;");

        Button loginBtn = new Button("Se connecter");
        loginBtn.setMaxWidth(Double.MAX_VALUE);

        loginBtn.setStyle("-fx-background-color:#6a11cb;" +
                        "-fx-text-fill:white;" +
                        "-fx-font-weight:bold;" +
                        "-fx-padding:10;" +
                        "-fx-background-radius:8;");

        loginBtn.setOnAction(e -> authenticate());

        VBox testBox = new VBox(5);
        testBox.setPadding(new Insets(10));
        testBox.setStyle("-fx-background-color:#eef3ff;" +
                        "-fx-border-color:#c5d0ff;" +
                        "-fx-background-radius:6;");

                Label testTitle = new Label("Identifiants de test :");
                Label prof = new Label("Enseignant : prof / 1234");
                Label jury = new Label("Jury : jury / 1234");

        testBox.getChildren().addAll(testTitle, prof, jury);

        formBox.getChildren().addAll(
                        loginTitle,
                        userLabel,
                        username,
                        passLabel,
                        password,
                        errorLabel,
                        loginBtn,
                        testBox);

        Label footer = new Label("© 2026 Université de Yaoundé I");

        root.getChildren().addAll(header, formBox, footer);

        return new Scene(root, 700, 500);
    }

    private static void authenticate() {
        String user = username.getText().trim();
        String pass = password.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            errorLabel.setText("Veuillez entrer login et mot de passe");
            return;
        }

        DatabaseConnector.User authenticatedUser = DatabaseConnector.authenticate(user, pass);

        if (authenticatedUser != null) {
            App.setCurrentUser(authenticatedUser);
            errorLabel.setText("");

            switch (authenticatedUser.role) {
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