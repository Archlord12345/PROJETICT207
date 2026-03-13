package com.projetict207;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Form1 {
    public static Scene getScene() {
        VBox layout = new VBox(10);
        layout.setPadding(new javafx.geometry.Insets(20));

        Label label = new Label("This is Form1");
        Button toForm2 = new Button("Go to Form2");
        toForm2.setOnAction(e -> App.showForm2());

        Button toForm3 = new Button("Go to Form3");
        toForm3.setOnAction(e -> App.showForm3());

        layout.getChildren().addAll(label, toForm2, toForm3);
        return new Scene(layout, 320, 240);
    }
}
