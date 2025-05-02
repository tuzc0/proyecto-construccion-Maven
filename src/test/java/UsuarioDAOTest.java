import logica.DAOs.UsuarioDAO;
import logica.DTOs.UsuarioDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsuarioDAOTest {

    private static UsuarioDAO usuarioDAO;

    @BeforeAll
    static void setUp() {

        usuarioDAO = new UsuarioDAO();
    }

    @AfterAll
    static void tearDown() {

        usuarioDAO = null;
    }

    @Test
    @Order(1)
    void testInsertarUsuarioDatosValidos() {
        UsuarioDTO usuario = new UsuarioDTO(0, "Prueba", "Insertar Usuario", 1);

        try {

            int idUsuario = usuarioDAO.insertarUsuario(usuario);
            assertEquals(54, idUsuario, "El ID del usuario insertado debe coincidir con el esperado.");

        } catch (SQLException | IOException e) {

            fail("Ocurrió una excepción durante la prueba: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    void testEliminarUsuarioPorIDDatosValidos() {

        try {

            boolean resultado = usuarioDAO.eliminarUsuarioPorID(51);
            assertTrue(resultado, "El usuario debería haberse eliminado correctamente.");

        } catch (SQLException | IOException e) {

            fail("Ocurrió una excepción durante la prueba: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    void testModificarUsuarioDatosValidos() {

        UsuarioDTO usuario = new UsuarioDTO(51, "Modificado", "Usuario", 1);

        try {

            boolean resultado = usuarioDAO.modificarUsuario(usuario);
            assertTrue(resultado, "El usuario debería haberse modificado correctamente.");

        } catch (SQLException | IOException e) {

            fail("Ocurrió una excepción durante la prueba: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    void testBuscarUsuarioPorIDDatosValidos() {

        try {
            UsuarioDTO usuario = usuarioDAO.buscarUsuarioPorID(51);
            assertEquals(51, usuario.getIdUsuario(), "El ID del usuario debería coincidir.");

        } catch (SQLException | IOException e) {

            fail("Ocurrió una excepción durante la prueba: " + e.getMessage());
        }
    }
}