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
import logica.DAOs.CriterioAutoevaluacionDAO;
import logica.DTOs.CriterioAutoevaluacionDTO;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

public class ControladorHabilitarAutoevaluacionGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorHabilitarAutoevaluacionGUI.class);


    @FXML
    TableView<CriterioAutoevaluacionDTO> tablaCriterios;

    @FXML
    TableColumn<CriterioAutoevaluacionDTO, String> columnaNumeroCriterio;

    @FXML
    TableColumn<CriterioAutoevaluacionDTO, String> columnaDescripcion;

    @FXML
    Button botonEditarCriterio;

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

    }

    @FXML
    public void cargarCriterios() {

        try {

            CriterioAutoevaluacionDAO criterioAutoevaluacionDAO = new CriterioAutoevaluacionDAO();
            ObservableList<CriterioAutoevaluacionDTO> listaCriterios =
                    FXCollections.observableArrayList(criterioAutoevaluacionDAO.listarCriteriosAutoevaluacionActivos());
            tablaCriterios.setItems(listaCriterios);

        } catch (SQLException e){

            logger.error("Error al cargar los datos de la tabla de criterios de autoevaluación: " + e);

        } catch (IOException e) {

            logger.error("Error al cargar los datos de la tabla de criterios de autoevaluación: " + e);

        } catch (Exception e) {

            logger.error("Error inesperado al cargar los datos de la tabla de criterios de autoevaluación: " + e);

        }
    }

    @FXML
    public void abrirVentanaRegistrarCriterioAutoevaluacionGUI() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegistrarCriterioAutoevaluacionGUI.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Registrar Nuevo Criterio");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            cargarCriterios();

        } catch (IOException e) {

            logger.error("Error al abrir la ventana de registrar criterio de autoevaluación: " + e);

        } catch (Exception e) {

            logger.error("Error inesperado al abrir la ventana de registrar criterio de autoevaluación: " + e);
        }

    }

    @FXML
    public void eliminarCriterio() {
        CriterioAutoevaluacionDTO criterioSeleccionado = tablaCriterios.getSelectionModel().getSelectedItem();

        if (criterioSeleccionado == null) {

            logger.warn("No se ha seleccionado ningún criterio para eliminar.");
            utilidades.mostrarAlerta("Error",
                    "No se ha seleccionado ningún criterio.",
                    "Por favor seleccione un criterio para eliminar.");
            return;
        }

        try {

            CriterioAutoevaluacionDAO criterioAutoevaluacionDAO = new CriterioAutoevaluacionDAO();
            boolean eliminado =
                    criterioAutoevaluacionDAO.eliminarCriterioAutoevaluacionPorID(criterioSeleccionado.getIDCriterio());

            if (eliminado) {

                logger.info("Criterio de autoevaluación eliminado correctamente.");
                criterioAutoevaluacionDAO.enumerarCriterios();
                cargarCriterios();

            } else {

                logger.warn("No se pudo eliminar el criterio de autoevaluación.");
            }

        } catch (SQLException e) {

            logger.error("Error al eliminar el criterio de autoevaluación: " + e);

        } catch (IOException e) {

            logger.error("Error de IO al eliminar el criterio de autoevaluación: " + e);
        }
    }

    @FXML
    public void editarCriterios() {

        columnaDescripcion.setCellFactory(TextFieldTableCell.forTableColumn());
        columnaDescripcion.setOnEditCommit(event -> {
            CriterioAutoevaluacionDTO criterio = event.getRowValue();
            criterio.setDescripcion(event.getNewValue());
            actualizarCriterioEnBaseDeDatos(criterio);

            botonAñadirCriterio.setDisable(false);
            botonEditarCriterio.setDisable(false);
            botonEliminarCriterio.setDisable(false);

            tablaCriterios.setEditable(false);
        });

        tablaCriterios.setEditable(true);

        botonAñadirCriterio.setDisable(true);
        botonEditarCriterio.setDisable(true);
        botonEliminarCriterio.setDisable(true);
    }

    private void actualizarCriterioEnBaseDeDatos(CriterioAutoevaluacionDTO criterio) {

        try {

            CriterioAutoevaluacionDAO criterioAutoevaluacionDAO = new CriterioAutoevaluacionDAO();
            boolean actualizado = criterioAutoevaluacionDAO.modificarCriterioAutoevaluacion(criterio);

            if (actualizado) {
                logger.info("Criterio de autoevaluación actualizado correctamente.");
            } else {
                logger.warn("No se pudo actualizar el criterio de autoevaluación.");
            }

        } catch (SQLException | IOException e) {

            logger.error("Error al actualizar el criterio de autoevaluación: " + e);
        }
    }

}
