package GUI.gestioncronogramaactividades;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import logica.DAOs.*;
import logica.DTOs.*;
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
    @FXML private DatePicker fechaInicioActividad;
    @FXML private DatePicker fechaFinActividad;
    @FXML private CheckBox checkLunes;
    @FXML private CheckBox checkMartes;
    @FXML private CheckBox checkMiercoles;
    @FXML private CheckBox checkJueves;
    @FXML private CheckBox checkViernes;
    @FXML private TextField campoNombreActividad;
    @FXML private TextField campoDuracionActividad;
    @FXML private TextField campoHitosActividad;
    @FXML private Button botonAgregarActividad;
    @FXML private Button botonCancelar;
    @FXML private Button botonGuardar;
    @FXML private VBox contenedorActividades;
    @FXML private Label contadorNombreActividad;
    @FXML private Label contadorDuracionActividad;
    @FXML private Label contadorHitosActividad;

    private String matriculaEstudiante = "S23014091";
    private final Utilidades UTILIDADES = new Utilidades();

    @FXML
    public void initialize() {

        final int MAX_CARACTERES_NOMBRE_ACTIVIDAD = 255;
        final int MAX_CARACTERES_DURACION_ACTIVIDAD = 50;
        final int MAX_CARACTERES_HITOS_ACTIVIDAD = 100;

        VerificicacionGeneral verificacionGeneralUtilidad = new VerificicacionGeneral();

        verificacionGeneralUtilidad.contadorCaracteresTextField(campoNombreActividad,
                contadorNombreActividad, MAX_CARACTERES_NOMBRE_ACTIVIDAD);
        verificacionGeneralUtilidad.contadorCaracteresTextField(campoDuracionActividad,
                contadorDuracionActividad, MAX_CARACTERES_DURACION_ACTIVIDAD);
        verificacionGeneralUtilidad.contadorCaracteresTextField(campoHitosActividad,
                contadorHitosActividad, MAX_CARACTERES_HITOS_ACTIVIDAD);

        inicializarHorario();
        configurarHabilitacionDeHorariosPorDia();

        botonAgregarActividad.setOnAction(cargarNuevaActividad -> agregarNuevaActividad());
        botonAgregarActividad.setCursor(Cursor.HAND);
        botonCancelar.setCursor(Cursor.HAND);
        botonGuardar.setCursor(Cursor.HAND);
    }

    private void inicializarHorario() {

        final int HORAS_POR_DIA = 24;
        final int MINUTOS_POR_HORA = 60;

        ObservableList<String> listaHoras = FXCollections.observableArrayList();
        ObservableList<String> listaMinutos = FXCollections.observableArrayList();

        for (int hora = 0; hora < HORAS_POR_DIA; hora++) {
            listaHoras.add(String.format("%02d", hora));
        }

        for (int minuto = 0; minuto < MINUTOS_POR_HORA; minuto++) {
            listaMinutos.add(String.format("%02d", minuto));
        }

        comboHoraInicioLunesHorario.setItems(listaHoras);
        comboHoraFinLunesHorario.setItems(listaHoras);
        comboMinutosInicioLunesHorario.setItems(listaMinutos);
        comboMinutosFinLunesHorario.setItems(listaMinutos);

        comboHoraInicioMartesHorario.setItems(listaHoras);
        comboHoraFinMartesHorario.setItems(listaHoras);
        comboMinutosInicioMartesHorario.setItems(listaMinutos);
        comboMinutosFinMartesHorario.setItems(listaMinutos);

        comboHoraInicioMiercolesHorario.setItems(listaHoras);
        comboHoraFinMiercolesHorario.setItems(listaHoras);
        comboMinutosFinMiercolesHorario.setItems(listaMinutos);
        comboMinutosInicioMiercolesHorario.setItems(listaMinutos);

        comboHoraInicioJuevesHorario.setItems(listaHoras);
        comboHoraFinJuevesHorario.setItems(listaHoras);
        comboMinutosInicioJuevesHorario.setItems(listaMinutos);
        comboMinutosFinJuevesHorario.setItems(listaMinutos);

        comboHoraInicioViernesHorario.setItems(listaHoras);
        comboHoraFinViernesHorario.setItems(listaHoras);
        comboMinutosInicioViernesHorario.setItems(listaMinutos);
        comboMinutosFinViernesHorario.setItems(listaMinutos);

        botonGuardar.setCursor(Cursor.HAND);
        botonCancelar.setCursor(Cursor.HAND);
        botonAgregarActividad.setCursor(Cursor.HAND);

        configurarHabilitacionDeHorariosPorDia();
    }

    private void configurarHabilitacionDeHorariosPorDia() {

        Map<CheckBox, List<ComboBox<String>>> diasConSusHorarios = Map.of(
                checkLunes, List.of(comboHoraInicioLunesHorario, comboMinutosInicioLunesHorario,
                        comboHoraFinLunesHorario, comboMinutosFinLunesHorario),
                checkMartes, List.of(comboHoraInicioMartesHorario, comboMinutosInicioMartesHorario,
                        comboHoraFinMartesHorario, comboMinutosFinMartesHorario),
                checkMiercoles, List.of(comboHoraInicioMiercolesHorario, comboMinutosInicioMiercolesHorario,
                        comboHoraFinMiercolesHorario, comboMinutosFinMiercolesHorario),
                checkJueves, List.of(comboHoraInicioJuevesHorario, comboMinutosInicioJuevesHorario,
                        comboHoraFinJuevesHorario, comboMinutosFinJuevesHorario),
                checkViernes, List.of(comboHoraInicioViernesHorario, comboMinutosInicioViernesHorario,
                        comboHoraFinViernesHorario, comboMinutosFinViernesHorario)
        );

        diasConSusHorarios.forEach((diaCheckBox, listaDeCombosHorario) -> {

            diaCheckBox.selectedProperty().addListener((observable, valorAnterior, valorActual) -> {
                listaDeCombosHorario.forEach(comboHorario -> comboHorario.setDisable(!valorActual));
            });
        });
    }

    private void agregarNuevaActividad() {

        VBox espacioActividadNueva = new VBox();
        espacioActividadNueva.setSpacing(5);
        espacioActividadNueva.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5; -fx-padding: 10;");

        Label titulo = new Label("Actividad");
        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        GridPane espacioDatosActividad = new GridPane();
        espacioDatosActividad.setAlignment(Pos.CENTER);
        espacioDatosActividad.setHgap(10);
        espacioDatosActividad.setVgap(10);
        espacioDatosActividad.setPrefHeight(865.0);
        espacioDatosActividad.setStyle("-fx-padding: 20;");

        ColumnConstraints columnaPeticionDeDatos = new ColumnConstraints();
        columnaPeticionDeDatos.setPercentWidth(200);
        ColumnConstraints columnaInsercionDeDatos = new ColumnConstraints();
        columnaInsercionDeDatos.setPercentWidth(600);
        espacioDatosActividad.getColumnConstraints().addAll(columnaPeticionDeDatos,
                columnaInsercionDeDatos);

        int filasNecesarias = 5;
        for (int numeroDeFilas = 0; numeroDeFilas < filasNecesarias; numeroDeFilas++) {
            espacioDatosActividad.getRowConstraints().addAll(new RowConstraints(30));
        }

        espacioDatosActividad.add(new Label("Nombre de la actividad:"), 0, 0);
        espacioDatosActividad.add(new Label("Duración"), 0, 1);
        espacioDatosActividad.add(new Label("Hitos:"), 0, 2);
        espacioDatosActividad.add(new Label("Fecha de Inicio:"), 0, 3);
        espacioDatosActividad.add(new Label("Fecha Fin:"), 0, 4);

        VBox espacioNombre = new VBox(3);
        TextField campoNombreNuevaActividad = new TextField();
        Label contadorNombre = new Label("255/255");
        contadorNombre.setStyle("-fx-text-fill: gray;");
        espacioNombre.getChildren().addAll(campoNombreNuevaActividad, contadorNombre);
        espacioDatosActividad.add(espacioNombre, 1, 0);

        VBox espacioDuracion = new VBox(3);
        TextField campoDuracionNuevaActividad = new TextField();
        Label contadorDuracion = new Label("255/255");
        contadorDuracion.setStyle("-fx-text-fill: gray;");
        espacioDuracion.getChildren().addAll(campoDuracionNuevaActividad, contadorDuracion);
        espacioDatosActividad.add(espacioDuracion, 1, 1);

        VBox espacioHitos = new VBox(3);
        TextField campoHitosNuevaActividad = new TextField();
        Label contadorHitos = new Label("255/255");
        contadorHitos.setStyle("-fx-text-fill: gray;");
        espacioHitos.getChildren().addAll(campoHitosNuevaActividad, contadorHitos);
        espacioDatosActividad.add(espacioHitos, 1, 2);

        DatePicker fechaInicio = new DatePicker();
        fechaInicio.setPrefSize(230, 31);
        espacioDatosActividad.add(fechaInicio, 1, 3);

        DatePicker fechaFin = new DatePicker();
        fechaFin.setPrefSize(232, 31);
        espacioDatosActividad.add(fechaFin, 1, 4);

        espacioActividadNueva.getChildren().addAll(titulo, espacioDatosActividad);
        contenedorActividades.getChildren().add(espacioActividadNueva);
    }

    @FXML
    private void guardarCronograma() {
        try {
            // Validación de campos principales
            String nombreActividad = campoNombreActividad.getText();
            String duracion = campoDuracionActividad.getText();
            String hitos = campoHitosActividad.getText();

            ActividadDTO actividadDTO = new ActividadDTO();
            List<String> camposVacios = actividadDTO.validarCamposVacios(nombreActividad, duracion, hitos);

            if (!camposVacios.isEmpty()) {
                UTILIDADES.mostrarAlerta("Campos vacíos", "Complete los campos requeridos", String.join("\n", camposVacios));
                return;
            }

            List<String> datosInvalidos = actividadDTO.validarCamposInvalidos(nombreActividad, duracion, hitos);
            if (!datosInvalidos.isEmpty()) {
                UTILIDADES.mostrarAlerta("Datos inválidos", "Verifique los campos", String.join("\n", datosInvalidos));
                return;
            }

            // Validación de fechas
            LocalDate fechaFin = fechaFinActividad.getValue();
            LocalDate fechaInicio = fechaInicioActividad.getValue();

            if (fechaInicio == null || fechaFin == null) {
                UTILIDADES.mostrarAlerta("Fechas obligatorias", "Complete las fechas", "");
                return;
            }

            if (!fechaInicio.isBefore(fechaFin)) {
                UTILIDADES.mostrarAlerta("Error en fechas", "La fecha inicio debe ser anterior a la fecha fin", "");
                return;
            }

            // Validación de periodo académico
            PeriodoDAO periodoDAO = new PeriodoDAO();
            PeriodoDTO periodoDTO = periodoDAO.mostrarPeriodoActual();
            if (periodoDTO.getIDPeriodo() == -1) {
                UTILIDADES.mostrarAlerta("Error", "No hay periodo activo", "Espere a que inicie un periodo");
                return;
            }

            // Validación de días seleccionados
            if (!validarDiasSeleccionados()) {
                UTILIDADES.mostrarAlerta("Días no seleccionados", "Seleccione al menos un día", "Marque al menos un día en el panel derecho");
                return;
            }

            // Validación de horarios
            List<String> erroresEnHorarios = validarHorarios();
            if (!erroresEnHorarios.isEmpty()) {
                UTILIDADES.mostrarAlerta("Error en horarios", "Hora entrada debe ser menor a salida", String.join("\n", erroresEnHorarios));
                return;
            }

            // Validación de proyecto del estudiante
            EstudianteDAO estudianteDAO = new EstudianteDAO();
            EstudianteDTO estudianteDTO = estudianteDAO.buscarEstudiantePorMatricula(matriculaEstudiante);
            if (estudianteDTO.getIdProyecto() == -1) {
                UTILIDADES.mostrarAlerta("Error", "No asignado a proyecto", "Espere a ser asignado");
                return;
            }

            // Creación del cronograma principal
            CronogramaActividadesDAO cronogramaDAO = new CronogramaActividadesDAO();
            CronogramaActividadesDTO cronogramaDTO = new CronogramaActividadesDTO(
                    0, matriculaEstudiante, estudianteDTO.getIdProyecto(), periodoDTO.getIDPeriodo(), 1);

            int idCronograma = cronogramaDAO.crearNuevoCronogramaDeActividades(cronogramaDTO);
            if (idCronograma == -1) {
                UTILIDADES.mostrarAlerta("Error", "No se pudo crear cronograma", "Contacte al administrador");
                return;
            }

            // Registro de horarios
            if (!insertarHorarios(estudianteDTO.getIdProyecto())) {
                UTILIDADES.mostrarAlerta("Error", "No se pudieron insertar horarios", "Contacte al administrador");
                return;
            }

            // Registro de actividad principal
            ActividadDAO actividadDAO = new ActividadDAO();
            ActividadDTO actividadPrincipal = new ActividadDTO(
                    0, nombreActividad, duracion, hitos,
                    Date.valueOf(fechaInicio), Date.valueOf(fechaFin), 1);

            int idActividadPrincipal = actividadDAO.crearNuevaActividad(actividadPrincipal);
            if (idActividadPrincipal == -1) {
                UTILIDADES.mostrarAlerta("Error", "No se pudo crear actividad principal", "Contacte al administrador");
                return;
            }

            // Relación actividad-cronograma
            CronogramaContieneDAO contieneDAO = new CronogramaContieneDAO();
            String mesPrincipal = obtenerMes(fechaInicio);
            CronogramaContieneDTO contienePrincipal = new CronogramaContieneDTO(
                    idCronograma, idActividadPrincipal, mesPrincipal, 1);

            if (!contieneDAO.insertarCronogramaContiene(contienePrincipal)) {
                UTILIDADES.mostrarAlerta("Error", "No se pudo relacionar actividad", "Contacte al administrador");
                return;
            }

            // Procesamiento de actividades adicionales
            procesarActividadesAdicionales(idCronograma, actividadDAO, contieneDAO);

            UTILIDADES.mostrarAlerta("Éxito", "Cronograma guardado", "");
            botonCancelar.getScene().getWindow().hide();

        } catch (SQLException | IOException e) {
            LOGGER.error("Error al guardar cronograma: " + e.getMessage(), e);
            UTILIDADES.mostrarAlerta("Error", "Error al guardar cronograma", "Detalles en el log");
        }
    }

    private void procesarActividadesAdicionales(int idCronograma, ActividadDAO actividadDAO,
                                                CronogramaContieneDAO contieneDAO) {
        for (Node node : contenedorActividades.getChildren()) {
            try {
                if (!(node instanceof VBox actividadBox)) {
                    continue;
                }

                if (actividadBox.getChildren().size() < 2) {
                    LOGGER.warn("VBox de actividad no tiene la estructura esperada");
                    continue;
                }

                Node posibleGrid = actividadBox.getChildren().get(1);
                if (!(posibleGrid instanceof GridPane grid)) {
                    LOGGER.warn("Segundo elemento no es GridPane como se esperaba");
                    continue;
                }

                // Obtener campos con validación
                TextField nombreField = obtenerTextField(grid, 2);
                TextField duracionField = obtenerTextField(grid, 4);
                TextField hitosField = obtenerTextField(grid, 6);
                DatePicker inicioPicker = obtenerDatePicker(grid, 8);
                DatePicker finPicker = obtenerDatePicker(grid, 10);

                if (nombreField == null || duracionField == null || hitosField == null ||
                        inicioPicker == null || finPicker == null) {
                    LOGGER.warn("No se pudieron obtener todos los campos de la actividad");
                    continue;
                }

                // Validar campos no vacíos
                if (nombreField.getText().isEmpty() || duracionField.getText().isEmpty() ||
                        hitosField.getText().isEmpty() || inicioPicker.getValue() == null ||
                        finPicker.getValue() == null) {
                    continue;
                }

                // Crear actividad adicional
                ActividadDTO actividadAdicional = new ActividadDTO(
                        0,
                        nombreField.getText(),
                        duracionField.getText(),
                        hitosField.getText(),
                        Date.valueOf(inicioPicker.getValue()),
                        Date.valueOf(finPicker.getValue()),
                        1
                );

                int idActividadAdicional = actividadDAO.crearNuevaActividad(actividadAdicional);
                if (idActividadAdicional != -1) {
                    String mesAdicional = obtenerMes(inicioPicker.getValue());
                    CronogramaContieneDTO contieneAdicional = new CronogramaContieneDTO(
                            idCronograma,
                            idActividadAdicional,
                            mesAdicional,
                            1
                    );
                    contieneDAO.insertarCronogramaContiene(contieneAdicional);
                }

            } catch (Exception e) {
                LOGGER.error("Error procesando actividad adicional: " + e.getMessage(), e);
            }
        }
    }

    private TextField obtenerTextField(GridPane grid, int index) {
        try {
            Node node = grid.getChildren().get(index);
            if (node instanceof VBox vbox && !vbox.getChildren().isEmpty()) {
                Node fieldNode = vbox.getChildren().get(0);
                if (fieldNode instanceof TextField) {
                    return (TextField) fieldNode;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            LOGGER.warn("Índice de GridPane no válido: " + index);
        }
        return null;
    }

    private DatePicker obtenerDatePicker(GridPane grid, int index) {
        try {
            Node node = grid.getChildren().get(index);
            if (node instanceof DatePicker) {
                return (DatePicker) node;
            }
        } catch (IndexOutOfBoundsException e) {
            LOGGER.warn("Índice de GridPane no válido: " + index);
        }
        return null;
    }

    private String obtenerMes(LocalDate fecha) {
        return fecha.getMonth().toString();
    }

    private Map<CheckBox, List<ComboBox<String>>> obtenerDiasConSusHorarios() {
        return Map.of(
                checkLunes, List.of(
                        comboHoraInicioLunesHorario, comboMinutosInicioLunesHorario,
                        comboHoraFinLunesHorario, comboMinutosFinLunesHorario
                ),
                checkMartes, List.of(
                        comboHoraInicioMartesHorario, comboMinutosInicioMartesHorario,
                        comboHoraFinMartesHorario, comboMinutosFinMartesHorario
                ),
                checkMiercoles, List.of(
                        comboHoraInicioMiercolesHorario, comboMinutosInicioMiercolesHorario,
                        comboHoraFinMiercolesHorario, comboMinutosFinMiercolesHorario
                ),
                checkJueves, List.of(
                        comboHoraInicioJuevesHorario, comboMinutosInicioJuevesHorario,
                        comboHoraFinJuevesHorario, comboMinutosFinJuevesHorario
                ),
                checkViernes, List.of(
                        comboHoraInicioViernesHorario, comboMinutosInicioViernesHorario,
                        comboHoraFinViernesHorario, comboMinutosFinViernesHorario
                )
        );
    }

    private boolean insertarHorarios(int idProyecto) throws SQLException, IOException {

        Map<CheckBox, List<ComboBox<String>>> diasConHorarios = obtenerDiasConSusHorarios();
        HorarioProyectoDAO horarioDAO = new HorarioProyectoDAO();
        boolean horariosInsertados = false;

        for (Map.Entry<CheckBox, List<ComboBox<String>>> entrada : diasConHorarios.entrySet()) {

            CheckBox diaSeleccionado = entrada.getKey();
            List<ComboBox<String>> horarios = entrada.getValue();

            if (diaSeleccionado.isSelected() && verificarHorarioDia(horarios)) {

                String diaSemana = String.valueOf(obtenerNombreDia(diaSeleccionado));
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

    private boolean validarDiasSeleccionados() {

        return checkLunes.isSelected() ||
                checkMartes.isSelected() ||
                checkMiercoles.isSelected() ||
                checkJueves.isSelected() ||
                checkViernes.isSelected();
    }

    private List<String> validarHorarios() {

        Map<CheckBox, List<ComboBox<String>>> diasConHorarios = obtenerDiasConSusHorarios();
        List<String> listaErrores = new ArrayList<>();

        diasConHorarios.forEach((diaSeleccionado, horarios) -> {

            if (diaSeleccionado.isSelected() && !verificarHorarioDia(horarios)) {
                listaErrores.add("Horario inválido para " + obtenerNombreDia(diaSeleccionado));
            }
        });

        return listaErrores;
    }

    private List<String> obtenerNombreDia(CheckBox checkBox) {

        List<String> diasSeleccionados = new ArrayList<>();

        if (checkBox == checkLunes) {
            diasSeleccionados.add("Lunes");
        }

        if (checkBox == checkMartes) {
            diasSeleccionados.add("Martes");
        }

        if (checkBox == checkMiercoles) {
            diasSeleccionados.add("Miercoles");
        }

        if (checkBox == checkJueves) {
            diasSeleccionados.add("Jueves");
        }

        if (checkBox == checkViernes) {
            diasSeleccionados.add("Viernes");
        }

        return diasSeleccionados;
    }

    private boolean verificarHorarioDia(List<ComboBox<String>> horarios) {

        if (!todosLosCombosTienenValor(horarios)) {
            return false;
        }

        int horaInicio = Integer.parseInt(horarios.get(0).getValue());
        int minutoInicio = Integer.parseInt(horarios.get(1).getValue());
        int horaFin = Integer.parseInt(horarios.get(2).getValue());
        int minutoFin = Integer.parseInt(horarios.get(3).getValue());

        return esHorarioCorrecto(horaInicio, minutoInicio, horaFin, minutoFin);
    }

    private boolean todosLosCombosTienenValor(List<ComboBox<String>> horarios) {

        return horarios.stream().allMatch(combo -> combo.getValue() != null);
    }

    private boolean esHorarioCorrecto(int horaInicio, int minutoInicio, int horaFin, int minutoFin) {

        if (horaInicio > horaFin) {
            return false;
        }

        return horaInicio != horaFin || minutoInicio < minutoFin;
    }

    @FXML
    private void cancelar() {
        botonCancelar.getScene().getWindow().hide();
    }

    public void setDatosIniciales(String matriculaEstudiante) {
        this.matriculaEstudiante = matriculaEstudiante;
    }
}
