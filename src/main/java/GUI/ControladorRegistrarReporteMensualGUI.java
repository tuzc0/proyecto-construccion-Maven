package GUI;

import GUI.utilidades.Utilidades;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logica.*;
import logica.DAOs.*;
import logica.DTOs.*;
import logica.interfaces.IGestorAlertas;
import logica.verificacion.ValidarFechas;
import logica.verificacion.VerificicacionGeneral;
import org.apache.logging.log4j.Logger;
import logica.ManejadorExcepciones;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

public class ControladorRegistrarReporteMensualGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorRegistrarReporteMensualGUI.class);

    @FXML
    Label etiquetaMatricula;

    @FXML
    Label etiquetaFecha;

    @FXML
    TextField campoMetodologia;

    @FXML
    TextField campoObservaciones;

    @FXML
    TextField campoHoras;

    @FXML
    ComboBox <String> comboActividades;

    @FXML
    DatePicker fechaInicio;

    @FXML
    DatePicker fechaFin;

    @FXML
    TableView <ContenedorActividadesReporte> tablaActividades;

    @FXML
    TableColumn <ContenedorActividadesReporte, String> columnaActividad;

    @FXML
    TableColumn <ContenedorActividadesReporte, String> columnaFechaInicio;

    @FXML
    TableColumn <ContenedorActividadesReporte, String> columnaFechaFin;

    @FXML
    private ListView<String> listaArchivos;

    @FXML
    private Label etiquetaErrorArchivos;

    @FXML
    private Label etiquetaContadorMetodologia;

    @FXML
    private Label etiquetaContadorObservaciones;

    @FXML
    private Label etiquetaContadorHoras;

    String matricula = ControladorInicioDeSesionGUI.matricula;

    private Utilidades gestorVentanas = new Utilidades();
    private IGestorAlertas utilidades = new Utilidades();
    private Map<String, String> urlsDocumentos = new HashMap<>();
    private VerificicacionGeneral verificacionGeneral = new VerificicacionGeneral();
    private List<java.io.File> archivosLocales = new ArrayList<>();
    public static VerificacionEntradas verificacionEntradas = new VerificacionEntradas();
    private ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, logger);
    private ValidarFechas validarFechas = new ValidarFechas();

    int idCronograma = 0;
    public static int idReporte = 0;
    private static final double TAMANO_MAXIMO_MB = 10.0;
    private final int MAX_CARACTERES_METODOLOGIA = 100;
    private final int MAX_CARACTERES_OBSERVACIONES = 255;
    private final int MAX_CARACTERES_HORAS = 2;

    @FXML
    public void initialize() {

        verificacionGeneral.contadorCaracteresTextField(
                campoMetodologia,
                etiquetaContadorMetodologia,
                MAX_CARACTERES_METODOLOGIA
        );

        verificacionGeneral.contadorCaracteresTextField(
                campoObservaciones,
                etiquetaContadorObservaciones,
                MAX_CARACTERES_OBSERVACIONES
        );

        verificacionGeneral.contadorCaracteresTextField(
                campoHoras,
                etiquetaContadorHoras,
                MAX_CARACTERES_HORAS
        );


        etiquetaMatricula.setText(matricula);
        etiquetaFecha.setText(LocalDate.now().toString());

        columnaActividad.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getActividadDTO().getNombre()));
        columnaFechaInicio.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getReporteContieneDTO().getFechaInicioReal().toString()));
        columnaFechaFin.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getReporteContieneDTO().getFechaFinReal().toString()));

        cargarComboActividades();
        crearReporteVacio();
        cargarActividades();

    }

    @FXML
    private void seleccionarArchivo() {

        SelectorArchivos selector = new SelectorArchivos(
                listaArchivos.getScene().getWindow(),
                "Seleccionar archivos de evidencia"
        );

        List<java.io.File> archivosSeleccionados = selector.seleccionarMultiplesArchivos();

        if (archivosSeleccionados != null && !archivosSeleccionados.isEmpty()) {

            if (SelectorArchivos.validarArchivos(archivosSeleccionados, TAMANO_MAXIMO_MB)) {

                archivosLocales.addAll(archivosSeleccionados);
                actualizarListaArchivos();
                etiquetaErrorArchivos.setText("");

            } else {
                etiquetaErrorArchivos.setText("Archivos no válidos. Verifique tamaño y formatos.");
            }
        }
    }

    @FXML
    public void guardarReporte () {

        String metodologia = campoMetodologia.getText();
        String observaciones = campoObservaciones.getText();
        String horasTexto = campoHoras.getText();

        List<String> errores = validarReporte(metodologia, observaciones, horasTexto);

        if (tablaActividades.getItems().isEmpty()) {

            errores.add("Debe añadir al menos una actividad al reporte.");

        }

        if (listaArchivos.getItems().isEmpty()) {

            errores.add("Debe adjuntar al menos un archivo de evidencia.");

        }

        if (!errores.isEmpty()) {

            gestorVentanas.mostrarAlerta("Errores de validación",
                    "Por favor, corrija los siguientes errores:",
                    String.join("\n", errores));
            return;
        }

        try {

            ReporteDAO reporteDAO = new ReporteDAO();
            ReporteDTO reporteDTO = new ReporteDTO();

            reporteDTO.setMatricula(matricula);
            reporteDTO.setFecha(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
            reporteDTO.setObservaciones(campoObservaciones.getText());
            reporteDTO.setMetodologia(campoMetodologia.getText());
            reporteDTO.setNumeroHoras(Integer.parseInt(campoHoras.getText()));
            reporteDTO.setIDReporte(idReporte);

            reporteDAO.modificarReporte(reporteDTO);
            guardarEvidenciaReporte();

            gestorVentanas.mostrarAlerta("Éxito",
                    "Reporte guardado",
                    "El reporte se ha guardado correctamente.");


            Stage stage = (Stage) campoMetodologia.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al guardar el reporte: ", e);
            gestorVentanas.mostrarAlerta("Error",
                    "Ocurrió un error inesperado",
                    "Por favor, intente nuevamente más tarde.");
        }
    }

    private static List<String> validarReporte (String metodologia, String observaciones, String horas) {

        List<String> errores = new ArrayList<>();

        if (metodologia.isEmpty()) {

            errores.add("El campo de metodología no puede estar vacío.");

        } else if (!verificacionEntradas.validarTextoAlfanumerico(metodologia)) {

            errores.add("La metodología contiene caracteres invalidos.");
        }

        if (observaciones.isEmpty()) {

            errores.add("El campo de observaciones no puede estar vacío.");

        } else if (!verificacionEntradas.validarTextoAlfanumerico(observaciones)) {

            errores.add("Las observaciones contienen caracteres invalidos.");
        }

        if (horas.isEmpty()) {

            errores.add("El campo de horas no puede estar vacío.");

        } else if (!verificacionEntradas.esEnteroPositivo(horas)) {

            errores.add("El número de horas debe ser un número entero positivo.");

        }

        return errores;

    }

    private void actualizarListaArchivos() {

        listaArchivos.getItems().clear();

        for (java.io.File archivo : archivosLocales) {

            String nombre = archivo.getName();
            String extension = nombre.substring(nombre.lastIndexOf('.') + 1).toUpperCase();
            double sizeMB = archivo.length() / (1024.0 * 1024.0);

            listaArchivos.getItems().add(String.format(
                    "%s (%s, %.2f MB)",
                    nombre, extension, sizeMB
            ));
        }
    }


    private void guardarEvidenciaReporte() {

        if (archivosLocales.isEmpty()) {

            etiquetaErrorArchivos.setText("Debe adjuntar al menos un archivo de evidencia");
            return;
        }

        try {

            SubidorArchivosDrive subidor = new SubidorArchivosDrive("1LGx2JpfFoNvqm7Dodf6i8Q2ijqDDONzR");
            urlsDocumentos = subidor.subirArchivos(archivosLocales);

            subirUrlBD();

            gestorVentanas.mostrarAlerta("Subida Exitosa", "Archivos subidos correctamente",
                    "Se subieron " + urlsDocumentos.size() + " archivos a Google Drive");

        } catch (Exception e) {

            etiquetaErrorArchivos.setText("Error al subir archivos: " + e);
            logger.error("Error al subir archivos: ", e);

        }
    }


    private void subirUrlBD() {

        try{

            EvidenciaReporteDAO evidenciaDAO = new EvidenciaReporteDAO();

            for (File archivo : archivosLocales) {

                String url = urlsDocumentos.get(archivo.getName());

                if (url != null) {

                    EvidenciaReporteDTO evidencia = new EvidenciaReporteDTO();
                    evidencia.setIdEvidencia(0);
                    evidencia.setURL(url);
                    evidencia.setIdReporte(idReporte);

                    if (!evidenciaDAO.insertarEvidenciaReporte(evidencia)) {

                        logger.error("Error al insertar evidencia: " + archivo.getName());
                    }
                }
            }

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al insertar evidencia: " + e);
            gestorVentanas.mostrarAlerta("Error", "Error inesperado al insertar evidencia",
                    "No se pudo insertar la evidencia en la base de datos. Verifique los registros.");
        }
    }

    public void cargarComboActividades () {

        CronogramaActividadesDAO cronogramaActividadesDAO = new CronogramaActividadesDAO();
        CronogramaContieneDAO cronogramaContieneDAO = new CronogramaContieneDAO();
        ActividadDAO actividadDAO = new ActividadDAO();

        try{
            CronogramaActividadesDTO cronogramaActividadesDTO = cronogramaActividadesDAO.buscarCronogramaPorMatricula(matricula);
            System.out.println("Cronograma encontrado: " + cronogramaActividadesDTO.getIDCronograma());

            idCronograma = cronogramaActividadesDTO.getIDCronograma();
            List<CronogramaContieneDTO> cronogramaContieneList = cronogramaContieneDAO.listarCronogramaContienePorID(idCronograma);

            for (CronogramaContieneDTO cronogramaContiene : cronogramaContieneList) {
                ActividadDTO actividadDTO = actividadDAO.buscarActividadPorID(cronogramaContiene.getIdActividad());
                comboActividades.getItems().add(actividadDTO.getNombre() + actividadDTO.getIDActividad());
            }


        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al buscar el cronograma por matrícula: " + e);
            gestorVentanas.mostrarAlerta("Error",
                    "No se pudo cargar el cronograma de actividades.",
                    "Por favor, intente nuevamente más tarde.");

        }

    }


    public void crearReporteVacio () {

        ReporteDAO reporteDAO = new ReporteDAO();

        try{

            ReporteDTO reporteDTO = new ReporteDTO();
            reporteDTO.setMatricula(matricula);
            reporteDTO.setFecha(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
            reporteDTO.setObservaciones("");
            reporteDTO.setMetodologia("");
            reporteDTO.setNumeroHoras(0);
            reporteDTO.setIDReporte(0);

            idReporte = reporteDAO.insertarReporte(reporteDTO);


        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al crear el reporte: " + e);
            gestorVentanas.mostrarAlerta("Error",
                    "No se pudo crear el reporte.",
                    "Por favor, intente nuevamente más tarde.");
        }
    }


    @FXML
    public void añadirActividad() {

        LocalDate fechaInicioObtenida = this.fechaInicio.getValue();
        LocalDate fechaFinObtenida = this.fechaFin.getValue();

        List<String> errores = new ArrayList<>();

        ReporteContieneDAO reporteContieneDAO = new ReporteContieneDAO();

        try {

            errores = validarFechas.validarFechas(fechaInicioObtenida, fechaFinObtenida);

            if (!errores.isEmpty()) {

                gestorVentanas.mostrarAlerta("Errores de validación",
                        "Por favor, corrija los siguientes errores:",
                        String.join("\n", errores));
                return;
            }

            ReporteContieneDTO reporteContieneDTO = new ReporteContieneDTO();
            reporteContieneDTO.setIdReporte(idReporte);
            reporteContieneDTO.setFechaInicioReal(Timestamp.valueOf(fechaInicio.getValue().atStartOfDay()));
            reporteContieneDTO.setFechaFinReal(Timestamp.valueOf(fechaFin.getValue().atStartOfDay()));

            int idActividadSeleccionada = obtenerIdActividadSeleccionada();
            if (idActividadSeleccionada == -1) return;

            reporteContieneDTO.setIdActividad(idActividadSeleccionada);
            reporteContieneDAO.insertarReporteContiene(reporteContieneDTO);

            gestorVentanas.mostrarAlerta("Éxito",
                    "Actividad añadida",
                    "La actividad se ha añadido al reporte.");


            tablaActividades.getItems().clear();
            cargarActividades();

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (Exception e) {

            logger.error("Error inesperado: " + e);
            gestorVentanas.mostrarAlerta("Error",
                    "Ocurrió un error inesperado",
                    "Por favor, intente nuevamente más tarde.");
        }
    }

    public int obtenerIdActividadSeleccionada() {

        String actividadSeleccionada = comboActividades.getValue();

        if (actividadSeleccionada != null && !actividadSeleccionada.isEmpty()) {

            String idActividadStr = actividadSeleccionada.replaceAll("[^0-9]", "");
            return Integer.parseInt(idActividadStr);

        } else {

            gestorVentanas.mostrarAlerta("Error",
                    "No se seleccionó ninguna actividad.",
                    "Por favor, seleccione una actividad.");
            return -1;
        }
    }


    public void cargarActividades () {

        ActividadDAO actividadDAO = new ActividadDAO();
        ReporteContieneDAO reporteContieneDAO = new ReporteContieneDAO();

        try {

            List<ReporteContieneDTO> listaActividadesReporte = reporteContieneDAO.listarReporteContienePorIDReporte(idReporte);
            System.out.println(idReporte);

            for (ReporteContieneDTO reporteContiene : listaActividadesReporte) {

                ActividadDTO actividadDTO = actividadDAO.buscarActividadPorID(reporteContiene.getIdActividad());

                ContenedorActividadesReporte contenedorActividadesReporte = new ContenedorActividadesReporte(
                        actividadDTO,
                        reporteContiene
                );


                tablaActividades.getItems().add(contenedorActividadesReporte);
            }


        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al cargar las actividades: " + e);
            gestorVentanas.mostrarAlerta("Error",
                    "No se pudieron cargar las actividades.",
                    "Por favor, intente nuevamente más tarde.");
        }
    }

    @FXML
    public void confirmacionCancelarRegistro() {

        gestorVentanas.mostrarAlertaConfirmacion(
                "Confirmar eliminación",
                "¿Está seguro que desea cancelar el reporte mensual?",
                "Se cancelara el registro, esta accion no se puede deshacer.",
                () -> {
                    cancelarRegistro();
                },
                () -> {
                    gestorVentanas.mostrarAlerta("Cancelado",
                            "Eliminación cancelada",
                            "No se ha cancelado ningun reporte.");
                }
        );


    }

    public void cancelarRegistro() {

        ReporteDAO reporteDAO = new ReporteDAO();
        ReporteContieneDAO reporteContieneDAO = new ReporteContieneDAO();

        try {

            reporteContieneDAO.eliminarReporteContieneDefinitivamentePorIDReporte(idReporte);
            reporteDAO.eliminarReporte(idReporte);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {
            logger.error("Error inesperado al eliminar el reporte: ", e);
            gestorVentanas.mostrarAlerta("Error",
                    "No se pudo eliminar el reporte.",
                    "Por favor, intente nuevamente más tarde.");
        }

        Stage stage = (Stage) campoMetodologia.getScene().getWindow();
        stage.close();

    }

}
