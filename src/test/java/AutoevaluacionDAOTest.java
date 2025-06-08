import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.AutoevaluacionDAO;
import logica.DAOs.EstudianteDAO;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.AutoevaluacionDTO;
import logica.DTOs.EstudianteDTO;
import logica.DTOs.UsuarioDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class AutoevaluacionDAOTest {

    private AutoevaluacionDAO autoevaluacionDAO;
    private EstudianteDAO estudianteDAO;
    private UsuarioDAO usuarioDAO;
    private Connection conexionBaseDeDatos;

    private final List<Integer> IDS_AUTOEVALUACIONES_INSERTADAS = new ArrayList<>();
    private final List<String> MATRICULAS_INSERTADAS = new ArrayList<>();
    private final List<Integer> IDS_USUARIOS_INSERTADOS    = new ArrayList<>();

    @BeforeEach
    void prepararDatosDePrueba() {

        autoevaluacionDAO = new AutoevaluacionDAO();
        usuarioDAO = new UsuarioDAO();

        try {

            estudianteDAO = new EstudianteDAO();
            IDS_AUTOEVALUACIONES_INSERTADAS.clear();
            MATRICULAS_INSERTADAS.clear();
            IDS_USUARIOS_INSERTADOS.clear();

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();

            conexionBaseDeDatos.prepareStatement("DELETE FROM autoevaluacion WHERE idAutoevaluacion BETWEEN 1000 AND 2000").executeUpdate();

            conexionBaseDeDatos.prepareStatement("DELETE FROM estudiante WHERE matricula LIKE 'S230%'").executeUpdate();

            conexionBaseDeDatos.prepareStatement("DELETE FROM usuario WHERE idUsuario BETWEEN 1000 AND 2000").executeUpdate();

            UsuarioDTO usuarioEstudiante1DTO = new UsuarioDTO(0, "Nombre", "Prueba", 1);
            UsuarioDTO usuarioEstudiante2DTO = new UsuarioDTO(0, "Usuario2", "Apellido2", 1);
            UsuarioDTO usuarioEstudiante3DTO = new UsuarioDTO(0, "Usuario3", "Apellido3", 1);

            int idUsuarioEstudiante1 = usuarioDAO.insertarUsuario(usuarioEstudiante1DTO);
            int idUsuarioEstudiante2 = usuarioDAO.insertarUsuario(usuarioEstudiante2DTO);
            int idUsuarioEstudiante3 = usuarioDAO.insertarUsuario(usuarioEstudiante3DTO);

            IDS_USUARIOS_INSERTADOS.addAll(List.of(idUsuarioEstudiante1, idUsuarioEstudiante2, idUsuarioEstudiante3));

            estudianteDAO.insertarEstudiante(new EstudianteDTO(idUsuarioEstudiante1, usuarioEstudiante1DTO.getNombre(), usuarioEstudiante1DTO.getApellido(), "S23014102", 1,0));
            estudianteDAO.insertarEstudiante(new EstudianteDTO(idUsuarioEstudiante2, usuarioEstudiante2DTO.getNombre(), usuarioEstudiante2DTO.getApellido(), "S23014203", 1,0));
            estudianteDAO.insertarEstudiante(new EstudianteDTO(idUsuarioEstudiante3, usuarioEstudiante3DTO.getNombre(), usuarioEstudiante3DTO.getApellido(), "S23014304", 1,0));

            MATRICULAS_INSERTADAS.addAll(List.of("S23014102", "S23014203", "S23014304"));

            autoevaluacionDAO.crearNuevaAutoevaluacion(new AutoevaluacionDTO(1001, Timestamp.valueOf("2023-01-01 00:00:00"), "Xalapa", 9.5f, "S23014102", 1));
            autoevaluacionDAO.crearNuevaAutoevaluacion(new AutoevaluacionDTO(1002, Timestamp.valueOf("2023-02-02 00:00:00"), "Veracruz", 8.0f, "S23014203", 1));
            autoevaluacionDAO.crearNuevaAutoevaluacion(new AutoevaluacionDTO(1003, Timestamp.valueOf("2023-03-03 00:00:00"), "Orizaba", 7.5f, "S23014304", 1));

            IDS_AUTOEVALUACIONES_INSERTADAS.addAll(List.of(1001, 1002, 1003));

        } catch (SQLException | IOException e) {

            fail("Error en @BeforeEach al preparar datos: " + e.getMessage());
        }
    }

    @AfterEach
    void limpiarDatosDePrueba() {

        try {

            for (int idAutoevaluacion : IDS_AUTOEVALUACIONES_INSERTADAS) {
                PreparedStatement eliminarAutoevaluacion = conexionBaseDeDatos.prepareStatement(
                        "DELETE FROM autoevaluacion WHERE idAutoevaluacion = ?");
                eliminarAutoevaluacion.setInt(1, idAutoevaluacion);
                eliminarAutoevaluacion.executeUpdate();
                eliminarAutoevaluacion.close();
            }

            for (String matricula : MATRICULAS_INSERTADAS) {
                PreparedStatement eliminarEstudiante = conexionBaseDeDatos.prepareStatement(
                        "DELETE FROM estudiante WHERE matricula = ?");
                eliminarEstudiante.setString(1, matricula);
                eliminarEstudiante.executeUpdate();
                eliminarEstudiante.close();
            }

            for (int idUsuario : IDS_USUARIOS_INSERTADOS) {
                PreparedStatement eliminarUsuario = conexionBaseDeDatos.prepareStatement(
                        "DELETE FROM usuario WHERE idUsuario = ?");
                eliminarUsuario.setInt(1, idUsuario);
                eliminarUsuario.executeUpdate();
                eliminarUsuario.close();
            }

            conexionBaseDeDatos.close();

        } catch (SQLException e) {

            fail("Error en @AfterEach al limpiar datos: " + e.getMessage());
        }
    }

    @Test
    void testInsertarAutoevaluacionConDatosValidos() {

        try {

            UsuarioDTO usuarioDTO = new UsuarioDTO(0, "AcademicoTest",
                    "ApellidoTest", 1);

            int idUsuarioDTO = usuarioDAO.insertarUsuario(usuarioDTO);
            IDS_USUARIOS_INSERTADOS.add(idUsuarioDTO);

            estudianteDAO.insertarEstudiante(new EstudianteDTO(idUsuarioDTO,
                    usuarioDTO.getNombre(), usuarioDTO.getApellido(), "S23014405", 1,0));
            MATRICULAS_INSERTADAS.add("S23014405");

            AutoevaluacionDTO nuevaAutoevaluacion = new AutoevaluacionDTO(1004, Timestamp.valueOf("2023-05-06 00:00:00"),
                    "Acayucan", 6.4f,"S23014405", 1);
            boolean insercionExitosa = autoevaluacionDAO.crearNuevaAutoevaluacion(nuevaAutoevaluacion);

            assertTrue(insercionExitosa, "La autoevaluación debería ser insertado correctamente.");
            IDS_AUTOEVALUACIONES_INSERTADAS.add(1004);

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testInsertarAutoevaluacionConDatosInvalidos() {

        AutoevaluacionDTO autoevaluacionDTO = new AutoevaluacionDTO(1006, Timestamp.valueOf("2023-05-06 00:00:00"),
                "Coatzacoalcos", 6.4f, "S23014805", 1);

        assertThrows(SQLException.class, () -> autoevaluacionDAO.crearNuevaAutoevaluacion(autoevaluacionDTO),
                "Se esperaba una excepción debido a datos inválidos."
        );
    }

    @Test
    void testBuscarAutoevaluacionPorIDConDatosValidos() {

        AutoevaluacionDTO autoevaluacionEsperada = new AutoevaluacionDTO(1001, Timestamp.valueOf("2023-01-01 00:00:00"),
                "Xalapa", 9.5f,"S23014102", 1);

        try {

            AutoevaluacionDTO autoevaluacionEncontrada = autoevaluacionDAO.buscarAutoevaluacionPorID(autoevaluacionEsperada.getIDAutoevaluacion());
            assertEquals(autoevaluacionEsperada, autoevaluacionEncontrada, "La autoevaluacion debería ser encontrado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testBuscarAutoevaluacionPorIDConDatosInvalidos() {

        int idAutoevaluacionInvalida = 99999;

        try {

            AutoevaluacionDTO autoevaluacionEncontrada = autoevaluacionDAO.buscarAutoevaluacionPorID(idAutoevaluacionInvalida);
            assertEquals(-1, autoevaluacionEncontrada.getIDAutoevaluacion(), "No debería encontrarse una autoevaluación con ese número.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testEliminarAutoevaluacionPorIDConDatosValidos() {

        int idAutoevaluacionParaEliminar = 1002;

        try {

            boolean resutadoDeEliminarAutoevaluacion = autoevaluacionDAO.eliminarAutoevaluacionPorID(idAutoevaluacionParaEliminar);
            assertTrue(resutadoDeEliminarAutoevaluacion, "La autoevaluacion debería ser eliminada correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testEliminarAutoevaluacionPorIDConDatosInvalidos() {

        int idAutoevaluacionInexistente = 88888;

        try {

            boolean resultadoDeEliminarAutoevaluacion = autoevaluacionDAO.eliminarAutoevaluacionPorID(idAutoevaluacionInexistente);
            assertFalse(resultadoDeEliminarAutoevaluacion, "No debería eliminarse ninguna autoevaluacion inexistente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testModificarAutoevaluacionConDatosValidos() {

        try {

            AutoevaluacionDTO autoevaluacionActualizada = new AutoevaluacionDTO(
                    1003, Timestamp.valueOf("2023-01-15 00:00:00"), "NuevoLugar", 8.5f, "S23014102", 1);

            boolean resultadoDeModificarAutoevaluacion = autoevaluacionDAO.modificarAutoevaluacion(autoevaluacionActualizada);
            assertTrue(resultadoDeModificarAutoevaluacion, "La autoevaluación debería haberse modificado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testModificarAutoevaluacionConDatosInvalidos() throws SQLException, IOException {

        AutoevaluacionDTO autoevaluacionInvalida = new AutoevaluacionDTO(
                1003, Timestamp.valueOf("2023-01-15 00:00:00"), "NuevoLugar", 8.5f, "S23014054", 1);

        assertThrows(SQLException.class, () -> {
            autoevaluacionDAO.modificarAutoevaluacion(autoevaluacionInvalida);
        }, "Se esperaba una SQLException debido a la violación de la clave foránea.");
    }


    @Test
    void testModificarAutoevaluacionInexistente() {

        AutoevaluacionDTO autoevaluacionInexistente = new AutoevaluacionDTO(
                99999, Timestamp.valueOf("2023-01-01 00:00:00"), "LugarInexistente", 5.0f, "S23099999", 1);

        try {

            boolean resultadoModificacion = autoevaluacionDAO.modificarAutoevaluacion(autoevaluacionInexistente);
            assertFalse(resultadoModificacion, "No debería modificarse una autoevaluación inexistente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }
}
