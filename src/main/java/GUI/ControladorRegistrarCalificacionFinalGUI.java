package GUI;

import GUI.gestionestudiante.AuxiliarGestionEstudiante;
import GUI.utilidades.Utilidades;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import logica.DAOs.EstudianteDAO;
import logica.DTOs.EstudianteDTO;
import logica.ManejadorExcepciones;
import logica.interfaces.IGestorAlertas;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ControladorRegistrarCalificacionFinalGUI {

    private static final Logger LOGGER = LogManager.getLogger(ControladorRegistrarCalificacionFinalGUI.class);

    @FXML private TableView<EstudianteDTO> tablaAsignacion;
    @FXML private TableColumn<EstudianteDTO, String> columnaMatricula;
    @FXML private TableColumn<EstudianteDTO, String> columnaNombre;
    @FXML private TableColumn<EstudianteDTO, Float> columnaCalificacion;
    @FXML private TableColumn<EstudianteDTO, Void> columnaAccion;

    private Utilidades gestorVentanas = new Utilidades();
    private IGestorAlertas utilidades = new Utilidades();
    private ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, LOGGER);

    private int nrcGrupo = 0;

    @FXML
    private void initialize() {

        configurarTabla();
        cargarEstudiantes();
        tablaAsignacion.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void configurarTabla() {

        columnaMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        columnaNombre.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getNombre() + " " + cellData.getValue().getApellido()
                ));

        columnaCalificacion.setCellValueFactory(cellData ->
                new SimpleFloatProperty(cellData.getValue().getCalificacion()).asObject());


        columnaAccion.setCellFactory(crearBotonAsignarCalificacion());
    }

    private Callback<TableColumn<EstudianteDTO, Void>, TableCell<EstudianteDTO, Void>> crearBotonAsignarCalificacion() {
        return param -> new TableCell<>() {
            private final Button botonAsignar = new Button("Asignar/Editar");

            {
                botonAsignar.setOnAction(event -> {
                    EstudianteDTO estudiante = getTableView().getItems().get(getIndex());
                    mostrarDialogoCalificacion(estudiante);
                });

                botonAsignar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(botonAsignar);
                }
            }
        };
    }

    private void cargarEstudiantes() {

        try {

            nrcGrupo = new AuxiliarGestionEstudiante().obtenerNRC();
            EstudianteDAO estudianteDAO = new EstudianteDAO();
            List<EstudianteDTO> estudiantesBD = estudianteDAO.obtenerEstudiantesActivosConCalificacionPorNRC(nrcGrupo);
            ObservableList<EstudianteDTO> estudiantes = FXCollections.observableArrayList();
            estudiantes.addAll(estudiantesBD);

            tablaAsignacion.setItems(estudiantes);
            tablaAsignacion.refresh();

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            LOGGER.error("Error inesperado al cargar estudiantes: ", e);
            gestorVentanas.mostrarAlerta(
                    "Error",
                    "Error inesperado",
                    "Ocurrió un error interno dentro del sistema, por favor intente más tarde."
            );
        }
    }

    private void mostrarDialogoCalificacion(EstudianteDTO estudiante) {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Asignar Calificación");
        dialog.setHeaderText(String.format(
                "Estudiante: %s %s (%s)\nIngrese la calificación final:",
                estudiante.getNombre(),
                estudiante.getApellido(),
                estudiante.getMatricula()
        ));
        dialog.setContentText("Calificación (0-10):");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(calificacionStr -> {

            try {

                float calificacion = Float.parseFloat(calificacionStr);

                if (calificacion < 0 || calificacion > 10) {

                    gestorVentanas.mostrarAlerta(
                            "Error",
                            "Valor inválido",
                            "La calificación debe estar entre 0 y 10"
                    );
                    return;
                }

                asignarCalificacion(estudiante, calificacion);

            } catch (NumberFormatException e) {

                LOGGER.error("Error al convertir la calificación: ", e);
                gestorVentanas.mostrarAlerta(
                        "Error",
                        "Entrada inválida",
                        "Debe ingresar un número válido"
                );

            } catch (Exception e) {

                LOGGER.error("Error inesperado al asignar calificación: ", e);
                gestorVentanas.mostrarAlerta(
                        "Error",
                        "Error inesperado",
                        "Ocurrió un error interno dentro del sistema, por favor intente más tarde."
                );
            }
        });
    }

    private void asignarCalificacion(EstudianteDTO estudiante, float calificacion) {

        try {

            EstudianteDAO estudianteDAO = new EstudianteDAO();
            boolean exito = estudianteDAO.asignarCalificacion(calificacion, estudiante.getMatricula());

            if (exito) {

                estudiante.setCalificacion(calificacion);
                tablaAsignacion.refresh();

                gestorVentanas.mostrarAlerta(
                        "Éxito",
                        "Calificación asignada",
                        String.format("Se asignó %.2f a %s", calificacion, estudiante.getNombre())
                );

                LOGGER.info("Calificación asignada: {} -> {}", estudiante.getMatricula(), calificacion);

            } else {

                gestorVentanas.mostrarAlerta(
                        "Error",
                        "No se pudo guardar",
                        "La calificación no se pudo guardar en la base de datos"
                );
            }

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            LOGGER.error("Error inesperado al asignar calificación: ", e);
            gestorVentanas.mostrarAlerta(
                    "Error",
                    "Error inesperado",
                    "Ocurrió un error interno dentro del sistema, por favor intente más tarde."
            );
        }
    }
}