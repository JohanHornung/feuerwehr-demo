package com.feuerwehrdemo;

import java.util.Scanner;

public class Feuerwehrmann {
    int id;
    boolean verfuegbar; 
    String fahrerTyp;

    public Feuerwehrmann(int id, boolean verfuegbar, String fahrerTyp) {
        this.id = id;
        this.verfuegbar = verfuegbar;
        this.fahrerTyp = fahrerTyp;
    }
    public static void changeStatusTeam(Feuerwehrmann[] team) {
        // Ändern der Verfügbarkeit
        Scanner sc = new Scanner(System.in);
        int status = sc.nextInt();
        // "krank" = 1;
        // "urlaub" = 2;
        // "verfügbar" = 3;
        int id = sc.nextInt();
        sc.close();

        // Feuerwehrmann nicht mehr verfügbar
        if (status < 3) team[id].verfuegbar = false;
            // Feuerwehrmann wieder verfügbar
        else team[id].verfuegbar = true;

        // Aktualisierung von Ressourcen
        int LkwSum = 0;
        int PkwSum = 0;
        for (int i = 0; i < team.length; i++) {
            if (team[i].verfuegbar) {
                if (team[i].fahrerTyp == "Lkw") {
                    LkwSum += 1;
                } else {
                    PkwSum += 1;
                }
            }
        }
//        System.out.println(LkwSum);
//        System.out.println(PkwSum);

    }
}
