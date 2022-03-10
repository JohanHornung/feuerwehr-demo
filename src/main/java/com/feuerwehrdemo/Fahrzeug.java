package com.feuerwehrdemo;

import java.util.HashMap;
import java.util.Scanner;

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
    // Array von Fahrzeugkategorien als Strings
    public static String[] fahrzeugKategorien = {
            "Einsatz-Leitfahrzeug",
            "Tank-Löschfahrzeug",
            "Mannschaftstransporter",
            "Leiterwagen"
    };
    // Reihenfolge der Kategorie Enumeration respektiv
    public static int[] fahrzeugAnzahl = {4, 5, 4, 5};
    // constructor
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
    /**
     *
     * @param garage Array von Fahrzeugen dessen Verfügbarkeit geändert wird
     * @see Fahrzeug
     */
//    public static void changeStatusGarage(Fahrzeug[] garage) {
//        // Ändern der Verfügbarkeit
//        Scanner sc = new Scanner(System.in);
//        int id = sc.nextInt();
//        String status = sc.nextLine().trim();
//        sc.close();
//        // Kategorie von ausgewähltem Fahrzeug
//        Fahrzeug.Kategorie key = garage[id].kategorie;
//        if (status.equals("Wartung")) {
//            // Fahrzeug wird schon gewartet
//            if (!garage[id].verfuegbar) {
//                System.out.println("Fahrzeug wird schon gewartet");
//            }
//            else {
//                garage[id].verfuegbar = false;
//                anzahlVerfuegbar.replace(key, anzahlVerfuegbar.get(key) - 1);
//            }
//            // Fahrzeug ist schon verfügbar
//        } else if (garage[id].verfuegbar) {
//            System.out.println("Fahrzeug schon verfügbar");
//        } else {
//            garage[id].verfuegbar = true;
//            anzahlVerfuegbar.replace(key, anzahlVerfuegbar.get(key) + 1);
//        }
//        System.out.println(key);
//        System.out.println(anzahlVerfuegbar.get(key));
//    }
}
