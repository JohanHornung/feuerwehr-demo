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
        String anzahlLkwFahrer = firefightersCount.get("Lkw").toString();
        String anzahlPkwFahrer = firefightersCount.get("Pkw").toString();
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
        // Start bei 0
        firefighters.put("Lkw", 0);
        firefighters.put("Pkw", 0);

        for (int i = 0; i < team.length; i++) {
            if (team[i].verfuegbar) {
                // Erhöhung der Anzahl des jeweiligen Fahrer Typs um 1
                firefighters.replace(team[i].fahrerTyp, firefighters.get(team[i].fahrerTyp) + 1);
            }
        }
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
    void fillMinEinsatzParameter(String einsatzart) {
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
     * Setzt gegebene Werte in ein Array von Text Feldern (GUI) ein
     *
     * @author Johan Hornung
     * @param textFelder Array von Text Feldern dessen Werte geändert werden
     * @param values werden eingesetzt
     */
    void setTextFieldValues(TextField[] textFelder, int[] values) {

        if (textFelder.length != values.length) {
            // An diesem Punkt muss das Programm beendet werden
            throw new AssertionError("Anzahl von Text Feldern und Werten stimmt nicht überein");
        }
        // Iteration durch das Array von TextFeldern
        Iterator<TextField> it = Arrays.stream(textFelder).iterator();
        // Schleifenzähler für values[]
        int i = 0;
        while (it.hasNext()) {
            // einzelnes Text Feld
            TextField textField = it.next();
            // Wenn alle Werte in values[] 0 sind, dann möchte man dass alle Felder "zurückgesetzt" werden
            if (Arrays.stream(values).sum() == 0) {
                textField.setText("");
            } else {
                textField.setText(String.valueOf(values[i]));
            }
            // Nächster Wert
            i++;
        }
    }

    /**
     * Liest Werte in aus Text Feldern aus und in ein Array abgespeichert
     *
     * @param textFelder Array von Text Feldern dessen Werte ausgelesen werden
     * @return Array der numerischen Einsatzparametern
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
                            "Keine validen Parameter Werte!"
                    );
                }
                einsatzParameter[i] = Integer.parseInt(value);
            } catch (NumberFormatException nfe) {
                setLabelTextMessage(
                        einsatzErstellungMessage,
                        Color.RED,
                        "Kein gültiges Format für Einsatz-Parameter!"
                );
            }
        }
        return einsatzParameter;
    }

    /**
     * Die vom Nutzer ausgewählte Einsatzart wird im Menu Button (GUI) angezeigt.
     * Es werden die minimalen Einsatzparameter für die Einsatzart eingesetzt.
     *
     * @author Luca Langer
     * @param menuItem dessen Wert ausgelesen wird und im Menü Button angezeigt wird
     */
    void setMenuButtonValue(MenuItem menuItem) {
        // gewählte Einsatzart auslesen
        String einsatzart = menuItem.getText();
        // ausgewählte Einsatzart im Menu button anzeigen
        einsatzartMenuButton.setText(einsatzart);
        // Minimalen Einsatzparameter für die Einsatzart
        fillMinEinsatzParameter(einsatzart);
    }
    /**
     * @author Luca Langer
     * @param min inklusiv
     * @param max inklusiv
     * @return zufälliger Wert zwischen min und max
     */
    int randomNumberInRange(int min , int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }
    /**
     * Gibt an, ob mindestens ein Wert in currentParameter an der gleichen Stelle kleiner ist als in minParameter.
     *
     * @param currentParameter aktuellen Parameter
     * @param minParameter minimalen Parameter
     * @return true sobald ein Wert in den aktuellen Parameter kleiner ist als der Wert der minimalen Parameter
     */
    boolean lowerValue(int[] currentParameter, int[] minParameter) {
        assert currentParameter.length == minParameter.length : "Unterschiedlich viele Elemente in den Arrays";
        for (int i = 0; i < currentParameter.length; i++) {
            if (currentParameter[i] < minParameter[i]) return true;
        }
        return false;
    }

    /**
     * Fügt in gewünschtes Text Feld den gewünschten Text ein in gewünschter Farbe
     *
     * @author Moritz Schmidt
     * @param label Text Feld
     * @param color in die der Text eingefügt wird
     * @param message wird eingefügt
     */
    void setLabelTextMessage(Label label, Color color, String message) {
        label.setTextFill(color);
//        label.setStyle("-fx-font-style: italic");
        label.setText(message);
    }

    /**
     * Eingegebene Einsatzparameter sind gültig wenn:
     *      - Im Menü Button eine Einsatzart ausgewählt wurde (Schritt 1)
     *      - Die minimalen Einsatzparameter angegeben wurden (Schritt 2)
     *      - Die verfügbaren Einsatz Ressourcen ausreichen (Schritt 3)
     *
     * @author Johan Hornung
     * @param einsatzParameter Array von numerischen Parametern
     * @param aktuelleFm Map von aktueller Anzahl an Feuerwehrleuten (nach fahrer Typ)
     * @param aktuelleFz Map von aktueller Anzahl an Fahrzeugen (nach Kategorie)
     *
     * @return true wenn die vom Nutzer in die Text Felder eingegebene Parameter gültig sind
     */
    boolean validEinsatzParameter(
            int[] einsatzParameter,
            HashMap<String, Integer> aktuelleFm,
            HashMap<String, Integer> aktuelleFz
    ) {
        // Schritt 1
        String einsatzart = einsatzartMenuButton.getText();
        // Keine Einsatzart ausgewählt
        if (einsatzart.equals("Einsatzart auswählen")) {
            // Entsprechender Hinweis wird ausgegeben
            setLabelTextMessage(
                    einsatzErstellungMessage,
                    Color.RED,
                    "Keine Einsatzart ausgewählt!"
            );
            return false;
        // Falls kein Einsatz mehr erstellbar ist
        } else if (einsatzart.equals("Kein neuer Einsatz mehr erstellbar!")) {
            setLabelTextMessage(
                    einsatzErstellungMessage,
                    Color.RED,
                    "Kein neuer Einsatz mehr erstellbar!"
            );
            return false;
        }
        // Schritt 2
        // @see Einsatz
        int einsatzIndex = Arrays.asList(Einsatz.einsatzarten).indexOf(einsatzart);
        int[] minEinsatzParameter = Einsatz.minParameter[einsatzIndex];
        // Wenn die angegebenen Parameter nicht der minimalen Parameter entsprechen
        if (lowerValue(einsatzParameter, minEinsatzParameter)) {
            setLabelTextMessage(
                    einsatzErstellungMessage,
                    Color.RED,
                    "Minimale Einsatzressourcen für " + einsatzart + " nicht erfüllt!"
            );
            return false;
        }
        // Schritt 3
        // Für Feuerwehrleute
        int anzahlFeuerwehrleute = einsatzParameter[0];
        int anzahlLkwFahrer = einsatzParameter[2] + einsatzParameter[3] + einsatzParameter[4];
        int anzahlAktuelleFm = aktuelleFm.get("Pkw") + aktuelleFm.get("Lkw");

        // Es reicht wenn einer der beiden Fahrer Typen nicht ausreichen
        boolean genugPkwFahrer = einsatzParameter[1] <= aktuelleFm.get("Pkw");
        boolean genugLkwFahrer = anzahlLkwFahrer <= aktuelleFm.get("Lkw");

        if (!(genugLkwFahrer || genugPkwFahrer) || anzahlAktuelleFm < anzahlFeuerwehrleute) {
            setLabelTextMessage(
                    einsatzErstellungMessage,
                    Color.RED,
                    "Zu wenig Ressourcen für " + einsatzart + " Einsatz!"
            );
            return false;
        }
        // Für Fahrzeuge
        int i = 1; // Anzahl der benötigten Fahrzeuge startet ab dem 2. Element in einsatzParameter
        for (int vehicleCount : aktuelleFz.values()) {
            if (einsatzParameter[i] > vehicleCount) {
                setLabelTextMessage(
                        einsatzErstellungMessage,
                        Color.RED,
                        "Zu wenig Ressourcen für " + einsatzart + " Einsatz!"
                );
                return false;
            }
            i++;
        }
        // Alle Bedingungen für valide Einsatzparameter sind erfüllt
        // Menü Button wird zurückgesetzt
        einsatzartMenuButton.setText("Einsatzart auswählen");

        return true;
    }
    /**
     * Methode generiert basierend auf die Kategorien der eingesetzten Fahrzeuge (fzTeam)
     * eine Hashmap von Sonderattributen. Die Generierung basiert auf sinnvollen/realistischen Daten.
     *
     * @author Sophie Weber
     * @param fzTeam eingesetzte Fahrzeuge
     * @see Einsatz
     * @return generierte Sonderattribute für alle Fahrzeuge
     */
    HashMap<Integer, String> generateSpecialAttributes(HashMap<Integer, Fahrzeug> fzTeam) {
        HashMap<Integer, String> fahrzeugSonderattribute = new HashMap<>();
        // Für jedes Fahrzeug wird ein Sonderattribut generiert
        for (int id : fzTeam.keySet()) {
            // Zusammengesetzter String aus "<Sonderattribut>: <Wert> <Suffix>"
            String sonderattribut;
            // Das Sonderattribut hängt von der Fahrzeugkategorie ab
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
                // Sollte nicht passieren
                default:
                    sonderattribut = "";
            };
            // Dem Fahrzeug das Sonderattribut zugewiesen
            fahrzeugSonderattribute.put(id, sonderattribut);
        }
        return fahrzeugSonderattribute;
    }
    /**
     * Methode füllt eine Tabelle mit Werten aus einer erzeugten Hashmap (Reihenweise).
     *
     * @author Johan Hornung
     * @param table Tabellen Objekt (TableView)
     * @param columns Spalten Objekte (TableColumn)
     * @param columnKeys Spalten Namen für das Festlegen der Spalten-Attribute
     * @param values werden Reihenweise eingesetzt, gleiche Reihenfolge wie columnKeys
     */
    void fillTable(TableView<Map<String, String>> table, TableColumn[] columns, String[] columnKeys, String[] values) {
        // Daten-Objekt
        ObservableList<Map<String, String>> operationMap = FXCollections.<Map<String, String>>observableArrayList();
        // Neue Hashmap mit allen nötigen Attributen (Reihe in der Tabelle)
        Map<String, String> item = new HashMap<>();
        // Für jede Spalte
        for (int i = 0; i < columns.length; i++) {
            // Festlegen der Spalten-Attribute
            columns[i].setCellValueFactory(new MapValueFactory<>(columnKeys[i]));
            // Wert wird eingesetzt
            item.put(columnKeys[i], values[i]);
        }
        // Reihe wird zur Tabelle hinzugefügt
        operationMap.add(item);
        table.getItems().addAll(operationMap);
    }
    /**
     * Aktualisiert die Tabelle (GUI) der aktiven Einsätze
     * @author Johan Hornung
     * @param einsatz Objekt der Einsatz Klasse
     * @see Einsatz
     */
    void updateActiveOperationsTable(Einsatz einsatz) {
        // Array von Spalten Objekten
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
        // Werte werden in die Tabelle geschrieben
        fillTable(aktiveEinsatzTabelle, TABLE_COLUMNS, COLUMN_KEYS, values);
    }
    /**
     * Aktualisiert die Tabelle (GUI) der Sonderattribute für Fahrzuge im Einsatz.
     *
     * @author Johan Hornung
     * @param fzTeam eingesetzte Fahrzeuge
     * @param sonderattribute der eingesetzten Fahrzeuge
     */
    void updateSpecialAttributesTable(HashMap<Integer, Fahrzeug> fzTeam, HashMap<Integer, String> sonderattribute) {
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
            // Werte werden in die Tabelle geschrieben
            fillTable(aktiveFahrzeugTabelle, TABLE_COLUMNS, COLUMN_KEYS, values);
        }
    }

    /**
     * Aktualisiert die Auswahl an Verfügbaren Einsatzarten im GUI (MenuButton Dropdown).
     *
     * @author Johan Hornung
     * @param team an Feuerwehrmänner
     * @see Feuerwehrmann
     * @param garage an Fahrzeugen
     * @see Fahrzeug
     */
    void updateEinsatzAuswahl(Feuerwehrmann[] team, Fahrzeug[] garage) {
        // Standard-Auswahl an Einsätzen (respektive Reihenfolge)
        MenuItem[] einsatzartAuswahl = {
                wohnungsbrandButton,
                verkehrsunfallButton,
                naturkatastropheButton,
                industrieunfallButton
        };
        // Vergleich der aktuellen Ressourcen mit der minimalen Ressourcenanforderungen pro Einsatzart
        LinkedHashMap<String, Integer> firefighters = countFirefighters(team);
        LinkedHashMap<String, Integer> vehicles = countVehicles(garage);
        int[][] minParameter = Einsatz.minParameter;

        // Ein vergleichbares Array aus den aktuellen Ressourcen wird produziert
        int[] aktuelleRessourcen = new int[minParameter[1].length];
        // Gesamte Anzahl der verfügbaren Feuerwehrleute
        aktuelleRessourcen[0] = firefighters.get("Pkw") + firefighters.get("Lkw");
        // Gesamte Anzahl der Verfügbaren Fahrzeuge
        for (int i = 1; i < aktuelleRessourcen.length; i++) {
            aktuelleRessourcen[i] = vehicles.get(Fahrzeug.fahrzeugKategorien[i-1]);
        }

        // Vergleich mit den für jede Einsatzart minimal erforderlichen Ressourcen
        int anzahlVerfuegbar = Einsatz.einsatzarten.length; // Es sind erstmal alle Einsatzarten verfügbar
        for (int i = 0; i < minParameter.length; i++) {
            if (lowerValue(aktuelleRessourcen, minParameter[i])) {
                // Einsatzart-Option nicht mehr verfügbar
//                System.out.println(Einsatz.einsatzarten[i] + " nicht mehr verfügbar!");
                einsatzartAuswahl[i].setVisible(false);
                anzahlVerfuegbar--;
            }
        }
        // Falls die Ressourcen für keine Einsatzart mehr ausreichen (= Keine Einsatzarten sind mehr verfügbar)
        if (anzahlVerfuegbar == 0) einsatzartMenuButton.setText("Kein neuer Einsatz mehr erstellbar!");
    }
    /**
     * 1. Überprüft die Gültigkeit der Einsatzparameter (@see validEinsatzParameter())
     * 2. Erstellt das Team (Fahrzeuge + Feuerwehrleute) für den Einsatz
     * 3. Aktualisiert Anzeigen für verfügbare Fahrzeuge und Feuerwehrleute
     * 4. Erstellt ein neues Einsatz Objekt basierend auf Einsatzart und Einsatz-Parameter.
     * 4. Generiert Sonderattribute für Fahrzeuge und Füllt/Aktualisiert Tabellen
     *
     * @author Johan Hornung
     * @param einsatzart
     * @param einsatzParameter
     * @see Einsatz
     */
    void createEinsatz(String einsatzart, int[] einsatzParameter, TextField[] einsatzTextfelder) {
        // Aktuelle Ressourcen abfragen
        LinkedHashMap<String, Integer> firefighters = countFirefighters(team);
        LinkedHashMap<String, Integer> vehicles = countVehicles(garage);

        // 1. HAUPTBEDINGUNG für Einsatzerstellung
        if (validEinsatzParameter(einsatzParameter, firefighters, vehicles)) {
            setLabelTextMessage(
                    einsatzErstellungMessage,
                    Color.GREEN,
                    "Einsatz wurde erfolgreich erstellt!"
            );
            // Textfelder (Einsatzparameter) zurücksetzen
            setTextFieldValues(einsatzTextfelder, new int[einsatzTextfelder.length]); // [0, 0, 0, ...]
            // zufällig generierte id für Einsatz Objekt
            int einsatzId = randomNumberInRange(0, 100);

            // 2. TEAM (basiert auf die vom Nutzer eingegebenen Einsatzparameter)
            HashMap<Integer, Feuerwehrmann> fmTeam = new HashMap<>();
            HashMap<Integer, Fahrzeug> fzTeam = new HashMap<>();
            // Gesamte Anzahl von eingesetzten Feuerwehrleuten
            int anzahlFm = einsatzParameter[0];
            // Ermittlung von anzahl an benötigten Fahrern für den Einsatz (pro Fahrer Typ)
            int minAnzahlFahrer = 0;
            for (int i = 1; i < einsatzParameter.length; i++) minAnzahlFahrer += einsatzParameter[i];
            // Das 2. Element im einsatzParameter Array ist immer die Anzahl von Einsatz-Leitfahrzeugen
            int neededPkwFahrer = einsatzParameter[1];

            // Es wird solange nach verfügbaren Pkw-Fahrer gesucht bis die nötige Anzahl erreicht ist
            for (Feuerwehrmann fm : team) {
                // Wenn ein Pkw-Fahrer verfügbar ist wird er eingesetzt
                if (fm.fahrerTyp.equals("Pkw") && fm.verfuegbar) {
                    // Feuerwehrmann ist jetzt im Einsatz und nicht mehr verfügbar
                    fm.verfuegbar = false;
                    fmTeam.put(fm.id, fm);
                }
                // Suche wird beendet falls die nötige Anzahl an Pkw-Fahrern erreicht wurde
                if (fmTeam.size() == neededPkwFahrer) break;
            }
            // Für Lkw-Fahrer (gleiches Prinzip)
            for (Feuerwehrmann fm : team) {
                if (fm.fahrerTyp.equals("Lkw") && fm.verfuegbar) {
                    fm.verfuegbar = false;
                    fmTeam.put(fm.id, fm);
                }
                // Da es nur 2 Fahrer-Typen gibt ist die Anzahl der
                // nötigen Lkw-Fahrer = minAnzahlFahrer - neededPkwFahrer
                if (fmTeam.size() == minAnzahlFahrer) break;
            }
            // Für die restlichen Feuerwehrleute wo der Fahrertyp keine Rolle mehr spielt
            // Falls nicht schon die benötigte Anzahl an Feuerwehrleuten erreicht ist
            if (fmTeam.size() != anzahlFm) {
                for (Feuerwehrmann fm : team) {
                    if (fm.verfuegbar) {
                        fm.verfuegbar = false;
                        fmTeam.put(fm.id, fm);
                    }
                    if (fmTeam.size() == anzahlFm) break;
                }
            }
            // Programm-Logikfehler (diese Bedingung wurde vor der Einsatzerstellung sichergestellt)
            if (fmTeam.size() != anzahlFm) {
                throw new AssertionError("Nicht genug Feuerwehrleute im Team!");
            }
            // Fahrzeug Hashmap mit Einsatzparameter
            HashMap<String, Integer> fahrzeugParameter = new HashMap<>();
            for (int i = 1; i < einsatzParameter.length; i++) {
                fahrzeugParameter.put(Fahrzeug.fahrzeugKategorien[i - 1], einsatzParameter[i]);
            }
            // Fahrzeug-Team Erstellung (Kategorie pro Kategorie)
            for (String category : fahrzeugParameter.keySet()) {
                // Für jede Fahrzeugkategorie wird die nötige Anzahl an Fahrzeugen gesammelt
                int count = 0;
                int anzahl = fahrzeugParameter.get(category);

                for (Fahrzeug fz : garage) {
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
            // Proramm-Logik Fehler (diese Bedingung wurde vor der Einsatzerstellung sichergestellt)
            if (fzTeam.size() != minAnzahlFahrer) {
                throw new AssertionError("Nicht genug Feuerwehrleute im Team!");
            }
            // 3. ANZEIGEN
            // Ressourcen Anzeige und Auswahl für verfügbare Einsätze aktualisieren
            displayResources(team, garage);
            updateEinsatzAuswahl(team, garage);

            // 4. NEUES EINSATZ OBJEKT
            // Einsatz Objekt wird basierend auf die Parameter (fzTeam, fmTeam) erstellt
            Einsatz einsatz = new Einsatz(einsatzId, einsatzart, fmTeam, fzTeam);
            aktiveEinsaetze.put(einsatz.id, einsatz);

            // 5. TABELLEN
            // Einsatz Tabelle wird aktualisiert
            updateActiveOperationsTable(einsatz);
            // Sonderattribute der Fahrzeuge im Einsatz werden generiert und in Tabelle eingesetzt
            HashMap<Integer, String> sonderattribute = generateSpecialAttributes(fzTeam);
            updateSpecialAttributesTable(fzTeam, sonderattribute);
        }
    }
    /*
    //////////////////////////// JAVAFX EVENT-BASIERTE METHODEN /////////////////////////////////
     */
    @FXML
    void fillIndustrieunfallParameter(ActionEvent event) {
        setMenuButtonValue(industrieunfallButton);
    }
    @FXML
    void fillNaturkatastropheParameter(ActionEvent event) {
        setMenuButtonValue(naturkatastropheButton);
    }
    @FXML
    void fillVerkehrsunfallParameter(ActionEvent event) {
        setMenuButtonValue(verkehrsunfallButton);
    }
    @FXML
    void fillWohnungsbrandParameter(ActionEvent event) {
        setMenuButtonValue(wohnungsbrandButton);
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
        // Menü Button wird zurückgesetzt falls noch neue Einsätze möglich sind
        if (!einsatzartMenuButton.getText().equals("Kein neuer Einsatz mehr erstellbar!")) {
            einsatzartMenuButton.setText("Einsatzart auswählen");
        }
        // Rückmeldung (Naricht) zur Einsatzerstellung wird zurückgesetzt
        einsatzErstellungMessage.setText("");
        // Text Felder werden zurückgesetzt
        int[] defaultValues = new int[einsatzTextfelder.length];
        setTextFieldValues(einsatzTextfelder, defaultValues);
    }

    @FXML
    void onCreateEinsatzClick(ActionEvent event) {
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
        // Einsatz wird erstellt
        createEinsatz(einsatzart, einsatzParameter, einsatzTextfelder);
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
