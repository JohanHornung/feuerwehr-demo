package com.feuerwehrdemo;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class HelloController {
    // Feste Anzahl von Ressourcen
    public final int FIREFIGHTER_CAP = 80;
    public final int VERHICLES_CAP = 18;

    @FXML
    private MenuItem industrieunfallButton;

    @FXML
    private MenuItem naturkatastropheButton;

    @FXML
    private TableView<?> aktiveEinsatzTabelle;

    @FXML
    private TableColumn<?, ?> aktiveEinsatzId;

    @FXML
    private TableColumn<?, ?> aktiveEinsatzart;

    @FXML
    private TableColumn<?, ?> aktiveFahrzeuge;

    @FXML
    private TableColumn<?, ?> aktiveFeuerwehrleute;

    @FXML
    private TableColumn<?, ?> aktiveSonderattribute;

    @FXML
    private TextField anzahlELFTextField;

    @FXML
    private TextField anzahlFLTextField;

    @FXML
    private Label anzahlFahrzeuge;

    @FXML
    private Label anzahlFeuerwehrleute;

    @FXML
    private TextField anzahlLWTextField;

    @FXML
    private TextField anzahlMTTextField;

    @FXML
    private TextField anzahlTLTextField;

    @FXML
    private Button einsatzBeendenSubmit;

    @FXML
    private Button einsatzErstellenSubmit;

    @FXML
    private TextField einsatzIdBeenden;

    @FXML
    private MenuButton einsatzartMenuButton;

    @FXML
    private ImageView logo;

    @FXML
    private Label verfuegbareLkwFahrer;

    @FXML
    private Label verfuegbarePkwFahrer;

    @FXML
    private Label verfuegbareEinsatzLeitfahrzeuge;

    @FXML
    private Label verfuegbareLeiterwagen;


    @FXML
    private Label verfuegbareManschaftstransporter;

    @FXML
    private Label verfuegbareTLFahrzeuge;

    @FXML
    private MenuItem verkehrsunfallButton;

    @FXML
    private MenuItem wohnungsbrandButton;

    @FXML
    public void initialize() {
        Feuerwehrmann[] team = new Feuerwehrmann[FIREFIGHTER_CAP];
        Fahrzeug[] garage = new Fahrzeug[VERHICLES_CAP];
        // Feuerwehrleute & Fahrzeuge werden initialisiert
        fillResources(team, garage);

        // Durchzählen von verfügbaren Feuerwehrleuten (pro Fahrer Typ)
        HashMap firefightersCount = countFirefighters(team);
        // Text Felder ausfüllen
        String anzahlLkwFahrer = firefightersCount.get("Lkw-Fahrer").toString();
        String anzahlPkwFahrer = firefightersCount.get("Pkw-Fahrer").toString();
        verfuegbareLkwFahrer.setText(anzahlLkwFahrer);
        verfuegbarePkwFahrer.setText(anzahlPkwFahrer);



        // Array aus Labels für Fahrzeugkategorien
        Label[] verfuegbareFahrzeuge = {
                verfuegbareEinsatzLeitfahrzeuge,
                verfuegbareTLFahrzeuge,
                verfuegbareManschaftstransporter,
                verfuegbareLeiterwagen,
        };
        // Array von Fahrzeugkategorien als Strings

        HashMap<String, Integer> vehicleCount = countVehicles(garage);

        assert vehicleCount.values().size() != Fahrzeug.fahrzeugKategorien.length : "Anzahl der Fahrzeugkategorien fehlerhaft";

        for (int i = 0; i < Fahrzeug.fahrzeugKategorien.length; i++) {
            // Output Text ("Anzahl von <Fahrzeugkategorie>: <Wert>")
            String text = vehicleCount.get(Fahrzeug.fahrzeugKategorien[i]).toString();
            // Label wird beschrieben
            verfuegbareFahrzeuge[i].setText(text);
        }
    }

    /**
     * Methode welche die initialen Einsatzressourcen (Fahrzeuge + Feuerwehrleute) konfiguriert
     * @param team Array von Feuerwehrleuten (Feuerwehrmann Objekt) wird befüllt
     * @see Feuerwehrmann
     * @param garage Array von Fahrzeugen (Fahrzeug Objekt) wird befüllt
     * @see Fahrzeug
     */
    public void fillResources(Feuerwehrmann[] team, Fahrzeug[] garage) {
        HashMap<String, Integer> anzahlVerfuegbar = new HashMap<>();
        for (int i = 0; i < Fahrzeug.fahrzeugKategorien.length; i++) {
            anzahlVerfuegbar.put(Fahrzeug.fahrzeugKategorien[i], Fahrzeug.fahrzeugAnzahl[i]);
        }

        // Feuerwehrleute
        String fahrerTyp = "Pkw";
        // 70 Pkw-Fahrer
        for (int i = 0; i < FIREFIGHTER_CAP; i++) {
            team[i] = new Feuerwehrmann(i, true, fahrerTyp);
            // 10 Lkw-Fahrer
            if (i >= (FIREFIGHTER_CAP - 1) - 10) fahrerTyp = "Lkw";
        }
        // Fahrzeuge
        int start = 0;
        int amount = 0;

        for (String category: Fahrzeug.fahrzeugKategorien) {
            amount += anzahlVerfuegbar.get(category);

            for (int i = start; i < amount; i++) {
                garage[i] = new Fahrzeug(i, category, true);
            }
            start += (amount - start);
        }
    }
    /**
     * Methode zählt verfügbare Fahrzeuge pro Kategorie
     *
     * @author Luca Langer
     * @param garage Array von Fahrzeugen (Fahrzeug Objekt)
     * @see Fahrzeug
     * @return Hashmap mit Anzahl der verfügbaren Fahrzeuge (value) pro Kategorie (key)
     */
    // Durchzählen von verfügbaren Fahrzeugen (pro Kategorie)
    public static HashMap<String, Integer> countVehicles(Fahrzeug[] garage) {
        HashMap<String, Integer> vehicleCount = new HashMap<>();
        // initialisierung
        for (int i = 0; i < Fahrzeug.fahrzeugKategorien.length; i++) {
            vehicleCount.put(Fahrzeug.fahrzeugKategorien[i], 0);
        }
        // Iteration durch jedes Fahrzeug in der Garage
        for (Fahrzeug fahrzeug: garage) {
            // Wenn das Fahrzeug nicht verfügbar ist wird es ignoriert
            if (fahrzeug.verfuegbar) {
                // Die jeweilige Anzahl Fahrzeugkategorie wird um 1 erhöht
                vehicleCount.replace(fahrzeug.kategorie, vehicleCount.get(fahrzeug.kategorie) + 1);
            }
        }
        return vehicleCount;
    }
    /**
     * Methode zählt verfügbare Feuerwehrmänner pro fahrer Typ
     *
     * @author Luca Langer
     * @param team Array von Feuerwehrleuten (Feuerwehrmann Objekten)
     * @see Feuerwehrmann
     * @return Hashmap mit Anzahl der verfügbaren Feuerwehrmänner (value) pro fahrer Typ (key)
     */
    public static HashMap<String, Integer> countFirefighters(Feuerwehrmann[] team) {
        HashMap<String, Integer> firefighters = new HashMap<>();
        int LkwCount = 0;
        int PkwCount = 0;

        for (int i = 0; i < team.length; i++) {
            if (team[i].verfuegbar) {
                if (team[i].fahrerTyp == "Lkw") {
                    LkwCount += 1;
                } else {
                    PkwCount += 1;
                }
            }
        }
        firefighters.put("Lkw-Fahrer", LkwCount);
        firefighters.put("Pkw-Fahrer", PkwCount);

        return firefighters;
    };
        /**
         * Methode zum automatischen Ausfüllen der mininmalen Einsatzparameter in die
         * jeweiligen Textfelder
         *
         * @author Johan Hornung
         * @param einsatzart String gewählte Einsatzart
         * @see Einsatz
         */
    void fillEinsatzParameter(String einsatzart) {
        // Array aus Textfelder für Einsatzparameter
        TextField[] einsatzTextfelder = {
                anzahlFLTextField,
                anzahlELFTextField,
                anzahlTLTextField,
                anzahlMTTextField,
                anzahlLWTextField,
        };
        // Passende EinsatzParameter
        int[] einsatzParameter = new int[4];
        // Falls keine passenden Einsatzparameter gefunden wurden
        boolean found = false;
        // Suche nach richtigen Einsatzparameter für jeweilige Einsatzart
        for (int i = 0; i < Einsatz.einsatzarten.length; i++) {
            if (einsatzart.equals(Einsatz.einsatzarten[i])) {
                found = true;
                // Respektive Reihenfolge in String[] einsatzarten und in int[][] minParameter
                einsatzParameter = Einsatz.minParameter[i];
            }
        }
        // Beendung des Programms
        if (!found) throw new AssertionError(
                "Keine passenden Einsazparameter gefunden, " +
                "Einsatzart" + einsatzart + " Unbekannt");

        // Iteration durch das Array von TextFeldern
        Iterator it = Arrays.stream(einsatzTextfelder).iterator();
        int i = 0;
        while (it.hasNext()) {
            TextField textField = (TextField) it.next();
            textField.setText(String.valueOf(einsatzParameter[i]));
            i++;
        }
    }

    @FXML
    void fillIndustrieunfallParameter(ActionEvent event) {
        // gewählte Einsatzart abspeichern
        String einsatzart = industrieunfallButton.getText();
        // ausgewählte Einsatzart im Menu button anzeigen
        einsatzartMenuButton.setText(einsatzart);
        // Parameter in Textfelder einsetzen
        fillEinsatzParameter(einsatzart);
    }
    @FXML
    void fillNaturkatastropheParameter(ActionEvent event) {
        // gewählte Einsatzart abspeichern
        String einsatzart = naturkatastropheButton.getText();
        // ausgewählte Einsatzart im Menu button anzeigen
        einsatzartMenuButton.setText(einsatzart);
        // Parameter in Textfelder einsetzen
        fillEinsatzParameter(einsatzart);

    }

    @FXML
    void fillVerkehrsunfallParameter(ActionEvent event) {
        // gewählte Einsatzart abspeichern
        String einsatzart = verkehrsunfallButton.getText();
        // ausgewählte Einsatzart im Menu button anzeigen
        einsatzartMenuButton.setText(einsatzart);
        // Parameter in Textfelder einsetzen
        fillEinsatzParameter(einsatzart);

    }

    @FXML
    void fillWohnungsbrandParameter(ActionEvent event) {
        // gewählte Einsatzart abspeichern
        String einsatzart = wohnungsbrandButton.getText();
        // ausgewählte Einsatzart im Menu button anzeigen
        einsatzartMenuButton.setText(einsatzart);
        // Parameter in Textfelder einsetzen
        fillEinsatzParameter(einsatzart);
    }

    @FXML
    void onCreateOperationClick(ActionEvent event) {

    }

    @FXML
    void onDeleteOperationClick(ActionEvent event) {

    }

}
