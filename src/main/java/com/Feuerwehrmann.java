package com;

public class Feuerwehrmann {
    int id;
    boolean verfuegbar; 
    String fahrerTyp;

    /**
     *
     * @param id macht Feuerwehrmann Objekt eindeutig identifizierbar
     * @param verfuegbar gibt an ob ein Feuerwehrmann eingesetzt werden kann
     * @param fahrerTyp manche Fahrzeuge können nur von gewissen Fahrer Typen
     *                  gefahren werden
     * @see Fahrzeug
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
