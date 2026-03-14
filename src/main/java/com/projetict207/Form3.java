// package com.projetict207;

// import javafx.scene.Scene;
// import javafx.scene.control.Button;
// import javafx.scene.control.Label;
// import javafx.scene.layout.VBox;

// public class Form3 {
//     public static Scene getScene() {
//         VBox layout = new VBox(10);
//         layout.setPadding(new javafx.geometry.Insets(20));

//         Label label = new Label("This is Form3");
//         Button backToForm1 = new Button("Back to Form1");
//         backToForm1.setOnAction(e -> App.showForm1());

//         layout.getChildren().addAll(label, backToForm1);
//         return new Scene(layout, 320, 240);
//     }
// }

package com.projetict207;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import java.sql.*;

public class Form3 {
    public static Scene getScene() {
        BorderPane layout = new BorderPane();
        TextArea area = new TextArea(); // Pour simplifier au début, on affiche en texte brut
        
        Button btnCharger = new Button("Charger le PV depuis SQLite");
        btnCharger.setOnAction(e -> {
            area.setText("MATRICULE | UE | MOYENNE (20/40/40)\n");
            try (Connection conn = DatabaseConnector.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM notes")) {
                while (rs.next()) {
                    double moy = (rs.getDouble("cc")*0.2) + (rs.getDouble("tp")*0.4) + (rs.getDouble("sn")*0.4);
                    area.appendText(rs.getString("matricule") + " | " + rs.getString("ue") + " | " + String.format("%.2f", moy) + "\n");
                }
            } catch (SQLException ex) { ex.printStackTrace(); }
        });

        layout.setTop(btnCharger);
        layout.setCenter(area);
        return new Scene(layout, 600, 400);
    }
}