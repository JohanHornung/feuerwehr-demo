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

public class HelloController {

    @FXML
    private MenuItem IndustrieunfallButton;

    @FXML
    private MenuItem NaturkatastropheButton;

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
    private Label verfuegbareEinsatzLeitfahrzeuge;

    @FXML
    private Label verfuegbareLeiterwagen;

    @FXML
    private Label verfuegbareLkwFahrer;

    @FXML
    private Label verfuegbarePkwFahrer;

    @FXML
    private Label verfuegbareManschaftstransporter;

    @FXML
    private Label verfuegbareTLFahrzeug;

    @FXML
    private MenuItem verkehrsunfallButton;

    @FXML
    private MenuItem wohnungsbrandButton;

    @FXML
    void fillParameter(ActionEvent event) {

    }

    @FXML
    void onCreateOperationClick(ActionEvent event) {

    }

    @FXML
    void onDeleteOperationClick(ActionEvent event) {

    }

}
