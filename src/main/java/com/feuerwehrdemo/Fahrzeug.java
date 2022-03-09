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
    public static HashMap<Kategorie, Integer> anzahlVerfuegbar = new HashMap<>();

    public int id;
    public Kategorie kategorie;
    public boolean verfuegbar;
    public String klasse;
    public int anzahlFeuerwehrleute;

    // constructor
    public Fahrzeug(int id, Kategorie kategorie, boolean verfuegbar) {
        this.id = id;
        this.kategorie = kategorie;
        this.verfuegbar = verfuegbar;

        klasse = switch (kategorie) {
            case EINSATZLEITFAHRZEUG -> "Pkw";
            default -> "Lkw";
        };
        anzahlFeuerwehrleute = switch (kategorie) {
            case MANNSCHAFTSTRANSPORTER -> 14;
            case TANKLOESCHFAHRZEUG -> 4;
            default -> 2;
        };
    }
    /**
     *
     * @param garage Array von Fahrzeugen dessen Verfügbarkeit geändert wird
     * @see Fahrzeug
     */
    public static void changeStatusGarage(Fahrzeug[] garage) {
        // Ändern der Verfügbarkeit
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        String status = sc.nextLine().trim();
        sc.close();
        // Kategorie von ausgewähltem Fahrzeug
        Fahrzeug.Kategorie key = garage[id].kategorie;
        if (status.equals("Wartung")) {
            // Fahrzeug wird schon gewartet
            if (!garage[id].verfuegbar) {
                System.out.println("Fahrzeug wird schon gewartet");
            }
            else {
                garage[id].verfuegbar = false;
                anzahlVerfuegbar.replace(key, anzahlVerfuegbar.get(key) - 1);
            }
            // Fahrzeug ist schon verfügbar
        } else if (garage[id].verfuegbar) {
            System.out.println("Fahrzeug schon verfügbar");
        } else {
            garage[id].verfuegbar = true;
            anzahlVerfuegbar.replace(key, anzahlVerfuegbar.get(key) + 1);
        }
//        System.out.println(key);
//        System.out.println(anzahlVerfuegbar.get(key));
    }
}
