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

    private PeriodoDAO periodoDAO;
    private UsuarioDAO usuarioDAO;
    private AcademicoDAO academicoDAO;
    private GrupoDAO grupoDAO;
    private OrganizacionVinculadaDAO organizacionDAO;
    private RepresentanteDAO representanteDAO;
    private ProyectoDAO proyectoDAO;
    private EstudianteDAO estudianteDAO;
    private AcademicoEvaluadorDAO academicoEvaluadorDAO;
    private EvaluacionDAO evaluacionDAO;
    private Connection conexionBaseDeDatos;

    private final List<Integer> IDS_USUARIOS_INSERTADOS = new ArrayList<>();
    private final List<Integer> IDS_GRUPOS_INSERTADOS = new ArrayList<>();
    private final List<Integer> IDS_PROYECTOS_INSERTADOS = new ArrayList<>();
    private final List<String> MATRICULAS_INSERTADAS = new ArrayList<>();

    @BeforeEach
    void prepararDatosDePrueba() {

        periodoDAO = new PeriodoDAO();
        usuarioDAO = new UsuarioDAO();
        academicoDAO = new AcademicoDAO();
        grupoDAO = new GrupoDAO();
        organizacionDAO = new OrganizacionVinculadaDAO();
        representanteDAO = new RepresentanteDAO();
        proyectoDAO = new ProyectoDAO();
        estudianteDAO = new EstudianteDAO();
        academicoEvaluadorDAO = new AcademicoEvaluadorDAO();
        evaluacionDAO = new EvaluacionDAO();

        MATRICULAS_INSERTADAS.clear();
        IDS_USUARIOS_INSERTADOS.clear();
        IDS_GRUPOS_INSERTADOS.clear();
        IDS_PROYECTOS_INSERTADOS.clear();

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();

            conexionBaseDeDatos.prepareStatement("DELETE FROM evaluacion").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM estudiante").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM grupo").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM proyecto").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM academicoevaluador").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM academico").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM representante").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM organizacionvinculada").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM periodo").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM usuario").executeUpdate();

            periodoDAO.crearNuevoPeriodo(new PeriodoDTO(1, "Periodo 2025", 1,
                    Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31")));

            UsuarioDTO usuarioAcademicoDTO = new UsuarioDTO(0, "Nombre", "Prueba", 1);
            UsuarioDTO usuarioEstudiante1DTO = new UsuarioDTO(0, "Usuario2", "Apellido2", 1);
            UsuarioDTO usuarioEstudiante2DTO = new UsuarioDTO(0, "Usuario3", "Apellido3", 1);
            UsuarioDTO usuarioEstudiante3DTO = new UsuarioDTO(0, "Usuario4", "Apellido4", 1);
            UsuarioDTO usuarioAcademcioEvaluadorDTO = new UsuarioDTO(0, "Usuario Evaluador",
                    "Apellido Evaluador", 1);

            int idUsuarioAcademico = usuarioDAO.insertarUsuario(usuarioAcademicoDTO);
            int idUsuarioEstudiante1 = usuarioDAO.insertarUsuario(usuarioEstudiante1DTO);
            int idUsuarioEstudiante2 = usuarioDAO.insertarUsuario(usuarioEstudiante2DTO);
            int idUsuarioEstudiante3 = usuarioDAO.insertarUsuario(usuarioEstudiante3DTO);
            int idUsuarioAcademicoEvaluador = usuarioDAO.insertarUsuario(usuarioAcademcioEvaluadorDTO);

            IDS_USUARIOS_INSERTADOS.add(idUsuarioAcademico);
            IDS_USUARIOS_INSERTADOS.add(idUsuarioEstudiante1);
            IDS_USUARIOS_INSERTADOS.add(idUsuarioEstudiante2);
            IDS_USUARIOS_INSERTADOS.add(idUsuarioEstudiante3);
            IDS_USUARIOS_INSERTADOS.add(idUsuarioAcademicoEvaluador);

            academicoDAO.insertarAcademico(new AcademicoDTO(1001, idUsuarioAcademico,
                    usuarioAcademicoDTO.getNombre(), usuarioAcademicoDTO.getApellido(),
                    usuarioAcademicoDTO.getEstado()));

            academicoEvaluadorDAO.insertarAcademicoEvaluador(new AcademicoEvaluadorDTO(1002, idUsuarioAcademicoEvaluador,
                    usuarioAcademcioEvaluadorDTO.getNombre(), usuarioAcademcioEvaluadorDTO.getApellido(),
                    usuarioAcademcioEvaluadorDTO.getEstado()));

            grupoDAO.crearNuevoGrupo(new GrupoDTO(40776, "Grupo 1", 1001, 1, 1));
            grupoDAO.crearNuevoGrupo(new GrupoDTO(40789, "Grupo 2", 1001, 1, 1));

            IDS_GRUPOS_INSERTADOS.add(40776);
            IDS_GRUPOS_INSERTADOS.add(40789);

            int idOrganizacion = organizacionDAO.crearNuevaOrganizacion(
                    new OrganizacionVinculadaDTO(1, "Empresa 1", "Dirección 1",
                            "empresa1@test.com", "5551111111", 1)
            );

            representanteDAO.insertarRepresentante(
                    new RepresentanteDTO(1, "representante1@gmail.com", "5554444444",
                            "Representante 1", "Apellido 1", idOrganizacion, 1)
            );

            int idProyecto1 = proyectoDAO.crearNuevoProyecto(
                    new ProyectoDTO(
                            0,
                            "Proyecto Innovador",
                            "Desarrollar una solución tecnológica avanzada",
                            "Implementar funcionalidades clave",
                            "Mejorar la experiencia del usuario",
                            "Metodología ágil",
                            "Recursos tecnológicos y humanos",
                            "Desarrollo, pruebas y despliegue",
                            "Responsabilidad del equipo de desarrollo",
                            "6 meses",
                            1,
                            1,
                            "Proyecto para optimizar procesos internos",
                            50,
                            200,
                            5
                    )
            );

            int idProyecto2 = proyectoDAO.crearNuevoProyecto(
                    new ProyectoDTO(
                            0,
                            "Proyecto Educativo",
                            "Fomentar el aprendizaje interactivo en estudiantes",
                            "Desarrollar una plataforma educativa",
                            "Promover el uso de tecnología en la educación",
                            "Metodología basada en diseño centrado en el usuario",
                            "Recursos digitales y soporte técnico",
                            "Análisis, diseño, desarrollo y evaluación",
                            "Responsabilidad del equipo de diseño y desarrollo",
                            "12 meses",
                            1,
                            1,
                            "Proyecto para mejorar la calidad educativa mediante tecnología",
                            100,
                            500,
                            0
                    )
            );

            IDS_PROYECTOS_INSERTADOS.add(idProyecto1);
            IDS_PROYECTOS_INSERTADOS.add(idProyecto2);

            EstudianteDTO estudiante1DTO = new EstudianteDTO(idUsuarioEstudiante1,
                    usuarioEstudiante1DTO.getNombre(), usuarioEstudiante1DTO.getApellido(),
                    "S23014102", usuarioEstudiante1DTO.getEstado(), idProyecto1, 40776, 10);

            EstudianteDTO estudiante2DTO = new EstudianteDTO(idUsuarioEstudiante2,
                    usuarioEstudiante2DTO.getNombre(), usuarioEstudiante2DTO.getApellido(),
                    "S23014095", usuarioEstudiante2DTO.getEstado(), idProyecto2, 40789, 7.5F);

            estudianteDAO.insertarEstudiante(estudiante1DTO);
            estudianteDAO.insertarEstudiante(estudiante2DTO);

            MATRICULAS_INSERTADAS.add(estudiante1DTO.getMatricula());

            evaluacionDAO.crearNuevaEvaluacion( new EvaluacionDTO(0, "Buen desempeño",
                    10.0F, 1002, estudiante1DTO.getMatricula(), 1));

        } catch (SQLException | IOException e) {

            fail("Error en @BeforeEach al preparar datos: " + e);
        }
    }

    @AfterEach
    void limpiarDatosDePrueba() {

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();

            conexionBaseDeDatos.prepareStatement("DELETE FROM evaluacion").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM estudiante").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM grupo").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM proyecto").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM academicoevaluador").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM academico").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM representante").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM organizacionvinculada").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM periodo").executeUpdate();
            conexionBaseDeDatos.prepareStatement("DELETE FROM usuario").executeUpdate();

            conexionBaseDeDatos.close();

        } catch (SQLException | IOException e) {

            fail("Error en @AfterEach al limpiar datos: " + e);
        }
    }

    @Test
    void insertarEstudianteConDatosValidos() {

        EstudianteDTO estudianteDTO = new EstudianteDTO(IDS_USUARIOS_INSERTADOS.get(3),
                "Usuario 4", "Apellido 4", "S23014103", 1, IDS_PROYECTOS_INSERTADOS.get(0),
                IDS_GRUPOS_INSERTADOS.get(0), 9.5F);

        try {

            boolean resultadoPrueba = estudianteDAO.insertarEstudiante(estudianteDTO);
            assertTrue(resultadoPrueba, "El estudiante se insertó correctamente");

        } catch (SQLException | IOException e) {

            fail("Error al insertar estudiante con datos válidos: " + e);
        }
    }

    @Test
    void insertarEstudianteConProyectoYGrupoInexistentes() {

        EstudianteDTO estudianteDTO = new EstudianteDTO(IDS_USUARIOS_INSERTADOS.get(3),
                "Usuario 4", "Apellido 4", "S23014103", 1, 10,
                7, 5.0F);

        assertThrows(SQLException.class, () -> {
            estudianteDAO.insertarEstudiante(estudianteDTO);
        }, "Se esperaba una excepción al insertar estudiante con grupo inexistentes");
    }

    @Test
    void eliminarEstudiantePorMatriculaExistente() {

        String matriculaEstudiante = "S23014102";

        try {

            boolean resultadoPrueba = estudianteDAO.eliminarEstudiantePorMatricula(0, matriculaEstudiante);
            assertTrue(resultadoPrueba, "El estudiante se eliminó correctamente");

        } catch (SQLException | IOException e) {

            fail("Error al eliminar estudiante por matrícula existente: " + e);
        }
    }

    @Test
    void eliminarEstudiantePorMatriculaInexistente() {

        String matriculaEstudiante = "S23014199";

        try {

            boolean resultadoPrueba = estudianteDAO.eliminarEstudiantePorMatricula(0, matriculaEstudiante);
            assertFalse(resultadoPrueba, "El estudiante no se eliminó porque la matrícula no existe");

        } catch (SQLException | IOException e) {

            fail("Error al eliminar estudiante por matrícula inexistente: " + e);
        }
    }

    @Test
    void reasignarProyectoAEstudianteConProyectoExistente() {

        EstudianteDTO estudianteReasignado = new EstudianteDTO(IDS_USUARIOS_INSERTADOS.get(1),
                "Usuario2", "Apellido2", "S23014102", 1, IDS_PROYECTOS_INSERTADOS.get(1),
                40776, 10);

        try {

            boolean resultadoPrueba = estudianteDAO.reasignarProyecto(estudianteReasignado);
            assertTrue(resultadoPrueba, "El proyecto se reasignó correctamente al estudiante");

        } catch (SQLException | IOException e) {

            fail("Error al reasignar proyecto a estudiante existente: " + e);
        }
    }

    @Test
    void reasignarProyectoAEstudianteConProyectoInexistente() {

        EstudianteDTO estudianteReasignado = new EstudianteDTO(IDS_USUARIOS_INSERTADOS.get(1),
                "Usuario2", "Apellido2", "S23014102", 1, 9999,
                40776, 10);

        assertThrows(SQLException.class, () -> {
            estudianteDAO.reasignarProyecto(estudianteReasignado);
        }, "Se esperaba una excepción al reasignar proyecto a estudiante con proyecto inexistente");
    }

    @Test
    void modificarEstudianteConDatosValidos() {

        EstudianteDTO estudianteModificado = new EstudianteDTO(IDS_USUARIOS_INSERTADOS.get(2),
                "Usuario3", "Apellido3", "S23014095", 1, IDS_PROYECTOS_INSERTADOS.get(0),
                IDS_GRUPOS_INSERTADOS.get(1), 8.5F);

        try {

            boolean resultadoPrueba = estudianteDAO.modificarEstudiante(estudianteModificado);
            assertTrue(resultadoPrueba, "El estudiante se modificó correctamente");

        } catch (SQLException | IOException e) {

            fail("Error al modificar estudiante con datos válidos: " + e);
        }
    }

    @Test
    void modificarEstudianteConDatosInvalidos() {

        EstudianteDTO estudianteModificado = new EstudianteDTO(IDS_USUARIOS_INSERTADOS.get(2),
                "Usuario3", "Apellido3", "S23014099", 1, 9999,
                9999, 8.5F);

        try {

            boolean resultadoPrueba = estudianteDAO.modificarEstudiante(estudianteModificado);
            assertFalse(resultadoPrueba, "El estudiante no se modificó porque los datos son inválidos");

        } catch (SQLException | IOException e) {

            fail("Error al modificar estudiante con datos inválidos: " + e);
        }
    }

    @Test
    void modificarEstudianteInexistente() {

        EstudianteDTO estudianteModificado = new EstudianteDTO(9999,
                "Usuario Inexistente", "Apellido Inexistente", "S99999999", 1,
                IDS_PROYECTOS_INSERTADOS.get(0), IDS_GRUPOS_INSERTADOS.get(0), 10);

        try {

            boolean resultadoPrueba = estudianteDAO.modificarEstudiante(estudianteModificado);
            assertFalse(resultadoPrueba, "El estudiante no se modificó porque no existe");

        } catch (SQLException | IOException e) {

            fail("Error al modificar estudiante inexistente: " + e);
        }
    }

    @Test
    void asignarProyectoAEstudianteConProyectoExistente() {

        String matriculaEstudiante = MATRICULAS_INSERTADAS.get(0);
        int idProyecto = IDS_PROYECTOS_INSERTADOS.get(0);

        try (PreparedStatement eliminarProyectoExistente = conexionBaseDeDatos
                .prepareStatement("UPDATE estudiante SET idProyecto = null WHERE matricula = ?")) {

            eliminarProyectoExistente.setString(1, matriculaEstudiante);
            eliminarProyectoExistente.executeUpdate();

            boolean resultadoPrueba = estudianteDAO.asignarProyecto(idProyecto, matriculaEstudiante);
            assertTrue(resultadoPrueba, "El proyecto se asignó correctamente al estudiante");

        } catch (SQLException | IOException e) {
            fail("Error al asignar proyecto a estudiante con proyecto existente: " + e);
        }
    }

    @Test
    void asignarProyectoAEstudianteConProyectoInexistente() {

        String matriculaEstudiante = MATRICULAS_INSERTADAS.get(0);
        int idProyectoInexistente = 9999;

        try (PreparedStatement eliminarProyectoExistente = conexionBaseDeDatos
                        .prepareStatement("UPDATE estudiante SET idProyecto = null WHERE matricula = ?")) {

            eliminarProyectoExistente.setString(1, matriculaEstudiante);
            eliminarProyectoExistente.executeUpdate();

            assertThrows(SQLException.class, () -> {
                estudianteDAO.asignarProyecto(idProyectoInexistente, matriculaEstudiante);
            }, "Se esperaba una excepción al asignar proyecto a estudiante con proyecto inexistente");

        } catch (SQLException e) {

            fail("Error al asignar proyecto a estudiante con proyecto inexistente: " + e);
        }
    }

    @Test
    void asignarCalificacionFinalAEstudianteConDatosValidos() {

        String matriculaEstudiante = MATRICULAS_INSERTADAS.get(0);
        double calificacionFinal = 9.0F;

        try (PreparedStatement eliminarCalificacionExistente = conexionBaseDeDatos
                        .prepareStatement("UPDATE estudiante SET calificacionFinal = null WHERE matricula = ?")) {

            eliminarCalificacionExistente.setString(1, matriculaEstudiante);

            boolean resultadoPrueba = estudianteDAO.asignarCalificacion(calificacionFinal, matriculaEstudiante);
            assertTrue(resultadoPrueba, "La calificación final se asignó correctamente al estudiante");

        } catch (SQLException | IOException e) {

            fail("Error al asignar calificación final a estudiante con datos válidos: " + e);
        }
    }

    @Test
    void asignarCalificacionFinalAEstudianteConDatosInvalidos() {

        String matriculaEstudiante = "S23014199";
        double calificacionFinal = 9.0F;

        try {

            boolean resultadoPrueba = estudianteDAO.asignarCalificacion(calificacionFinal, matriculaEstudiante);
            assertFalse(resultadoPrueba, "La calificación final no se asignó porque la matrícula no existe");

        } catch (SQLException | IOException e) {

            fail("Error al asignar calificación final a estudiante con datos inválidos: " + e);
        }
    }

    @Test
    void buscarEstudiantePorMatriculaExistente() {

        EstudianteDTO estudianteEsperado = new EstudianteDTO(IDS_USUARIOS_INSERTADOS.get(1),
                "Usuario2", "Apellido2", "S23014102", 1, IDS_PROYECTOS_INSERTADOS.get(1),
                IDS_GRUPOS_INSERTADOS.get(0), 10);

        try {

            EstudianteDTO estudianteEncontrado = estudianteDAO.buscarEstudiantePorMatricula("S23014102");
            assertEquals(estudianteEsperado, estudianteEncontrado,
                    "El estudiante se encontró correctamente por matrícula");

        } catch (SQLException | IOException e) {

            fail("Error al buscar estudiante por matrícula existente: " + e);
        }
    }

    @Test
    void buscarEstudiantePorMatriculaInexistente() {

        EstudianteDTO estudianteEsperado = new EstudianteDTO(-1, "N/A", "N/A",
                "N/A", 0,-1);

        try {

            EstudianteDTO estudianteEncontrado = estudianteDAO.buscarEstudiantePorMatricula("S23014199");
            assertEquals(estudianteEsperado, estudianteEncontrado,
                    "El estudiante encontrado debe coincidir con el estudiante esperado");

        } catch (SQLException | IOException e) {

            fail("Error al buscar estudiante por matrícula inexistente: " + e);
        }
    }

    @Test
    void buscarEstudiantePorIDValido () {

        EstudianteDTO estudianteEsperado = new EstudianteDTO(IDS_USUARIOS_INSERTADOS.get(1),
                "Usuario2", "Apellido2", "S23014102", 1, IDS_PROYECTOS_INSERTADOS.get(1),
                IDS_GRUPOS_INSERTADOS.get(0), 10);

        try {

            EstudianteDTO estudianteEncontrado = estudianteDAO.buscarEstudiantePorID(IDS_USUARIOS_INSERTADOS.get(1));
            assertEquals(estudianteEsperado, estudianteEncontrado,
                    "El estudiante se encontró correctamente por ID");

        } catch (SQLException | IOException e) {

            fail("Error al buscar estudiante por ID válido: " + e);
        }
    }

    @Test
    void buscarEstudiantePorIDInexistente() {

        EstudianteDTO estudianteEsperado = new EstudianteDTO(-1, "N/A", "N/A",
                "N/A", 0, -1);

        try {

            EstudianteDTO estudianteEncontrado = estudianteDAO.buscarEstudiantePorID(9999);
            assertEquals(estudianteEsperado, estudianteEncontrado,
                    "El estudiante encontrado debe coincidir con el estudiante esperado");

        } catch (SQLException | IOException e) {

            fail("Error al buscar estudiante por ID inválido: " + e);
        }
    }

    @Test
    void listarEstudiantesConDatosEnLaBase() {

        List<EstudianteDTO> estudiantesEsperados = List.of(
                new EstudianteDTO(IDS_USUARIOS_INSERTADOS.get(2), "Usuario3", "Apellido3", "S23014095", 1,
                        IDS_PROYECTOS_INSERTADOS.get(1)),
                new EstudianteDTO(IDS_USUARIOS_INSERTADOS.get(1), "Usuario2", "Apellido2", "S23014102", 1,
                        IDS_PROYECTOS_INSERTADOS.get(0))
        );

        try {

            List<EstudianteDTO> estudiantesEncontrados = estudianteDAO.listarEstudiantes();

            for (int estudianteDTO = 0; estudianteDTO < estudiantesEsperados.size(); estudianteDTO++) {

                assertEquals(estudiantesEsperados.get(estudianteDTO), estudiantesEncontrados.get(estudianteDTO),
                        "Los estudiantes deben coincidir");
            }
        } catch (SQLException | IOException e) {

            fail("Error al listar estudiantes con datos en la base: " + e);
        }
    }

    @Test
    void listarEstudiantesSinDatosEnLaBase() {

        try {

            PreparedStatement eliminarEvaluaciones = conexionBaseDeDatos
                    .prepareStatement("DELETE FROM evaluacion");
            eliminarEvaluaciones.executeUpdate();

            PreparedStatement eliminarEstudiantes = conexionBaseDeDatos
                    .prepareStatement("DELETE FROM estudiante");
            eliminarEstudiantes.executeUpdate();

            List<EstudianteDTO> estudiantes = estudianteDAO.listarEstudiantes();
            assertTrue(estudiantes.isEmpty(), "La lista de estudiantes debe estar vacía");

        } catch (SQLException | IOException e) {

            fail("Error al listar estudiantes sin datos en la base: " + e);
        }
    }

    @Test
    void obtenerEstudiantesActivosPorNRCValido() {

        List<EstudianteDTO> estudiantesEsperados = List.of(
                new EstudianteDTO(1, "Usuario2", "Apellido2",
                        "S23014102", 1, 1)
        );

        try {

            List<EstudianteDTO> estudiantesEncontrados = estudianteDAO.obtenerEstudiantesActivosPorNRC(40776);

            for (int estudianteDTO = 0; estudianteDTO < estudiantesEsperados.size(); estudianteDTO++) {

                assertEquals(estudiantesEsperados.get(estudianteDTO), estudiantesEncontrados.get(estudianteDTO),
                        "Los estudiantes deben coincidir");
            }
        } catch (SQLException | IOException e) {

            fail("Error al obtener estudiantes activos por NRC válido: " + e);
        }
    }

    @Test
    void obtenerEstudiantesActivosPorNRCInvalido() {

        int nrcInvalido = 9999;

        try {

            List<EstudianteDTO> estudiantes = estudianteDAO.obtenerEstudiantesActivosPorNRC(nrcInvalido);
            assertTrue(estudiantes.isEmpty(), "La lista de estudiantes debe estar vacía para NRC inválido");

        } catch (SQLException | IOException e) {

            fail("Error al obtener estudiantes activos por NRC inválido: " + e);
        }
    }

    @Test
    void listarEstudiantesNoEvaluadosSinDatosEnLaBase() {

        try {

            PreparedStatement eliminarEvaluaciones = conexionBaseDeDatos
                    .prepareStatement("DELETE FROM evaluacion");
            eliminarEvaluaciones.executeUpdate();

            PreparedStatement eliminarEstudiantes = conexionBaseDeDatos
                    .prepareStatement("DELETE FROM estudiante");
            eliminarEstudiantes.executeUpdate();

            List<EstudianteDTO> estudiantes = estudianteDAO.listarEstudiantesNoEvaluados(1002);
            assertTrue(estudiantes.isEmpty(), "La lista de estudiantes no evaluados debe estar vacía");

        } catch (SQLException | IOException e) {

            fail("Error al listar estudiantes no evaluados sin datos en la base: " + e);
        }
    }

    @Test
    void listarEstudiantesConEvaluacionesPorGrupoConDatosEnLaBase() {

        List<EstudianteDTO> estudiantesEsperados = List.of(
                new EstudianteDTO(-1, "Usuario2", "Apellido2",
                        "S23014102", 1, 0)
        );

        try {

            List<EstudianteDTO> estudiantesEncontrados =
                    estudianteDAO.listarEstudiantesConEvaluacionesPorGrupo(40776);

            for (int estudianteDTO = 0; estudianteDTO < estudiantesEsperados.size(); estudianteDTO++) {

                assertEquals(estudiantesEsperados.get(estudianteDTO), estudiantesEncontrados.get(estudianteDTO),
                        "Los estudiantes deben coincidir");
            }

        } catch (SQLException | IOException e) {

            fail("Error al listar estudiantes con evaluaciones por grupo con datos en la base: " + e);
        }
    }


    @Test
    void listarEstudiantesConEvaluacionesPorGrupoSinDatosEnLaBase() {

        try {

            PreparedStatement eliminarEvaluaciones = conexionBaseDeDatos
                    .prepareStatement("DELETE FROM evaluacion");
            eliminarEvaluaciones.executeUpdate();

            PreparedStatement eliminarEstudiantes = conexionBaseDeDatos
                    .prepareStatement("DELETE FROM estudiante");
            eliminarEstudiantes.executeUpdate();

            List<EstudianteDTO> estudiantes = estudianteDAO.listarEstudiantesConEvaluacionesPorGrupo(40776);
            assertTrue(estudiantes.isEmpty(), "La lista de estudiantes con evaluaciones debe estar vacía");

        } catch (SQLException | IOException e) {

            fail("Error al listar estudiantes con evaluaciones por grupo sin datos en la base: " + e);
        }
    }

    @Test
    void listarEstudiantesConEvaluacionesPorGrupoInvalido() {

        int NRCInvalido = 9999;

        try {

            List<EstudianteDTO> estudiantes = estudianteDAO.listarEstudiantesConEvaluacionesPorGrupo(NRCInvalido);
            assertTrue(estudiantes.isEmpty(), "La lista de estudiantes con evaluaciones debe estar vacía para NRC inválido");

        } catch (SQLException | IOException e) {

            fail("Error al listar estudiantes con evaluaciones por grupo inválido: " + e);
        }
    }
}