package GUI.gestionproyecto;

import GUI.utilidades.Utilidades;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logica.DAOs.OrganizacionVinculadaDAO;
import logica.DAOs.RepresentanteDAO;
import logica.DTOs.OrganizacionVinculadaDTO;
import logica.DTOs.RepresentanteDTO;
import logica.utilidadesproyecto.AsociacionRepresentanteOrganizacionProyecto;
import logica.utilidadesproyecto.SeleccionRepresentanteOrganizacion;
import logica.interfaces.ISeleccionRepresentante;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorSeleccionRepresentanteGUI {

    private static final Logger LOGGER =
            LogManager.getLogger(ControladorSeleccionRepresentanteGUI.class);

    @FXML
    private TableView<AsociacionRepresentanteOrganizacionProyecto> tablaRepresentantes;
    @FXML
    private TableColumn<AsociacionRepresentanteOrganizacionProyecto, String> columnaRepresentante;
    @FXML
    private TableColumn<AsociacionRepresentanteOrganizacionProyecto, String> columnaOrganizacion;
    @FXML
    private TextField campoBusqueda;
    @FXML
    private Button botonBuscar;
    @FXML
    private Button botonCancelarSeleccion;
    @FXML
    private Button botonSeleccionarRepresentante;

    private Utilidades utilidades = new Utilidades();

    @FXML
    private void initialize() {

        botonBuscar.setCursor(Cursor.HAND);
        botonCancelarSeleccion.setCursor(Cursor.HAND);
        botonSeleccionarRepresentante.setCursor(Cursor.HAND);

        columnaRepresentante.setCellValueFactory(cellData -> {

            return new SimpleStringProperty(cellData.getValue().getRepresentante().getNombre() + " " +
                    cellData.getValue().getRepresentante().getApellidos());

        });

        columnaOrganizacion.setCellValueFactory(cellData -> {

            LOGGER.debug("Configurando columna organización para: " + cellData.getValue().getOrganizacion());
            return new SimpleStringProperty(cellData.getValue().getOrganizacion().getNombre());

        });

        tablaRepresentantes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        cargarRepresentantesYOrganizaciones();
    }

    private void cargarRepresentantesYOrganizaciones() {

        RepresentanteDAO representanteDAO = new RepresentanteDAO();
        OrganizacionVinculadaDAO organizacionDAO = new OrganizacionVinculadaDAO();

        try {

            List<RepresentanteDTO> representantes = representanteDAO.obtenerTodosLosRepresentantes();
            List<OrganizacionVinculadaDTO> organizaciones = organizacionDAO.obtenerTodasLasOrganizaciones();

            ObservableList<AsociacionRepresentanteOrganizacionProyecto> listaRepresentantesYOrganizaciones =
                    FXCollections.observableArrayList();

            for (RepresentanteDTO representante : representantes) {

                for (OrganizacionVinculadaDTO organizacion : organizaciones) {

                    if (representante.getIdOrganizacion() == organizacion.getIdOrganizacion()) {

                        listaRepresentantesYOrganizaciones.add(new AsociacionRepresentanteOrganizacionProyecto(
                                representante, organizacion, null));
                    }
                }
            }

            tablaRepresentantes.setItems(listaRepresentantesYOrganizaciones);

        } catch (SQLException e) {

            String estadoSQL = e.getSQLState();

            switch (estadoSQL) {

                case "08S01":

                    LOGGER.error("El servicio de SQL se encuentra desactivado: " + e);
                    utilidades.mostrarAlerta(
                            "Error de conexión",
                            "No se pudo establecer una conexión con la base de datos.",
                            "La conexión con la base de datos se encuentra interrumpida."
                    );
                    break;

                case "42000":

                    LOGGER.error("La base de datos no existe: " + e);
                    utilidades.mostrarAlerta(
                            "Error de conexión",
                            "No se pudo establecer conexión con la base de datos.",
                            "La base de datos actualmente no existe."
                    );
                    break;

                case "28000":

                    LOGGER.error("Credenciales invalidas para el acceso: " + e);
                    utilidades.mostrarAlerta(
                            "Credenciales inválidas",
                            "Usuario o contraseña incorrectos.",
                            "Por favor, verifique los datos de acceso a la base" +
                                    "de datos"
                    );
                    break;

                default:

                    LOGGER.error("Error de SQL no manejado: " + estadoSQL + "-" + e);
                    utilidades.mostrarAlerta(
                            "Error del sistema.",
                            "Se produjo un error al acceder a la base de datos.",
                            "Por favor, contacte al soporte técnico."
                    );
                    break;
            }

        } catch (IOException e) {

            LOGGER.error("Error de IOException: " + e);
            utilidades.mostrarAlerta(
                    "Error interno del sistema",
                    "No se pudo completar la operación.",
                    "Ocurrió un error dentro del sistema, por favor inténtelo de nuevo más tarde " +
                            "o contacte al administrador."
            );
        }
    }

    @FXML
    private void filtrarRepresentantesPorBusqueda() {

        String criterioBusqueda = campoBusqueda.getText().toLowerCase();

        if (criterioBusqueda.isEmpty()) {

            utilidades.mostrarAlerta(
                    "Error de búsqueda",
                    "El campo de búsqueda se encuentra vacío",
                    "Por favor, ingrese el nombre de la organización a buscar"
            );
            return;
        }

        ObservableList<AsociacionRepresentanteOrganizacionProyecto> listaFiltrada =
                FXCollections.observableArrayList();

        for (AsociacionRepresentanteOrganizacionProyecto asociacion : tablaRepresentantes.getItems()) {

            String nombreRepresentante = asociacion.getRepresentante().getNombre() + " " +
                    asociacion.getRepresentante().getApellidos();
            String nombreOrganizacion = asociacion.getOrganizacion().getNombre();

            if (nombreOrganizacion.toLowerCase().contains(criterioBusqueda) ||
                    nombreRepresentante.toLowerCase().contains(criterioBusqueda)) {

                listaFiltrada.add(asociacion);
            }
        }

        tablaRepresentantes.setItems(listaFiltrada);

        if (listaFiltrada.isEmpty()) {

            utilidades.mostrarAlerta(
                    "Representante u Organización no encontrados",
                    "No se ha encontrado ningún representante u organización activo con ese nombre",
                    "Por favor, verifique que haya ingresado bien el nombre o registre la organización"
            );

            cargarRepresentantesYOrganizaciones();
        }
    }

    @FXML
    private void confirmarSeleccionRepresentante() {

        AsociacionRepresentanteOrganizacionProyecto asociacionSeleccionada =
                tablaRepresentantes.getSelectionModel().getSelectedItem();

        if (asociacionSeleccionada != null) {

            SeleccionRepresentanteOrganizacion.setRepresentanteSeleccionado(asociacionSeleccionada.getRepresentante());
            SeleccionRepresentanteOrganizacion.setOrganizacionSeleccionada(asociacionSeleccionada.getOrganizacion());

            utilidades.mostrarAlerta(
                    "Representante seleccionado. ",
                    "Se ha seleccionado un representante y organizacion para el proyecto",
                    "");

            if (controladorPadre != null) {

                controladorPadre.actualizarRepresentanteYOrganizacion();

            }

            Stage stageActual = (Stage) tablaRepresentantes.getScene().getWindow();
            stageActual.close();

        } else {

            LOGGER.warn("Intento de selección sin elemento seleccionado");
            utilidades.mostrarAlerta("Error",
                    "Selección inválida",
                    "Por favor, seleccione un representante y una organización.");
        }
    }

    private ISeleccionRepresentante controladorPadre;

    public void setControladorPadre(ISeleccionRepresentante controladorPadre) {

        this.controladorPadre = controladorPadre;
    }

    @FXML
    private void botonCancelarSeleccion() {

        Stage stageActual = (Stage) tablaRepresentantes.getScene().getWindow();
        stageActual.close();
    }
}