package GUI.gestionorganizacion;

import GUI.utilidades.Utilidades;
import logica.DAOs.RepresentanteDAO;
import logica.DTOs.RepresentanteDTO;
import logica.VerificacionUsuario;

import java.io.IOException;
import java.sql.SQLException;

public class AuxiliarRegistroRepresentante {


    public void registrarRepresentante(String nombre, String apellidos, String correo, String numeroContacto, int idOrganizacion) {

        Utilidades utilidades = new Utilidades();
        VerificacionUsuario verificacionUsuario = new VerificacionUsuario();
        int estadoActivo = 1;
        int idRepresentante = 0;


        if (nombre.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || numeroContacto.isEmpty()) {
            utilidades.mostrarAlerta("Error", "campos vacios.", "Por favor, complete todos los campos.");
            return;
        }

        if (!verificacionUsuario.nombreValido(nombre)) {
            utilidades.mostrarAlerta("Error", "Nombre inválido", "El nombre solo puede contener letras y espacios.");
            return;

        }

        if (!verificacionUsuario.apellidosValidos(apellidos)) {
            utilidades.mostrarAlerta("Error", "Apellidos inválidos", "Los apellidos solo pueden contener letras y espacios.");
            return;

        }

        if (!verificacionUsuario.correoValido(correo)) {
            utilidades.mostrarAlerta("Error", "Correo inválido", "El correo electrónico no es válido.");
            return;

        }

        RepresentanteDTO representante = new RepresentanteDTO(idRepresentante, nombre, apellidos, correo, numeroContacto, idOrganizacion, estadoActivo);
        RepresentanteDAO representanteDAO = new RepresentanteDAO();


        try {

            RepresentanteDTO representanteExistente = representanteDAO.buscarRepresentantePorCorreo(correo);

            if (representanteExistente.getCorreo() != ("N/A")) {
                utilidades.mostrarAlerta("Error", "Correo ya registrado", "El correo electrónico ya está asociado a otro representante.");
                return;
            }

            representanteExistente = representanteDAO.buscarRepresentantePorTelefono(numeroContacto);

            if (representanteExistente.getTelefono() != ("N/A")) {
                utilidades.mostrarAlerta("Error", "Número de contacto ya registrado", "El número de contacto ya está asociado a otro representante.");
                return;
            }

            representanteDAO.insertarRepresentante(representante);
            utilidades.mostrarAlerta("Éxito", "Registro exitoso", "El representante se ha registrado correctamente.");

        } catch (IOException e) {

            utilidades.mostrarAlerta("Error", "Error de conexión", "No se pudo conectar a la base de datos.");

        } catch (SQLException e) {

            utilidades.mostrarAlerta("Error", "Error al registrar el representante", "No se pudo registrar el representante.");

        } catch (Exception e) {

            utilidades.mostrarAlerta("Error", "Error al registrar el representante", "No se pudo registrar el representante.");
        }
    }
}
