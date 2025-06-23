package logica;


import accesoadatos.AutenticadorDrive;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import javafx.stage.FileChooser;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubidorArchivosDrive {
    private final Drive servicioDrive;
    private final String folderId;

    public SubidorArchivosDrive(String folderId) throws IOException, GeneralSecurityException {

        this.servicioDrive = AutenticadorDrive.obtenerServicioDrive();
        this.folderId = folderId;
    }


    public Map<String, String> subirArchivos(List<java.io.File> archivosLocales) throws IOException {

        Map<String, String> urlsDrive = new HashMap<>();

        for (java.io.File archivoLocal : archivosLocales) {
            String nombreArchivo = archivoLocal.getName();
            String tipoMIME = determinarTipoMIME(nombreArchivo);


            File metadatos = new File();
            metadatos.setName(nombreArchivo);
            metadatos.setParents(Collections.singletonList(folderId));


            FileContent contenido = new FileContent(tipoMIME, archivoLocal);
            File archivoSubido = servicioDrive.files()
                    .create(metadatos, contenido)
                    .setFields("id, name, webViewLink")
                    .execute();

            urlsDrive.put(archivoSubido.getName(), archivoSubido.getWebViewLink());
        }

        return urlsDrive;
    }


    private String determinarTipoMIME(String nombreArchivo) {

        String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf('.')).toLowerCase();
        switch (extension) {
            case ".pdf": return "application/pdf";
            case ".jpg":
            case ".jpeg": return "image/jpeg";
            case ".png": return "image/png";
            default: return "application/octet-stream";
        }
    }
}