package accesoadatos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private Connection conexion;

    public ConexionBD() throws IOException, SQLException {

        LectorPropiedadesBD propiedades = new LectorPropiedadesBD();

        conexion = DriverManager.getConnection(
                propiedades.getUrl(),
                propiedades.getUsuario(),
                propiedades.getContraseña()
        );
    }

    public Connection getConnection() {
        return conexion;
    }
}
