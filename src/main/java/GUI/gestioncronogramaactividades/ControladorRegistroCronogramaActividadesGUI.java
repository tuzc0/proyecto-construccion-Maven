package GUI.gestioncronogramaactividades;

import GUI.GestorHorarios;
import GUI.utilidades.Utilidades;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import logica.DAOs.*;
import logica.DTOs.*;
import logica.ManejadorExcepciones;
import logica.interfaces.IGestorAlertas;
import logica.verificacion.ValidarFechas;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class ControladorRegistroCronogramaActividadesGUI {

    private static final Logger LOGGER =
            LogManager.getLogger(ControladorRegistroCronogramaActividadesGUI.class);

    @FXML private ComboBox<String> comboHoraInicioLunesHorario;
    @FXML private ComboBox<String> comboMinutosInicioLunesHorario;
    @FXML private ComboBox<String> comboHoraFinLunesHorario;
    @FXML private ComboBox<String> comboMinutosFinLunesHorario;
    @FXML private ComboBox<String> comboHoraInicioMartesHorario;
    @FXML private ComboBox<String> comboMinutosInicioMartesHorario;
    @FXML private ComboBox<String> comboHoraFinMartesHorario;
    @FXML private ComboBox<String> comboMinutosFinMartesHorario;
    @FXML private ComboBox<String> comboHoraInicioMiercolesHorario;
    @FXML private ComboBox<String> comboMinutosInicioMiercolesHorario;
    @FXML private ComboBox<String> comboHoraFinMiercolesHorario;
    @FXML private ComboBox<String> comboMinutosFinMiercolesHorario;
    @FXML private ComboBox<String> comboHoraInicioJuevesHorario;
    @FXML private ComboBox<String> comboMinutosInicioJuevesHorario;
    @FXML private ComboBox<String> comboHoraFinJuevesHorario;
    @FXML private ComboBox<String> comboMinutosFinJuevesHorario;
    @FXML private ComboBox<String> comboHoraInicioViernesHorario;
    @FXML private ComboBox<String> comboMinutosInicioViernesHorario;
    @FXML private ComboBox<String> comboHoraFinViernesHorario;
    @FXML private ComboBox<String> comboMinutosFinViernesHorario;
    @FXML private TableView<ActividadDTO> tablaActividades;
    @FXML private TableColumn<ActividadDTO, String> columnaNombreActividad;
    @FXML private TableColumn<ActividadDTO, Void> columnaEliminar;
    @FXML private CheckBox checkLunes;
    @FXML private CheckBox checkMartes;
    @FXML private CheckBox checkMiercoles;
    @FXML private CheckBox checkJueves;
    @FXML private CheckBox checkViernes;
    @FXML private Button botonCancelar;
    @FXML private Button botonGuardar;
    @FXML private Label etiquetaContadorActividades;

    private Utilidades gestorVentanas = new Utilidades();
    private IGestorAlertas gestorAlertas = new Utilidades();
    private ManejadorExcepciones manejadorExcepciones =
            new ManejadorExcepciones(gestorAlertas, LOGGER);
    private ObservableList<ActividadDTO> actividadesSecundarias =
            FXCollections.observableArrayList();

    private String matriculaEstudiante;
    private GestorHorarios gestorHorarios;

    @FXML
    public void initialize() {

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
        gestorHorarios.habilitarHorariosPorDiaSeleccionado();

        botonCancelar.setCursor(Cursor.HAND);
        botonGuardar.setCursor(Cursor.HAND);
        configurarTablaActividades();
    }

    private void configurarTablaActividades() {

        columnaNombreActividad.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        añadirBotonEliminarATabla();
        actividadesSecundarias.addListener((ListChangeListener<ActividadDTO>) change -> {
            actualizarTablaActividades();
        });
    }

    private void actualizarTablaActividades() {

        tablaActividades.getItems().setAll(actividadesSecundarias);
        etiquetaContadorActividades.setText("Actividades: " + actividadesSecundarias.size());
    }

    private void añadirBotonEliminarATabla() {

        Callback<TableColumn<ActividadDTO, Void>, TableCell<ActividadDTO, Void>> fabricadorCeldas =
                columnaEliminar -> new TableCell<>() {

            private final Button botonEliminar = new Button("Eliminar");

            {
                botonEliminar.setOnAction(evento -> {

                    ActividadDTO actividadSeleccionada = getTableView().getItems().get(getIndex());

                    gestorVentanas.mostrarAlertaConfirmacion(
                            "Confirmar eliminación",
                            "¿Está seguro de que desea eliminar esta actividad?",
                            "Esta acción no se puede deshacer.",
                            () -> actividadesSecundarias.remove(actividadSeleccionada),
                            () -> {}
                    );
                });
            }

            @Override
            protected void updateItem(Void valorCelda, boolean vacio) {

                super.updateItem(valorCelda, vacio);

                if (vacio || getTableView().getItems().get(getIndex()) == null) {
                    setGraphic(null);
                } else {
                    setGraphic(botonEliminar);
                }
            }
        };

        columnaEliminar.setCellFactory(fabricadorCeldas);
    }

    private boolean guardando = false;

    @FXML
    private void guardarCronograma() {

        if (guardando) {
            return;
        }

        guardando = true;
        botonGuardar.setDisable(true);

        ValidarFechas validadorFechas = new ValidarFechas();

        try {

            if (actividadesSecundarias.isEmpty()) {
                gestorVentanas.mostrarAlerta(
                        "Error",
                        "No hay actividades registradas",
                        "Debe agregar al menos una actividad al cronograma."
                );
                return;
            }

            if (!gestorHorarios.validarDiasSeleccionados()) {

                gestorVentanas.mostrarAlerta(
                        "Error",
                        "Días no seleccionados",
                        "Seleccione al menos un día de la semana."
                );
                return;
            }

            List<String> erroresHorarios = gestorHorarios.validarHorarios();

            if (!erroresHorarios.isEmpty()) {

                gestorVentanas.mostrarAlerta(
                        "Error en horarios",
                        "La hora de inicio debe ser menor que la de fin",
                        String.join("\n", erroresHorarios)
                );
                return;
            }

            PeriodoDAO periodoDAO = new PeriodoDAO();
            EstudianteDAO estudianteDAO = new EstudianteDAO();

            EstudianteDTO estudiante = estudianteDAO.buscarEstudiantePorMatricula(matriculaEstudiante);
            PeriodoDTO periodo = periodoDAO.mostrarPeriodoActual();

            if (estudiante.getIdProyecto() == -1) {

                gestorVentanas.mostrarAlerta(
                        "Error",
                        "Estudiante no asignado a proyecto",
                        "Contacte al coordinador."
                );
                return;
            }

            CronogramaActividadesDTO cronograma = new CronogramaActividadesDTO(
                    0,
                    matriculaEstudiante,
                    estudiante.getIdProyecto(),
                    periodo.getIDPeriodo(),
                    1
            );

            CronogramaActividadesDAO cronogramaDAO = new CronogramaActividadesDAO();
            int idCronograma = cronogramaDAO.crearNuevoCronogramaDeActividades(cronograma);

            if (idCronograma == -1) {

                gestorVentanas.mostrarAlerta(
                        "Error",
                        "No sé pudo completar el registro del cronograma dentro del sistema.",
                        "Por favor, inténtelo de nuevo más tarde o contacte al administrador."
                );
                return;
            }

            boolean horarioCreado = insertarHorarios(estudiante.getIdProyecto());

            if (!horarioCreado){

                gestorVentanas.mostrarAlerta(
                        "Error",
                        "No sé pudo completar el registro del horario dentro del sistema.",
                        "Por favor, inténtelo de nuevo más tarde o contacte al administrador."
                );
                return;
            }

            CronogramaContieneDAO cronogramaContieneDAO = new CronogramaContieneDAO();
            ActividadDAO actividadDAO = new ActividadDAO();

            for (ActividadDTO actividad : actividadesSecundarias) {

                int idActividad = actividadDAO.crearNuevaActividad(actividad);
                String mes = actividad.getFechaInicio().toLocalDate().getMonth().toString();

                CronogramaContieneDTO cronogramaContieneDTO = new CronogramaContieneDTO(
                        idCronograma,
                        idActividad,
                        mes,
                        1
                );

                cronogramaContieneDAO.insertarCronogramaContiene(cronogramaContieneDTO);
            }

            gestorVentanas.mostrarAlerta(
                    "Éxito",
                    "Cronograma registrado",
                    ""
            );
            ((Stage) botonGuardar.getScene().getWindow()).close();

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            gestorVentanas.mostrarAlerta(
                    "Error inesperado",
                    "No se pudo registrar el cronograma de actividades",
                    "Por favor, intente nuevamente más tarde."
            );
            LOGGER.error("Error inesperado al registrar el cronograma de actividades: " + e);

        }
    }

    private boolean insertarHorarios(int idProyecto) throws SQLException, IOException {

        HorarioProyectoDAO horarioDAO = new HorarioProyectoDAO();
        boolean horarioInsertadoConExito = false;

        for (Map.Entry<CheckBox, List<ComboBox<String>>> entry : gestorHorarios.obtenerDiasConHorarios().entrySet()) {

            CheckBox dia = entry.getKey();
            List<ComboBox<String>> combos = entry.getValue();

            if (dia.isSelected() && gestorHorarios.verificarHorarioDia(combos)) {

                String nombreDia = gestorHorarios.obtenerNombreDia(dia);

                int hInicio = Integer.parseInt(combos.get(0).getValue());
                int mInicio = Integer.parseInt(combos.get(1).getValue());
                int hFin = Integer.parseInt(combos.get(2).getValue());
                int mFin = Integer.parseInt(combos.get(3).getValue());

                Time inicio = Time.valueOf(String.format("%02d:%02d:00", hInicio, mInicio));
                Time fin = Time.valueOf(String.format("%02d:%02d:00", hFin, mFin));

                HorarioProyectoDTO horario = new HorarioProyectoDTO(
                        0,
                        idProyecto,
                        nombreDia,
                        inicio,
                        fin,
                        matriculaEstudiante
                );

                horarioInsertadoConExito = horarioDAO.crearNuevoHorarioProyecto(horario);
            }
        }

        return horarioInsertadoConExito;
    }

    @FXML
    private void agregarActividad() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegistrarActividadGUI.fxml"));
            Parent root = loader.load();

            ControladorRegistroActividadGUI controlador = loader.getController();
            controlador.setControladorPrincipal(this);
            controlador.setDatosIniciales(matriculaEstudiante);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Registrar Actividad");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            LOGGER.error("Error al abrir la ventana de registro de actividad: " + e);
            gestorVentanas.mostrarAlerta(
                    "Error",
                    "No se pudo abrir la ventana de registro de actividad",
                    "Por favor, intente nuevamente más tarde."
            );
        }
    }

    public void agregarActividadSecundaria(ActividadDTO actividad) {

        actividadesSecundarias.add(actividad);
    }

    @FXML
    private void cancelar() {

        ((Stage) botonCancelar.getScene().getWindow()).close();
    }

    public void setDatosIniciales(String matriculaEstudiante) {

        this.matriculaEstudiante = matriculaEstudiante;
    }
}
