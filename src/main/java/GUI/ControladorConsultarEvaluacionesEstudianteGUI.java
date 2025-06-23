package GUI;

import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import logica.DAOs.AcademicoEvaluadorDAO;
import logica.DAOs.EvaluacionDAO;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.AcademicoEvaluadorDTO;
import logica.DTOs.EvaluacionDTO;
import logica.DTOs.UsuarioDTO;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorConsultarEvaluacionesEstudianteGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorConsultarEvaluacionesEstudianteGUI.class);

    @FXML
    private VBox contenedorEvaluaciones;

    @FXML
    Label etiquetaCalificacion;

    String matricula = ControladorInicioDeSesionGUI.matricula;

    Utilidades utilidades = new Utilidades();

    ManejadorExepciones manejadorExepciones = new ManejadorExepciones();

    public void setMatricula(String matricula) {

        this.matricula = matricula;
        cargarEvaluaciones();

    }

    @FXML
    public void initialize() {

        if (matricula != null && !matricula.isBlank() ) {
            cargarEvaluaciones();
        }

    }

    private void cargarEvaluaciones() {

        EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
        double calificacionFinal = calcularCalificacionFinal();
        etiquetaCalificacion.setText(String.valueOf(calificacionFinal));

        try {
            List<EvaluacionDTO> listaEvaluaciones = evaluacionDAO.listarEvaluacionesPorIdEstudiante(matricula);

            if (listaEvaluaciones == null || listaEvaluaciones.isEmpty()) {

                logger.info("No se encontraron evaluaciones con la matricula: " + matricula);
                utilidades.mostrarAlerta("No hay evaluaciones", "No se encontraron evaluaciones para el estudiante con matrícula: "
                        + matricula, "Información");
                return;
            }

            for (EvaluacionDTO evaluacion : listaEvaluaciones) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ObjetoEvaluacion.fxml"));
                Node objetoEvaluacion;

                try {

                    objetoEvaluacion = loader.load();

                } catch (IOException e) {

                    manejadorExepciones.manejarIOException(e, logger, utilidades);
                    continue;

                } catch (Exception e) {

                    logger.error("Error inesperado al cargar el objeto de evaluación: " + e);
                    utilidades.mostrarAlerta("Error inesperado", "Ocurrió un error al cargar las evaluaciones.", "Error");
                    continue;
                }

                ControladorObjetoEvaluacion controlador = loader.getController();
                String nombreEvaluador = obtenerNombreEvaluador(evaluacion.getNumeroDePersonal());
                controlador.setDatosEvaluacion(nombreEvaluador, String.valueOf(evaluacion.getCalificacionFinal()), evaluacion.getIDEvaluacion());

                contenedorEvaluaciones.getChildren().add(objetoEvaluacion);
            }

        } catch (SQLException e) {

            manejadorExepciones.manejarSQLException(e, logger, utilidades);


        } catch (IOException e) {

            manejadorExepciones.manejarIOException(e, logger, utilidades);

        } catch (Exception e) {

            logger.error("Error inesperado al cargar las evaluaciones: " + e);
            utilidades.mostrarAlerta("Error inesperado", "Ocurrió un error al cargar las evaluaciones.", "Error");
        }
    }

    public double calcularCalificacionFinal() {
        EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
        double calificacionFinal = 0.0;

        try {

            List<EvaluacionDTO> listaEvaluaciones = evaluacionDAO.listarEvaluacionesPorIdEstudiante(matricula);

            if (listaEvaluaciones == null || listaEvaluaciones.isEmpty()) {
                logger.info("No se encontraron evalauciones " + matricula);
                return 0.0;
            }

            double sumaCalificaciones = 0.0;
            for (EvaluacionDTO evaluacion : listaEvaluaciones) {
                sumaCalificaciones += evaluacion.getCalificacionFinal();
            }


            calificacionFinal = sumaCalificaciones / listaEvaluaciones.size();

        } catch (SQLException e) {

            manejadorExepciones.manejarSQLException(e, logger, utilidades);

        } catch (IOException e) {

            manejadorExepciones.manejarIOException(e, logger, utilidades);

        } catch (Exception e) {

            logger.error("Error inesperado al calcular la calificación final: " + e);
            utilidades.mostrarAlerta("Error inesperado", "Ocurrió un error al calcular la calificación final.", "Error");

        }

        return calificacionFinal;
    }


    private String obtenerNombreEvaluador(int numeroPersonal) {

        AcademicoEvaluadorDAO academicoEvaluadorDAO = new AcademicoEvaluadorDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        String nombreEvaluador = "";

        try {

            AcademicoEvaluadorDTO evaluador = academicoEvaluadorDAO.buscarAcademicoEvaluadorPorNumeroDePersonal(numeroPersonal);
            int idUsuario = evaluador.getIdUsuario();
            UsuarioDTO usuario = usuarioDAO.buscarUsuarioPorID(idUsuario);
            nombreEvaluador = usuario.getNombre() + " " + usuario.getApellido();

        } catch (SQLException e) {

            manejadorExepciones.manejarSQLException(e, logger, utilidades);

        } catch (IOException e) {

            manejadorExepciones.manejarIOException(e, logger, utilidades);

        } catch (Exception e) {

            logger.error("Error inesperado al obtener el nombre del evaluador: " + e);
            utilidades.mostrarAlerta("Error inesperado", "Ocurrió un error al obtener el nombre del evaluador.", "Error");

        }

        return nombreEvaluador;
    }


}
