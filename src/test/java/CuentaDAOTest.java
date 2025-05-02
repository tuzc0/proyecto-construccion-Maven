import org.junit.jupiter.api.*;
import logica.DAOs.CuentaDAO;
import logica.DTOs.CuentaDTO;
import java.io.IOException;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaDAOTest {

    private CuentaDAO cuentaDAO;

    @BeforeAll
    void setUp() {

        cuentaDAO = new CuentaDAO();
    }

    @Test
    void testCrearNuevaCuentaDatosValidos() {

        CuentaDTO cuenta = new CuentaDTO("nue@correo.com", "12345", 1);

        try {

            boolean resultado = cuentaDAO.crearNuevaCuenta(cuenta);
            assertTrue(resultado, "La cuenta debería ser creada correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e.getMessage());
        }
    }

    @Test
    void testEliminarCuentaPorIDDatosValidos() {

        int idUsuario = 3;

        try {

            boolean resultado = cuentaDAO.eliminarCuentaPorID(idUsuario);
            assertTrue(resultado, "La cuenta debería ser eliminada correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e.getMessage());
        }
    }

    @Test
    void testModificarCuentaDatosValidos() {

        CuentaDTO cuenta = new CuentaDTO("modifica@correo.com", "nuevaPass", 1);

        try {

            boolean resultado = cuentaDAO.modificarCuenta(cuenta);
            assertTrue(resultado, "La cuenta debería ser modificada correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e.getMessage());
        }
    }

    @Test
    void testBuscarCuentaPorIDDatosValidos() {

        String correo = "modificad@correo.com";

        try {

            CuentaDTO cuenta = cuentaDAO.buscarCuentaPorCorreo(correo);
            assertEquals(correo, cuenta.getCorreoElectronico(), "El correo debería coincidir.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e.getMessage());
        }
    }
}
