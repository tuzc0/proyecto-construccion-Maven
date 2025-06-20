package GUI.menuusarios;

import GUI.ControladorInicioDeSesionGUI;
import GUI.gestioncronogramaactividades.ControladorRegistroCronogramaActividadesGUI;
import GUI.gestionproyecto.asignacionproyecto.ControladorDetallesAsignacionProyectoGUI;
import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logica.DAOs.*;
import logica.DTOs.AutoevaluacionDTO;
import logica.DTOs.CronogramaActividadesDTO;
import logica.DTOs.EstudianteDTO;
import logica.DTOs.ProyectoDTO;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

import static GUI.ControladorRegistrarAutoevaluacionGUI.idAutoevaluacion;
import static GUI.ControladorRegistrarReporteMensualGUI.idReporte;
import static java.sql.Types.NULL;

public class ControladorMenuEstudianteGUI {


    Utilidades utilidades = new Utilidades();
    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorMenuEstudianteGUI.class);

    @FXML
    private Button botonRegistrarAutoevaluacion;

    @FXML
    private Button botonConsultarAutoevaluacion;

    private String matricula = ControladorInicioDeSesionGUI.matricula;


    @FXML
    public void initialize() {

        verificarAutoevaluacionRegistrada();
    }

    private void verificarAutoevaluacionRegistrada() {

        if (!verificarAsignacionProyecto()) {
            return;
        }

        AutoevaluacionDAO autoevaluacionDAO = new AutoevaluacionDAO();

        try {

            AutoevaluacionDTO autoevaluacion = autoevaluacionDAO.buscarAutoevaluacionPorMatricula(matricula);

            if (autoevaluacion.getIDAutoevaluacion() == -1) {

                botonRegistrarAutoevaluacion.setDisable(false);
                botonConsultarAutoevaluacion.setDisable(true);

            } else {

                botonConsultarAutoevaluacion.setDisable(false);
                botonRegistrarAutoevaluacion.setDisable(true);
            }
        } catch (SQLException e) {

            logger.error("Error de SQL al verificar autoevaluación: " + e);

        } catch (IOException e) {

            logger.error("Error de IO al verificar autoevaluación: " + e);

        } catch (Exception e) {

            logger.error("Error inesperado al verificar autoevaluación: " + e);
        }
    }

    @FXML
    public void abrirEditarPerfilEstudiante() {

        if (!verificarAsignacionProyecto()) {
            return;
        }

        utilidades.mostrarVentana("/EditarPerfilEstudianteGUI.fxml");
    }

    @FXML
    public void abrirRegistrarAutoevaluacion() {

        if (!verificarAsignacionProyecto()) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegistrarAutoevaluacionGUI.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Registrar Autoevaluación");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setOnHidden(event -> {

                verificarAutoevaluacionRegistrada();
            });

            stage.setOnCloseRequest(event -> {
                eliminarEvaluacion();
            });

            stage.showAndWait();

        } catch (IOException e) {

            logger.error("Error al abrir la ventana RegistrarAutoevaluacionGUI: " + e);
        }
    }

    public void eliminarEvaluacion() {

        AutoevaluacionDAO autoevaluacionDAO = new AutoevaluacionDAO();
        AutoevaluacionContieneDAO autoevaluacionContieneDAO = new AutoevaluacionContieneDAO();

        try {

            autoevaluacionContieneDAO.eliminarCriteriosDefinitivamentePorIdAutoevaluacion(idAutoevaluacion);
            autoevaluacionDAO.eliminarAutoevaluacionDefinitivamentePorID(idAutoevaluacion);


        } catch (SQLException e) {

            logger.error("Error de SQL al eliminar la evaluación: " + e);

        } catch (IOException e) {

            logger.error("Error de IO al eliminar la evaluación: " + e);

        } catch (Exception e) {

            logger.error("Error inesperado al eliminar la evaluación: " + e);

        }
    }

    public void eliminarReporte() {

        ReporteDAO reporteDAO = new ReporteDAO();

        try {

            reporteDAO.eliminarReporte(idReporte);

        } catch (SQLException e) {

            logger.error("Error de SQL al eliminar el reporte: " + e);

        } catch (IOException e) {

            logger.error("Error de IO al eliminar el reporte: " + e);

        } catch (Exception e) {

            logger.error("Error inesperado al eliminar el reporte: " + e);
        }

    }
    @FXML
    public void abrirConsultarAutoevaluacion() {

        if (!verificarAsignacionProyecto()) {
            return;
        }

        utilidades.mostrarVentana("/ConsultarAutoevaluacionGUI.fxml");


    }

    @FXML
    public void abrirConsultarEvaluacionesEstudiante() {

        if (!verificarAsignacionProyecto()) {
            return;
        }

        utilidades.mostrarVentana("/ConsultarEvaluacionesEstudianteGUI.fxml");

    }

    @FXML
    public void abrirRegistrarReporteMensual() {

        if (!verificarAsignacionProyecto()) {
            return;
        }

        try {
            ReporteDAO reporteDAO = new ReporteDAO();

            int mesActual = java.time.LocalDate.now().getMonthValue();
            int añoActual = java.time.LocalDate.now().getYear();

            if (reporteDAO.existeReporteEnMes(matricula, mesActual, añoActual)) {
                utilidades.mostrarAlerta("Reporte Mensual", "Ya has registrado un reporte mensual este mes.",
                        "No puedes registrar más de un reporte mensual por mes.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegistrarReporteMensualGUI.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Registrar Reporte Mensual");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setOnCloseRequest(event -> eliminarReporte());

            stage.showAndWait();
        } catch (SQLException | IOException e) {
            logger.error("Error al verificar reporte mensual: " + e);
            utilidades.mostrarAlerta("Error", "No se pudo verificar el reporte mensual.", "Por favor, intenta nuevamente más tarde.");
        }
    }

    @FXML
    public void abrirRegistrarCronogramaActividades() {

        if (!verificarAsignacionProyecto()) {
            return;
        }

        if (!verificarCronogramaActividades()) {
            return;
        }


        try {
            FXMLLoader cargarVentana = new FXMLLoader(getClass().getResource("/RegistroCronogramaActividadesGUI.fxml"));
            Parent root = cargarVentana.load();

            ControladorRegistroCronogramaActividadesGUI controlador = cargarVentana.getController();
            controlador.setDatosIniciales(matricula);

            Stage stage = new Stage();
            stage.setTitle("Registrar Cronograma de Actividades");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            logger.error("Error al abrir la ventana RegistrarCronogramaActividadesGUI: " + e);
            utilidades.mostrarAlerta(
                    "Error",
                    "Ocurrió un error al intentar abrir la ventana",
                    "Por favor, inténtelo de nuevo más tarde."
            );
        }
    }

    @FXML
    public void abrirDetallesAsignacionComoEstudiante() {

        if (!verificarAsignacionProyecto()) {
            return;
        }

        EstudianteDAO estudianteDAO = new EstudianteDAO();
        ProyectoDAO proyectoDAO = new ProyectoDAO();

        try {

            EstudianteDTO estudianteDTO = estudianteDAO.buscarEstudiantePorMatricula(matricula);
            ProyectoDTO proyectoDTO = proyectoDAO.buscarProyectoPorID(estudianteDTO.getIdProyecto());

            FXMLLoader cargarVentana =
                    new FXMLLoader(getClass().getResource("/DetallesAsignacionProyectoGUI.fxml"));
            Parent nodoRaiz = cargarVentana.load();

            ControladorDetallesAsignacionProyectoGUI controlador = cargarVentana.getController();
            controlador.inicializarDatos(proyectoDTO, estudianteDTO, null);
            controlador.setEsVistaDeCoordinador(false);

            Stage escenario = new Stage();
            escenario.setTitle("Detalles Asignación");
            escenario.setScene(new Scene(nodoRaiz));
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();

        } catch (SQLException e) {

            logger.error("Error en la base de datos al buscar el proyecto del estudiante: " + e);
            utilidades.mostrarAlerta(
                    "Error",
                    "No se pudo abrir el proyecto.",
                    "Contacta al administrador si el problema persiste."
            );

        } catch (IOException e) {

            logger.error("Error al cargar la ventana de detalles del proyecto para el estudiante: " + e);
            utilidades.mostrarAlerta(
                    "Error",
                    "Ocurrio un error al cargar la ventana",
                    "Contacte al administrador si el problema persiste"
            );
        }
    }


    private boolean verificarAsignacionProyecto() {

        EstudianteDAO estudianteDAO = new EstudianteDAO();
        try {

            EstudianteDTO estudianteDTO = estudianteDAO.buscarEstudiantePorMatricula(matricula);
            if (estudianteDTO.getIdProyecto() == NULL) {
                utilidades.mostrarAlerta(
                        "Error",
                        "No estás asignado a un proyecto.",
                        "Por favor, contacta al administrador para más información."
                );
                return false;
            }
            return true;
        } catch (SQLException e) {
            logger.error("Error de SQL al verificar asignación de proyecto: " + e);
        } catch (IOException e) {
            logger.error("Error de IO al verificar asignación de proyecto: " + e);
        }
        utilidades.mostrarAlerta(
                "Error",
                "Ocurrió un error al verificar tu asignación de proyecto.",
                "Por favor, intenta nuevamente más tarde."
        );
        return false;
    }

    public boolean verificarCronogramaActividades() {
        CronogramaActividadesDAO cronogramaActividadesDAO = new CronogramaActividadesDAO();
        try {

            CronogramaActividadesDTO cronograma = cronogramaActividadesDAO.buscarCronogramaPorMatricula(matricula);
            if (cronograma != null && cronograma.getIDCronograma() != NULL) {
                utilidades.mostrarAlerta(
                        "Cronograma de Actividades",
                        "Ya has registrado un cronograma de actividades.",
                        "No puedes registrar más de un cronograma de actividades por proyecto."
                );
                return false;
            }
            return true;
        } catch (SQLException e) {
            logger.error("Error al verificar cronograma de actividades: " + e);
            utilidades.mostrarAlerta(
                    "Error",
                    "No se pudo verificar el cronograma de actividades.",
                    "Por favor, intenta nuevamente más tarde."
            );
            return false;
        } catch (IOException e) {
            logger.error("Error de IO al verificar cronograma de actividades: " + e);
            utilidades.mostrarAlerta(
                    "Error",
                    "Error de entrada/salida al verificar el cronograma de actividades.",
                    "Por favor, intenta nuevamente más tarde."
            );
            return false;
        } catch (Exception e) {
            logger.error("Error inesperado al verificar cronograma de actividades: " + e);
            utilidades.mostrarAlerta(
                    "Error",
                    "Ocurrió un error inesperado al verificar el cronograma de actividades.",
                    "Por favor, contacta al administrador si el problema persiste."
            );
            return false;
        }
    }
}
