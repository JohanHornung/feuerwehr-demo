package com;

public class Fahrzeug {
    public enum Kategorie {
        EINSATZLEITFAHRZEUG,
        TANKLOESCHFAHRZEUG,
        MANNSCHAFTSTRANSPORTER,
        LEITERWAGEN
    };

    public int id;
    public String kategorie;
    public boolean verfuegbar;
    public String klasse;
    public int anzahlFeuerwehrleute;
    public int einsatzId;
    // Array von Fahrzeugkategorien als Strings
    public static String[] fahrzeugKategorien = {
            "Einsatz-Leitfahrzeug",
            "Tank-Löschfahrzeug",
            "Mannschaftstransporter",
            "Leiterwagen"
    };
    // In respektiver Reihenfolge der Kategorien
    public static int[] fahrzeugAnzahl = {4, 5, 4, 5};

    /**
     *
     * @param id
     * @param kategorie
     * @param verfuegbar
     */
    public Fahrzeug(int id, String kategorie, boolean verfuegbar) {
        this.id = id;
        this.kategorie = kategorie;
        this.verfuegbar = verfuegbar;

        this.klasse = switch (kategorie) {
            case "Einsatz-Leitfahrzeug" -> "Pkw";
            default -> "Lkw";
        };
        this.anzahlFeuerwehrleute = switch (kategorie) {
            case "Mannschaftstransporter" -> 14;
            case "Tank-Löschfahrzeug" -> 4;
            default -> 2;
        };

    }
    @Override
    public String toString() {
        // TODO: 10.03.22 Mögliche Ausgabe Text für Fahrzeuge
        return null;
    }

}
