// package com.projetict207;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.SQLException;

// public class DatabaseConnector {
//     private static final String URL = "jdbc:sqlite:database.db"; // Path to your SQLite database file

//     public static Connection getConnection() throws SQLException {
//         return DriverManager.getConnection(URL);
//     }

//     public static void createTableIfNotExists() {
//         String sql = "CREATE TABLE IF NOT EXISTS users (" +
//                      "id INTEGER PRIMARY KEY AUTOINCREMENT," +
//                      "name TEXT NOT NULL," +
//                      "email TEXT NOT NULL UNIQUE" +
//                      ");";
//         try (Connection conn = getConnection();
//              var stmt = conn.createStatement()) {
//             stmt.execute(sql);
//             System.out.println("Table 'users' created or already exists.");
//         } catch (SQLException e) {
//             System.out.println("Error creating table: " + e.getMessage());
//         }
//     }
// }

package com.projetict207;
import java.sql.*;

public class DatabaseConnector {
    private static final String URL = "jdbc:sqlite:gestion_notes.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            // Création de la table Notes
            stmt.execute("CREATE TABLE IF NOT EXISTS notes (" +
                         "matricule TEXT, " +
                         "ue TEXT, " +
                         "cc REAL, " +
                         "tp REAL, " +
                         "sn REAL, " +
                         "PRIMARY KEY (matricule, ue))");
            System.out.println("Base de données initialisée !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}