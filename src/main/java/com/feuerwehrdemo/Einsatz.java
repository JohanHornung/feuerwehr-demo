package com.feuerwehrdemo;

import com.feuerwehrdemo.Fahrzeug;

import java.util.HashMap;
import java.util.Scanner;

public class Einsatz {
    public int id;
    public String einsatzart;
    public HashMap<String, Integer> parameter;

    final public static String[] einsatzarten = {"Wohnungsbrand", "Verkehrsunfall", "Naturkatastrophe", "Industrieunfall"};
    final public static int[][] minParameter = {
            {22, 1, 2, 1, 1},
            {16, 1, 1, 1, 0},
            {55, 3, 3, 3, 2},
            {40, 3, 2, 2, 2}
    };


    public Einsatz(int id, String einsatzart, HashMap<String, Integer> parameter) {
        this.id = id;
        this.einsatzart = einsatzart;
        this.parameter = parameter;
    }

    // Hashmap of needed resources for deploy
    final public static HashMap<String, int[]> einsatzParameter = new HashMap<>();

    public static void setEinsatzParameter() {
        for (int i = 0; i < einsatzarten.length; i++) {
            einsatzParameter.put(einsatzarten[i], minParameter[i]);
        }
    };

    public static Feuerwehrmann[] team = new Feuerwehrmann[80];
    public static Fahrzeug[] garage = new Fahrzeug[18];

    public static void fillResources() {
        Fahrzeug.anzahlVerfuegbar.put(Fahrzeug.Kategorie.EINSATZLEITFAHRZEUG, 4);
        Fahrzeug.anzahlVerfuegbar.put(Fahrzeug.Kategorie.TANKLOESCHFAHRZEUG, 5);
        Fahrzeug.anzahlVerfuegbar.put(Fahrzeug.Kategorie.MANNSCHAFTSTRANSPORTER, 4);
        Fahrzeug.anzahlVerfuegbar.put(Fahrzeug.Kategorie.LEITERWAGEN, 5);

        // Feuerwehrmänner
        String fahrerTyp = "Pkw";
        for (int i = 0; i < 80; i++) {
            team[i] = new Feuerwehrmann(i, true, fahrerTyp);
            if (i >= 69) fahrerTyp = "Lkw";
        }
        // Fahrzeuge
        int start = 0;
        for (Fahrzeug.Kategorie key: Fahrzeug.anzahlVerfuegbar.keySet()) {
            int amount = Fahrzeug.anzahlVerfuegbar.get(key);
            for (int i = start; i < amount; i++) {
                garage[i] = new Fahrzeug(i, key, true);
            }
            start += amount;
        }
    }
    /**
     *
     * @param team Array von Feuerwehrmänner dessen Verfügbarkeit geändert wird
     * @see Feuerwehrmann
     *
     */

    /**
     *
     * @param einsatzart
     * @return
     */
    public static boolean checkResources(int einsatzart) {
        // TODO: check if current resources are enough to satisfy needed resources
        return true;
    };
    /**
     *
     * @param einsatzart aus 4 möglichen Einsatzarten
     */
    public static void createEinsatz(int einsatzart) {
//        checkResources(einsatzart);
//        Einsatz einsatz = new Einsatz(3, "Wohnungsbrand", new HashMap<>());
//        aktiveEinsaetze.put(einsatz.id, einsatz);
    };

    /**
     *
     * @param id zugewiesene Einsatz-id die bei Einsatz-Erstellung generiert wurde
     */
    public static void removeEinsatz(int id) {
        // Einsatz aus aktiveEinsaetze löschen
        // Ressourcen wieder freigeben
    }
}
