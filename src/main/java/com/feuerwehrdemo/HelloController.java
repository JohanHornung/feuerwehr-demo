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
    // Array aus Textfelder für Einsatzparameter
    private TextField[] einsatzTextfelder = {
            anzahlFLTextField,
            anzahlELFTextField,
            anzahlTLTextField,
            anzahlMTTextField,
            anzahlLWTextField,
    };
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
        Feuerwehrmann[] team = new Feuerwehrmann[80];
        Fahrzeug[] garage = new Fahrzeug[18];
        // Feuerwehrleute & Fahrzeuge werden initialisiert
        Einsatz.fillResources(team, garage);

        // Durchzählen von verfügbaren Feuerwehrleuten (pro Fahrer Typ)
        HashMap firefightersCount = countFirefighters(team);
        // Text Felder ausfüllen
        String anzahlLkwFahrer = "Lkw-Fahrer: " + firefightersCount.get("Lkw-Fahrer");
        String anzahlPkwFahrer = "Pkw-Fahrer: " + firefightersCount.get("Pkw-Fahrer");
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
        String[] fahrzeugKategorien = {
                "Einsatz-Leitfahrzeuge",
                "Tank-Löschfahrzeuge",
                "Mannschaftstransporter",
                "Leiterwagen"
        };
//        HashMap<Fahrzeug.Kategorie, Integer> vehicleCount = countVehicles(garage, fahrzeugKategorien);
//        Fahrzeug.Kategorie[] vehicleCategories = Fahrzeug.Kategorie.values();
//        assert vehicleCount.values().size() != fahrzeugKategorien.length : "Anzahl der Fahrzeugkategorien fehlerhaft";
//
//        for (int i = 0; i < fahrzeugKategorien.length; i++) {
//            // Output Text ("Anzahl von <Fahrzeugkategorie>: <Wert>")
//            String text = fahrzeugKategorien[i] + ": " + vehicleCount.get(vehicleCategories[i]);
//            // Label wird beschrieben
//            verfuegbareFahrzeuge[i].setText(text);
//        }
    }

    /**
     *
     * @param garage Array von Fahrzeugen (Fahrzeug Objekt)
     * @see Fahrzeug
     * @return Hashmap mit Anzahl der verfügbaren Fahrzeugkategorien
     */
    // Durchzählen von verfügbaren Fahrzeugen (pro Kategorie)
    public static HashMap<Fahrzeug.Kategorie, Integer> countVehicles(Fahrzeug[] garage, String[] fahrzeugKategorien) {
        HashMap<Fahrzeug.Kategorie, Integer> vehicleCount = new HashMap<>();
        // TODO: 09.03.22 Bug bei fillResources() für Fahrzeuge finden
        // initialisierung
        Fahrzeug.Kategorie[] vehicleCategories = Fahrzeug.Kategorie.values();
        for (int i = 0; i < vehicleCategories.length; i++) {
            vehicleCount.put(vehicleCategories[i], 0);
        }
        // Iteration durch jedes Fahrzeug in der Garage
        for (Fahrzeug fahrzeug: garage) {
            // Wenn das Fahrzeug nicht verfügbar ist wird es ignoriert
            if (fahrzeug.verfuegbar) {
                // Die jeweilige Anzahl Fahrzeugkategorie wird erhöht
                vehicleCount.replace(fahrzeug.kategorie, vehicleCount.get(fahrzeug.kategorie) + 1);
            }
        }
        return vehicleCount;
    }
    /**
     *
     * @param team Array von Feuerwehrleuten (Feuerwehrmann Objekten)
     * @see Feuerwehrmann
     * @return Hashmap mit Anzahl der verfügbaren FahrerTypen
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
         * @author Johan Hornung
         * @param einsatzart String der gewählten Einsatzart
         * @param felder Array von zu ausfüllenden Textfeldern
         * @see Einsatz
         */
    void fillTextFeldEinsatzParameter(String einsatzart, TextField[] felder) {
        final String[] einsatzarten = {
                "Wohnungsbrand",
                "Verkehrsunfall",
                "Naturkatastrophe",
                "Industrieunfall"
        };
        final int[][] minParameter = {
                {22, 1, 2, 1, 1},
                {16, 1, 1, 1, 0},
                {55, 3, 3, 3, 2},
                {40, 3, 2, 2, 2}
        };
        final HashMap<String, int[]> parameterMap = new HashMap<>();
        for (int i = 0; i < einsatzarten.length; i++) {
            parameterMap.put(einsatzarten[i], minParameter[i]);
        }

        int[] einsatzParameter = parameterMap.get(einsatzart);
        assert felder.length != einsatzParameter.length: "Anzahl der Einsatz Parameter und Textfelder fehlerhaft";
        // Iteration durch das Array von TextFeldern
        Iterator it = Arrays.stream(felder).iterator();
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
        fillTextFeldEinsatzParameter(einsatzart, einsatzTextfelder);
    }
    @FXML
    void fillNaturkatastropheParameter(ActionEvent event) {
        // gewählte Einsatzart abspeichern
        String einsatzart = naturkatastropheButton.getText();
        // ausgewählte Einsatzart im Menu button anzeigen
        einsatzartMenuButton.setText(einsatzart);
        // Parameter in Textfelder einsetzen
        fillTextFeldEinsatzParameter(einsatzart, einsatzTextfelder);

    }

    @FXML
    void fillVerkehrsunfallParameter(ActionEvent event) {
        // gewählte Einsatzart abspeichern
        String einsatzart = verkehrsunfallButton.getText();
        // ausgewählte Einsatzart im Menu button anzeigen
        einsatzartMenuButton.setText(einsatzart);
        // Parameter in Textfelder einsetzen
        fillTextFeldEinsatzParameter(einsatzart, einsatzTextfelder);

    }

    @FXML
    void fillWohnungsbrandParameter(ActionEvent event) {
        // gewählte Einsatzart abspeichern
        String einsatzart = wohnungsbrandButton.getText();
        // ausgewählte Einsatzart im Menu button anzeigen
        einsatzartMenuButton.setText(einsatzart);
        // Parameter in Textfelder einsetzen
        fillTextFeldEinsatzParameter(einsatzart, einsatzTextfelder);
    }

    @FXML
    void onCreateOperationClick(ActionEvent event) {

    }

    @FXML
    void onDeleteOperationClick(ActionEvent event) {

    }

}
