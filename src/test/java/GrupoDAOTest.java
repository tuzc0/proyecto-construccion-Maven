import logica.DAOs.GrupoDAO;
import logica.DTOs.GrupoDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GrupoDAOTest {

    private static GrupoDAO grupoDAO;

    @BeforeAll
    static void setUp() throws SQLException, IOException {

        grupoDAO = new GrupoDAO();
    }

    @Test
    @Order(1)
    void testCrearNuevoGrupoDatosValidos() throws SQLException, IOException {

        GrupoDTO grupo = new GrupoDTO(12345, "Grupo A", 1, 1, 1, 1);
        boolean resultado = grupoDAO.crearNuevoGrupo(grupo);
        assertTrue(resultado, "El grupo debería haberse creado correctamente.");
    }

    @Test
    @Order(2)
    void testEliminarGrupoPorNRCDatosValidos() throws SQLException, IOException {

        int NRC = 12345;
        boolean resultado = grupoDAO.eliminarGrupoPorNRC(NRC);
        assertTrue(resultado, "El grupo debería haberse eliminado correctamente.");
    }

    @Test
    @Order(3)
    void testModificarGrupoDatosValidos() throws SQLException, IOException {

        GrupoDTO grupo = new GrupoDTO(12345, "Grupo B", 2, 2, 2, 1);
        boolean resultado = grupoDAO.modificarGrupo(grupo);
        assertTrue(resultado, "El grupo debería haberse modificado correctamente.");
    }

    @Test
    @Order(4)
    void testBuscarGrupoPorNRCDatosValidos() throws SQLException, IOException {

        int NRC = 12345;
        GrupoDTO grupo = grupoDAO.buscarGrupoPorNRC(NRC);
        assertNotNull(grupo, "El grupo no debería ser nulo.");
        assertEquals(NRC, grupo.getNRC(), "El NRC del grupo debería coincidir.");
    }

    @Test
    @Order(5)
    void testMostrarGruposActivosDatosValidos() throws SQLException, IOException {

        GrupoDTO grupo = grupoDAO.mostrarGruposActivos();
        assertNotNull(grupo, "El grupo no debería ser nulo.");
        assertEquals(1, grupo.getEstadoActivo(), "El estado del grupo debería ser activo.");
    }
}
