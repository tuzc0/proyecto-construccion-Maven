import logica.DAOs.EstudianteDAO;
import logica.DTOs.EstudianteDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EstudianteDAOTest {

    private static EstudianteDAO estudianteDAO;

    @BeforeAll
    static void setUp() throws SQLException, IOException {

        estudianteDAO = new EstudianteDAO();
    }

    @Test
    @Order(1)
    void testInsertarEstudianteDatosValidos() throws SQLException, IOException {

        EstudianteDTO estudiante = new EstudianteDTO(2, "Juan", "Pérez", "A12346", 1);
        boolean resultado = estudianteDAO.insertarEstudiante(estudiante);
        assertTrue(resultado, "El estudiante debería haberse insertado correctamente.");
    }

    @Test
    @Order(2)
    void testEliminarEstudiantePorMatriculaDatosValidos() throws SQLException, IOException {

        boolean resultado = estudianteDAO.eliminarEstudiantePorMatricula(0, "A12345");
        assertTrue(resultado, "El estudiante debería haberse eliminado correctamente.");
    }

    @Test
    @Order(3)
    void testModificarEstudianteDatosValidos() throws SQLException, IOException {

        EstudianteDTO estudiante = new EstudianteDTO(2, "Juan", "Pérez", "A12345", 1);
        boolean resultado = estudianteDAO.modificarEstudiante(estudiante);
        assertTrue(resultado, "El estudiante debería haberse modificado correctamente.");
    }

    @Test
    @Order(4)
    void testBuscarEstudiantePorMatriculaDatosValidos() throws SQLException, IOException {

        EstudianteDTO estudiante = estudianteDAO.buscarEstudiantePorMatricula("A12345");
        assertEquals("A12345", estudiante.getMatricula(), "La matrícula del estudiante debería coincidir.");
    }

    @Test
    @Order(5)
    void testListarEstudiantesDatosValidos() throws SQLException, IOException {

        List<EstudianteDTO> estudiantes = estudianteDAO.listarEstudiantes();
        assertNotNull(estudiantes, "La lista de estudiantes no debería ser nula.");
    }
}