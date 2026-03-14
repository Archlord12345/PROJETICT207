package com.projetict207;

import javafx.beans.property.*;

/**
 * MISSION POUR TRECY (LOT A - Structure technique)
 * 
 * Bonjour Trecy ! Ton rôle est de définir l'objet "Etudiant" qui sera utilisé 
 * par Jemina (pour la saisie) et Elisée (pour l'affichage).
 * 
 * Consignes :
 * 1. Définis les attributs : id (int), nom (String), prenom (String), note (double).
 * 2. Utilise les propriétés JavaFX (SimpleStringProperty, SimpleDoubleProperty) 
 *    pour que le tableau d'Elisée puisse se mettre à jour automatiquement.
 * 3. Crée un constructeur complet.
 * 4. Ajoute les getters et setters.
 */
public class Etudiant {
    // Trecy, commence à coder ici :
    
    private final IntegerProperty id;
    private final StringProperty nom;
    private final StringProperty prenom;
    private final DoubleProperty note;

    public Etudiant(int id, String nom, String prenom, double note) {
        this.id = new SimpleIntegerProperty(id);
        this.nom = new SimpleStringProperty(nom);
        this.prenom = new SimpleStringProperty(prenom);
        this.note = new SimpleDoubleProperty(note);
    }

    // Getters pour les propriétés (Elisée en aura besoin pour son tableau)
    public IntegerProperty idProperty() { return id; }
    public StringProperty nomProperty() { return nom; }
    public StringProperty prenomProperty() { return prenom; }
    public DoubleProperty noteProperty() { return note; }

    // Getters et Setters classiques
    public int getId() { return id.get(); }
    public String getNom() { return nom.get(); }
    public String getPrenom() { return prenom.get(); }
    public double getNote() { return note.get(); }
}
