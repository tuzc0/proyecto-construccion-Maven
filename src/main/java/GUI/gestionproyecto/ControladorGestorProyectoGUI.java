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
import logica.ManejadorExcepciones;
import logica.interfaces.IGestorAlertas;
import logica.interfaces.ISeleccionRepresentante;
import logica.utilidadesproyecto.SeleccionRepresentanteOrganizacion;
import logica.verificacion.ValidadorDatosProyecto;
import logica.verificacion.VerificicacionGeneral;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorGestorProyectoGUI implements ISeleccionRepresentante {

    private static final Logger LOGGER =
            org.apache.logging.log4j.LogManager.getLogger(ControladorGestorProyectoGUI.class);

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
    private Label etiquetaContadorNombre;
    @FXML
    private Label etiquetaContadorDescripcion;
    @FXML
    private Label etiquetaContadorObjetivoGeneral;
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

    private Utilidades gestorVentanas = new Utilidades();

    private IGestorAlertas utilidades = new Utilidades();

    private ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, LOGGER);

    private ProyectoDTO proyectoSeleccionado;

    int idRepresentante = 0;

    @FXML
    private void initialize() {

        final int MAX_CARACTERES_NOMBRE = 150;
        final int MAX_CARACTERES_CAMPOS_TEXTO = 255;

        VerificicacionGeneral verificacionGeneralUtilidad = new VerificicacionGeneral();

        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoNombre,
                etiquetaContadorNombre, MAX_CARACTERES_NOMBRE);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoDescripcionGeneral,
                etiquetaContadorDescripcion, MAX_CARACTERES_CAMPOS_TEXTO);
        verificacionGeneralUtilidad.contadorCaracteresTextArea(textoObjetivoGeneral,
                etiquetaContadorObjetivoGeneral, MAX_CARACTERES_CAMPOS_TEXTO);
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

            etiquetaRepresentante.setText(representanteDTO.getNombre() + " " +
                    representanteDTO.getApellidos());
            etiquetaOrganizacion.setText(organizacionVinculadaDTO.getNombre());

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            LOGGER.error("Error al cargar datos del proyecto: " + e);
            utilidades.mostrarAlerta(
                    "Error interno del sistema",
                    "Ocurrió un error al completar la operación.",
                    "Ocurrió un error dentro del sistema, por favor inténtelo de nuevo más tarde " +
                            "o contacte al administrador."
            );

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
            tablaHorarios.getItems().add(new ContenedorHorarioProyectoGUI(
                    lunes,
                    martes,
                    miercoles,
                    jueves,
                    viernes
            ));

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            LOGGER.error("Error al cargar horarios en la tabla: " + e);
            utilidades.mostrarAlerta(
                    "Error interno del sistema",
                    "Ocurrió un error al completar la operación.",
                    "Ocurrió un error dentro del sistema, por favor inténtelo de nuevo más tarde " +
                            "o contacte al administrador."
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

        etiquetaContadorNombre.setVisible(true);
        etiquetaContadorDescripcion.setVisible(true);
        etiquetaContadorObjetivoGeneral.setVisible(true);
        etiquetaContadorObjetivosInmediatos.setVisible(true);
        etiquetaContadorObjetivosMediatos.setVisible(true);
        etiquetaContadorMetodologia.setVisible(true);
        etiquetaContadorRecursos.setVisible(true);
        etiquetaContadorActividades.setVisible(true);
        etiquetaContadorResponsabilidades.setVisible(true);

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

        etiquetaContadorNombre.setVisible(false);
        etiquetaContadorDescripcion.setVisible(false);
        etiquetaContadorObjetivoGeneral.setVisible(false);
        etiquetaContadorObjetivosInmediatos.setVisible(false);
        etiquetaContadorObjetivosMediatos.setVisible(false);
        etiquetaContadorMetodologia.setVisible(false);
        etiquetaContadorRecursos.setVisible(false);
        etiquetaContadorActividades.setVisible(false);
        etiquetaContadorResponsabilidades.setVisible(false);

        botonEditar.setVisible(true);
        botonRegresar.setVisible(true);

        botonCambiarOrganizacion.setVisible(false);
        botonActualizar.setVisible(false);
        botonCancelar.setVisible(false);
    }

    @FXML
    private void actualizarProyecto() {

        String nombre = textoNombre.getText().trim();
        String descripcionGeneral = textoDescripcionGeneral.getText().trim();
        String objetivoGeneral = textoObjetivoGeneral.getText().trim();
        String objetivosInmediatos = textoObjetivosInmediatos.getText().trim();
        String objetivosMediatos = textoObjetivosMediatos.getText().trim();
        String metodologia = textoMetodologia.getText().trim();
        String recursos = textoRecursos.getText().trim();
        String duracion = etiquetaDuracion.getText().trim();
        String actividades = textoActividades.getText().trim();
        String responsabilidades = textoResponsabilidades.getText().trim();
        String usuariosDirectos = campoUsuariosDirectos.getText().trim();
        String usuariosIndirectos = campoUsuariosIndirectos.getText().trim();
        String estudiantesSolicitados = campoEstudiantesSolicitados.getText().trim();

        ValidadorDatosProyecto validadorDatosProyecto = new ValidadorDatosProyecto();
        ProyectoDTO proyectoDTO =
                new ProyectoDTO(nombre, objetivoGeneral, objetivosInmediatos, objetivosMediatos,
                        metodologia, recursos, actividades, responsabilidades, descripcionGeneral);

        List<String> camposVacios = validadorDatosProyecto.camposVaciosProyecto(proyectoDTO);

        if (!camposVacios.isEmpty()) {

            String mensajeError = String.join("\n", camposVacios);
            utilidades.mostrarAlerta(
                    "Campos vacíos",
                    "Por favor, complete todos los campos requeridos.",
                    mensajeError
            );
            return;
        }

        List<String> camposInvalidos = validadorDatosProyecto.validarCamposProyecto(proyectoDTO);

        if (!camposInvalidos.isEmpty()) {

            String mensajeError = String.join("\n", camposInvalidos);
            utilidades.mostrarAlerta(
                    "Campos inválidos",
                    "Algunos campos contienen información inválida.",
                    mensajeError
            );
            return;
        }

        List<String> usuariosInvalidos = validadorDatosProyecto.camposNumericosInvalidos(usuariosDirectos,
                usuariosIndirectos, estudiantesSolicitados);

        if (!usuariosInvalidos.isEmpty()) {

            String mensajeError = String.join("\n", usuariosInvalidos);
            utilidades.mostrarAlerta(
                    "Campos Inválidos",
                    "Algunos campos contienen información invalida.",
                    mensajeError
            );
        }

        int idRepresentanteFinal = representanteCambiado && representanteSeleccionadoTemporal != null
                ? representanteSeleccionadoTemporal.getIDRepresentante()
                : idRepresentante;

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
        proyectoSeleccionado.setIdRepresentante(idRepresentanteFinal);
        proyectoSeleccionado.setUsuariosDirectos(numeroUsuariosDirectos);
        proyectoSeleccionado.setUsuariosIndirectos(numeroUsuariosIndirectos);
        proyectoSeleccionado.setEstudiantesRequeridos(numeroEstudiantesSolicitados);

        ProyectoDAO proyectoDAO = new ProyectoDAO();

        try {

            boolean proyectoModificado = proyectoDAO.modificarProyecto(proyectoSeleccionado);

            if (proyectoModificado) {

                SeleccionRepresentanteOrganizacion.
                        setRepresentanteSeleccionado(representanteSeleccionadoTemporal);
                SeleccionRepresentanteOrganizacion.
                        setOrganizacionSeleccionada(organizacionVinculadaSeleccionadaTemporal);

                utilidades.mostrarAlerta(
                        "Modificación exitosa.",
                        "Se ha modificado con éxito el proyecto.",
                        "");

                deshabilitarCamposParaEdicion();
                cargarDatosProyecto();
                representanteSeleccionadoTemporal = null;
                organizacionVinculadaSeleccionadaTemporal = null;

            } else {

                utilidades.mostrarAlerta(
                        "No se pudo modificar el proyecto.",
                        "Ocurrió un error al modificar el proyecto.",
                        "Por favor contacte al administrador.");
            }

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            LOGGER.error("Error al actualizar el proyecto: " + e);
            utilidades.mostrarAlerta(
                    "Error interno del sistema",
                    "Ocurrió un error al completar la operación.",
                    "Ocurrió un error dentro del sistema, por favor inténtelo de nuevo más tarde " +
                            "o contacte al administrador."
            );
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

        gestorVentanas.mostrarAlertaConfirmacion(

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

                    utilidades.mostrarAlerta(
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

                etiquetaRepresentante.setText(representanteSeleccionadoTemporal.getNombre() + " " +
                        representanteSeleccionadoTemporal.getApellidos());
                etiquetaOrganizacion.setText(organizacionVinculadaSeleccionadaTemporal.getNombre());
            });
        }
    }

    @FXML
    private void abrirVentanaSeleccionRepresentante() {

        try {

            Stage ventanaActual = (Stage) botonCambiarOrganizacion.getScene().getWindow();

            FXMLLoader cargarFXML = new FXMLLoader(getClass().getResource("/SeleccionarRepresentante.fxml"));
            Parent contenidoVentana = cargarFXML.load();

            ControladorSeleccionRepresentanteGUI controladorSeleccionRepresentante = cargarFXML.getController();
            controladorSeleccionRepresentante.setControladorPadre(this);

            Stage nuevaVentana = new Stage();
            nuevaVentana.setScene(new Scene(contenidoVentana));
            nuevaVentana.setTitle("Seleccionar Representante");
            nuevaVentana.initModality(Modality.WINDOW_MODAL);
            nuevaVentana.initOwner(ventanaActual);

            nuevaVentana.showAndWait();

            if (SeleccionRepresentanteOrganizacion.getRepresentanteSeleccionado() != null) {
                actualizarRepresentanteYOrganizacion();
            }

        } catch (IOException e) {

            LOGGER.error("Error al abrir la ventana de selección de representante: " + e);
            utilidades.mostrarAlerta(
                    "Error de sistema",
                    "No se pudo abrir la ventana de selección.",
                    "Verifique el archivo de interfaz o contacte al administrador.");

        } catch (Exception e) {

            LOGGER.error("Error inesperado al abrir la ventana de selección de representante: " + e);
            utilidades.mostrarAlerta(
                    "Error interno del sistema",
                    "Ocurrió un error al completar la operación.",
                    "Ocurrió un error dentro del sistema, por favor inténtelo de nuevo más tarde " +
                            "o contacte al administrador."
            );
        }
    }

    @FXML
    private void regresarAConsultarProyecto() {

        gestorVentanas.mostrarAlertaConfirmacion(
                "Confirmar regreso",
                "¿Está seguro que desea regresar a la ventana anterior?",
                "",
                () -> {

                    if (controladorPadre != null) {
                        controladorPadre.cargarRepresentanteYProyecto();
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
