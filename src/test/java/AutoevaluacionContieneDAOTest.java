import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.*;
import logica.DTOs.*;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AutoevaluacionContieneDAOTest {

    private AutoevaluacionContieneDAO autoevaluacionContieneDAO;
    private AutoevaluacionDAO autoevaluacionDAO;
    private EstudianteDAO estudianteDAO;
    private UsuarioDAO usuarioDAO;
    private CriterioAutoevaluacionDAO criterioAutoevaluacionDAO;
    private Connection conexionBaseDeDatos;

    private final List<Integer> IDS_AUTOEVALUACIONES_CREADAS = new ArrayList<>();
    private final List<AutoEvaluacionContieneDTO> REGISTROS_AUTOEVALUACIONES_CONTIENE = new ArrayList<>();
    private final List<String> MATRICULAS_ESTUDIANTES_INSERTADAS = new ArrayList<>();
    private final List<Integer> IDS_USUARIOS_INSERTADOS = new ArrayList<>();

    @BeforeAll
    void prepararDatosDePrueba() {
        try {
            autoevaluacionContieneDAO = new AutoevaluacionContieneDAO();
            autoevaluacionDAO = new AutoevaluacionDAO();
            estudianteDAO = new EstudianteDAO();
            usuarioDAO = new UsuarioDAO();
            criterioAutoevaluacionDAO = new CriterioAutoevaluacionDAO();

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();

            conexionBaseDeDatos.prepareStatement("DELETE FROM autoevaluacioncontiene").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM autoevaluacion WHERE idAutoevaluacion BETWEEN 1000 AND 2000").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM estudiante WHERE matricula LIKE 'S230%'").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM usuario WHERE idUsuario BETWEEN 1000 AND 2000").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM criterioautoevaluacion WHERE IDCriterio BETWEEN 1000 AND 2000").executeUpdate();

            UsuarioDTO usuarioEstudianteDTO1 = new UsuarioDTO(0, "Nombre1", "Apellido1", 1);
            UsuarioDTO usuarioEstudianteDTO2 = new UsuarioDTO(0, "Nombre2", "Apellido2", 1);

            int idUsuarioEstudiante1 = usuarioDAO.insertarUsuario(usuarioEstudianteDTO1);
            int idUsuarioEstudiante2 = usuarioDAO.insertarUsuario(usuarioEstudianteDTO2);

            IDS_USUARIOS_INSERTADOS.addAll(List.of(idUsuarioEstudiante1, idUsuarioEstudiante2));

            estudianteDAO.insertarEstudiante(new EstudianteDTO(idUsuarioEstudiante1, "Nombre1", "Apellido1", "S23014102", 1));
            estudianteDAO.insertarEstudiante(new EstudianteDTO(idUsuarioEstudiante2, "Nombre2", "Apellido2", "S23014203", 1));

            MATRICULAS_ESTUDIANTES_INSERTADAS.addAll(List.of("S23014102", "S23014203"));

            autoevaluacionDAO.crearNuevaAutoevaluacion(new AutoevaluacionDTO(1001, Timestamp.valueOf("2023-01-01 00:00:00"), "Xalapa", 9.5f, "S23014102", 1));
            autoevaluacionDAO.crearNuevaAutoevaluacion(new AutoevaluacionDTO(1002, Timestamp.valueOf("2023-02-02 00:00:00"), "Veracruz", 8.0f, "S23014203", 1));

            IDS_AUTOEVALUACIONES_CREADAS.addAll(List.of(1001, 1002));

            criterioAutoevaluacionDAO.crearNuevoCriterioAutoevaluacion(new CriterioAutoevaluacionDTO(1001, "Criterio para insertar", 1, 1));
            criterioAutoevaluacionDAO.crearNuevoCriterioAutoevaluacion(new CriterioAutoevaluacionDTO(1002, "Criterio para modificar", 1, 1));

            AutoEvaluacionContieneDTO registroInicial = new AutoEvaluacionContieneDTO(1001, 4.0f, 1002);
            autoevaluacionContieneDAO.insertarAutoevaluacionContiene(registroInicial);
            REGISTROS_AUTOEVALUACIONES_CONTIENE.add(registroInicial);

        } catch (Exception e) {

            fail("Error en preparación de datos: " + e.getMessage());
        }
    }

    @AfterAll
    void limpiarDatosDePrueba() {

        try {

            for (AutoEvaluacionContieneDTO registroAutoEvaluacionContieneDTO : REGISTROS_AUTOEVALUACIONES_CONTIENE) {

                PreparedStatement eliminarAutoevaluacionContiene = conexionBaseDeDatos.prepareStatement(
                        "DELETE FROM autoevaluacioncontiene WHERE idAutoevaluacion = ? AND IDCriterio = ?");

                eliminarAutoevaluacionContiene.setInt(1, registroAutoEvaluacionContieneDTO.getIdAutoevaluacion());
                eliminarAutoevaluacionContiene.setInt(2, registroAutoEvaluacionContieneDTO.getIdCriterio());
                eliminarAutoevaluacionContiene.executeUpdate();
                eliminarAutoevaluacionContiene.close();
            }

            for (int idAutoevaluacion : IDS_AUTOEVALUACIONES_CREADAS) {

                PreparedStatement eliminarAutoevaluacion = conexionBaseDeDatos.prepareStatement(
                        "DELETE FROM autoevaluacion WHERE idAutoevaluacion = ?");

                eliminarAutoevaluacion.setInt(1, idAutoevaluacion);
                eliminarAutoevaluacion.executeUpdate();
                eliminarAutoevaluacion.close();
            }

            for (String matricula : MATRICULAS_ESTUDIANTES_INSERTADAS) {

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

            PreparedStatement eliminarCriterios = conexionBaseDeDatos.prepareStatement(
                    "DELETE FROM criterioautoevaluacion WHERE IDCriterio IN (1001, 1002)");
            eliminarCriterios.executeUpdate();
            eliminarCriterios.close();

            conexionBaseDeDatos.close();

        } catch (SQLException e) {

            fail("Error al limpiar datos: " + e.getMessage());
        }
    }


    @Test
    void testInsertarAutoevaluacionContieneConDatosValidos() {

        try {

            AutoEvaluacionContieneDTO autoEvaluacionContieneDTO = new AutoEvaluacionContieneDTO(1001, 4.5f, 1001);
            boolean fueInsertado = autoevaluacionContieneDAO.insertarAutoevaluacionContiene(autoEvaluacionContieneDTO);
            REGISTROS_AUTOEVALUACIONES_CONTIENE.add(autoEvaluacionContieneDTO);
            assertTrue(fueInsertado);

        } catch (Exception e) {

            fail("Excepción inesperada: " + e.getMessage());
        }
    }

    @Test
    void testInsertarAutoevaluacionContieneConDatosInvalidos() {

        AutoEvaluacionContieneDTO registroAutoEvaluacionContieneInvalido = new AutoEvaluacionContieneDTO(-1, -1.0f, -1);

        assertThrows(SQLException.class, () -> {
            autoevaluacionContieneDAO.insertarAutoevaluacionContiene(registroAutoEvaluacionContieneInvalido);
        });
    }


    @Test
    void testBuscarAutoevaluacionContieneConDatosValidos() {

        try {

            AutoEvaluacionContieneDTO resultadoBusqueda = autoevaluacionContieneDAO.buscarAutoevaluacionContienePorID(1001, 1002);
            assertNotNull(resultadoBusqueda);

        } catch (Exception e) {

            fail("Error al buscar registro existente: " + e.getMessage());
        }
    }

    @Test
    void testBuscarAutoevaluacionContieneConDatosInvalidos() {

        try {

            AutoEvaluacionContieneDTO resultadoBusqueda = autoevaluacionContieneDAO.buscarAutoevaluacionContienePorID(-1, -1);
            assertEquals(-1, resultadoBusqueda.getIdAutoevaluacion());

        } catch (Exception e) {

            fail("Error inesperado: " + e.getMessage());
        }
    }

    @Test
    void testModificarAutoevaluacionContieneConDatosValidos() {

        try {

            AutoEvaluacionContieneDTO registroAutoEvaluacionContieneDTO = new AutoEvaluacionContieneDTO(1001, 5.0f, 1002);
            boolean fueModificado = autoevaluacionContieneDAO.modificarCalificacion(registroAutoEvaluacionContieneDTO);
            assertTrue(fueModificado);

        } catch (Exception e) {

            fail("Error inesperado: " + e.getMessage());
        }
    }

    @Test
    void testModificarAutoevaluacionContieneConDatosInvalidos() {

        try {

            AutoEvaluacionContieneDTO registroAutoEvaluacionContieneDTOInvalido = new AutoEvaluacionContieneDTO(-1, -1.0f, -1);
            boolean fueModificado = autoevaluacionContieneDAO.modificarCalificacion(registroAutoEvaluacionContieneDTOInvalido);
            assertFalse(fueModificado);

        } catch (Exception e) {

            fail("Error inesperado: " + e.getMessage());
        }
    }

    @Test
    void testModificarAutoevaluacionContieneInexistente() {

        AutoEvaluacionContieneDTO autoevaluacionContieneInexistente = new AutoEvaluacionContieneDTO(
                99999, 5.0f, 99999);

        try {

            boolean resultadoModificacion = autoevaluacionContieneDAO.modificarCalificacion(autoevaluacionContieneInexistente);
            assertFalse(resultadoModificacion, "No debería modificarse un registro inexistente en autoevaluacioncontiene.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }
}
