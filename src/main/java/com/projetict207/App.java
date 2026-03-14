package com.projetict207;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        DatabaseConnector.initDatabase();
        showForm1();
        stage.setTitle("JavaFX App : GESTION DES NOTES ICT-L2");
        stage.setMaximized(true);
        stage.show();
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
