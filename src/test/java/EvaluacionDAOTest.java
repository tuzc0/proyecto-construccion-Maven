import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.*;
import logica.DTOs.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EvaluacionDAOTest {

    private EvaluacionDAO evaluacionDAO;
    private AcademicoEvaluadorDAO academicoEvaluadorDAO;
    private EstudianteDAO estudianteDAO;
    private UsuarioDAO usuarioDAO;
    private Connection conexion;

    private final List<Integer> idsEvaluacionesInsertadas = new ArrayList<>();
    private final List<Integer> numerosPersonalCreados      = new ArrayList<>();
    private final List<String>  matriculasEstudiantesCreadas = new ArrayList<>();
    private final List<Integer> idsUsuariosCreados           = new ArrayList<>();

    @BeforeEach
    void prepararDatosDePrueba() {
        evaluacionDAO             = new EvaluacionDAO();
        academicoEvaluadorDAO     = new AcademicoEvaluadorDAO();
        usuarioDAO                = new UsuarioDAO();

        try {
            estudianteDAO             = new EstudianteDAO();
            // 1) Conexión y limpieza de evaluaciones
            conexion = new ConexionBaseDeDatos().getConnection();
            conexion
                    .prepareStatement("DELETE FROM evaluacion WHERE idEvaluacion BETWEEN 2000 AND 3000")
                    .executeUpdate();

            // 2) Crear dependencia académica y del estudiante
            crearDependencias(12345, "A001");

            // 3) Insertar la evaluación de prueba
            int idEval = evaluacionDAO.crearNuevaEvaluacion(
                    new EvaluacionDTO(2999, "Primera evaluación", 9.5f, 12345, "A001", 1)
            );
            idsEvaluacionesInsertadas.add(idEval);

        } catch (SQLException | IOException e) {
            fail("Error al preparar datos de prueba: " + e.getMessage());
        }
    }

    /** Inserta usuario→académicoEvaluador y usuario→estudiante para las FK */
    private void crearDependencias(int numeroPersonal, String matricula) throws SQLException, IOException {
        // Académico evaluador
        UsuarioDTO usuarioAca = new UsuarioDTO(0, "Acad" + numeroPersonal, "Test", 1);
        int idUsuarioAca = usuarioDAO.insertarUsuario(usuarioAca);
        idsUsuariosCreados.add(idUsuarioAca);

        AcademicoEvaluadorDTO academico = new AcademicoEvaluadorDTO(
                numeroPersonal, idUsuarioAca, usuarioAca.getNombre(), usuarioAca.getApellido(), 1
        );
        academicoEvaluadorDAO.insertarAcademicoEvaluador(academico);
        numerosPersonalCreados.add(numeroPersonal);

        // Estudiante
        UsuarioDTO usuarioEst = new UsuarioDTO(0, "Est" + matricula, "Test", 1);
        int idUsuarioEst = usuarioDAO.insertarUsuario(usuarioEst);
        idsUsuariosCreados.add(idUsuarioEst);

        EstudianteDTO estudiante = new EstudianteDTO(
                idUsuarioEst, usuarioEst.getNombre(), usuarioEst.getApellido(),
                matricula, 1,0
        );
        estudianteDAO.insertarEstudiante(estudiante);
        matriculasEstudiantesCreadas.add(matricula);
    }

    @AfterEach
    void limpiarDatosDePrueba() {
        try {
            // Eliminar evaluaciones
            for (int id : idsEvaluacionesInsertadas) {
                conexion
                        .prepareStatement("DELETE FROM evaluacion WHERE idEvaluacion = " + id)
                        .executeUpdate();
            }
            // Eliminar estudiantes
            for (String mat : matriculasEstudiantesCreadas) {
                conexion
                        .prepareStatement("DELETE FROM estudiante WHERE matricula = '" + mat + "'")
                        .executeUpdate();
            }
            // Eliminar académicos evaluadores
            for (int num : numerosPersonalCreados) {
                conexion
                        .prepareStatement("DELETE FROM academicoevaluador WHERE numeroDePersonal = " + num)
                        .executeUpdate();
            }
            // Eliminar usuarios
            for (int idUser : idsUsuariosCreados) {
                conexion
                        .prepareStatement("DELETE FROM usuario WHERE idUsuario = " + idUser)
                        .executeUpdate();
            }
            conexion.close();
        } catch (SQLException e) {
            fail("Error al limpiar datos de prueba: " + e.getMessage());
        }
    }

    @Test
    void insertarEvaluacion_DeberiaRetornarIdPositivo() {
        try {
            // Preparo nuevas dependencias para A002
            crearDependencias(12346, "A002");

            EvaluacionDTO nuevaEval = new EvaluacionDTO(
                    3000, "Evaluación válida", 10.0f, 12346, "A002", 1
            );
            int idInsert = evaluacionDAO.crearNuevaEvaluacion(nuevaEval);
            boolean insercionExitosa = idInsert > 0;
            assertTrue(insercionExitosa, "La evaluación debería insertarse correctamente.");
            idsEvaluacionesInsertadas.add(idInsert);
        } catch (SQLException | IOException e) {
            fail("No se esperaba excepción al insertar evaluación válida: " + e.getMessage());
        }
    }

    @Test
    void buscarEvaluacionPorId_DeberiaEncontrarLaEvaluacion() {
        try {
            EvaluacionDTO esperada = new EvaluacionDTO(2999, "Primera evaluación", 9.5f, 12345, "A001", 1
            );
            EvaluacionDTO obtenida = evaluacionDAO.buscarEvaluacionPorID(esperada.getIDEvaluacion());
            assertEquals(esperada, obtenida, "La evaluación obtenida debería coincidir con la esperada.");
        } catch (SQLException | IOException e) {
            fail("No se esperaba excepción al buscar evaluación válida: " + e.getMessage());
        }
    }

    @Test
    void buscarEvaluacionPorId_Invalido_DeberiaRetornarIdNegativo() {
        try {
            EvaluacionDTO obtenida = evaluacionDAO.buscarEvaluacionPorID(6888);
            assertEquals(-1, obtenida.getIDEvaluacion(),
                    "No debería encontrarse una evaluación con ID inexistente.");
        } catch (SQLException | IOException e) {
            fail("No se esperaba excepción al buscar evaluación inválida: " + e.getMessage());
        }
    }

    @Test
    void modificarEvaluacion_ConDatosValidos_DeberiaRetornarTrue() {
        try {
            int id = idsEvaluacionesInsertadas.get(0);
            EvaluacionDTO modificada = new EvaluacionDTO(
                    id, "Evaluación actualizada", 8.0f, 12345, "A001", 1
            );
            boolean resultado = evaluacionDAO.modificarEvaluacion(modificada);
            assertTrue(resultado, "La evaluación debería ser modificada correctamente.");
        } catch (SQLException | IOException e) {
            fail("No se esperaba excepción al modificar evaluación válida: " + e.getMessage());
        }
    }

    @Test
    void modificarEvaluacion_IdInvalido_DeberiaRetornarFalse() {
        try {
            EvaluacionDTO invalida = new EvaluacionDTO(
                    6888, "No existe", 0.0f, 0, "X000", 0
            );
            boolean resultado = evaluacionDAO.modificarEvaluacion(invalida);
            assertFalse(resultado, "No debería modificarse una evaluación con ID inválido.");
        } catch (SQLException | IOException e) {
            fail("No se esperaba excepción al modificar evaluación inválida: " + e.getMessage());
        }
    }

    @Test
    void eliminarEvaluacionDefinitivamente_ConIdValido_DeberiaRetornarTrue() {
        try {
            int id = idsEvaluacionesInsertadas.get(0);
            boolean resultado = evaluacionDAO.eliminarEvaluacionDefinitivamente(id);
            assertTrue(resultado, "La evaluación debería ser eliminada correctamente.");
            idsEvaluacionesInsertadas.remove(Integer.valueOf(id));
        } catch (SQLException | IOException e) {
            fail("No se esperaba excepción al eliminar evaluación válida: " + e.getMessage());
        }
    }

    @Test
    void eliminarEvaluacionDefinitivamente_IdInvalido_DeberiaRetornarFalse() {
        try {
            boolean resultado = evaluacionDAO.eliminarEvaluacionDefinitivamente(6888);
            assertFalse(resultado, "No debería eliminarse una evaluación inexistente.");
        } catch (SQLException | IOException e) {
            fail("No se esperaba excepción al eliminar evaluación inválida: " + e.getMessage());
        }
    }
}