package com.ciayiti.speed;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by bienvenue on 5/21/2017.
 */

public class Arret {
     public String Identification_arret;
     public String Identification_trajet;
     public String   Heure_debut;
     public List Coord_arr;
     public int Pers_Des;
     public int Pers_Mon;
     public String Heure_fin;

    public Arret() {
    }

    public Arret(String identification_trajet, String heure_debut, double coord_arr[], int pers_Des, int pers_Mon, String heure_fin) {
        Identification_trajet = identification_trajet;
        Heure_debut = heure_debut;
        Coord_arr = Arrays.asList(coord_arr);
        Pers_Des = pers_Des;
        Pers_Mon = pers_Mon;
        Heure_fin = heure_fin;
    }

    public void setIdentificationtrajet(String identification_trajet) {
        Identification_trajet = identification_trajet;
    }

    public void setHeuredebut(String heure_debut) {

        Heure_debut = heure_debut;
    }

    public void setCoordarr(List<Double> coord_arr) {
        Coord_arr = Arrays.asList(coord_arr);
    }

    public void setPersdes(int pers_Des) {
        Pers_Des = pers_Des;
    }

    public void setPersmon(int pers_Mon) {
        Pers_Mon = pers_Mon;
    }
    public void setHeurefin(String heure_fin) {
        Heure_fin = heure_fin;
    }

}
