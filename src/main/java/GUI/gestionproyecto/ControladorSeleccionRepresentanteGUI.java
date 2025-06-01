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

        botonBuscar.setCursor(Cursor.HAND);
        botonCancelarSeleccion.setCursor(Cursor.HAND);
        botonSeleccionarRepresentante.setCursor(Cursor.HAND);

        columnaRepresentante.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRepresentante().getNombre() + " " +
                        cellData.getValue().getRepresentante().getApellidos()));
        columnaOrganizacion.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getOrganizacion().getNombre()));

        tablaRepresentantes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        cargarRepresentantesYOrganizaciones();
    }

    private void cargarRepresentantesYOrganizaciones() {

        try {

            RepresentanteDAO representanteDAO = new RepresentanteDAO();
            OrganizacionVinculadaDAO organizacionDAO = new OrganizacionVinculadaDAO();

            List<RepresentanteDTO> representantes = representanteDAO.obtenerTodosLosRepresentantes();
            List<OrganizacionVinculadaDTO> organizaciones = organizacionDAO.obtenerTodasLasOrganizaciones();

            ObservableList<ContenedoraRepresentanteOrganizacionProyecto> listaCombinada = FXCollections.observableArrayList();

            for (RepresentanteDTO representante : representantes) {

                for (OrganizacionVinculadaDTO organizacion : organizaciones) {

                    if (representante.getIdOrganizacion() == organizacion.getIdOrganizacion()) {

                        listaCombinada.add(new ContenedoraRepresentanteOrganizacionProyecto(representante, organizacion, null));
                    }
                }
            }

            tablaRepresentantes.setItems(listaCombinada);

        } catch (IOException | SQLException e) {

            LOGGER.error("Error al cargar representantes y organizaciones: " + e.getMessage());
            UTILIDADES.mostrarAlerta("Error", "Error al cargar datos", "No se pudo cargar la lista de representantes y organizaciones.");
        }
    }

    @FXML
    private void buscarRepresentante() {

        String textoBusqueda = campoBusqueda.getText().toLowerCase();
        ObservableList<ContenedoraRepresentanteOrganizacionProyecto> listaFiltrada = FXCollections.observableArrayList();

        if (textoBusqueda.isEmpty()) {

            UTILIDADES.mostrarAlerta(
                    "Error",
                    "El campo de busqueda se encuentra vacio",
                    "Por favor, ingrese el nombre del representante o nombre de la organización a buscar"
            );
            return;
        }

        for (ContenedoraRepresentanteOrganizacionProyecto contenedor : tablaRepresentantes.getItems()) {

            if (contenedor.getRepresentante().getNombre().toLowerCase().contains(textoBusqueda) ||
                contenedor.getOrganizacion().getNombre().toLowerCase().contains(textoBusqueda)) {

                listaFiltrada.add(contenedor);
            }
        }

        if (listaFiltrada.isEmpty()) {

            UTILIDADES.mostrarAlerta(
                    "Representante u Organización no encontrados",
                    "No se ha encontrado ningún representante u organización activo con ese nombre",
                    "Por favor, verifique que haya ingresado bien el nombre o registre la organización"
            );
            return;
        }

        tablaRepresentantes.setItems(listaFiltrada);
    }

    @FXML
    private void seleccionarRepresentante() {

        ContenedoraRepresentanteOrganizacionProyecto seleccion = tablaRepresentantes.getSelectionModel().getSelectedItem();

        if (seleccion != null) {

            SeleccionRepresentanteOrganizacion.setRepresentanteSeleccionado(seleccion.getRepresentante());
            SeleccionRepresentanteOrganizacion.setOrganizacionSeleccionada(seleccion.getOrganizacion());

            Stage stageActual = (Stage) tablaRepresentantes.getScene().getWindow();
            stageActual.close();

        } else {

            UTILIDADES.mostrarAlerta("Error", "Selección inválida", "Por favor, seleccione un representante y una organización.");
        }
    }

    @FXML
    private void botonCancelarSeleccion() {

        Stage stageActual = (Stage) tablaRepresentantes.getScene().getWindow();
        stageActual.close();
    }

    private ControladorRegistroProyectoGUI controladorRegistro;

    public void setControladorRegistro(ControladorRegistroProyectoGUI controladorRegistro) {

        this.controladorRegistro = controladorRegistro;
    }
}
