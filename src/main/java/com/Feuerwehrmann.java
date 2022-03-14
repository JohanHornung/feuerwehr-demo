package com;

public class Feuerwehrmann {
    int id;
    boolean verfuegbar;
    String status;
    String fahrerTyp;

    /**
     *
     * @param id macht Feuerwehrmann Objekt eindeutig identifizierbar
     * @param status kann vom Nutzer manuell geändert werden (z.B. "In Wartung").
     *               Mögliche Werte: "frei", "im Urlaub", "krank", "im Einsatz"
     * @param verfuegbar gibt an ob ein Feuerwehrmann eingesetzt werden kann
     * @param fahrerTyp manche Fahrzeug-Kategorien können nur von gewissen
     *                  Fahrer Typen gefahren werden
     * @see Fahrzeug
     */
    public Feuerwehrmann(int id, String status, boolean verfuegbar, String fahrerTyp) {
        this.id = id;
        this.status = status;
        this.verfuegbar = verfuegbar;
        this.fahrerTyp = fahrerTyp;
    }
}
