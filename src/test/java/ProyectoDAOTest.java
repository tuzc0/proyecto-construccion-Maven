import logica.DAOs.*;
import logica.DTOs.*;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProyectoDAOTest {

    private ProyectoDAO proyectoDAO;
    private OrganizacionVinculadaDAO organizacionVinculadaDAO;
    private CronogramaActividadesDAO cronogramaActividadesDAO;
    private EstudianteDAO estudianteDAO;
    private UsuarioDAO usuarioDAO;
    private RepresentanteDAO representanteDAO;

    private final List<Integer> IDS_PROYECTOS_INSERTADOS = new ArrayList<>();
    private final List<Integer> IDS_ORGANIZACIONES_INSERTADAS = new ArrayList<>();
    private final List<Integer> IDS_CRONOGRAMAS_INSERTADOS = new ArrayList<>();
    private final List<String> MATRICULAS_ESTUDIANTES_INSERTADAS = new ArrayList<>();
    private final List<Integer> IDS_USUARIOS_INSERTADOS = new ArrayList<>();
    private final List<Integer> IDS_REPRESENTANTES_INSERTADOS = new ArrayList<>();

    @BeforeAll
    void inicializarDAOs() throws SQLException, IOException {
        proyectoDAO = new ProyectoDAO();
        organizacionVinculadaDAO = new OrganizacionVinculadaDAO();
        cronogramaActividadesDAO = new CronogramaActividadesDAO();
        estudianteDAO = new EstudianteDAO();
        usuarioDAO = new UsuarioDAO();
        representanteDAO = new RepresentanteDAO();
    }

    @BeforeEach
    void prepararDatosDePrueba() {
        try {
            // --- Limpiar datos anteriores ---
            for (int idProyecto : IDS_PROYECTOS_INSERTADOS) {
                proyectoDAO.eliminarProyectoPorID(idProyecto);
            }
            for (int idOrganizacion : IDS_ORGANIZACIONES_INSERTADAS) {
                organizacionVinculadaDAO.eliminarOrganizacionPorID(idOrganizacion);
            }
            for (int idCronograma : IDS_CRONOGRAMAS_INSERTADOS) {
                cronogramaActividadesDAO.modificarCronogramaDeActividades(
                        new CronogramaActividadesDTO(idCronograma, null, null, "0")
                );
            }
            // <-- aquí cambiamos el 0 por el 1 para que coincida con la carrera usada al insertar -->
            for (String matricula : MATRICULAS_ESTUDIANTES_INSERTADAS) {
                estudianteDAO.eliminarEstudiantePorMatricula(1, matricula);
            }
            for (int idUsuario : IDS_USUARIOS_INSERTADOS) {
                usuarioDAO.eliminarUsuarioPorID(idUsuario);
            }
            for (int idRepresentante : IDS_REPRESENTANTES_INSERTADOS) {
                representanteDAO.eliminarRepresentantePorID(idRepresentante);
            }

            // --- Insertar usuarios de prueba ---
            UsuarioDTO usuarioPrueba1 = new UsuarioDTO(0, "Estudiante1", "Apellido1", 1);
            UsuarioDTO usuarioPrueba2 = new UsuarioDTO(0, "Estudiante2", "Apellido2", 1);
            int idUsuario1 = usuarioDAO.insertarUsuario(usuarioPrueba1);
            int idUsuario2 = usuarioDAO.insertarUsuario(usuarioPrueba2);
            IDS_USUARIOS_INSERTADOS.add(idUsuario1);
            IDS_USUARIOS_INSERTADOS.add(idUsuario2);

            // --- Insertar estudiantes con los IDs correctos ---
            EstudianteDTO estudiante1 = new EstudianteDTO(idUsuario1, "Estudiante1", "Apellido1", "12345", 1);
            EstudianteDTO estudiante2 = new EstudianteDTO(idUsuario2, "Estudiante2", "Apellido2", "67890", 1);
            estudianteDAO.insertarEstudiante(estudiante1);
            estudianteDAO.insertarEstudiante(estudiante2);
            MATRICULAS_ESTUDIANTES_INSERTADAS.add("12345");
            MATRICULAS_ESTUDIANTES_INSERTADAS.add("67890");

            // --- Insertar representantes ---
            RepresentanteDTO representante1 = new RepresentanteDTO(0, "correo1@example.com", "1234567890", "Nombre1", "Apellido1", 1, 1);
            RepresentanteDTO representante2 = new RepresentanteDTO(0, "correo2@example.com", "0987654321", "Nombre2", "Apellido2", 1, 1);
            representanteDAO.insertarRepresentante(representante1);
            representanteDAO.insertarRepresentante(representante2);
            IDS_REPRESENTANTES_INSERTADOS.add(representante1.getIDRepresentante());
            IDS_REPRESENTANTES_INSERTADOS.add(representante2.getIDRepresentante());

            // --- Organizaciones ---
            int idOrganizacion1 = organizacionVinculadaDAO.crearNuevaOrganizacion(
                    new OrganizacionVinculadaDTO(0, "Organizacion 1", "Direccion 1", "correo1@test.com", "1234567890", 1)
            );
            int idOrganizacion2 = organizacionVinculadaDAO.crearNuevaOrganizacion(
                    new OrganizacionVinculadaDTO(0, "Organizacion 2", "Direccion 2", "correo2@test.com", "0987654321", 1)
            );
            IDS_ORGANIZACIONES_INSERTADAS.add(idOrganizacion1);
            IDS_ORGANIZACIONES_INSERTADAS.add(idOrganizacion2);

            // --- Cronogramas ---
            int idCronograma1 = 1, idCronograma2 = 2;
            cronogramaActividadesDAO.crearNuevoCronogramaDeActividades(
                    new CronogramaActividadesDTO(idCronograma1,
                            Timestamp.valueOf("2023-01-01 00:00:00"),
                            Timestamp.valueOf("2023-12-31 23:59:59"),
                            "12345")
            );
            cronogramaActividadesDAO.crearNuevoCronogramaDeActividades(
                    new CronogramaActividadesDTO(idCronograma2,
                            Timestamp.valueOf("2023-02-01 00:00:00"),
                            Timestamp.valueOf("2023-11-30 23:59:59"),
                            "67890")
            );
            IDS_CRONOGRAMAS_INSERTADOS.add(idCronograma1);
            IDS_CRONOGRAMAS_INSERTADOS.add(idCronograma2);

            // --- Proyectos ---
            ProyectoDTO proyecto1 = new ProyectoDTO(0, "Proyecto 1", "Objetivo 1",
                    "Inmediato 1", "Mediato 1", "Metodología 1",
                    "Recursos 1", "Actividades 1", "Responsabilidades 1",
                    "1 mes", "Lunes a Viernes", idCronograma1, 1, idOrganizacion1 ,"descripcion");
            ProyectoDTO proyecto2 = new ProyectoDTO(0, "Proyecto 2", "Objetivo 2",
                    "Inmediato 2", "Mediato 2", "Metodología 2",
                    "Recursos 2", "Actividades 2", "Responsabilidades 2",
                    "2 meses", "Lunes a Sábado", idCronograma2, 1, idOrganizacion2, "descripcion");
            proyectoDAO.crearNuevoProyecto(proyecto1);
            proyectoDAO.crearNuevoProyecto(proyecto2);
            IDS_PROYECTOS_INSERTADOS.add(proyecto1.getIdProyecto());
            IDS_PROYECTOS_INSERTADOS.add(proyecto2.getIdProyecto());

        } catch (SQLException | IOException e) {
            fail("Error al preparar los datos de prueba: " + e.getMessage());
        }
    }

    @AfterAll
    void limpiarBaseDeDatos() {
        try {
            for (int idProyecto : IDS_PROYECTOS_INSERTADOS) {
                proyectoDAO.eliminarProyectoPorID(idProyecto);
            }
            for (int idOrganizacion : IDS_ORGANIZACIONES_INSERTADAS) {
                organizacionVinculadaDAO.eliminarOrganizacionPorID(idOrganizacion);
            }
            for (int idCronograma : IDS_CRONOGRAMAS_INSERTADOS) {
                cronogramaActividadesDAO.modificarCronogramaDeActividades(
                        new CronogramaActividadesDTO(idCronograma, null, null, "0")
                );
            }
            for (String matricula : MATRICULAS_ESTUDIANTES_INSERTADAS) {
                estudianteDAO.eliminarEstudiantePorMatricula(1, matricula);
            }
            for (int idUsuario : IDS_USUARIOS_INSERTADOS) {
                usuarioDAO.eliminarUsuarioPorID(idUsuario);
            }
            for (int idRepresentante : IDS_REPRESENTANTES_INSERTADOS) {
                representanteDAO.eliminarRepresentantePorID(idRepresentante);
            }
        } catch (SQLException | IOException e) {
            fail("Error al limpiar la base de datos: " + e.getMessage());
        }
    }

    @Test
    void testInsertarProyectoConDatosValidos() {
        try {
            ProyectoDTO nuevoProyecto = new ProyectoDTO(4, "Proyecto 4", "Objetivo 4", "Inmediato 4",
                    "Mediato 4", "Metodología 4", "Recursos 4", "Actividades 4",
                    "Responsabilidades 4", "4 meses", "Lunes a Viernes", 4, 1, 4,"descripcion");
            boolean resultado = proyectoDAO.crearNuevoProyecto(nuevoProyecto);
            assertTrue(resultado, "El proyecto debería ser insertado correctamente.");
        } catch (SQLException | IOException e) {
            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testInsertarProyectoConDatosInvalidos() {
        ProyectoDTO proyectoInvalido = new ProyectoDTO(5, null, null, null,
                null, null, null, null, null, null, null,
                -1, -1, 1, null);
        try {
            boolean resultado = proyectoDAO.crearNuevoProyecto(proyectoInvalido);
            assertFalse(resultado, "El proyecto no debería ser insertado con datos inválidos.");
        } catch (SQLException | IOException e) {
            assertTrue(true);
        }
    }

    @Test
    void testBuscarProyectoPorIDConDatosValidos() {
        try {
            ProyectoDTO proyecto = proyectoDAO.buscarProyectoPorID(1);
            assertEquals(1, proyecto.getIdProyecto(), "El ID del proyecto debería coincidir.");
        } catch (SQLException | IOException e) {
            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testBuscarProyectoPorIDConDatosInvalidos() {
        try {
            ProyectoDTO proyecto = proyectoDAO.buscarProyectoPorID(999);
            assertEquals(-1, proyecto.getIdProyecto(), "No debería encontrarse un proyecto con ese ID.");
        } catch (SQLException | IOException e) {
            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testEliminarProyectoPorIDConDatosValidos() {
        try {
            boolean resultado = proyectoDAO.eliminarProyectoPorID(2);
            assertTrue(resultado, "El proyecto debería ser eliminado correctamente.");
        } catch (SQLException | IOException e) {
            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testEliminarProyectoPorIDConDatosInvalidos() {
        try {
            boolean resultado = proyectoDAO.eliminarProyectoPorID(999);
            assertFalse(resultado, "No debería eliminarse un proyecto inexistente.");
        } catch (SQLException | IOException e) {
            assertTrue(true);
        }
    }

    @Test
    void testModificarProyectoConDatosValidos() {
        try {
            ProyectoDTO proyectoActualizado = new ProyectoDTO(3, "Proyecto Modificado", "Objetivo Modificado",
                    "Inmediato Modificado", "Mediato Modificado", "Metodología Modificada",
                    "Recursos Modificados", "Actividades Modificadas", "Responsabilidades Modificadas",
                    "6 meses", "Lunes a Domingo", 3, 3, 1, "descripcion" );
            boolean resultado = proyectoDAO.modificarProyecto(proyectoActualizado);
            assertTrue(resultado, "El proyecto debería ser modificado correctamente.");
        } catch (SQLException | IOException e) {
            fail("No se esperaba una excepción: " + e.getMessage());
        }
    }

    @Test
    void testModificarProyectoConDatosInvalidos() {
        ProyectoDTO proyectoInvalido = new ProyectoDTO(3, null, null, null, null,
                null, null, null, null, null, null, -1, -1,
                1, null);
        try {
            boolean resultado = proyectoDAO.modificarProyecto(proyectoInvalido);
            assertFalse(resultado, "No debería modificarse un proyecto con datos inválidos.");
        } catch (SQLException | IOException e) {
            assertTrue(true);
        }
    }
}