package GUI;

import GUI.utilidades.Utilidades;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logica.DAOs.CriterioEvaluacionDAO;
import logica.DTOs.CriterioEvaluacionDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ControladorHabilitarEvaluacionesGUI {

    Logger logger = Logger.getLogger(ControladorHabilitarEvaluacionesGUI.class.getName());

    @FXML
    TableView<CriterioEvaluacionDTO> tablaCriterios;

    @FXML
    TableColumn<CriterioEvaluacionDTO, String> columnaNumeroCriterio;

    @FXML
    TableColumn<CriterioEvaluacionDTO, String> columnaDescripcion;

    @FXML
    Button botonEditarCriterios;


    @FXML
    Button botonAñadirCriterio;

    @FXML
    Button botonEliminarCriterio;

    Utilidades utilidades = new Utilidades();


    @FXML
    public void initialize() {

        columnaDescripcion.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescripcion()));
        columnaNumeroCriterio.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getNumeroCriterio())));

        cargarCriterios();
        tablaCriterios.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
    }

    public void cargarCriterios() {

        try{

            CriterioEvaluacionDAO criterioEvaluacionDAO = new CriterioEvaluacionDAO();
            ObservableList<CriterioEvaluacionDTO> listaCriterios =
                    FXCollections.observableArrayList(criterioEvaluacionDAO.listarCriteriosActivos());
            tablaCriterios.setItems(listaCriterios);

        } catch (SQLException e) {

            logger.severe("Error al cargar los criterios: " + e);

        } catch (IOException e) {

            logger.severe("Error de IO: " + e);

        } catch (Exception e){

            logger.severe("Error inesperado: " + e);

        }
    }

    @FXML
    public void abrirVentanaRegistrarNuevoCriterio() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegistrarCriterioEvaluacionGUI.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Registrar Nuevo Criterio");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            cargarCriterios();

        } catch (IOException e) {

            logger.severe("Error al abrir la ventana RegistrarCriterioEvaluacionGUI: " + e);
        }
    }

    @FXML
    public void eliminarCriterio() {

        CriterioEvaluacionDTO criterioSeleccionado = tablaCriterios.getSelectionModel().getSelectedItem();

        if (criterioSeleccionado == null) {

            logger.warning("No se ha seleccionado ningún criterio para eliminar.");
            utilidades.mostrarAlerta("Error",
                    "No se ha seleccionado ningún criterio.",
                    "porfavor seleccione un criterio para eliminar");
            return;
        }

        try {

            CriterioEvaluacionDAO criterioEvaluacionDAO = new CriterioEvaluacionDAO();
            boolean eliminado =
                    criterioEvaluacionDAO.eliminarCriterioEvaluacionPorID(criterioSeleccionado.getIDCriterio());

            if (eliminado) {

                logger.info("Criterio eliminado correctamente.");
                criterioEvaluacionDAO.enumerarCriterios();
                cargarCriterios();

            } else {
                logger.warning("No se pudo eliminar el criterio.");
            }

        } catch (SQLException e) {

            logger.severe("Error al eliminar el criterio: " + e);

        } catch (IOException e) {

            logger.severe("Error de IO al eliminar el criterio: " + e);
        }
    }

    @FXML
    public void editarCriterios() {

        columnaDescripcion.setCellFactory(TextFieldTableCell.forTableColumn());
        columnaDescripcion.setOnEditCommit(event -> {
            CriterioEvaluacionDTO criterio = event.getRowValue();
            criterio.setDescripcion(event.getNewValue());
            actualizarCriterioEnBaseDeDatos(criterio);


            botonAñadirCriterio.setDisable(false);
            botonEliminarCriterio.setDisable(false);


            tablaCriterios.setEditable(false);
        });

        tablaCriterios.setEditable(true);

        botonAñadirCriterio.setDisable(true);
        botonEliminarCriterio.setDisable(true);
    }

    private void actualizarCriterioEnBaseDeDatos(CriterioEvaluacionDTO criterio) {

        try {

            CriterioEvaluacionDAO criterioEvaluacionDAO = new CriterioEvaluacionDAO();
            boolean actualizado = criterioEvaluacionDAO.modificarCriterioEvaluacion(criterio);

            if (actualizado) {
                logger.info("Criterio actualizado correctamente.");
            } else {
                logger.warning("No se pudo actualizar el criterio.");
            }

        } catch (SQLException | IOException e) {
            logger.severe("Error al actualizar el criterio: " + e);
        }
    }

}
