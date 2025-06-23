package GUI.menuusarios;

import GUI.ControladorInicioDeSesionGUI;
import GUI.gestioncronogramaactividades.ControladorDetallesCronogramaActividadesGUI;
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
import logica.ManejadorExcepciones;
import logica.interfaces.IGestorAlertas;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import static GUI.ControladorRegistrarAutoevaluacionGUI.idAutoevaluacion;
import static GUI.ControladorRegistrarReporteMensualGUI.idReporte;
import static java.sql.Types.NULL;

public class ControladorMenuEstudianteGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorMenuEstudianteGUI.class);

    @FXML
    private Button botonRegistrarAutoevaluacion;

    @FXML
    private Button botonConsultarAutoevaluacion;

    private String matricula = ControladorInicioDeSesionGUI.matricula;

    Utilidades gestorVentanas = new Utilidades();
    IGestorAlertas utilidades = new Utilidades();
    ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, logger);

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

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al verificar autoevaluación registrada: " + e);
            gestorVentanas.mostrarAlerta(
                    "Error",
                    "Ocurrió un error inesperado al verificar la autoevaluación.",
                    "Por favor, contacta al administrador si el problema persiste."
            );
        }
    }

    @FXML
    public void abrirEditarPerfilEstudiante() {

        if (!verificarAsignacionProyecto()) {
            return;
        }

        gestorVentanas.mostrarVentana("/EditarPerfilEstudianteGUI.fxml");
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

            manejadorExcepciones.manejarIOException(e);

        }
    }

    public void eliminarEvaluacion() {

        AutoevaluacionDAO autoevaluacionDAO = new AutoevaluacionDAO();
        AutoevaluacionContieneDAO autoevaluacionContieneDAO = new AutoevaluacionContieneDAO();

        try {

            autoevaluacionContieneDAO.eliminarCriteriosDefinitivamentePorIdAutoevaluacion(idAutoevaluacion);
            autoevaluacionDAO.eliminarAutoevaluacionDefinitivamentePorID(idAutoevaluacion);


        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al eliminar la evaluación: " + e);
            gestorVentanas.mostrarAlerta(
                    "Error",
                    "Ocurrió un error inesperado al eliminar la evaluación.",
                    "Por favor, contacta al administrador si el problema persiste."
            );

        }
    }

    public void eliminarReporte() {

        ReporteDAO reporteDAO = new ReporteDAO();

        try {

            reporteDAO.eliminarReporte(idReporte);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al eliminar el reporte: " + e);
            gestorVentanas.mostrarAlerta(
                    "Error",
                    "Ocurrió un error inesperado al eliminar el reporte.",
                    "Por favor, contacta al administrador si el problema persiste."
            );

        }

    }
    @FXML
    public void abrirConsultarAutoevaluacion() {

        if (!verificarAsignacionProyecto()) {
            return;
        }

        gestorVentanas.mostrarVentana("/ConsultarAutoevaluacionGUI.fxml");


    }

    @FXML
    public void abrirConsultarEvaluacionesEstudiante() {

        if (!verificarAsignacionProyecto()) {
            return;
        }

        gestorVentanas.mostrarVentana("/ConsultarEvaluacionesEstudianteGUI.fxml");

    }

    @FXML
    public void abrirRegistrarReporteMensual() {

        if (!verificarAsignacionProyecto() || !verificarExistenciaCronogramaActividades()) {

            return;
        }

        try {

            ReporteDAO reporteDAO = new ReporteDAO();

            int mesActual = LocalDate.now().getMonthValue();
            int añoActual = LocalDate.now().getYear();

            if (reporteDAO.existeReporteEnMes(matricula, mesActual, añoActual)) {
                gestorVentanas.mostrarAlerta(
                        "Reporte Mensual",
                        "Ya has registrado un reporte mensual este mes.",
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

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al abrir la ventana de reporte mensual: " + e);
            gestorVentanas.mostrarAlerta(
                    "Error",
                    "Ocurrió un error inesperado al abrir la ventana de reporte mensual.",
                    "Por favor, contacta al administrador si el problema persiste."
            );
        }
    }

    private boolean verificarExistenciaCronogramaActividades() {

        CronogramaActividadesDAO cronogramaActividadesDAO = new CronogramaActividadesDAO();

        try {

            CronogramaActividadesDTO cronograma = cronogramaActividadesDAO.buscarCronogramaPorMatricula(matricula);

            if (cronograma == null || cronograma.getIDCronograma() == NULL) {

                gestorVentanas.mostrarAlerta(
                        "Cronograma de Actividades",
                        "No tienes un cronograma de actividades registrado.",
                        "Debes registrar un cronograma de actividades antes de poder registrar un reporte mensual."
                );

                return false;

            }

            return true;

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            return false;

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            return false;

        } catch (Exception e) {

            logger.error("Error inesperado al verificar cronograma de actividades: " + e);
            gestorVentanas.mostrarAlerta(
                    "Error",
                    "Ocurrió un error inesperado al verificar el cronograma de actividades.",
                    "Por favor, contacta al administrador si el problema persiste."
            );
            return false;

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

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al abrir la ventana de registro de cronograma de actividades: " + e);
            gestorVentanas.mostrarAlerta(
                    "Error",
                    "Ocurrió un error inesperado al abrir la ventana de registro de cronograma de actividades.",
                    "Por favor, contacta al administrador si el problema persiste."
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

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al abrir los detalles de asignación del proyecto: " + e);
            gestorVentanas.mostrarAlerta(
                    "Error",
                    "Ocurrió un error inesperado al abrir los detalles de asignación del proyecto.",
                    "Por favor, contacta al administrador si el problema persiste."
            );
        }
    }


    private boolean verificarAsignacionProyecto() {

        EstudianteDAO estudianteDAO = new EstudianteDAO();
        boolean proyectoAsignado = false;

        try {

            EstudianteDTO estudianteDTO = estudianteDAO.buscarEstudiantePorMatricula(matricula);

            if (estudianteDTO.getIdProyecto() == NULL) {

                gestorVentanas.mostrarAlerta(
                        "Error",
                        "No estás asignado a un proyecto.",
                        "Por favor, contacta al administrador para más información."
                );

                proyectoAsignado = false;

            } else {

                proyectoAsignado = true;

            }

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            proyectoAsignado = false;

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            proyectoAsignado = false;

        } catch (Exception e) {

            logger.error("Error inesperado al verificar asignación de proyecto: " + e);
            gestorVentanas.mostrarAlerta(
                    "Error",
                    "Ocurrió un error al verificar tu asignación de proyecto.",
                    "Por favor, intenta nuevamente más tarde."
            );
            proyectoAsignado = false;
        }

        return proyectoAsignado;
    }

    public boolean verificarCronogramaActividades() {

        CronogramaActividadesDAO cronogramaActividadesDAO = new CronogramaActividadesDAO();
        boolean cronogramaRegistrado = false;

        try {

            CronogramaActividadesDTO cronograma = cronogramaActividadesDAO.buscarCronogramaPorMatricula(matricula);

            if (cronograma != null && cronograma.getIDCronograma() != NULL) {

                gestorVentanas.mostrarAlerta(
                        "Cronograma de Actividades",
                        "Ya has registrado un cronograma de actividades.",
                        "No puedes registrar más de un cronograma de actividades por proyecto."
                );

                cronogramaRegistrado = false;

            } else {

                cronogramaRegistrado = true;

            }

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);
            cronogramaRegistrado = false;

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
            cronogramaRegistrado = false;

        } catch (Exception e) {

            logger.error("Error inesperado al verificar cronograma de actividades: " + e);
            gestorVentanas.mostrarAlerta(
                    "Error",
                    "Ocurrió un error inesperado al verificar el cronograma de actividades.",
                    "Por favor, contacta al administrador si el problema persiste."
            );
            cronogramaRegistrado = false;
        }

        return cronogramaRegistrado;
    }

    @FXML
    public void abrirConsultarCronograma() {

        if (!verificarAsignacionProyecto()) {
            return;
        }

        try {

            CronogramaActividadesDAO cronogramaDAO = new CronogramaActividadesDAO();
            CronogramaActividadesDTO cronograma = cronogramaDAO.buscarCronogramaPorMatricula(matricula);

            if (cronograma == null || cronograma.getIDCronograma() == NULL) {
                gestorVentanas.mostrarAlerta(
                        "Información",
                        "No hay cronograma registrado",
                        "No tienes un cronograma de actividades registrado."
                );
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetallesCronogramaActividadesGUI.fxml"));
            Parent root = loader.load();

            ControladorDetallesCronogramaActividadesGUI controlador = loader.getController();
            controlador.setMatriculaDTO(matricula);

            Stage stage = new Stage();
            stage.setTitle("Detalles del Cronograma");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error al abrir detalles del cronograma: " + e);
            gestorVentanas.mostrarAlerta(
                    "Error",
                    "No se pudo abrir los detalles del cronograma",
                    "Por favor, intente nuevamente más tarde."
            );
        }
    }
}
