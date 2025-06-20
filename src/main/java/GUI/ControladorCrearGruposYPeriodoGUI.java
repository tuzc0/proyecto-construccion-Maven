package GUI;

import GUI.utilidades.Utilidades;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logica.ContenedorGrupo;
import logica.DAOs.*;
import logica.DTOs.AcademicoDTO;
import logica.DTOs.GrupoDTO;
import logica.DTOs.PeriodoDTO;
import logica.DTOs.UsuarioDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorCrearGruposYPeriodoGUI {

    Utilidades utilidades = new Utilidades();

    @FXML
    ComboBox<String> comboPeriodo;

    @FXML
    DatePicker fechaInicio;

    @FXML
    DatePicker fechaFinal;

    @FXML
    Label etiquetaExperienciaEducativa;

    @FXML
    TableView <GrupoDTO> tablaGrupos;

    @FXML
    TableColumn<GrupoDTO, String> columnaGrupo;

    @FXML
    TableColumn<GrupoDTO, String> columnaAcademico;

    @FXML
    TableColumn<GrupoDTO, String> columnaNRC;

    @FXML
    Label etiquetaPeriodo;

    @FXML
    Label etiquetaPeriodoActivo;

    @FXML
    Button botonEliminarPeriodo;

    @FXML
    Button botonCrearNuevoPeriodo;

    @FXML
    Label etiquetaInicio;

    @FXML
    Label etiquetaFinal;



    @FXML
    private void initialize() {

        cargarPeriodos();
        verificarPeriodoActivo();

        comboPeriodo.getSelectionModel().selectedItemProperty().addListener((periodoObservado,
                                                                             periodoAnterior,
                                                                             nuevoPeriodo) -> {
            if (nuevoPeriodo != null) {
                restringirFechas(nuevoPeriodo);
            }
        });

        etiquetaExperienciaEducativa.setText("Practicas Profesionales");

        columnaGrupo.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        columnaNRC.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getNRC())));
        columnaAcademico.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getNumeroPersonal())));
        cargarGruposActivos();



    }

    private void restringirFechas(String periodo) {

        fechaInicio.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(java.time.LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (periodo.equals("febrero-julio")) {
                    setDisable(empty || date.getMonthValue() != 2);
                } else if (periodo.equals("agosto-enero")) {
                    setDisable(empty || date.getMonthValue() != 8);
                }
            }
        });

        fechaFinal.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(java.time.LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (periodo.equals("febrero-julio")) {
                    setDisable(empty || date.getMonthValue() != 7);
                } else if (periodo.equals("agosto-enero")) {
                    setDisable(empty || date.getMonthValue() != 1);
                }
            }
        });

        fechaInicio.setEditable(false);
        fechaFinal.setEditable(false);
    }



    public void verificarPeriodoActivo() {

        PeriodoDTO periodo = new PeriodoDTO(-1, "", -1, null, null);
        PeriodoDAO periodoDAO = new PeriodoDAO();
        try {

            periodo = periodoDAO.mostrarPeriodoActual();

            if (periodo.getIDPeriodo() != -1) {

                etiquetaPeriodoActivo.setVisible(true);
                etiquetaPeriodo.setText(periodo.getDescripcion());
                comboPeriodo.setVisible(false);
                fechaInicio.setVisible(false);
                fechaFinal.setVisible(false);
                etiquetaInicio.setText("Fecha de inicio: " + periodo.getFechaInicio().toLocalDate());
                etiquetaFinal.setText("Fecha final: " + periodo.getFechaFinal().toLocalDate());
                etiquetaPeriodo.setVisible(true);
                botonEliminarPeriodo.setVisible(true);
                botonCrearNuevoPeriodo.setVisible(false);

            } else {

                activarNuevoPeriodo();

            }

        } catch (SQLException e) {
            utilidades.mostrarAlerta("Error de base de datos",
                    "Error al verificar periodo activo",
                    "Ocurrió un problema al acceder a la base de datos: " + e);
        } catch (IOException e) {
            utilidades.mostrarAlerta("Error de entrada/salida",
                    "Error al verificar periodo activo",
                    "Ocurrió un problema al procesar los datos: " + e);
        }
    }

    private void activarNuevoPeriodo () {

        etiquetaPeriodoActivo.setVisible(false);
        comboPeriodo.setVisible(true);
        fechaInicio.setVisible(true);
        fechaFinal.setVisible(true);
        etiquetaInicio.setText("inicio:");
        etiquetaFinal.setText("final:");
        etiquetaPeriodo.setVisible(false);
        botonEliminarPeriodo.setVisible(false);
        botonCrearNuevoPeriodo.setVisible(true);


    }

    @FXML
    public void eliminarPeriodo() {

        utilidades.mostrarAlertaConfirmacion(
                "Confirmar eliminación",
                "¿Está seguro que desea eliminar el periodo activo?",
                "Se eliminará el periodo, grupos y estduiantes activos. Esta acción no se puede deshacer.",
                () -> {
                    eliminarPeriodoConfirmado();
                },
                () -> {
                    utilidades.mostrarAlerta("Cancelado",
                            "Eliminación cancelada",
                            "No se ha eliminado ningún periodo.");
                }
        );
    }

    public void eliminarPeriodoConfirmado() {

        try {

            PeriodoDAO periodoDAO = new PeriodoDAO();
            EstudianteDAO estudianteDAO = new EstudianteDAO();
            int idPeriodo = periodoDAO.mostrarPeriodoActual().getIDPeriodo();
            GrupoDAO grupoDAO = new GrupoDAO();

            List<GrupoDTO> gruposDelPeriodo = grupoDAO.mostrarGruposActivosEnPeriodoActivo();
            for (GrupoDTO grupo : gruposDelPeriodo) {
                estudianteDAO.eliminarEstudiantesPorGrupo(grupo.getNRC());
            }

            grupoDAO.eliminarGruposPorPeriodo(idPeriodo);
            periodoDAO.eliminarPeriodoPorID(idPeriodo);

            cargarGruposActivos();
            verificarPeriodoActivo();

            utilidades.mostrarAlerta("Éxito",
                    "Periodo eliminado",
                    "El periodo activo se ha eliminado correctamente.");

        } catch (SQLException e) {
            utilidades.mostrarAlerta("Error de base de datos",
                    "Error al eliminar periodo",
                    "Ocurrió un problema al acceder a la base de datos: " + e);
        } catch (IOException e) {
            utilidades.mostrarAlerta("Error de entrada/salida",
                    "Error al eliminar periodo",
                    "Ocurrió un problema al procesar los datos: " + e);
        }
    }


    @FXML
    private void crearGrupo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CrearGrupoGUI.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Crear nuevo grupo");
            stage.setScene(new Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();
            cargarGruposActivos();

        } catch (IOException e) {
            utilidades.mostrarAlerta("Error",
                    "No se pudo cargar la ventana",
                    "Ocurrió un error al abrir la ventana para crear grupo.");
        }
    }


    @FXML
    private void cargarPeriodos() {

        ObservableList<String> periodos = FXCollections.observableArrayList("febrero-julio", "agosto-enero");
        comboPeriodo.setItems(periodos);

    }

    @FXML
    private void crearNuevoPeriodo() {

        try {

            if (fechaInicio.getValue() == null || fechaFinal.getValue() == null) {
                utilidades.mostrarAlerta("Error",
                        "Fechas inválidas",
                        "Debe seleccionar ambas fechas.");
                return;
            }

            if (!fechaInicio.getValue().isBefore(fechaFinal.getValue())) {
                utilidades.mostrarAlerta("Error",
                        "Fechas inválidas",
                        "La fecha de inicio debe ser menor a la fecha final.");
                return;
            }

            PeriodoDAO periodoDAO = new PeriodoDAO();
            if (periodoDAO.existePeriodoActivo()) {
                utilidades.mostrarAlerta("Error",
                        "Periodo activo",
                        "Ya existe un periodo activo. Desactívelo antes de crear uno nuevo.");
                return;
            }

            PeriodoDTO nuevoPeriodo = new PeriodoDTO();
            nuevoPeriodo.setIDPeriodo(0);
            nuevoPeriodo.setDescripcion(comboPeriodo.getValue());
            nuevoPeriodo.setFechaInicio(java.sql.Date.valueOf(fechaInicio.getValue()));
            nuevoPeriodo.setFechaFinal(java.sql.Date.valueOf(fechaFinal.getValue()));
            nuevoPeriodo.setEstadoActivo(1);

            periodoDAO.crearNuevoPeriodo(nuevoPeriodo);

            utilidades.mostrarAlerta("Éxito",
                    "Periodo creado",
                    "El nuevo periodo se ha creado correctamente.");

            verificarPeriodoActivo();

        } catch (Exception e) {
            utilidades.mostrarAlerta("Error",
                    "Error al crear el periodo",
                    "Ocurrió un error al guardar el periodo: ");
        }
    }

    @FXML
    private void eliminarGrupo() {
        GrupoDTO grupoSeleccionado = tablaGrupos.getSelectionModel().getSelectedItem();

        if (grupoSeleccionado == null) {
            utilidades.mostrarAlerta("Error",
                    "Grupo no seleccionado",
                    "Por favor, seleccione un grupo para eliminar.");
            return;
        }

        utilidades.mostrarAlertaConfirmacion(
                "Confirmar eliminación",
                "¿Está seguro que desea eliminar el grupo seleccionado?",
                "Se eliminarán el grupo seleccionado . Esta acción no se puede deshacer.",
                () -> {
                    eliminarGrupoConfirmado(grupoSeleccionado);
                },
                () -> {
                    utilidades.mostrarAlerta("Cancelado",
                            "Eliminación cancelada",
                            "No se ha eliminado ningún grupo.");
                }

        );

    }

    private void eliminarGrupoConfirmado(GrupoDTO grupoSeleccionado) {
        try {

            GrupoDAO grupoDAO = new GrupoDAO();
            EstudianteDAO estudianteDAO = new EstudianteDAO();
            estudianteDAO.eliminarEstudiantesPorGrupo(grupoSeleccionado.getNRC());
            grupoDAO.eliminarGrupoPorNRC(grupoSeleccionado.getNRC());
            cargarGruposActivos();
            utilidades.mostrarAlerta("Éxito",
                    "Grupo eliminado",
                    "El grupo se ha eliminado correctamente.");

        } catch (Exception e) {
            utilidades.mostrarAlerta("Error",
                    "Error al eliminar el grupo",
                    "Ocurrió un error al eliminar el grupo: ");
        }
    }

    @FXML
    private void cargarGruposActivos() {

        try {

            GrupoDAO grupoDAO = new GrupoDAO();

            List<GrupoDTO> gruposActivos = grupoDAO.mostrarGruposActivosEnPeriodoActivo();
            tablaGrupos.setItems(FXCollections.observableArrayList(gruposActivos));

        } catch (SQLException e) {

            utilidades.mostrarAlerta("Error de base de datos",
                    "Error al cargar grupos",
                    "Ocurrió un problema al acceder a la base de datos: " + e);

        } catch (IOException e) {
            utilidades.mostrarAlerta("Error de entrada/salida",
                    "Error al cargar grupos",
                    "Ocurrió un problema al procesar los datos: " + e);

        }
    }
}
