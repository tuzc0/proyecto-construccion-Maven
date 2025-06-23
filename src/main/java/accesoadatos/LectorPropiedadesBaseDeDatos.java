package accesoadatos;

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class LectorPropiedadesBaseDeDatos {

    Properties propiedades = new Properties();
    String url;
    String usuario;
    String contraseña;

    public LectorPropiedadesBaseDeDatos() throws IOException {

        try (InputStream lector =
                     getClass().getClassLoader().getResourceAsStream("archivosdeconfiguracion/configuracionBD.properties")) {

            propiedades.load(lector);
            url = propiedades.getProperty("URL");
            usuario = propiedades.getProperty("usuario");
            contraseña = propiedades.getProperty("password");

        }
    }

    public String getUrl() {
        return url;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContraseña() {
        return contraseña;
    }
}
