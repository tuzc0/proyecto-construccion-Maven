package GUI;

import GUI.utilidades.Utilidades;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import logica.DAOs.AcademicoDAO;
import logica.DAOs.GrupoDAO;
import logica.DAOs.PeriodoDAO;
import logica.DTOs.AcademicoDTO;
import logica.DTOs.GrupoDTO;
import logica.DTOs.PeriodoDTO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorCrearGrupoGUI {

    Utilidades utilidades = new Utilidades();

    @FXML
    ComboBox <String> comboAcademico;

    @FXML
    Label etiquetaPeriodo;

    @FXML
    Label etiquetaNRC;

    @FXML
    TextField textoNombreGrupo;

    private Map<String, Integer> academicoMap = new HashMap<>();

    @FXML
    public void initialize() {
        cargarAcademicos();
        cargarPeriodo();
        cargarNRC();
    }

    @FXML
    private void guardarGrupo() {
        GrupoDAO grupoDAO = new GrupoDAO();
        String nombreSeleccionado = comboAcademico.getValue();

        if (nombreSeleccionado == null || nombreSeleccionado.isEmpty()) {
            utilidades.mostrarAlerta("Error",
                    "Académico no seleccionado",
                    "Debe seleccionar un académico para crear el grupo.");
            return;
        }

        if (!academicoMap.containsKey(nombreSeleccionado)) {
            utilidades.mostrarAlerta("Error",
                    "Académico no válido",
                    "Seleccione un académico válido.");
            return;
        }

        int numeroPersonal = academicoMap.get(nombreSeleccionado);

        try {

            if (grupoDAO.existeGrupoPorNumeroAcademico(numeroPersonal)) {
                utilidades.mostrarAlerta("Error",
                        "Académico ya asignado",
                        "El académico seleccionado ya está asignado a un grupo.");
                return;
            }

            GrupoDTO nuevoGrupo = new GrupoDTO();
            nuevoGrupo.setNRC(Integer.parseInt(etiquetaNRC.getText()));
            nuevoGrupo.setNombre(textoNombreGrupo.getText());
            nuevoGrupo.setNumeroPersonal(numeroPersonal);
            nuevoGrupo.setIdPeriodo(new PeriodoDAO().mostrarPeriodoActual().getIDPeriodo());
            nuevoGrupo.setEstadoActivo(1);

            grupoDAO.crearNuevoGrupo(nuevoGrupo);

            utilidades.mostrarAlerta("Éxito",
                    "Grupo guardado",
                    "El grupo se ha guardado correctamente.");

        } catch (SQLException e) {

            utilidades.mostrarAlerta(
                    "Error",
                    "Error al guardar el grupo",
                    "No se pudo guardar el grupo en la base de datos.");

        } catch (Exception e) {

            utilidades.mostrarAlerta("Error inesperado",
                    "Error",
                    "Ocurrió un error inesperado: ");
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

            utilidades.mostrarAlerta("Error al cargar los académicos: ",
                    "Error",
                    "No se pudieron cargar los académicos desde la base de datos.");

        } catch (Exception e) {

            utilidades.mostrarAlerta("Error inesperado: ",
                    "Error",
                    "Ocurrió un error inesperado al cargar los académicos.");
        }
    }

    public void cargarPeriodo(){

        PeriodoDAO periodoDAO = new PeriodoDAO();

        try {

            PeriodoDTO periodo = periodoDAO.mostrarPeriodoActual();
            etiquetaPeriodo.setText(periodo.getDescripcion());

        } catch (SQLException e) {
            utilidades.mostrarAlerta("Error al cargar los periodos: " ,
                    "Error",
                    "No se pudieron cargar los periodos desde la base de datos.");
        } catch (Exception e) {
            utilidades.mostrarAlerta("Error inesperado: ",
                    "Error",
                    "Ocurrió un error inesperado al cargar los periodos.");
        }
    }

    public void cargarNRC() {

        GrupoDAO grupoDAO = new GrupoDAO();

        try {

            int nrc = grupoDAO.generarNRC();
            etiquetaNRC.setText(String.valueOf(nrc));

        } catch (SQLException e) {

            utilidades.mostrarAlerta("Error al generar NRC: ",
                    "Error",
                    "No se pudo generar el NRC desde la base de datos.");
        } catch (Exception e) {

            utilidades.mostrarAlerta("Error inesperado: ",
                    "Error",
                    "Ocurrió un error inesperado al generar el NRC.");
        }

    }
}
