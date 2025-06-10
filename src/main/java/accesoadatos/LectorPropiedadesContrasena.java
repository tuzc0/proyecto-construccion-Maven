package accesoadatos;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LectorPropiedadesContrasena {

    public static String obtenerClaveSecreta() throws IOException {
        Properties propiedades = new Properties();

        try (InputStream input = LectorPropiedadesContrasena.class.getClassLoader().getResourceAsStream("contrasena.properties")) {
            if (input == null) {
                throw new IOException("No se encontró el archivo config.properties");
            }
            propiedades.load(input);
            return propiedades.getProperty("clave.secreta");
        }
    }
}
