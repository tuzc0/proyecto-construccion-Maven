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

    private Utilidades utilidades = new Utilidades();
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

            LOGGER.severe("Error al cargar los detalles del estudiante en MySQL: " + e);
            utilidades.mostrarAlerta(
                    "Error",
                    "Ocurrió un error al cargar los datos.",
                    "Por favor, intente nuevamente o contacte al administrador.");

        } catch (IOException e) {

            LOGGER.severe("Error al cargar los detalles del estudiante: " + e);
            utilidades.mostrarAlerta(
                    "Error",
                    "Ocurrió un error al intentar cargar los datos del estudiante.",
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

            LOGGER.severe("Error al gerar documento: " + e);
            utilidades.mostrarAlerta(
                    "Error al generar el documento.",
                    "No se puedo generar el PDF.",
                    "Ocurrió un problema al crear el documento. Por favor, intente nuevamente o contacte al administrador.");

        } catch (SQLException e) {

            LOGGER.severe("Error de base de datos: " + e);
            utilidades.mostrarAlerta(
                    "Error",
                    "Ocurrió problema al acceder a los datos.",
                    "Por favor, intente nuevamente o contacte al administrador.");

        } catch (IOException e) {

            LOGGER.severe("Error de E/S: " + e);
            utilidades.mostrarAlerta(
                    "Error",
                    "Ocurrió un problema al procesar los datos.",
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

            LOGGER.severe("Error dentro de la base de datos: " + e);
            utilidades.mostrarAlerta(
                    "Error",
                    "No se pudo iniciar la reasignación",
                    "Por favor intentelo de nuevo más tarde");

        } catch (IOException e) {

            LOGGER.severe("Error durante la reasignación: " + e);
            utilidades.mostrarAlerta(
                    "Error",
                    "Error al cargar la ventana",
                    "Ocurrio un error al intentar cargar la ventana, por favor, " +
                            "intentelo de nuevo o contacte al administrador."
            );
        }
    }
}