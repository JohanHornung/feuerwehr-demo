package com;

import java.util.HashMap;

public class Einsatz {
    public int id;
    public String einsatzart;
    public HashMap<Integer, Feuerwehrmann> fmParameter;
    public HashMap<Integer, Fahrzeug> fzParameter;
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


    public Einsatz(
            int id,
            String einsatzart,
            HashMap<Integer, Feuerwehrmann> fmParameter,
            HashMap<Integer, Fahrzeug> fzParameter
    ) {
        this.id = id;
        this.einsatzart = einsatzart;
        this.fmParameter = fmParameter;
        this.fzParameter = fzParameter;
    }
}