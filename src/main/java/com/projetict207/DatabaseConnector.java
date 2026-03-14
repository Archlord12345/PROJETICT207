package com.projetict207;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnector {
    private static final String DB_URL = "jdbc:sqlite:gestion_notes.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static User authenticate(String username, String password) {
        String sql = "SELECT id, username, full_name, email, role FROM users WHERE username = ? AND password_hash = ? AND active = 1";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("full_name"), rs.getString("email"), rs.getString("role"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur authentification: " + e.getMessage());
        }
        return null;
    }

    public static List<Filiere> getFilieres() {
        List<Filiere> list = new ArrayList<>();
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM filieres ORDER BY code")) {
            while (rs.next()) {
                list.add(new Filiere(rs.getInt("id"), rs.getString("code"), rs.getString("nom")));
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return list;
    }

    public static List<Niveau> getNiveaux() {
        List<Niveau> list = new ArrayList<>();
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM niveaux ORDER BY ordre")) {
            while (rs.next()) {
                list.add(new Niveau(rs.getInt("id"), rs.getString("code"), rs.getString("nom")));
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return list;
    }

    public static List<UE> getUnitesEnseignement() {
        List<UE> list = new ArrayList<>();
        String sql = "SELECT ue.*, f.code as filiere_code, n.code as niveau_code FROM unites_ensemble ue LEFT JOIN filieres f ON ue.filiere_id = f.id LEFT JOIN niveaux n ON ue.niveau_id = n.id";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new UE(rs.getInt("id"), rs.getString("code"), rs.getString("nom"), rs.getDouble("coefficient"), rs.getString("filiere_code"), rs.getString("niveau_code")));
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return list;
    }

    public static List<UE> getUnitesEnseignementByFiliereNiveau(Integer filiereId, Integer niveauId) {
        List<UE> list = new ArrayList<>();
        String sql = "SELECT ue.*, f.code as filiere_code, n.code as niveau_code FROM unites_ensemble ue LEFT JOIN filieres f ON ue.filiere_id = f.id LEFT JOIN niveaux n ON ue.niveau_id = n.id WHERE 1=1";
        if (filiereId != null) sql += " AND ue.filiere_id = " + filiereId;
        if (niveauId != null) sql += " AND ue.niveau_id = " + niveauId;
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new UE(rs.getInt("id"), rs.getString("code"), rs.getString("nom"), rs.getDouble("coefficient"), rs.getString("filiere_code"), rs.getString("niveau_code")));
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return list;
    }

    public static List<Etudiant> getEtudiants() {
        List<Etudiant> list = new ArrayList<>();
        String sql = "SELECT e.*, f.code as filiere_code, n.code as niveau_code FROM etudiants e LEFT JOIN filieres f ON e.filiere_id = f.id LEFT JOIN niveaux n ON e.niveau_id = n.id WHERE e.actif = 1 ORDER BY e.matricule";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Etudiant(rs.getInt("id"), rs.getString("matricule"), rs.getString("nom"), rs.getString("prenom"), rs.getString("filiere_code"), rs.getString("niveau_code")));
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return list;
    }

    public static List<Etudiant> getEtudiantsByFiliereNiveau(Integer filiereId, Integer niveauId) {
        List<Etudiant> list = new ArrayList<>();
        String sql = "SELECT e.*, f.code as filiere_code, n.code as niveau_code FROM etudiants e LEFT JOIN filieres f ON e.filiere_id = f.id LEFT JOIN niveaux n ON e.niveau_id = n.id WHERE e.actif = 1";
        if (filiereId != null) sql += " AND e.filiere_id = " + filiereId;
        if (niveauId != null) sql += " AND e.niveau_id = " + niveauId;
        sql += " ORDER BY e.matricule";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Etudiant(rs.getInt("id"), rs.getString("matricule"), rs.getString("nom"), rs.getString("prenom"), rs.getString("filiere_code"), rs.getString("niveau_code")));
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return list;
    }

    public static List<TypeEvaluation> getTypesEvaluation() {
        List<TypeEvaluation> list = new ArrayList<>();
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM types_evaluation")) {
            while (rs.next()) {
                list.add(new TypeEvaluation(rs.getInt("id"), rs.getString("code"), rs.getString("nom")));
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return list;
    }

    public static List<NoteDetail> getNotes(Integer filiereId, Integer niveauId, Integer ueId) {
        List<NoteDetail> list = new ArrayList<>();
        String sql = "SELECT n.id, n.valeur, n.statut, e.matricule, e.nom as etudiant_nom, e.prenom as etudiant_prenom, " +
                     "f.code as filiere_code, nv.code as niveau_code, " +
                     "ue.code as ue_code, ue.nom as ue_nom, " +
                     "te.code as type_eval_code, te.nom as type_eval_nom " +
                     "FROM notes n " +
                     "JOIN etudiants e ON n.etudiant_id = e.id " +
                     "LEFT JOIN filieres f ON e.filiere_id = f.id " +
                     "LEFT JOIN niveaux nv ON e.niveau_id = nv.id " +
                     "JOIN unites_ensemble ue ON n.ue_id = ue.id " +
                     "JOIN types_evaluation te ON n.type_evaluation_id = te.id " +
                     "WHERE 1=1";
        if (filiereId != null) sql += " AND e.filiere_id = " + filiereId;
        if (niveauId != null) sql += " AND e.niveau_id = " + niveauId;
        if (ueId != null) sql += " AND n.ue_id = " + ueId;
        sql += " ORDER BY e.matricule, ue.code, te.code";
        
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new NoteDetail(
                    rs.getInt("id"),
                    rs.getString("matricule"),
                    rs.getString("etudiant_nom"),
                    rs.getString("etudiant_prenom"),
                    rs.getString("filiere_code"),
                    rs.getString("niveau_code"),
                    rs.getString("ue_code"),
                    rs.getString("ue_nom"),
                    rs.getString("type_eval_code"),
                    rs.getString("type_eval_nom"),
                    rs.getDouble("valeur"),
                    rs.getString("statut")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return list;
    }

    public static boolean saveNote(int etudiantId, int ueId, int typeEvalId, int enseignantId, double valeur) {
        String sql = "INSERT OR REPLACE INTO notes (etudiant_id, ue_id, type_evaluation_id, enseignant_id, valeur, statut) VALUES (?, ?, ?, ?, ?, 'SAISIE')";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, etudiantId);
            pstmt.setInt(2, ueId);
            pstmt.setInt(3, typeEvalId);
            pstmt.setInt(4, enseignantId);
            pstmt.setDouble(5, valeur);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur sauvegarde note: " + e.getMessage());
            return false;
        }
    }

    public static boolean validerNotes(List<Integer> noteIds, int validePar) {
        String sql = "UPDATE notes SET statut = 'VALIDEE', valide_par = ?, date_validation = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (Integer id : noteIds) {
                pstmt.setInt(1, validePar);
                pstmt.setInt(2, id);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur validation: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateNote(int noteId, double newValue) {
        String sql = "UPDATE notes SET valeur = ?, statut = 'SAISIE' WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, newValue);
            pstmt.setInt(2, noteId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour note: " + e.getMessage());
            return false;
        }
    }

    public static PVResult generatePV(int ueId, int semestreId, String anneeAcademique, int generePar) {
        String sql = "SELECT e.matricule, e.nom, e.prenom, f.code as filiere_code, nv.code as niveau_code, " +
                     "ue.code as ue_code, ue.nom as ue_nom, " +
                     "SUM(CASE WHEN te.code = 'CC' THEN n.valeur ELSE 0 END) as cc, " +
                     "SUM(CASE WHEN te.code = 'TP' THEN n.valeur ELSE 0 END) as tp, " +
                     "SUM(CASE WHEN te.code = 'EE' THEN n.valeur * 2 ELSE 0 END) as ee, " +
                     "SUM(CASE WHEN te.code = 'EP' THEN n.valeur * 2 ELSE 0 END) as ep " +
                     "FROM notes n " +
                     "JOIN etudiants e ON n.etudiant_id = e.id " +
                     "LEFT JOIN filieres f ON e.filiere_id = f.id " +
                     "LEFT JOIN niveaux nv ON e.niveau_id = nv.id " +
                     "JOIN unites_ensemble ue ON n.ue_id = ue.id " +
                     "JOIN types_evaluation te ON n.type_evaluation_id = te.id " +
                     "WHERE n.ue_id = ? " +
                     "GROUP BY e.id " +
                     "ORDER BY e.matricule";
        
        List<PVLine> lines = new ArrayList<>();
        int effectifTotal = 0, nbCa = 0, nbCant = 0, nbNc = 0, nbEl = 0;
        
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ueId);
            ResultSet rs = pstmt.executeQuery();
            
            String ueCode = "", ueNom = "";
            while (rs.next()) {
                if (ueCode.isEmpty()) {
                    ueCode = rs.getString("ue_code");
                    ueNom = rs.getString("ue_nom");
                }
                
                double cc = rs.getDouble("cc");
                double tp = rs.getDouble("tp");
                double ee = rs.getDouble("ee");
                double ep = rs.getDouble("ep");
                double total = cc + tp + ee + ep;
                
                String decision;
                if (total >= 50) decision = "CA";
                else if (total >= 35) decision = "CANT";
                else if (total > 0) decision = "NC";
                else decision = "EL";
                
                switch (decision) {
                    case "CA": nbCa++; break;
                    case "CANT": nbCant++; break;
                    case "NC": nbNc++; break;
                    case "EL": nbEl++; break;
                }
                effectifTotal++;
                
                lines.add(new PVLine(rs.getString("matricule"), rs.getString("nom") + " " + rs.getString("prenom"),
                    rs.getString("filiere_code"), rs.getString("niveau_code"), cc, tp, ee, ep, total, decision));
            }
            
            String pvFileName = String.format("PV_UE_%s_%s__S%d_%s__%s.pdf", ueCode, ueCode, semestreId, anneeAcademique, 
                new java.text.SimpleDateFormat("dd_MM_yyyy_HH_mm").format(new java.util.Date()));
            
            String filiereCode = "";
            String niveauCode = "";
            for (PVLine line : lines) {
                if (filiereCode.isEmpty() && line.filiereCode != null) filiereCode = line.filiereCode;
                if (niveauCode.isEmpty() && line.niveauCode != null) niveauCode = line.niveauCode;
            }
            
            try {
                String dateGen = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
                PDFGenerator.generatePV(pvFileName, ueCode, ueNom, filiereCode != null ? filiereCode : "", niveauCode != null ? niveauCode : "",
                    String.valueOf(semestreId), anneeAcademique, dateGen, lines, effectifTotal, nbCa, nbCant, nbNc, nbEl);
            } catch (Exception e) {
                System.err.println("Erreur génération PDF: " + e.getMessage());
            }
            
            String insertPv = "INSERT INTO proces_verbaux (ue_id, semestre_id, annee_academique, genere_par, nom_fichier, " +
                "effectif_total, effectif_ca, effectif_cant, effectif_nc, effectif_el, " +
                "pourcentage_ca, pourcentage_cant, pourcentage_nc, pourcentage_el, statut) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'BROUILLON')";
            
            try (PreparedStatement pstmt2 = conn.prepareStatement(insertPv)) {
                pstmt2.setInt(1, ueId);
                pstmt2.setInt(2, semestreId);
                pstmt2.setString(3, anneeAcademique);
                pstmt2.setInt(4, generePar);
                pstmt2.setString(5, pvFileName);
                pstmt2.setInt(6, effectifTotal);
                pstmt2.setInt(7, nbCa);
                pstmt2.setInt(8, nbCant);
                pstmt2.setInt(9, nbNc);
                pstmt2.setInt(10, nbEl);
                pstmt2.setDouble(11, effectifTotal > 0 ? (nbCa * 100.0 / effectifTotal) : 0);
                pstmt2.setDouble(12, effectifTotal > 0 ? (nbCant * 100.0 / effectifTotal) : 0);
                pstmt2.setDouble(13, effectifTotal > 0 ? (nbNc * 100.0 / effectifTotal) : 0);
                pstmt2.setDouble(14, effectifTotal > 0 ? (nbEl * 100.0 / effectifTotal) : 0);
                pstmt2.executeUpdate();
            }
            
            return new PVResult(pvFileName, lines, effectifTotal, nbCa, nbCant, nbNc, nbEl);
            
        } catch (SQLException e) {
            System.err.println("Erreur génération PV: " + e.getMessage());
            return null;
        }
    }

    public static class User {
        public final int id;
        public final String username;
        public final String fullName;
        public final String email;
        public final String role;
        
        public User(int id, String username, String fullName, String email, String role) {
            this.id = id;
            this.username = username;
            this.fullName = fullName;
            this.email = email;
            this.role = role;
        }
    }

    public static class Filiere {
        public final int id;
        public final String code;
        public final String nom;
        
        public Filiere(int id, String code, String nom) {
            this.id = id;
            this.code = code;
            this.nom = nom;
        }
        
        public String getLibelle() { return code + " - " + nom; }
    }

    public static class Niveau {
        public final int id;
        public final String code;
        public final String nom;
        
        public Niveau(int id, String code, String nom) {
            this.id = id;
            this.code = code;
            this.nom = nom;
        }
        
        public String getLibelle() { return code + " - " + nom; }
    }

    public static class Etudiant {
        public final int id;
        public final String matricule;
        public final String nom;
        public final String prenom;
        public final String filiereCode;
        public final String niveauCode;
        
        public Etudiant(int id, String matricule, String nom, String prenom, String filiereCode, String niveauCode) {
            this.id = id;
            this.matricule = matricule;
            this.nom = nom;
            this.prenom = prenom;
            this.filiereCode = filiereCode;
            this.niveauCode = niveauCode;
        }
        
        public String getNomComplet() { return nom + " " + prenom; }
        public String getDisplayName() { return matricule + " - " + nom + " " + prenom + " (" + filiereCode + ")"; }
    }

    public static class UE {
        public final int id;
        public final String code;
        public final String nom;
        public final double coefficient;
        public final String filiereCode;
        public final String niveauCode;
        
        public UE(int id, String code, String nom, double coefficient, String filiereCode, String niveauCode) {
            this.id = id;
            this.code = code;
            this.nom = nom;
            this.coefficient = coefficient;
            this.filiereCode = filiereCode;
            this.niveauCode = niveauCode;
        }
        
        public String getLibelle() { return code + " - " + nom + " (" + filiereCode + "/" + niveauCode + ")"; }
    }

    public static class TypeEvaluation {
        public final int id;
        public final String code;
        public final String nom;
        
        public TypeEvaluation(int id, String code, String nom) {
            this.id = id;
            this.code = code;
            this.nom = nom;
        }
        
        public String getLibelle() { return code + " - " + nom; }
    }

    public static class NoteDetail {
        public final int id;
        public final String matricule;
        public final String etudiantNom;
        public final String etudiantPrenom;
        public final String filiereCode;
        public final String niveauCode;
        public final String ueCode;
        public final String ueNom;
        public final String typeEvalCode;
        public final String typeEvalNom;
        public final double valeur;
        public final String statut;
        
        public NoteDetail(int id, String matricule, String etudiantNom, String etudiantPrenom, String filiereCode, String niveauCode,
            String ueCode, String ueNom, String typeEvalCode, String typeEvalNom, double valeur, String statut) {
            this.id = id;
            this.matricule = matricule;
            this.etudiantNom = etudiantNom;
            this.etudiantPrenom = etudiantPrenom;
            this.filiereCode = filiereCode;
            this.niveauCode = niveauCode;
            this.ueCode = ueCode;
            this.ueNom = ueNom;
            this.typeEvalCode = typeEvalCode;
            this.typeEvalNom = typeEvalNom;
            this.valeur = valeur;
            this.statut = statut;
        }
        
        public String getEtudiantNomComplet() { return etudiantNom + " " + etudiantPrenom; }
    }

    public static class PVLine {
        public final String matricule;
        public final String nomComplet;
        public final String filiereCode;
        public final String niveauCode;
        public final double cc;
        public final double tp;
        public final double ee;
        public final double ep;
        public final double total;
        public final String decision;
        
        public PVLine(String matricule, String nomComplet, String filiereCode, String niveauCode, double cc, double tp, double ee, double ep, double total, String decision) {
            this.matricule = matricule;
            this.nomComplet = nomComplet;
            this.filiereCode = filiereCode;
            this.niveauCode = niveauCode;
            this.cc = cc;
            this.tp = tp;
            this.ee = ee;
            this.ep = ep;
            this.total = total;
            this.decision = decision;
        }
    }

    public static class PVResult {
        public final String fileName;
        public final List<PVLine> lines;
        public final int effectifTotal;
        public final int nbCa, nbCant, nbNc, nbEl;
        
        public PVResult(String fileName, List<PVLine> lines, int effectifTotal, int nbCa, int nbCant, int nbNc, int nbEl) {
            this.fileName = fileName;
            this.lines = lines;
            this.effectifTotal = effectifTotal;
            this.nbCa = nbCa;
            this.nbCant = nbCant;
            this.nbNc = nbNc;
            this.nbEl = nbEl;
        }
    }
}
