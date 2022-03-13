package com;

import java.util.Scanner;

public class Feuerwehrmann {
    int id;
    boolean verfuegbar; 
    String fahrerTyp;

    /**
     *
     * @param id
     * @param verfuegbar
     * @param fahrerTyp
     */
    public Feuerwehrmann(int id, boolean verfuegbar, String fahrerTyp) {
        this.id = id;
        this.verfuegbar = verfuegbar;
        this.fahrerTyp = fahrerTyp;
    }
    @Override
    public String toString() {
        // TODO: 10.03.22 Mögliche Ausgabe Text für Feuerwehrmänner
        return null;
    }
}
