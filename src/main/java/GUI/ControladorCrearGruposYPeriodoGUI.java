package GUI;

import GUI.utilidades.Utilidades;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import logica.ContenedorGrupo;
import logica.DAOs.AcademicoDAO;
import logica.DAOs.GrupoDAO;
import logica.DAOs.PeriodoDAO;
import logica.DAOs.UsuarioDAO;
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
    private void initialize() {

        cargarPeriodos();

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
                    setDisable(empty || (date.getMonthValue() != 2 && date.getMonthValue() != 7));
                } else if (periodo.equals("agosto-enero")) {
                    setDisable(empty || (date.getMonthValue() != 8 && date.getMonthValue() != 1));
                }
            }
        });

        fechaFinal.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(java.time.LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (periodo.equals("febrero-julio")) {
                    setDisable(empty || (date.getMonthValue() != 2 && date.getMonthValue() != 7));
                } else if (periodo.equals("agosto-enero")) {
                    setDisable(empty || (date.getMonthValue() != 8 && date.getMonthValue() != 1));
                }
            }
        });
    }


    @FXML
    private void crearGrupo() {

        utilidades.mostrarVentana("/CrearGrupoGUI.fxml");

    }

    @FXML
    private void cargarPeriodos() {

        ObservableList<String> periodos = FXCollections.observableArrayList("febrero-julio", "agosto-enero");
        comboPeriodo.setItems(periodos);
    }

    @FXML
    private void crearNuevoPeriodo() {
        try {
            // Validate that both dates are selected
            if (fechaInicio.getValue() == null || fechaFinal.getValue() == null) {
                utilidades.mostrarAlerta("Error",
                        "Fechas inválidas",
                        "Debe seleccionar ambas fechas.");
                return;
            }

            // Validate that the start date is before the end date
            if (!fechaInicio.getValue().isBefore(fechaFinal.getValue())) {
                utilidades.mostrarAlerta("Error",
                        "Fechas inválidas",
                        "La fecha de inicio debe ser menor a la fecha final.");
                return;
            }

            // Check if there is already an active period
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

            List<GrupoDTO> gruposActivos = grupoDAO.mostrarGruposActivos();
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
