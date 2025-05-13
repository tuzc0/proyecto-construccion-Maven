import accesoadatos.ConexionBaseDeDatos;
import org.junit.jupiter.api.*;
import logica.DAOs.AcademicoDAO;
import logica.DTOs.AcademicoDTO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AcademicoDAOTest {

    private AcademicoDAO academicoDAO;

    @BeforeAll
    void setUp() {

        academicoDAO = new AcademicoDAO();
    }

    @AfterAll
    public void borrarDatos() throws SQLException, IOException {

        List<Integer> createdAcademics = List.of(22222, 12348);


        for (int numeroDePersonal : createdAcademics) {
            String deleteSQL = "DELETE FROM academico WHERE numeroDePersonal = ?";
            try (var statement = new ConexionBaseDeDatos().getConnection().prepareStatement(deleteSQL)) {
                statement.setInt(1, numeroDePersonal);
                statement.executeUpdate();
            }
        }

    }

    @Test
    void testInsertarAcademicoDatosValidos() {
        AcademicoDTO academico = new AcademicoDTO(22222, 1, "Prueba", "Insercion", 1);

        try {

            boolean resultado = academicoDAO.insertarAcademico(academico);
            assertTrue(resultado, "El académico debería ser insertado correctamente.");

        } catch (SQLException e) {

            fail("No se esperaba una excepción: " + e.getMessage());
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    @Test
    void testEliminarAcademicoPorNumeroDePersonalDatosValidos() {

        String numeroDePersonal = "4";

        try {

            boolean resultado = academicoDAO.eliminarAcademicoPorNumeroDePersonal(Integer.parseInt(numeroDePersonal));
            assertTrue(resultado, "El académico debería ser eliminado correctamente.");

        } catch (SQLException e) {

            fail("No se esperaba una excepción: " + e.getMessage());

        }  catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    @Test
    void testModificarAcademicoDatosValidos() {

        AcademicoDTO academico = new AcademicoDTO(12348, 39, "Carlos", "Lopez", 1);

        try {

            boolean resultado = academicoDAO.modificarAcademico(academico);
            assertTrue(resultado, "El académico debería ser modificado correctamente.");

        } catch (SQLException e) {

            fail("No se esperaba una excepción: " + e.getMessage());

        }  catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    @Test
    void testBuscarAcademicoPorNumeroDePersonalDatosValidos() {

        int numeroDePersonal = 22222;

        try {

            AcademicoDTO academico = academicoDAO.buscarAcademicoPorNumeroDePersonal(numeroDePersonal);
            assertEquals(numeroDePersonal, academico.getNumeroDePersonal(), "El número de personal debería coincidir.");

        } catch (SQLException e) {

            fail("No se esperaba una excepción: " + e.getMessage());

        }  catch (IOException e) {

            throw new RuntimeException(e);
        }
    }
}