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

public class ConsultarEvaluacionesEstudiante {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ConsultarEvaluacionesEstudiante.class);

    @FXML
    private VBox contenedorEvaluaciones;

    @FXML
    Label etiquetaCalificacion;

    String matricula = ControladorInicioDeSesionGUI.matricula;

    Utilidades utilidades = new Utilidades();

    public void setMatricula(String matricula) {
        this.matricula = matricula;
        System.out.println("Matricula establecida: " + matricula);
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
                logger.info("No evaluations found for student ID: " + matricula);
                return;
            }

            for (EvaluacionDTO evaluacion : listaEvaluaciones) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ObjetoEvaluacion.fxml"));
                Node objetoEvaluacion;

                try {
                    objetoEvaluacion = loader.load();
                } catch (IOException e) {
                    logger.error("Error loading ObjetoEvaluacion.fxml: " + e.getMessage(), e);
                    continue;
                }

                ControladorObjetoEvaluacion controlador = loader.getController();
                String nombreEvaluador = obtenerNombreEvaluador(evaluacion.getNumeroDePersonal());
                controlador.setDatosEvaluacion(nombreEvaluador, String.valueOf(evaluacion.getCalificacionFinal()), evaluacion.getIDEvaluacion());

                contenedorEvaluaciones.getChildren().add(objetoEvaluacion);
            }
        } catch (SQLException | IOException e) {
            utilidades.mostrarAlerta("Error al cargar las evaluaciones", "No se pudieron cargar las evaluaciones del estudiante. Por favor, inténtelo de nuevo más tarde.", "Error de conexión");
            logger.error("Error al cargar las evaluaciones del estudiante: " + e.getMessage(), e);
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

            utilidades.mostrarAlerta("Error al calcular la calificación final", "No se pudieron obtener las evaluaciones del estudiante.", "Error de conexión");
            logger.error("Error al calcular la calificación final: " + e);

        } catch (IOException e) {
            utilidades.mostrarAlerta("Error de entrada/salida", "Ocurrió un error al intentar cargar los datos del estudiante.", "Error de conexión");
            logger.error("Error de entrada/salida al calcular la calificación final: " +  e);
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
            utilidades.mostrarAlerta("Error al obtener el nombre del evaluador", "No se pudo obtener el nombre del evaluador. Por favor, inténtelo de nuevo más tarde.", "Error de conexión");
            logger.error("Error al obtener el nombre del evaluador: " + e.getMessage(), e);
        } catch (IOException e) {
            utilidades.mostrarAlerta("Error de entrada/salida", "Ocurrió un error al intentar cargar los datos del evaluador.", "Error de conexión");
            logger.error("Error de entrada/salida al obtener el nombre del evaluador: " + e.getMessage(), e);
        }

        return nombreEvaluador;
    }


}
