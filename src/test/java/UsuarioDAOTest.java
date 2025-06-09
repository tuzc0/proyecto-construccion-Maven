import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.UsuarioDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioDAOTest {

    private UsuarioDAO usuarioDAO;
    private Connection conexionBaseDeDatos;
    private final List<Integer> IDS_USUARIOS_INSERTADOS = new ArrayList<>();

    @BeforeEach
    void prepararDatosDePrueba() {

        try {

            usuarioDAO = new UsuarioDAO();
            IDS_USUARIOS_INSERTADOS.clear();
            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();

            try (PreparedStatement eliminarUsuario = conexionBaseDeDatos.prepareStatement(

                    "DELETE FROM usuario WHERE idUsuario BETWEEN 1000 AND 2000")) {
                eliminarUsuario.executeUpdate();
            }
        } catch (SQLException | IOException e) {

            fail("Error al preparar los datos de prueba: " + e);
        }
    }

    @AfterEach
    void limpiarDatosDePrueba() {

        try {

            for (int idUsuarioInsertado : IDS_USUARIOS_INSERTADOS) {

                try (PreparedStatement eliminarUsuario =
                             conexionBaseDeDatos.prepareStatement("DELETE FROM usuario WHERE idUsuario = ?")) {

                    eliminarUsuario.setInt(1, idUsuarioInsertado);
                    eliminarUsuario.executeUpdate();
                }
            }

            conexionBaseDeDatos.close();

        } catch (SQLException e) {

            fail("Error al limpiar los datos de prueba: " + e);
        }
    }

    @Test
    void testInsertarUsuarioConDatosValidos() {

        try {

            UsuarioDTO usuarioDTO = new UsuarioDTO(0, "Luis", "González", 1);
            int idGenerado = usuarioDAO.insertarUsuario(usuarioDTO);

            IDS_USUARIOS_INSERTADOS.add(idGenerado);
            assertTrue(idGenerado > 0, "El ID generado debería ser mayor a 0.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void insertarUsuarioConDatosInvalidos() {

        UsuarioDTO usuarioInvalido = new UsuarioDTO(0, null, "Apellido", 1);

        assertThrows(SQLException.class, () -> usuarioDAO.insertarUsuario(usuarioInvalido),
                "Debería lanzar SQLException con datos inválidos");
    }

    @Test
    void testEliminarUsuarioPorIDConUsuarioExistente() {

        try {

            UsuarioDTO usuarioDTO = new UsuarioDTO(0, "Carlos", "Martínez", 1);
            int idInsertado = usuarioDAO.insertarUsuario(usuarioDTO);
            IDS_USUARIOS_INSERTADOS.add(idInsertado);

            boolean resultadoUsuarioEliminado = usuarioDAO.eliminarUsuarioPorID(idInsertado);
            assertTrue(resultadoUsuarioEliminado, "El usuario debería ser eliminado correctamente.");

            UsuarioDTO usuarioObtenido = usuarioDAO.buscarUsuarioPorID(idInsertado);
            assertEquals(0, usuarioObtenido.getEstado(), "El estado del usuario debería ser inactivo.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testEliminarUsuarioPorIDConUsuarioInexistente() {

        try {

            boolean resutadoUsuarioEliminado = usuarioDAO.eliminarUsuarioPorID(88888);
            assertFalse(resutadoUsuarioEliminado, "No debería eliminarse un usuario inexistente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void eliminarUsuarioPorIDInvalido() {

        try {

            boolean resultado = usuarioDAO.eliminarUsuarioPorID(-1);
            assertFalse(resultado, "No debería eliminar un usuario con ID inválido");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testModificarUsuarioConUsuarioExistente() {

        try {

            UsuarioDTO usuarioDTOOriginal = new UsuarioDTO(0, "Elena", "Gómez", 1);
            int idInsertado = usuarioDAO.insertarUsuario(usuarioDTOOriginal);
            IDS_USUARIOS_INSERTADOS.add(idInsertado);

            UsuarioDTO usuarioActualizado = new UsuarioDTO(idInsertado, "Elena Patricia", "Gómez Ruiz", 1);
            boolean ResultadoModificacion = usuarioDAO.modificarUsuario(usuarioActualizado);

            UsuarioDTO usuarioOriginal = usuarioDAO.buscarUsuarioPorID(idInsertado);
            assertEquals(usuarioActualizado, usuarioOriginal, "El usuario en la base de datos debería coincidir con el actualizado.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void modificarConDatosInvalidos() {

        try {

            UsuarioDTO usuarioValido = new UsuarioDTO(0, "NombreValido", "ApellidoValido", 1);
            int idInsertado = usuarioDAO.insertarUsuario(usuarioValido);
            IDS_USUARIOS_INSERTADOS.add(idInsertado);

            UsuarioDTO usuarioConDatosInvalidos = new UsuarioDTO(idInsertado, null, "ApellidoInvalido", 1);

            assertThrows(SQLException.class, () -> usuarioDAO.modificarUsuario(usuarioConDatosInvalidos),
                    "Debería lanzar SQLException al intentar modificar con datos inválidos");

        } catch (SQLException | IOException e) {
            fail("No se esperaba una excepción en este punto: " + e);
        }
    }

    @Test
    void testModificarUsuarioConUsuarioInexistente() {

        try {

            UsuarioDTO usuarioInexistente = new UsuarioDTO(77777, "Nombre", "Apellido", 1);
            boolean resultadoUsuarioModificado = usuarioDAO.modificarUsuario(usuarioInexistente);

            assertFalse(resultadoUsuarioModificado, "No debería modificarse un usuario inexistente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testBuscarUsuarioPorIDConUsuarioExistente() {

        try {

            UsuarioDTO usuarioEsperado = new UsuarioDTO(0, "Ana", "Ramírez", 1);
            int idInsertado = usuarioDAO.insertarUsuario(usuarioEsperado);
            IDS_USUARIOS_INSERTADOS.add(idInsertado);

            UsuarioDTO usuarioObtenido = usuarioDAO.buscarUsuarioPorID(idInsertado);
            UsuarioDTO usuarioConIdEsperado = new UsuarioDTO(idInsertado, "Ana", "Ramírez", 1);

            assertEquals(usuarioConIdEsperado, usuarioObtenido, "El usuario obtenido debería coincidir con el esperado.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testBuscarUsuarioPorIDConUsuarioInexistente() {

        try {

            UsuarioDTO usuarioObtenido = usuarioDAO.buscarUsuarioPorID(99999);
            UsuarioDTO usuarioEsperado = new UsuarioDTO(-1, "N/A", "N/A", 0);

            assertEquals(usuarioEsperado, usuarioObtenido, "El usuario obtenido debería ser el usuario por defecto.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void buscarUsuarioPorIDInvalido() {

        try {

            UsuarioDTO usuarioEsperado = new UsuarioDTO(-1, "N/A", "N/A", 0);
            UsuarioDTO usuarioObtenido = usuarioDAO.buscarUsuarioPorID(-1);

            assertEquals(usuarioEsperado, usuarioObtenido,
                    "Debería retornar el usuario por defecto para ID inválido");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }
}