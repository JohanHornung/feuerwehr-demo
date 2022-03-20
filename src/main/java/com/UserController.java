package com;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

/**
 * Beschäftigt sich mit der Nutzerinteraktion in der GUI sowie mit der Programmlogik
 */
public class UserController {
    /**
     * Feste Anzahl von generierten Feuerwehrleuten
     */
    private final int FIREFIGHTER_CAP = 80;
    /**
     * Array von 80 Feuerwehrleuten
     * @see Firefighter
     */
    private final Firefighter[] team = new Firefighter[FIREFIGHTER_CAP];
    /**
     * Array aus möglichen Dienstgraden für Einsatz-Leitfahrzeuge
     * (https://de.wikipedia.org/wiki/Dienstgrade_der_Feuerwehr_in_Hessen#Dienstgrade)
     */
    private final String[] DIENSTGRADE = {
            "Firefighter",
            "Oberfeuerwehrmann",
            "Löschmeister",
            "Brandmeister",
            "Oberbrandmeister",
            "Hauptlöschmeister"
    };
    /**
     * Eintrag für laufende Einsätze (Einsatz ID: Einsatz Objekt)
     * @see Einsatz
     */
    private final HashMap<Integer, Einsatz> aktiveEinsaetze = new HashMap<>();

    /*
                        GUI Elemente (javaFX)
     */
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
    private TableView<Map<String, String>> tableBestandFm;

    @FXML
    private TableColumn<Map, String> bestandFmID;

    @FXML
    private TableColumn<Map, String> bestandFmFahrerTyp;

    @FXML
    private TableColumn<Map, String> bestandFmStatus;

    @FXML
    private TableColumn<Map, String> bestandFmVerfuegbar;

    @FXML
    private TableView<Map<String, String>> tableBestandFz;

    @FXML
    private TableColumn<Map, String> bestandFzID;

    @FXML
    private TableColumn<Map, String> bestandFzKategorie;

    @FXML
    private TableColumn<Map, String> bestandFzKlasse;

    @FXML
    private TableColumn<Map, String> bestandFzStatus;

    @FXML
    private TableColumn<Map, String> bestandFzVerfuegbar;

    @FXML
    private MenuItem fahrzeugVerfuegbarItem;

    @FXML
    private MenuItem fahrzeugWartungItem;

    @FXML
    private MenuItem feuerwehrmannKrankItem;

    @FXML
    private MenuItem feuerwehrmannUrlaubItem;

    @FXML
    private MenuItem feuerwehrmannVerfuegbarItem;

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
    private Button closeButton;

    @FXML
    private Label infoNaricht;

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


    @FXML
    /**
     * Wird bei Initialisierung von App.main() ausgeführt
     */
    void initialize() {
        // Feuerwehrleute & Fahrzeuge werden initialisiert und werden in
        // Bestandstabellen geladen
        ladeRessourcen(team, Vehicle.garage);
        // Ressourcen werden aktualisiert und angezeigt
        zeigeRessourcen(team);

    }
    /**
     * Konfiguriert die initialen Einsatzressourcen (Fahrzeuge + Feuerwehrleute) und
     * schreibt diese in die Bestandstabellen
     *
     * @author Johan Hornung, Luca Langer
     * @param team Array von Feuerwehrleuten
     * @see Firefighter
     * @param garage Array von Fahrzeugen
     * @see Vehicle
     */
    public void ladeRessourcen(Firefighter[] team, Vehicle[] garage) {

        // Erstellung der Firefighter-Objekte
        // 70 Pkw-Fahrer
        String fahrerTyp = "Pkw";
        for (int i = 0; i < FIREFIGHTER_CAP; i++) {
            // Die ID vom Firefighter ist der index im team Array
            team[i] = new Firefighter(i, "frei", true, fahrerTyp);
            // i = (FIREFIGHTER_CAP - 11) = 69 bei i = 0 -> 70 Pkw-Fahrer
            if (i >= FIREFIGHTER_CAP - 11) {
                // 10 Lkw-Fahrer
                fahrerTyp = "Lkw";
            };
        }
        Vehicle.fillGarage();

    }

    /**
     * Methode zählt aktuellen Bestand der Fahrzeuge und Feuerwehrleute
     * und schreibt diese in die Text Felder der GUI.
     *
     * @author Johan Hornung
     * @param team von Feuerwehrleuten
     * @see Firefighter
     */
    public void zeigeRessourcen(Firefighter[] team) {
        // Durchzählen von verfügbaren Feuerwehrleuten (pro Fahrer Typ)
        LinkedHashMap<String, Integer> firefightersCount = zaehleFeuerwehrleute(team);
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
        LinkedHashMap<String, Integer> anzahlFahrzeuge = Vehicle.countVehicles();
        // An diesem Punkt muss das Programm beendet werden falls ein Logik-Fehler vorhanden ist
        assert anzahlFahrzeuge.values().size() == Vehicle.getVEHICLE_CAP() : "Anzahl der Fahrzeugkategorien fehlerhaft";

        Set<String> categories = anzahlFahrzeuge.keySet();
        int i = 0;

        for (String category: categories) {
            // Output Text ("Anzahl von <Fahrzeugkategorie>: <Wert>")
            String text = anzahlFahrzeuge.get(anzahlFahrzeuge.get(category)).toString();
            // Label wird beschrieben
            verfuegbareFahrzeuge[i].setText(text);
            i++;
        }
    }

    /**
     * Methode zählt verfügbare Feuerwehrmänner pro fahrer Typ durch
     *
     * @author Luca Langer
     * @param team Array von Feuerwehrleuten
     * @see Firefighter
     * @return Hashmap mit Anzahl der verfügbaren Feuerwehrmänner (value) pro fahrer Typ (key)
     */
    public LinkedHashMap<String, Integer> zaehleFeuerwehrleute(Firefighter[] team) {
        // Hashmap mit Anzahl der verfügbaren Feuerwehrleute (value) pro Fahrer-Typ (key)
        LinkedHashMap<String, Integer> anzahlFl = new LinkedHashMap<>();
        // Start bei 0
        anzahlFl.put("Lkw", 0);
        anzahlFl.put("Pkw", 0);
        // Gleiches Prinzip wie bei zaehleFahrzeuge()
        for (Firefighter fm: team) {
            boolean isAvailable = fm.getAvailability();
            if (isAvailable) {
                // Erhöhung der Anzahl des jeweiligen Fahrer Typs um 1
                anzahlFl.replace(fm.getDriverType(), anzahlFl.get(fm.getDriverType()) + 1);
            }
        }
        return anzahlFl;
    };
    /**
     * Methode zum automatischen Ausfüllen der mininmal nötigen Einsatzparameter in die
     * jeweiligen Textfelder
     *
     * @author Johan Hornung
     * @param einsatzart vom Nutzer ausgewählte Einsatzart
     * @see Einsatz
     */
    public void ladeMinEinsatzParameter(String einsatzart) {
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
        boolean gefunden = false;
        // Suche nach richtigen Einsatzparameter für jeweilige Einsatzart
        for (int i = 0; i < Einsatz.einsatzarten.length; i++) {
            if (einsatzart.equals(Einsatz.einsatzarten[i])) {
                gefunden = true;
                einsatzParameter = Einsatz.minParameter[i];
            }
        }
        // Beendung des Programms
        if (!gefunden) throw new AssertionError(
                "Keine passenden Einsazparameter gefunden, " +
                "Einsatzart" + einsatzart + " Unbekannt");

        // Werte werden in die Textfelder geschrieben
        setzeTextFeldWerte(einsatzTextfelder, einsatzParameter);
    }

    /**
     * Setzt gegebene Werte in ein Array von Text Feldern (GUI) ein
     *
     * @author Johan Hornung
     * @param textFelder Array von Text Feldern dessen Werte geändert werden
     * @param werte werden eingesetzt
     */
    public void setzeTextFeldWerte(TextField[] textFelder, int[] werte) {

        if (textFelder.length != werte.length) {
            // An diesem Punkt muss das Programm beendet werden
            throw new AssertionError("Anzahl von Text Feldern und Werten stimmt nicht überein");
        }
        // Iteration durch das Array von TextFeldern
        Iterator<TextField> it = Arrays.stream(textFelder).iterator();
        // Schleifenzähler für werte[]
        int i = 0;
        while (it.hasNext()) {
            // einzelnes Text Feld
            TextField textFeld = it.next();
            // Wenn alle Werte in werte[] 0 sind, dann möchte man dass alle Felder "zurückgesetzt" werden
            if (Arrays.stream(werte).sum() == 0) {
                textFeld.setText("");
            } else {
                textFeld.setText(String.valueOf(werte[i]));
            }
            // Nächster Wert
            i++;
        }
    }

    /**
     * Liest Werte aus Text Feldern aus und speichert diese in ein Array
     *
     * @param textFelder Array von Text Feldern dessen Werte ausgelesen werden
     * @return Array der numerischen Werte (Einsatzparameter)
     */
    public int[] getTextFeldWerte(TextField[] textFelder) {
        // Output Array
        int[] einsatzParameter = new int[textFelder.length];

        // jeder Wert aus dem Text Feld wird ausgelesen und abgespeichert
        for (int i = 0; i < textFelder.length; i++) {
            try {
                String wert = textFelder[i].getText();
                if (wert.equals("")) {
                    setzeLabelTextNaricht(
                            infoNaricht,
                            Color.RED,
                            "Keine validen Parameter Werte!"
                    );
                }
                // Werte der Text Felder sind Strings
                int intWert = Integer.parseInt(wert);
                einsatzParameter[i] = intWert;

            } catch (NumberFormatException nfe) {
                setzeLabelTextNaricht(
                        infoNaricht,
                        Color.RED,
                        "Kein gültiges Format für Einsatz-Parameter!"
                );
            }
        }
        return einsatzParameter;
    }
    /**
     * Zeigt die vom Nutzer ausgewählte Einsatzart im Menu Button (GUI).
     * Füllt Einsatzparameter in die Text Felder.
     *
     * @author Luca Langer
     * @param menuItem dessen Wert ausgelesen wird und im Menü Button angezeigt wird
     */
    public void setzeMenuButtonWert(MenuItem menuItem) {
        // gewählte Einsatzart auslesen
        String einsatzart = menuItem.getText();
        // ausgewählte Einsatzart im Menu button anzeigen
        einsatzartMenuButton.setText(einsatzart);
        // Es werden die minimalen Einsatzparameter für die Einsatzart eingesetzt.
        ladeMinEinsatzParameter(einsatzart);
    }
    /**
     * Generiert eine zufällige Ganzzahl zwischen min und max.
     *
     * @author Luca Langer
     * @param min inklusiv
     * @param max inklusiv
     * @return zufälliger Wert zwischen min und max
     */
    public int randomNumberInRange(int min , int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }
    /**
     * Gibt an, ob mindestens ein Wert in aktuelleParameter (an der gleichen Stelle) kleiner ist als in minParameter.
     *
     * @param aktuelleParameter aktuellen Parameter
     * @param minParameter minimalen Parameter
     * @return true sobald ein Wert in den aktuellen Parameter kleiner ist als der Wert der minimalen Parameter
     */
    public boolean arrayKleiner(int[] aktuelleParameter, int[] minParameter) {
        assert aktuelleParameter.length == minParameter.length : "Unterschiedlich viele Elemente in den Arrays";
        for (int i = 0; i < aktuelleParameter.length; i++) {
            if (aktuelleParameter[i] < minParameter[i]) return true;
        }
        return false;
    }

    /**
     * Fügt in gewünschtes Text Feld den gewünschten Text in gewünschter Farbe ein.
     *
     * @author Moritz Schmidt
     * @param label Text Feld
     * @param farbe in die der Text eingefügt wird
     * @param naricht wird eingefügt
     */
    public void setzeLabelTextNaricht(Label label, Color farbe, String naricht) {
        label.setTextFill(farbe);
        label.setText(naricht);
    }

    /**
     * Eingegebene Einsatzparameter sind gültig wenn
     * Im Menü Button eine Einsatzart ausgewählt wurde (Schritt 1).
     * Die minimalen Einsatzparameter angegeben wurden (Schritt 2).
     * Die verfügbaren Einsatz Ressourcen ausreichen (Schritt 3).
     *
     * @author Johan Hornung
     * @param einsatzParameter Array von numerischen Parametern
     * @param aktuelleFm Map von aktueller Anzahl an Feuerwehrleuten (nach fahrer Typ)
     * @param aktuelleFz Map von aktueller Anzahl an Fahrzeugen (nach Kategorie)
     *
     * @return true wenn die vom Nutzer in die Text Felder eingegebene Parameter gültig sind
     */
    public boolean valideEinsatzParameter(
            int[] einsatzParameter,
            HashMap<String, Integer> aktuelleFm,
            HashMap<String, Integer> aktuelleFz
    ) {
        // Schritt 1
        String einsatzart = einsatzartMenuButton.getText();
        // Keine Einsatzart ausgewählt
        if (einsatzart.equals("Einsatzart auswählen")) {
            // Entsprechender Hinweis wird ausgegeben
            setzeLabelTextNaricht(
                    infoNaricht,
                    Color.RED,
                    "Keine Einsatzart ausgewählt!"
            );
            return false;
        // Falls kein Einsatz mehr erstellbar ist
        } else if (einsatzart.equals("Kein neuer Einsatz mehr erstellbar!")) {
            setzeLabelTextNaricht(
                    infoNaricht,
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
        if (arrayKleiner(einsatzParameter, minEinsatzParameter)) {
            setzeLabelTextNaricht(
                    infoNaricht,
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
            setzeLabelTextNaricht(
                    infoNaricht,
                    Color.RED,
                    "Zu wenig Ressourcen für " + einsatzart + " Einsatz!"
            );
            return false;
        }
        // Für Fahrzeuge
        int i = 1; // Anzahl der benötigten Fahrzeuge startet ab dem 2. Element in einsatzParameter
        for (int vehicleCount : aktuelleFz.values()) {
            if (einsatzParameter[i] > vehicleCount) {
                setzeLabelTextNaricht(
                        infoNaricht,
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
    public HashMap<Integer, String> generiereSpezialAttribute(HashMap<Integer, Vehicle> fzTeam) {
        HashMap<Integer, String> fahrzeugSonderattribute = new HashMap<>();
        // Für jedes Vehicle wird ein Sonderattribut generiert
        for (int id : fzTeam.keySet()) {
            // Zusammengesetzter String aus "<Sonderattribut>: <Wert> <Suffix>"
            String sonderattribut;
            // Das Sonderattribut hängt von der Fahrzeugkategorie ab
            switch (fzTeam.get(id).getCategory()) {
                case "Einsatz-Leitfahrzeug":
                    // Dienstgrad wird zufällig ausgesucht
                    int zufallsIndex = new Random().nextInt(DIENSTGRADE.length);
                    String dienstgrad = DIENSTGRADE[zufallsIndex];
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
            // Dem Vehicle das Sonderattribut zugewiesen
            fahrzeugSonderattribute.put(id, sonderattribut);
        }
        return fahrzeugSonderattribute;
    }

    /**
     * Aktualisiert die Bestandstabellen
     *
     * @author Johan Hornung
     * @param team Array aus Feuerwehrleuten
     * @param garage Array aus Fahrzeugen
     */
    void aktualisiereBestandsTabellen(Firefighter[] team, Vehicle[] garage) {
        // Beide Tabellen werden zurückgesetzt und wieder beschrieben
        tableBestandFm.getItems().clear();
        tableBestandFz.getItems().clear();
        // Fahrzeugbestand wird in Fahrzeugtabelle geladen
        // (gleiches Prinzip wie bei aktiven Einsätzen @see updateActiveOperationsTable)

        // Spalten in Vehicle-Tabelle
        TableColumn[] fzSpalten = {
                bestandFzID,
                bestandFzKategorie,
                bestandFzKlasse,
                bestandFzStatus,
                bestandFzVerfuegbar,
        };
        // Vehicle Attribute
        String[] fzAttribute = {
                "id",
                "kategorie",
                "klasse",
                "status",
                "verfuegbar"
        };
        // Tabelle wird reihenweise beschrieben
        for (Vehicle vehicle : garage) {
            // Reihe bestehend aus den Vehicle-Attributen
            String[] werte = {
                    String.valueOf(vehicle.getId()),
                    vehicle.getCategory(),
                    vehicle.getClaass(),
                    vehicle.getStatus(),
                    String.valueOf(vehicle.getAvailability()),
            };
            ladeTabelle(tableBestandFz, fzSpalten, fzAttribute, werte);
        }
        // Feuerwehrmannbestand wird in Tabelle geladen (gleiches Prinzip)
        TableColumn[] fmSpalten = {
                bestandFmID,
                bestandFmFahrerTyp,
                bestandFmStatus,
                bestandFmVerfuegbar,
        };
        // Vehicle Attribute
        String[] fmAttribute = {
                "id",
                "fahrerTyp",
                "status",
                "verfuegbar"
        };
        for (Firefighter fm: team) {
            String[] werte = {
                    String.valueOf(fm.getId()),
                    fm.getDriverType(),
                    fm.getStatus(),
                    String.valueOf(fm.getAvailability()),
            };
            ladeTabelle(tableBestandFm, fmSpalten, fmAttribute, werte);
        }
    }
    /**
     *
     * @param ausgewaehlteOption neuer Status der vom Nutzer ausgewählt wurde (Option im Menü)
     * @param item ausgewählte Reihe aus der Tabelle (Map Object)
     */
    void aktualisiereStatus(MenuItem ausgewaehlteOption, Map<String, String> item) {

        int id = Integer.parseInt(item.get("id"));
        String subject;
        // Wenn das Objekt ein Klassen Attribut besitzt dann handelt es sich um ein Vehicle
        if (item.containsKey("klasse")) subject = "Vehicle";
        else subject = "Firefighter";

        // Neuer Staus wird ausgelesen
        String newStatus = ausgewaehlteOption.getText();
        String currentStatus = item.get("status");

        // Der aktuelle Status ist schon ausgewählt/geltend oder Im Einsatz
        if (currentStatus.equals(newStatus) || currentStatus.equals("im Einsatz")) {
            if (currentStatus.equals("im Einsatz")) {
                setzeLabelTextNaricht(
                        infoNaricht,
                        Color.RED,
                        subject + " ist " + currentStatus
                );
            } else {
                setzeLabelTextNaricht(
                        infoNaricht,
                        Color.RED,
                        subject + " ist bereits " + currentStatus
                );
            }
        } else {
            // Status wird geändert
            if (subject.equals("Firefighter")) {
                team[id].setStatus(newStatus);
                // Falls nötig wird die Verfügbarkeit angepasst
                if (!newStatus.equals("frei")) {
                    team[id].setAvailability(false);
                } else if (!team[id].getStatus().equals("im Einsatz")){
                    team[id].setAvailability(true);
                }
            } else {
                Vehicle.garage[id].setStatus(newStatus);
                String vehicleStatus = Vehicle.garage[id].getStatus();
                // Falls nötig wird die Verfügbarkeit wird angepasst
                if (!newStatus.equals("frei")) {
                    Vehicle.garage[id].setAvailability(false);
                }
                else if (!vehicleStatus.equals("im Einsatz")) {
                    Vehicle.garage[id].setAvailability(true);
                }
            }
        }
        // Ressourcen werden aktualisiert
        zeigeRessourcen(team);
        // Auswahl von möglichen Einsätzen wird aktualisiert
        aktualisiereEinsatzAuswahl(team, Vehicle.garage);
        // Bestandstabellen werden aktualisiert
        aktualisiereBestandsTabellen(team, Vehicle.garage);
    }
    /**
     * Methode füllt eine Tabelle mit Werten aus einer erzeugten Hashmap (Reihenweise).
     *
     * @author Johan Hornung
     * @param tabelle Tabellen Objekt (TableView)
     * @param spalten Spalten Objekte (TableColumn)
     * @param spaltenNamen Spalten Namen für das Festlegen der Spalten-Attribute
     * @param werte werden Reihenweise eingesetzt, gleiche Reihenfolge wie spaltenNamen
     */
    public void ladeTabelle(
            TableView<Map<String, String>> tabelle,
            TableColumn[] spalten,
            String[] spaltenNamen,
            String[] werte
    ) {
        // Daten-Objekt
        ObservableList<Map<String, String>> einsatzAttribute = FXCollections.<Map<String, String>>observableArrayList();
        // Neue Hashmap mit allen nötigen Attributen (Reihe in der Tabelle)
        Map<String, String> item = new HashMap<>();
        // Für jede Spalte
        for (int i = 0; i < spalten.length; i++) {
            // Festlegen der Spalten-Attribute
            spalten[i].setCellValueFactory(new MapValueFactory<>(spaltenNamen[i]));
            // Wert wird eingesetzt
            item.put(spaltenNamen[i], werte[i]);
        }
        // Reihe wird zur Tabelle hinzugefügt
        einsatzAttribute.add(item);
        tabelle.getItems().addAll(einsatzAttribute);
    }
    /**
     * Aktualisiert die Tabelle (GUI) der aktiven Einsätze
     * @author Johan Hornung
     * @param einsatz Objekt der Einsatz Klasse
     * @see Einsatz
     */
    public void aktualisiereAktiveEinsatzTabelle(Einsatz einsatz) {
        // Array von Spalten Objekten
        final TableColumn[] SPALTEN = {
                aktiveEinsatzId,
                aktiveEinsatzart,
                aktiveFeuerwehrleute,
                aktiveFahrzeuge
        };
        // Festlegen der Spalten-Attribute
        final String[] SPALTEN_NAMEN = {
                "id",
                "einsatzart",
                "anzahlFeuerwehrleute",
                "anzahlFahrzeuge"
        };
        final String[] WERTE  = {
                String.valueOf(einsatz.id),
                einsatz.einsatzart,
                String.valueOf(einsatz.anzahlFeuerwehrleute),
                String.valueOf(einsatz.anzahlFahrzeuge)
        };
        // Werte werden in die Tabelle geschrieben
        ladeTabelle(aktiveEinsatzTabelle, SPALTEN, SPALTEN_NAMEN, WERTE);
    }
    /**
     * Aktualisiert die Tabelle (GUI) der Sonderattribute für Fahrzuge im Einsatz.
     *
     * @author Johan Hornung
     * @param fzTeam eingesetzte Fahrzeuge
     * @param sonderattribute der eingesetzten Fahrzeuge
     */
    public void aktualisiereSonderattributenTabelle(HashMap<Integer, Vehicle> fzTeam, HashMap<Integer, String> sonderattribute) {
        // Array von Spalten Objekte
        final TableColumn[] COLUMNS = {
                aktiveFahrzeugId,
                aktiveFahrzeugKategorie,
                aktiveFahrzeugSonderattribut,
                aktiveFahrzeugEinsatzId
        };
        // Festlegen der Spalten-Attribute
        final String[] COLUMNS_ATTRIBUTES = {
                "fahrzeugId",
                "kategorie",
                "sonderattribut",
                "einsatzId"
        };
        // Jede Reihe (Vehicle) wird befüllt
        for (Vehicle fz: fzTeam.values()) {
            final String[] VALUES = {
                    String.valueOf(fz.getId()),
                    fz.getCategory(),
                    sonderattribute.get(fz.getId()),
                    String.valueOf(fz.getOperationId())
            };
            // Werte werden in die Tabelle geschrieben
            ladeTabelle(aktiveFahrzeugTabelle, COLUMNS, COLUMNS_ATTRIBUTES, VALUES);
        }
    }

    /**
     * Aktualisiert die Auswahl an Verfügbaren Einsatzarten im GUI (MenuButton Dropdown).
     *
     * @author Johan Hornung
     * @param team an Feuerwehrmänner
     * @see Firefighter
     * @param garage an Fahrzeugen
     * @see Vehicle
     */
    public void aktualisiereEinsatzAuswahl(Firefighter[] team, Vehicle[] garage) {
        // Standard-Auswahl an Einsätzen (respektive Reihenfolge)
        MenuItem[] einsatzartAuswahl = {
                wohnungsbrandButton,
                verkehrsunfallButton,
                naturkatastropheButton,
                industrieunfallButton
        };
        // Vergleich der aktuellen Ressourcen mit der minimalen Ressourcenanforderungen pro Einsatzart
        LinkedHashMap<String, Integer> anzahlFl = zaehleFeuerwehrleute(team);
        LinkedHashMap<String, Integer> anzahlFz = Vehicle.countVehicles();
        int[][] minParameter = Einsatz.minParameter;

        // Ein vergleichbares Array aus den aktuellen Ressourcen wird produziert
        int[] aktuelleRessourcen = new int[minParameter[1].length];
        // Gesamte Anzahl der verfügbaren Feuerwehrleute
        aktuelleRessourcen[0] = anzahlFl.get("Pkw") + anzahlFl.get("Lkw");
        // Gesamte Anzahl der Verfügbaren Fahrzeuge
        String[] vehicleCategories = Vehicle.getVehicleCategories();

        for (int i = 1; i < aktuelleRessourcen.length; i++) {
            aktuelleRessourcen[i] = anzahlFz.get(vehicleCategories[i-1]);
        }

        // Vergleich mit den für jede Einsatzart minimal erforderlichen Ressourcen
        int anzahlVerfuegbar = Einsatz.einsatzarten.length; // Es sind erstmal alle Einsatzarten verfügbar
        for (int i = 0; i < minParameter.length; i++) {
            MenuItem einsatzart = einsatzartAuswahl[i];
            if (arrayKleiner(aktuelleRessourcen, minParameter[i])) {
                // Einsatzart-Option nicht mehr verfügbar
//                System.out.println(Einsatz.einsatzarten[i] + " nicht mehr verfügbar!");
                einsatzart.setVisible(false);
                anzahlVerfuegbar--;
            } else {
                // Einsatzart-Option wieder verfügbar
                einsatzart.setVisible(true);
                einsatzartMenuButton.setText("Einsatzart auswählen");
            }
        }
        // Falls die Ressourcen für keine Einsatzart mehr ausreichen (= Keine Einsatzarten sind mehr verfügbar)
        if (anzahlVerfuegbar == 0) einsatzartMenuButton.setText("Kein neuer Einsatz mehr erstellbar!");

    }
    /**
     * Überprüft die Gültigkeit der Einsatzparameter (@see valideEinsatzParameter()).
     * Erstellt das Team (Fahrzeuge + Feuerwehrleute) für den Einsatz.
     * Aktualisiert Anzeigen für verfügbare Fahrzeuge und Feuerwehrleute.
     * Erstellt ein neues Einsatz Objekt basierend auf Einsatzart und Einsatz-Parameter.
     * Generiert Sonderattribute für Fahrzeuge und Füllt/Aktualisiert Tabellen.
     *
     * @author Johan Hornung
     * @param einsatzart Attribut für Einsatzobjekt
     * @param einsatzParameter für Team-Erstellung
     * @param einsatzTextfelder in der GUI werden zurückgesetzt
     * @see Einsatz
     */
    public void erstelleEinsatz(String einsatzart, int[] einsatzParameter, TextField[] einsatzTextfelder) {
        // Aktuelle Ressourcen abfragen
        LinkedHashMap<String, Integer> feuerwehrleute = zaehleFeuerwehrleute(team);
        LinkedHashMap<String, Integer> fahrzeuge = Vehicle.countVehicles();

        // 1. HAUPTBEDINGUNG für Einsatzerstellung
        if (valideEinsatzParameter(einsatzParameter, feuerwehrleute, fahrzeuge)) {
            setzeLabelTextNaricht(
                    infoNaricht,
                    Color.GREEN,
                    "Einsatz wurde erfolgreich erstellt!"
            );
            // Textfelder in der GUI zurücksetzen
            setzeTextFeldWerte(einsatzTextfelder, new int[einsatzTextfelder.length]); // [0, 0, 0, ...]
            // zufällig generierte id für Einsatz Objekt
            // falls diese id schon existiert wird eine neue generiert
            int operationID = randomNumberInRange(0, 250);
            if (aktiveEinsaetze.containsKey(operationID)) {
                operationID = randomNumberInRange(0, 250);
            }
            // 2. TEAM (basiert auf die vom Nutzer eingegebenen Einsatzparameter)
            HashMap<Integer, Firefighter> fmTeam = new HashMap<>();
            HashMap<Integer, Vehicle> fzTeam = new HashMap<>();
            // Gesamte Anzahl von eingesetzten Feuerwehrleuten
            int anzahlFm = einsatzParameter[0];
            // Ermittlung von anzahl an benötigten Fahrern für den Einsatz (pro Fahrer Typ)
            int minFahrerAnzahl = 0;
            for (int i = 1; i < einsatzParameter.length; i++) minFahrerAnzahl += einsatzParameter[i];
            // Das 2. Element im einsatzParameter Array ist immer die Anzahl von Einsatz-Leitfahrzeugen
            int gebrauchtePkwFahrer = einsatzParameter[1];

            // Es wird solange nach verfügbaren Pkw-Fahrer gesucht bis die nötige Anzahl erreicht ist
            for (Firefighter fm : team) {
                // Wenn ein Pkw-Fahrer verfügbar ist wird er eingesetzt
                if (fm.getDriverType().equals("Pkw") && fm.getAvailability()) {
                    // Firefighter ist jetzt im Einsatz und nicht mehr verfügbar
                    fm.setAvailability(false);
                    // Neuer Status
                    fm.setStatus("im Einsatz");
                    fmTeam.put(fm.getId(), fm);
                }
                // Suche wird beendet falls die nötige Anzahl an Pkw-Fahrern erreicht wurde
                if (fmTeam.size() == gebrauchtePkwFahrer) break;
            }
            // Gleiches Prinzip für Lkw Fahrer
            for (Firefighter fm : team) {
                if (fm.getDriverType().equals("Lkw") && fm.getAvailability()) {
                    fm.setAvailability(false);
                    fm.setStatus("im Einsatz");
                    fmTeam.put(fm.getId(), fm);
                }
                // Da es nur 2 Fahrer-Typen gibt ist die Anzahl der
                // nötigen Lkw-Fahrer = minAnzahlFahrer - gebrauchtePkwFahrer
                if (fmTeam.size() == minFahrerAnzahl) break;
            }
            // Falls nicht schon die benötigte Anzahl an Feuerwehrleuten erreicht ist
            if (fmTeam.size() != anzahlFm) {
                for (Firefighter fm : team) {
                    if (fm.getAvailability()) {
                        fm.setAvailability(false);
                        fm.setStatus("im Einsatz");
                        fmTeam.put(fm.getId(), fm);
                    }
                    if (fmTeam.size() == anzahlFm) break;
                }
            }
            // Programm-Logikfehler (diese Bedingung wurde vor der Einsatzerstellung erfüllt)
            if (fmTeam.size() != anzahlFm) {
                throw new AssertionError("Nicht genug Feuerwehrleute im Team!");
            }
            // Vehicle Hashmap mit Einsatzparameter
            HashMap<String, Integer> fahrzeugParameter = new HashMap<>();
            String[] vehicleCategories = Vehicle.getVehicleCategories();

            for (int i = 1; i < einsatzParameter.length; i++) {
                fahrzeugParameter.put(vehicleCategories[i-1], einsatzParameter[i]);
            }
            // Vehicle-Team Erstellung (Kategorie pro Kategorie)
            for (String kategorie : fahrzeugParameter.keySet()) {
                // Für jede Fahrzeugkategorie wird die nötige Anzahl an Fahrzeugen gesammelt
                int aktuelleAnzahl = 0;
                int anzahlProKategorie = fahrzeugParameter.get(kategorie);

                for (Vehicle fz : Vehicle.garage) {
                    // Wenn für die Fahrzeuge aus einer Kategorie nicht gebraucht werden wird
                    // nicht weiter gemacht
                    if (anzahlProKategorie == 0) continue;
                    // Treffer (gleiches Prinzip wie bei der Suche nach Feuerwehrleuten)
                    if (kategorie.equals(fz.getCategory()) && fz.getAvailability()) {
                        fz.setAvailability(false);
                        fz.setStatus("im Einsatz");
                        fz.setOperationId(operationID);
                        fzTeam.put(fz.getId(), fz);
                        aktuelleAnzahl++;
                    }
                    // Wenn die gewünschte Anzahl an Fahrzeugen (pro Kategorie) erreicht ist
                    if (aktuelleAnzahl == anzahlProKategorie) break;
                }
            }
            // Proramm-Logik Fehler (diese Bedingung wurde vor der Einsatzerstellung sichergestellt)
            if (fzTeam.size() != minFahrerAnzahl) {
                throw new AssertionError("Nicht genug Feuerwehrleute im Team!");
            }
            // 3. ANZEIGEN
            // Ressourcen Anzeige und Auswahl für verfügbare Einsätze aktualisieren
            zeigeRessourcen(team);
            aktualisiereEinsatzAuswahl(team, Vehicle.garage);
            // Bestandstabelle wird aktualisiert
            aktualisiereBestandsTabellen(team, Vehicle.garage);

            // 4. NEUES EINSATZ OBJEKT
            // Einsatz Objekt wird basierend auf die Parameter (fzTeam, fmTeam) erstellt
            Einsatz einsatz = new Einsatz(operationID, einsatzart, fmTeam, fzTeam);
            aktiveEinsaetze.put(einsatz.id, einsatz);

            // 5. TABELLEN
            // Einsatz Tabelle wird aktualisiert
            aktualisiereAktiveEinsatzTabelle(einsatz);
            // Sonderattribute der Fahrzeuge im Einsatz werden generiert und in Tabelle eingesetzt
            HashMap<Integer, String> sonderattribute = generiereSpezialAttribute(fzTeam);
            aktualisiereSonderattributenTabelle(fzTeam, sonderattribute);
        }
    }

    /**
     * Einsatz Objekt/Item wird aus allen Einträgen gelöscht, Ressourcen werden wieder freigegeben
     *
     * @author Johan Hornung, Luca Langer
     * @param einsatzMap Map Objekt vom Einsatz (nicht Einsatz Objekt) welches in die Tabelle geschrieben wurde
     */
    public void loescheEinsatz(Map<String, String> einsatzMap) {
        int einsatzId = Integer.parseInt(einsatzMap.get("id"));
        // Einsatz wird aus der Tabelle der aktiven Einsätze gelöscht
        aktiveEinsatzTabelle.getItems().removeAll(einsatzMap);
        // Tabelle der aktiven Fahrzeuge
        ObservableList<Map<String, String>> items = aktiveFahrzeugTabelle.getItems();
        // Suche Reihenweise in Tabelle
        // Wenn das Vehicle dem in dem zu löschenden Einsatz eingesetzt wurde wird es gelöscht
        items.removeIf(fahrzeug -> Integer.parseInt(fahrzeug.get("einsatzId")) == einsatzId);

        // Einsatz Objekt mit dazugehörigen Attributen und Parametern, @see erstelleEinsatz
        Einsatz einsatz = aktiveEinsaetze.get(einsatzId);

        // Fahrzeuge werden freigegeben
        for (int fzId: einsatz.fzTeam.keySet()) {
            // NOTICE: Index vom Vehicle in der garage[] ist auch die id von diesem Vehicle, @see ladeRessourcen()
            // Vehicle ist wieder verfügbar
            Vehicle.garage[fzId].setAvailability(true);
            Vehicle.garage[fzId].setStatus("frei");
        }
        // Feuerwehrmänner werden freigegeben
        for (int fmId: einsatz.fmTeam.keySet()) {
            // NOTICE: Gleiches Prinzip wie bei Fahrzeugen
            // Firefighter ist wieder verfügbar
            team[fmId].setAvailability(true);
            team[fmId].setStatus("frei");
        }

        // Ressourcenanzeige wird aktualisiert
        zeigeRessourcen(team);
        // Einsatzauswahl wird aktualisiert
        aktualisiereEinsatzAuswahl(team, Vehicle.garage);
        // Bestandstabelle wird aktualisiert
        aktualisiereBestandsTabellen(team, Vehicle.garage);
        // Einsatz wird aus Eintrag der aktiven Einsätze gelöscht
        aktiveEinsaetze.remove(einsatzId);
    }
    /*
    //////////////////////////// JAVAFX EVENT METHODEN /////////////////////////////////
     */

    /**
     * Automatisches Ausfüllen der Einsatzparameter nach Einsatzart
     *
     */
    @FXML
    void ladeIndustrieunfallParameter(ActionEvent event) {
        setzeMenuButtonWert(industrieunfallButton);
    }
    @FXML
    void ladeNaturkatastropheParameter(ActionEvent event) {
        setzeMenuButtonWert(naturkatastropheButton);
    }
    @FXML
    void ladeVerkehrsunfallParameter(ActionEvent event) {
        setzeMenuButtonWert(verkehrsunfallButton);
    }
    @FXML
    void ladeWohnungsbrandParameter(ActionEvent event) {
        setzeMenuButtonWert(wohnungsbrandButton);
    }

    /**
     * Manuelle Statusänderung des Nutzers im GUI nach
     * Vehicle/Firefighter und ausgewähltem Status
     */
    @FXML
    void fmWechselAufKrank(ActionEvent event) {
        Map<String, String> item = tableBestandFm.getSelectionModel().getSelectedItem();
        // Zurücksetzen der relevanten GUI Elemente
        tableBestandFm.getSelectionModel().clearSelection();
        tableBestandFz.getSelectionModel().clearSelection();
        infoNaricht.setText("");

        // Nutzer hat das falsche oder gar kein Element aus der Firefighter-Tabelle ausgewählt
        if (item == null) {
            setzeLabelTextNaricht(
                    infoNaricht,
                    Color.RED,
                    "Es muss eine Einsatzkraft ausgewählt sein!"
            );
        } else {
            // Gültige Auswahl
            aktualisiereStatus(feuerwehrmannKrankItem, item);
        }
    }
    @FXML
    void fmWechselAufUrlaub(ActionEvent event) {
        Map<String, String> item = tableBestandFm.getSelectionModel().getSelectedItem();
        // Zurücksetzen der relevanten GUI Elemente
        tableBestandFm.getSelectionModel().clearSelection();
        tableBestandFz.getSelectionModel().clearSelection();
        infoNaricht.setText("");

        // Nutzer hat das falsche oder gar kein Element aus der Firefighter-Tabelle ausgewählt
        if (item == null) {
            setzeLabelTextNaricht(
                    infoNaricht,
                    Color.RED,
                    "Es muss eine Einsatzkraft ausgewählt sein!"
            );
        } else {
            // Gültige Auswahl
            aktualisiereStatus(feuerwehrmannUrlaubItem, item);
        }
    }
    @FXML
    void fmWechselAufFrei(ActionEvent event) {
        // Auswahl wird ausgelesen
        Map<String, String> item = tableBestandFm.getSelectionModel().getSelectedItem();
        // Zurücksetzen der relevanten GUI Elemente
        tableBestandFm.getSelectionModel().clearSelection();
        tableBestandFz.getSelectionModel().clearSelection();
        infoNaricht.setText("");

        // Nutzer hat das falsche oder gar kein Element aus der Firefighter-Tabelle ausgewählt
        if (item == null) {
            setzeLabelTextNaricht(
                    infoNaricht,
                    Color.RED,
                    "Es muss eine Einsatzkraft ausgewählt sein!"
            );
        } else {
            // Gültige Auswahl
            aktualisiereStatus(feuerwehrmannVerfuegbarItem, item);
        }

    }
    @FXML
    void fzWechselAufFrei(ActionEvent event) {
        // Auswahl wird ausgelesen
        Map<String, String> item = tableBestandFz.getSelectionModel().getSelectedItem();
        // Zurücksetzen der relevanten GUI Elemente
        tableBestandFz.getSelectionModel().clearSelection();
        tableBestandFm.getSelectionModel().clearSelection();
        infoNaricht.setText("");

        // Nutzer hat das falsche oder gar kein Element aus der Firefighter-Tabelle ausgewählt
        if (item == null) {
            setzeLabelTextNaricht(
                    infoNaricht,
                    Color.RED,
                    "Es muss ein Vehicle ausgewählt sein!"
            );
        } else {
            // Gültige Auswahl
            aktualisiereStatus(fahrzeugVerfuegbarItem, item);
        }
    }
    @FXML
    void fzWechselAufWartung(ActionEvent event) {
        // Auswahl wird ausgelesen
        Map<String, String> item = tableBestandFz.getSelectionModel().getSelectedItem();
        // Zurücksetzen der relevanten GUI Elemente
        tableBestandFz.getSelectionModel().clearSelection();
        tableBestandFm.getSelectionModel().clearSelection();
        infoNaricht.setText("");

        // Nutzer hat das falsche oder gar kein Element aus der Firefighter-Tabelle ausgewählt
        if (item == null) {
            setzeLabelTextNaricht(
                    infoNaricht,
                    Color.RED,
                    "Es muss ein Vehicle ausgewählt sein!"
            );
        } else {
            // Gültige Auswahl
            aktualisiereStatus(fahrzeugWartungItem, item);
        }
    }
    /**
     * Methode setzt Text Felder der Einsatz Parameter zurück sowie den Menu Button für Einsatzartauswahl
     *
     * @author Moritz Schmidt
     * @param event On Action event (Buttons)
     */
    @FXML
    void onResetClick(ActionEvent event) {
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
        infoNaricht.setText("");
        // Text Felder werden zurückgesetzt
        int[] standartWerte = new int[einsatzTextfelder.length];
        setzeTextFeldWerte(einsatzTextfelder, standartWerte);
    }

    @FXML
    void onCreateClick(ActionEvent event) {
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
        int[] einsatzParameter = getTextFeldWerte(einsatzTextfelder);
        // Einsatz wird erstellt
        erstelleEinsatz(einsatzart, einsatzParameter, einsatzTextfelder);
    }

    @FXML
    void onDeleteClick(ActionEvent event) {
        // Daten-Objekt (Einsatz Map) welches in die Tabelle geschrieben wurde wird ausgelesen, @see ladeTabelle()
        Map<String, String> einsatzMap = aktiveEinsatzTabelle.getSelectionModel().getSelectedItem();
        // Sonst wird der nächste Einsatz in der Tabelle automatisch ausgewählt
        aktiveEinsatzTabelle.getSelectionModel().clearSelection();
        // Es gibt keine laufenden Einsätze
        if (aktiveEinsaetze.isEmpty()) {
            setzeLabelTextNaricht(
                    infoNaricht,
                    Color.RED,
                    "Es gibt keine aktiven Einsätze!"
            );
        // Nutzer hat kein Einsatz in der Tabelle ausgewählt
        } else if (einsatzMap == null) {
            setzeLabelTextNaricht(
                    infoNaricht,
                    Color.RED,
                    "Es wurde kein Einsatz ausgewählt!"
            );
        // ausgewählter Einsatz wird gelöscht
        } else {
            setzeLabelTextNaricht(
                    infoNaricht,
                    Color.GREEN,
                    "Einsatz Nummer " + einsatzMap.get("id") + " gelöscht. " +
                            "Ressourcen werden freigegeben."
            );
            loescheEinsatz(einsatzMap);
        }
    }
    @FXML
    void closeButtonAction(){
        // get a handle to the stage
        Stage stage = (Stage) closeButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}
