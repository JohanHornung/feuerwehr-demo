package com;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

public class UserController {
    // Feste Anzahl von Ressourcen
    public final int FIREFIGHTER_CAP = 80;
    public final int VEHICLES_CAP = 18;
    // Array aus möglichen Dienstgraden für Einsatz-Leitfahrzeuge
    // (https://de.wikipedia.org/wiki/Dienstgrade_der_Feuerwehr_in_Hessen#Dienstgrade)
    public final String[] DIENSTGRADE = {
            "Feuerwehrmann",
            "Oberfeuerwehrmann",
            "Löschmeister",
            "Brandmeister",
            "Oberbrandmeister",
            "Hauptlöschmeister"

    };

    @FXML
    private TableView<Map<String, String>> aktiveEinsatzTabelle;

    @FXML
    private TableColumn<Map, String> aktiveEinsatzId;

    @FXML
    private TableColumn<Map, String> aktiveEinsatzart;

    @FXML
    private TableColumn<Map, String> aktiveFahrzeuge;

    @FXML
    private TableColumn<Map, String> aktiveFeuerwehrleute;

    @FXML
    private TableView<Map<String, String>> aktiveFahrzeugTabelle;

    @FXML
    private TableColumn<Map, String> aktiveFahrzeugEinsatzId;

    @FXML
    private TableColumn<Map, String> aktiveFahrzeugId;

    @FXML
    private TableColumn<Map, String> aktiveFahrzeugKategorie;

    @FXML
    private TableColumn<Map, String> aktiveFahrzeugSonderattribut;


    @FXML
    private TextField anzahlELFTextField;

    @FXML
    private TextField anzahlFLTextField;

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
    private Button closeButton;

    @FXML
    private Label einsatzErstellungMessage;

    @FXML
    private MenuButton einsatzartMenuButton;

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
    private MenuItem industrieunfallButton;

    @FXML
    private MenuItem naturkatastropheButton;

    private Feuerwehrmann[] team = new Feuerwehrmann[FIREFIGHTER_CAP];
    private Fahrzeug[] garage = new Fahrzeug[VEHICLES_CAP];
    private HashMap<Integer, Einsatz> aktiveEinsaetze = new HashMap<>();

    @FXML
    /**
     * Methode wird bei der initialisierung der Anwendung (App.main()) ausgeführt und erledigt folgende Schritte:
     *      - Erstellt und befüllt Array von Feuerwehrmännern und Fahrzeugen
     *      - Zählt Ressourcen durch und schreibt jeweilige Werte in die grafischen Text Felder
     */
    public void initialize() {
        // Feuerwehrleute & Fahrzeuge werden initialisiert
        fillResources(team, garage);
        // Ressourcen werden aktualisiert und angezeigt
        displayResources(team, garage);
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
        // Feuerwehrleute
        // 70 Pkw-Fahrer
        String fahrerTyp = "Pkw";
        for (int i = 0; i < FIREFIGHTER_CAP; i++) {
            // Feuerwehrmann ID ist der index im sich befindenden Array
            team[i] = new Feuerwehrmann(i, true, fahrerTyp);
            // i = (FIREFIGHTER_CAP - 11) = 69 bei i = 0 -> 70 Pkw-Fahrer
            if (i >= FIREFIGHTER_CAP - 11) {
                // 10 Lkw-Fahrer
                fahrerTyp = "Lkw";
            };
        }
        // Erstellung der Fahrzeuge in die Garage
        // key:Fahrzeugkategorie, value: anzahl der vefügbaren Fahrzeuge für die Kategorie
        HashMap<String, Integer> anzahlVerfuegbar = new HashMap<>();
        for (int i = 0; i < Fahrzeug.fahrzeugKategorien.length; i++) {
            anzahlVerfuegbar.put(Fahrzeug.fahrzeugKategorien[i], Fahrzeug.fahrzeugAnzahl[i]);
        }
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
     * Methode zählt aktuellen Bestand der Fahrzeuge und Feuerwehrleute
     * und schreibt diese in die Text Felder der GUI.
     *
     * @author Johan Hornung
     * @param team von Feuerwehrleuten, Objekte der Feuerwehrmann Klasse
     * @see Feuerwehrmann
     * @param garage von Fahrzeugen, Objekte der Fahrzeug Klasse
     * @see Fahrzeug
     */
    void displayResources(Feuerwehrmann[] team, Fahrzeug[] garage) {
        // Durchzählen von verfügbaren Feuerwehrleuten (pro Fahrer Typ)
        LinkedHashMap<String, Integer> firefightersCount = countFirefighters(team);
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
        LinkedHashMap<String, Integer> vehicleCount = countVehicles(garage);
        // An diesem Punkt muss das Programm beendet werden falls ein Logik-Fehler vorhanden ist
        assert vehicleCount.values().size() == Fahrzeug.fahrzeugKategorien.length : "Anzahl der Fahrzeugkategorien fehlerhaft";

        for (int i = 0; i < Fahrzeug.fahrzeugKategorien.length; i++) {
            // Output Text ("Anzahl von <Fahrzeugkategorie>: <Wert>")
            String text = vehicleCount.get(Fahrzeug.fahrzeugKategorien[i]).toString();
            // Label wird beschrieben
            verfuegbareFahrzeuge[i].setText(text);
        }
        // TODO: 12.03.22 Menu Button (Einsatzarten) anpassen, switch case
//        wohnungsbrandButton.setVisible(false);
    }
    /**
     * Methode zählt verfügbare Fahrzeuge pro Kategorie durch
     *
     * @author Luca Langer
     * @param garage Array von Fahrzeugen (Fahrzeug Objekt)
     * @see Fahrzeug
     * @return Hashmap mit Anzahl der verfügbaren Fahrzeuge (value) pro Kategorie (key)
     */
    // Durchzählen von verfügbaren Fahrzeugen (pro Kategorie)
    public static LinkedHashMap<String, Integer> countVehicles(Fahrzeug[] garage) {
        LinkedHashMap<String, Integer> vehicleCount = new LinkedHashMap<>();
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
     * Methode zählt verfügbare Feuerwehrmänner pro fahrer Typ durch
     *
     * @author Luca Langer
     * @param team Array von Feuerwehrleuten (Feuerwehrmann Objekten)
     * @see Feuerwehrmann
     * @return Hashmap mit Anzahl der verfügbaren Feuerwehrmänner (value) pro fahrer Typ (key)
     */
    public static LinkedHashMap<String, Integer> countFirefighters(Feuerwehrmann[] team) {
        LinkedHashMap<String, Integer> firefighters = new LinkedHashMap<>();
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
                einsatzParameter = Einsatz.minParameter[i];
            }
        }
        // Beendung des Programms
        if (!found) throw new AssertionError(
                "Keine passenden Einsazparameter gefunden, " +
                "Einsatzart" + einsatzart + " Unbekannt");

        // Werte werden in die Textfelder geschrieben
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
        Iterator<TextField> it = Arrays.stream(textFelder).iterator();
        // Für Array von Werten
        int i = 0;
        while (it.hasNext()) {
            // einzelnes Text Feld
            TextField textField = it.next();
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
        // TODO: 11.03.22 Leere Angaben und nicht numerische Werte gesondert behandeln

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
     * @param min inklusiv
     * @param max inklusiv
     * @return zufälliger Wert zwischen min und max
     */
    int randomNumberInRange(int min , int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }
    /**
     *
     * @param currentParameter aktuellen Parameter
     * @param minParameter minimalen Parameter
     */
    boolean lowerValues(int[] currentParameter, int[] minParameter) {
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
     * @param aktuelleFm Map von aktueller Anzahl an Feuerwehrleuten (nach fahrer Typ)
     * @param aktuelleFz Map von aktueller Anzahl an Fahrzeugen (nach Kategorie)
     *
     * @return gültige Einsatzparameter
     */
    boolean validEinsatzParameter(
            int[] einsatzParameter,
            HashMap<String, Integer> aktuelleFm,
            HashMap<String, Integer> aktuelleFz
    ) {
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

        int einsatzIndex = Arrays.asList(Einsatz.einsatzarten).indexOf(einsatzart);
        int[] minParameter = Einsatz.minParameter[einsatzIndex];
        // Wenn die angegebenen Parameter nicht der minimalen Parameter entsprechen
        if (lowerValues(einsatzParameter, minParameter)) {
            setLabelTextMessage(
                    einsatzErstellungMessage,
                    Color.RED,
                    "Minimale Einsatzressourcen für " + einsatzart + " nicht erfüllt!"
            );
            return false;
        }
        // Schritt 3 - Aktuelle Ressourcen mit Einsatzparameter vergleichen
        // Für Feuerwehrleute
        int anzahlFeuerwehrleute = einsatzParameter[0];
        int anzahlLkwFahrer = einsatzParameter[2] + einsatzParameter[3] + einsatzParameter[4];
        int anzahlAktuelleFm = aktuelleFm.get("Pkw-Fahrer") + aktuelleFm.get("Lkw-Fahrer");

        // Es reicht wenn einer der beiden Fahrer Typen nicht ausreichen
        boolean genugPkwFahrer = einsatzParameter[1] <= aktuelleFm.get("Pkw-Fahrer");
        boolean genugLkwFahrer = anzahlLkwFahrer <= aktuelleFm.get("Lkw-Fahrer");

        if (!(genugLkwFahrer || genugPkwFahrer) || anzahlAktuelleFm < anzahlFeuerwehrleute) {
            setLabelTextMessage(
                    einsatzErstellungMessage,
                    Color.RED,
                    "Zu wenig Feuerwehrleute für " + einsatzart + " Einsatz!"
            );
            return false;
        }
        // Für Fahrzeuge
        int i = 1; // Anzahl der benötigten Fahrzeug startet ab dem 2. Element in einsatzParameter
        for (int vehicleCount : aktuelleFz.values()) {
            if (einsatzParameter[i] > vehicleCount) {
                setLabelTextMessage(
                        einsatzErstellungMessage,
                        Color.RED,
                        "Zu wenig Fahrzeuge für " + einsatzart + " Einsatz!"
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
    /**
     * Methode generiert basierend auf die Kategorien der Fahrzeuge für den aktiven Einsatz (fzTeam)
     * eine Hashmap von Sonderattributen. Die Generierung basiert auf sinvollen/realistischen Daten.
     * @see Fahrzeug
     *
     * @author Sophie Weber
     * @param fzTeam ausgewählte Fahrzeuge für jeweiligen Einsatz
     * @return generierte Sonderattribute pro Fahrzeug
     */
    HashMap<Integer, String> generateSpecialAttributes(HashMap<Integer, Fahrzeug> fzTeam) {
        HashMap<Integer, String> fahrzeugSonderattribute = new HashMap<>();

        for (int id : fzTeam.keySet()) {
            // Zusammengesetzter String aus "<Sonderattribut>: <Wert>"
            String sonderattribut;

            switch (fzTeam.get(id).kategorie) {
                case "Einsatz-Leitfahrzeug":
                    // Dienstgrad wird zufällig ausgesucht
                    int randomIndex = new Random().nextInt(DIENSTGRADE.length);
                    String dienstgrad = DIENSTGRADE[randomIndex];
                    sonderattribut = "Einsatzleiter-Dienstgrad:" + dienstgrad;
                    break;

                case "Tank-Löschfahrzeug":
                    int zufallsWert = randomNumberInRange(600, 1200);
                    // Aufgerundet auf die nächste volle hundert (z.B. 49 -> 100)
                    int tankkapazitaet = ((zufallsWert + 99) / 100) * 100;
                    sonderattribut = "Tankkapazität: " + tankkapazitaet + " L";
                    break;

                case "Mannschaftstransporter":
                    int baujahr = randomNumberInRange(1980, 2022);
                    sonderattribut = "Baujahr: " + baujahr;
                    break;

                case "Leiterwagen":
                    int max_leiterhoehe = randomNumberInRange(1, 15);
                    sonderattribut = "Maximale Leiterhöhe: " + max_leiterhoehe + " m";
                    break;

                default:
                    sonderattribut = "";
            };
            // Dem Fahrzeug das Sonderattribut zugewiesen
            fahrzeugSonderattribute.put(id, sonderattribut);
        }
        return fahrzeugSonderattribute;
    }
    /**
     * Methode füllt eine Tabelle (TableView) mit Daten aus einer erzeugten Hashmap
     * @author Johan Hornung
     * @param table Tabellen Objekt
     * @param columns Spalten Objekte
     * @param columnKeys Spalten Namen für das Festlegen der Spalten-Attribute
     * @param values die Reihenweise eingesetzt werden
     */
    void fillTable(TableView<Map<String, String>> table, TableColumn[] columns, String[] columnKeys, String[] values) {
        // Daten-Objekt
        ObservableList<Map<String, String>> operationMap = FXCollections.<Map<String, String>>observableArrayList();
        // Neue Hashmap mit allen nötigen Attribute (Reihe in der Tabelle)
        Map<String, String> item = new HashMap<>();

        for (int i = 0; i < columns.length; i++) {
            // Festlegen der Spalten-Attribute
            columns[i].setCellValueFactory(new MapValueFactory<>(columnKeys[i]));
            item.put(columnKeys[i], values[i]);
        }
        // Reihe wird zur Tabelle hinzugefügt
        operationMap.add(item);
        table.getItems().addAll(operationMap);
    }
    /**
     * Aktualisiert die Tabelle (GUI) der aktiven Einsätze
     *
     * @param einsatz Objekt der Einsatz Klasse
     * @see Einsatz
     */
    void updateActiveOperationsTable(Einsatz einsatz) {
        // Array von Spalten Objekte
        final TableColumn[] TABLE_COLUMNS = {
                aktiveEinsatzId,
                aktiveEinsatzart,
                aktiveFeuerwehrleute,
                aktiveFahrzeuge
        };
        // Festlegen der Spalten-Attribute
        final String[] COLUMN_KEYS = {
                "id",
                "einsatzart",
                "anzahlFeuerwehrleute",
                "anzahlFahrzeuge"
        };
        String[] values = {
                String.valueOf(einsatz.id),
                einsatz.einsatzart,
                String.valueOf(einsatz.anzahlFeuerwehrleute),
                String.valueOf(einsatz.anzahlFahrzeuge)
        };
        fillTable(aktiveEinsatzTabelle, TABLE_COLUMNS, COLUMN_KEYS, values);
    }
    /**
     * Aktualisiert die Tabelle (GUI) der Sonderattribute für Fahrzuge im Einsatz
     *
     * @author Johan Hornung
     * @param fzTeam eingestzte Fahrzeuge
     * @param sonderattribute der eingesetzten Fahrzeuge
     */
    void updateSpecialAttributesTable(HashMap<Integer, Fahrzeug> fzTeam, HashMap<Integer, String> sonderattribute) {
        // TODO: 12.03.22 Methode für Tabelle der Sonderattribute
        // Array von Spalten Objekte
        final TableColumn[] TABLE_COLUMNS = {
                aktiveFahrzeugId,
                aktiveFahrzeugKategorie,
                aktiveFahrzeugSonderattribut,
                aktiveFahrzeugEinsatzId
        };
        // Festlegen der Spalten-Attribute
        final String[] COLUMN_KEYS = {
                "fahrzeugId",
                "kategorie",
                "sonderattribut",
                "einsatzId"
        };
        // Jede Reihe (Fahrzeug) wird befüllt
        for (Fahrzeug fz: fzTeam.values()) {
            String[] values = {
                    String.valueOf(fz.id),
                    fz.kategorie,
                    sonderattribute.get(fz.id),
                    String.valueOf(fz.einsatzId)
            };
            fillTable(aktiveFahrzeugTabelle, TABLE_COLUMNS, COLUMN_KEYS, values);
        }
    }
    /*
    //////////////////////////// JAVAFX EVENT-BASIERTE METHODEN /////////////////////////////////
     */
    @FXML
    void fillIndustrieunfallParameter(ActionEvent event) {
        setButtonParameter(industrieunfallButton);
    }
    @FXML
    void fillNaturkatastropheParameter(ActionEvent event) {
        setButtonParameter(naturkatastropheButton);
    }
    @FXML
    void fillVerkehrsunfallParameter(ActionEvent event) {
        setButtonParameter(verkehrsunfallButton);
    }
    @FXML
    void fillWohnungsbrandParameter(ActionEvent event) {
        setButtonParameter(wohnungsbrandButton);
    }
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
    // TODO: 11.03.22 JavDoc für onCreateEinsatzClick()
    /**
     * @author Johan Hornung
     * @see Fahrzeug
     * @see Feuerwehrmann
     * @see Einsatz
     */
    @FXML
    void onCreateEinsatzClick(ActionEvent event) {
        // TODO: 10.03.22 Algorithmus für Einsatzerstellung schreiben
        // Einsatzparameter Textfelder auslesen
        TextField[] einsatzTextfelder = {
                anzahlFLTextField,
                anzahlELFTextField,
                anzahlTLTextField,
                anzahlMTTextField,
                anzahlLWTextField,
        };
        // Einsatzparameter werden ausgelesen und überprüft
        String einsatzart = einsatzartMenuButton.getText();
        int[] einsatzParameter = getTextFieldValues(einsatzTextfelder);
        // Aktuelle Ressourcen abfragen
        LinkedHashMap<String, Integer> firefighters = countFirefighters(team);
        LinkedHashMap<String, Integer> vehicles = countVehicles(garage);

        // Hauptbedingung für Einsatzerstellung
        if (validEinsatzParameter(einsatzParameter, firefighters, vehicles)) {
            // Naricht ausgeben
            setLabelTextMessage(
                    einsatzErstellungMessage,
                    Color.GREEN,
                    "Einsatz wurde erfolgreich erstellt!"
            );
            // zufällig generierte id für Einsatz Objekt
            int einsatzId = randomNumberInRange(0, 250);
            // Textfelder zurücksetzen
            setTextFieldValues(einsatzTextfelder, new int[einsatzTextfelder.length]);
            // Teamerstellung, basierend auf einsatzParameter[]
            HashMap<Integer, Feuerwehrmann> fmTeam = new HashMap<>();
            HashMap<Integer, Fahrzeug> fzTeam = new HashMap<>();

            int anzahlFm = einsatzParameter[0];
            // Ermittlung von anzahl an benötigten Fahrern für den Einsatz
            int minAnzahlFahrer = 0;
            for (int i = 1; i < einsatzParameter.length; i++) minAnzahlFahrer += einsatzParameter[i];
            // Das 2. Element im einsatzParameter Array ist immer die Anzahl von Einsatz-Leitfahrzeugen
            int neededPkwFahrer = einsatzParameter[1];
            // gesamte Fahrer Anzahl - anzahl An Pkw Fahrern
            int neededLkwFahrer = minAnzahlFahrer - neededPkwFahrer;

            // Es wird solange nach verfügbaren Pkw-Fahrer gesucht bis die nötige Anzahl erreicht ist
            // Für Pkw-Fahrer
            for (Feuerwehrmann fm: team) {
                // Wenn ein Pkw-Fahrer verfügbar ist wird er eingesetzt
                if (fm.fahrerTyp.equals("Pkw") && fm.verfuegbar) {
                    // Feuerwehrmann ist jetzt nicht mehr verfügbar
                    fm.verfuegbar = false;
                    fmTeam.put(fm.id, fm);
                }
                // Suche wird beendet fall die nötige Anzahl an Pkw-Fahrern erreicht wurde
                if (fmTeam.size() == (minAnzahlFahrer - neededLkwFahrer)) break;
            }
            // Für Lkw-Fahrer (gleiches Prinzip)
            for (Feuerwehrmann fm: team) {
                if (fm.fahrerTyp.equals("Lkw") && fm.verfuegbar) {
                    // Feuerwehrmann ist jetzt nicht mehr verfügbar
                    fm.verfuegbar = false;
                    fmTeam.put(fm.id, fm);
                }
                // Suche wird beendet falls die nötige Anzahl an Lkw-Fahrern erreicht wurde
                if (fmTeam.size() == minAnzahlFahrer) break;
            }
            // Für die restlichen Feuerwehrleute wo der Fahrertyp keine Rolle mehr spielt
            // Falls nicht schon die benötigte Anzahl an Feuerwehrleuten erreicht ist
            if (fmTeam.size() != anzahlFm) {
                for (Feuerwehrmann fm: team) {
                    if (fm.verfuegbar) {
                        fm.verfuegbar = false;
                        fmTeam.put(fm.id, fm);
                    }
                    if (fmTeam.size() == anzahlFm) break;
                }
            }
            // Programm-Logikfehler falls die nötige Anzahl an Feuerwehrleuten nicht erreicht ist
            if (fmTeam.size() != anzahlFm) {
                throw new AssertionError("Nicht genug Feuerwehrleute im Team!");
            }
            // Ressourcen Anzeige für Feuerwehrleute aktualisieren
            displayResources(team, garage);
            // Fahrzeug Hashmap mit Einsatzparameter
            HashMap<String, Integer> fahrzeugParameter = new HashMap<>();
            for (int i = 1; i < einsatzParameter.length; i++) {
                fahrzeugParameter.put(Fahrzeug.fahrzeugKategorien[i-1], einsatzParameter[i]);
            }
            // Fahrzeug-Team Erstellung (Kategorie pro Kategorie)
            for (String category : fahrzeugParameter.keySet()) {
                // Für jede Fahrzeugkategorie wird die nötige Anzahl an Fahrzeugen gesammelt
                int count = 0;
                int anzahl = fahrzeugParameter.get(category);

                for (Fahrzeug fz: garage) {
                    // Wenn für die Fahrzeuge aus einer Kategorie nicht gebraucht werden wird
                    // nicht weiter gemacht
                    if (anzahl == 0) continue;
                    // Treffer (gleiches Prinzip wie bei der Suche nach Feuerwehrleuten)
                    if (category.equals(fz.kategorie) && fz.verfuegbar) {
                        fz.verfuegbar = false;
                        fz.einsatzId = einsatzId;
                        fzTeam.put(fz.id, fz);
                        count++;
                    }
                    // Wenn die gewünschte Anzahl an Fahrzeugen (pro Kategorie) erreicht ist
                    if (anzahl == count) break;
                }
            }
            // Proramm-Logik Fehler, es müssen genau so viele Fahrzeug wie Fahrer für die Fahrzeuge vorhanden sein
            if (fzTeam.size() != minAnzahlFahrer) {
                throw new AssertionError("Nicht genug Feuerwehrleute im Team!");
            }
            // Ressourcen Anzeige aktualisieren
            displayResources(team, garage);

            // Einsatz Objekt wird basierend auf die Parameter (fzTeam, fmTeam) erstellt
            Einsatz einsatz = new Einsatz(einsatzId, einsatzart, fmTeam, fzTeam);
            // Einsatz Tabelle wird aktualisiert
            aktiveEinsaetze.put(einsatz.id, einsatz);
            updateActiveOperationsTable(einsatz);
            // Sonderattribute der Fahrzeuge im Einsatz werden generiert
            HashMap<Integer, String> sonderattribute = generateSpecialAttributes(fzTeam);
            updateSpecialAttributesTable(fzTeam, sonderattribute);

        }
    }

    @FXML
    void onDeleteOperationClick(ActionEvent event) {
        // TODO: 10.03.22 Algorithmus für Einsatzbeendung schreiben
    }
    @FXML
    private void closeButtonAction(){
        // get a handle to the stage
        Stage stage = (Stage) closeButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

}
