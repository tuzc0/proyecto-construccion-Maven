import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.EstudianteDAO;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.EstudianteDTO;
import logica.DTOs.UsuarioDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EstudianteDAOTest {

    private EstudianteDAO estudianteDAO;
    private UsuarioDAO usuarioDAO;
    private Connection conexionBaseDeDatos;
    
    private final List<String> MATRICULAS_INSERTADAS = new ArrayList<>();
    private final List<Integer> IDS_USUARIOS_INSERTADOS = new ArrayList<>();

    @BeforeAll
    void prepararDatosDePrueba() {

        try {

            estudianteDAO = new EstudianteDAO();
            usuarioDAO = new UsuarioDAO();
            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();

            for (String matricula : MATRICULAS_INSERTADAS) {
                conexionBaseDeDatos.prepareStatement("DELETE FROM estudiante WHERE matricula = '" + matricula + "'").executeUpdate();
            }

            MATRICULAS_INSERTADAS.clear();
            IDS_USUARIOS_INSERTADOS.clear();

            conexionBaseDeDatos.prepareStatement("DELETE FROM estudiante").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM usuario WHERE idUsuario BETWEEN 1000 AND 2000").executeUpdate();

        } catch (SQLException | IOException e) {

            fail("Error al preparar los datos de prueba: " + e.getMessage());
        }
    }

    @AfterAll
    void limpiarDatosDePrueba() {

        try {

            for (String matricula : MATRICULAS_INSERTADAS) {
                conexionBaseDeDatos.prepareStatement("DELETE FROM estudiante WHERE matricula = '" + matricula + "'").executeUpdate();
            }

            for (int idUsuario : IDS_USUARIOS_INSERTADOS) {
                conexionBaseDeDatos.prepareStatement("DELETE FROM usuario WHERE idUsuario = " + idUsuario).executeUpdate();
            }

            conexionBaseDeDatos.close();

        } catch (SQLException e) {

            fail("Error al limpiar los datos de prueba: " + e.getMessage());
        }
    }

    @Test
    void testInsertarEstudianteConDatosValidos() {

        try {

            UsuarioDTO usuarioDTO = new UsuarioDTO(0, "Juan", "Pérez", 1);
            int idUsuario = usuarioDAO.insertarUsuario(usuarioDTO);
            IDS_USUARIOS_INSERTADOS.add(idUsuario);

            EstudianteDTO estudianteDTO = new EstudianteDTO(idUsuario, "Juan", "Pérez", "A12345", 1);
            boolean resultadoAlInsertar = estudianteDAO.insertarEstudiante(estudianteDTO);

            assertTrue(resultadoAlInsertar, "El estudiante debería ser insertado correctamente.");
            MATRICULAS_INSERTADAS.add("A12345");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e.getMessage());
        }
    }

    @Test
    void testInsertarEstudianteConDatosInvalidos() {

        EstudianteDTO estudianteInvalido = new EstudianteDTO(-1, null, null, null, 1);

        assertThrows(SQLException.class, () -> estudianteDAO.insertarEstudiante(estudianteInvalido),
                "Se esperaba una excepción debido a datos inválidos.");
    }

    @Test
    void testBuscarEstudiantePorMatriculaConDatosValidos() {

        try {

            UsuarioDTO usuarioDTO = new UsuarioDTO(0, "Laura", "Gómez", 1);
            int idUsuario = usuarioDAO.insertarUsuario(usuarioDTO);
            IDS_USUARIOS_INSERTADOS.add(idUsuario);

            EstudianteDTO estudianteABuscar = new EstudianteDTO(idUsuario, "Laura", "Gómez", "A12346", 1);
            estudianteDAO.insertarEstudiante(estudianteABuscar);
            MATRICULAS_INSERTADAS.add("A12346");

            EstudianteDTO estudianteEncontrado = estudianteDAO.buscarEstudiantePorMatricula("A12346");
            assertEquals(estudianteABuscar, estudianteEncontrado, "El estudiante encontrado debería coincidir con el esperado.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e.getMessage());
        }
    }

    @Test
    void testBuscarEstudiantePorMatriculaConDatosInvalidos() {

        try {

            EstudianteDTO estudianteABuscar = new EstudianteDTO(-1, "N/A", "N/A", "N/A", 0);
            EstudianteDTO estudianteEncontrado = estudianteDAO.buscarEstudiantePorMatricula("Inexistente");

            assertEquals(estudianteABuscar, estudianteEncontrado,
                    "El estudiante encontrado debería ser el estudiante por defecto.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e.getMessage());
        }
    }

    @Test
    void testModificarEstudianteConDatosValidos() {

        try {

            UsuarioDTO usuarioDTO = new UsuarioDTO(0, "Carlos", "Martínez", 1);
            int idUsuario = usuarioDAO.insertarUsuario(usuarioDTO);
            IDS_USUARIOS_INSERTADOS.add(idUsuario);

            EstudianteDTO estudianteDTO = new EstudianteDTO(idUsuario, "Carlos", "Martínez", "A12347", 1);
            estudianteDAO.insertarEstudiante(estudianteDTO);
            MATRICULAS_INSERTADAS.add("A12347");

            EstudianteDTO estudianteModificado = new EstudianteDTO(idUsuario, "Carlos", "Martínez López", "A12347", 1);
            boolean resultadoAlModificar = estudianteDAO.modificarEstudiante(estudianteModificado);
            assertTrue(resultadoAlModificar, "El estudiante debería ser modificado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e.getMessage());
        }
    }

    @Test
    void testModificarEstudianteConDatosInvalidos() {

        try {

            EstudianteDTO estudianteInvalido = new EstudianteDTO(-1, null, null, null, 1);
            boolean resultadoModificacion = estudianteDAO.modificarEstudiante(estudianteInvalido);
            assertFalse(resultadoModificacion, "Se esperaba que la modificación fallara debido a datos inválidos.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e.getMessage());
        }


    }

    @Test
    void testModificarEstuidanteInexistente() {

        try {

            EstudianteDTO estudianteInexistente = new EstudianteDTO(99999, "X", "Y", "Inexistente", 1);
            boolean resultadoAlModificar = estudianteDAO.modificarEstudiante(estudianteInexistente);
            assertFalse(resultadoAlModificar, "No debería modificarse un estudiante inexistente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e.getMessage());
        }
    }

    @Test
    void testEliminarEstudiantePorMatriculaConDatosValidos() {

        try {

            UsuarioDTO usuarioDTO = new UsuarioDTO(0, "Ana", "López", 1);
            int idUsuario = usuarioDAO.insertarUsuario(usuarioDTO);
            IDS_USUARIOS_INSERTADOS.add(idUsuario);

            EstudianteDTO estudianteDTO = new EstudianteDTO(idUsuario, "Ana", "López", "A12348", 1);
            estudianteDAO.insertarEstudiante(estudianteDTO);
            MATRICULAS_INSERTADAS.add("A12348");

            boolean resultadoAlEliminar = estudianteDAO.eliminarEstudiantePorMatricula(0, "A12348");
            assertTrue(resultadoAlEliminar, "El estudiante debería ser eliminado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e.getMessage());
        }
    }

    @Test
    void testEliminarEstudiantePorMatriculaConDatosInvalidos() {

        try {

            boolean resultadoAlEliminar = estudianteDAO.eliminarEstudiantePorMatricula(0, "Inexistente");
            assertFalse(resultadoAlEliminar, "No debería eliminarse un estudiante inexistente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e.getMessage());
        }
    }

    @Test
    void testListarEstudiantesConDatos() {

        try {

            UsuarioDTO usuarioEstudiante1 = new UsuarioDTO(0, "Juan", "Pérez", 1);
            int idJuan = usuarioDAO.insertarUsuario(usuarioEstudiante1);
            IDS_USUARIOS_INSERTADOS.add(idJuan);

            EstudianteDTO estudianteDTO1 = new EstudianteDTO(idJuan, "Juan", "Pérez", "B10001", 1);
            estudianteDAO.insertarEstudiante(estudianteDTO1);
            MATRICULAS_INSERTADAS.add("B10001");

            UsuarioDTO usuarioEstudiante2 = new UsuarioDTO(0, "Laura", "Gómez", 1);
            int idLaura = usuarioDAO.insertarUsuario(usuarioEstudiante2);
            IDS_USUARIOS_INSERTADOS.add(idLaura);

            EstudianteDTO estudianteDTO2 = new EstudianteDTO(idLaura, "Laura", "Gómez", "B10002", 1);
            estudianteDAO.insertarEstudiante(estudianteDTO2);
            MATRICULAS_INSERTADAS.add("B10002");

            List<EstudianteDTO> listaEsperada = List.of(estudianteDTO1, estudianteDTO2);

            List<EstudianteDTO> listaObtenida = estudianteDAO.listarEstudiantes();
            assertTrue(listaObtenida.containsAll(listaEsperada),
                    "La lista de estudiantes obtenida debería contener los datos esperados.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e.getMessage());
        }
    }



    @Test
    void testListarEstudiantesSinDatos() {

        try {

            for (String matricula : MATRICULAS_INSERTADAS) {

                estudianteDAO.eliminarEstudiantePorMatricula(0, matricula);
            }

            MATRICULAS_INSERTADAS.clear();

            List<EstudianteDTO> estudiantes = estudianteDAO.listarEstudiantes();
            assertEquals(0, estudiantes.size(), "La lista de estudiantes debería estar vacía.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e.getMessage());
        }
    }
}