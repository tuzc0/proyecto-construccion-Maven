package GUI.gestionproyecto;

import GUI.utilidades.Utilidades;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logica.DAOs.HorarioProyectoDAO;
import logica.DAOs.ProyectoDAO;
import logica.DTOs.HorarioProyectoDTO;
import logica.DTOs.ProyectoDTO;
import logica.DTOs.RepresentanteDTO;
import logica.utilidadesproyecto.SeleccionRepresentanteOrganizacion;
import logica.verificacion.VerificicacionGeneral;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ControladorRegistroProyectoGUI {

    private static final Logger LOGGER =
            LogManager.getLogger(ControladorRegistroProyectoGUI.class);

    @FXML private TextField campoNombre;
    @FXML private TextArea textoDescripcionGeneral;
    @FXML private TextArea textoObjetivosGenerales;
    @FXML private TextField campoUsuariosDirectos;
    @FXML private TextField campoUsuariosIndirectos;
    @FXML private TextArea textoObjetivosInmediatos;
    @FXML private TextArea textoObjetivosMediatos;
    @FXML private TextArea textoMetodologia;
    @FXML private TextArea textoRecursos;
    @FXML private TextArea textoActividades;
    @FXML private TextArea textoResponsabilidades;
    @FXML private CheckBox checkLunes;
    @FXML private CheckBox checkMartes;
    @FXML private CheckBox checkMiercoles;
    @FXML private CheckBox checkJueves;
    @FXML private CheckBox checkViernes;
    @FXML private ComboBox<String> comboHoraLunesInicio;
    @FXML private ComboBox<String> comboHoraLunesFin;
    @FXML private ComboBox<String> comboMinutosLunesInicio;
    @FXML private ComboBox<String> comboMinutosLunesFin;
    @FXML private ComboBox<String> comboHoraMartesInicio;
    @FXML private ComboBox<String> comboHoraMartesFin;
    @FXML private ComboBox<String> comboMinutosMartesInicio;
    @FXML private ComboBox<String> comboMinutosMartesFin;
    @FXML private ComboBox<String> comboHoraMiercolesInicio;
    @FXML private ComboBox<String> comboHoraMiercolesFin;
    @FXML private ComboBox<String> comboMinutosMiercolesInicio;
    @FXML private ComboBox<String> comboMinutosMiercolesFin;
    @FXML private ComboBox<String> comboHoraJuevesInicio;
    @FXML private ComboBox<String> comboHoraJuevesFin;
    @FXML private ComboBox<String> comboMinutosJuevesInicio;
    @FXML private ComboBox<String> comboMinutosJuevesFin;
    @FXML private ComboBox<String> comboHoraViernesInicio;
    @FXML private ComboBox<String> comboHoraViernesFin;
    @FXML private ComboBox<String> comboMinutosViernesInicio;
    @FXML private ComboBox<String> comboMinutosViernesFin;
    @FXML private Label etiquetaDuracion;
    @FXML private Label contadorNombre;
    @FXML private Label contadorDescripcionGeneral;
    @FXML private Label contadorObjetivosGenerales;
    @FXML private Label contadorObjetivosInmediatos;
    @FXML private Label contadorObjetivosMediatos;
    @FXML private Label contadorMetodologia;
    @FXML private Label contadorRecursos;
    @FXML private Label contadorActividades;
    @FXML private Label contadorResponsabilidades;
    @FXML private Button botonRegistrar;
    @FXML private Button botonCancelar;
    @FXML private Button botonSeleccionarRepresentante;

    private final Utilidades UTILIDADES = new Utilidades();

    @FXML
    private void initialize() {

        int maxCaracteresNombreProyecto = 150;
        int maxCaracteresCamposTexto = 255;

        VerificicacionGeneral verificacionGeneralUtilidad = new VerificicacionGeneral();

        verificacionGeneralUtilidad.contadorCaracteresTextField(campoNombre,
                contadorNombre, maxCaracteresNombreProyecto);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoDescripcionGeneral,
                contadorDescripcionGeneral, maxCaracteresCamposTexto);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoObjetivosGenerales,
                contadorObjetivosGenerales, maxCaracteresCamposTexto);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoObjetivosInmediatos,
                contadorObjetivosInmediatos, maxCaracteresCamposTexto);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoObjetivosMediatos,
                contadorObjetivosMediatos, maxCaracteresCamposTexto);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoMetodologia,
                contadorMetodologia, maxCaracteresCamposTexto);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoRecursos,
                contadorRecursos, maxCaracteresCamposTexto);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoActividades,
                contadorActividades, maxCaracteresCamposTexto);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoResponsabilidades,
                contadorResponsabilidades, maxCaracteresCamposTexto);

        ObservableList<String> listaHoras = FXCollections.observableArrayList();
        ObservableList<String> listaMinutos = FXCollections.observableArrayList();

        for (int hora = 0; hora < 24; hora++) {
            listaHoras.add(String.format("%02d", hora));
        }

        for (int minuto = 0; minuto < 60; minuto++) {
            listaMinutos.add(String.format("%02d", minuto));
        }

        comboHoraLunesInicio.setItems(listaHoras);
        comboHoraLunesFin.setItems(listaHoras);
        comboMinutosLunesInicio.setItems(listaMinutos);
        comboMinutosLunesFin.setItems(listaMinutos);

        comboHoraMartesInicio.setItems(listaHoras);
        comboHoraMartesFin.setItems(listaHoras);
        comboMinutosMartesInicio.setItems(listaMinutos);
        comboMinutosMartesFin.setItems(listaMinutos);

        comboHoraMiercolesInicio.setItems(listaHoras);
        comboHoraMiercolesFin.setItems(listaHoras);
        comboMinutosMiercolesInicio.setItems(listaMinutos);
        comboMinutosMiercolesFin.setItems(listaMinutos);

        comboHoraJuevesInicio.setItems(listaHoras);
        comboHoraJuevesFin.setItems(listaHoras);
        comboMinutosJuevesInicio.setItems(listaMinutos);
        comboMinutosJuevesFin.setItems(listaMinutos);

        comboHoraViernesInicio.setItems(listaHoras);
        comboHoraViernesFin.setItems(listaHoras);
        comboMinutosViernesInicio.setItems(listaMinutos);
        comboMinutosViernesFin.setItems(listaMinutos);

        botonRegistrar.setCursor(Cursor.HAND);
        botonCancelar.setCursor(Cursor.HAND);
        botonSeleccionarRepresentante.setCursor(Cursor.HAND);

        configurarHabilitacionDeHorariosPorDia();
    }

    private void configurarHabilitacionDeHorariosPorDia() {

        Map<CheckBox, List<ComboBox<String>>> diasConSusHorarios = Map.of(
                checkLunes, List.of(comboHoraLunesInicio, comboMinutosLunesInicio,
                        comboHoraLunesFin, comboMinutosLunesFin),
                checkMartes, List.of(comboHoraMartesInicio, comboMinutosMartesInicio,
                        comboHoraMartesFin, comboMinutosMartesFin),
                checkMiercoles, List.of(comboHoraMiercolesInicio, comboMinutosMiercolesInicio,
                        comboHoraMiercolesFin, comboMinutosMiercolesFin),
                checkJueves, List.of(comboHoraJuevesInicio, comboMinutosJuevesInicio,
                        comboHoraJuevesFin, comboMinutosJuevesFin),
                checkViernes, List.of(comboHoraViernesInicio, comboMinutosViernesInicio,
                        comboHoraViernesFin, comboMinutosViernesFin)
        );

        diasConSusHorarios.forEach((diaCheckBox, listaDeCombosHorario) -> {
            diaCheckBox.selectedProperty().addListener((observable, valorAnterior,
                                                        valorActual) -> {
                listaDeCombosHorario.forEach(comboHorario ->
                        comboHorario.setDisable(!valorActual));
            });
        });
    }

    @FXML
    private void registrarProyecto() {

        String nombreProyecto = campoNombre.getText();
        String descripcionProyecto = textoDescripcionGeneral.getText();
        String objetivosGeneralesProyecto = textoObjetivosGenerales.getText();
        String objetivosInmediatosProyecto = textoObjetivosInmediatos.getText();
        String objetivosMediatosProyecto = textoObjetivosMediatos.getText();
        String metodologiaProyecto = textoMetodologia.getText();
        String recursosProyecto = textoRecursos.getText();
        String actividadesProyecto = textoActividades.getText();
        String responsabilidadesProyecto = textoResponsabilidades.getText();
        String duracionProyecto = etiquetaDuracion.getText();
        String textoUsuariosDirectos = campoUsuariosDirectos.getText();
        String textoUsuariosIndirectos = campoUsuariosIndirectos.getText();

        ProyectoDTO proyectoDTO = new ProyectoDTO();

        List<String> camposFaltantes = proyectoDTO.camposVaciosProyecto(
                nombreProyecto, descripcionProyecto, objetivosGeneralesProyecto,
                objetivosInmediatosProyecto, objetivosMediatosProyecto, metodologiaProyecto,
                recursosProyecto, actividadesProyecto, responsabilidadesProyecto,
                textoUsuariosDirectos, textoUsuariosIndirectos);

        if (!camposFaltantes.isEmpty()) {

            String mensajeError = String.join("\n", camposFaltantes);
            UTILIDADES.mostrarAlerta(
                    "Campos vacíos",
                    "Por favor, complete todos los campos requeridos.",
                    mensajeError
            );
            return;
        }

        List<String> erroresDeValidacion = proyectoDTO.validarCamposProyecto(
                nombreProyecto, descripcionProyecto, objetivosGeneralesProyecto,
                objetivosInmediatosProyecto, objetivosMediatosProyecto, metodologiaProyecto,
                recursosProyecto, actividadesProyecto, responsabilidadesProyecto,
                textoUsuariosDirectos, textoUsuariosIndirectos);

        if (!erroresDeValidacion.isEmpty()) {

            String mensajeError = String.join("\n", erroresDeValidacion);
            UTILIDADES.mostrarAlerta(
                    "Errores de validación",
                    "Por favor, ingrese información válida en los campos.",
                    mensajeError
            );
            return;
        }

        if (!validarDiasSeleccionados()) {

            UTILIDADES.mostrarAlerta(
                    "Días no seleccionados",
                    "Por favor, seleccione al menos un día de la semana.",
                    "Debe marcar al menos un día en el panel derecho."
            );
            return;
        }

        List<String> erroresEnHorarios = validarHorarios();

        if (!erroresEnHorarios.isEmpty()) {

            UTILIDADES.mostrarAlerta(
                    "Error en horarios",
                    "La hora de entrada debe ser menor a la hora de salida. " +
                            "Por favor verifique el horario.",
                    String.join("\n", erroresEnHorarios)
            );
            return;
        }

        int usuariosDirectos = Integer.parseInt(textoUsuariosDirectos);
        int usuariosIndirectos = Integer.parseInt(textoUsuariosIndirectos);

        RepresentanteDTO representanteSeleccionado =
                SeleccionRepresentanteOrganizacion.getRepresentanteSeleccionado();

        if (representanteSeleccionado == null) {

            UTILIDADES.mostrarAlerta(
                    "Error.",
                    "Por favor, seleccione un representante para el proyecto.",
                    "De clic en el botón 'Seleccionar Representante'"
            );
            return;
        }

        int idRepresentante = representanteSeleccionado.getIDRepresentante();
        int estadoActivoProyecto = 1;
        int idProyecto = 0;
        int idCronograma = 0;

        proyectoDTO = new ProyectoDTO(
                idProyecto, nombreProyecto, objetivosGeneralesProyecto,
                objetivosInmediatosProyecto, objetivosMediatosProyecto, metodologiaProyecto,
                recursosProyecto, actividadesProyecto, responsabilidadesProyecto,
                duracionProyecto, idCronograma, estadoActivoProyecto, idRepresentante,
                descripcionProyecto, usuariosDirectos, usuariosIndirectos
        );

        ProyectoDAO proyectoDAO = new ProyectoDAO();

        try {

            idProyecto = proyectoDAO.crearNuevoProyecto(proyectoDTO);
            boolean horariosInsertados = insertarHorarios(idProyecto);

            if (horariosInsertados) {

                LOGGER.info("Registro de proyecto exitoso.");
                UTILIDADES.mostrarAlerta(
                        "Registro exitoso",
                        "El proyecto ha sido registrado correctamente.",
                        ""
                );

            } else {

                LOGGER.warn("No se pudieron guardar todos los datos del proyecto.");
                UTILIDADES.mostrarAlerta(
                        "Registro incompleto",
                        "No se pudieron guardar todos los datos.",
                        "Por favor, verifique la información e intente nuevamente."
                );
            }

        } catch (SQLException e) {

            LOGGER.error("Error durante el registro del proyecto.", e);
            UTILIDADES.mostrarAlerta(
                    "Error durante el registro.",
                    "Ocurrió un error. Por favor, inténtelo de nuevo más tarde.",
                    ""
            );

        } catch (IOException e) {

            LOGGER.error("Error durante el registro del proyecto.", e);
            UTILIDADES.mostrarAlerta(
                    "Error.",
                    "Ocurrió un error. Por favor, inténtelo de nuevo más tarde.",
                    ""
            );

        } catch (Exception e) {

            LOGGER.error("Error durante el registro del proyecto.", e);
            UTILIDADES.mostrarAlerta(
                    "Error.",
                    "Ocurrió un error. Por favor, inténtelo de nuevo más tarde.",
                    ""
            );
        }
    }

    @FXML
    private void cancelarRegistroProyecto() {

        String nombreProyecto = campoNombre.getText();
        String descripcionProyecto = textoDescripcionGeneral.getText();
        String objetivosGeneralesProyecto = textoObjetivosGenerales.getText();
        String objetivosInmediatosProyecto = textoObjetivosInmediatos.getText();
        String objetivosMediatosProyecto = textoObjetivosMediatos.getText();
        String metodologiaProyecto = textoMetodologia.getText();
        String recursosProyecto = textoRecursos.getText();
        String actividadesProyecto = textoActividades.getText();
        String responsabilidadesProyecto = textoResponsabilidades.getText();
        String duracionProyecto = etiquetaDuracion.getText();
        String textoUsuariosDirectos = campoUsuariosDirectos.getText();
        String textoUsuariosIndirectos = campoUsuariosIndirectos.getText();

        UTILIDADES.mostrarAlertaConfirmacion(
                "Confirmar cancelación",
                "¿Está seguro que desea cancelar?",
                "Los cambios no guardados se perderán",
                () -> {

                    Stage ventanaActual = (Stage) botonCancelar.getScene().getWindow();
                    ventanaActual.close();
                },
                () -> {

                    campoNombre.setText(nombreProyecto);
                    textoDescripcionGeneral.setText(descripcionProyecto);
                    textoObjetivosGenerales.setText(objetivosGeneralesProyecto);
                    textoObjetivosInmediatos.setText(objetivosInmediatosProyecto);
                    textoObjetivosMediatos.setText(objetivosMediatosProyecto);
                    textoMetodologia.setText(metodologiaProyecto);
                    textoRecursos.setText(recursosProyecto);
                    textoActividades.setText(actividadesProyecto);
                    textoResponsabilidades.setText(responsabilidadesProyecto);
                    etiquetaDuracion.setText(duracionProyecto);
                    campoUsuariosDirectos.setText(textoUsuariosDirectos);
                    campoUsuariosIndirectos.setText(textoUsuariosIndirectos);

                    UTILIDADES.mostrarAlerta(
                            "Operación cancelada.",
                            "Los cambios no han sido descartados..",
                            "Puede continuar con el registro."
                    );
                }
        );
    }

    @FXML
    private void abrirVentanaSeleccionRepresentante() {

        try {

            FXMLLoader cargadorVentanaSeleccion =
                    new FXMLLoader(getClass().getResource("/SeleccionarRepresentante.fxml"));
            Stage ventanaSeleccionRepresentante = new Stage();
            ventanaSeleccionRepresentante.setScene(
                    new Scene(cargadorVentanaSeleccion.load()));

            ControladorSeleccionRepresentanteGUI controladorSeleccion =
                    cargadorVentanaSeleccion.getController();

            controladorSeleccion.setControladorRegistro(this);
            ventanaSeleccionRepresentante.show();

        } catch (IOException errorCargaVentana) {

            LOGGER.error("Error al cargar la ventana de selección de representante: "
                    + errorCargaVentana.getMessage());
            UTILIDADES.mostrarAlerta(
                    "Error",
                    "No se pudo abrir la ventana de selección de representante.",
                    "Por favor, inténtelo de nuevo más tarde o contacte al administrador.");
        }
    }

    private Map<CheckBox, List<ComboBox<String>>> obtenerDiasConSusHorarios() {

        return Map.of(
                checkLunes, List.of(
                        comboHoraLunesInicio, comboMinutosLunesInicio,
                        comboHoraLunesFin, comboMinutosLunesFin
                ),
                checkMartes, List.of(
                        comboHoraMartesInicio, comboMinutosMartesInicio,
                        comboHoraMartesFin, comboMinutosMartesFin
                ),
                checkMiercoles, List.of(
                        comboHoraMiercolesInicio, comboMinutosMiercolesInicio,
                        comboHoraMiercolesFin, comboMinutosMiercolesFin
                ),
                checkJueves, List.of(
                        comboHoraJuevesInicio, comboMinutosJuevesInicio,
                        comboHoraJuevesFin, comboMinutosJuevesFin
                ),
                checkViernes, List.of(
                        comboHoraViernesInicio, comboMinutosViernesInicio,
                        comboHoraViernesFin, comboMinutosViernesFin
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

                HorarioProyectoDTO horarioDTO = new HorarioProyectoDTO(
                        0, idProyecto, diaSemana,
                        Timestamp.valueOf(String.format("1970-01-01 %02d:%02d:00", horaInicio, minutoInicio)),
                        Timestamp.valueOf(String.format("1970-01-01 %02d:%02d:00", horaFin, minutoFin))
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
}
