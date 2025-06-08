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
import logica.utilidadesproyecto.ContenedoraOrganizacionProyecto;
import logica.utilidadesproyecto.SeleccionRepresentanteOrganizacion;
import logica.interfaces.ISeleccionRepresentante;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorSeleccionRepresentanteGUI {

    private static final Logger LOGGER = LogManager.getLogger(ControladorSeleccionRepresentanteGUI.class);
    private final Utilidades UTILIDADES = new Utilidades();

    @FXML
    private TableView<ContenedoraOrganizacionProyecto> tablaRepresentantes;
    @FXML
    private TableColumn<ContenedoraOrganizacionProyecto, String> columnaRepresentante;
    @FXML
    private TableColumn<ContenedoraOrganizacionProyecto, String> columnaOrganizacion;
    @FXML
    private TextField campoBusqueda;
    @FXML
    private Button botonBuscar;
    @FXML
    private Button botonCancelarSeleccion;
    @FXML
    private Button botonSeleccionarRepresentante;

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

        try {

            RepresentanteDAO representanteDAO = new RepresentanteDAO();
            OrganizacionVinculadaDAO organizacionDAO = new OrganizacionVinculadaDAO();

            List<RepresentanteDTO> representantes = representanteDAO.obtenerTodosLosRepresentantes();
            List<OrganizacionVinculadaDTO> organizaciones = organizacionDAO.obtenerTodasLasOrganizaciones();

            ObservableList<ContenedoraOrganizacionProyecto> listaCombinada = FXCollections.observableArrayList();

            for (RepresentanteDTO representante : representantes) {

                for (OrganizacionVinculadaDTO organizacion : organizaciones) {

                    if (representante.getIdOrganizacion() == organizacion.getIdOrganizacion()) {

                        listaCombinada.add(new ContenedoraOrganizacionProyecto(
                                representante, organizacion, null));
                    }
                }
            }

            tablaRepresentantes.setItems(listaCombinada);

        } catch (SQLException e) {

            LOGGER.error("Error SQL al cargar representantes y organizaciones: " + e.getMessage(), e);
            UTILIDADES.mostrarAlerta("Error",
                    "Error al cargar los datos",
                    "No se pudo cargar la lista de representantes y organizaciones");

        } catch (IOException e) {

            LOGGER.error("Error de IO al cargar representantes y organizaciones: " + e.getMessage(), e);
            UTILIDADES.mostrarAlerta("Error",
                    "Error al cargar datos",
                    "No se pudo cargar la lista de representantes y organizaciones.");
        }
    }

    @FXML
    private void buscarRepresentante() {

        String textoBusqueda = campoBusqueda.getText().toLowerCase();

        if (textoBusqueda.isEmpty()) {

            UTILIDADES.mostrarAlerta(
                    "Error",
                    "El campo de búsqueda se encuentra vacío",
                    "Por favor, ingrese el nombre del representante o nombre de la organización a buscar"
            );
            return;
        }

        ObservableList<ContenedoraOrganizacionProyecto> listaFiltrada = FXCollections.observableArrayList();

        for (ContenedoraOrganizacionProyecto contenedor : tablaRepresentantes.getItems()) {

            String nombreCompleto = contenedor.getRepresentante().getNombre() + " " +
                    contenedor.getRepresentante().getApellidos();
            String orgNombre = contenedor.getOrganizacion().getNombre();

            if (orgNombre.toLowerCase().contains(textoBusqueda) ||
                    nombreCompleto.toLowerCase().contains(textoBusqueda)) {

                listaFiltrada.add(contenedor);
            }
        }

        tablaRepresentantes.setItems(listaFiltrada);

        if (listaFiltrada.isEmpty()) {

            UTILIDADES.mostrarAlerta(
                    "Representante u Organización no encontrados",
                    "No se ha encontrado ningún representante u organización activo con ese nombre",
                    "Por favor, verifique que haya ingresado bien el nombre o registre la organización"
            );

            cargarRepresentantesYOrganizaciones();
        }
    }

    private ISeleccionRepresentante controladorPadre;

    public void setControladorPadre(ISeleccionRepresentante controladorPadre) {

        this.controladorPadre = controladorPadre;
    }

    @FXML
    private void seleccionarRepresentante() {

        ContenedoraOrganizacionProyecto seleccion =
                tablaRepresentantes.getSelectionModel().getSelectedItem();

        if (seleccion != null) {

            SeleccionRepresentanteOrganizacion.setRepresentanteSeleccionado(seleccion.getRepresentante());
            SeleccionRepresentanteOrganizacion.setOrganizacionSeleccionada(seleccion.getOrganizacion());

            UTILIDADES.mostrarAlerta("Representante seleccionado. ",
                    "Se ha seleccionado un representante y organizacion para el proyecto",
                    "");

            if (controladorPadre != null) {

                controladorPadre.actualizarRepresentanteYOrganizacion();

            }

            Stage stageActual = (Stage) tablaRepresentantes.getScene().getWindow();
            stageActual.close();

        } else {

            LOGGER.warn("Intento de selección sin elemento seleccionado");
            UTILIDADES.mostrarAlerta("Error",
                    "Selección inválida",
                    "Por favor, seleccione un representante y una organización.");
        }
    }

    @FXML
    private void botonCancelarSeleccion() {

        Stage stageActual = (Stage) tablaRepresentantes.getScene().getWindow();
        stageActual.close();
    }
}