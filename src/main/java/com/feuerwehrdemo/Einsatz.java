package com.feuerwehrdemo;

import com.feuerwehrdemo.Fahrzeug;

import java.util.HashMap;
import java.util.Scanner;

public class Einsatz {
    public int id;
    public String einsatzart;
    public HashMap<String, Integer> parameter;

    public static final String[] einsatzarten = {
            "Wohnungsbrand",
            "Verkehrsunfall",
            "Naturkatastrophe",
            "Industrieunfall"
    };
    public static final int[][] minParameter = {
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

    /**
     *
     * @param einsatzart
     * @return
     */

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
