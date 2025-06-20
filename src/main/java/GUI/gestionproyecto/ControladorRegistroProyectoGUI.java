package GUI.gestionproyecto;

import GUI.GestorHorarios;
import GUI.utilidades.Utilidades;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logica.DAOs.HorarioProyectoDAO;
import logica.DAOs.ProyectoDAO;
import logica.DTOs.HorarioProyectoDTO;
import logica.DTOs.OrganizacionVinculadaDTO;
import logica.DTOs.ProyectoDTO;
import logica.DTOs.RepresentanteDTO;
import logica.interfaces.ISeleccionRepresentante;
import logica.utilidadesproyecto.SeleccionRepresentanteOrganizacion;
import logica.verificacion.ValidadorDatosProyecto;
import logica.verificacion.VerificicacionGeneral;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.Map;

public class ControladorRegistroProyectoGUI implements ISeleccionRepresentante {

    private static final Logger LOGGER =
            LogManager.getLogger(ControladorRegistroProyectoGUI.class);

    @FXML
    private TextField campoNombre;
    @FXML
    private TextArea textoDescripcionGeneral;
    @FXML
    private TextArea textoObjetivosGenerales;
    @FXML
    private TextField campoUsuariosDirectos;
    @FXML
    private TextField campoUsuariosIndirectos;
    @FXML
    private TextField campoEstudianteRequeridos;
    @FXML
    private TextArea textoObjetivosInmediatos;
    @FXML
    private TextArea textoObjetivosMediatos;
    @FXML
    private TextArea textoMetodologia;
    @FXML
    private TextArea textoRecursos;
    @FXML
    private TextArea textoActividades;
    @FXML
    private TextArea textoResponsabilidades;
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
    private ComboBox<String> comboHoraLunesInicio;
    @FXML
    private ComboBox<String> comboHoraLunesFin;
    @FXML
    private ComboBox<String> comboMinutosLunesInicio;
    @FXML
    private ComboBox<String> comboMinutosLunesFin;
    @FXML
    private ComboBox<String> comboHoraMartesInicio;
    @FXML
    private ComboBox<String> comboHoraMartesFin;
    @FXML
    private ComboBox<String> comboMinutosMartesInicio;
    @FXML
    private ComboBox<String> comboMinutosMartesFin;
    @FXML
    private ComboBox<String> comboHoraMiercolesInicio;
    @FXML
    private ComboBox<String> comboHoraMiercolesFin;
    @FXML
    private ComboBox<String> comboMinutosMiercolesInicio;
    @FXML
    private ComboBox<String> comboMinutosMiercolesFin;
    @FXML
    private ComboBox<String> comboHoraJuevesInicio;
    @FXML
    private ComboBox<String> comboHoraJuevesFin;
    @FXML
    private ComboBox<String> comboMinutosJuevesInicio;
    @FXML
    private ComboBox<String> comboMinutosJuevesFin;
    @FXML
    private ComboBox<String> comboHoraViernesInicio;
    @FXML
    private ComboBox<String> comboHoraViernesFin;
    @FXML
    private ComboBox<String> comboMinutosViernesInicio;
    @FXML
    private ComboBox<String> comboMinutosViernesFin;
    @FXML
    private Label etiquetaDuracion;
    @FXML
    private Label etiquetaContadorNombre;
    @FXML
    private Label etiquetaContadorDescripcionGeneral;
    @FXML
    private Label etiquetaContadorObjetivosGenerales;
    @FXML
    private Label etiquetaContadorObjetivosInmediatos;
    @FXML
    private Label etiquetaContadorObjetivosMediatos;
    @FXML
    private Label etiquetaContadorMetodologia;
    @FXML
    private Label etiquetaContadorRecursos;
    @FXML
    private Label etiquetaContadorActividades;
    @FXML
    private Label etiquetaContadorResponsabilidades;
    @FXML
    private Button botonRegistrar;
    @FXML
    private Button botonCancelar;
    @FXML
    private Button botonSeleccionarRepresentante;

    GestorHorarios gestorHorarios;
    private final Utilidades UTILIDADES = new Utilidades();

    @FXML
    private void initialize() {

        final int MAX_CARACTERES_NOMBRE = 150;
        final int MAX_CARACTERES_CAMPOS_TEXTO = 255;

        VerificicacionGeneral verificacionGeneralUtilidad = new VerificicacionGeneral();

        verificacionGeneralUtilidad.contadorCaracteresTextField(campoNombre,
                etiquetaContadorNombre, MAX_CARACTERES_NOMBRE);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoDescripcionGeneral,
                etiquetaContadorDescripcionGeneral, MAX_CARACTERES_CAMPOS_TEXTO);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoObjetivosGenerales,
                etiquetaContadorObjetivosGenerales, MAX_CARACTERES_CAMPOS_TEXTO);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoObjetivosInmediatos,
                etiquetaContadorObjetivosInmediatos, MAX_CARACTERES_CAMPOS_TEXTO);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoObjetivosMediatos,
                etiquetaContadorObjetivosMediatos, MAX_CARACTERES_CAMPOS_TEXTO);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoMetodologia,
                etiquetaContadorMetodologia, MAX_CARACTERES_CAMPOS_TEXTO);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoRecursos,
                etiquetaContadorRecursos, MAX_CARACTERES_CAMPOS_TEXTO);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoActividades,
                etiquetaContadorActividades, MAX_CARACTERES_CAMPOS_TEXTO);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoResponsabilidades,
                etiquetaContadorResponsabilidades, MAX_CARACTERES_CAMPOS_TEXTO);

        gestorHorarios = new GestorHorarios();

        gestorHorarios.agregarDia(checkLunes, comboHoraLunesInicio, comboMinutosLunesInicio,
                comboHoraLunesFin, comboMinutosLunesFin);
        gestorHorarios.agregarDia(checkMartes, comboHoraMartesInicio, comboMinutosMartesInicio,
                comboHoraMartesFin, comboMinutosMartesFin);
        gestorHorarios.agregarDia(checkMiercoles, comboHoraMiercolesInicio, comboMinutosMiercolesInicio,
                comboHoraMiercolesFin, comboMinutosMiercolesFin);
        gestorHorarios.agregarDia(checkJueves, comboHoraJuevesInicio, comboMinutosJuevesInicio,
                comboHoraJuevesFin, comboMinutosJuevesFin);
        gestorHorarios.agregarDia(checkViernes, comboHoraViernesInicio, comboMinutosViernesInicio,
                comboHoraViernesFin, comboMinutosViernesFin);

        gestorHorarios.inicializarCombosHorarios();
        gestorHorarios.configurarHabilitacionPorDia();

        botonRegistrar.setCursor(Cursor.HAND);
        botonCancelar.setCursor(Cursor.HAND);
        botonSeleccionarRepresentante.setCursor(Cursor.HAND);
    }

    @FXML
    private void registrarProyecto() {

        String nombreProyecto = campoNombre.getText().trim();
        String descripcionProyecto = textoDescripcionGeneral.getText().trim();
        String objetivosGeneralesProyecto = textoObjetivosGenerales.getText().trim();
        String objetivosInmediatosProyecto = textoObjetivosInmediatos.getText().trim();
        String objetivosMediatosProyecto = textoObjetivosMediatos.getText().trim();
        String metodologiaProyecto = textoMetodologia.getText().trim();
        String recursosProyecto = textoRecursos.getText().trim();
        String actividadesProyecto = textoActividades.getText().trim();
        String responsabilidadesProyecto = textoResponsabilidades.getText().trim();
        String duracionProyecto = etiquetaDuracion.getText().trim();
        String textoUsuariosDirectos = campoUsuariosDirectos.getText().trim();
        String textoUsuariosIndirectos = campoUsuariosIndirectos.getText().trim();
        String textoEstudiantesRequeridos = campoEstudianteRequeridos.getText().trim();

        ValidadorDatosProyecto validadorDatosProyecto = new ValidadorDatosProyecto();

        ProyectoDTO proyectoDTO = new ProyectoDTO(nombreProyecto, objetivosGeneralesProyecto,
                objetivosInmediatosProyecto, objetivosMediatosProyecto, metodologiaProyecto,
                recursosProyecto, actividadesProyecto, responsabilidadesProyecto, descripcionProyecto);

        List<String> camposVacios = validadorDatosProyecto.camposVaciosProyecto(proyectoDTO);

        if (!camposVacios.isEmpty()) {

            String mensajeError = String.join("\n", camposVacios);
            UTILIDADES.mostrarAlerta(
                    "Campos vacíos",
                    "Por favor, complete todos los campos requeridos.",
                    mensajeError
            );
            return;
        }

        List<String> camposErroneos = validadorDatosProyecto.validarCamposProyecto(proyectoDTO);

        if (!camposErroneos.isEmpty()) {

            String mensajeError = String.join("\n", camposErroneos);
            UTILIDADES.mostrarAlerta(
                    "Errores de validación",
                    "Por favor, ingrese información válida en los campos.",
                    mensajeError
            );
            return;
        }

        List<String> usuariosInvalidos = validadorDatosProyecto.camposNumericosInvalidos(textoUsuariosDirectos,
                textoUsuariosIndirectos, textoEstudiantesRequeridos);

        if (!usuariosInvalidos.isEmpty()) {

            String mensajeError = String.join("\n", usuariosInvalidos);
            UTILIDADES.mostrarAlerta(
                    "Campos vacíos",
                    "Por favor, complete todos los campos requeridos.",
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
        int usuariosDirectos = Integer.parseInt(textoUsuariosDirectos);
        int usuariosIndirectos = Integer.parseInt(textoUsuariosIndirectos);
        int estudiantesRequedidos = Integer.parseInt(textoEstudiantesRequeridos);
        int estadoActivoProyecto = 1;
        int idProyecto = 0;

        proyectoDTO = new ProyectoDTO(
                idProyecto, nombreProyecto, objetivosGeneralesProyecto,
                objetivosInmediatosProyecto, objetivosMediatosProyecto, metodologiaProyecto,
                recursosProyecto, actividadesProyecto, responsabilidadesProyecto,
                duracionProyecto, estadoActivoProyecto, idRepresentante,
                descripcionProyecto, usuariosDirectos, usuariosIndirectos, estudiantesRequedidos
        );

        ProyectoDAO proyectoDAO = new ProyectoDAO();

        try {

            ProyectoDTO proyectoExistente = proyectoDAO.buscarProyectoPorNombre(nombreProyecto);
            int proyectoNoEncontrado = -1;

            if (proyectoExistente.getIdProyecto() != proyectoNoEncontrado) {

                UTILIDADES.mostrarAlerta(
                        "Error",
                        "El nombre del proyecto ya se encuentra registrado dentro del sistema.",
                        "Por favor, ingrese un nombre diferente."
                );
                return;
            }

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
                    "Error",
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
        String textoEstudiantesRequeridos = campoEstudianteRequeridos.getText();

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
                    campoEstudianteRequeridos.setText(textoEstudiantesRequeridos);

                    UTILIDADES.mostrarAlerta(
                            "Operación cancelada.",
                            "Los cambios no han sido descartados..",
                            "Puede continuar con el registro."
                    );
                }
        );
    }

    private boolean validarDiasSeleccionados() {
        return gestorHorarios.validarDiasSeleccionados();
    }

    private List<String> validarHorarios() {
        return gestorHorarios.validarHorarios();
    }

    private boolean insertarHorarios(int idProyecto) throws SQLException, IOException {

        HorarioProyectoDAO horarioDAO = new HorarioProyectoDAO();

        Map<CheckBox, List<ComboBox<String>>> diasConHorarios = gestorHorarios.obtenerDiasConHorarios();
        boolean horariosInsertados = false;

        for (Map.Entry<CheckBox, List<ComboBox<String>>> entrada : diasConHorarios.entrySet()) {

            CheckBox diaSeleccionado = entrada.getKey();
            List<ComboBox<String>> horarios = entrada.getValue();

            if (diaSeleccionado.isSelected() && gestorHorarios.verificarHorarioDia(horarios)) {

                String diaSemana = gestorHorarios.obtenerNombreDia(diaSeleccionado);
                int horaInicio = Integer.parseInt(horarios.get(0).getValue());
                int minutoInicio = Integer.parseInt(horarios.get(1).getValue());
                int horaFin = Integer.parseInt(horarios.get(2).getValue());
                int minutoFin = Integer.parseInt(horarios.get(3).getValue());
                Time horaInicioSQL = Time.valueOf(String.format("%02d:%02d:00", horaInicio, minutoInicio));
                Time horaFinSQL = Time.valueOf(String.format("%02d:%02d:00", horaFin, minutoFin));

                HorarioProyectoDTO horarioDTO = new HorarioProyectoDTO(
                        0, idProyecto, diaSemana, horaInicioSQL, horaFinSQL, null
                );

                horariosInsertados = horarioDAO.crearNuevoHorarioProyecto(horarioDTO);
            }
        }

        return horariosInsertados;
    }

    @Override
    public void actualizarRepresentanteYOrganizacion() {

        RepresentanteDTO representanteDTO =
                SeleccionRepresentanteOrganizacion.getRepresentanteSeleccionado();
        OrganizacionVinculadaDTO organizacionDTO =
                SeleccionRepresentanteOrganizacion.getOrganizacionSeleccionada();
    }

    @FXML
    private void abrirVentanaSeleccionRepresentante() {

        Stage ventanaActual = (Stage) botonSeleccionarRepresentante.getScene().getWindow();

        FXMLLoader cargadorVentana = new FXMLLoader(getClass().getResource("/SeleccionarRepresentante.fxml"));
        UTILIDADES.abrirVentana(
                "/SeleccionarRepresentante.fxml",
                "Seleccionar Representante",
                ventanaActual);

        ControladorSeleccionRepresentanteGUI controlador = cargadorVentana.getController();

        if (controlador != null) {
            controlador.setControladorPadre(this);
        }
    }
}
