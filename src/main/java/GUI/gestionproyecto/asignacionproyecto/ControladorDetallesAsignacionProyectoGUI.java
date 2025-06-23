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
import logica.ManejadorExcepciones;
import logica.generacionPDFs.GeneradoresPDF;
import logica.interfaces.IGestorAlertas;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorDetallesAsignacionProyectoGUI {

    private static final Logger LOGGER =
            org.apache.logging.log4j.LogManager.getLogger(ControladorDetallesAsignacionProyectoGUI.class);

    @FXML
    private Label etiquetaNombreProyecto;
    @FXML
    private Label etiquetaOrganizacionVinculada;
    @FXML
    private Label etiquetaRepresentante;
    @FXML
    private TextArea textoDescripcionProyecto;
    @FXML
    private Button botonDescargarOficioAsignacion;
    @FXML
    private Button botonReasignar;
    @FXML
    private Button botonCancelar;
    @FXML
    private Button botonAceptar;

    private Utilidades gestorVentanas = new Utilidades();

    private IGestorAlertas utilidades = new Utilidades();

    private ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, LOGGER);

    private ProyectoDTO proyectoAsignado;
    private EstudianteDTO estudianteAsignado;
    private ControladorAsignacionEstudianteAProyectoGUI controladorAsignacionPrincipal;
    private boolean vistaCoordinador = false;

    private final GeneradoresPDF GENERADOR_PDF = new GeneradoresPDF();
    private final ManejadorArchivos MANEJADOR_ARCHIVOS = new ManejadorArchivos();

    public void inicializarDatos(ProyectoDTO proyectoDTO,
                                 EstudianteDTO estudianteDTO,
                                 ControladorAsignacionEstudianteAProyectoGUI controladorPrincipal) {

        this.proyectoAsignado = proyectoDTO;
        this.estudianteAsignado = estudianteDTO;
        this.controladorAsignacionPrincipal = controladorPrincipal;

        botonReasignar.setVisible(vistaCoordinador);
        botonReasignar.setManaged(vistaCoordinador);

        etiquetaNombreProyecto.setText(proyectoDTO.getNombre());
        textoDescripcionProyecto.setText(proyectoDTO.getDescripcion());
        cargarDetallesProyecto();
    }

    public void setEsVistaDeCoordinador(boolean coordinador) {

        this.vistaCoordinador = coordinador;
    }

    public void configurarVista() {

        botonReasignar.setVisible(vistaCoordinador);
        botonReasignar.setManaged(vistaCoordinador);
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

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            LOGGER.error("Error al cargar los detalles del proyecto: " + e);
            utilidades.mostrarAlerta(
                    "Error",
                    "No se pudieron cargar los detalles del proyecto.",
                    "Por favor, intente nuevamente o contacte al administrador.");
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

            File rutaArchivo = MANEJADOR_ARCHIVOS.seleccionarUbicacionDeGuardado(ventana,
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

                utilidades.mostrarAlerta(
                        "Archivo descargado",
                        "El archivo fue descargado de forma exitosa",
                        "");
            }

        } catch (DocumentException e) {

            LOGGER.error("Error al generar el PDF: " + e);
            utilidades.mostrarAlerta(
                    "Error al generar el documento.",
                    "No se puedo generar el PDF.",
                    "Ocurrió un problema al crear el documento. Por favor, intente nuevamente o contacte al administrador.");

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            LOGGER.error("Error inesperado al generar el PDF: " + e);
            utilidades.mostrarAlerta(
                    "Error inesperado",
                    "Ocurrió un error al generar el PDF.",
                    "Por favor, intente nuevamente o contacte al administrador.");
        }
    }

    @FXML
    private void reasignarProyecto() {

        ProyectoDAO proyectoDAO = new ProyectoDAO();
        ManejadorDeAccionAsignacion manejador = new ManejadorDeAccionAsignacion();

        try {

            List<ProyectoDTO> proyectosDisponibles = proyectoDAO.listarProyectosConCupo();

            manejador.abrirVentanaSeleccionProyecto(
                    estudianteAsignado,
                    controladorAsignacionPrincipal,
                    proyectosDisponibles
            );

            Stage ventanaActual = (Stage) botonReasignar.getScene().getWindow();
            ventanaActual.close();

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            LOGGER.error("Error al abrir la ventana de selección de proyecto: " + e);
            utilidades.mostrarAlerta(
                    "Error",
                    "No se pudo abrir la ventana de selección de proyecto.",
                    "Por favor, intente nuevamente o contacte al administrador.");
        }
    }
}