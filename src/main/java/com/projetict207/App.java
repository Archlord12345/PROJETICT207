package com.projetict207;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Stage primaryStage;
    private static DatabaseConnector.User currentUser;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        showForm1();
        stage.setTitle("Gestion des Notes - UY1");
        stage.setMaximized(true);
        stage.show();
    }

    public static void setCurrentUser(DatabaseConnector.User user) {
        currentUser = user;
    }

    public static DatabaseConnector.User getCurrentUser() {
        return currentUser;
    }

    public static void showForm1() {
        Scene scene = Form1.getScene();
        primaryStage.setScene(scene);
    }

    public static void showForm2() {
        Scene scene = Form2.getScene();
        primaryStage.setScene(scene);
    }

    public static void showForm3() {
        Scene scene = Form3.getScene();
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}
