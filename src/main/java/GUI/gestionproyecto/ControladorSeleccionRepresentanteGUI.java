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
import logica.utilidadesproyecto.ContenedoraRepresentanteOrganizacionProyecto;
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

    @FXML private TableView <ContenedoraRepresentanteOrganizacionProyecto> tablaRepresentantes;
    @FXML private TableColumn<ContenedoraRepresentanteOrganizacionProyecto, String> columnaRepresentante;
    @FXML private TableColumn<ContenedoraRepresentanteOrganizacionProyecto, String> columnaOrganizacion;
    @FXML private TextField campoBusqueda;
    @FXML private Button botonBuscar;
    @FXML private Button botonCancelarSeleccion;
    @FXML private Button botonSeleccionarRepresentante;

    @FXML
    private void initialize() {
        LOGGER.info("Inicializando ControladorSeleccionRepresentanteGUI");

        botonBuscar.setCursor(Cursor.HAND);
        botonCancelarSeleccion.setCursor(Cursor.HAND);
        botonSeleccionarRepresentante.setCursor(Cursor.HAND);

        columnaRepresentante.setCellValueFactory(cellData -> {
            LOGGER.debug("Configurando columna representante para: " + cellData.getValue().getRepresentante());
            return new SimpleStringProperty(cellData.getValue().getRepresentante().getNombre() + " " +
                    cellData.getValue().getRepresentante().getApellidos());
        });

        columnaOrganizacion.setCellValueFactory(cellData -> {
            LOGGER.debug("Configurando columna organización para: " + cellData.getValue().getOrganizacion());
            return new SimpleStringProperty(cellData.getValue().getOrganizacion().getNombre());
        });

        tablaRepresentantes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        LOGGER.info("Modo de selección configurado a SINGLE");

        cargarRepresentantesYOrganizaciones();
    }

    private void cargarRepresentantesYOrganizaciones() {
        LOGGER.info("Cargando representantes y organizaciones...");

        try {
            RepresentanteDAO representanteDAO = new RepresentanteDAO();
            OrganizacionVinculadaDAO organizacionDAO = new OrganizacionVinculadaDAO();

            LOGGER.debug("Obteniendo lista de representantes...");
            List<RepresentanteDTO> representantes = representanteDAO.obtenerTodosLosRepresentantes();
            LOGGER.debug("Obteniendo lista de organizaciones...");
            List<OrganizacionVinculadaDTO> organizaciones = organizacionDAO.obtenerTodasLasOrganizaciones();

            LOGGER.info("Encontrados " + representantes.size() + " representantes y " +
                    organizaciones.size() + " organizaciones");

            ObservableList<ContenedoraRepresentanteOrganizacionProyecto> listaCombinada = FXCollections.observableArrayList();
            LOGGER.debug("Creando lista combinada de representantes y organizaciones...");

            for (RepresentanteDTO representante : representantes) {
                for (OrganizacionVinculadaDTO organizacion : organizaciones) {
                    if (representante.getIdOrganizacion() == organizacion.getIdOrganizacion()) {
                        LOGGER.trace("Emparejando representante " + representante.getNombre() +
                                " con organización " + organizacion.getNombre());
                        listaCombinada.add(new ContenedoraRepresentanteOrganizacionProyecto(
                                representante, organizacion, null));
                    }
                }
            }

            LOGGER.info("Total de emparejamientos encontrados: " + listaCombinada.size());
            tablaRepresentantes.setItems(listaCombinada);
            LOGGER.info("Tabla de representantes actualizada");

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
        LOGGER.info("Iniciando búsqueda con texto: " + textoBusqueda);

        if (textoBusqueda.isEmpty()) {
            LOGGER.warn("Intento de búsqueda con campo vacío");
            UTILIDADES.mostrarAlerta(
                    "Error",
                    "El campo de búsqueda se encuentra vacío",
                    "Por favor, ingrese el nombre del representante o nombre de la organización a buscar"
            );
            return;
        }

        ObservableList<ContenedoraRepresentanteOrganizacionProyecto> listaFiltrada = FXCollections.observableArrayList();
        LOGGER.debug("Filtrando representantes/organizaciones...");

        for (ContenedoraRepresentanteOrganizacionProyecto contenedor : tablaRepresentantes.getItems()) {
            String nombreCompleto = contenedor.getRepresentante().getNombre() + " " +
                    contenedor.getRepresentante().getApellidos();
            String orgNombre = contenedor.getOrganizacion().getNombre();

            if (orgNombre.toLowerCase().contains(textoBusqueda) ||
                    nombreCompleto.toLowerCase().contains(textoBusqueda)) {
                LOGGER.trace("Coincidencia encontrada: " + nombreCompleto + " - " + orgNombre);
                listaFiltrada.add(contenedor);
            }
        }

        LOGGER.info("Resultados de búsqueda: " + listaFiltrada.size() + " coincidencias");
        tablaRepresentantes.setItems(listaFiltrada);

        if (listaFiltrada.isEmpty()) {
            LOGGER.info("No se encontraron coincidencias para: " + textoBusqueda);
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
        LOGGER.info("Estableciendo controlador padre: " + controladorPadre.getClass().getSimpleName());
        this.controladorPadre = controladorPadre;
    }

    @FXML
    private void seleccionarRepresentante() {
        LOGGER.info("Intentando seleccionar representante...");

        ContenedoraRepresentanteOrganizacionProyecto seleccion =
                tablaRepresentantes.getSelectionModel().getSelectedItem();

        if (seleccion != null) {
            LOGGER.info("Representante seleccionado: " +
                    seleccion.getRepresentante().getNombre() + " " +
                    seleccion.getRepresentante().getApellidos());
            LOGGER.info("Organización seleccionada: " +
                    seleccion.getOrganizacion().getNombre());

            // Almacenar selección en clase estática
            SeleccionRepresentanteOrganizacion.setRepresentanteSeleccionado(seleccion.getRepresentante());
            SeleccionRepresentanteOrganizacion.setOrganizacionSeleccionada(seleccion.getOrganizacion());
            LOGGER.debug("Selección almacenada en SeleccionRepresentanteOrganizacion");

            UTILIDADES.mostrarAlerta("Representante seleccionado. ",
                    "Se ha seleccionado un representante y organizacion para el proyecto",
                    "");

            if (controladorPadre != null) {
                LOGGER.debug("Notificando al controlador padre...");
                controladorPadre.actualizarRepresentanteYOrganizacion();
            } else {
                LOGGER.warn("Controlador padre es nulo - no se puede notificar");
            }

            Stage stageActual = (Stage) tablaRepresentantes.getScene().getWindow();
            LOGGER.info("Cerrando ventana de selección...");
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
        LOGGER.info("Cancelando selección de representante...");
        Stage stageActual = (Stage) tablaRepresentantes.getScene().getWindow();
        stageActual.close();
    }
}