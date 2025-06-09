package GUI;

import GUI.utilidades.Utilidades;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import logica.ContenedorCriteriosEvaluacion;
import logica.DAOs.*;
import logica.DTOs.*;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorDetallesEvaluacionGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorDetallesEvaluacionGUI.class);

    @FXML private Label lblTitulo;
    @FXML private Label lblEvaluador;
    @FXML private Label lblFecha;
    @FXML private Label lblCalificacionFinal;
    @FXML private TableView<ContenedorCriteriosEvaluacion> tablaCriterios;
    @FXML private TableColumn<ContenedorCriteriosEvaluacion, String> colNumeroCriterio;
    @FXML private TableColumn<ContenedorCriteriosEvaluacion, String> colDescripcion;
    @FXML private TableColumn<ContenedorCriteriosEvaluacion, String> colCalificacion;

    @FXML private TextArea txtComentarios;

    Utilidades utilidades = new Utilidades();

    int idEvaluacion = 0;


    public void setIdEvaluacion(int idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }

    @FXML
    public void initialize() {

        colNumeroCriterio.setCellValueFactory(new PropertyValueFactory<>("numeroCriterio"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colCalificacion.setCellValueFactory(new PropertyValueFactory<>("calificacion"));



    }

    public void cargarDetallesEvaluacion(int idEvaluacion) {
        EvaluacionDTO evaluacion = obtenerEvaluacionPorId(idEvaluacion);

        if (evaluacion == null) {
            logger.error("No se pudo cargar la evaluación con ID: " + idEvaluacion);
            utilidades.mostrarAlerta("Error", "No se pudo cargar la evaluación", "La evaluación solicitada no existe o no se pudo cargar.");
            cerrarVentana();
            return;
        }

        lblTitulo.setText("Detalles de la Evaluación #" + idEvaluacion);
        lblCalificacionFinal.setText(String.format("%.2f", evaluacion.getCalificacionFinal()));
        txtComentarios.setText(evaluacion.getComentarios());
        lblEvaluador.setText(obtenerNombreEvaluador(evaluacion.getNumeroDePersonal()));


        cargarDatosCriteriosEvaluacion(idEvaluacion);
    }

    @FXML
    public void cerrarVentana() {
        Stage stage = (Stage) lblTitulo.getScene().getWindow();
        stage.close();
    }

    private EvaluacionDTO obtenerEvaluacionPorId(int idEvaluacion) {
        EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
        try {
            EvaluacionDTO evaluacion = evaluacionDAO.buscarEvaluacionPorID(idEvaluacion);
            if (evaluacion.getIDEvaluacion() == -1) {
                logger.info("No se encontró la evaluación con ID: " + idEvaluacion);
                return null;
            }
            return evaluacion;
        } catch (SQLException e) {
            logger.error("Error al buscar la evaluación por ID: " + e);
            utilidades.mostrarAlerta("Error de BD", "Error al buscar evaluación", "No se pudo conectar con la base de datos.");
        } catch (IOException e) {
            logger.error("Error al cargar la evaluación: " + e);
            utilidades.mostrarAlerta("Error", "Error al cargar datos", "Ocurrió un error al cargar los datos de la evaluación.");
        }
        return null;
    }

    private String obtenerNombreEvaluador(int numeroPersonal) {
        AcademicoEvaluadorDAO academicoEvaluadorDAO = new AcademicoEvaluadorDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        try {
            AcademicoEvaluadorDTO evaluador = academicoEvaluadorDAO.buscarAcademicoEvaluadorPorNumeroDePersonal(numeroPersonal);
            if (evaluador == null) {
                return "Evaluador no encontrado";
            }
            UsuarioDTO usuario = usuarioDAO.buscarUsuarioPorID(evaluador.getIdUsuario());
            return usuario.getNombre() + " " + usuario.getApellido();
        } catch (SQLException | IOException e) {
            logger.error("Error al obtener nombre del evaluador: " + e.getMessage());
            return "Error al cargar evaluador";
        }
    }

    private void cargarDatosCriteriosEvaluacion(int idEvaluacionGenerada) {
        try {
            CriterioEvaluacionDAO criterioEvaluacionDAO = new CriterioEvaluacionDAO();
            EvaluacionContieneDAO evaluacionContieneDAO = new EvaluacionContieneDAO();

            List<CriterioEvaluacionDTO> listaCriterios = criterioEvaluacionDAO.listarCriteriosActivos();
            List<EvaluacionContieneDTO> listaEvaluacionContiene = evaluacionContieneDAO.listarCriteriosEvaluacionPorIdEvaluacion(idEvaluacionGenerada);

            ObservableList<ContenedorCriteriosEvaluacion> listaContenedorCriterios = FXCollections.observableArrayList();

            for (CriterioEvaluacionDTO criterio : listaCriterios) {
                for (EvaluacionContieneDTO evaluacionContiene : listaEvaluacionContiene) {
                    if (criterio.getIDCriterio() == evaluacionContiene.getIdCriterio()) {
                        listaContenedorCriterios.add(new ContenedorCriteriosEvaluacion(criterio, evaluacionContiene));
                    }
                }
            }

            tablaCriterios.setItems(listaContenedorCriterios);

        } catch (SQLException e) {
            logger.error("Error de SQL: " + e.getMessage());
            utilidades.mostrarAlerta("Error de BD", "Error al cargar criterios", "No se pudieron cargar los criterios de evaluación.");
        } catch (IOException e) {
            logger.error("Error de IO: " + e.getMessage());
            utilidades.mostrarAlerta("Error", "Error al cargar datos", "Ocurrió un error al cargar los criterios.");
        } catch (Exception e) {
            logger.error("Error inesperado: " + e.getMessage());
            utilidades.mostrarAlerta("Error", "Error inesperado", "Ocurrió un error inesperado al cargar los criterios.");
        }
    }
}