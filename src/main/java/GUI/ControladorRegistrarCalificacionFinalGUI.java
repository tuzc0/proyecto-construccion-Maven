package GUI;

import GUI.gestionestudiante.AuxiliarGestionEstudiante;
import GUI.utilidades.Utilidades;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import logica.DAOs.EstudianteDAO;
import logica.DTOs.EstudianteDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ControladorRegistrarCalificacionFinalGUI {

    private static final Logger LOGGER = LogManager.getLogger(ControladorRegistrarCalificacionFinalGUI.class);

    @FXML private TableView<EstudianteDTO> tablaAsignacion;
    @FXML private TableColumn<EstudianteDTO, String> columnaMatricula;
    @FXML private TableColumn<EstudianteDTO, String> columnaNombre;
    @FXML private TableColumn<EstudianteDTO, Double> columnaCalificacion;
    @FXML private TableColumn<EstudianteDTO, Void> columnaAccion;  // Nueva columna de acción

    private Utilidades utilidades = new Utilidades();
    private int nrcGrupo;

    @FXML
    private void initialize() {
        configurarTabla();
        cargarEstudiantes();
        tablaAsignacion.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void configurarTabla() {
        // Configurar columnas de datos
        columnaMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        columnaNombre.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getNombre() + " " + cellData.getValue().getApellido()
                ));

        // Mostrar calificación existente
        columnaCalificacion.setCellValueFactory(new PropertyValueFactory<>("calificacionFinal"));
        columnaCalificacion.setCellFactory(column -> new TableCell<EstudianteDTO, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : String.format("%.2f", item));
            }
        });

        // Configurar columna de acción con botón
        columnaAccion.setCellFactory(crearBotonAsignarCalificacion());
    }

    private Callback<TableColumn<EstudianteDTO, Void>, TableCell<EstudianteDTO, Void>> crearBotonAsignarCalificacion() {
        return param -> new TableCell<>() {
            private final Button btnAsignar = new Button("Asignar/Editar");

            {
                btnAsignar.setOnAction(event -> {
                    EstudianteDTO estudiante = getTableView().getItems().get(getIndex());
                    mostrarDialogoCalificacion(estudiante);
                });

                // Estilo opcional para el botón
                btnAsignar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnAsignar);
                }
            }
        };
    }

    private void cargarEstudiantes() {
        try {
            // Obtener NRC del grupo actual
            nrcGrupo = new AuxiliarGestionEstudiante().obtenerNRC();

            EstudianteDAO estudianteDAO = new EstudianteDAO();
            ObservableList<EstudianteDTO> estudiantes = FXCollections.observableArrayList(
                    estudianteDAO.obtenerEstudiantesActivosPorNRC(nrcGrupo)
            );

            tablaAsignacion.setItems(estudiantes);

        } catch (Exception e) {
            LOGGER.error("Error al cargar estudiantes: ", e);
            utilidades.mostrarAlerta(
                    "Error",
                    "No se pudieron cargar los estudiantes",
                    "Detalle: " + e.getMessage()
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

        // Validar entrada
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(calificacionStr -> {
            try {
                float calificacion = Float.parseFloat(calificacionStr);

                if (calificacion < 0 || calificacion > 10) {
                    utilidades.mostrarAlerta(
                            "Error",
                            "Valor inválido",
                            "La calificación debe estar entre 0 y 10"
                    );
                    return;
                }

                // Asignar calificación
                asignarCalificacion(estudiante, calificacion);

            } catch (NumberFormatException e) {
                utilidades.mostrarAlerta(
                        "Error",
                        "Entrada inválida",
                        "Debe ingresar un número válido"
                );
            }
        });
    }

    private void asignarCalificacion(EstudianteDTO estudiante, float calificacion) {
        try {
            EstudianteDAO estudianteDAO = new EstudianteDAO();
            boolean exito = estudianteDAO.asignarCalificacion(calificacion, estudiante.getMatricula());

            if (exito) {
                // Actualizar la tabla
                estudiante.setCalificacion(calificacion);
                tablaAsignacion.refresh();

                utilidades.mostrarAlerta(
                        "Éxito",
                        "Calificación asignada",
                        String.format("Se asignó %.2f a %s", calificacion, estudiante.getNombre())
                );
                LOGGER.info("Calificación asignada: {} -> {}", estudiante.getMatricula(), calificacion);
            } else {
                utilidades.mostrarAlerta(
                        "Error",
                        "No se pudo guardar",
                        "La calificación no se pudo guardar en la base de datos"
                );
            }
        } catch (SQLException | IOException e) {
            LOGGER.error("Error al asignar calificación: ", e);
            utilidades.mostrarAlerta(
                    "Error",
                    "Error de sistema",
                    "No se pudo completar la operación: " + e.getMessage()
            );
        }
    }
}