package com;

import java.util.HashMap;


public class Einsatz {
    public int id;
    public String einsatzart;
    public HashMap<Integer, Feuerwehrmann> fmTeam;
    public HashMap<Integer, Fahrzeug> fzTeam;
    public int anzahlFeuerwehrleute;
    public int anzahlFahrzeuge;

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
    /**
     *
     * @param id macht Einsatz Objekt eindeutig identifizierbar
     * @param einsatzart gibt die nötigen minimalen Einsatzparameter an
     * @param fmTeam Hashmap aus Feuerwehrmännern welche dem Einsatz zugeordnet wurden
     * @see Feuerwehrmann
     * @param fzTeam Hashmap aus Fahrzeugen welche dem Einsatz zugeordnet wurden
     * @see Fahrzeug
     */
    public Einsatz(
            int id,
            String einsatzart,
            HashMap<Integer, Feuerwehrmann> fmTeam,
            HashMap<Integer, Fahrzeug> fzTeam
    ) {
        this.id = id;
        this.einsatzart = einsatzart;
        this.fmTeam = fmTeam;
        this.fzTeam = fzTeam;
        this.anzahlFeuerwehrleute = fmTeam.size();
        this.anzahlFahrzeuge = fzTeam.size();
    }
}