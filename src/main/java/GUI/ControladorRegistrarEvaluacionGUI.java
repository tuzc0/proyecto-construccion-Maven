package GUI;

import GUI.utilidades.Utilidades;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import logica.ContenedorCriteriosEvaluacion;
import logica.DAOs.CriterioEvaluacionDAO;
import logica.DAOs.EvaluacionContieneDAO;
import logica.DAOs.EvaluacionDAO;
import logica.DTOs.CriterioEvaluacionDTO;
import logica.DTOs.EvaluacionContieneDTO;
import logica.DTOs.EvaluacionDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class ControladorRegistrarEvaluacionGUI {

    private static final Logger logger = LogManager.getLogger(ControladorRegistrarEvaluacionGUI.class);

    Utilidades utilidades = new Utilidades();

    @FXML
    Label etiquetaPromedioEvaluacionGenerado;

    @FXML
    TextArea textoComentarios;

    @FXML
    TableView<ContenedorCriteriosEvaluacion> tablaCriteriosEvaluacion;

    @FXML
    TableColumn<ContenedorCriteriosEvaluacion, String> columnaNumeroCriterio;

    @FXML
    TableColumn<ContenedorCriteriosEvaluacion, String> columnaCriterio;

    @FXML
    TableColumn<ContenedorCriteriosEvaluacion, String> columnaCalificacion;

    @FXML
    Label etiquetaEstudianteEvaluado;

    static int idEvaluacionGenerada = 0;

    float calificacionFinal = 0.0f;

    int numeroDePersonal = ControladorInicioDeSesionGUI.numeroDePersonal;


    public String matriculaEstudianteEvaluado = ControladorConsultarEstudiantesAEvaluarGUI.matriculaEstudianteSeleccionado;

    @FXML
    public void initialize() {

        columnaNumeroCriterio.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getCriterioEvaluacion().getNumeroCriterio())));
        columnaCriterio.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCriterioEvaluacion().getDescripcion()));
        columnaCalificacion.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getEvaluacionContiene().getCalificacion())));

        tablaCriteriosEvaluacion.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        etiquetaEstudianteEvaluado.setText(matriculaEstudianteEvaluado);

        actualizarCalificacion();

        registrarEvaluacionVacia();
        registrarCriteriosVacios();
        cargarCriterios();
    }

    public void actualizarCalificacion () {


        columnaCalificacion.setCellFactory(TextFieldTableCell.forTableColumn());
        columnaCalificacion.setOnEditCommit(event -> {
            ContenedorCriteriosEvaluacion contenedor = event.getRowValue();
            EvaluacionContieneDTO evaluacionContiene = contenedor.getEvaluacionContiene();

            try {

                float nuevaCalificacion = Float.parseFloat(event.getNewValue());

                if (nuevaCalificacion < 0 || nuevaCalificacion > 10 ) {

                    logger.warn("La calificación debe estar entre 0 y 100.");
                    utilidades.mostrarAlerta("Error",
                            "La calificación debe estar entre 0 y 10.",
                            "porfavor llene todos los campos");
                    return;
                }

                evaluacionContiene.setCalificacion(nuevaCalificacion);

                EvaluacionContieneDAO evaluacionContieneDAO = new EvaluacionContieneDAO();
                boolean actualizado = evaluacionContieneDAO.modificarCalificacion(evaluacionContiene);

                if (actualizado) {

                    logger.info("Calificación actualizada correctamente.");
                } else {

                    logger.warn("No se pudo actualizar la calificación.");
                }
            } catch (NumberFormatException e) {

                logger.warn("El valor ingresado no es un número válido.");
            } catch (SQLException | IOException e) {

                logger.error("Error al actualizar la calificación: " + e);
            }
        });


        tablaCriteriosEvaluacion.setEditable(true);
    }

    public void cargarCriterios() {

        try{

            CriterioEvaluacionDAO criterioEvaluacionDAO = new CriterioEvaluacionDAO();
            EvaluacionContieneDAO evaluacionContieneDAO = new EvaluacionContieneDAO();

            List<CriterioEvaluacionDTO> listaCriterios = criterioEvaluacionDAO.listarCriteriosActivos();
            List<EvaluacionContieneDTO> listaEvaluacionContiene =
                    evaluacionContieneDAO.listarCriteriosEvaluacionPorIdEvaluacion(idEvaluacionGenerada);
            ObservableList<ContenedorCriteriosEvaluacion> listaContenedorCriterios =
                    FXCollections.observableArrayList();

            for (CriterioEvaluacionDTO criterio : listaCriterios){

                for (EvaluacionContieneDTO evaluacionContiene : listaEvaluacionContiene){

                    if (criterio.getIDCriterio() == evaluacionContiene.getIdCriterio()){

                        ContenedorCriteriosEvaluacion contenedorCriterios =
                                new ContenedorCriteriosEvaluacion(criterio, evaluacionContiene);
                        listaContenedorCriterios.add(contenedorCriterios);
                    }
                }
            }

            tablaCriteriosEvaluacion.setItems(listaContenedorCriterios);


        } catch (SQLException e) {

            logger.error("Error de SQL: " + e);

        } catch (IOException e) {

            logger.error("Error de IO: " + e);

        } catch (Exception e){

            logger.error("Error inesperado: " + e);

        }
    }

    public void registrarEvaluacionVacia () {

        EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
        EvaluacionDTO evaluacionDTO = new EvaluacionDTO();

        evaluacionDTO.setComentarios(textoComentarios.getText());
        evaluacionDTO.setMatriculaEstudiante(matriculaEstudianteEvaluado);
        evaluacionDTO.setCalificacionFinal(0.0f);
        evaluacionDTO.setEstadoActivo(1);
        evaluacionDTO.setNumeroDePersonal(numeroDePersonal);
        evaluacionDTO.setIDEvaluacion(0);


        try {

            idEvaluacionGenerada= evaluacionDAO.crearNuevaEvaluacion(evaluacionDTO);

        } catch (SQLException e) {

            logger.error("Error de SQL: " + e);
        } catch (IOException e) {

            logger.error("Error de IO: " + e);
        } catch (Exception e) {

            logger.error("Error inesperado: " + e);
        }
    }

    public void registrarCriteriosVacios(){

        EvaluacionContieneDAO evaluacionContieneDAO = new EvaluacionContieneDAO();
        CriterioEvaluacionDAO criterioEvaluacionDAO = new CriterioEvaluacionDAO();

        try {

            List<CriterioEvaluacionDTO> listaCriterios = criterioEvaluacionDAO.listarCriteriosActivos();

            for (CriterioEvaluacionDTO criterio : listaCriterios){

                EvaluacionContieneDTO evaluacionContieneDTO = new EvaluacionContieneDTO();
                evaluacionContieneDTO.setIdEvaluacion(idEvaluacionGenerada);
                evaluacionContieneDTO.setIdCriterio(criterio.getIDCriterio());
                evaluacionContieneDTO.setCalificacion(0.0f);

                evaluacionContieneDAO.insertarEvaluacionContiene(evaluacionContieneDTO);
            }

        } catch (SQLException e) {

            logger.error("Error de SQL: " + e);

        } catch (IOException e) {

            logger.error("Error de IO: " + e);

        } catch (Exception e) {

            logger.error("Error inesperado: " + e);
        }
    }

    @FXML
    public void cancelarEvaluacion () {

        EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
        EvaluacionContieneDAO evaluacionContieneDAO = new EvaluacionContieneDAO();

        try {

            evaluacionContieneDAO.eliminarCriteriosPorIdEvaluacion(idEvaluacionGenerada);
            evaluacionDAO.eliminarEvaluacionDefinitivamente(idEvaluacionGenerada);
            javafx.stage.Stage stage = (javafx.stage.Stage) textoComentarios.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {

            logger.error("Error de SQL: " + e);
            e.printStackTrace();

        } catch (IOException e) {

            logger.error("Error de IO: " + e);

        } catch (Exception e) {

            logger.error("Error inesperado: " + e);
        }
    }

    @FXML
    public void calcularPromedio () {

        calificacionFinal = 0.0f;

        EvaluacionContieneDAO evaluacionContieneDAO = new EvaluacionContieneDAO();

        try{

            List<EvaluacionContieneDTO> listaEvaluacionContiene =
                    evaluacionContieneDAO.listarCriteriosEvaluacionPorIdEvaluacion(idEvaluacionGenerada);

            for (EvaluacionContieneDTO evaluacionContiene : listaEvaluacionContiene){

                calificacionFinal += evaluacionContiene.getCalificacion();

                if (evaluacionContiene.getCalificacion() < 1 || evaluacionContiene.getCalificacion() > 10) {

                    utilidades.mostrarAlerta("Error",
                            "La calificación debe estar entre 0 y 10.",
                            "porfavor llene todos los campos");
                    return;
                }
            }

            calificacionFinal = calificacionFinal / listaEvaluacionContiene.size();
            String promedioFormateado = String.format("%.2f", calificacionFinal);
            etiquetaPromedioEvaluacionGenerado.setText(promedioFormateado);

        } catch (SQLException e) {

            logger.error("Error de SQL: " + e);

        } catch (IOException e) {

            logger.error("Error de IO: " + e);

        } catch (Exception e) {

            logger.error("Error inesperado: " + e);
        }

    }

    @FXML
    public void guardarEvaluacion () {

        EvaluacionDAO evaluacionDAO = new EvaluacionDAO();

        try {

            EvaluacionDTO evaluacionDTO = new EvaluacionDTO();
            evaluacionDTO.setComentarios(textoComentarios.getText());
            evaluacionDTO.setMatriculaEstudiante(matriculaEstudianteEvaluado);
            evaluacionDTO.setCalificacionFinal(calificacionFinal);
            evaluacionDTO.setEstadoActivo(1);
            evaluacionDTO.setNumeroDePersonal(numeroDePersonal);
            evaluacionDTO.setIDEvaluacion(idEvaluacionGenerada);

            boolean actualizado = evaluacionDAO.modificarEvaluacion(evaluacionDTO);

            if (calificacionFinal < 1 || calificacionFinal > 10) {

                utilidades.mostrarAlerta("Error",
                        "Debe de calcular la calificacio primero",
                        "porfavor calcule la calificacion final");
                return;
            }

            if (actualizado) {

                utilidades.mostrarAlerta("Éxito",
                        "Evaluación guardada correctamente.",
                        "La evaluación ha sido guardada.");
                javafx.stage.Stage stage = (javafx.stage.Stage) textoComentarios.getScene().getWindow();
                stage.close();

            } else {

                utilidades.mostrarAlerta("Error",
                        "No se pudo guardar la evaluación.",
                        "ocurrio un error porfavor intentelo de nuevo en unos minutos");
                logger.warn("No se pudo guardar la evaluación.");
            }

        } catch (SQLException e) {

            logger.error("Error de SQL: " + e);
            utilidades.mostrarAlerta("Error",
                    "Error de conexion " ,
                    "ocurrio un error porfavor intentelo de nuevo en unos minutos");


        } catch (IOException e) {

            utilidades.mostrarAlerta("Error",
                    "ocurrio un error " ,
                    "ocurrio un error porfavor intentelo de nuevo en unos minutos");
            logger.error("Error de IO: " + e);

        } catch (Exception e) {

            utilidades.mostrarAlerta("Error",
                    "ocurrio un error ",
                    "ocurrio un error porfavor intentelo de nuevo en unos minutos");
            logger.error("Error inesperado: " + e);
        }
    }

}
