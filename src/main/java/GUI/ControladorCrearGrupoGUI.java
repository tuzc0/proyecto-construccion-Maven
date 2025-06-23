package GUI;

import GUI.utilidades.Utilidades;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logica.DAOs.AcademicoDAO;
import logica.DAOs.GrupoDAO;
import logica.DAOs.PeriodoDAO;
import logica.DTOs.AcademicoDTO;
import logica.DTOs.GrupoDTO;
import logica.DTOs.PeriodoDTO;
import logica.ManejadorExcepciones;
import logica.VerificacionEntradas;
import logica.interfaces.IGestorAlertas;
import logica.verificacion.VerificicacionGeneral;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorCrearGrupoGUI {

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(ControladorCrearGrupoGUI.class);

    @FXML
    ComboBox<String> comboAcademico;

    @FXML
    Label etiquetaPeriodo;

    @FXML
    Label etiquetaNRC;

    @FXML
    TextField textoNombreGrupo;

    @FXML
    Label etiquetaContadorNombre;

    private Map<String, Integer> academicoMap = new HashMap<>();
    Utilidades gestorVentanas = new Utilidades();
    VerificicacionGeneral verificicacionGeneralUtilidad = new VerificicacionGeneral();
    VerificacionEntradas verificacionEntradas = new VerificacionEntradas();
    IGestorAlertas utilidades = new Utilidades();
    ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, logger);

    final int MAX_CARACTERES_NOMBRE = 100;

    @FXML
    public void initialize() {

        verificicacionGeneralUtilidad.contadorCaracteresTextField(textoNombreGrupo,
                etiquetaContadorNombre, MAX_CARACTERES_NOMBRE);

        cargarAcademicos();
        cargarPeriodo();
        cargarNRC();
    }

    @FXML
    private void guardarGrupo() {

        GrupoDAO grupoDAO = new GrupoDAO();
        String nombreSeleccionado = comboAcademico.getValue();

        if (nombreSeleccionado == null || nombreSeleccionado.isEmpty()) {

            gestorVentanas.mostrarAlerta(
                    "Error",
                    "Académico no seleccionado",
                    "Debe seleccionar un académico para crear el grupo."
            );
            return;
        }

        if (!academicoMap.containsKey(nombreSeleccionado)) {

            gestorVentanas.mostrarAlerta("Error",
                    "Académico no válido",
                    "Seleccione un académico válido."
            );
            return;
        }

        int numeroPersonal = academicoMap.get(nombreSeleccionado);

        try {

            if (grupoDAO.existeGrupoPorNumeroAcademico(numeroPersonal)) {

                gestorVentanas.mostrarAlerta(
                        "Error",
                        "Académico ya asignado",
                        "El académico seleccionado ya está asignado a un grupo."
                );
                return;
            }

            GrupoDTO nuevoGrupo = new GrupoDTO();
            nuevoGrupo.setNRC(Integer.parseInt(etiquetaNRC.getText()));
            nuevoGrupo.setNombre(textoNombreGrupo.getText());
            nuevoGrupo.setNumeroPersonal(numeroPersonal);
            nuevoGrupo.setIdPeriodo(new PeriodoDAO().mostrarPeriodoActual().getIDPeriodo());
            nuevoGrupo.setEstadoActivo(1);

            if (nuevoGrupo.getNombre().isEmpty()) {

                gestorVentanas.mostrarAlerta(
                        "Error",
                        "Nombre de grupo vacío",
                        "El nombre del grupo no puede estar vacío."
                );
                return;
            }

            if (!verificacionEntradas.validarTextoAlfanumerico(nuevoGrupo.getNombre())) {

                gestorVentanas.mostrarAlerta(
                        "Error",
                        "Nombre de grupo inválido",
                        "El nombre del grupo no puede estar vacío o exceder los 100 caracteres."
                );
                return;
            }

            grupoDAO.crearNuevoGrupo(nuevoGrupo);

            gestorVentanas.mostrarAlerta("Éxito",
                    "Grupo guardado",
                    "El grupo se ha guardado correctamente.");

            ((Stage) comboAcademico.getScene().getWindow()).close();

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Ocurrio un error al guardar un nuevo grupo: " + e.getMessage(), e);
            gestorVentanas.mostrarAlerta(
                    "Error",
                    "Ocurrió un eror al guardar el grupo.",
                    "Por favor, inténtelo de nuevo más tarde."
            );
        }
    }

    public void cargarAcademicos() {

        AcademicoDAO academicoDAO = new AcademicoDAO();

        try {

            List<AcademicoDTO> academicos = academicoDAO.listarAcademicos();
            ObservableList<String> nombresAcademicos = FXCollections.observableArrayList();

            for (AcademicoDTO academico : academicos) {

                String nombreCompleto = academico.getNombre() + " " + academico.getApellido();
                nombresAcademicos.add(nombreCompleto);
                academicoMap.put(nombreCompleto, academico.getNumeroDePersonal());
            }

            comboAcademico.setItems(nombresAcademicos);

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Ocurrio un error al cargar el academico: " + e.getMessage(), e);
            gestorVentanas.mostrarAlerta(
                    "Error",
                    "Ocurrió un error cargar a los académicos.",
                    "Por favor, inténtelo de nuevo más tarde."
            );
        }
    }

    public void cargarPeriodo() {

        PeriodoDAO periodoDAO = new PeriodoDAO();

        try {

            PeriodoDTO periodo = periodoDAO.mostrarPeriodoActual();
            etiquetaPeriodo.setText(periodo.getDescripcion());

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Ocurrio un error al cargar el periodo: " + e.getMessage(), e);
            gestorVentanas.mostrarAlerta(
                    "Error",
                    "Ocurrió un error cargar el periodo .",
                    "Por favor, inténtelo de nuevo más tarde."
            );
        }
    }

    public void cargarNRC() {

        GrupoDAO grupoDAO = new GrupoDAO();

        try {

            int nrc = grupoDAO.generarNRC();
            etiquetaNRC.setText(String.valueOf(nrc));

        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);

        } catch (Exception e) {

            logger.error("Ocurrio un error al cargar el NRC: " + e.getMessage(), e);
            gestorVentanas.mostrarAlerta(
                    "Error",
                    "Ocurrió un error cargar el NRC.",
                    "Por favor, inténtelo de nuevo más tarde."
            );
        }

    }
}
