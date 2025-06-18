/*
import accesoadatos.ConexionBaseDeDatos;
import logica.DAOs.OrganizacionVinculadaDAO;
import logica.DAOs.RepresentanteDAO;
import logica.DTOs.OrganizacionVinculadaDTO;
import logica.DTOs.RepresentanteDTO;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RepresentanteDAOTest {

    private RepresentanteDAO representanteDAO;
    private OrganizacionVinculadaDAO organizacionDAO;

    private final List<Integer> IDS_ORGANIZACIONES_INSERTADAS = List.of();
    private final List<Integer> IDS_REPRESENTANTES_INSERTADOS = List.of();

    @BeforeEach
    void prepararDatosDePrueba() {

        organizacionDAO = new OrganizacionVinculadaDAO();
        representanteDAO = new RepresentanteDAO();

        IDS_ORGANIZACIONES_INSERTADAS.clear();
        IDS_REPRESENTANTES_INSERTADOS.clear();

        try (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection()) {

            for (int idOrganizacion : List.of(1, 2)) {

                PreparedStatement eliminar = conexionBaseDeDatos.prepareStatement
                        ("DELETE FROM organizacionvinculada WHERE idOV = ?");
                eliminar.setInt(1, idOrganizacion);
                eliminar.executeUpdate();
            }

            for (int idRepresentante : List.of(1, 2, 3, 4)) {

                PreparedStatement eliminar = conexionBaseDeDatos.prepareStatement
                        ("DELETE FROM representante WHERE IDRepresentante = ?");
                eliminar.setInt(1, idRepresentante);
                eliminar.executeUpdate();
            }

            IDS_ORGANIZACIONES_INSERTADAS.add(organizacionDAO.crearNuevaOrganizacion
                    (new OrganizacionVinculadaDTO(1, "Empresa 1", "Dirección 1",
                            "empresa1@test.com", "5551111111", 1)));
            IDS_ORGANIZACIONES_INSERTADAS.add(organizacionDAO.crearNuevaOrganizacion
                    (new OrganizacionVinculadaDTO(2, "Empresa 2", "Dirección 2",
                            "empresa2@test.com", "5552222222", 1)));

            IDS_REPRESENTANTES_INSERTADOS.add(representanteDAO.crearNuevoRepresentante(
                    new RepresentanteDTO(1, "representate1@gmail-com", "5553333333",));
                            "Representante 1", "Apellido 1", IDS_ORGANIZACIONES_INSERTADAS.get(0), 1)));

        } catch (SQLException | IOException e) {

            fail("Error en @BeforeEach al preparar datos: " + e);
        }
    }


    @AfterEach
    void limpiarDatosDePrueba() {

        try (Connection conexionBaseDeDatos = new ConexionBaseDeDatos().getConnection()) {

            for (int idOrganizacion : IDS_ORGANIZACIONES_INSERTADAS) {

                PreparedStatement consultaPreparadaSQL = conexionBaseDeDatos.prepareStatement
                        ("DELETE FROM organizacionvinculada WHERE idOV = ?");
                consultaPreparadaSQL.setInt(1, idOrganizacion);
                consultaPreparadaSQL.executeUpdate();
            }

        } catch (SQLException | IOException e) {

            fail("Error al limpiar datos: " + e);
        }
    }

}
*/