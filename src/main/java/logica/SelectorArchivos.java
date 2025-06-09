package logica;


import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.File;
import java.util.List;

public class SelectorArchivos {
    private final FileChooser fileChooser;
    private final Window ventanaPadre;

    public SelectorArchivos(Window ventanaPadre, String titulo) {
        this.ventanaPadre = ventanaPadre;
        this.fileChooser = new FileChooser();
        this.fileChooser.setTitle(titulo);
        configurarFiltros();
    }

    private void configurarFiltros() {
        FileChooser.ExtensionFilter filtroPDF = new FileChooser.ExtensionFilter("PDF", "*.pdf");
        FileChooser.ExtensionFilter filtroImagen = new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().addAll(filtroPDF, filtroImagen);
    }


    public List<File> seleccionarMultiplesArchivos() {
        return fileChooser.showOpenMultipleDialog(ventanaPadre);
    }


    public static boolean validarArchivos(List<File> archivos, double tamanoMaximoMB) {
        if (archivos == null || archivos.isEmpty()) {
            return false;
        }


        double tamanoTotalMB = archivos.stream()
                .mapToDouble(f -> f.length() / (1024.0 * 1024.0))
                .sum();

        if (tamanoTotalMB > tamanoMaximoMB) {
            return false;
        }


        for (File archivo : archivos) {
            String nombre = archivo.getName().toLowerCase();
            if (!nombre.endsWith(".pdf") && !nombre.endsWith(".png") &&
                    !nombre.endsWith(".jpg") && !nombre.endsWith(".jpeg")) {
                return false;
            }
        }

        return true;
    }
}
