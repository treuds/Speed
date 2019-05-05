package com.ciayiti.speed;

import java.util.Date;

/**
 * Created by bienvenue on 5/18/2017.
 */

public class Trajet {

    private String Identification_trajet;
    private String Identification_chauf;
    private String Identification_enq;
    private String Ligne_trajet;
    private double Prix_fixe; //prix fixe en gourde
    private String Sens_trajet;
    private long  Temps_charg; //Temps de chargement en milisecondes
    private int Capacite_vehi;
    private String Type_vehi;
    private String Heure_dep;
    private double Coord_dep[];
    private int Nbre_persD;
    private String Heure_arv;
    private double Coord_arv[];
    private int Nbre_persArv;
    private String commentaires;


    public Trajet() {
    }

    public Trajet(String ligne_trajet, double prix_fixe, String sens_trajet) {
        Ligne_trajet = ligne_trajet;
        Prix_fixe = prix_fixe;
        Sens_trajet = sens_trajet;
    }

    public Trajet(String identification_enq, String ligne_trajet, String heure_dep, double[] coord_dep) {
        Identification_enq = identification_enq;
        Ligne_trajet = ligne_trajet;
        Heure_dep = heure_dep;
        Coord_dep = coord_dep;
    }

    public Trajet( String identification_enq, String ligne_trajet,String type_vehi, int capacite_vehi,double prix, String identification_chauf, String heure_dep, double[] coord_dep) {
        Identification_chauf = identification_chauf;
        Identification_enq = identification_enq;
        Ligne_trajet = ligne_trajet;
        Capacite_vehi = capacite_vehi;
        Prix_fixe=prix;
        Type_vehi = type_vehi;
        Heure_dep = heure_dep;
        Coord_dep = coord_dep;
    }
}
