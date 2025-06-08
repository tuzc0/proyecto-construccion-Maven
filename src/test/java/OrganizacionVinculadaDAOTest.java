import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.OrganizacionVinculadaDAO;
import logica.DTOs.OrganizacionVinculadaDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrganizacionVinculadaDAOTest {

    private OrganizacionVinculadaDAO organizacionDAO;
    private Connection conexionBaseDeDatos;
    private final List<Integer> IDS_ORGANIZACIONES_INSERTADAS = new ArrayList<>();

    @BeforeAll
    void prepararDatosDePrueba() {

        try {

            organizacionDAO = new OrganizacionVinculadaDAO();
            conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection();

            PreparedStatement limpiarTabla = conexionBaseDeDatos.prepareStatement(
                    "DELETE FROM organizacionvinculada WHERE idOV BETWEEN 1000 AND 2000");
            limpiarTabla.executeUpdate();
            limpiarTabla.close();

        } catch (SQLException | IOException e) {

            fail("Error al preparar datos de prueba: " + e);
        }
    }

    @AfterAll
    void limpiarDatosDePrueba() {

        try {

            for (int idOrganizacion : IDS_ORGANIZACIONES_INSERTADAS) {

                PreparedStatement eliminar = conexionBaseDeDatos.prepareStatement(
                        "DELETE FROM organizacionvinculada WHERE idOV = ?");

                eliminar.setInt(1, idOrganizacion);
                eliminar.executeUpdate();
                eliminar.close();
            }

            conexionBaseDeDatos.close();

        } catch (SQLException e) {
            fail("Error al limpiar datos: " + e);
        }
    }

    @Test
    void crearOrganizacionVinculadaConDatosValidos() throws SQLException, IOException {
        OrganizacionVinculadaDTO org = new OrganizacionVinculadaDTO(0, "Empresa Test", "Calle Falsa 123","test@empresa.com",
                "5551234567", 1);
        int idGenerado = organizacionDAO.crearNuevaOrganizacion(org);
        assertTrue(idGenerado > 0, "Debería retornar ID válido");
        IDS_ORGANIZACIONES_INSERTADAS.add(idGenerado);
    }

    @Test
    void crearOrganizacionVinculadaConDatosInvalidos() {
        OrganizacionVinculadaDTO orgInvalida = new OrganizacionVinculadaDTO(0, null, null, null, null, 1);
        assertThrows(SQLException.class, () -> organizacionDAO.crearNuevaOrganizacion(orgInvalida),
                "Debería lanzar excepción con datos inválidos");
    }

    @Test
    void eliminarOrganizacionConIDValido() throws SQLException, IOException {
        OrganizacionVinculadaDTO org = new OrganizacionVinculadaDTO(0, "Empresa Eliminar", "Calle Falsa 123","eliminar@empresa.com",
                "5551234567", 1);
        int idGenerado = organizacionDAO.crearNuevaOrganizacion(org);
        IDS_ORGANIZACIONES_INSERTADAS.add(idGenerado);

        boolean resultado = organizacionDAO.eliminarOrganizacionPorID(idGenerado);
        assertTrue(resultado, "Debería eliminar correctamente");
    }

    @Test
    void eliminarOrganizacionConIDInvalido() throws SQLException, IOException {
        boolean resultado = organizacionDAO.eliminarOrganizacionPorID(-1);
        assertFalse(resultado, "No debería eliminar con ID inválido");
    }

    @Test
    void eliminarOrganizacionInexistente() throws SQLException, IOException {
        boolean resultado = organizacionDAO.eliminarOrganizacionPorID(99999);
        assertFalse(resultado, "No debería eliminar organización inexistente");
    }

    @Test
    void modificarOrganizacionConDatosValidos() throws SQLException, IOException {
        OrganizacionVinculadaDTO org = new OrganizacionVinculadaDTO(0, "Empresa Modificar", "Calle Falsa 123","modificar@empresa.com",
                "5551234567", 1);
        int idGenerado = organizacionDAO.crearNuevaOrganizacion(org);
        IDS_ORGANIZACIONES_INSERTADAS.add(idGenerado);

        OrganizacionVinculadaDTO orgModificada = new OrganizacionVinculadaDTO(idGenerado, "Empresa Modificada", "Calle Verdadera 456",
                "modificado@empresa.com", "5557654321",1);

        boolean resultado = organizacionDAO.modificarOrganizacion(orgModificada);
        assertTrue(resultado, "Debería modificar correctamente");
    }

    @Test
    void modificarOrganizacionConDatosInvalidos() throws SQLException, IOException {
        OrganizacionVinculadaDTO orgInvalida = new OrganizacionVinculadaDTO(1, null, null, null, null, 1);
        boolean resultado = organizacionDAO.modificarOrganizacion(orgInvalida);
        assertFalse(resultado, "No debería modificar con datos inválidos");
    }

    @Test
    void modificarOrganizacionInexistente() throws SQLException, IOException {
        OrganizacionVinculadaDTO orgInexistente = new OrganizacionVinculadaDTO(99999, "Inexistente",
                "inexistente@empresa.com", "5550000000", "Dirección", 1);
        boolean resultado = organizacionDAO.modificarOrganizacion(orgInexistente);
        assertFalse(resultado, "No debería modificar organización inexistente");
    }

    @Test
    void buscarOrganizacionPorIDValido() throws SQLException, IOException {
        OrganizacionVinculadaDTO org = new OrganizacionVinculadaDTO(0, "Empresa Buscar", "Calle Falsa 123","buscar@empresa.com",
                "5551234567", 1);
        int idGenerado = organizacionDAO.crearNuevaOrganizacion(org);
        IDS_ORGANIZACIONES_INSERTADAS.add(idGenerado);

        OrganizacionVinculadaDTO orgEsperada = new OrganizacionVinculadaDTO(idGenerado, "Empresa Buscar",
                "buscar@empresa.com", "5551234567", "Calle Falsa 123", 1);

        OrganizacionVinculadaDTO orgEncontrada = organizacionDAO.buscarOrganizacionPorID(idGenerado);
        assertEquals(orgEsperada, orgEncontrada, "Debería encontrar la organización correcta");
    }

    @Test
    void buscarOrganizacionPorIDInvalido() throws SQLException, IOException {
        OrganizacionVinculadaDTO orgEsperada = new OrganizacionVinculadaDTO(-1, "N/A", "N/A", "N/A", "N/A", 0);
        OrganizacionVinculadaDTO orgEncontrada = organizacionDAO.buscarOrganizacionPorID(-1);
        assertEquals(orgEsperada, orgEncontrada, "Debería retornar organización por defecto");
    }

    @Test
    void buscarOrganizacionInexistentePorID() throws SQLException, IOException {
        OrganizacionVinculadaDTO orgEsperada = new OrganizacionVinculadaDTO(-1, "N/A", "N/A", "N/A", "N/A", 0);
        OrganizacionVinculadaDTO orgEncontrada = organizacionDAO.buscarOrganizacionPorID(99999);
        assertEquals(orgEsperada, orgEncontrada, "Debería retornar organización por defecto");
    }

    @Test
    void buscarOrganizacionPorCorreoValido() throws SQLException, IOException {
        OrganizacionVinculadaDTO org = new OrganizacionVinculadaDTO(0, "Empresa Correo","Calle Falsa 123", "correo@empresa.com",
                "5551234567",  1);
        int idGenerado = organizacionDAO.crearNuevaOrganizacion(org);
        IDS_ORGANIZACIONES_INSERTADAS.add(idGenerado);

        OrganizacionVinculadaDTO orgEsperada = new OrganizacionVinculadaDTO(idGenerado, "Empresa Correo",
                "correo@empresa.com", "5551234567", "Calle Falsa 123", 1);

        OrganizacionVinculadaDTO orgEncontrada = organizacionDAO.buscarOrganizacionPorCorreo("correo@empresa.com");
        assertEquals(orgEsperada, orgEncontrada, "Debería encontrar la organización correcta");
    }

    @Test
    void buscarOrganizacionPorCorreoInvalido() throws SQLException, IOException {
        OrganizacionVinculadaDTO orgEsperada = new OrganizacionVinculadaDTO(-1, "N/A", "N/A", "N/A", "N/A", 0);
        OrganizacionVinculadaDTO orgEncontrada = organizacionDAO.buscarOrganizacionPorCorreo(null);
        assertEquals(orgEsperada, orgEncontrada, "Debería retornar organización por defecto");
    }

    @Test
    void buscarOrganizacionInexistentePorCorreo() throws SQLException, IOException {
        OrganizacionVinculadaDTO orgEsperada = new OrganizacionVinculadaDTO(-1, "N/A", "N/A", "N/A", "N/A", 0);
        OrganizacionVinculadaDTO orgEncontrada = organizacionDAO.buscarOrganizacionPorCorreo("noexiste@empresa.com");
        assertEquals(orgEsperada, orgEncontrada, "Debería retornar organización por defecto");
    }

    @Test
    void buscarOrganizacionPorTelefonoValido() throws SQLException, IOException {
        OrganizacionVinculadaDTO org = new OrganizacionVinculadaDTO(0, "Empresa Teléfono", "Calle Falsa 123","telefono@empresa.com",
                "5559876543", 1);
        int idGenerado = organizacionDAO.crearNuevaOrganizacion(org);
        IDS_ORGANIZACIONES_INSERTADAS.add(idGenerado);

        OrganizacionVinculadaDTO orgEsperada = new OrganizacionVinculadaDTO(idGenerado, "Empresa Teléfono",
                "telefono@empresa.com", "5559876543", "Calle Falsa 123", 1);

        OrganizacionVinculadaDTO orgEncontrada = organizacionDAO.buscarOrganizacionPorTelefono("5559876543");
        assertEquals(orgEsperada, orgEncontrada, "Debería encontrar la organización correcta");
    }

    @Test
    void buscarOrganizacionPorTelefonoInvalido() throws SQLException, IOException {
        OrganizacionVinculadaDTO orgEsperada = new OrganizacionVinculadaDTO(-1, "N/A", "N/A", "N/A", "N/A", 0);
        OrganizacionVinculadaDTO orgEncontrada = organizacionDAO.buscarOrganizacionPorTelefono(null);
        assertEquals(orgEsperada, orgEncontrada, "Debería retornar organización por defecto");
    }

    @Test
    void buscarOrganizacionInexistentePorTelefono() throws SQLException, IOException {
        OrganizacionVinculadaDTO orgEsperada = new OrganizacionVinculadaDTO(-1, "N/A", "N/A", "N/A", "N/A", 0);
        OrganizacionVinculadaDTO orgEncontrada = organizacionDAO.buscarOrganizacionPorTelefono("0000000000");
        assertEquals(orgEsperada, orgEncontrada, "Debería retornar organización por defecto");
    }

    @Test
    void obtenerTodasLasOrganizacionesConDatos() throws SQLException, IOException {

        PreparedStatement limpiar = conexionBaseDeDatos.prepareStatement(
                "DELETE FROM organizacionvinculada WHERE idOV BETWEEN 1000 AND 2000");
        limpiar.executeUpdate();
        limpiar.close();


        OrganizacionVinculadaDTO org1 = new OrganizacionVinculadaDTO(0, "Empresa 1", "Dirección 1","empresa1@test.com",
                "5551111111", 1);
        OrganizacionVinculadaDTO org2 = new OrganizacionVinculadaDTO(0, "Empresa 2", "Dirección 2", "empresa2@test.com",
                "5552222222",  1);

        int id1 = organizacionDAO.crearNuevaOrganizacion(org1);
        int id2 = organizacionDAO.crearNuevaOrganizacion(org2);
        IDS_ORGANIZACIONES_INSERTADAS.add(id1);
        IDS_ORGANIZACIONES_INSERTADAS.add(id2);

        List<OrganizacionVinculadaDTO> organizaciones = organizacionDAO.obtenerTodasLasOrganizaciones();
        assertTrue(organizaciones.size() >= 2, "Debería obtener al menos 2 organizaciones");
    }

    @Test
    void obtenerTodasLasOrganizacionesSinDatos() throws SQLException, IOException {

        PreparedStatement limpiar = conexionBaseDeDatos.prepareStatement(
                "UPDATE organizacionvinculada SET estadoActivo = 0");
        limpiar.executeUpdate();
        limpiar.close();

        List<OrganizacionVinculadaDTO> organizaciones = organizacionDAO.obtenerTodasLasOrganizaciones();
        assertTrue(organizaciones.isEmpty(), "La lista debería estar vacía");
    }
}