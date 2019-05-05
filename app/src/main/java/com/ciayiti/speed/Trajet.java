package com.ciayiti.speed;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by bienvenue on 5/18/2017.
 */

public class Trajet {

     String Identification_trajet;
    String Identification_chauf;
    String Identification_enq;
    String Ligne_trajet;
    double Prix_fixe; //prix fixe en gourde
    String Sens_trajet;
    long  Temps_charg; //Temps de chargement en milisecondes
    int Capacite_vehi;
    String Type_vehi;
    String Heure_dep;
     List Coord_dep;
     int Nbre_persD;
     String Heure_arv;
     List Coord_arv;
     int Nbre_persArv;
    String commentaires;


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
        Coord_dep = Arrays.asList(coord_dep);
    }

    public Trajet( String identification_enq, String ligne_trajet,String type_vehi, int capacite_vehi,double prix, String identification_chauf, String heure_dep, double[] coord_dep) {
        Identification_chauf = identification_chauf;
        Identification_enq = identification_enq;
        Ligne_trajet = ligne_trajet;
        Capacite_vehi = capacite_vehi;
        Prix_fixe=prix;
        Type_vehi = type_vehi;
        Heure_dep = heure_dep;
        Coord_dep = Arrays.asList(coord_dep);
    }
}
