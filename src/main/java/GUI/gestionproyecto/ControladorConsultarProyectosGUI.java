package GUI.gestionproyecto;

import GUI.utilidades.Utilidades;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import logica.utilidadesproyecto.AsociacionProyecto;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ControladorConsultarProyectosGUI {

    private static final Logger LOGGER = Logger
            .getLogger(ControladorConsultarProyectosGUI.class.getName());

    @FXML
    private TableView<AsociacionProyecto>
            tablaProyectos;
    @FXML
    private TableColumn<AsociacionProyecto, String>
            columnaNombreProyecto;
    @FXML
    private TableColumn<AsociacionProyecto, String>
            columnaNombreRepresentante;
    @FXML
    private TableColumn<AsociacionProyecto, Void>
            columnaVerDetalles;
    @FXML
    private TableColumn<AsociacionProyecto, Void>
            columnaEliminarProyecto;
    @FXML
    private TextField campoBusqueda;
    @FXML
    private Button botonRegistrar;

    private Utilidades utilidades = new Utilidades();
    private ProyectoDTO proyectoSeleccionado;

    @FXML
    public void initialize() {

        columnaNombreProyecto.setCellValueFactory(cellData ->

                new SimpleStringProperty(cellData.getValue().getProyecto().getNombre())
        );

        columnaNombreRepresentante.setCellValueFactory(cellData ->

                new SimpleStringProperty(cellData.getValue().getRepresentante().getNombre() + " " +
                                cellData.getValue().getRepresentante().getApellidos()
                )
        );

        tablaProyectos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tablaProyectos.getSelectionModel().selectedItemProperty()
                .addListener((proyectoObservado,
                              proyectoAnterior, nuevoProyecto) -> {

                    if (nuevoProyecto != null) {

                        proyectoSeleccionado = nuevoProyecto.getProyecto();

                    } else {

                        proyectoSeleccionado = null;

                    }

                    tablaProyectos.refresh();
                });

        cargarRepresentanteYProyecto();
        añadirBotonEliminarATabla();
        añadirBotonVerDetallesATabla();

        botonRegistrar.setCursor(Cursor.HAND);
    }

    private void añadirBotonVerDetallesATabla() {

        Callback<TableColumn<AsociacionProyecto, Void>,
                TableCell<AsociacionProyecto, Void>>
                fabricadorCeldas = columnaProyecto -> new TableCell<>() {

            private final Button botonDetalles = new Button("Ver detalles");
            {
                botonDetalles.setOnAction(evento -> {

                    AsociacionProyecto asociacionSeleccionada =
                            getTableView().getItems().get(getIndex());
                    ProyectoDTO proyectoSeleccionado = asociacionSeleccionada.getProyecto();
                    abrirVentanaDetallesProyecto(proyectoSeleccionado);
                });
            }

            @Override
            protected void updateItem(Void valorCelda, boolean vacio) {

                super.updateItem(valorCelda, vacio);

                if (vacio || tablaProyectos.getSelectionModel().getSelectedItem() == null ||
                        !tablaProyectos.getSelectionModel().getSelectedItem()
                                .equals(getTableView().getItems().get(getIndex()))) {

                    setGraphic(null);

                } else {

                    setGraphic(botonDetalles);
                }
            }
        };

        columnaVerDetalles.setCellFactory(fabricadorCeldas);
    }

    private void añadirBotonEliminarATabla() {

        Callback<TableColumn<AsociacionProyecto, Void>,
                TableCell<AsociacionProyecto, Void>>
                fabricadorCeldas = columna -> new TableCell<>() {

            private final Button botonEliminar = new Button("Eliminar");
            {
                botonEliminar.setOnAction(evento -> {
                    AsociacionProyecto asociacionSeleccionada =
                            getTableView().getItems().get(getIndex());
                    ProyectoDTO proyectoAEliminar = asociacionSeleccionada.getProyecto();
                    confirmarEliminacion(proyectoAEliminar);
                });
            }

            @Override
            protected void updateItem(Void valorCelda, boolean vacio) {

                super.updateItem(valorCelda, vacio);

                if (vacio || tablaProyectos.getSelectionModel().getSelectedItem() == null || !tablaProyectos
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

        columnaEliminarProyecto.setCellFactory(fabricadorCeldas);
    }

    private void abrirVentanaDetallesProyecto(ProyectoDTO proyectoSeleccionado) {

        try {

            FXMLLoader cargarFXML = new FXMLLoader(getClass().getResource("/GestorProyectoGUI.fxml"));
            Parent contenidoVentana = cargarFXML.load();

            ControladorGestorProyectoGUI controladorGestorProyectoGUI = cargarFXML.getController();
            controladorGestorProyectoGUI.setProyectoDTO(proyectoSeleccionado);
            controladorGestorProyectoGUI.setControladorPadre(this);

            Stage ventanaDetallesProyecto = new Stage();
            ventanaDetallesProyecto.setScene(new Scene(contenidoVentana));
            ventanaDetallesProyecto.setTitle("Detalles Proyecto");
            ventanaDetallesProyecto.show();

        } catch (IOException e) {

            LOGGER.severe("Error de E/S al abrir la ventana: " + e);

            utilidades.mostrarAlerta(
                    "Error de archivo",
                    "No se pudo cargar la interfaz del proyecto.",
                    "Verifique que el archivo FXML exista y sea válido.");
        }
    }

    public void cargarRepresentanteYProyecto() {

        ProyectoDAO proyectoDAO = new ProyectoDAO();
        RepresentanteDAO representanteDAO = new RepresentanteDAO();

        try {

            List<ProyectoDTO> proyectos = proyectoDAO.listarProyectos();
            List<RepresentanteDTO> representantes = representanteDAO.obtenerTodosLosRepresentantes();

            ObservableList<AsociacionProyecto> listaRepresentantesProyectos =
                    FXCollections.observableArrayList();

            for (ProyectoDTO proyectoDTO : proyectos) {

                for (RepresentanteDTO representanteDTO : representantes) {

                    if (proyectoDTO.getIdRepresentante() == representanteDTO.getIDRepresentante()) {

                        listaRepresentantesProyectos.add(
                                new AsociacionProyecto(
                                        representanteDTO,
                                        null,
                                        proyectoDTO
                                )
                        );
                    }
                }
            }

            tablaProyectos.setItems(listaRepresentantesProyectos);

        } catch (SQLException e) {

            String estadoSQL = e.getSQLState();

            switch (estadoSQL) {

                case "08S01":

                    LOGGER.severe("El servicio de SQL se encuentra desactivado: " + e);
                    utilidades.mostrarAlerta(
                            "Error de conexión",
                            "No se pudo establecer una conexión con la base de datos.",
                            "La conexión con la base de datos se encuentra interrumpida."
                    );
                    break;

                case "42000":

                    LOGGER.severe("La base de datos no existe: " + e);
                    utilidades.mostrarAlerta(
                            "Error de conexión",
                            "No se pudo establecer conexión con la base de datos.",
                            "La base de datos actualmente no existe."
                    );
                    break;

                case "28000":

                    LOGGER.severe("Credenciales invalidas para el acceso: " + e);
                    utilidades.mostrarAlerta(
                            "Credenciales inválidas",
                            "Usuario o contraseña incorrectos.",
                            "Por favor, verifique los datos de acceso a la base" +
                                    "de datos"
                    );
                    break;

                default:

                    LOGGER.severe("Error de SQL no manejado: " + estadoSQL + "-" + e);
                    utilidades.mostrarAlerta(
                            "Error del sistema.",
                            "Se produjo un error al acceder a la base de datos.",
                            "Por favor, contacte al soporte técnico."
                    );
                    break;
            }

        } catch (IOException e) {

            LOGGER.severe("Error de IOException: " + e);
            utilidades.mostrarAlerta(
                    "Error interno del sistema",
                    "No se pudo completar la carga de proyectos.",
                    "Ocurrió un error dentro del sistema, por favor inténtelo de nuevo más tarde " +
                            "o contacte al administrador."
            );

        } catch (Exception e) {

            LOGGER.severe("Error inesperado: " + e);
            utilidades.mostrarAlerta(
                    "Error interno del sistema",
                    "Ocurrió un error al completar la carga de proyectos.",
                    "Ocurrió un error dentro del sistema, por favor inténtelo de nuevo más tarde " +
                            "o contacte al administrador."
            );
        }
    }

    @FXML
    private void abrirVentanaRegistroProyecto() {

        utilidades.abrirVentana(
                "/RegistroProyectoGUI.fxml",
                "Registrar Proyecto",
                (Stage) botonRegistrar.getScene().getWindow()
        );

        cargarRepresentanteYProyecto();
    }

    @FXML
    private void filtrarProyectosPorNombre() {

        String criterioBusqueda = campoBusqueda.getText().toLowerCase();

        if (criterioBusqueda.isEmpty()) {

            utilidades.mostrarAlerta(
                    "Error",
                    "El campo de busqueda se encuentra vacio",
                    "Por favor, ingrese el nombre del proyecto a buscar"
            );
            cargarRepresentanteYProyecto();
            return;
        }

        ObservableList<AsociacionProyecto> proyectosFiltrados =
                FXCollections.observableArrayList();

        for (AsociacionProyecto asociacionProyecto : tablaProyectos.getItems()) {

            String nombreProyecto = asociacionProyecto.getProyecto().getNombre().toLowerCase();
            String nombreRepresentante = asociacionProyecto.getRepresentante().getNombre().toLowerCase();

            if (nombreProyecto.contains(criterioBusqueda) || nombreRepresentante.contains(criterioBusqueda)) {

                proyectosFiltrados.add(asociacionProyecto);

            }
        }

        if (proyectosFiltrados.isEmpty()) {

            utilidades.mostrarAlerta(
                    "Proyecto no encontrado",
                    "No se ha encontrado ningún proyecto activo con ese nombre",
                    "Por favor, verifique que haya ingresado bien el nombre o registre el proyecto"
            );
            cargarRepresentanteYProyecto();
            return;
        }

        tablaProyectos.setItems(proyectosFiltrados);
    }

    private void confirmarEliminacion(ProyectoDTO proyectoAEliminar) {

        ProyectoDAO proyectoDAO = new ProyectoDAO();

        utilidades.mostrarAlertaConfirmacion(
                "Confirmar eliminación",
                "¿Está seguro que desea eliminar el proyecto seleccionado?",
                "",
                () -> {

                    try {

                        if (!proyectoDAO.eliminarProyectoPorID(proyectoAEliminar.getIdProyecto())) {

                            utilidades.mostrarAlerta(
                                    "Error",
                                    "No fue posible eliminar el proyecto seleccionado.",
                                    "Inténtelo más tarde o contacte al administrador."
                            );

                        } else {

                            utilidades.mostrarAlerta(
                                    "Éxito",
                                    "El proyecto ha sido eliminado correctamente.",
                                    ""
                            );
                        }

                        cargarRepresentanteYProyecto();

                    } catch (SQLException e) {

                        String estadoSQL = e.getSQLState();

                        switch (estadoSQL) {

                            case "08S01":

                                LOGGER.severe("El servicio de SQL se encuentra desactivado: " + e);
                                utilidades.mostrarAlerta(
                                        "Error de conexión",
                                        "No se pudo establecer una conexión con la base de datos.",
                                        "La conexión con la base de datos se encuentra interrumpida."
                                );
                                break;

                            case "42000":

                                LOGGER.severe("La base de datos no existe: " + e);
                                utilidades.mostrarAlerta(
                                        "Error de conexión",
                                        "No se pudo establecer conexión con la base de datos.",
                                        "La base de datos actualmente no existe."
                                );
                                break;

                            case "28000":

                                LOGGER.severe("Credenciales invalidas para el acceso: " + e);
                                utilidades.mostrarAlerta(
                                        "Credenciales inválidas",
                                        "Usuario o contraseña incorrectos.",
                                        "Por favor, verifique los datos de acceso a la base" +
                                                "de datos"
                                );
                                break;

                            default:

                                LOGGER.severe("Error de SQL no manejado: " + estadoSQL + "-" + e);
                                utilidades.mostrarAlerta(
                                        "Error del sistema.",
                                        "Se produjo un error al acceder a la base de datos.",
                                        "Por favor, contacte al soporte técnico."
                                );
                                break;
                        }

                    } catch (IOException e) {

                        LOGGER.severe("Error de IOException: " + e);
                        utilidades.mostrarAlerta(
                                "Error interno del sistema",
                                "No se pudo completar la carga de proyectos.",
                                "Ocurrió un error dentro del sistema, por favor inténtelo de nuevo más tarde " +
                                        "o contacte al administrador."
                        );
                    }
                },
                () -> {

                    tablaProyectos.getSelectionModel().clearSelection();
                    tablaProyectos.getItems().stream()
                            .filter(asociacionProyecto ->
                                    asociacionProyecto.getProyecto().equals(proyectoAEliminar))
                            .findFirst()
                            .ifPresent(asociacionProyecto ->
                                    tablaProyectos.getSelectionModel().select(asociacionProyecto)
                            );

                    utilidades.mostrarAlerta(
                            "Operación cancelada",
                            "La eliminación fue cancelada",
                            "El proyecto no ha sido eliminado."
                    );
                }
        );
    }
}
