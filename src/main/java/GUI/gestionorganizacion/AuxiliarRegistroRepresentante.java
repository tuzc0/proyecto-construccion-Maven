package GUI.gestionorganizacion;

import GUI.utilidades.Utilidades;
import logica.DAOs.RepresentanteDAO;
import logica.DTOs.RepresentanteDTO;
import logica.ManejadorExcepciones;
import logica.VerificacionUsuario;
import logica.interfaces.IGestorAlertas;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AuxiliarRegistroRepresentante {

    Logger LOGGER =
            org.apache.logging.log4j.LogManager.getLogger(AuxiliarRegistroRepresentante.class);

    private Utilidades gestorVentanas = new Utilidades();

    private IGestorAlertas utilidades = new Utilidades();

    private ManejadorExcepciones manejadorExcepciones = new ManejadorExcepciones(utilidades, LOGGER);


    public void registrarRepresentante(String nombre, String apellidos, String correo, String numeroContacto, int idOrganizacion) {


        VerificacionUsuario verificacionUsuario = new VerificacionUsuario();
        int estadoActivo = 1;
        int idRepresentante = 0;

        List<String> errores = verificacionUsuario.validarRepresentante(nombre, apellidos, numeroContacto, correo);

        if (!errores.isEmpty()) {

            String mensajeError = String.join("\n", errores);
            utilidades.mostrarAlerta("Error de validación",
                    "Datos inválidos",
                    mensajeError);
            return;
        }

        RepresentanteDTO representante =
                new RepresentanteDTO(idRepresentante, correo, numeroContacto, nombre, apellidos, idOrganizacion, estadoActivo);
        RepresentanteDAO representanteDAO = new RepresentanteDAO();


        try {

            RepresentanteDTO representanteExistente = representanteDAO.buscarRepresentantePorCorreo(correo);

            if (representanteExistente.getCorreo() != ("N/A")) {

                utilidades.mostrarAlerta("Error",
                        "Correo ya registrado",
                        "El correo electrónico ya está asociado a otro representante.");
                return;
            }

            representanteExistente = representanteDAO.buscarRepresentantePorTelefono(numeroContacto);

            if (representanteExistente.getTelefono() != ("N/A")) {

                utilidades.mostrarAlerta("Error",
                        "Número de contacto ya registrado",
                        "El número de contacto ya está asociado a otro representante.");
                return;
            }

            representanteDAO.insertarRepresentante(representante);

            utilidades.mostrarAlerta("Éxito",
                    "Registro exitoso",
                    "El representante se ha registrado correctamente.");

        } catch (IOException e) {

            manejadorExcepciones.manejarIOException(e);


        } catch (SQLException e) {

            manejadorExcepciones.manejarSQLException(e);

        } catch (Exception e) {

            utilidades.mostrarAlerta("Error",
                    "Error al registrar el representante",
                    "No se pudo registrar el representante.");

            LOGGER.error("Error al registrar el representante: " + e.getMessage(), e);
        }
    }
}
