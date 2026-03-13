package com.projetict207;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Form2 {
    public static Scene getScene() {
        VBox layout = new VBox(10);
        layout.setPadding(new javafx.geometry.Insets(20));

        Label label = new Label("This is Form2");
        Button backToForm1 = new Button("Back to Form1");
        backToForm1.setOnAction(e -> App.showForm1());

        layout.getChildren().addAll(label, backToForm1);
        return new Scene(layout, 320, 240);
    }
}
