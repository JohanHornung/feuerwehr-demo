package com;

/**
 * Beschäftigt sich mit allen relevanten Details zu den Feuerwehrleuten
 */
public class Firefighter {
    private int id;
    private boolean availability;
    private String status;
    private String driverType;
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
}
