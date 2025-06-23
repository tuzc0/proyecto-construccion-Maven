package GUI;

import GUI.utilidades.Utilidades;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logica.DAOs.CriterioEvaluacionDAO;
import logica.DTOs.CriterioEvaluacionDTO;
import logica.ManejadorExcepciones;
import logica.interfaces.IGestorAlertas;
import java.io.IOException;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;

public class ControladorHabilitarEvaluacionesGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorHabilitarEvaluacionesGUI.class);

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

    Utilidades gestorVentana = new Utilidades();
    IGestorAlertas utilidades = new Utilidades();
    ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, logger);

    @FXML
    public void initialize() {

        columnaDescripcion.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescripcion()));
        columnaNumeroCriterio.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getNumeroCriterio())));

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

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e){

            gestorVentana.mostrarAlerta(
                    "Error",
                    "Ocurrió al cargar los criterios de evaluación",
                    "Por favor, intentelo de nuevo más tarde o contacte al administrador."
            );
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

            manejadorExcepciones.manejarIOException(e);
        }
    }

    @FXML
    public void eliminarCriterio() {

        CriterioEvaluacionDTO criterioSeleccionado = tablaCriterios.getSelectionModel().getSelectedItem();

        if (criterioSeleccionado == null) {

            logger.error("No se ha seleccionado ningún criterio para eliminar.");
            gestorVentana.mostrarAlerta(
                    "Error",
                    "No se ha seleccionado ningún criterio.",
                    "porfavor seleccione un criterio para eliminar"
            );
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
                logger.warn("No se pudo eliminar el criterio.");
            }

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
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
                logger.error("No se pudo actualizar el criterio.");
            }

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
        }
    }

}
