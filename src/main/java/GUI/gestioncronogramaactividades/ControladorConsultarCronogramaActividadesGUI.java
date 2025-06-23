package GUI.gestioncronogramaactividades;

import GUI.gestionestudiante.AuxiliarGestionEstudiante;
import GUI.utilidades.Utilidades;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import logica.DAOs.EstudianteDAO;
import logica.DTOs.EstudianteDTO;
import logica.ManejadorExcepciones;
import logica.interfaces.IGestorAlertas;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.IOException;
import java.sql.SQLException;

public class ControladorConsultarCronogramaActividadesGUI {

    Logger logger = LogManager.getLogger(ControladorConsultarCronogramaActividadesGUI.class);

    @FXML private TableView<EstudianteDTO> tablaCronograma;
    @FXML private TableColumn<EstudianteDTO, String> columnaMatricula;
    @FXML private TableColumn<EstudianteDTO, String> columnaNombre;
    @FXML private TableColumn<EstudianteDTO, Void> columnaDetalles;

    private Utilidades gestorVentanas = new Utilidades();
    private IGestorAlertas gestorAlertas = new Utilidades();
    private ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(gestorAlertas, logger);

    private String matriculaSeleccionada;
    private int NRC;

    @FXML
    private void initialize() {

        columnaMatricula.setCellValueFactory(cellData ->

                new SimpleStringProperty(cellData.getValue().getMatricula())
        );

        columnaNombre.setCellValueFactory(cellData ->

                new SimpleStringProperty(cellData.getValue().getNombre() + " " +
                        cellData.getValue().getApellido()
                )
        );

        tablaCronograma.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tablaCronograma.getSelectionModel().selectedItemProperty()
                .addListener((estudianteObservado,
                              estudianteAnterior, estudianteSeleccionado) -> {

                    if (estudianteSeleccionado != null) {
                        matriculaSeleccionada = estudianteSeleccionado.getMatricula();
                    } else {
                        matriculaSeleccionada = null;
                    }

                    tablaCronograma.refresh();
                });

        cargarEstudiantes();
        añadirBotonVerDetallesATabla();
    }

    private void añadirBotonVerDetallesATabla() {

        Callback<TableColumn<EstudianteDTO, Void>,
                TableCell<EstudianteDTO, Void>>
                fabricadorCeldas = columnaDetalles -> new TableCell<>() {

            private final Button botonDetalles = new Button("Ver detalles");
            {
                botonDetalles.setOnAction(evento -> {

                    EstudianteDTO estudianteSeleccionado =
                            getTableView().getItems().get(getIndex());
                    String matriculaSeleccionada = estudianteSeleccionado.getMatricula();
                    abrirVentanaDetallesCronograma(matriculaSeleccionada);
                });
            }

            @Override
            protected void updateItem(Void valorCelda, boolean vacio) {

                super.updateItem(valorCelda, vacio);

                if (vacio || tablaCronograma.getSelectionModel().getSelectedItem() == null ||
                        !tablaCronograma.getSelectionModel().getSelectedItem()
                                .equals(getTableView().getItems().get(getIndex()))) {

                    setGraphic(null);

                } else {

                    setGraphic(botonDetalles);
                }
            }
        };

        columnaDetalles.setCellFactory(fabricadorCeldas);
    }

    private void abrirVentanaDetallesCronograma(String matriculaSeleccionada) {

        try {

            FXMLLoader cargarFXML = new FXMLLoader(getClass().getResource("/DetallesCronogramaActividadesGUI.fxml"));
            Parent contenidoVentana = cargarFXML.load();

            ControladorDetallesCronogramaActividadesGUI controladorDetallesCronogramaActividadesGUI =
                    cargarFXML.getController();
            controladorDetallesCronogramaActividadesGUI.setMatriculaDTO(matriculaSeleccionada);
            controladorDetallesCronogramaActividadesGUI.setControladorPadre(this);

            Stage ventanaDetallesCronograma = new Stage();
            ventanaDetallesCronograma.setScene(new Scene(contenidoVentana));
            ventanaDetallesCronograma.setTitle("Detalles Proyecto");
            ventanaDetallesCronograma.show();

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);
        }
    }

    public void cargarEstudiantes() {

        AuxiliarGestionEstudiante auxiliarGestionEstudiantes = new AuxiliarGestionEstudiante();
        NRC = auxiliarGestionEstudiantes.obtenerNRC();

        try {

            EstudianteDAO estudianteDAO = new EstudianteDAO();
            ObservableList<EstudianteDTO> estudiantes =
                    FXCollections.observableArrayList(estudianteDAO.obtenerEstudiantesActivosPorNRC(NRC));
            tablaCronograma.setItems(estudiantes);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Error inesperado al cargar los estudiantes: " + e);
            gestorVentanas.mostrarAlerta(
                    "Error inesperado",
                    "No se pudieron cargar los estudiantes.",
                    "Por favor, intente nuevamente más tarde."
            );
        }
    }
}
