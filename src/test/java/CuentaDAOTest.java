import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.CuentaDAO;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.CuentaDTO;
import logica.DTOs.UsuarioDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaDAOTest {

    private CuentaDAO cuentaDAO;
    private UsuarioDAO usuarioDAO;
    private Connection conexionBaseDeDatos;

    private final List<Integer> IDS_USUARIOS_INSERTADOS = new ArrayList<>();

    @BeforeAll
    void prepararDatosDePrueba() {

        try {

            cuentaDAO = new CuentaDAO();
            usuarioDAO = new UsuarioDAO();
            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();

            IDS_USUARIOS_INSERTADOS.clear();

            PreparedStatement eliminarCuentas = conexionBaseDeDatos.prepareStatement("DELETE FROM cuenta");
            eliminarCuentas.executeUpdate();
            eliminarCuentas.close();

            PreparedStatement eliminarUsuarios = conexionBaseDeDatos.prepareStatement("DELETE FROM usuario WHERE idUsuario BETWEEN 1000 AND 2000");
            eliminarUsuarios.executeUpdate();
            eliminarUsuarios.close();

        } catch (SQLException | IOException e) {

            fail("Error al preparar los datos de prueba: " + e.getMessage());
        }
    }

    @AfterAll
    void limpiarDatosDePrueba() {

        try {

            PreparedStatement eliminarCuentas = conexionBaseDeDatos.prepareStatement("DELETE FROM cuenta WHERE idUsuario BETWEEN 1000 AND 2000");
            eliminarCuentas.executeUpdate();
            eliminarCuentas.close();


            for (int idUsuario : IDS_USUARIOS_INSERTADOS) {

                PreparedStatement eliminarUsuario = conexionBaseDeDatos.prepareStatement("DELETE FROM usuario WHERE idUsuario = ?");
                eliminarUsuario.setInt(1, idUsuario);
                eliminarUsuario.executeUpdate();
                eliminarUsuario.close();
            }

            conexionBaseDeDatos.close();

        } catch (SQLException e) {

            fail("Error al limpiar los datos de prueba: " + e.getMessage());
        }
    }

    @Test
    void testCrearNuevaCuentaConDatosValidos() {

        try {

            UsuarioDTO usuario = new UsuarioDTO(0, "Juan", "Pérez", 1);
            int idUsuario = usuarioDAO.insertarUsuario(usuario);
            IDS_USUARIOS_INSERTADOS.add(idUsuario);

            CuentaDTO cuenta = new CuentaDTO("juan@correo.com", "12345", idUsuario);
            boolean resultado = cuentaDAO.crearNuevaCuenta(cuenta);
            assertTrue(resultado, "La cuenta debería ser creada correctamente.");

        } catch (SQLException | IOException e) {
            fail("No se esperaba excepción: " + e.getMessage());
        }
    }

    @Test
    void testCrearNuevaCuentaConDatosInvalidos() {

        CuentaDTO cuentaInvalida = new CuentaDTO(null, null, -1);
        assertThrows(SQLException.class, () -> cuentaDAO.crearNuevaCuenta(cuentaInvalida), "Se esperaba una excepción debido a datos inválidos.");
    }

    @Test
    void testEliminarCuentaPorIDCuentaExistente() {

        try {

            UsuarioDTO usuario = new UsuarioDTO(0, "Ana", "López", 1);
            int idUsuario = usuarioDAO.insertarUsuario(usuario);
            IDS_USUARIOS_INSERTADOS.add(idUsuario);

            CuentaDTO cuenta = new CuentaDTO("ana@correo.com", "12345", idUsuario);
            cuentaDAO.crearNuevaCuenta(cuenta);

            boolean resultado = cuentaDAO.eliminarCuentaPorID(idUsuario);
            assertTrue(resultado, "La cuenta debería ser eliminada correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e.getMessage());
        }
    }

    @Test
    void testEliminarCuentaPorIDCuentaInexistente() {

        try {

            boolean resultado = cuentaDAO.eliminarCuentaPorID(99999);
            assertFalse(resultado, "No debería eliminarse una cuenta inexistente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e.getMessage());
        }
    }

    @Test
    void testModificarCuentaConDatosValidos() {

        try {

            UsuarioDTO usuario = new UsuarioDTO(0, "Carlos", "Martínez", 1);
            int idUsuario = usuarioDAO.insertarUsuario(usuario);
            IDS_USUARIOS_INSERTADOS.add(idUsuario);

            CuentaDTO cuenta = new CuentaDTO("carlos@correo.com", "12345", idUsuario);
            cuentaDAO.crearNuevaCuenta(cuenta);

            CuentaDTO cuentaModificada = new CuentaDTO("carlos@correo.com", "nuevaPass", idUsuario);
            boolean resultado = cuentaDAO.modificarCuenta(cuentaModificada);
            assertTrue(resultado, "La cuenta debería ser modificada correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e.getMessage());
        }
    }

    @Test
    void testModificarCuentaConDatosInvalidos() {

        try {

            CuentaDTO cuentaInvalida = new CuentaDTO(null, null, -1);
            boolean resultadoModificacion = cuentaDAO.modificarCuenta(cuentaInvalida);
            assertFalse(resultadoModificacion, "No debería modificarse una cuenta con datos inválidos.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e.getMessage());
        }
    }

    @Test
    void testModificarCuentaConCuentaInexistente() {

        try {

            CuentaDTO cuentaInexistente = new CuentaDTO("inexistente@correo.com", "12345", 99999);
            boolean resultado = cuentaDAO.modificarCuenta(cuentaInexistente);
            assertFalse(resultado, "No debería modificarse una cuenta inexistente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e.getMessage());
        }
    }

    @Test
    void testBuscarCuentaPorCorreoConCorreoExistente() {

        try {

            UsuarioDTO usuario = new UsuarioDTO(0, "Laura", "Gómez", 1);
            int idUsuario = usuarioDAO.insertarUsuario(usuario);
            IDS_USUARIOS_INSERTADOS.add(idUsuario);

            CuentaDTO cuenta = new CuentaDTO("laura@correo.com", "12345", idUsuario);
            cuentaDAO.crearNuevaCuenta(cuenta);

            CuentaDTO cuentaEncontrada = cuentaDAO.buscarCuentaPorCorreo("laura@correo.com");
            assertEquals(cuenta, cuentaEncontrada, "La cuenta encontrada debería coincidir con la esperada.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e.getMessage());
        }
    }

    @Test
    void testBuscarCuentaPorCorreoConCorreoInexistente() {

        try {

            CuentaDTO cuentaEncontrada = cuentaDAO.buscarCuentaPorCorreo("inexistente@correo.com");
            assertEquals(new CuentaDTO("N/A", "N/A", -1), cuentaEncontrada, "La cuenta encontrada debería ser la cuenta por defecto.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e.getMessage());
        }
    }

    @Test
    void testBuscarCuentaPorIDConIDExistente() {

        try {

            UsuarioDTO usuario = new UsuarioDTO(0, "Pedro", "Hernández", 1);
            int idUsuario = usuarioDAO.insertarUsuario(usuario);
            IDS_USUARIOS_INSERTADOS.add(idUsuario);

            CuentaDTO cuenta = new CuentaDTO("pedro@correo.com", "12345", idUsuario);
            cuentaDAO.crearNuevaCuenta(cuenta);

            CuentaDTO cuentaEncontrada = cuentaDAO.buscarCuentaPorID(idUsuario);
            assertEquals(cuenta, cuentaEncontrada, "La cuenta encontrada debería coincidir con la esperada.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e.getMessage());
        }
    }

    @Test
    void testBuscarCuentaPorIDconIDInexistente() {

        try {

            CuentaDTO cuentaEncontrada = cuentaDAO.buscarCuentaPorID(99999);
            assertEquals(new CuentaDTO("N/A", "N/A", -1), cuentaEncontrada, "La cuenta encontrada debería ser la cuenta por defecto.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e.getMessage());
        }
    }
}