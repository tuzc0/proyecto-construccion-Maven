package GUI.gestioncronogramaactividades;

import GUI.GestorHorarios;
import GUI.utilidades.Utilidades;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

public class ControladorDetallesCronogramaActividadesGUI {

    private static final Logger LOGGER = LogManager.getLogger(ControladorDetallesCronogramaActividadesGUI.class);

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
    private TableView<ActividadDTO> tablaActividades;
    @FXML
    private TableColumn<ActividadDTO, String> columnaNombreActividad;
    @FXML
    private TableColumn<ActividadDTO, Void> columnaDetalles;

    private ControladorConsultarCronogramaActividadesGUI controladorPadre;
    private String matricula;
    private Utilidades gestorVentanas = new Utilidades();
    private IGestorAlertas gestorAlertas = new Utilidades();
    private ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(gestorAlertas, LOGGER);
    private GestorHorarios gestorHorarios = new GestorHorarios();

    public void setControladorPadre(ControladorConsultarCronogramaActividadesGUI controladorPadre) {
        this.controladorPadre = controladorPadre;
    }

    public void setMatriculaDTO(String matriculaEstudianteSeleccionado) {
        this.matricula = matriculaEstudianteSeleccionado;
        cargarDatosCronograma();
    }

    @FXML
    public void initialize() {

        configurarTablaActividades();
    }

    private void configurarTablaActividades() {

        columnaNombreActividad.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        añadirBotonDetallesATabla();
    }

    public void cargarDatosCronograma() {

        try {

            CronogramaActividadesDAO cronogramaDAO = new CronogramaActividadesDAO();
            CronogramaActividadesDTO cronograma = cronogramaDAO.buscarCronogramaPorMatricula(matricula);

            if (cronograma == null) {
                gestorVentanas.mostrarAlerta("Información", "No se encontró cronograma",
                        "El estudiante no tiene un cronograma registrado.");
                return;
            }

            cargarHorariosProyecto(matricula);
            cargarActividadesCronograma(cronograma.getIDCronograma());

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            gestorVentanas.mostrarAlerta("Error inesperado", "Error al cargar cronograma",
                    "Por favor, intente nuevamente más tarde.");
            LOGGER.error("Error inesperado al cargar cronograma: " + e);
        }
    }

    private void cargarHorariosProyecto(String matricula) throws SQLException, IOException {

        HorarioProyectoDAO horarioDAO = new HorarioProyectoDAO();

        List<HorarioProyectoDTO> horarios = horarioDAO.buscarHorarioPorMatricula(matricula);

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

        for (HorarioProyectoDTO horario : horarios) {

            String dia = horario.getDiaSemana();
            Time horaInicio = horario.getHoraInicio();
            Time horaFin = horario.getHoraFin();

            switch (dia.toLowerCase()) {
                case "lunes":

                    checkLunes.setSelected(true);
                    setHorarioCombo(comboHoraInicioLunesHorario, comboMinutosInicioLunesHorario, horaInicio);
                    setHorarioCombo(comboHoraFinLunesHorario, comboMinutosFinLunesHorario, horaFin);
                    break;

                case "martes":

                    checkMartes.setSelected(true);
                    setHorarioCombo(comboHoraInicioMartesHorario, comboMinutosInicioMartesHorario, horaInicio);
                    setHorarioCombo(comboHoraFinMartesHorario, comboMinutosFinMartesHorario, horaFin);
                    break;

                case "miércoles":
                case "miercoles":

                    checkMiercoles.setSelected(true);
                    setHorarioCombo(comboHoraInicioMiercolesHorario, comboMinutosInicioMiercolesHorario, horaInicio);
                    setHorarioCombo(comboHoraFinMiercolesHorario, comboMinutosFinMiercolesHorario, horaFin);
                    break;

                case "jueves":

                    checkJueves.setSelected(true);
                    setHorarioCombo(comboHoraInicioJuevesHorario, comboMinutosInicioJuevesHorario, horaInicio);
                    setHorarioCombo(comboHoraFinJuevesHorario, comboMinutosFinJuevesHorario, horaFin);
                    break;

                case "viernes":

                    checkViernes.setSelected(true);
                    setHorarioCombo(comboHoraInicioViernesHorario, comboMinutosInicioViernesHorario, horaInicio);
                    setHorarioCombo(comboHoraFinViernesHorario, comboMinutosFinViernesHorario, horaFin);
                    break;
            }
        }
    }

    private void añadirBotonDetallesATabla() {

        Callback<TableColumn<ActividadDTO, Void>, TableCell<ActividadDTO, Void>> fabricadorCeldas =
                new Callback<TableColumn<ActividadDTO, Void>, TableCell<ActividadDTO, Void>>() {

                    @Override
                    public TableCell<ActividadDTO, Void> call(TableColumn<ActividadDTO, Void> param) {
                        return new TableCell<ActividadDTO, Void>() {

                            private final Button botonDetalles = new Button("Ver detalles");

                            {
                                botonDetalles.setOnAction(event -> {
                                    ActividadDTO actividad = getTableView().getItems().get(getIndex());
                                    abrirVentanaDetallesActividad(actividad.getIDActividad());
                                });
                            }

                            @Override
                            protected void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);

                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(botonDetalles);
                                }
                            }
                        };
                    }
                };

        columnaDetalles.setCellFactory(fabricadorCeldas);
    }

    private void abrirVentanaDetallesActividad(int idActividad) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetallesActividadGUI.fxml"));
            Parent root = loader.load();

            ControladorDetallesActividadGUI controlador = loader.getController();
            controlador.setIdActividad(idActividad);

            Stage stage = new Stage();
            stage.setTitle("Detalles de Actividad");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            gestorVentanas.mostrarAlerta("Error inesperado", "Error al abrir detalles de actividad",
                    "Por favor, intente nuevamente más tarde.");
            LOGGER.error("Error inesperado al abrir detalles de actividad: " + e);
        }
    }

    private void setHorarioCombo(ComboBox<String> comboHora, ComboBox<String> comboMinutos, Time time) {

        String[] partes = time.toString().split(":");
        comboHora.getSelectionModel().select(partes[0]);
        comboMinutos.getSelectionModel().select(partes[1]);
    }

    private void cargarActividadesCronograma(int idCronograma) throws SQLException, IOException {

        CronogramaContieneDAO cronogramaContieneDAO = new CronogramaContieneDAO();
        ActividadDAO actividadDAO = new ActividadDAO();

        List<Integer> idsActividades = cronogramaContieneDAO.obtenerActividadesPorCronograma(idCronograma);
        ObservableList<ActividadDTO> actividades = FXCollections.observableArrayList();

        for (Integer idActividad : idsActividades) {

            ActividadDTO actividad = actividadDAO.buscarActividadPorID(idActividad);

            if (actividad != null) {
                actividades.add(actividad);
            }
        }

        tablaActividades.setItems(actividades);
    }
}