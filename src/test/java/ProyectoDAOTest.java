import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.*;
import logica.DTOs.*;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProyectoDAOTest {

    private OrganizacionVinculadaDAO organizacionDAO;
    private RepresentanteDAO representanteDAO;
    private ProyectoDAO proyectoDAO;

    private final List<Integer> IDS_PROYECTOS_INSERTADOS = new ArrayList<>();
    private final List<Integer> IDS_ORGANIZACIONES_INSERTADAS = new ArrayList<>();
    private final List<Integer> IDS_REPRESENTANTES_INSERTADOS = new ArrayList<>();

    @BeforeEach
    void prepararDatosDePrueba() {

        organizacionDAO = new OrganizacionVinculadaDAO();
        representanteDAO = new RepresentanteDAO();
        proyectoDAO = new ProyectoDAO();

        IDS_PROYECTOS_INSERTADOS.clear();
        IDS_ORGANIZACIONES_INSERTADAS.clear();
        IDS_REPRESENTANTES_INSERTADOS.clear();

        try (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection()) {

            PreparedStatement eliminarProyectos =
                    conexionBaseDeDatos.prepareStatement("DELETE FROM proyecto");
            eliminarProyectos.executeUpdate();

            PreparedStatement eliminarRepresentantes =
                    conexionBaseDeDatos.prepareStatement("DELETE FROM representante");
            eliminarRepresentantes.executeUpdate();

            PreparedStatement eliminarOrganizaciones =
                    conexionBaseDeDatos.prepareStatement("DELETE FROM organizacionvinculada");
            eliminarOrganizaciones.executeUpdate();

            int idOrganizacion1 = organizacionDAO.crearNuevaOrganizacion(
                    new OrganizacionVinculadaDTO(1, "Empresa 1", "Dirección 1",
                            "empresa1@test.com", "5551111111", 1)
            );
            int idOrganizacion2 = organizacionDAO.crearNuevaOrganizacion(
                    new OrganizacionVinculadaDTO(2, "Empresa 2", "Dirección 2",
                            "empresa2@test.com", "5552222222", 1)
            );

            IDS_ORGANIZACIONES_INSERTADAS.add(idOrganizacion1);
            IDS_ORGANIZACIONES_INSERTADAS.add(idOrganizacion2);

            representanteDAO.insertarRepresentante(
                    new RepresentanteDTO(1, "representante1@gmail.com", "5554444444",
                            "Representante 1", "Apellido 1", idOrganizacion1, 1)
            );
            representanteDAO.insertarRepresentante(
                    new RepresentanteDTO(2, "representante2@gmail.com", "5555555555",
                            "Representante 2", "Apellido 2", idOrganizacion2, 1)
            );

            IDS_REPRESENTANTES_INSERTADOS.addAll(List.of(1, 2));

            int idProyecto1 = proyectoDAO.crearNuevoProyecto(
                    new ProyectoDTO(
                            1,
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
                            2,
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
                            2,
                            "Proyecto para mejorar la calidad educativa mediante tecnología",
                            100,
                            500,
                            0
                    )
            );

            IDS_PROYECTOS_INSERTADOS.addAll(List.of(idProyecto1, idProyecto2));

        } catch (SQLException | IOException e) {

            fail("Error en @BeforeEach al preparar datos: " + e);
        }
    }

    @AfterEach
    void limpiarDatosDePrueba() {

        try (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection()) {

            PreparedStatement eliminarProyectos =
                    conexionBaseDeDatos.prepareStatement("DELETE FROM proyecto");
            eliminarProyectos.executeUpdate();


            PreparedStatement eliminarRepresentantes =
                    conexionBaseDeDatos.prepareStatement("DELETE FROM representante");
            eliminarRepresentantes.executeUpdate();

            PreparedStatement eliminarOrganizaciones =
                    conexionBaseDeDatos.prepareStatement("DELETE FROM organizacionvinculada");
            eliminarOrganizaciones.executeUpdate();

        } catch (SQLException | IOException e) {

            fail("Error al limpiar datos: " + e);
        }
    }

    @Test
    void crearNuevoProyectoConDatosValidos() {

        try {

            ProyectoDTO nuevoProyecto = new ProyectoDTO(
                    3,
                    "Proyecto de Investigación",
                    "Investigar nuevas tecnologías emergentes",
                    "Desarrollar prototipos funcionales",
                    "Fomentar la innovación tecnológica",
                    "Metodología de investigación aplicada",
                    "Recursos de laboratorio y personal especializado",
                    "Investigación, desarrollo y validación",
                    "Responsabilidad del equipo de investigación",
                    "18 meses",
                    1,
                    2,
                    "Proyecto para explorar nuevas fronteras tecnológicas",
                    75,
                    300,
                    8
            );

            int idProyectoCreado = proyectoDAO.crearNuevoProyecto(nuevoProyecto);
            assertTrue(idProyectoCreado > 0, "El proyecto debería haberse creado con éxito");
            IDS_PROYECTOS_INSERTADOS.add(idProyectoCreado);

        } catch (SQLException | IOException e) {

            fail("Error al crear nuevo proyecto: " + e);
        }
    }

    @Test
    void crearNuevoProyectoConDatosInvalidos() {

        ProyectoDTO proyectoInvalido = new ProyectoDTO(
                1,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                1,
                4,
                null,
                20,
                100,
                10
        );

        assertThrows(SQLException.class, () -> {
            proyectoDAO.crearNuevoProyecto(proyectoInvalido);
            }, "Debería lanzar una excepción al intentar crear un proyecto con datos inválidos");
    }

    @Test
    void crearNuevoProyectoConIDRepresentanteInexistente() {

        ProyectoDTO proyectoInvalido = new ProyectoDTO(
                3,
                "Proyecto de Investigación",
                "Investigar nuevas tecnologías emergentes",
                "Desarrollar prototipos funcionales",
                "Fomentar la innovación tecnológica",
                "Metodología de investigación aplicada",
                "Recursos de laboratorio y personal especializado",
                "Investigación, desarrollo y validación",
                "Responsabilidad del equipo de investigación",
                "18 meses",
                1,
                4,
                "Proyecto para explorar nuevas fronteras tecnológicas",
                75,
                300,
                8
        );

        assertThrows(SQLException.class, () -> {
            proyectoDAO.crearNuevoProyecto(proyectoInvalido);
            }, "Debería lanzar una excepción al intentar crear un proyecto con un ID de representante inexistente");
    }

    @Test
    void eliminarProyectoPorIDExistente() {

        try {

            boolean proyectoEliminado = proyectoDAO.eliminarProyectoPorID(IDS_PROYECTOS_INSERTADOS.get(0));
            assertTrue(proyectoEliminado, "El proyecto debería haberse eliminado con éxito");

        } catch (SQLException | IOException e) {

            fail("Error al eliminar proyecto por ID: " + e);
        }
    }

    @Test
    void eliminarProyectoPorIDInexistente() {

        try {

            boolean proyectoEliminado = proyectoDAO.eliminarProyectoPorID(999);
            assertFalse(proyectoEliminado, "No debería eliminar un proyecto inexistente");

        } catch (SQLException | IOException e) {

            fail("Error al eliminar proyecto por ID inexistente: " + e);
        }
    }

    @Test
    void modificarProyectoConDatosValidos() {

        try {

            ProyectoDTO proyectoModificado = new ProyectoDTO(
                    IDS_PROYECTOS_INSERTADOS.get(1),
                    "Proyecto Educativo Modificado",
                    "Fomentar el aprendizaje interactivo en estudiantes",
                    "Desarrollar una plataforma educativa mejorada",
                    "Promover el uso de tecnología en la educación",
                    "Metodología basada en diseño centrado en el usuario",
                    "Recursos digitales y soporte técnico ampliados",
                    "Análisis, diseño, desarrollo y evaluación",
                    "Responsabilidad del equipo de diseño y desarrollo",
                    "12 meses",
                    1,
                    2,
                    "Proyecto para mejorar la calidad educativa mediante tecnología",
                    120,
                    600,
                    15
            );

            boolean proyectoModificadoConExito = proyectoDAO.modificarProyecto(proyectoModificado);
            assertTrue(proyectoModificadoConExito, "El proyecto debería haberse modificado con éxito");

        } catch (SQLException | IOException e) {

            fail("Error al modificar proyecto: " + e);
        }
    }

    @Test
    void modificarProyectoConIDInexistente() {

        ProyectoDTO proyectoInexistente = new ProyectoDTO(
                999,
                "Proyecto Inexistente",
                "Descripción",
                "Desarrollo",
                "Objetivos",
                "Metodología",
                "Recursos",
                "Actividades",
                "Responsabilidades",
                "6 meses",
                1,
                1,
                "Descripción general",
                50,
                200,
                5
        );

        try {

            boolean proyectoModificado = proyectoDAO.modificarProyecto(proyectoInexistente);
            assertFalse(proyectoModificado, "No debería modificar un proyecto inexistente");

        } catch (SQLException | IOException e) {

            fail("Error al modificar proyecto inexistente: " + e);
        }
    }

    @Test
    void modificarProyectoConDatosInvalidos() {

        ProyectoDTO proyectoInvalido = new ProyectoDTO(
                IDS_PROYECTOS_INSERTADOS.get(1),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                1,
                2,
                null,
                20,
                100,
                10
        );

        assertThrows(SQLException.class, () -> {
            proyectoDAO.modificarProyecto(proyectoInvalido);
            }, "Debería lanzar una excepción al intentar modificar un proyecto con datos inválidos");
    }

    @Test
    void buscarProyectoPorNombreValido() {

        ProyectoDTO proyectoEsperado = new ProyectoDTO(
                IDS_PROYECTOS_INSERTADOS.get(0),
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
        );

        try {

            ProyectoDTO proyectosEncontrado = proyectoDAO.buscarProyectoPorNombre("Proyecto Innovador");
            assertEquals(proyectoEsperado,proyectosEncontrado,
                    "El proyecto encontrado deberia ser igual al proyecto esperado");

        } catch (SQLException | IOException e) {

            fail("Error al buscar proyecto por nombre válido: " + e);
        }
    }

    @Test
    void buscarProyectoPorNombreInvalido() {

        ProyectoDTO proyectoEsperado = new ProyectoDTO(
                -1,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                -1,
                -1,
                null,
                -1,
                -1,
                -1
        );

        try {

            ProyectoDTO proyectoEncontrado = proyectoDAO.buscarProyectoPorNombre("Proyecto Inexistente");
            assertEquals(proyectoEsperado, proyectoEncontrado,
                    "El proyecto encontrado debería ser igual al proyecto esperado (inexistente)");

        } catch (SQLException | IOException e) {

            fail("Error al buscar proyecto por nombre inválido: " + e);
        }
    }

    @Test
    void buscarProyectoPorIDExistente() {

        int idProyecto = IDS_PROYECTOS_INSERTADOS.get(0);

        ProyectoDTO proyectoEsperado = new ProyectoDTO(
                idProyecto,
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
        );

        try {

            ProyectoDTO proyectoEncontrado = proyectoDAO.buscarProyectoPorID(idProyecto);
            assertEquals(proyectoEsperado, proyectoEncontrado,
                    "El proyecto encontrado debería ser igual al proyecto esperado");

        } catch (SQLException | IOException e) {

            fail("Error al buscar proyecto por ID existente: " + e);
        }
    }

    @Test
    void buscarProyectoPorIDInexistente() {

        ProyectoDTO proyectoEsperado = new ProyectoDTO(
                -1,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                -1,
                -1,
                null,
                -1,
                -1,
                -1
        );

        try {

            ProyectoDTO proyectoEncontrado = proyectoDAO.buscarProyectoPorID(999);
            assertEquals(proyectoEsperado, proyectoEncontrado,
                    "El proyecto encontrado debería ser igual al proyecto esperado (inexistente)");

        } catch (SQLException | IOException e) {

            fail("Error al buscar proyecto por ID inválido: " + e);
        }
    }

    @Test
    void listarProyectosConDatosEnLaBase() {

        try {

            List<ProyectoDTO> proyectosEsperados = List.of(
                    new ProyectoDTO(
                            IDS_PROYECTOS_INSERTADOS.get(0),
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
                    ),
                    new ProyectoDTO(
                            IDS_PROYECTOS_INSERTADOS.get(1),
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
                            2,
                            "Proyecto para mejorar la calidad educativa mediante tecnología",
                            100,
                            500,
                            0
                    )
            );

            List<ProyectoDTO> proyectosObtenidos = proyectoDAO.listarProyectos();

            for (int proyectoDTO = 0; proyectoDTO < proyectosEsperados.size(); proyectoDTO++) {

                assertEquals(proyectosEsperados.get(proyectoDTO), proyectosObtenidos.get(proyectoDTO),
                        "El proyecto en la posición " + proyectoDTO + " debería coincidir");
            }

        } catch (SQLException | IOException e) {

            fail("Error al listar proyectos con datos en la base: " + e);
        }
    }

    @Test
    void listarProyectosSinDatosEnLaBase() {

        try (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection()) {

            PreparedStatement eliminar = conexionBaseDeDatos.prepareStatement
                    ("DELETE FROM proyecto");
            eliminar.executeUpdate();

            List<ProyectoDTO> proyectos = proyectoDAO.listarProyectos();
            assertTrue(proyectos.isEmpty(), "La lista de proyectos debería estar vacía");

        } catch (SQLException | IOException e) {

            fail("Error al listar proyectos sin datos en la base: " + e);
        }
    }

    @Test
    void listarProyectosConCupoConDatosEnLaBase() {

        try {

            List<ProyectoDTO> proyectosEsperados = List.of(
                    new ProyectoDTO(
                            IDS_PROYECTOS_INSERTADOS.get(0),
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

            List<ProyectoDTO> proyectosConCupoObtenidos = proyectoDAO.listarProyectosConCupo();

            for (int proyectoDTO = 0; proyectoDTO < proyectosEsperados.size(); proyectoDTO++) {

                assertEquals(proyectosEsperados.get(proyectoDTO), proyectosConCupoObtenidos.get(proyectoDTO),
                        "El proyecto con cupo en la posición " + proyectoDTO + " debería coincidir");
            }

        } catch (SQLException | IOException e) {

            fail("Error al listar proyectos con cupo con datos en la base: " + e);
        }
    }

    @Test
    void listarProyectosConCupoSinDatosEnLaBase() {

        try (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection()) {

            PreparedStatement eliminar = conexionBaseDeDatos.prepareStatement
                    ("DELETE FROM proyecto");
            eliminar.executeUpdate();

            List<ProyectoDTO> proyectosConCupo = proyectoDAO.listarProyectosConCupo();
            assertTrue(proyectosConCupo.isEmpty(), "La lista de proyectos con cupo debería estar vacía");

        } catch (SQLException | IOException e) {

            fail("Error al listar proyectos con cupo sin datos en la base: " + e);
        }
    }

    @Test
    void listarProyectosSinCupoConDatosEnLaBase() {

        try (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection()) {

            PreparedStatement eliminarProyectos = conexionBaseDeDatos.prepareStatement
                    ("UPDATE proyecto SET estudiantesrequeridos = 0");
            eliminarProyectos.executeUpdate();

            List<ProyectoDTO> proyectosSinCupo = proyectoDAO.listarProyectosConCupo();
            assertTrue(proyectosSinCupo.isEmpty(), "La lista de proyectos sin cupo debería estar vacía");

        } catch (SQLException | IOException e) {

            fail("Error al listar proyectos sin cupo sin datos en la base: " + e);

        }
    }
}