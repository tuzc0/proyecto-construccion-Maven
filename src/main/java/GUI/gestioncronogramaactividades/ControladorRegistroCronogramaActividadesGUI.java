package GUI.gestioncronogramaactividades;

import GUI.GestorHorarios;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logica.DAOs.*;
import logica.DTOs.*;
import logica.verificacion.VerificacionDeActividad;
import logica.verificacion.VerificicacionGeneral;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import GUI.utilidades.Utilidades;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ControladorRegistroCronogramaActividadesGUI {

    private static final Logger LOGGER = LogManager.getLogger(ControladorRegistroCronogramaActividadesGUI.class);

    @FXML
    private ComboBox<String> comboHoraInicioLunesHorario;
    @FXML
    private ComboBox<String> comboMinutosInicioLunesHorario;
    @FXML
    private ComboBox<String> comboHoraFinLunesHorario;
    @FXML
    private ComboBox<String> comboMinutosFinLunesHorario;
    @FXML
    private ComboBox<String> comboHoraInicioMartesHorario;
    @FXML
    private ComboBox<String> comboMinutosInicioMartesHorario;
    @FXML
    private ComboBox<String> comboHoraFinMartesHorario;
    @FXML
    private ComboBox<String> comboMinutosFinMartesHorario;
    @FXML
    private ComboBox<String> comboHoraInicioMiercolesHorario;
    @FXML
    private ComboBox<String> comboMinutosInicioMiercolesHorario;
    @FXML
    private ComboBox<String> comboHoraFinMiercolesHorario;
    @FXML
    private ComboBox<String> comboMinutosFinMiercolesHorario;
    @FXML
    private ComboBox<String> comboHoraInicioJuevesHorario;
    @FXML
    private ComboBox<String> comboMinutosInicioJuevesHorario;
    @FXML
    private ComboBox<String> comboHoraFinJuevesHorario;
    @FXML
    private ComboBox<String> comboMinutosFinJuevesHorario;
    @FXML
    private ComboBox<String> comboHoraInicioViernesHorario;
    @FXML
    private ComboBox<String> comboMinutosInicioViernesHorario;
    @FXML
    private ComboBox<String> comboHoraFinViernesHorario;
    @FXML
    private ComboBox<String> comboMinutosFinViernesHorario;
    @FXML
    private DatePicker fechaInicioActividad;
    @FXML
    private DatePicker fechaFinActividad;
    @FXML
    private CheckBox checkLunes;
    @FXML
    private CheckBox checkMartes;
    @FXML
    private CheckBox checkMiercoles;
    @FXML
    private CheckBox checkJueves;
    @FXML
    private CheckBox checkViernes;
    @FXML
    private TextField campoNombreActividad;
    @FXML
    private TextField campoDuracionActividad;
    @FXML
    private TextField campoHitosActividad;
    @FXML
    private Button botonAgregarActividad;
    @FXML
    private Button botonCancelar;
    @FXML
    private Button botonGuardar;
    @FXML
    private Label etiquetaContadorNombreActividad;
    @FXML
    private Label etiquetaContadorDuracionActividad;
    @FXML
    private Label etiquetaContadorHitosActividad;
    @FXML
    private Label etiquetaContadorActividades;

    private String matriculaEstudiante;
    private final Utilidades UTILIDADES = new Utilidades();
    private GestorHorarios gestorHorarios;

    @FXML
    public void initialize() {

        final int MAX_CARACTERES_NOMBRE_ACTIVIDAD = 255;
        final int MAX_CARACTERES_DURACION_ACTIVIDAD = 50;
        final int MAX_CARACTERES_HITOS_ACTIVIDAD = 100;

        VerificicacionGeneral verificacionGeneralUtilidad = new VerificicacionGeneral();

        verificacionGeneralUtilidad.contadorCaracteresTextField(campoNombreActividad,
                etiquetaContadorNombreActividad, MAX_CARACTERES_NOMBRE_ACTIVIDAD);
        verificacionGeneralUtilidad.contadorCaracteresTextField(campoDuracionActividad,
                etiquetaContadorDuracionActividad, MAX_CARACTERES_DURACION_ACTIVIDAD);
        verificacionGeneralUtilidad.contadorCaracteresTextField(campoHitosActividad,
                etiquetaContadorHitosActividad, MAX_CARACTERES_HITOS_ACTIVIDAD);

        gestorHorarios = new GestorHorarios();
        gestorHorarios.agregarDia(checkLunes, comboHoraInicioLunesHorario, comboMinutosInicioLunesHorario,
                comboHoraFinLunesHorario, comboMinutosFinLunesHorario);
        gestorHorarios.agregarDia(checkMartes, comboHoraInicioMartesHorario, comboMinutosInicioMartesHorario,
                comboHoraFinMartesHorario, comboMinutosFinMartesHorario);
        gestorHorarios.agregarDia(checkMiercoles, comboHoraInicioMiercolesHorario, comboMinutosInicioMiercolesHorario,
                comboHoraFinMiercolesHorario, comboMinutosFinMiercolesHorario);
        gestorHorarios.agregarDia(checkJueves, comboHoraInicioJuevesHorario, comboMinutosInicioJuevesHorario,
                comboHoraFinJuevesHorario, comboMinutosFinJuevesHorario);
        gestorHorarios.agregarDia(checkViernes, comboHoraInicioViernesHorario, comboMinutosInicioViernesHorario,
                comboHoraFinViernesHorario, comboMinutosFinViernesHorario);

        gestorHorarios.inicializarCombosHorarios();
        gestorHorarios.configurarHabilitacionPorDia();

        fechaInicioActividad.setEditable(false);
        fechaFinActividad.setEditable(false);

        botonAgregarActividad.setCursor(Cursor.HAND);
        botonCancelar.setCursor(Cursor.HAND);
        botonGuardar.setCursor(Cursor.HAND);
    }

    @FXML
    private void guardarCronograma() {

        String nombreActividad = campoNombreActividad.getText();
        String duracion = campoDuracionActividad.getText();
        String hitos = campoHitosActividad.getText();

        ActividadDTO actividadDTO = new ActividadDTO(0, nombreActividad, duracion, hitos,
                null, null, 1);

        if (!validarActividad(actividadDTO)) {
            return;
        }

        try {

            LocalDate fechaInicio = fechaInicioActividad.getValue();
            LocalDate fechaFin = fechaFinActividad.getValue();

            if (fechaInicio == null || fechaFin == null) {

                UTILIDADES.mostrarAlerta(
                        "Fechas vacías",
                        "Se necesitan ambas fechas para la actividad.",
                        "Por favor, seleccione la fecha de inicio y la fecha de fin para la actividad."
                );
                return;
            }

            if (!fechaInicio.isBefore(fechaFin)) {

                UTILIDADES.mostrarAlerta(
                        "Error en fechas",
                        "La fecha inicio debe ser anterior a la fecha fin",
                        "Por favor, corriga las fechas."
                );
                return;
            }

            if (!gestorHorarios.validarDiasSeleccionados()) {

                UTILIDADES.mostrarAlerta(
                        "Días no seleccionados",
                        "Seleccione al menos un día",
                        "Marque al menos un día en el panel derecho"
                );
                return;
            }

            List<String> erroresEnHorarios = gestorHorarios.validarHorarios();

            if (!erroresEnHorarios.isEmpty()) {

                UTILIDADES.mostrarAlerta(
                        "Error en horarios",
                        "La hora de entrada debe ser menor a salida",
                        String.join("\n", erroresEnHorarios)
                );
                return;
            }

            PeriodoDAO periodoDAO = new PeriodoDAO();
            EstudianteDAO estudianteDAO = new EstudianteDAO();

            EstudianteDTO estudianteDTO = estudianteDAO.buscarEstudiantePorMatricula(matriculaEstudiante);
            PeriodoDTO periodoDTO = periodoDAO.mostrarPeriodoActual();
            int idCronograma = 0;
            int idProyecto = estudianteDTO.getIdProyecto();
            int estadoActivo = 1;
            int noAsignado = -1;

            if (idProyecto == noAsignado) {

                UTILIDADES.mostrarAlerta(
                        "Error",
                        "No se le ha asignado a proyecto",
                        "Por favor, espere a ser asignado");
                return;
            }

            CronogramaActividadesDTO cronogramaDTO = new CronogramaActividadesDTO(
                    idCronograma,
                    matriculaEstudiante,
                    idProyecto,
                    periodoDTO.getIDPeriodo(),
                    estadoActivo
            );

            if (!validarCronograma(cronogramaDTO)) {

                return;
            }

            if (!insertarHorarios(estudianteDTO.getIdProyecto())) {

                UTILIDADES.mostrarAlerta(
                        "Error",
                        "No se pudo insertar el horarios",
                        "Por favor, intentelo de nuevo más tarde o contacte al administrador."
                );
                return;
            }

            ActividadDAO actividadDAO = new ActividadDAO();
            ActividadDTO actividadPrincipal = new ActividadDTO(
                    0,
                    campoNombreActividad.getText(),
                    campoDuracionActividad.getText(),
                    campoHitosActividad.getText(),
                    Date.valueOf(fechaInicioActividad.getValue()),
                    Date.valueOf(fechaFinActividad.getValue()),
                    estadoActivo
            );

            int idActividadPrincipal = actividadDAO.crearNuevaActividad(actividadPrincipal);
            int actividadNoInsertada = -1;

            if (idActividadPrincipal == actividadNoInsertada) {

                UTILIDADES.mostrarAlerta(
                        "Error",
                        "No se pudo crear la actividad",
                        "Por favor, inténtelo de nuevo o contacte al administrador."
                );
                return;
            }

            CronogramaActividadesDAO cronogramaDAO = new CronogramaActividadesDAO();
            CronogramaContieneDAO cronogramaContieneDAO = new CronogramaContieneDAO();

            int idCronogramaCreado = cronogramaDAO.crearNuevoCronogramaDeActividades(cronogramaDTO);

            for (ActividadDTO actividadSecundaria : actividadesSecundarias) {

                int idActividadSecundaria = actividadDAO.crearNuevaActividad(actividadSecundaria);
                int noCreada = -1;

                if (idActividadSecundaria == noCreada) {

                    UTILIDADES.mostrarAlerta(
                            "Error",
                            "No se pudo crear una actividad secundaria.",
                            "Por favor, intentelo de nuevo más tarde o contacte al administrador.");
                    return;
                }

                String mesActividad = obtenerMes(actividadSecundaria.getFechaInicio().toLocalDate());

                CronogramaContieneDTO contieneSecundaria = new CronogramaContieneDTO(
                        idCronogramaCreado,
                        idActividadSecundaria,
                        mesActividad,
                        1
                );

                if (!cronogramaContieneDAO.insertarCronogramaContiene(contieneSecundaria)) {

                    UTILIDADES.mostrarAlerta(
                            "Error",
                            "Una actividad secundaria no se pudo añadir al cronograma.",
                            "Por favor intentelo de nuevo más tarde o contacte al administrador.");
                    return;
                }
            }

            UTILIDADES.mostrarAlerta(
                    "Éxito",
                    "El cronograma fue registrado con éxito.",
                    ""
            );

        } catch (SQLException e) {

            LOGGER.error("Error al guardar cronograma dentro de SQL: ", e);
            UTILIDADES.mostrarAlerta(
                    "Error durante el registro.",
                    "Ocurrio un error durante el registro.",
                    "Por favor, intentelo de nuevo más tarde o contacte al administrador.");

        } catch (IOException e) {

            LOGGER.error("Error durante la ejecución: ", e);
            UTILIDADES.mostrarAlerta(
                    "Error durante registro.",
                    "Ocurrio al momento del registro.",
                    "Por favor, intentelo de nuevo más tarde o contacte al administrador.");
        }
    }

    private boolean insertarHorarios(int idProyecto) throws SQLException, IOException {

        HorarioProyectoDAO horarioDAO = new HorarioProyectoDAO();

        boolean horariosInsertados = false;

        for (Map.Entry<CheckBox, List<ComboBox<String>>> diasSeleccionados : gestorHorarios.obtenerDiasConHorarios().entrySet()) {

            CheckBox diaSeleccionado = diasSeleccionados.getKey();
            List<ComboBox<String>> horarios = diasSeleccionados.getValue();

            if (diaSeleccionado.isSelected() && gestorHorarios.verificarHorarioDia(horarios)) {

                String diaSemana = gestorHorarios.obtenerNombreDia(diaSeleccionado);
                int horaInicio = Integer.parseInt(horarios.get(0).getValue());
                int minutoInicio = Integer.parseInt(horarios.get(1).getValue());
                int horaFin = Integer.parseInt(horarios.get(2).getValue());
                int minutoFin = Integer.parseInt(horarios.get(3).getValue());
                int idHorario = 0;
                String idEstudiante = matriculaEstudiante;

                Time horaInicioSQL = Time.valueOf(String.format("%02d:%02d:00", horaInicio, minutoInicio));
                Time horaFinSQL = Time.valueOf(String.format("%02d:%02d:00", horaFin, minutoFin));

                HorarioProyectoDTO horarioDTO = new HorarioProyectoDTO(
                        idHorario,
                        idProyecto,
                        diaSemana,
                        horaInicioSQL,
                        horaFinSQL,
                        idEstudiante
                );

                horariosInsertados = horarioDAO.crearNuevoHorarioProyecto(horarioDTO);
            }
        }

        return horariosInsertados;
    }

    private boolean validarActividad(ActividadDTO actividadDTO) {

        VerificacionDeActividad verificacionActividad = new VerificacionDeActividad();

        List<String> camposVacios = verificacionActividad.validarCamposVacios(
                actividadDTO.getNombre(),
                actividadDTO.getDuracion(),
                actividadDTO.getHitos()
        );

        if (!camposVacios.isEmpty()) {

            UTILIDADES.mostrarAlerta(
                    "Campos vacíos",
                    "Complete los campos requeridos",
                    String.join("\n", camposVacios)
            );
            return false;
        }

        List<String> datosInvalidos = verificacionActividad.validarCamposInvalidos(
                actividadDTO.getNombre(),
                actividadDTO.getDuracion(),
                actividadDTO.getHitos()
        );

        if (!datosInvalidos.isEmpty()) {

            UTILIDADES.mostrarAlerta(
                    "Datos inválidos",
                    "Verifique los campos",
                    String.join("\n", datosInvalidos)
            );
            return false;
        }

        return true;
    }

    private boolean validarCronograma(CronogramaActividadesDTO cronogramaActividadesDTO) {

        if (cronogramaActividadesDTO.getIdPeriodo() == -1) {

            UTILIDADES.mostrarAlerta(
                    "Error",
                    "No hay periodo activo",
                    "Por favor, espere a que inicie un periodo"
            );
            return false;
        }

        if (cronogramaActividadesDTO.getMatriculaEstudiante() == null || cronogramaActividadesDTO.getMatriculaEstudiante().isEmpty()) {

            UTILIDADES.mostrarAlerta(
                    "Error",
                    "Matrícula de estudiante no válida",
                    "Por favor, verifique la matrícula del estudiante"
            );
            return false;
        }

        return true;
    }

    private List<ActividadDTO> actividadesSecundarias = new ArrayList<>();

    @FXML
    private void agregarActividad() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegistrarActividadGUI.fxml"));
            Parent root = loader.load();

            ControladorRegistroActividadGUI controlador = loader.getController();
            controlador.setDatosIniciales(matriculaEstudiante);

            controlador.setControladorPrincipal(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Registrar Actividad");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {

            LOGGER.error("No se pudo cargar la ventana de registro actividad:" + e);
            UTILIDADES.mostrarAlerta(
                    "Error",
                    "Ocurrió un error al intentar abrir la ventana",
                    "Por favor, inténtelo de nuevo más tarde."
            );
        }
    }

    public void agregarActividadSecundaria(ActividadDTO actividad) {

        this.actividadesSecundarias.add(actividad);
        etiquetaContadorActividades.setText("Actividades: " + actividadesSecundarias.size());
    }

    private String obtenerMes(LocalDate fecha) {

        return fecha.getMonth().toString();
    }

    @FXML
    private void cancelar() {
        ((Stage) botonCancelar.getScene().getWindow()).close();
    }

    public void setDatosIniciales(String matriculaEstudiante) {

        this.matriculaEstudiante = matriculaEstudiante;
    }
}