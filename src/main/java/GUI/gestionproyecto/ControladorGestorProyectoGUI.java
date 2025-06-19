package GUI.gestionproyecto;

import GUI.utilidades.Utilidades;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logica.DAOs.HorarioProyectoDAO;
import logica.DAOs.OrganizacionVinculadaDAO;
import logica.DAOs.ProyectoDAO;
import logica.DAOs.RepresentanteDAO;
import logica.DTOs.HorarioProyectoDTO;
import logica.DTOs.OrganizacionVinculadaDTO;
import logica.DTOs.ProyectoDTO;
import logica.DTOs.RepresentanteDTO;
import logica.interfaces.ISeleccionRepresentante;
import logica.utilidadesproyecto.SeleccionRepresentanteOrganizacion;
import logica.verificacion.VerificadorDatosProyecto;
import logica.verificacion.VerificicacionGeneral;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ControladorGestorProyectoGUI implements ISeleccionRepresentante {

    private static final Logger LOGGER = Logger
            .getLogger(ControladorGestorProyectoGUI.class.getName());

    @FXML
    private TextArea textoNombre;
    @FXML
    private TextArea textoDescripcionGeneral;
    @FXML
    private TextArea textoObjetivoGeneral;
    @FXML
    private Label etiquetaUsuariosDirectos;
    @FXML
    private TextField campoUsuariosDirectos;
    @FXML
    private Label etiquetaUsuariosIndirectos;
    @FXML
    private Label etiquetaEstudiantesSolicitados;
    @FXML
    private TextField campoUsuariosIndirectos;
    @FXML
    private TextField campoEstudiantesSolicitados;
    @FXML
    private Label etiquetaDuracion;
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
    private TableView tablaHorarios;
    @FXML
    private TableColumn<ContenedorHorarioProyectoGUI, String> columnaLunes;
    @FXML
    private TableColumn<ContenedorHorarioProyectoGUI, String> columnaMartes;
    @FXML
    private TableColumn<ContenedorHorarioProyectoGUI, String> columnaMiercoles;
    @FXML
    private TableColumn<ContenedorHorarioProyectoGUI, String> columnaJueves;
    @FXML
    private TableColumn<ContenedorHorarioProyectoGUI, String> columnaViernes;
    @FXML
    private Button botonEditar;
    @FXML
    private Button botonRegresar;
    @FXML
    private Button botonActualizar;
    @FXML
    private Button botonCancelar;
    @FXML
    private Button botonCambiarOrganizacion;
    @FXML
    private Label etiquetaOrganizacion;
    @FXML
    private Label etiquetaRepresentante;
    @FXML
    private Label contadorNombre;
    @FXML
    private Label contadorDescripcion;
    @FXML
    private Label contadorObjetivoGeneral;
    @FXML
    private Label contadorObjetivosInmediatos;
    @FXML
    private Label contadorObjetivosMediatos;
    @FXML
    private Label contadorMetodologia;
    @FXML
    private Label contadorRecursos;
    @FXML
    private Label contadorActividades;
    @FXML
    private Label contadorResponsabilidades;

    private final Utilidades UTILIDADES = new Utilidades();
    private ProyectoDTO proyectoSeleccionado;
    int idRepresentante = 0;

    @FXML
    private void initialize() {

        final int MAX_CARACTERES_NOMBRE = 150;
        final int MAX_CARACTERES_CAMPOS_TEXTO = 255;

        VerificicacionGeneral verificacionGeneralUtilidad = new VerificicacionGeneral();

        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoNombre,
                contadorNombre, MAX_CARACTERES_NOMBRE);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoDescripcionGeneral,
                contadorDescripcion, MAX_CARACTERES_CAMPOS_TEXTO);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoObjetivoGeneral,
                contadorObjetivoGeneral, MAX_CARACTERES_CAMPOS_TEXTO);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoObjetivosInmediatos,
                contadorObjetivosInmediatos, MAX_CARACTERES_CAMPOS_TEXTO);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoObjetivosMediatos,
                contadorObjetivosMediatos, MAX_CARACTERES_CAMPOS_TEXTO);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoMetodologia,
                contadorMetodologia, MAX_CARACTERES_CAMPOS_TEXTO);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoRecursos,
                contadorRecursos, MAX_CARACTERES_CAMPOS_TEXTO);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoActividades,
                contadorActividades, MAX_CARACTERES_CAMPOS_TEXTO);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoResponsabilidades,
                contadorResponsabilidades, MAX_CARACTERES_CAMPOS_TEXTO);

        columnaLunes.setCellValueFactory(new PropertyValueFactory<>("lunes"));
        columnaMartes.setCellValueFactory(new PropertyValueFactory<>("martes"));
        columnaMiercoles.setCellValueFactory(new PropertyValueFactory<>("miercoles"));
        columnaJueves.setCellValueFactory(new PropertyValueFactory<>("jueves"));
        columnaViernes.setCellValueFactory(new PropertyValueFactory<>("viernes"));

        botonEditar.setCursor(Cursor.HAND);
        botonRegresar.setCursor(Cursor.HAND);
    }

    public void setProyectoDTO(ProyectoDTO proyectoDTO) {

        this.proyectoSeleccionado = proyectoDTO;
        cargarDatosProyecto();
    }

    @FXML
    private void cargarDatosProyecto() {

        textoNombre.setText(proyectoSeleccionado.getNombre());
        textoDescripcionGeneral.setText(proyectoSeleccionado.getDescripcion());
        textoObjetivoGeneral.setText(proyectoSeleccionado.getObjetivoGeneral());
        etiquetaUsuariosDirectos.setText(String.valueOf(proyectoSeleccionado.getUsuariosDirectos()));
        etiquetaUsuariosIndirectos.setText(String.valueOf(proyectoSeleccionado.getUsuariosIndirectos()));
        etiquetaEstudiantesSolicitados.setText(String.valueOf(proyectoSeleccionado.getEstudiantesRequeridos()));
        etiquetaDuracion.setText(proyectoSeleccionado.getDuracion());
        textoObjetivosInmediatos.setText(proyectoSeleccionado.getObjetivosInmediatos());
        textoObjetivosMediatos.setText(proyectoSeleccionado.getObjetivosMediatos());
        textoMetodologia.setText(proyectoSeleccionado.getMetodologia());
        textoRecursos.setText(proyectoSeleccionado.getRecursos());
        textoActividades.setText(proyectoSeleccionado.getActividades());
        textoResponsabilidades.setText(proyectoSeleccionado.getResponsabilidades());
        idRepresentante = proyectoSeleccionado.getIdRepresentante();

        RepresentanteDAO represetanteDAO = new RepresentanteDAO();
        OrganizacionVinculadaDAO organizacionDAO = new OrganizacionVinculadaDAO();

        try {

            RepresentanteDTO representanteDTO =
                    represetanteDAO.buscarRepresentantePorID(proyectoSeleccionado.getIdRepresentante());
            OrganizacionVinculadaDTO organizacionVinculadaDTO =
                    organizacionDAO.buscarOrganizacionPorID(representanteDTO.getIdOrganizacion());

            etiquetaRepresentante.setText(representanteDTO.getNombre() + " " + representanteDTO.getApellidos());
            etiquetaOrganizacion.setText(organizacionVinculadaDTO.getNombre());

        } catch (SQLException e) {

            LOGGER.severe("Error al cargar los horarios: " + e);
            UTILIDADES.mostrarAlerta("Error al cargar datos del encargado del proyecto.",
                    "Ocurrio un errro al cargar los datos del representante y Organización",
                    "Por favor, intentelo de nuevo más tarde o contacte a soporte.");

        } catch (IOException e) {

            LOGGER.severe("Error al cargar los horarios: " + e);
            UTILIDADES.mostrarAlerta("Error al cargar datos del encargado del proyecto.",
                    "Ocurrio un errro al cargar los datos del representante y Organización",
                    "Por favor, intentelo de nuevo más tarde o contacte a soporte.");

        }

        cargarHorariosEnTabla(proyectoSeleccionado.getIdProyecto());
    }

    @FXML
    private void cargarHorariosEnTabla(int idProyecto) {

        HorarioProyectoDAO horarioProyectoDAO = new HorarioProyectoDAO();

        try {

            List<HorarioProyectoDTO> horarios =
                    horarioProyectoDAO.buscarHorarioPorIdDeProyecto(idProyecto);

            String lunes = "";
            String martes = "";
            String miercoles = "";
            String jueves = "";
            String viernes = "";

            for (HorarioProyectoDTO horario : horarios) {

                String dia = horario.getDiaSemana().replace("[", "").replace("]", "");
                String horarioTexto = horario.getHoraInicio() + " - " + horario.getHoraFin();

                switch (dia) {

                    case "Lunes":

                        lunes = horarioTexto;
                        break;

                    case "Martes":

                        martes = horarioTexto;
                        break;

                    case "Miercoles":

                        miercoles = horarioTexto;
                        break;

                    case "Jueves":

                        jueves = horarioTexto;
                        break;

                    case "Viernes":

                        viernes = horarioTexto;
                        break;
                }
            }

            tablaHorarios.getItems().clear();
            tablaHorarios.getItems().add(
                    new ContenedorHorarioProyectoGUI(lunes, martes, miercoles, jueves, viernes));

        } catch (SQLException e) {

            LOGGER.warning("Error al cargar los horarios: " + e);
            UTILIDADES.mostrarAlerta(
                    "Error",
                    "No se pudieron cargar los horarios",
                    "Error al cargar los horarios del proyecto."
            );

        } catch (IOException e) {

            LOGGER.warning("Error al cargar los horarios: " + e);
            UTILIDADES.mostrarAlerta(
                    "Error",
                    "No se pudieron cargar los horarios",
                    "Error al cargar los horarios del proyecto."
            );
        }
    }

    @FXML
    private void habilitarCamposParaEdicion() {

        textoNombre.setEditable(true);
        textoDescripcionGeneral.setEditable(true);
        textoObjetivoGeneral.setEditable(true);
        textoObjetivosInmediatos.setEditable(true);
        textoObjetivosMediatos.setEditable(true);
        textoMetodologia.setEditable(true);
        textoRecursos.setEditable(true);
        textoActividades.setEditable(true);
        textoResponsabilidades.setEditable(true);

        contadorNombre.setVisible(true);
        contadorDescripcion.setVisible(true);
        contadorObjetivoGeneral.setVisible(true);
        contadorObjetivosInmediatos.setVisible(true);
        contadorObjetivosMediatos.setVisible(true);
        contadorMetodologia.setVisible(true);
        contadorRecursos.setVisible(true);
        contadorActividades.setVisible(true);
        contadorResponsabilidades.setVisible(true);

        campoUsuariosDirectos.setText(etiquetaUsuariosDirectos.getText());
        campoUsuariosIndirectos.setText(etiquetaUsuariosIndirectos.getText());
        campoEstudiantesSolicitados.setText(etiquetaEstudiantesSolicitados.getText());

        etiquetaUsuariosDirectos.setVisible(false);
        etiquetaUsuariosIndirectos.setVisible(false);
        etiquetaEstudiantesSolicitados.setVisible(false);
        botonEditar.setVisible(false);
        botonRegresar.setVisible(false);

        botonCambiarOrganizacion.setVisible(true);
        botonActualizar.setVisible(true);
        botonCancelar.setVisible(true);
        campoUsuariosDirectos.setVisible(true);
        campoUsuariosIndirectos.setVisible(true);
        campoEstudiantesSolicitados.setVisible(true);
    }

    private void deshabilitarCamposParaEdicion() {

        textoNombre.setEditable(false);
        textoDescripcionGeneral.setEditable(false);
        textoObjetivoGeneral.setEditable(false);
        textoObjetivosInmediatos.setEditable(false);
        textoObjetivosMediatos.setEditable(false);
        textoMetodologia.setEditable(false);
        textoRecursos.setEditable(false);
        textoActividades.setEditable(false);
        textoResponsabilidades.setEditable(false);
        etiquetaUsuariosDirectos.setVisible(true);
        etiquetaUsuariosIndirectos.setVisible(true);
        etiquetaEstudiantesSolicitados.setVisible(true);
        campoUsuariosDirectos.setVisible(false);
        campoUsuariosIndirectos.setVisible(false);
        campoEstudiantesSolicitados.setVisible(false);

        contadorNombre.setVisible(false);
        contadorDescripcion.setVisible(false);
        contadorObjetivoGeneral.setVisible(false);
        contadorObjetivosInmediatos.setVisible(false);
        contadorObjetivosMediatos.setVisible(false);
        contadorMetodologia.setVisible(false);
        contadorRecursos.setVisible(false);
        contadorActividades.setVisible(false);
        contadorResponsabilidades.setVisible(false);

        botonEditar.setVisible(true);
        botonRegresar.setVisible(true);

        botonCambiarOrganizacion.setVisible(false);
        botonActualizar.setVisible(false);
        botonCancelar.setVisible(false);
    }

    @FXML
    private void actualizarProyecto() {

        String nombre = textoNombre.getText();
        String descripcionGeneral = textoDescripcionGeneral.getText();
        String objetivoGeneral = textoObjetivoGeneral.getText();
        String objetivosInmediatos = textoObjetivosInmediatos.getText();
        String objetivosMediatos = textoObjetivosMediatos.getText();
        String metodologia = textoMetodologia.getText();
        String recursos = textoRecursos.getText();
        String duracion = etiquetaDuracion.getText();
        String actividades = textoActividades.getText();
        String responsabilidades = textoResponsabilidades.getText();
        String usuariosDirectos = campoUsuariosDirectos.getText();
        String usuariosIndirectos = campoUsuariosIndirectos.getText();
        String estudiantesSolicitados = campoEstudiantesSolicitados.getText();

        VerificadorDatosProyecto verificadorDatosProyecto = new VerificadorDatosProyecto();

        List<String> camposVacios = verificadorDatosProyecto.camposVaciosProyecto(nombre, descripcionGeneral,
                objetivoGeneral, objetivosInmediatos, objetivosMediatos, metodologia, recursos,
                actividades, responsabilidades, usuariosDirectos, usuariosIndirectos, estudiantesSolicitados);

        if (!camposVacios.isEmpty()) {
            String mensajeError = String.join("\n", camposVacios);
            UTILIDADES.mostrarAlerta(
                    "Campos vacíos",
                    "Por favor, complete todos los campos requeridos.",
                    mensajeError
            );
            return;
        }

        List<String> camposInvalidos = verificadorDatosProyecto.validarCamposProyecto(nombre, descripcionGeneral,
                objetivoGeneral, objetivosInmediatos, objetivosMediatos, metodologia, recursos,
                actividades, responsabilidades, usuariosDirectos, usuariosIndirectos, estudiantesSolicitados);

        if (!camposInvalidos.isEmpty()) {
            String mensajeError = String.join("\n", camposInvalidos);
            UTILIDADES.mostrarAlerta(
                    "Campos inválidos",
                    "Por favor, algunos campos contienen información inválida.",
                    mensajeError
            );
            return;
        }

        if (representanteCambiado) {

            idRepresentante = representanteSeleccionadoTemporal.getIDRepresentante();
        }

        int numeroUsuariosDirectos = Integer.parseInt(usuariosDirectos);
        int numeroUsuariosIndirectos = Integer.parseInt(usuariosIndirectos);
        int numeroEstudiantesSolicitados = Integer.parseInt(estudiantesSolicitados);

        proyectoSeleccionado.setNombre(nombre);
        proyectoSeleccionado.setDescripcion(descripcionGeneral);
        proyectoSeleccionado.setObjetivoGeneral(objetivoGeneral);
        proyectoSeleccionado.setObjetivosInmediatos(objetivosInmediatos);
        proyectoSeleccionado.setObjetivosMediatos(objetivosMediatos);
        proyectoSeleccionado.setMetodologia(metodologia);
        proyectoSeleccionado.setRecursos(recursos);
        proyectoSeleccionado.setActividades(actividades);
        proyectoSeleccionado.setResponsabilidades(responsabilidades);
        proyectoSeleccionado.setDuracion(duracion);
        proyectoSeleccionado.setIdRepresentante(idRepresentante);
        proyectoSeleccionado.setUsuariosDirectos(numeroUsuariosDirectos);
        proyectoSeleccionado.setUsuariosIndirectos(numeroUsuariosIndirectos);
        proyectoSeleccionado.setestudiantesRequeridos(numeroEstudiantesSolicitados);

        ProyectoDAO proyectoDAO = new ProyectoDAO();

        try {

            boolean proyectoModificado = proyectoDAO.modificarProyecto(proyectoSeleccionado);

            if (proyectoModificado) {

                SeleccionRepresentanteOrganizacion.
                        setRepresentanteSeleccionado(representanteSeleccionadoTemporal);
                SeleccionRepresentanteOrganizacion.
                        setOrganizacionSeleccionada(organizacionVinculadaSeleccionadaTemporal);

                UTILIDADES.mostrarAlerta("Modificación exitosa.",
                        "Se ha modificado con éxito el proyecto.",
                        "");
                deshabilitarCamposParaEdicion();
                cargarDatosProyecto();
                representanteSeleccionadoTemporal = null;
                organizacionVinculadaSeleccionadaTemporal = null;

            } else {

                UTILIDADES.mostrarAlerta("No se pudo modificar el proyecto.",
                        "Ocurrió un error al modificar el proyecto.",
                        "Por favor contacte al administrador.");
            }

        } catch (SQLException | IOException e) {

            LOGGER.warning("Error al modificar el proyecto: " + e);
            UTILIDADES.mostrarAlerta("Error al actualizar.",
                    "Ocurrió un error al intentar actualizar los datos del proyecto.",
                    "Por favor, intente de nuevo más tarde o contacte al administrador.");
        }
    }


    @FXML
    private void cancelarActualizacionProyecto() {

        String nombreProyecto = textoNombre.getText();
        String descripcionProyecto = textoDescripcionGeneral.getText();
        String objetivosGeneralesProyecto = textoObjetivoGeneral.getText();
        String objetivosInmediatosProyecto = textoObjetivosInmediatos.getText();
        String objetivosMediatosProyecto = textoObjetivosMediatos.getText();
        String metodologiaProyecto = textoMetodologia.getText();
        String recursosProyecto = textoRecursos.getText();
        String actividadesProyecto = textoActividades.getText();
        String responsabilidadesProyecto = textoResponsabilidades.getText();
        String duracionProyecto = etiquetaDuracion.getText();
        String textoUsuariosDirectos = campoUsuariosDirectos.getText();
        String textoUsuariosIndirectos = campoUsuariosIndirectos.getText();
        String textoEstudiantesSolicitados = campoEstudiantesSolicitados.getText();
        representanteSeleccionadoTemporal = null;
        organizacionVinculadaSeleccionadaTemporal = null;

        UTILIDADES.mostrarAlertaConfirmacion(
                "Confirmar cancelación",
                "¿Está seguro que desea cancelar?",
                "Los cambios no guardados se perderán",
                () -> {

                    cargarDatosProyecto();
                    deshabilitarCamposParaEdicion();

                },
                () -> {

                    textoNombre.setText(nombreProyecto);
                    textoDescripcionGeneral.setText(descripcionProyecto);
                    textoObjetivoGeneral.setText(objetivosGeneralesProyecto);
                    textoObjetivosInmediatos.setText(objetivosInmediatosProyecto);
                    textoObjetivosMediatos.setText(objetivosMediatosProyecto);
                    textoMetodologia.setText(metodologiaProyecto);
                    textoRecursos.setText(recursosProyecto);
                    textoActividades.setText(actividadesProyecto);
                    textoResponsabilidades.setText(responsabilidadesProyecto);
                    etiquetaDuracion.setText(duracionProyecto);
                    campoUsuariosDirectos.setText(textoUsuariosDirectos);
                    campoUsuariosIndirectos.setText(textoUsuariosIndirectos);
                    campoEstudiantesSolicitados.setText(textoEstudiantesSolicitados);

                    UTILIDADES.mostrarAlerta(
                            "Operación cancelada.",
                            "Los cambios no han sido descartados..",
                            "Puede continuar con las modificaciones."
                    );
                }
        );
    }

    private RepresentanteDTO representanteSeleccionadoTemporal;
    private OrganizacionVinculadaDTO organizacionVinculadaSeleccionadaTemporal;
    private boolean representanteCambiado = false;

    @Override
    public void actualizarRepresentanteYOrganizacion() {

        this.representanteSeleccionadoTemporal =
                SeleccionRepresentanteOrganizacion.getRepresentanteSeleccionado();
        this.organizacionVinculadaSeleccionadaTemporal =
                SeleccionRepresentanteOrganizacion.getOrganizacionSeleccionada();

        if (representanteSeleccionadoTemporal != null && organizacionVinculadaSeleccionadaTemporal != null) {

            representanteCambiado = true;

            Platform.runLater(() -> {

                etiquetaRepresentante.setText(
                        representanteSeleccionadoTemporal.getNombre() + " " +
                                representanteSeleccionadoTemporal.getApellidos());
                etiquetaOrganizacion.setText(organizacionVinculadaSeleccionadaTemporal.getNombre());
            });
        }
    }

    @FXML
    private void abrirVentanaSeleccionRepresentante() {

        try {

            Stage ventanaActual = (Stage) botonCambiarOrganizacion.getScene().getWindow();

            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/SeleccionarRepresentante.fxml"));
            Parent root = cargador.load();

            ControladorSeleccionRepresentanteGUI controlador = cargador.getController();
            controlador.setControladorPadre(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Seleccionar Representante");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(ventanaActual);

            stage.showAndWait();

            if (SeleccionRepresentanteOrganizacion.getRepresentanteSeleccionado() != null) {
                actualizarRepresentanteYOrganizacion();
            }

        } catch (IOException e) {

            LOGGER.severe("Error al abrir ventana de selección: " + e);
            UTILIDADES.mostrarAlerta("Error",
                    "No se pudo abrir la ventana de selección",
                    "Por favor, intente nuevamente o contacte al administrador.");
        }
    }

    @FXML
    private void regresarAConsultarProyecto() {

        UTILIDADES.mostrarAlertaConfirmacion(
                "Confirmar regreso",
                "¿Está seguro que desea regresar a la ventana anterior?",
                "",
                () -> {
                    if (controladorPadre != null) {
                        controladorPadre.cargarProyectoYOrganizacion();
                    }
                    Stage ventanaActual = (Stage) botonCancelar.getScene().getWindow();
                    ventanaActual.close();
                },
                () -> {

                }
        );
    }

    private ControladorConsultarProyectosGUI controladorPadre;

    public void setControladorPadre(ControladorConsultarProyectosGUI controladorPadre) {

        this.controladorPadre = controladorPadre;
    }
}
