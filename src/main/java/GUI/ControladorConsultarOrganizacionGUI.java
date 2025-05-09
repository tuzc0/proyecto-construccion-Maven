package GUI;

import GUI.utilidades.Utilidades;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import logica.DAOs.OrganizacionVinculadaDAO;
import logica.DTOs.OrganizacionVinculadaDTO;


import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ControladorConsultarOrganizacionGUI {

    Logger logger = Logger.getLogger(ControladorConsultarOrganizacionGUI.class.getName());

    @FXML
    private TableColumn <OrganizacionVinculadaDTO, String> columnaNombre;

    @FXML
    private TableColumn <OrganizacionVinculadaDTO, String> columnaCorreo;

    @FXML
    private TableColumn <OrganizacionVinculadaDTO, String> columnaContacto;

    @FXML
    private TableColumn <OrganizacionVinculadaDTO, Void> columnaDetalles;

    @FXML
    private TableView <OrganizacionVinculadaDTO> tablaOrganizaciones;

    @FXML
    private TextField campoCorreo;

    private OrganizacionVinculadaDTO organizacionSeleccionada;

    public static int idOrganizacionSeleccionada;

    @FXML
    public void initialize() {


        tablaOrganizaciones.setCursor(Cursor.HAND);


        columnaNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        columnaCorreo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCorreo()));
        columnaContacto.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNumeroDeContacto()));


        cargarOrganizaciones();

        añadirBotonConsultarATable();

        tablaOrganizaciones.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tablaOrganizaciones.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            organizacionSeleccionada = newValue;
            tablaOrganizaciones.refresh();
        });
    }

    private void cargarOrganizaciones() {

        Utilidades utilidades = new Utilidades();

        try {

            OrganizacionVinculadaDAO organizacionDAO = new OrganizacionVinculadaDAO();
            ObservableList <OrganizacionVinculadaDTO> organizaciones = FXCollections.observableArrayList(organizacionDAO.obtenerTodasLasOrganizaciones());
            tablaOrganizaciones.setItems(organizaciones);

        } catch (IOException e) {

            logger.severe("Error al cargar las organizaciones: " + e.getMessage());
            utilidades.mostrarAlerta("Error", "Error de entrada/salida", "No se pudo cargar la lista de organizaciones.");

        } catch (SQLException e) {

            logger.severe("Error al cargar las organizaciones: " + e.getMessage());
            utilidades.mostrarAlerta("Error", "Error de SQL", "No se pudo cargar la lista de organizaciones.");

        } catch (Exception e) {

            logger.severe("Error inesperado al cargar las organizaciones: " + e.getMessage());
            utilidades.mostrarAlerta("Error", "Error inesperado", "No se pudo cargar la lista de organizaciones.");

        }
    }



    private void añadirBotonConsultarATable() {

        Callback<TableColumn<OrganizacionVinculadaDTO, Void>, TableCell<OrganizacionVinculadaDTO, Void>> cellFactory = param -> new TableCell<>() {

            private final Button botonConsultar = new Button("Consultar Organizacion");

            {
                botonConsultar.setOnAction(event -> {
                    OrganizacionVinculadaDTO organizacion = getTableView().getItems().get(getIndex());
                    abrirVentanaDetallesOrganizacion(organizacion);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView().getItems().get(getIndex()) != organizacionSeleccionada) {
                    setGraphic(null);
                } else {
                    setGraphic(botonConsultar);
                }
            }
        };

        columnaDetalles.setCellFactory(cellFactory);
    }

    private void abrirVentanaDetallesOrganizacion(OrganizacionVinculadaDTO organizacion) {

        Utilidades utilidades = new Utilidades();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestorOrganizacionGUI.fxml"));
            Parent root = loader.load();

            idOrganizacionSeleccionada = organizacion.getIdOrganizacion();


            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Lista de Participantes");
            stage.show();


        } catch (Exception e) {

            logger.severe("Error al abrir la ventana de detalles de la organización: " + e.getMessage());
            utilidades.mostrarAlerta("Error", "Error al abrir la ventana", "No se pudo abrir la ventana de detalles de la organización.");
        }
    }

    @FXML
    private void buscarOrganizacion() {

    }

    @FXML
    private void activarModoSeleccion() {


    }

    @FXML
    private void cancelarSeleccionOrganizacion(){

    }

    @FXML
    private void eliminarOrganizacionSeleccionada(){

    }

    @FXML
    private void abrirVentanaRegistrarOrganizacion(){

    }



}
