package GUI.gestionproyecto.asignacionproyecto;

import GUI.utilidades.ManejadorArchivos;
import GUI.utilidades.Utilidades;
import com.lowagie.text.DocumentException;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logica.DAOs.OrganizacionVinculadaDAO;
import logica.DAOs.ProyectoDAO;
import logica.DAOs.RepresentanteDAO;
import logica.DTOs.*;
import logica.generacionPDFs.GeneradoresPDF;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ControladorDetallesAsignacionProyectoGUI {

    private static final Logger LOGGER =
            Logger.getLogger(ControladorDetallesAsignacionProyectoGUI.class.getName());

    @FXML private Label etiquetaNombreProyecto;
    @FXML private Label etiquetaOrganizacionVinculada;
    @FXML private Label etiquetaRepresentante;
    @FXML private TextArea textoDescripcionProyecto;
    @FXML private Button botonDescargarOficioAsignacion;
    @FXML private Button botonReasignar;
    @FXML private Button botonCancelar;
    @FXML private Button botonAceptar;

    private ProyectoDTO proyectoAsignado;
    private EstudianteDTO estudianteAsignado;
    private ControladorAsignacionEstudianteAProyectoGUI controladorAsignacionPrincipal;

    private final Utilidades UTILIDADES = new Utilidades();
    private final GeneradoresPDF GENERADOR_PDF = new GeneradoresPDF();
    private final ManejadorArchivos MANEJADOR_ARCHIVOS = new ManejadorArchivos();

    public void inicializarDatos(ProyectoDTO proyectoDTO,
                                 EstudianteDTO estudianteDTO,
                                 ControladorAsignacionEstudianteAProyectoGUI controladorPrincipal) {

        this.proyectoAsignado = proyectoDTO;
        this.estudianteAsignado = estudianteDTO;
        this.controladorAsignacionPrincipal = controladorPrincipal;

        etiquetaNombreProyecto.setText(proyectoDTO.getNombre());
        textoDescripcionProyecto.setText(proyectoDTO.getDescripcion());
        cargarDetallesProyecto();
    }

    @FXML
    public void initialize() {

        botonDescargarOficioAsignacion.setCursor(Cursor.HAND);
        botonReasignar.setCursor(Cursor.HAND);
        botonCancelar.setCursor(Cursor.HAND);
        botonAceptar.setCursor(Cursor.HAND);
    }

    public void cargarDetallesProyecto() {

        RepresentanteDAO representanteDAO = new RepresentanteDAO();
        OrganizacionVinculadaDAO organizacionDAO = new OrganizacionVinculadaDAO();

        try {

            RepresentanteDTO representanteDTO =
                    representanteDAO.buscarRepresentantePorID(proyectoAsignado.getIdRepresentante());
            OrganizacionVinculadaDTO organizacionDTO =
                    organizacionDAO.buscarOrganizacionPorID(representanteDTO.getIdOrganizacion());

            etiquetaRepresentante.setText(representanteDTO.getNombre() + " " + representanteDTO.getApellidos());
            etiquetaOrganizacionVinculada.setText(organizacionDTO.getNombre());

        } catch (SQLException e) {
            manejarError("Error al cargar datos del proyecto", e);
        } catch (IOException e) {
            manejarError("Error al cargar datos del proyecto", e);
        }
    }

    @FXML
    private void manejarBotonGenerarPDF() {

        RepresentanteDAO representanteDAO = new RepresentanteDAO();
        OrganizacionVinculadaDAO organizacionDAO = new OrganizacionVinculadaDAO();

        try {

            RepresentanteDTO representanteDTO =
                    representanteDAO.buscarRepresentantePorID(proyectoAsignado.getIdRepresentante());
            OrganizacionVinculadaDTO organizacionDTO =
                    organizacionDAO.buscarOrganizacionPorID(representanteDTO.getIdOrganizacion());

            Stage ventana = (Stage) botonDescargarOficioAsignacion.getScene().getWindow();

            File rutaArchivo = MANEJADOR_ARCHIVOS.seleccionarUbicacionDeGuardado(
                    ventana,
                    "Asignacion-" + estudianteAsignado.getNombre() + ".pdf"
            );

            if (rutaArchivo != null) {

                GENERADOR_PDF.generarOficionAsignacion(
                        rutaArchivo,
                        estudianteAsignado,
                        proyectoAsignado,
                        representanteDTO,
                        organizacionDTO
                );

                UTILIDADES.mostrarAlerta("Archivo descargado",
                        "El archivo fue descargado de forma exitosa",
                        "");
            }
        } catch (SQLException | IOException | DocumentException e) {
            manejarError("Error al generar documento", e);
        }
    }

    @FXML
    private void reasignarProyecto() {
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        ManejadorDeAccionAsignacion manejador = new ManejadorDeAccionAsignacion();

        try {
            List<ProyectoDTO> proyectosDisponibles = proyectoDAO.listarProyectosConCupo();

            // Primero abrir la nueva ventana
            manejador.mostrarSeleccionProyecto(
                    estudianteAsignado,
                    controladorAsignacionPrincipal,
                    proyectosDisponibles
            );

            // Luego cerrar la actual (solo si la nueva se abrió correctamente)
            Stage ventanaActual = (Stage) botonReasignar.getScene().getWindow();
            ventanaActual.close();

        } catch (SQLException | IOException e) {
            UTILIDADES.mostrarAlerta(
                    "Error",
                    "No se pudo iniciar la reasignación",
                    "Por favor intentelo de nuevo más tarde");
            LOGGER.severe("Error en reasignación: " + e);
        }
    }

    private void manejarError(String mensaje, Exception e) {
        LOGGER.severe(mensaje + ": " + e);
        UTILIDADES.mostrarAlerta("Error",
                "Ocurrió un error: " + mensaje,
                "Por favor, intente nuevamente o contacte al administrador.");
    }
}