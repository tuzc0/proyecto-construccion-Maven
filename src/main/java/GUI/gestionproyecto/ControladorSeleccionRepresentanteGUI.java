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
import logica.ManejadorExcepciones;
import logica.interfaces.IGestorAlertas;
import logica.utilidadesproyecto.AsociacionProyecto;
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
    private TableView<AsociacionProyecto> tablaRepresentantes;
    @FXML
    private TableColumn<AsociacionProyecto, String> columnaRepresentante;
    @FXML
    private TableColumn<AsociacionProyecto, String> columnaOrganizacion;
    @FXML
    private TextField campoBusqueda;
    @FXML
    private Button botonBuscar;
    @FXML
    private Button botonCancelarSeleccion;
    @FXML
    private Button botonSeleccionarRepresentante;


    private Utilidades gestorVentanas = new Utilidades();

    private IGestorAlertas utilidades = new Utilidades();

    private ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, LOGGER);

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

            ObservableList<AsociacionProyecto> listaRepresentantesYOrganizaciones =
                    FXCollections.observableArrayList();

            for (RepresentanteDTO representante : representantes) {

                for (OrganizacionVinculadaDTO organizacion : organizaciones) {

                    if (representante.getIdOrganizacion() == organizacion.getIdOrganizacion()) {

                        listaRepresentantesYOrganizaciones.add(new AsociacionProyecto(
                                representante, organizacion, null));
                    }
                }
            }

            tablaRepresentantes.setItems(listaRepresentantesYOrganizaciones);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            LOGGER.error("Error al cargar representantes y organizaciones: " + e.getMessage(), e);
            utilidades.mostrarAlerta(
                    "Error del sistema",
                    "No se pudieron cargar los representantes y organizaciones",
                    "Contacte al administrador"
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

        ObservableList<AsociacionProyecto> listaFiltrada =
                FXCollections.observableArrayList();

        for (AsociacionProyecto asociacion : tablaRepresentantes.getItems()) {

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

        AsociacionProyecto asociacionSeleccionada =
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