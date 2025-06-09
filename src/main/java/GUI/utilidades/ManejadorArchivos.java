package GUI.utilidades;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ManejadorArchivos {

    public File seleccionarUbicacionDeGuardado(Stage ventana, String nombreInicial) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Oficio de Asignación");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivo PDF", "*.pdf")
        );
        fileChooser.setInitialFileName(nombreInicial);
        return fileChooser.showSaveDialog(ventana);
    }
}