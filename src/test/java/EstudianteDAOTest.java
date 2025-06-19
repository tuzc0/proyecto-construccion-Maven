import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.*;
import logica.DTOs.*;
import logica.interfaces.IGrupoDAO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
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
    private GrupoDAO grupoDAO;
    private AcademicoDAO academicoDAO;
    private PeriodoDAO periodoDAO;
    private ProyectoDAO proyectoDAO;
    private OrganizacionVinculadaDAO organizacionDAO;
    private RepresentanteDAO representanteDAO;
    private Connection conexionBaseDeDatos;
    
    private final List<String> MATRICULAS_INSERTADAS = new ArrayList<>();
    private final List<Integer> IDS_USUARIOS_INSERTADOS = new ArrayList<>();
    private final List<Integer> IDS_GRUPOS_INSERTADOS = new ArrayList<>();
    private final List<Integer> IDS_PROYECTOS_INSERTADOS = new ArrayList<>();

    @BeforeEach
    void prepararDatosDePrueba() {

        estudianteDAO = new EstudianteDAO();
        usuarioDAO = new UsuarioDAO();
        grupoDAO = new GrupoDAO();
        academicoDAO = new AcademicoDAO();
        periodoDAO = new PeriodoDAO();
        proyectoDAO = new ProyectoDAO();
        organizacionDAO = new OrganizacionVinculadaDAO();
        representanteDAO = new RepresentanteDAO();

        MATRICULAS_INSERTADAS.clear();
        IDS_USUARIOS_INSERTADOS.clear();
        IDS_GRUPOS_INSERTADOS.clear();
        IDS_PROYECTOS_INSERTADOS.clear();

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();

            for (String matricula : List.of("S23014102", "S23014095", "S23014069", "S23014054")) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM estudiante WHERE matricula = '" + matricula + "'")
                        .executeUpdate();
            }

            for (int NRCGrupo : List.of(40776, 40789)) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM Grupo WHERE NRC = " + NRCGrupo)
                        .executeUpdate();
            }

            conexionBaseDeDatos.prepareStatement("DELETE FROM periodo").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM academico").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM usuario WHERE idUsuario BETWEEN 1000 AND 2000")
                    .executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM organizacionvinculada").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM representante").executeUpdate();

            int idUsuario = 0;
            int estadoActivo = 1;

            UsuarioDTO usuarioEstudiante1DTO = new UsuarioDTO(idUsuario, "Claudio", "Trujillo Zepeda",
                    estadoActivo
            );
            UsuarioDTO usuarioEstudiante2DTO = new UsuarioDTO(idUsuario, "Irene", "Paz Gonzalez",
                    estadoActivo
            );
            UsuarioDTO usuarioEstudiante3DTO = new UsuarioDTO(idUsuario, "Angel", "Aburto Ruiz",
                    estadoActivo
            );
            UsuarioDTO usuarioAcademicoDTO = new UsuarioDTO(idUsuario, "Arturo", "Villa",
                    estadoActivo
            );

            int idUsuarioEstudiante1 = usuarioDAO.insertarUsuario(usuarioEstudiante1DTO);
            int idUsuarioEstudiante2 = usuarioDAO.insertarUsuario(usuarioEstudiante2DTO);
            int idUsuarioEstudiante3 = usuarioDAO.insertarUsuario(usuarioEstudiante3DTO);
            int idUsuarioAcademico = usuarioDAO.insertarUsuario(usuarioAcademicoDTO);

            IDS_USUARIOS_INSERTADOS.addAll(List.of(idUsuarioEstudiante1, idUsuarioEstudiante2,
                    idUsuarioEstudiante3, idUsuarioAcademico)
            );

            academicoDAO.insertarAcademico(new AcademicoDTO(1001, idUsuarioAcademico,
                    usuarioAcademicoDTO.getNombre(), usuarioAcademicoDTO.getApellido(),
                    usuarioAcademicoDTO.getEstado())
            );

            periodoDAO.crearNuevoPeriodo(new PeriodoDTO(1, "Periodo 2024", 1,
                    Date.valueOf("2024-01-01"), Date.valueOf("2024-12-31"))
            );

            grupoDAO.crearNuevoGrupo(new GrupoDTO(40776, "Grupo 1", 1001, 1,
                    1)
            );
            grupoDAO.crearNuevoGrupo(new GrupoDTO(40789, "Grupo 2", 1001, 2,
                    0)
            );

            IDS_GRUPOS_INSERTADOS.addAll(List.of(40776, 40789));
            int idProyecto = 0;


            organizacionDAO.crearNuevaOrganizacion(new OrganizacionVinculadaDTO(1, "Empresa 1",
                    "Dirección 1", "empresa1@test.com", "5551111111", 1));

            representanteDAO.insertarRepresentante(
                    new RepresentanteDTO(1, "representate1@gmail.com", "5554444444",
                            "Representante 1", "Apellido 1", 1, 1)
            );

            estudianteDAO.insertarEstudiante(new EstudianteDTO(idUsuarioEstudiante1,
                    usuarioEstudiante1DTO.getNombre(), usuarioEstudiante1DTO.getApellido(), "S23014102",
                    usuarioEstudiante1DTO.getEstado(), idProyecto));

            estudianteDAO.insertarEstudiante(new EstudianteDTO(idUsuarioEstudiante2,
                    usuarioEstudiante2DTO.getNombre(), usuarioEstudiante2DTO.getApellido(), "S23014095",
                    usuarioEstudiante2DTO.getEstado(), idProyecto));

            estudianteDAO.insertarEstudiante(new EstudianteDTO(idUsuarioEstudiante3,
                    usuarioEstudiante3DTO.getNombre(), usuarioEstudiante3DTO.getApellido(), "S23014069",
                    usuarioEstudiante3DTO.getEstado(), idProyecto));

            MATRICULAS_INSERTADAS.addAll(List.of("S23014102", "S23014095", "S23014069"));

        } catch (SQLException | IOException e) {

            fail("Error en @BeforeEach al preparar datos: " + e);
        }
    }

    @AfterEach
    void limpiarDatosDePrueba() {

        try {

            for (String matricula : MATRICULAS_INSERTADAS) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM estudiante WHERE matricula = " + matricula)
                        .executeUpdate();
            }

            for (int idUsuario : IDS_USUARIOS_INSERTADOS) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM usuario WHERE idUsuario = " + idUsuario)
                        .executeUpdate();
            }

            conexionBaseDeDatos.close();

        } catch (SQLException e) {

            fail("Error en @AfterEach al limpiar datos: " + e);
        }
    }

    @Test
    void testInsertarEstudianteConDatosValidos() {

        int idProyecto = 0;

        try {

            UsuarioDTO usuarioDTO = new UsuarioDTO(0, "Manuel", "Valdivia Garcia", 1);
            int idUsuario = usuarioDAO.insertarUsuario(usuarioDTO);
            IDS_USUARIOS_INSERTADOS.add(idUsuario);

            EstudianteDTO estudianteDTO = new EstudianteDTO(idUsuario, usuarioDTO.getNombre(), usuarioDTO.getApellido(),
                    "S23014054", usuarioDTO.getEstado(),idProyecto);
            boolean resultadoAlInsertar = estudianteDAO.insertarEstudiante(estudianteDTO);

            assertTrue(resultadoAlInsertar, "El estudiante debería ser insertado correctamente.");
            MATRICULAS_INSERTADAS.add("S23014054");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e);
        }
    }

    @Test
    void testInsertarEstudianteConDatosInvalidos() {

        int idUsuario = -1;
        int idProyecto = 0;
        int estadoActivo = 1;

        EstudianteDTO estudianteDTO = new EstudianteDTO(idUsuario, null, null, null,
                estadoActivo, idProyecto);

        assertThrows(SQLException.class, () -> estudianteDAO.insertarEstudiante(estudianteDTO),
                "Se esperaba una excepción debido a datos inválidos.");
    }

    @Test
    void testBuscarEstudiantePorMatriculaConDatosValidos() {

        EstudianteDTO estudianteEsperado = new EstudianteDTO(IDS_USUARIOS_INSERTADOS.get(0),
                "Claudio", "Trujillo Zepeda", "S23014102", 1, 0);

        try {

            EstudianteDTO estudianteEncontrado = estudianteDAO.buscarEstudiantePorMatricula("S23014102");
            assertEquals(estudianteEsperado, estudianteEncontrado,
                    "El estudiante encontrado debería coincidir con el esperado.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e);
        }
    }

    @Test
    void testBuscarEstudiantePorMatriculaConDatosInvalidos() {

        EstudianteDTO estudianteEsperado = new EstudianteDTO(-1, "N/A", "N/A", "N/A", 0,0);

        try {

            EstudianteDTO estudianteEncontrado = estudianteDAO.buscarEstudiantePorMatricula("S23014037");

            assertEquals(estudianteEsperado, estudianteEncontrado,
                    "El estudiante encontrado debería ser el estudiante por defecto.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e);
        }
    }

    @Test
    void testModificarEstudianteConDatosValidos() {

        EstudianteDTO estudianteModificadoEsperado = new EstudianteDTO(IDS_USUARIOS_INSERTADOS.get(1),
                "Gillermo", "Velazquez Rosiles", "S23014102", 1,0);

        try {

            boolean resultadoAlModificar = estudianteDAO.modificarEstudiante(estudianteModificadoEsperado);
            assertTrue(resultadoAlModificar, "El estudiante debería ser modificado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e);
        }
    }

    @Test
    void testModificarEstudianteConDatosInvalidos() {

        EstudianteDTO estudianteInvalido = new EstudianteDTO(-1, null, null, null,
                1,1);

        try {

            boolean resultadoModificacion = estudianteDAO.modificarEstudiante(estudianteInvalido);
            assertFalse(resultadoModificacion, "Se esperaba que la modificación fallara debido a datos inválidos.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e);
        }


    }

    @Test
    void testModificarEstuidanteInexistente() {

        try {

            EstudianteDTO estudianteInexistente = new EstudianteDTO(99999, "X", "Y", "Inexistente", 1,0);
            boolean resultadoAlModificar = estudianteDAO.modificarEstudiante(estudianteInexistente);
            assertFalse(resultadoAlModificar, "No debería modificarse un estudiante inexistente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e.getMessage());
        }
    }

    @Test
    void testEliminarEstudiantePorMatriculaConDatosValidos() {

        int estadoActivo = 0;
        String matriculaAEliminar = "S23014095";

        try {

            boolean resultadoAlEliminar = estudianteDAO.eliminarEstudiantePorMatricula(estadoActivo, matriculaAEliminar);
            assertTrue(resultadoAlEliminar, "El estudiante debería ser eliminado correctamente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e);
        }
    }

    @Test
    void testEliminarEstudiantePorMatriculaConDatosInvalidos() {

        int estadoActivo = 0;
        String matriculaAEliminar = "SSSSSSSS";

        try {

            boolean resultadoAlEliminar = estudianteDAO.eliminarEstudiantePorMatricula(estadoActivo, matriculaAEliminar);
            assertFalse(resultadoAlEliminar, "No debería eliminarse un estudiante inexistente.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e);
        }
    }

    @Test
    void testListarEstudiantesConDatos() {

        List<EstudianteDTO> listaEsperada = List.of(
                new EstudianteDTO(IDS_USUARIOS_INSERTADOS.get(0), "Claudio", "Trujillo Zepeda",
                        "S23014102", 1, 0),
                new EstudianteDTO(IDS_USUARIOS_INSERTADOS.get(1), "Irene", "Paz Gonzalez",
                        "S23014095", 1, 0),
                new EstudianteDTO(IDS_USUARIOS_INSERTADOS.get(2), "Angel", "Aburto Ruiz",
                        "S23014069", 1, 0)
        );

        try {

            List<EstudianteDTO> listaRecuperada = estudianteDAO.listarEstudiantes();

            assertEquals(listaEsperada.size(), listaRecuperada.size(),
                    "La cantidad de estudiantes recuperados debería ser igual a la esperada.");

            for (int estudianteRecuperado = 0; estudianteRecuperado < listaEsperada.size(); estudianteRecuperado++) {

                assertTrue(listaEsperada.get(estudianteRecuperado).equals(listaRecuperada.get(estudianteRecuperado)),
                        "El estudiante esperado debería ser igual al estudiante recuperado.");
            }

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void testListarEstudiantesSinDatos() {

        int resultadoEsperado = 0;
        int estadoActivo = 0;

        try {

            for (String matricula : MATRICULAS_INSERTADAS) {

                estudianteDAO.eliminarEstudiantePorMatricula(estadoActivo, matricula);
            }

            MATRICULAS_INSERTADAS.clear();

            List<EstudianteDTO> estudiantes = estudianteDAO.listarEstudiantes();
            assertEquals(resultadoEsperado, estudiantes.size(),
                    "La lista de estudiantes debería estar vacía.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba excepción: " + e);
        }
    }
}