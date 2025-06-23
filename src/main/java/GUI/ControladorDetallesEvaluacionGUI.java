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
import logica.ManejadorExcepciones;
import logica.interfaces.IGestorAlertas;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorDetallesEvaluacionGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorDetallesEvaluacionGUI.class);

    @FXML
    private Label lblTitulo;
    @FXML
    private Label lblEvaluador;
    @FXML
    private Label lblCalificacionFinal;
    @FXML
    private TableView<ContenedorCriteriosEvaluacion> tablaCriterios;
    @FXML
    private TableColumn<ContenedorCriteriosEvaluacion, String> colNumeroCriterio;
    @FXML
    private TableColumn<ContenedorCriteriosEvaluacion, String> colDescripcion;
    @FXML
    private TableColumn<ContenedorCriteriosEvaluacion, String> colCalificacion;

    @FXML
    private TextArea txtComentarios;

    Utilidades gestionVentanas = new Utilidades();
    IGestorAlertas utilidades = new Utilidades();
    ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, logger);

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
            gestionVentanas.mostrarAlerta("Error",
                    "No se pudo cargar la evaluación",
                    "La evaluación solicitada no existe o no se pudo cargar.");
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

                return null;

            }

            return evaluacion;

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al obtener la evaluación: " + e);
            gestionVentanas.mostrarAlerta(
                    "Error",
                    "Error inesperado",
                    "Ocurrió un error inesperado al cargar los detalles de la evaluación.");
        }

        return null;
    }

    private String obtenerNombreEvaluador(int numeroPersonal) {

        AcademicoEvaluadorDAO academicoEvaluadorDAO = new AcademicoEvaluadorDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        try {

            AcademicoEvaluadorDTO evaluador =
                    academicoEvaluadorDAO.buscarAcademicoEvaluadorPorNumeroDePersonal(numeroPersonal);

            if (evaluador == null) {

                return "Evaluador no encontrado";

            }

            UsuarioDTO usuario = usuarioDAO.buscarUsuarioPorID(evaluador.getIdUsuario());
            return usuario.getNombre() + " " + usuario.getApellido();

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            return "Error al cargar evaluador";

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            return "Error al cargar evaluador";

        } catch (Exception e) {

            logger.error("Error inesperado al obtener el nombre del evaluador: " + e);
            gestionVentanas.mostrarAlerta(
                    "Error",
                    "Error inesperado",
                    "Ocurrió un error inesperado al cargar el nombre del evaluador.");
            return "Error inesperado";
        }

    }

    private void cargarDatosCriteriosEvaluacion(int idEvaluacionGenerada) {

        CriterioEvaluacionDAO criterioEvaluacionDAO = new CriterioEvaluacionDAO();
        EvaluacionContieneDAO evaluacionContieneDAO = new EvaluacionContieneDAO();

        try {

            List<CriterioEvaluacionDTO> listaCriterios = criterioEvaluacionDAO.listarCriteriosActivos();
            List<EvaluacionContieneDTO> listaEvaluacionContiene =
                    evaluacionContieneDAO.listarCriteriosEvaluacionPorIdEvaluacion(idEvaluacionGenerada);

            ObservableList<ContenedorCriteriosEvaluacion> listaContenedorCriterios =
                    FXCollections.observableArrayList();

            for (CriterioEvaluacionDTO criterio : listaCriterios) {

                for (EvaluacionContieneDTO evaluacionContiene : listaEvaluacionContiene) {

                    if (criterio.getIDCriterio() == evaluacionContiene.getIdCriterio()) {

                        listaContenedorCriterios.add(new ContenedorCriteriosEvaluacion(criterio,
                                evaluacionContiene));
                    }
                }
            }

            tablaCriterios.setItems(listaContenedorCriterios);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado: " + e);
            gestionVentanas.mostrarAlerta(
                    "Error",
                    "Error inesperado",
                    "Ocurrió un error inesperado al cargar los criterios.");
        }
    }
}