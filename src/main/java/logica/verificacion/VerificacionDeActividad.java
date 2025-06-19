package logica.verificacion;

import java.util.ArrayList;
import java.util.List;

public class VerificacionDeActividad {

    private boolean validarNombreActividad(String campoNombre) {

        String[] palabras = campoNombre.trim().split("\\s+");
        return palabras.length >= 1 && campoNombre.length() <= 255;
    }

    private boolean validarDuracion(String campoDuracion) {

        String[] palabras = campoDuracion.trim().split("\\s+");
        return palabras.length >= 1 && campoDuracion.length() <= 50;
    }

    private boolean validarHitos(String hitos) {

        String[] palabras = hitos.trim().split("\\s+");
        return palabras.length >= 1 && hitos.length() <= 100;
    }

    public List<String> validarCamposVacios(String nombreActividad, String duracionActividad, String hitosActividad) {

        List<String> camposVacios = new ArrayList<>();

        if (nombreActividad == null || nombreActividad.isEmpty()) {

            camposVacios.add("El campo de nombre no puede estar vacío.");
        }

        if (duracionActividad == null || duracionActividad.isEmpty()) {

            camposVacios.add("El campo duración no puede estar vacío");
        }

        if (hitosActividad == null || hitosActividad.isEmpty()) {

            camposVacios.add("El campo hitos no puede estar vacío.");
        }

        return camposVacios;
    }

    public List<String> validarCamposInvalidos( String nombreActividad, String duracionActividad, String hitosActividad) {

        List<String> camposInvalidos = new ArrayList<>();

        if (!validarNombreActividad(nombreActividad)) {

            camposInvalidos.add("El campo de nombre debe tener una o más palabras y no exceder los 255 caracteres.");
        }

        if (!validarDuracion(duracionActividad)) {

            camposInvalidos.add("El campo de duración debe tener minimo una palabra y no exceder los 50 caracteres.");
        }

        if(!validarHitos(hitosActividad)) {

            camposInvalidos.add("El campo de hitos debe tener minimo una palabra y no exceder los 100 caracteres.");
        }

        return camposInvalidos;
    }
}
