package accesoadatos;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class AutenticadorDrive {

    private static final String RUTA_CREDENCIALES = "src/main/resources/credenciales/Credenciales2.json";
    private static final List<String> ALCANCES = Collections.singletonList(DriveScopes.DRIVE_FILE);

    public static Drive obtenerServicioDrive() throws IOException, GeneralSecurityException {

        final NetHttpTransport transporte = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredential credencial = GoogleCredential.fromStream(new FileInputStream(RUTA_CREDENCIALES))
                .createScoped(ALCANCES);

        return new Drive.Builder(transporte, GsonFactory.getDefaultInstance(), credencial)
                .setApplicationName("Proyecto-Construccion")
                .build();
    }
}