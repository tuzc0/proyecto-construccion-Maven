package GUI;

import GUI.gestionestudiante.AuxiliarGestionEstudiante;
import GUI.utilidades.Utilidades;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import logica.DAOs.EstudianteDAO;
import logica.DTOs.EstudianteDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

public class ControladorRegistrarCalificacionFinalGUI {

    private static final Logger LOGGER = LogManager.getLogger(ControladorRegistrarCalificacionFinalGUI.class);

    @FXML TableView<EstudianteDTO> tablaAsignacion;
    @FXML TableColumn<EstudianteDTO, String> columnaMatricula;
    @FXML TableColumn<EstudianteDTO, String> columnaNombre;
    @FXML TableColumn<EstudianteDTO, String> columnaCalificacion;

    private EstudianteDTO estudianteSeleccionado;
    private Utilidades utilidades = new Utilidades();

    @FXML
    private void initialize() {

        columnaMatricula.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMatricula()));
        columnaNombre.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre() + " " + cellData.getValue().getApellido()));

        cargarEstudiantes();
        añadirBotonAsignarCalificacionATabla();

        tablaAsignacion.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tablaAsignacion.getSelectionModel().selectedItemProperty().addListener((valorObservado, antiguoValor, nuevoValor) -> {

            estudianteSeleccionado = nuevoValor;
            tablaAsignacion.refresh();
        });
    }

    private void añadirBotonAsignarCalificacionATabla() {

        Callback<TableColumn<EstudianteDTO, String>, TableCell<EstudianteDTO, String>> cellFactory = param -> new TableCell<>() {

            private final Button botonAsignar = new Button("Asignar Calificación");
            {

                botonAsignar.setOnAction(evento -> {
                    EstudianteDTO contenedorEstudianteDTO = getTableView().getItems().get(getIndex());
                    asignarCalificacion();
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {

                super.updateItem(item, empty);

                if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {

                    setGraphic(null);

                } else {

                    setGraphic(botonAsignar);
                }
            }
        };

        columnaCalificacion.setCellFactory(cellFactory);
    }

    private void cargarEstudiantes() {

        try {

            EstudianteDAO estudianteDAO = new EstudianteDAO();
            ObservableList<EstudianteDTO> estudiantes = FXCollections.observableArrayList(estudianteDAO.listarEstudiantes());
            tablaAsignacion.setItems(estudiantes);

        } catch (Exception e) {

            LOGGER.error("Error al cargar la lista de estudiantes: ", e);
            utilidades.mostrarAlerta(
                    "Error al cargar estudiantes",
                    "No se pudieron cargar los estudiantes.",
                    "Por favor, intente nuevamente más tarde."
            );
        }
    }

    private void asignarCalificacion() {

        if (estudianteSeleccionado == null) {

            utilidades.mostrarAlerta(
                    "Error",
                    "Primero necesita escoger un estudiante.",
                    "Por favor, seleccione un estudiante."
            );
            return;
        }

        try {

            EstudianteDAO estudianteDAO = new EstudianteDAO();
            EstudianteDTO estudiante = estudianteDAO.buscarEstudiantePorMatricula(estudianteSeleccionado.getMatricula());

            if (estudiante.getCalificacion() > 0) {

                utilidades.mostrarAlerta(
                        "Advertencia",
                        "El estudiante ya tiene una calificación asignada.",
                        "La calificación actual es: " + estudiante.getCalificacion()
                );
                return;
            }

            TextInputDialog dialogoDeEntrada = new TextInputDialog();

            dialogoDeEntrada.setTitle("Asignar Calificación");
            dialogoDeEntrada.setHeaderText("Ingrese la calificación final para el estudiante:");
            dialogoDeEntrada.setContentText("Calificación:");

            String calificacionIngresada = dialogoDeEntrada.showAndWait().orElse("");

            double calificacion = Double.parseDouble(calificacionIngresada);

            if (calificacion < 0 || calificacion > 10) {

                utilidades.mostrarAlerta(
                        "Error",
                        "Calificación inválida.",
                        "La calificación debe estar entre 0 y 10."
                );
                return;
            }

            boolean calificacionAsignada = estudianteDAO.asignarCalificacion(calificacion, estudianteSeleccionado.getMatricula());

            if (calificacionAsignada) {

                utilidades.mostrarAlerta(
                        "Éxito",
                        "Calificación asignada correctamente.",
                        "La calificación ha sido registrada."
                );
                LOGGER.info("Calificación asignada correctamente al estudiante con matrícula: {}", estudianteSeleccionado.getMatricula());

            } else {

                utilidades.mostrarAlerta(
                        "Error",
                        "No fue posible asignar la calificación al estudiante.",
                        "Por favor, intentelo más tarde o contacte al administrador."
                );
                LOGGER.error("No se pudo asignar la calificación al estudiante con matrícula: {}", estudianteSeleccionado.getMatricula());
            }

            tablaAsignacion.refresh();

        } catch (NumberFormatException e) {

            utilidades.mostrarAlerta(
                    "Error",
                    "Entrada inválida.",
                    "Por favor, ingrese un número válido."
            );
            LOGGER.warn("Entrada inválida al intentar asignar calificación: {}", e);

        } catch (SQLException | IOException e) {

            utilidades.mostrarAlerta(
                    "Error",
                    "Error inesperado.",
                    "Por favor, contacte al administrador."
            );
            LOGGER.error("Error al asignar calificación al estudiante: ", e);
        }
    }
}