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
    public String klasse;
    public String status;
    public boolean verfuegbar;
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
     * @param id macht Fahrzeug Objekt eindeutig identifizierbar
     * @param kategorie hat Einfluss auf die Fahrer-Klasse des Fahrzeugs
     * @param status kann vom Nutzer manuell geändert werden (z.B. "In Wartung").
     *               Mögliche Werte: "frei", "in Wartung", "im Einsatz"
     * @param verfuegbar gibt an ob das Fahrzeug eingesetzt werden kann
     */
    public Fahrzeug(int id, String kategorie, String status, boolean verfuegbar) {
        this.id = id;
        this.kategorie = kategorie;
        this.status = status;
        this.verfuegbar = verfuegbar;
        /**
         * Die Fahrzeugklasse gibt an ob ein bestimmter Fahrer Typ eine bestimmte Fahrzeugkategorie fahren kann
         * @see Feuerwehrmann
         */
        this.klasse = switch (kategorie) {
            case "Einsatz-Leitfahrzeug" -> "Pkw";
            default -> "Lkw";
        };
        /**
         * Die Anzahl der Feuerwehrmänner Kapazität
         */
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
