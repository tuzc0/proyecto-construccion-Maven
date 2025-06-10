package GUI;

import GUI.utilidades.Utilidades;
import accesoadatos.AutenticadorDrive;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logica.ContenedorCriteriosAutoevaluacion;
import logica.ContenedorCriteriosEvaluacion;
import logica.DAOs.*;
import logica.DTOs.AutoEvaluacionContieneDTO;
import logica.DTOs.AutoevaluacionDTO;
import logica.DTOs.CriterioAutoevaluacionDTO;
import logica.DTOs.EvidenciaAutoevaluacionDTO;
import logica.SelectorArchivos;
import logica.SubidorArchivosDrive;
import org.apache.logging.log4j.Logger;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.sql.SQLException;


public class ControladorRegistrarAutoevaluacionGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorRegistrarAutoevaluacionGUI.class);

    Utilidades utilidades = new Utilidades();

    @FXML
    Label etiquetaFecha;

    @FXML
    Label etiquetaPromedio;

    @FXML
    TableView<ContenedorCriteriosAutoevaluacion> tablaAutoevaluacion;

    @FXML
    TableColumn<ContenedorCriteriosAutoevaluacion, String> columnaCriterio;

    @FXML
    TableColumn<ContenedorCriteriosAutoevaluacion, String> columnaNumeroCriterio;

    @FXML
    TableColumn<ContenedorCriteriosAutoevaluacion, Void> columna1;

    @FXML
    TableColumn<ContenedorCriteriosAutoevaluacion, Void> columna2;

    @FXML
    TableColumn<ContenedorCriteriosAutoevaluacion, Void> columna3;

    @FXML
    TableColumn<ContenedorCriteriosAutoevaluacion, Void> columna4;

    @FXML
    TableColumn<ContenedorCriteriosAutoevaluacion, Void> columna5;

    @FXML private ListView<String> listaArchivos;

    @FXML private Label lblErrorArchivos;

    @FXML private Button btnGuardar;

    private static final String FOLDER_ID = "1LGx2JpfFoNvqm7Dodf6i8Q2ijqDDONzR";

    private Map<String, String> urlsDrive = new HashMap<>();

    private static final double TAMANO_MAXIMO_MB = 10.0;

    private List<java.io.File> archivosLocales = new ArrayList<>();

    static int idAutoevaluacion = 0;

    String idEstudiante = ControladorInicioDeSesionGUI.matricula;


    @FXML
    public void initialize() {

        columnaNumeroCriterio.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getCriterioAutoevaluacion().getNumeroCriterio())));
        columnaCriterio.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCriterioAutoevaluacion().getDescripcion()));


        crearColumnaPuntuacion(columna1, 1);
        crearColumnaPuntuacion(columna2, 2);
        crearColumnaPuntuacion(columna3, 3);
        crearColumnaPuntuacion(columna4, 4);
        crearColumnaPuntuacion(columna5, 5);

        etiquetaFecha.setText(java.time.LocalDate.now().toString());
        guardarAutoevaluacionVacia();
        guardarCriteriosVacios();
        cargarCriteriosAutoevaluacion();
    }

    public void cargarCriteriosAutoevaluacion() {

        try {

            CriterioAutoevaluacionDAO criterioAutoevaluacionDAO = new CriterioAutoevaluacionDAO();
            AutoevaluacionContieneDAO autoEvaluacionContieneDAO = new AutoevaluacionContieneDAO();

            List<CriterioAutoevaluacionDTO> listaCriterios = criterioAutoevaluacionDAO.listarCriteriosAutoevaluacionActivos();
            List<AutoEvaluacionContieneDTO> listaAutoEvaluacionContiene = autoEvaluacionContieneDAO.listarAutoevaluacionesPorIdAutoevaluacion(idAutoevaluacion);
            ObservableList<ContenedorCriteriosAutoevaluacion> listaContenedorCriterios = FXCollections.observableArrayList();

            for (CriterioAutoevaluacionDTO criterio : listaCriterios) {

                for (AutoEvaluacionContieneDTO autoEvaluacionContiene : listaAutoEvaluacionContiene) {

                    if (criterio.getIDCriterio() == autoEvaluacionContiene.getIdCriterio()) {

                        ContenedorCriteriosAutoevaluacion contenedorCriterios = new ContenedorCriteriosAutoevaluacion(criterio, autoEvaluacionContiene);
                        listaContenedorCriterios.add(contenedorCriterios);
                    }
                }
            }

            tablaAutoevaluacion.setItems(listaContenedorCriterios);

        } catch (SQLException e) {

            logger.error("Error de SQL: " + e);

        } catch (IOException e) {

            logger.error("Error de IO: " + e);

        } catch (Exception e) {

            logger.error("Error inesperado: " + e);

        }
    }

    private void crearColumnaPuntuacion(TableColumn<ContenedorCriteriosAutoevaluacion, Void> columna, int valor) {

        columna.setCellFactory(col -> new TableCell<>() {

            private final RadioButton radio = new RadioButton();

            {
                radio.setOnAction(e -> {
                    AutoEvaluacionContieneDTO autoEvaluacionContiene =
                            getTableView().getItems().get(getIndex()).getAutoEvaluacionContiene();
                    autoEvaluacionContiene.setCalificacion(valor);
                    getTableView().refresh();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {

                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {

                    AutoEvaluacionContieneDTO autoEvaluacionContiene =
                            getTableView().getItems().get(getIndex()).getAutoEvaluacionContiene();
                    radio.setSelected(autoEvaluacionContiene.getCalificacion() == valor);
                    setGraphic(radio);
                }
            }
        });
    }

    @FXML
    private void calcularPromedio() {
        double suma = 0;
        int contador = 0;

        for (ContenedorCriteriosAutoevaluacion contenedor : tablaAutoevaluacion.getItems()) {
            AutoEvaluacionContieneDTO autoEvaluacionContiene = contenedor.getAutoEvaluacionContiene();
            if (autoEvaluacionContiene.getCalificacion() == 0) {
                utilidades.mostrarAlerta("Advertencia",
                        "Calificación incompleta",
                        "Debe calificar todos los criterios antes de calcular el promedio.");
                return;
            }
            suma += autoEvaluacionContiene.getCalificacion();
            contador++;
        }

        double promedio = contador > 0 ? suma / contador : 0;
        etiquetaPromedio.setText(String.format("%.2f", promedio));
    }


    private void guardarAutoevaluacionVacia() {

        AutoevaluacionDAO autoevaluacionDAO = new AutoevaluacionDAO();
        AutoevaluacionDTO autoevaluacion = new AutoevaluacionDTO();

        autoevaluacion.setFecha(java.sql.Timestamp.from(java.time.Instant.now()));
        autoevaluacion.setIDAutoevaluacion(0);
        autoevaluacion.setCalificacionFinal(0.0f);
        autoevaluacion.setLugar(" ");
        autoevaluacion.setEstadoActivo(1);
        autoevaluacion.setidEstudiante(idEstudiante);

        try {

            idAutoevaluacion = autoevaluacionDAO.crearNuevaAutoevaluacion(autoevaluacion);

        } catch (SQLException e) {

            logger.error("Error de SQL al crear la autoevaluación: " + e);

        } catch (IOException e) {

            logger.error("Error de IO al crear la autoevaluación: " + e);

        } catch (Exception e) {

            logger.error("Error inesperado al crear la autoevaluación: " + e);

        }
    }

    private void guardarCriteriosVacios () {

        CriterioAutoevaluacionDAO criterioAutoevaluacionDAO = new CriterioAutoevaluacionDAO();
        AutoevaluacionContieneDAO autovaluacionContieneDAO = new AutoevaluacionContieneDAO();

        try {

            List<CriterioAutoevaluacionDTO> listaCriterios = criterioAutoevaluacionDAO.listarCriteriosAutoevaluacionActivos();

            for (CriterioAutoevaluacionDTO criterio : listaCriterios) {

                AutoEvaluacionContieneDTO autoevaluacionContiene = new AutoEvaluacionContieneDTO();
                autoevaluacionContiene.setIdCriterio(criterio.getIDCriterio());
                autoevaluacionContiene.setIdAutoevaluacion(idAutoevaluacion);
                autoevaluacionContiene.setCalificacion(0);
                autovaluacionContieneDAO.insertarAutoevaluacionContiene(autoevaluacionContiene);
            }

        } catch (SQLException e) {

            logger.error("Error de SQL al guardar los criterios vacíos: " + e);

        } catch (IOException e) {

            logger.error("Error de IO al guardar los criterios vacíos: " + e);

        } catch (Exception e) {
            logger.error("Error inesperado al guardar los criterios vacíos: " + e);
        }
    }

    @FXML
    private void guardarAutoevaluacion() {

        if (archivosLocales.isEmpty()) {

            utilidades.mostrarAlerta("Error",
                    "Archivos no adjuntados",
                    "Debe adjuntar al menos un archivo de evidencia antes de guardar.");
            return;
        }


        if (etiquetaPromedio.getText() == null || etiquetaPromedio.getText().isEmpty()) {

            utilidades.mostrarAlerta("Error",
                    "Calificación no calculada",
                    "Debe calcular la calificación final antes de guardar.");
            return;
        }

        guardarRegistroAutoevaluacion();
        guardarEvidenciaAutoevaluacion();
        subirUrlBD();

    }

    private void guardarRegistroAutoevaluacion() {

        AutoevaluacionDTO autoevaluacion = new AutoevaluacionDTO();
        autoevaluacion.setIDAutoevaluacion(idAutoevaluacion);
        autoevaluacion.setFecha(java.sql.Timestamp.from(java.time.Instant.now()));
        autoevaluacion.setCalificacionFinal(Float.parseFloat(etiquetaPromedio.getText()));
        autoevaluacion.setLugar(" ");
        autoevaluacion.setEstadoActivo(1);
        autoevaluacion.setidEstudiante(idEstudiante);
        AutoevaluacionDAO autoevaluacionDAO = new AutoevaluacionDAO();

        try {

            autoevaluacionDAO.modificarAutoevaluacion(autoevaluacion);
            actualizarCriteriosAutoevaluacion();
            utilidades.mostrarAlerta("Registro Exitoso",
                    "Autoevaluación guardada",
                    "La autoevaluación se ha guardado correctamente.");

            Stage currentStage = (Stage) etiquetaFecha.getScene().getWindow();
            currentStage.close();

            utilidades.mostrarVentana("GUI/MenuEstudianteGUI.fxml");

        } catch (SQLException e) {

            logger.error("Error de SQL al guardar la autoevaluación: " + e);

        } catch (IOException e) {

            logger.error("Error de IO al guardar la autoevaluación: " + e);

        } catch (Exception e) {

            logger.error("Error inesperado al guardar la autoevaluación: " + e);

        }
    }

    private void actualizarCriteriosAutoevaluacion() {

        AutoevaluacionContieneDAO autoevaluacionContieneDAO = new AutoevaluacionContieneDAO();

        try {

            for (ContenedorCriteriosAutoevaluacion contenedor : tablaAutoevaluacion.getItems()) {

                AutoEvaluacionContieneDTO autoEvaluacionContiene = contenedor.getAutoEvaluacionContiene();

                if (autoEvaluacionContiene.getCalificacion() > 0) {
                    autoevaluacionContieneDAO.modificarCalificacion(autoEvaluacionContiene);
                }
            }

            logger.info("Criterios de autoevaluación actualizados correctamente.");

        } catch (SQLException e) {

            logger.error("Error de SQL al actualizar los criterios de autoevaluación: " + e);

        } catch (IOException e) {

            logger.error("Error de IO al actualizar los criterios de autoevaluación: " + e);

        } catch (Exception e) {

            logger.error("Error inesperado al actualizar los criterios de autoevaluación: " + e);
        }
    }

    @FXML
    private void cancelarRegistro() {

        AutoevaluacionDAO autoevaluacionDAO = new AutoevaluacionDAO();
        AutoevaluacionContieneDAO autoevaluacionContieneDAO = new AutoevaluacionContieneDAO();

        try {

            autoevaluacionContieneDAO.eliminarCriteriosDefinitivamentePorIdAutoevaluacion(idAutoevaluacion);
            autoevaluacionDAO.eliminarAutoevaluacionDefinitivamentePorID(idAutoevaluacion);
            Stage currentStage = (Stage) etiquetaFecha.getScene().getWindow();
            currentStage.close();

            utilidades.mostrarVentana("GUI/MenuEstudianteGUI.fxml");

        } catch (SQLException e) {
            logger.error("Error de SQL al cancelar el registro: " + e);
        } catch (IOException e) {
            logger.error("Error de IO al cancelar el registro: " + e);
        } catch (Exception e) {
            logger.error("Error inesperado al cancelar el registro: " + e);
        }

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
                lblErrorArchivos.setText("");

            } else {
                lblErrorArchivos.setText("Archivos no válidos. Verifique tamaño y formatos.");
            }
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


    private void guardarEvidenciaAutoevaluacion() {

        if (archivosLocales.isEmpty()) {

            lblErrorArchivos.setText("Debe adjuntar al menos un archivo de evidencia");
            return;
        }

        try {

            SubidorArchivosDrive subidor = new SubidorArchivosDrive("1LGx2JpfFoNvqm7Dodf6i8Q2ijqDDONzR");
            urlsDrive = subidor.subirArchivos(archivosLocales);

            subirUrlBD();

            utilidades.mostrarAlerta("Subida Exitosa", "Archivos subidos correctamente",
                    "Se subieron " + urlsDrive.size() + " archivos a Google Drive");

        } catch (Exception e) {
            lblErrorArchivos.setText("Error al subir archivos: " + e);
            logger.error("Error al subir archivos: ", e);
        }
    }


    private void subirUrlBD() {

        try{
            EvidenciaAutoevaluacionDAO evidenciaDAO = new EvidenciaAutoevaluacionDAO();

            for (java.io.File archivo : archivosLocales) {
                String url = urlsDrive.get(archivo.getName());
                if (url != null) {
                    EvidenciaAutoevaluacionDTO evidencia = new EvidenciaAutoevaluacionDTO();
                    evidencia.setIdEvidencia(0);
                    evidencia.setURL(url);
                    evidencia.setIdAutoevaluacion(idAutoevaluacion);

                    if (!evidenciaDAO.insertarEvidenciaAutoevaluacion(evidencia)) {

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


}



