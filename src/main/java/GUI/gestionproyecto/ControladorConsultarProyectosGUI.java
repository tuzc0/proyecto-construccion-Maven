package GUI.gestionproyecto;

import GUI.utilidades.Utilidades;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import logica.DAOs.ProyectoDAO;
import logica.DAOs.RepresentanteDAO;
import logica.DTOs.ProyectoDTO;
import logica.DTOs.RepresentanteDTO;
import logica.utilidadesproyecto.ContenedoraRepresentanteOrganizacionProyecto;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ControladorConsultarProyectosGUI {

    private static final Logger LOGGER = Logger
            .getLogger(ControladorConsultarProyectosGUI.class.getName());

    @FXML
    private TableView<ContenedoraRepresentanteOrganizacionProyecto>
            tablaProyectos;
    @FXML
    private TableColumn<ContenedoraRepresentanteOrganizacionProyecto, String>
            columnaNombreProyecto;
    @FXML
    private TableColumn<ContenedoraRepresentanteOrganizacionProyecto, String>
            columnaNombreRepresentante;
    @FXML
    private TableColumn<ContenedoraRepresentanteOrganizacionProyecto, Void>
            columnaVerDetalles;
    @FXML
    private TableColumn<ContenedoraRepresentanteOrganizacionProyecto, Void>
            columnaEliminarProyecto;
    @FXML
    private TextField campoBusqueda;
    @FXML
    private Button botonRegistrar;
    @FXML
    private Button botonRegresar;

    private final Utilidades UTILIDADES = new Utilidades();
    private ProyectoDTO proyectoSeleccionado;

    @FXML
    public void initialize() {

        columnaNombreProyecto.setCellValueFactory(cellData ->

                new SimpleStringProperty(
                        cellData.getValue()
                                .getProyecto()
                                .getNombre()
                )
        );

        columnaNombreRepresentante.setCellValueFactory(cellData ->

                new SimpleStringProperty(
                        cellData.getValue()
                                .getRepresentante()
                                .getNombre() + " " +
                                cellData.getValue()
                                        .getRepresentante()
                                        .getApellidos()
                )
        );

        tablaProyectos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tablaProyectos
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((valorObservado,
                              valorAnterior, nuevoValor) -> {

                    if (nuevoValor != null) {

                        proyectoSeleccionado = nuevoValor.getProyecto();

                    } else {

                        proyectoSeleccionado = null;

                    }

                    tablaProyectos.refresh();
                });

        cargarProyectoYOrganizacion();
        añadirBotonEliminarATabla();
        añadirBotonVerDetallesATabla();

        botonRegistrar.setCursor(Cursor.HAND);
        botonRegresar.setCursor(Cursor.HAND);
    }

    private void añadirBotonVerDetallesATabla() {

        Callback<TableColumn<ContenedoraRepresentanteOrganizacionProyecto, Void>,
                TableCell<ContenedoraRepresentanteOrganizacionProyecto, Void>>
                cellFactory = param -> new TableCell<>() {

            private final Button botonVerDetalles = new Button("Ver detalles");
            {
                botonVerDetalles.setOnAction(evento -> {
                    ContenedoraRepresentanteOrganizacionProyecto contenedor =
                            getTableView().getItems().get(getIndex());
                    ProyectoDTO proyectoDTO = contenedor.getProyecto();

                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {

                super.updateItem(item, empty);

                if (empty || tablaProyectos.getSelectionModel().getSelectedItem() == null || !tablaProyectos
                        .getSelectionModel()
                        .getSelectedItem()
                        .equals(getTableView().getItems().get(getIndex()))
                ) {

                    setGraphic(null);

                } else {

                    setGraphic(botonVerDetalles);
                }
            }
        };

        columnaVerDetalles.setCellFactory(cellFactory);
    }

    private void añadirBotonEliminarATabla() {

        Callback<TableColumn<ContenedoraRepresentanteOrganizacionProyecto, Void>,
                TableCell<ContenedoraRepresentanteOrganizacionProyecto, Void>>
                cellFactory = param -> new TableCell<>() {

            private final Button botonEliminar = new Button("Eliminar");
            {
                botonEliminar.setOnAction(evento -> {
                    ContenedoraRepresentanteOrganizacionProyecto contenedor =
                            getTableView().getItems().get(getIndex());
                    ProyectoDTO proyectoDTO = contenedor.getProyecto();
                    confirmarEliminacion(proyectoDTO);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {

                super.updateItem(item, empty);

                if (empty || tablaProyectos.getSelectionModel().getSelectedItem() == null || !tablaProyectos
                        .getSelectionModel()
                        .getSelectedItem()
                        .equals(getTableView().getItems().get(getIndex()))
                ) {

                    setGraphic(null);

                } else {

                    setGraphic(botonEliminar);
                }
            }
        };

        columnaEliminarProyecto.setCellFactory(cellFactory);
    }

    private void cargarProyectoYOrganizacion() {

        ProyectoDAO proyectoDAO = new ProyectoDAO();
        RepresentanteDAO representanteDAO = new RepresentanteDAO();

        try {

            List<ProyectoDTO> proyectos = proyectoDAO.listarProyectos();
            List<RepresentanteDTO> representantes = representanteDAO.obtenerTodosLosRepresentantes();

            ObservableList<ContenedoraRepresentanteOrganizacionProyecto> listaCombinada =
                    FXCollections.observableArrayList();

            for (ProyectoDTO proyecto : proyectos) {

                for (RepresentanteDTO representante : representantes) {

                    if (proyecto.getIdRepresentante() == representante.getIDRepresentante()) {

                        listaCombinada.add(

                                new ContenedoraRepresentanteOrganizacionProyecto(
                                        representante,
                                        null,
                                        proyecto
                                )
                        );
                    }
                }
            }

            tablaProyectos.setItems(listaCombinada);

        } catch (IOException | SQLException e) {

            LOGGER.severe("Error al cargar representantes y organizaciones: " + e);
            UTILIDADES.mostrarAlerta(
                    "Error",
                    "Error al cargar datos",
                    "No se pudo cargar la lista de representantes y organizaciones."
            );
        }
    }

    @FXML
    private void abrirVentanaRegistroProyecto() {

        UTILIDADES.abrirVentana(
                "/RegistroProyectoGUI.fxml",
                "Registrar Proyecto",
                (Stage) botonRegistrar.getScene().getWindow()
        );

        cargarProyectoYOrganizacion();
    }

    @FXML
    private void buscarProyecto() {

        String textoBusqueda = campoBusqueda.getText().toLowerCase();

        if (textoBusqueda.isEmpty()) {

            UTILIDADES.mostrarAlerta(
                    "Error",
                    "El campo de busqueda se encuentra vacio",
                    "Por favor, ingrese el nombre del proyecto a buscar"
            );
            return;
        }

        ObservableList<ContenedoraRepresentanteOrganizacionProyecto> listaFiltrada =
                FXCollections.observableArrayList();

        for (ContenedoraRepresentanteOrganizacionProyecto contenedor : tablaProyectos.getItems()) {

            String nombreProyecto = contenedor.getProyecto().getNombre().toLowerCase();
            String nombreRepresentante = contenedor.getRepresentante().getNombre().toLowerCase();

            if (nombreProyecto.contains(textoBusqueda) || nombreRepresentante.contains(textoBusqueda)) {

                listaFiltrada.add(contenedor);

            }
        }

        if (listaFiltrada.isEmpty()) {
            UTILIDADES.mostrarAlerta(
                    "Proyecto no encontrado",
                    "No se ha encontrado ningun proyecto activo con ese nombre",
                    "Por favor, verifique que haya ingresado bien el nombre o registre el proyecto"
            );
            return;
        }

        tablaProyectos.setItems(listaFiltrada);
    }


    private void confirmarEliminacion(ProyectoDTO proyectoAEliminar) {

        ProyectoDAO proyectoDAO = new ProyectoDAO();

        UTILIDADES.mostrarAlertaConfirmacion(
                "Confirmar eliminación",
                "¿Está seguro que desea eliminar el proyecto seleccionado?",
                "",
                () -> {

                    try {

                        if (!proyectoDAO.eliminarProyectoPorID(proyectoAEliminar.getIdProyecto())) {

                            UTILIDADES.mostrarAlerta(
                                    "Error",
                                    "No fue posible eliminar el proyecto seleccionado.",
                                    "Inténtelo más tarde o contacte al administrador."
                            );

                        } else {

                            UTILIDADES.mostrarAlerta(
                                    "Éxito",
                                    "El proyecto ha sido eliminado correctamente.",
                                    ""
                            );
                        }

                        cargarProyectoYOrganizacion();

                    } catch (IOException | SQLException e) {

                        LOGGER.severe("Error al eliminar proyecto: " + e);
                        UTILIDADES.mostrarAlerta(
                                "Error",
                                "Error al eliminar",
                                "Ocurrió un error dentro del sistema, por favor intente de nuevo más tarde."
                        );
                    }
                },
                () -> {

                    tablaProyectos.getSelectionModel().clearSelection();
                    tablaProyectos.getItems().stream().filter(item ->
                                    item.getProyecto().equals(proyectoAEliminar)
                            )
                            .findFirst()
                            .ifPresent(item ->
                                    tablaProyectos
                                            .getSelectionModel()
                                            .select(item)
                            );

                    UTILIDADES.mostrarAlerta(
                            "Operación cancelada",
                            "La eliminación fue cancelada",
                            "El proyecto no ha sido eliminado."
                    );
                }
        );
    }
}
