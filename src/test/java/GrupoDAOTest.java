import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.AcademicoDAO;
import logica.DAOs.GrupoDAO;
import logica.DAOs.PeriodoDAO;
import logica.DAOs.UsuarioDAO;
import logica.DTOs.AcademicoDTO;
import logica.DTOs.GrupoDTO;
import logica.DTOs.PeriodoDTO;
import logica.DTOs.UsuarioDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GrupoDAOTest {

    private GrupoDAO grupoDAO;
    private PeriodoDAO periodoDAO;
    private AcademicoDAO academicoDAO;
    private UsuarioDAO usuarioDAO;
    private Connection conexionBaseDeDatos;

    private final List<Integer> NUMEROS_DE_PERSONAL_INSERTADOS = new ArrayList<>();
    private final List<Integer> IDS_USUARIOS_INSERTADOS = new ArrayList<>();
    private final List<Integer> IDS_GRUPOS_INSERTADOS = new ArrayList<>();
    private final List<Integer> IDS_PERIODOS_INSERTADOS = new ArrayList<>();

    @BeforeEach
    void prepararDatosDePrueba() {

        academicoDAO = new AcademicoDAO();
        usuarioDAO = new UsuarioDAO();
        grupoDAO = new GrupoDAO();
        periodoDAO = new PeriodoDAO();

        IDS_PERIODOS_INSERTADOS.clear();
        NUMEROS_DE_PERSONAL_INSERTADOS.clear();
        IDS_USUARIOS_INSERTADOS.clear();
        IDS_GRUPOS_INSERTADOS.clear();

        try {

            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();

            for (int NRCGrupo : List.of(40776, 40789, 40889)) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM Grupo WHERE NRC = " + NRCGrupo)
                        .executeUpdate();
            }

            for (int idPeriodo : List.of(1, 2, 3)) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM periodo WHERE idPeriodo = " + idPeriodo).executeUpdate();
            }

            for (int numeroDePersonal : List.of(1001, 1002)) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM academico WHERE numeroDePersonal = " + numeroDePersonal)
                        .executeUpdate();
            }

            conexionBaseDeDatos
                    .prepareStatement("DELETE FROM usuario WHERE idUsuario BETWEEN 1000 AND 2000")
                    .executeUpdate();

            UsuarioDTO usuarioAcademico1DTO = new UsuarioDTO(0, "Nombre", "Prueba", 1);
            UsuarioDTO usuarioAcadmico2DTO = new UsuarioDTO(0, "Usuario2", "Apellido2", 1);
            UsuarioDTO usuarioAcademico3DTO = new UsuarioDTO(0, "Usuario3", "Apellido3", 1);

            int idUsuarioAcademico1 = usuarioDAO.insertarUsuario(usuarioAcademico1DTO);
            int idUsuarioAcademico2 = usuarioDAO.insertarUsuario(usuarioAcadmico2DTO);
            int idUsuarioAcademico3 = usuarioDAO.insertarUsuario(usuarioAcademico3DTO);

            IDS_USUARIOS_INSERTADOS.addAll(List.of(idUsuarioAcademico1, idUsuarioAcademico2, idUsuarioAcademico3));

            academicoDAO.insertarAcademico(new AcademicoDTO(1001, idUsuarioAcademico1,
                    usuarioAcademico1DTO.getNombre(), usuarioAcademico1DTO.getApellido(),
                    usuarioAcademico1DTO.getEstado()));

            academicoDAO.insertarAcademico(new AcademicoDTO(1002, idUsuarioAcademico2,
                    usuarioAcadmico2DTO.getNombre(), usuarioAcadmico2DTO.getApellido(),
                    usuarioAcadmico2DTO.getEstado()));

            NUMEROS_DE_PERSONAL_INSERTADOS.addAll(List.of(1001, 1002));

            periodoDAO.crearNuevoPeriodo(new PeriodoDTO(1, "Periodo 2024", 1,
                    Date.valueOf("2024-01-01"), Date.valueOf("2024-12-31")));
            periodoDAO.crearNuevoPeriodo(new PeriodoDTO(2, "Periodo 2025", 0,
                    Date.valueOf("2025-01-01"), Date.valueOf("2025-12-31")));

            IDS_PERIODOS_INSERTADOS.addAll(List.of(1, 2));

            grupoDAO.crearNuevoGrupo(new GrupoDTO(40776, "Grupo 1", 1001, 1, 1));
            grupoDAO.crearNuevoGrupo(new GrupoDTO(40789, "Grupo 2", 1002, 2, 1));
            grupoDAO.crearNuevoGrupo(new GrupoDTO(40889, "Grupo 3", 1002, 1, 1));

            IDS_GRUPOS_INSERTADOS.addAll(List.of(40776, 40789, 40889));

        } catch (SQLException | IOException e) {

            fail("Error en @BeforeEach al preparar datos: " + e);
        }
    }

    @AfterEach
    void limpiarDatosDePrueba() {

        try {

            for (int NRCGrupo : IDS_GRUPOS_INSERTADOS) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM Grupo WHERE NRC = " + NRCGrupo)
                        .executeUpdate();
            }

            for (int idPeriodo : IDS_PERIODOS_INSERTADOS) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM periodo WHERE idPeriodo = " + idPeriodo).executeUpdate();
            }

            for (int numeroDePersonal : NUMEROS_DE_PERSONAL_INSERTADOS) {

                conexionBaseDeDatos
                        .prepareStatement("DELETE FROM academico WHERE numeroDePersonal = " + numeroDePersonal)
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
    void crearNuevoGrupoConDatosValidos() {

        GrupoDTO grupoDTO = new GrupoDTO(40790, "Grupo 3", 1002, 2, 1);

        try {

            boolean resultadoPrueba = grupoDAO.crearNuevoGrupo(grupoDTO);
            assertTrue(resultadoPrueba, "El grupo debería haberse creado correctamente.");

            IDS_GRUPOS_INSERTADOS.add(grupoDTO.getNRC());

        } catch (SQLException | IOException e) {

            fail("Error al crear nuevo grupo: " + e);
        }
    }

    @Test
    void crearNuevoGrupoConAcademicoYPeriodoInexistentes() {

        GrupoDTO grupoDTO = new GrupoDTO(40791, "Grupo 4", 9999, 3, 1);

        assertThrows(SQLException.class, () -> {grupoDAO.crearNuevoGrupo(grupoDTO);});
    }

    @Test
    void eliminarGrupoPorNRCExistente() {

        int NRCGrupo = 40776;

        try {

            boolean resultadoPrueba = grupoDAO.eliminarGrupoPorNRC(NRCGrupo);
            assertTrue(resultadoPrueba, "El grupo debería haberse eliminado correctamente.");

        } catch (SQLException | IOException e) {

            fail("Error al eliminar el grupo: " + e);
        }
    }

    @Test
    void eliminarGrupoPorNRCInexistente() {

        int NRCGrupoInvalido = 9999;

        try {

            boolean resultadoPrueba = grupoDAO.eliminarGrupoPorNRC(NRCGrupoInvalido);
            assertFalse(resultadoPrueba, "No debería poder eliminar un grupo inexistente.");

        } catch (SQLException | IOException e) {

            fail("Error al eliminar el grupo: " + e);
        }
    }

    @Test
    void eliminarGrupoPorPeriodoExistente() {

        int idPeriodo = 1;

        try {

            boolean resultadoPrueba = grupoDAO.eliminarGruposPorPeriodo(idPeriodo);
            assertTrue(resultadoPrueba, "Los grupos del periodo deberían haberse eliminado correctamente.");

        } catch (SQLException | IOException e) {

            fail("Error al eliminar grupos por periodo: " + e);
        }
    }

    @Test
    void eliminarGrupoPorPeriodoInexistente() {

        int idPeriodoInvalido = 9999;

        try {

            boolean resultadoPrueba = grupoDAO.eliminarGruposPorPeriodo(idPeriodoInvalido);
            assertFalse(resultadoPrueba, "No debería poder eliminar grupos de un periodo inexistente.");

        } catch (SQLException | IOException e) {

            fail("Error al eliminar grupos por periodo: " + e);
        }
    }

    @Test
    void modificarGrupoConDatosValidos() {

        GrupoDTO grupoDTO = new GrupoDTO(40776, "Grupo 1 Modificado", 1001, 2, 1);

        try {

            boolean resultadoPrueba = grupoDAO.modificarGrupo(grupoDTO);
            assertTrue(resultadoPrueba, "El grupo debería haberse modificado correctamente.");

        } catch (SQLException | IOException e) {

            fail("Error al modificar el grupo: " + e);
        }
    }

    @Test
    void modificarGrupoPorNRCInexistente() {

        GrupoDTO grupoDTO = new GrupoDTO(9999, "Grupo Inexistente", 1001, 2, 1);

        try {

            boolean resultadoPrueba = grupoDAO.modificarGrupo(grupoDTO);
            assertFalse(resultadoPrueba, "No debería poder modificar un grupo inexistente.");

        } catch (SQLException | IOException e) {

            fail("Error al modificar el grupo inexistente: " + e);
        }
    }

    @Test
    void modificarGrupoConDatosInvalidos() {

        GrupoDTO grupoDTO = new GrupoDTO(40776, "Grupo 1 Modificado", 9999, 2, 1);

        assertThrows(SQLException.class, () -> {grupoDAO.modificarGrupo(grupoDTO);});
    }

    @Test
    void buscarGrupoPorNRCExistente() {

        int NRCGrupo = 40776;
        GrupoDTO grupoEsperado = new GrupoDTO(NRCGrupo, "Grupo 1", 1001, 1, 1);

        try {

            GrupoDTO grupoEncontrado = grupoDAO.buscarGrupoPorNRC(NRCGrupo);
            assertEquals(grupoEsperado, grupoEncontrado,
                    "El grupo encontrado debería coincidir con el esperado.");

        } catch (SQLException | IOException e) {

            fail("Error al buscar el grupo por NRC: " + e);
        }
    }

    @Test
    void buscarGrupoPorNRCInexistente() {

        int NRCGrupoInvalido = 9999;
        GrupoDTO grupoEsperado =  new GrupoDTO(-1, "N/A", -1, -1, -1);;

        try {

            GrupoDTO grupoEncontrado = grupoDAO.buscarGrupoPorNRC(NRCGrupoInvalido);
            assertEquals(grupoEsperado, grupoEncontrado,
                    "No debería encontrar un grupo con NRC inexistente.");

        } catch (SQLException | IOException e) {

            fail("Error al buscar el grupo por NRC inexistente: " + e);
        }
    }

    @Test
    void buscarGrupoActivoPorNumeroDePersonalExistente() {

        int numeroDePersonal = 1001;
        GrupoDTO grupoEsperado = new GrupoDTO(40776, "Grupo 1", numeroDePersonal, 1, 1);

        try {

            GrupoDTO grupoEncontrado = grupoDAO.buscarGrupoActivoPorNumeroDePersonal(numeroDePersonal);
            assertEquals(grupoEsperado, grupoEncontrado, "El grupo encontrado debería coincidir con el esperado.");

        } catch (SQLException | IOException e) {

            fail("Error al buscar el grupo por número de personal: " + e);
        }
    }

    @Test
    void buscarGrupoActivoPorNumeroDePersonalInexistente() {

        int numeroDePersonalInvalido = 9999;
        GrupoDTO grupoEsperado = new GrupoDTO(-1, "N/A", -1, -1, -1);

        try {

            GrupoDTO grupoEncontrado = grupoDAO.buscarGrupoActivoPorNumeroDePersonal(numeroDePersonalInvalido);
            assertEquals(grupoEsperado, grupoEncontrado,
                    "No debería encontrar un grupo con número de personal inexistente.");

        } catch (SQLException | IOException e) {

            fail("Error al buscar el grupo por número de personal inexistente: " + e);
        }
    }

    @Test
    void mostrarGruposActivosConDatosEnLaBase() {

        List<GrupoDTO> gruposActivosEsperados = List.of(

                new GrupoDTO(40776, "Grupo 1", 1001, 1, 1),
                new GrupoDTO(40789, "Grupo 2", 1002, 2, 1),
                new GrupoDTO(40889, "Grupo 3", 1002, 1, 1)
        );

        try {

            List<GrupoDTO> gruposActivosEncontrados = grupoDAO.mostrarGruposActivos();

            for (int grupoDTO = 0; grupoDTO < gruposActivosEsperados.size(); grupoDTO++) {

                assertEquals(gruposActivosEsperados.get(grupoDTO), gruposActivosEncontrados.get(grupoDTO),
                        "El grupo activo esperado debería ser igual al grupo activo encontrado.");
            }

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }

    @Test
    void mostrarGruposActivosSinDatosEnLaBase() {

        try {

            conexionBaseDeDatos.prepareStatement("DELETE FROM Grupo").executeUpdate();

            List<GrupoDTO> gruposActivosEncontrados = grupoDAO.mostrarGruposActivos();
            assertTrue(gruposActivosEncontrados.isEmpty(), "No debería haber grupos activos.");

        } catch (SQLException | IOException e) {

            fail("Error al mostrar grupos activos sin datos: " + e);
        }
    }

    @Test
    void mostrarGruposActivosEnPeriodoActivo() {

        List<GrupoDTO> gruposActivosEsperados = List.of(

                new GrupoDTO(40776, "Grupo 1", 1001, 1, 1),
                new GrupoDTO(40889, "Grupo 3", 1002, 1, 1)
        );

        try {

            List<GrupoDTO> gruposActivosEncontrados = grupoDAO.mostrarGruposActivosEnPeriodoActivo();

            for (int grupoDTO = 0; grupoDTO < gruposActivosEsperados.size(); grupoDTO++) {

                assertEquals(gruposActivosEsperados.get(grupoDTO), gruposActivosEncontrados.get(grupoDTO),
                        "El grupo activo esperado debería ser igual al grupo activo encontrado.");
            }

        } catch (SQLException | IOException e) {

            fail("Error al mostrar grupos activos en periodo activo: " + e);
        }
    }

    @Test
    void mostrarGruposActivosSinPeriodoActivo() {

        try {

            conexionBaseDeDatos.prepareStatement("UPDATE periodo SET estadoActivo = 0").executeUpdate();

            List<GrupoDTO> gruposActivosEncontrados = grupoDAO.mostrarGruposActivosEnPeriodoActivo();
            assertTrue(gruposActivosEncontrados.isEmpty(),
                    "No debería haber grupos activos en periodo inactivo.");

        } catch (SQLException | IOException e) {

            fail("Error al mostrar grupos activos en periodo inactivo: " + e);
        }
    }

    @Test
    void mostrarGruposActivosEnPeriodoActivoSinDatos() {

        try {

            conexionBaseDeDatos.prepareStatement("DELETE FROM Grupo").executeUpdate();

            List<GrupoDTO> gruposActivosEncontrados = grupoDAO.mostrarGruposActivosEnPeriodoActivo();
            assertTrue(gruposActivosEncontrados.isEmpty(),
                    "No debería haber grupos activos en periodo activo.");

        } catch (SQLException | IOException e) {

            fail("Error al mostrar grupos activos en periodo activo sin datos: " + e);
        }
    }

    @Test
    void existeGrupoPorNumeroDePersonalExistente() {

        int numeroDePersonal = 1001;

        try {

            boolean existeGrupo = grupoDAO.existeGrupoPorNumeroAcademico(numeroDePersonal);
            assertTrue(existeGrupo, "Debería existir un grupo para el número de personal " + numeroDePersonal);

        } catch (SQLException | IOException e) {

            fail("Error al verificar la existencia de un grupo por número de personal: " + e);
        }
    }

    @Test
    void existeGrupoPorNumeroDePersonalInexistente() {

        int numeroDePersonalInvalido = 9999;

        try {

            boolean existeGrupo = grupoDAO.existeGrupoPorNumeroAcademico(numeroDePersonalInvalido);
            assertFalse(existeGrupo, "No debería existir un grupo para el número de personal " +
                    numeroDePersonalInvalido);

        } catch (SQLException | IOException e) {

            fail("Error al verificar la existencia de un grupo por número de personal inexistente: " + e);
        }
    }

    @Test
    void generarNRCConDatosEnLaBase() {

        int NRCEsperado = 40890;

        try {

            int nuevoNRC = grupoDAO.generarNRC();

            assertEquals(NRCEsperado, nuevoNRC,
                    "El NRC generado debería ser 40690 basado en los datos preparados.");

        } catch (SQLException | IOException e) {

            fail("No se esperaba una excepción: " + e);
        }
    }
}
