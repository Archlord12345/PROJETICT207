package com.projetict207;

import javafx.beans.property.*;

/**
 * CLASSE ETUDIANT - Version Harmonisée
 * 
 * Basée sur le travail de Trecy (LOT A) avec les ajouts techniques
 * nécessaires pour Jemina (LOT B) et Elisée (LOT C).
 */
public class Etudiant {
    private final IntegerProperty id;
    private final StringProperty matricule;
    private final StringProperty nom;
    private final StringProperty prenom;
    private final DoubleProperty note;
    private final StringProperty filiere;
    private final StringProperty niveau;

    // Constructeur complet (pour Trecy)
    public Etudiant(int id, String matricule, String nom, String prenom, double note, String filiere, String niveau) {
        this.id = new SimpleIntegerProperty(id);
        this.matricule = new SimpleStringProperty(matricule);
        this.nom = new SimpleStringProperty(nom);
        this.prenom = new SimpleStringProperty(prenom);
        this.note = new SimpleDoubleProperty(note);
        this.filiere = new SimpleStringProperty(filiere);
        this.niveau = new SimpleStringProperty(niveau);
    }

    // Getters pour les Properties (Indispensable pour le tableau d'Elisée)
    public IntegerProperty idProperty() { return id; }
    public StringProperty matriculeProperty() { return matricule; }
    public StringProperty nomProperty() { return nom; }
    public StringProperty prenomProperty() { return prenom; }
    public DoubleProperty noteProperty() { return note; }
    public StringProperty filiereProperty() { return filiere; }
    public StringProperty niveauProperty() { return niveau; }

    // Getters et Setters classiques (Pour Jemina et Trecy)
    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }

    public String getMatricule() { return matricule.get(); }
    public void setMatricule(String value) { matricule.set(value); }

    public String getNom() { return nom.get(); }
    public void setNom(String value) { nom.set(value); }

    public String getPrenom() { return prenom.get(); }
    public void setPrenom(String value) { prenom.set(value); }

    public double getNote() { return note.get(); }
    public void setNote(double value) { note.set(value); }

    public String getFiliere() { return filiere.get(); }
    public void setFiliere(String value) { filiere.set(value); }

    public String getNiveau() { return niveau.get(); }
    public void setNiveau(String value) { niveau.set(value); }

    @Override
    public String toString() {
        return getNom() + " " + getPrenom() + " (" + getMatricule() + ") - Note: " + getNote();
    }
}
