package com;

import java.util.LinkedHashMap;

/**
 * Beschäftigt sich mit allen relevanten Details zu den Feuerwehrleuten
 */
public class Firefighter {
    private int id;
    private boolean availability;
    private String status;
    private String driverType;

    /**
     * Feste Anzahl von generierten Feuerwehrleuten
     */
    static final int FIREFIGHTER_CAP = 80;
    /**
     * Array von 80 Feuerwehrleuten
     * @see Firefighter
     */
    static final Firefighter[] team = new Firefighter[FIREFIGHTER_CAP];

    /**
     *
     * @param id macht Firefighter Objekt eindeutig identifizierbar
     * @param status kann vom Nutzer manuell geändert werden (z.B. "In Wartung").
     *               Mögliche Werte: "frei", "im Urlaub", "krank", "im Einsatz"
     * @param availability gibt an ob ein Firefighter eingesetzt werden kann
     * @param driverType manche Vehicle-Kategorien können nur von gewissen
     *                  Fahrer Typen gefahren werden
     * @see Vehicle
     */
    public Firefighter(int id, String status, boolean availability, String driverType) {
        this.id = id;
        this.status = status;
        this.availability = availability;
        this.driverType = driverType;
    }
    // SETTER AND GETTER METHODS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDriverType() {
        return driverType;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType;
    }

    // Erstellung der Firefighter-Objekte
    public static void initTeam() {
        // 70 Pkw-Fahrer
        String driverType = "Pkw";
        for (int i = 0; i < FIREFIGHTER_CAP; i++) {
            // Die ID vom Firefighter ist der index im team Array
            team[i] = new Firefighter(i, "frei", true, driverType);
            // i = (FIREFIGHTER_CAP - 11) = 69 bei i = 0 -> 70 Pkw-Fahrer
            if (i >= FIREFIGHTER_CAP - 11) {
                // 10 Lkw-Fahrer
                driverType = "Lkw";
            }
        }
    }
    /**
     * Methode zählt verfügbare Feuerwehrmänner pro fahrer Typ
     *
     * @return Hashmap mit Anzahl der verfügbaren Feuerwehrmänner (value) pro fahrer Typ (key)
     */
    public static LinkedHashMap<String, Integer> countFirefighters() {
        // Hashmap mit Anzahl der verfügbaren Feuerwehrleute (value) pro Fahrer-Typ (key)
        LinkedHashMap<String, Integer> count = new LinkedHashMap<>();
        // Start bei 0
        count.put("Lkw", 0);
        count.put("Pkw", 0);
        // Wenn Feuerwehrmann verfügbar ist wird er mitgezählt
        for (Firefighter fm: team) {
            boolean isAvailable = fm.getAvailability();
            if (isAvailable) {
                // Erhöhung der Anzahl des jeweiligen Fahrer Typs um 1
                count.replace(fm.getDriverType(), count.get(fm.getDriverType()) + 1);
            }
        }
        return count;
    }
}
