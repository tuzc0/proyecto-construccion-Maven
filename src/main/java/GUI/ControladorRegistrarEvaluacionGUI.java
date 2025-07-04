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
import logica.ManejadorExcepciones;
import logica.VerificacionEntradas;
import logica.interfaces.IGestorAlertas;
import logica.verificacion.VerificicacionGeneral;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class ControladorRegistrarEvaluacionGUI {

    private static final Logger LOGGER = LogManager.getLogger(ControladorRegistrarEvaluacionGUI.class);

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

    @FXML
    Label etiquetaContadorComentarios;

    static int idEvaluacionGenerada = 0;
    float calificacionFinal = 0.0f;
    int numeroDePersonal = ControladorInicioDeSesionGUI.numeroDePersonal;
    final int MAX_CARACTERES_COMENTARIOS = 255;
    public String matriculaEstudianteEvaluado =
            ControladorConsultarEstudiantesAEvaluarGUI.matriculaEstudianteSeleccionado;

    VerificicacionGeneral verificicacionGeneralUtilidad = new VerificicacionGeneral();
    Utilidades gestorVentanas = new Utilidades();
    IGestorAlertas utilidades = new Utilidades();
    VerificacionEntradas verificacionEntradas = new VerificacionEntradas();

    ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, LOGGER);

    @FXML
    public void initialize() {

        verificicacionGeneralUtilidad.contadorCaracteresTextArea(textoComentarios,
                etiquetaContadorComentarios, MAX_CARACTERES_COMENTARIOS);

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

    public void actualizarCalificacion() {


        columnaCalificacion.setCellFactory(TextFieldTableCell.forTableColumn());
        columnaCalificacion.setOnEditCommit(evento -> {

            ContenedorCriteriosEvaluacion contenedor = evento.getRowValue();
            EvaluacionContieneDTO evaluacionContiene = contenedor.getEvaluacionContiene();

            try {

                float nuevaCalificacion = Float.parseFloat(evento.getNewValue());

                if (nuevaCalificacion < 0 || nuevaCalificacion > 10) {

                    LOGGER.warn("La calificación debe estar entre 0 y 100.");
                    gestorVentanas.mostrarAlerta("Error",
                            "La calificación debe estar entre 0 y 10.",
                            "porfavor llene todos los campos");
                    return;
                }

                evaluacionContiene.setCalificacion(nuevaCalificacion);

                EvaluacionContieneDAO evaluacionContieneDAO = new EvaluacionContieneDAO();
                boolean actualizado = evaluacionContieneDAO.modificarCalificacion(evaluacionContiene);

                if (actualizado) {

                    LOGGER.info("Calificación actualizada correctamente.");


                } else {

                    LOGGER.warn("No se pudo actualizar la calificación.");
                }

            } catch (NumberFormatException e) {

                LOGGER.warn("El valor ingresado no es un número válido.");

            } catch (SQLException e) {

                manejadorExcepciones.manejarSQLException(e);

            } catch (IOException e) {

                manejadorExcepciones.manejarIOException(e);

            } catch (Exception e) {

                LOGGER.error("Error inesperado: " + e);
                utilidades.mostrarAlerta("Error",
                        "ocurrio un error ",
                        "ocurrio un error porfavor intentelo de nuevo en unos minutos");
            }
        });

        tablaCriteriosEvaluacion.setEditable(true);
    }

    public void cargarCriterios() {

        try {

            CriterioEvaluacionDAO criterioEvaluacionDAO = new CriterioEvaluacionDAO();
            EvaluacionContieneDAO evaluacionContieneDAO = new EvaluacionContieneDAO();

            List<CriterioEvaluacionDTO> listaCriterios = criterioEvaluacionDAO.listarCriteriosActivos();
            List<EvaluacionContieneDTO> listaEvaluacionContiene =
                    evaluacionContieneDAO.listarCriteriosEvaluacionPorIdEvaluacion(idEvaluacionGenerada);
            ObservableList<ContenedorCriteriosEvaluacion> listaContenedorCriterios =
                    FXCollections.observableArrayList();

            for (CriterioEvaluacionDTO criterio : listaCriterios) {

                for (EvaluacionContieneDTO evaluacionContiene : listaEvaluacionContiene) {

                    if (criterio.getIDCriterio() == evaluacionContiene.getIdCriterio()) {

                        ContenedorCriteriosEvaluacion contenedorCriterios =
                                new ContenedorCriteriosEvaluacion(criterio, evaluacionContiene);
                        listaContenedorCriterios.add(contenedorCriterios);
                    }
                }
            }

            tablaCriteriosEvaluacion.setItems(listaContenedorCriterios);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            LOGGER.error("Error inesperado: " + e);
            utilidades.mostrarAlerta("Error",
                    "ocurrio un error ",
                    "ocurrio un error porfavor intentelo de nuevo en unos minutos");

        }
    }

    public void registrarEvaluacionVacia() {

        EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
        EvaluacionDTO evaluacionDTO = new EvaluacionDTO();

        evaluacionDTO.setComentarios(textoComentarios.getText());
        evaluacionDTO.setMatriculaEstudiante(matriculaEstudianteEvaluado);
        evaluacionDTO.setCalificacionFinal(0.0f);
        evaluacionDTO.setEstadoActivo(1);
        evaluacionDTO.setNumeroDePersonal(numeroDePersonal);
        evaluacionDTO.setIDEvaluacion(0);


        try {

            idEvaluacionGenerada = evaluacionDAO.crearNuevaEvaluacion(evaluacionDTO);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            LOGGER.error("Error inesperado: " + e);
            utilidades.mostrarAlerta("Error",
                    "ocurrio un error ",
                    "ocurrio un error porfavor intentelo de nuevo en unos minutos");
        }
    }

    public void registrarCriteriosVacios() {

        EvaluacionContieneDAO evaluacionContieneDAO = new EvaluacionContieneDAO();
        CriterioEvaluacionDAO criterioEvaluacionDAO = new CriterioEvaluacionDAO();

        try {

            List<CriterioEvaluacionDTO> listaCriterios = criterioEvaluacionDAO.listarCriteriosActivos();

            for (CriterioEvaluacionDTO criterio : listaCriterios) {

                EvaluacionContieneDTO evaluacionContieneDTO = new EvaluacionContieneDTO();
                evaluacionContieneDTO.setIdEvaluacion(idEvaluacionGenerada);
                evaluacionContieneDTO.setIdCriterio(criterio.getIDCriterio());
                evaluacionContieneDTO.setCalificacion(0.0f);

                evaluacionContieneDAO.insertarEvaluacionContiene(evaluacionContieneDTO);
            }

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            LOGGER.error("Error inesperado: " + e);
            utilidades.mostrarAlerta("Error",
                    "ocurrio un error ",
                    "ocurrio un error porfavor intentelo de nuevo en unos minutos");
        }
    }

    @FXML
    public void cancelarEvaluacion() {

        EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
        EvaluacionContieneDAO evaluacionContieneDAO = new EvaluacionContieneDAO();

        try {

            evaluacionContieneDAO.eliminarCriteriosPorIdEvaluacion(idEvaluacionGenerada);
            evaluacionDAO.eliminarEvaluacionDefinitivamente(idEvaluacionGenerada);
            javafx.stage.Stage stage = (javafx.stage.Stage) textoComentarios.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            LOGGER.error("Error inesperado: " + e);
            utilidades.mostrarAlerta("Error",
                    "ocurrio un error ",
                    "ocurrio un error porfavor intentelo de nuevo en unos minutos");
        }
    }

    @FXML
    public void calcularPromedio() {

        calificacionFinal = 0.0f;

        EvaluacionContieneDAO evaluacionContieneDAO = new EvaluacionContieneDAO();

        try {

            List<EvaluacionContieneDTO> listaEvaluacionContiene =
                    evaluacionContieneDAO.listarCriteriosEvaluacionPorIdEvaluacion(idEvaluacionGenerada);

            for (EvaluacionContieneDTO evaluacionContiene : listaEvaluacionContiene) {

                calificacionFinal += evaluacionContiene.getCalificacion();

                if (evaluacionContiene.getCalificacion() < 1 || evaluacionContiene.getCalificacion() > 10) {

                    gestorVentanas.mostrarAlerta("Error",
                            "La calificación debe estar entre 0 y 10.",
                            "porfavor llene todos los campos");
                    return;
                }
            }

            calificacionFinal = calificacionFinal / listaEvaluacionContiene.size();
            String promedioFormateado = String.format("%.2f", calificacionFinal);
            etiquetaPromedioEvaluacionGenerado.setText(promedioFormateado);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            LOGGER.error("Error inesperado: " + e);
            utilidades.mostrarAlerta("Error",
                    "ocurrio un error ",
                    "ocurrio un error porfavor intentelo de nuevo en unos minutos");
        }

    }

    @FXML
    public void guardarEvaluacion() {

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

                gestorVentanas.mostrarAlerta("Error",
                        "Debe de calcular la calificacio primero",
                        "porfavor calcule la calificacion final");
                return;
            }

            if (textoComentarios.getText().isEmpty()) {

                gestorVentanas.mostrarAlerta("Error",
                        "El campo de comentarios no puede estar vacío.",
                        "porfavor llene el campo de comentarios");
                return;
            }

            if (!verificacionEntradas.validarTextoAlfanumerico(textoComentarios.getText())) {

                gestorVentanas.mostrarAlerta("Error",
                        "Los comentarios no son válidos.",
                        "porfavor llene el campo de comentarios");
                return;
            }

            if (actualizado) {

                gestorVentanas.mostrarAlerta("Éxito",
                        "Evaluación guardada correctamente.",
                        "La evaluación ha sido guardada.");
                javafx.stage.Stage stage = (javafx.stage.Stage) textoComentarios.getScene().getWindow();
                stage.close();

            } else {

                utilidades.mostrarAlerta("Error",
                        "No se pudo guardar la evaluación.",
                        "Por favor, inténtelo de nuevo más tarde.");
                LOGGER.warn("No se pudo guardar la evaluación.");
            }

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            utilidades.mostrarAlerta("Error",
                    "ocurrio un error ",
                    "ocurrio un error porfavor intentelo de nuevo en unos minutos");
            LOGGER.error("Error inesperado: " + e);
        }
    }
}
