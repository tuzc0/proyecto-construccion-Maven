package GUI;

import GUI.utilidades.Utilidades;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logica.*;
import logica.DAOs.*;
import logica.DTOs.*;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ControladorRegistrarReporteMensualGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorRegistrarCriterioEvaluacionGUI.class);

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

    String matricula = ControladorInicioDeSesionGUI.matricula;

    Utilidades utilidades = new Utilidades();

    int idCronograma = 0;

    int idReporte = 0;

    private Map<String, String> urlsDrive = new HashMap<>();

    private static final double TAMANO_MAXIMO_MB = 10.0;

    private List<java.io.File> archivosLocales = new ArrayList<>();

    VerificacionEntradas verificacionEntradas = new VerificacionEntradas();

    @FXML
    public void initialize() {

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

        if (campoMetodologia.getText().isEmpty() || campoObservaciones.getText().isEmpty() ||
                campoHoras.getText().isEmpty()) {

            utilidades.mostrarAlerta("Error",
                    "Campos incompletos",
                    "Debe completar todos los campos antes de guardar el reporte.");
            return;
        }

        if (tablaActividades.getItems().isEmpty()) {

            utilidades.mostrarAlerta("Error",
                    "No hay actividades",
                    "Debe añadir al menos una actividad al reporte.");
            return;
        }

        if (listaArchivos.getItems().isEmpty()) {

            utilidades.mostrarAlerta("Error",
                    "No hay archivos adjuntos",
                    "Debe adjuntar al menos un archivo de evidencia.");
            return;
        }

        String metodologia = campoMetodologia.getText();
        String observaciones = campoObservaciones.getText();
        String horasTexto = campoHoras.getText();

        if (metodologia.isEmpty() || observaciones.isEmpty() || horasTexto.isEmpty()) {

            utilidades.mostrarAlerta("Error",
                    "Campos incompletos",
                    "Debe completar todos los campos antes de guardar el reporte.");
            return;
        }

        if (!verificacionEntradas.esEnteroPositivo(horasTexto)) {

            utilidades.mostrarAlerta("Error",
                    "Número de horas inválido",
                    "El número de horas debe ser un número entero positivo.");
            return;
        }

        if (!verificacionEntradas.esTextoSeguro(observaciones)) {

            utilidades.mostrarAlerta("Error",
                    "Número de horas inválido",
                    "El número de horas debe ser un número positivo.");
            return;
        }

        if (!verificacionEntradas.esTextoSeguro(metodologia)) {

            utilidades.mostrarAlerta("Error",
                    "Metodología inválida",
                    "La metodología debe ser un texto seguro.");
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

            utilidades.mostrarAlerta("Éxito",
                    "Reporte guardado",
                    "El reporte se ha guardado correctamente.");

            guardarEvidenciaReporte();

        } catch (SQLException e) {
            logger.error("Error al guardar el reporte: ", e);
            utilidades.mostrarAlerta("Error",
                    "No se pudo guardar el reporte",
                    "Por favor, intente nuevamente más tarde.");

        } catch (IOException e) {

            logger.error("Error de IO al guardar el reporte: ", e);
            utilidades.mostrarAlerta("Error",
                    "No se pudo guardar el reporte",
                    "Por favor, intente nuevamente más tarde.");

        } catch (Exception e) {

            logger.error("Error inesperado al guardar el reporte: ", e);
            utilidades.mostrarAlerta("Error",
                    "Ocurrió un error inesperado",
                    "Por favor, intente nuevamente más tarde.");
        }
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
            urlsDrive = subidor.subirArchivos(archivosLocales);

            subirUrlBD();

            utilidades.mostrarAlerta("Subida Exitosa", "Archivos subidos correctamente",
                    "Se subieron " + urlsDrive.size() + " archivos a Google Drive");

        } catch (Exception e) {
            etiquetaErrorArchivos.setText("Error al subir archivos: " + e);
            logger.error("Error al subir archivos: ", e);
        }
    }


    private void subirUrlBD() {

        try{

            EvidenciaReporteDAO evidenciaDAO = new EvidenciaReporteDAO();

            for (java.io.File archivo : archivosLocales) {

                String url = urlsDrive.get(archivo.getName());

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

            logger.error("Error de SQL al insertar evidencia: " + e);
            utilidades.mostrarAlerta("Error", "Error al insertar evidencia",
                    "No se pudo insertar la evidencia en la base de datos. Verifique los registros.");

        } catch (IOException e) {

            logger.error("Error de IO al insertar evidencia: " + e);
            utilidades.mostrarAlerta("Error", "Error de IO al insertar evidencia",
                    "No se pudo insertar la evidencia en la base de datos. Verifique los registros.");

        } catch (Exception e) {

            logger.error("Error inesperado al insertar evidencia: " + e);
            utilidades.mostrarAlerta("Error", "Error inesperado al insertar evidencia",
                    "No se pudo insertar la evidencia en la base de datos. Verifique los registros.");
        }
    }

    public void cargarComboActividades () {

        CronogramaActividadesDAO cronogramaActividadesDAO = new CronogramaActividadesDAO();
        CronogramaContieneDAO cronogramaContieneDAO = new CronogramaContieneDAO();
        ActividadDAO actividadDAO = new ActividadDAO();

        try{
            CronogramaActividadesDTO cronogramaActividadesDTO = cronogramaActividadesDAO.buscarCronogramaPorMatricula(matricula);

            idCronograma = cronogramaActividadesDTO.getIDCronograma();
            List<CronogramaContieneDTO> cronogramaContieneList = cronogramaContieneDAO.listarCronogramaContienePorID(idCronograma);

            for (CronogramaContieneDTO cronogramaContiene : cronogramaContieneList) {
                ActividadDTO actividadDTO = actividadDAO.buscarActividadPorID(cronogramaContiene.getIdActividad());
                comboActividades.getItems().add(actividadDTO.getNombre() + actividadDTO.getIDActividad());
            }


        } catch (SQLException e) {

            logger.error("Error al buscar el cronograma por matrícula: " + e);
        } catch (IOException e) {

            logger.error("Error de IO al buscar el cronograma por matrícula: " + e);
        } catch (Exception e) {

            logger.error("Error inesperado al buscar el cronograma por matrícula: " + e);
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

            logger.error("Error al crear el reporte: " + e);
            utilidades.mostrarAlerta("Error",
                    "No se pudo crear el reporte.",
                    "Por favor, intente nuevamente más tarde.");

        } catch (IOException e) {

            logger.error("Error de IO al crear el reporte: " + e);
            utilidades.mostrarAlerta("Error",
                    "No se pudo crear el reporte.",
                    "Por favor, intente nuevamente más tarde.");

        } catch (Exception e) {

            logger.error("Error inesperado al crear el reporte: " + e);
            utilidades.mostrarAlerta("Error",
                    "No se pudo crear el reporte.",
                    "Por favor, intente nuevamente más tarde.");
        }
    }

    @FXML
    public void añadirActividad() {

        if (fechaInicio.getValue() == null || fechaFin.getValue() == null) {

            utilidades.mostrarAlerta("Error",
                    "Fechas incompletas",
                    "Debe seleccionar ambas fechas");
            return;
        }

        if (fechaInicio.getValue().isAfter(fechaFin.getValue())) {

            utilidades.mostrarAlerta("Error",
                    "Fecha de inicio posterior a fecha de fin",
                    "La fecha de inicio no puede ser posterior a la fecha de fin.");
            return;
        }

        ReporteContieneDAO reporteContieneDAO = new ReporteContieneDAO();

        try {

            ReporteContieneDTO reporteContieneDTO = new ReporteContieneDTO();
            reporteContieneDTO.setIdReporte(idReporte);
            reporteContieneDTO.setFechaInicioReal(Timestamp.valueOf(fechaInicio.getValue().atStartOfDay()));
            reporteContieneDTO.setFechaFinReal(Timestamp.valueOf(fechaFin.getValue().atStartOfDay()));

            int idActividadSeleccionada = obtenerIdActividadSeleccionada();
            if (idActividadSeleccionada == -1) return;

            reporteContieneDTO.setIdActividad(idActividadSeleccionada);
            reporteContieneDAO.insertarReporteContiene(reporteContieneDTO);

            utilidades.mostrarAlerta("Éxito",
                    "Actividad añadida",
                    "La actividad se ha añadido al reporte.");


            tablaActividades.getItems().clear();
            cargarActividades();

        } catch (SQLException e) {

            logger.error("Error al añadir actividad: " + e);
            utilidades.mostrarAlerta("Error",
                    "No se pudo añadir la actividad",
                    "Por favor, intente nuevamente más tarde.");

        } catch (Exception e) {
            logger.error("Error inesperado: " + e);
            utilidades.mostrarAlerta("Error",
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

            utilidades.mostrarAlerta("Error",
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

            for (ReporteContieneDTO reporteContiene : listaActividadesReporte) {

                ActividadDTO actividadDTO = actividadDAO.buscarActividadPorID(reporteContiene.getIdActividad());

                ContenedorActividadesReporte contenedorActividadesReporte = new ContenedorActividadesReporte(
                        actividadDTO,
                        reporteContiene
                );


                tablaActividades.getItems().add(contenedorActividadesReporte);
            }


        } catch (SQLException e) {

            logger.error("Error al cargar las actividades: " + e);
            utilidades.mostrarAlerta("Error",
                    "No se pudieron cargar las actividades.",
                    "Por favor, intente nuevamente más tarde.");

        } catch (IOException e) {

            logger.error("Error de IO al cargar las actividades: " + e);
            utilidades.mostrarAlerta("Error",
                    "No se pudieron cargar las actividades.",
                    "Por favor, intente nuevamente más tarde.");

        } catch (Exception e) {

            logger.error("Error inesperado al cargar las actividades: " + e);
            utilidades.mostrarAlerta("Error",
                    "No se pudieron cargar las actividades.",
                    "Por favor, intente nuevamente más tarde.");
        }
    }

    @FXML
    public void cancelarRegistro () {

        Stage stage = (Stage) campoMetodologia.getScene().getWindow();
        stage.close();

    }
}
