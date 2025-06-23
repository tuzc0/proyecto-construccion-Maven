package GUI.gestionorganizacion;

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
import logica.ManejadorExcepciones;
import logica.interfaces.IGestorAlertas;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.sql.SQLException;

public class ControladorConsultarOrganizacionGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorConsultarOrganizacionGUI.class);

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
    private TextField campoBusqueda;

    @FXML
    private TableColumn <OrganizacionVinculadaDTO, Void> columnaBotonEliminar;

    private OrganizacionVinculadaDTO organizacionSeleccionada;

    IGestorAlertas mensajeDeAlerta = new Utilidades();

    Utilidades utilidades = new Utilidades();

    ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(mensajeDeAlerta, logger);

    public static int idOrganizacionSeleccionada = 0;

    @FXML
    public void initialize() {

        tablaOrganizaciones.setCursor(Cursor.HAND);

        columnaNombre.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        columnaCorreo.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCorreo()));
        columnaContacto.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNumeroDeContacto()));


        cargarOrganizaciones();

        añadirBotonConsultarATable();
        añadirBotonEliminarATable();

        tablaOrganizaciones.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tablaOrganizaciones.getSelectionModel().selectedItemProperty().addListener((
                organizacionObservada,
                valorOrganizacionAnterior, nuevoValorOrganizacion) -> {
            organizacionSeleccionada = nuevoValorOrganizacion;
            tablaOrganizaciones.refresh();
        });
    }

    private void cargarOrganizaciones() {

        Utilidades utilidades = new Utilidades();

        try {

            OrganizacionVinculadaDAO organizacionDAO = new OrganizacionVinculadaDAO();
            ObservableList <OrganizacionVinculadaDTO> organizaciones =
                    FXCollections.observableArrayList(organizacionDAO.obtenerTodasLasOrganizaciones());
            tablaOrganizaciones.setItems(organizaciones);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al cargar organizaciones: " + e.getMessage());
            utilidades.mostrarAlerta("Error", "Error inesperado",
                    "No se pudo cargar la lista de organizaciones.");
        }
    }



    private void añadirBotonConsultarATable() {

        Callback<TableColumn<OrganizacionVinculadaDTO, Void>, TableCell<OrganizacionVinculadaDTO, Void>> cellFactory =
                param -> new TableCell<>() {

            private final Button botonConsultar = new Button("Consultar");

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

    private void añadirBotonEliminarATable() {

        Callback<TableColumn<OrganizacionVinculadaDTO, Void>, TableCell<OrganizacionVinculadaDTO, Void>> cellFactory =
                param -> new TableCell<>() {

            private final Button botonEliminar = new Button("Eliminar");

            {
                botonEliminar.setOnAction(event -> {
                    OrganizacionVinculadaDTO organizacion = getTableView().getItems().get(getIndex());

                    utilidades.mostrarAlertaConfirmacion(
                            "Confirmar eliminación",
                            "¿Está seguro que desea eliminar la organizacion seleccionado?",
                            "Se eliminará la organizacion seleccionada. Esta acción no se puede deshacer.",
                            () -> {
                                eliminarOrganizacion(organizacion);
                            },
                            () -> {
                                utilidades.mostrarAlerta("Cancelado",
                                        "Eliminación cancelada",
                                        "No se ha eliminado ningúna organizacion.");
                            }
                    );

                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {

                super.updateItem(item, empty);
                if (empty || getTableView().getItems().get(getIndex()) != organizacionSeleccionada) {

                    setGraphic(null);

                } else {

                    setGraphic(botonEliminar);
                }
            }
        };

        columnaBotonEliminar.setCellFactory(cellFactory);
    }

    private void abrirVentanaDetallesOrganizacion(OrganizacionVinculadaDTO organizacion) {

        Utilidades utilidades = new Utilidades();

        try {

            idOrganizacionSeleccionada = organizacion.getIdOrganizacion();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestorOrganizacionGUI.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Lista de Participantes");
            stage.show();


        } catch (IOException e){

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e){
            logger.error("Error inesperado al abrir la ventana de detalles de organización: " + e.getMessage());
            utilidades.mostrarAlerta("Error", "Error inesperado",
                    "No se pudo abrir la ventana de detalles de organización.");
        }

        cargarOrganizaciones();
    }

    private void eliminarOrganizacion(OrganizacionVinculadaDTO organizacionSeleccionada) {

        Utilidades utilidades = new Utilidades();

        try {

            OrganizacionVinculadaDAO organizacionDAO = new OrganizacionVinculadaDAO();

            organizacionDAO.tieneRepresentanteConProyectoActivo(organizacionSeleccionada.getIdOrganizacion());

            if (organizacionDAO.tieneRepresentanteConProyectoActivo(organizacionSeleccionada.getIdOrganizacion())) {
                utilidades.mostrarAlerta("Error", "No se puede eliminar la organización",
                        "La organización tiene un representante con un proyecto activo.");
                return;
            }

            organizacionDAO.eliminarOrganizacionPorID(organizacionSeleccionada.getIdOrganizacion());
            cargarOrganizaciones();
            utilidades.mostrarAlerta("Éxito", "Organización eliminada",
                    "La organización ha sido eliminada correctamente.");
        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al eliminar organización: " + e.getMessage());
            utilidades.mostrarAlerta("Error", "Error inesperado",
                    "No se pudo eliminar la organización seleccionada.");
        }

    }

    @FXML
    private void buscarOrganizacion() {

        String textoBusqueda = campoBusqueda.getText().trim();

        if (textoBusqueda.isEmpty()) {
            cargarOrganizaciones();
            return;
        }

        Utilidades utilidades = new Utilidades();

        try {
            OrganizacionVinculadaDAO organizacionDAO = new OrganizacionVinculadaDAO();
            ObservableList<OrganizacionVinculadaDTO> organizaciones =
                    FXCollections.observableArrayList(organizacionDAO.buscarOrganizacionesPorNombre(textoBusqueda));
            tablaOrganizaciones.setItems(organizaciones);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al buscar organizaciones: " + e.getMessage());
            utilidades.mostrarAlerta("Error", "Error inesperado",
                    "No se pudo buscar la organización.");
        }




    }


    @FXML
    private void abrirVentanaRegistrarOrganizacion(){

        Utilidades utilidades = new Utilidades();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegistroOrganizacionVinculadaGUI.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Registrar Organización Vinculada");
            stage.show();

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al abrir la ventana de registro de organización: " + e.getMessage());
            utilidades.mostrarAlerta("Error", "Error inesperado",
                    "No se pudo abrir la ventana de registro de organización.");
        }

        cargarOrganizaciones();

    }
}
