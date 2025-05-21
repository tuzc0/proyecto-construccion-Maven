package GUI.proyecto;

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
import logica.utilidadesproyecto.ContenedoraRepresentanteOrganizacion;
import logica.utilidadesproyecto.SeleccionRepresentanteOrganizacion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorSeleccionRepresentanteGUI {

    private static final Logger LOGGER = LogManager.getLogger(ControladorSeleccionRepresentanteGUI.class);
    private final Utilidades UTILIDADES = new Utilidades();

    @FXML private TableView <ContenedoraRepresentanteOrganizacion> tablaRepresentantes;
    @FXML private TableColumn<ContenedoraRepresentanteOrganizacion, String> columnaRepresentante;
    @FXML private TableColumn<ContenedoraRepresentanteOrganizacion, String> columnaOrganizacion;
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

            ObservableList<ContenedoraRepresentanteOrganizacion> listaCombinada = FXCollections.observableArrayList();

            for (RepresentanteDTO representante : representantes) {

                for (OrganizacionVinculadaDTO organizacion : organizaciones) {

                    if (representante.getIdOrganizacion() == organizacion.getIdOrganizacion()) {

                        listaCombinada.add(new ContenedoraRepresentanteOrganizacion(representante, organizacion));
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
        ObservableList<ContenedoraRepresentanteOrganizacion> listaFiltrada = FXCollections.observableArrayList();

        for (ContenedoraRepresentanteOrganizacion contenedor : tablaRepresentantes.getItems()) {

            if (contenedor.getRepresentante().getNombre().toLowerCase().contains(textoBusqueda) ||
                contenedor.getOrganizacion().getNombre().toLowerCase().contains(textoBusqueda)) {

                listaFiltrada.add(contenedor);
            }
        }

        tablaRepresentantes.setItems(listaFiltrada);
    }

    @FXML
    private void seleccionarRepresentante() {

        ContenedoraRepresentanteOrganizacion seleccion = tablaRepresentantes.getSelectionModel().getSelectedItem();

        if (seleccion != null) {

            SeleccionRepresentanteOrganizacion.setRepresentanteSeleccionado(seleccion.getRepresentante());
            SeleccionRepresentanteOrganizacion.setOrganizacionSeleccionada(seleccion.getOrganizacion());

            if (controladorRegistro != null) {
                controladorRegistro.setRepresentanteSeleccionado(
                        seleccion.getRepresentante(),
                        seleccion.getOrganizacion()
                );
            }

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
