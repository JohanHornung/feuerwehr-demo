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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class HelloController {
    // Feste Anzahl von Ressourcen
    public final int FIREFIGHTER_CAP = 80;
    public final int VEHICLES_CAP = 18;

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
    private Button einsatzParameterResetButton;

    @FXML
    private TextField einsatzIdBeenden;

    @FXML
    private Label einsatzErstellungMessage;

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

    public Feuerwehrmann[] team = new Feuerwehrmann[FIREFIGHTER_CAP];
    public Fahrzeug[] garage = new Fahrzeug[VEHICLES_CAP];

    @FXML
    /**
     * Methode wird bei der initialisierung der Anwendung (App.main()) ausgeführt und erledigt folgende Schritte:
     *      - Erstellt und befüllt Array von Feuerwehrmännern und Fahrzeugen
     *      - Zählt Ressourcen durch und schreibt jeweilige Werte in die grafischen Text Felder
     */
    public void initialize() {

        // Leere Hashmap der aktiven Einsätze
        HashMap<Integer, Einsatz> activeOperations = new HashMap<>();
        // Feuerwehrleute & Fahrzeuge werden initialisiert
        fillResources(team, garage);

        // Durchzählen von verfügbaren Feuerwehrleuten (pro Fahrer Typ)
        HashMap firefightersCount = countFirefighters(team);
        // Auslesen der Werte pro fahrer Typ
        String anzahlLkwFahrer = firefightersCount.get("Lkw-Fahrer").toString();
        String anzahlPkwFahrer = firefightersCount.get("Pkw-Fahrer").toString();
        // Text Felder ausfüllen
        verfuegbareLkwFahrer.setText(anzahlLkwFahrer);
        verfuegbarePkwFahrer.setText(anzahlPkwFahrer);

        // Array aus Labels für Fahrzeugkategorien
        Label[] verfuegbareFahrzeuge = {
                verfuegbareEinsatzLeitfahrzeuge,
                verfuegbareTLFahrzeuge,
                verfuegbareManschaftstransporter,
                verfuegbareLeiterwagen,
        };
        // Durchzählen von verfügbaren Fahrzeugen (pro Kategorie)
        HashMap<String, Integer> vehicleCount = countVehicles(garage);
        // An diesem Punkt muss das Programm beendet werden falls ein Logik-Fehler vorhanden ist
        assert vehicleCount.values().size() == Fahrzeug.fahrzeugKategorien.length : "Anzahl der Fahrzeugkategorien fehlerhaft";

        for (int i = 0; i < Fahrzeug.fahrzeugKategorien.length; i++) {
            // Output Text ("Anzahl von <Fahrzeugkategorie>: <Wert>")
            String text = vehicleCount.get(Fahrzeug.fahrzeugKategorien[i]).toString();
            // Label wird beschrieben
            verfuegbareFahrzeuge[i].setText(text);
        }
    }

    /**
     * Methode welche die initialen Einsatzressourcen (Fahrzeuge + Feuerwehrleute) konfiguriert
     *
     * @author Johan Hornung, Luca Langer
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
            // Feuerwehrmann ID ist der index im sich befindenden Array
            team[i] = new Feuerwehrmann(i, true, fahrerTyp);
            // 10 Lkw-Fahrer
            // i = (FIREFIGHTER_CAP - 11) = 69 bei 70. Feuerwehrmann
            if (i >= FIREFIGHTER_CAP - 11) fahrerTyp = "Lkw";
        }
        // Erstellung der Fahrzeuge in die Garage
        int start = 0;
        int currentAmount = 0;
        // Für jede Kategorie wird eine bestimmte Anzahl an Fahrzeugen generiert
        for (String category: Fahrzeug.fahrzeugKategorien) {
            // Temporäre Anzahl der zu erstellenden Fahrzeuge
            currentAmount += anzahlVerfuegbar.get(category);
            // Fahrzeug ID ist der index im sich befindenden Array
            for (int i = start; i < currentAmount; i++) {
                // Gewünschte Anzahl an Fahrzeuge wird pro Kategorie erstellt
                garage[i] = new Fahrzeug(i, category, true);
            }
            // Neuer "Einstiegspunkt" im Array garage für nächste Kategorie
            start += (currentAmount - start);
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

        // Werte werden in die Textfelder eingesetzt
        setTextFieldValues(einsatzTextfelder, einsatzParameter);
    }

    /**
     * @author Johan Hornung
     * @param textFelder Array von Text Feldern dessen Werte geändert werden
     * @param values werden eingesetzt
     */
    void setTextFieldValues(TextField[] textFelder, int[] values) {
        // An diesem Punkt muss das Programm beendet werden falls ein Logik-Fehler vorhanden ist
        assert textFelder.length == values.length : "Anzahl von Text Feldern und Werten stimmt nicht überein";
        // Iteration durch das Array von TextFeldern
        Iterator it = Arrays.stream(textFelder).iterator();
        // Für Array von Werten
        int i = 0;
        while (it.hasNext()) {
            // einzelnes Text Feld
            TextField textField = (TextField) it.next();
            // Sonderfall: wenn alle Werte in values Array 0 sind, dann wird zurückgesetzt
            if (Arrays.stream(values).sum() == 0) {
                textField.setText("");
            } else {
                textField.setText(String.valueOf(values[i]));
            }
            i++;
        }
    }

    /**
     *
     * @param textFelder Array von Text Feldern dessen Werte ausgelesen werden
     * @return Array der numerischen Einsatzparameter
     */
    int[] getTextFieldValues(TextField[] textFelder) {
        int[] einsatzParameter = new int[textFelder.length];
        // jeder Wert aus dem Text Feld wird ausgelesen und in einsatzParameter abgespeichert
        for (int i = 0; i < textFelder.length; i++) {
            try {
                String value = textFelder[i].getText();
                if (value.equals("")) {
                    setLabelTextMessage(
                            einsatzErstellungMessage,
                            Color.RED,
                            "Keine validen Parameter Werte"
                    );
                }
                einsatzParameter[i] = Integer.parseInt(value);
            } catch (NumberFormatException nfe) {
                assert false : "Kein gültiges Input Format für Einsatz-parmeter";
            }
        }
        return einsatzParameter;
    }

    /**
     * @author Luca Langer
     * @param menuItem dessen Wert ausgelesen wird, im Menü Button angezeigt wird
     */
    void setButtonParameter(MenuItem menuItem) {
        // gewählte Einsatzart abspeichern
        String einsatzart = menuItem.getText();
        // ausgewählte Einsatzart im Menu button anzeigen
        einsatzartMenuButton.setText(einsatzart);
        // Parameter in Textfelder einsetzen
        fillEinsatzParameter(einsatzart);
    }

    /**
     *
     * @param currentParameter aktuellen Parameter
     * @param minParameter minimalen Parameter
     * @return ob beide Arrays die gleichen Werte in der gleichen Reihenfolge haben
     */
    boolean arrayLess(int[] currentParameter, int[] minParameter) {
        assert currentParameter.length == minParameter.length : "Unterschiedlich viele Elemente in den Arrays";
        for (int i = 0; i < currentParameter.length; i++) {
            if (currentParameter[i] < minParameter[i]) return true;
        }
        return false;
    }

    /**
     * Fügt in gewünschtes Text Feld den gewünschten Text ein in gewünschter Farbe
     * @param label Text Feld
     * @param color in die der Text eingefügt wird
     * @param message wird eingefügt
     */
    void setLabelTextMessage(Label label, Color color, String message) {
        label.setTextFill(color);
        label.setStyle("-fx-font-style: italic");
        label.setText(message);
    }

    /**
     * Eingegebene Einsatzparameter sind gültig wenn:
     *      - Im Menü Button eine Einsatzart ausgewählt wurde (Schritt 1)
     *      - Die minimalen Einsatzparameter angegeben wurden (Schritt 2)
     *      - Die aktuellen Einsatz Ressourcen ausreichen (Schritt 3)
     *
     * @author Johan Hornung
     * @param einsatzParameter Array von numerischen Parametern
     * @return gültige Einsatzparameter
     */
    boolean validEinsatzParameter(int[] einsatzParameter) {
        // Schritt 1
        String einsatzart = einsatzartMenuButton.getText();
        if (einsatzart.equals("Einsatzart auswählen")) {
            // Entsprechender Hinweis wird ausgegeben
            setLabelTextMessage(
                    einsatzErstellungMessage,
                    Color.RED,
                    "Keine Einsatzart ausgewählt!"
            );
            return false;
        }
        // Schritt 2

        // Respektive Reihenfolge in Einsatz.einsatzarten und Einsatz.minParameter
        int einsatzIndex = Arrays.asList(Einsatz.einsatzarten).indexOf(einsatzart);
        int[] minParameter = Einsatz.minParameter[einsatzIndex];
        // Wenn die angegebenen Parameter nicht der minimalen Parameter entsprechen
        if (arrayLess(einsatzParameter, minParameter)) {
            setLabelTextMessage(
                    einsatzErstellungMessage,
                    Color.RED,
                    "Minimale Einsatzressourcen für " + einsatzart + " nicht erfüllt!"
            );
            return false;
        }
        // Schritt 3
        // Aktuelle Ressourcen abfragen
        HashMap<String, Integer> firefighters = countFirefighters(team);
        HashMap<String, Integer> vehicles = countVehicles(garage);
        // Mit EinsatzParameter vergleichen

        // Für Feuerwehrleute
        int anzahlFeuerwehrleute = einsatzParameter[0];
        // Wenn nicht genug Feuerwehrleute verfügbar
        if (anzahlFeuerwehrleute > firefighters.get("Lkw-Fahrer") + firefighters.get("Pkw-Fahrer")) {
            setLabelTextMessage(
                    einsatzErstellungMessage,
                    Color.RED,
                    "Zu wenig Feuerwehrleute für " + einsatzart + " Einsatz!"
            );
            return false;
        }
        // Für Fahrzeuge
        // (Reihenfolge der Fahrzeugkategorien respektiv, Kategorien müssen nicht ausgelesen werden)
        int i = 1; // Anzahl der benötigten Fahrzeug startet ab dem 2. Element in einsatzParameter
        for (int vehicleCount : vehicles.values()) {
            if (einsatzParameter[i] > vehicleCount) {
                setLabelTextMessage(
                        einsatzErstellungMessage,
                        Color.RED,
                        "Zu wenig Fahrzeuge für " + einsatzart + "Einsatz!"
                );
                return false;
            }
            i++;
        }
        // Alle Bedigungen für valide Einsatzparameter sind erfüllt
        // Menü Button wird zurückgesetzt
        einsatzartMenuButton.setText("Einsatzart auswählen");

        return true;
    }
    /*
    //////////////////////////// JAVAFX EVENT METHODEN /////////////////////////////////
     */
    /**
     * Methode setzt Text Felder der Einsatz Parameter zurück sowie den Menu Button für Einsatzartauswahl
     *
     * @author Moritz Schmidt
     * @param event On Action event (Buttons)
     */
    @FXML
    void onEinsatzParameterResetClick(ActionEvent event) {
        // Array aus Textfelder für Einsatzparameter
        TextField[] einsatzTextfelder = {
                anzahlFLTextField,
                anzahlELFTextField,
                anzahlTLTextField,
                anzahlMTTextField,
                anzahlLWTextField,
        };
        // Menü Button wird zurückgesetzt
        einsatzartMenuButton.setText("Einsatzart auswählen");
        // "Feedback-Naricht" zur Einsatzerstellung wird zurückgesetzt
        einsatzErstellungMessage.setText("");
        // Text Felder werden zurückgesetzt
        int[] defaultValues = new int[einsatzTextfelder.length];
        setTextFieldValues(einsatzTextfelder, defaultValues);
    }
    @FXML
    void fillIndustrieunfallParameter(ActionEvent event) {
        setButtonParameter(industrieunfallButton);
    }
    @FXML
    void fillNaturkatastropheParameter(ActionEvent event) { setButtonParameter(naturkatastropheButton);
    }
    @FXML
    void fillVerkehrsunfallParameter(ActionEvent event) {
        setButtonParameter(verkehrsunfallButton);
    }
    @FXML
    void fillWohnungsbrandParameter(ActionEvent event) {
        setButtonParameter(wohnungsbrandButton);
    }


    @FXML
    void onCreateOperationClick(ActionEvent event) {
        // TODO: 10.03.22 Algorithmus für Einsatzerstellung schreiben
        // Einsatzparameter Textfelder auslesen
        TextField[] einsatzTextfelder = {
                anzahlFLTextField,
                anzahlELFTextField,
                anzahlTLTextField,
                anzahlMTTextField,
                anzahlLWTextField,
        };
        // Einsatzparameter werden überprüft
        // TODO: 11.03.22 Leere Angaben und nicht numerische Werte gesondert behandeln
        int[] einsatzParameter = getTextFieldValues(einsatzTextfelder);
        System.out.println(Arrays.toString(einsatzParameter));
        if (validEinsatzParameter(einsatzParameter)) {
            // Naricht ausgeben
            setLabelTextMessage(
                    einsatzErstellungMessage,
                    Color.GREEN,
                    "Einsatz wurde erfolgreich erstellt!"
            );
            // Textfelder zurücksetzen
            setTextFieldValues(einsatzTextfelder, new int[einsatzTextfelder.length]);
            // Basierend auf Einsatz parameter das Team und die Fahrzeuge aussuchen
            // TODO: 11.03.22 Suchprozess für Teamerstellung
        }
    }

    @FXML
    void onDeleteOperationClick(ActionEvent event) {
        // TODO: 10.03.22 Algorithmus für Einsatzbeendung schreiben
    }

}
