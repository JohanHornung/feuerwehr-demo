package com;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Beschäftigt sich mit allen relevanten Details zu den Fahrzeugen
 */
public class Vehicle {
    /**
     * Feste Anzahl von generierten Fahrzeugen
     */
    private static final int VEHICLE_CAP = 18;

    private static final int VEHICLE_CATEGORIES_CAP = 4;
    /**
     * Array von 18 Fahrzeugen
     */
    static final Vehicle[] garage = new Vehicle[VEHICLE_CAP];

    private int id;
    private int firefigherCapacity;
    private int operationId;
    private String category;
    private String claass;
    private String status;
    private boolean availability;

    // Hashmap mit Anzahl der verfügbaren Fahrzeuge (value) pro Kategorie (key)
    private static LinkedHashMap<String, Integer> vehicleAmount = new LinkedHashMap<>();

    private static final String[] vehicleCategories = {
            "Einsatz-Leitfahrzeug",
            "Tank-Löschfahrzeug",
            "Mannschaftstransporter",
            "Leiterwagen"
    };
    /**
     *
     * @param id macht Vehicle Objekt eindeutig identifizierbar
     * @param category hat Einfluss auf die Fahrer-Klasse des Fahrzeugs
     * @param status kann vom Nutzer manuell geändert werden.
     *               Mögliche Werte: "frei", "in Wartung", "im Einsatz"
     * @param availability gibt an ob das Vehicle eingesetzt werden kann
     */
    public Vehicle(int id, String category, String status, boolean availability) {
        this.id = id;
        this.category = category;
        this.status = status;
        this.availability = availability;
        /**
         * Die Fahrzeugklasse gibt an ob ein bestimmter Fahrer Typ eine bestimmte
         * Fahrzeugkategorie fahren kann
         * @see Firefighter
         */
        this.claass = (this.category.equals("Einsatz-Leitfahrzeug")) ? "Pkw": "Lkw";
        /**
         * Die Anzahl der Feuerwehrmänner Kapazität
         */
        this.firefigherCapacity = switch (this.category) {
            case "Mannschaftstransporter" -> 14;
            case "Tank-Löschfahrzeug" -> 4;
            default -> 2;
        };
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFirefigherCapacity() {
        return firefigherCapacity;
    }

    public void setFirefigherCapacity(int firefigherCapacity) {
        this.firefigherCapacity = firefigherCapacity;
    }

    public int getOperationId() {
        return operationId;
    }

    public void setOperationId(int operationId) {
        this.operationId = operationId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getClaass() {
        return claass;
    }

    public void setClaass(String claass) {
        this.claass = claass;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public static LinkedHashMap<String, Integer> getVehicleAmount() {
        return Vehicle.vehicleAmount;
    }

    public static int getVEHICLE_CAP() {
        return VEHICLE_CAP;
    }

    public static int getVEHICLE_CATEGORIES_CAP() {
        return VEHICLE_CATEGORIES_CAP;
    }

    public static String[] getVehicleCategories() {
        return vehicleCategories;
    }

    // Generierung der Vehicle-Objekte
    public static void initGarage() {
        Vehicle.vehicleAmount.put("Mannschaftstransporter", 4);
        Vehicle.vehicleAmount.put("Einsatz-Leitfahrzeug", 4);
        Vehicle.vehicleAmount.put("Leiterwagen", 5);
        Vehicle.vehicleAmount.put("Tank-Löschfahrzeug", 5);

        int start = 0;
        int currAmount = 0;
        // Für jede Kategorie wird eine bestimmte Anzahl an Fahrzeugen generiert
        for (String category: Vehicle.getVehicleAmount().keySet()) {
            // Temporäre Anzahl der zu erstellenden Fahrzeuge (pro Kategorie)
            currAmount += Vehicle.getVehicleAmount().get(category);
            // Gewünschte Anzahl an Fahrzeuge wird pro Kategorie erstellt
            for (int i = start; i < currAmount; i++) {
                // Vehicle ID ist der index im garagen Array
                garage[i] = new Vehicle(i, category, "frei", true);
            }
            // Neuer "Index-Einstiegspunkt" in der garage[] für nächste Kategorie
            start += (currAmount - start);
        }
    }
    /**
     * Methode zählt verfügbare Fahrzeuge pro Kategorie durch
     *
     * @return Hashmap mit Anzahl der verfügbaren Fahrzeuge (value) pro Kategorie (key)
     */
    // Durchzählen von verfügbaren Fahrzeugen (pro Kategorie)
    public static LinkedHashMap<String, Integer> countVehicles() {
        // Hashmap mit Anzahl der verfügbaren Fahrzeuge (value) pro Kategorie (key)
        LinkedHashMap<String, Integer> vehicleAmount = new LinkedHashMap<>();
        // Start bei 0
        for (int i = 0; i < Vehicle.vehicleCategories.length; i++) {
            vehicleAmount.put(Vehicle.vehicleCategories[i], 0);
        }
        // Die Verfügbarkeit wird bei jedem Vehicle überprüft
        for (Vehicle vehicle : garage) {
            // Wenn das Vehicle nicht verfügbar ist wird es nicht dazugezählt
            if (vehicle.getAvailability()) {
                // Anzahl der Fahrzeugkategorie wird um 1 erhöht
                int prevValue = vehicleAmount.get(vehicle.getCategory());
                vehicleAmount.replace(vehicle.getCategory(),  prevValue + 1);
            }
        }
        return vehicleAmount;
    }

}
