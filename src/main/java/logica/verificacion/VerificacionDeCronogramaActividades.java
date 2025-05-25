package logica.verificacion;

import java.util.ArrayList;
import java.util.List;

public class VerificacionDeCronogramaActividades {

    public List<String> camposVaciosCronogramaActividades(String agostoFebrero,
                                                                 String septiembreMarzo,
                                                                 String octubreAbril,
                                                                 String noviembreMayo) {

        List<String> errores = new ArrayList<>();

        if (agostoFebrero.isEmpty()) {
            errores.add("El campo de actividades para agosto-febrero no puede estar vacío.");
        }

        if (septiembreMarzo.isEmpty()) {
            errores.add("El campo de actividades para septiembre-marzo no puede estar vacío.");
        }

        if (octubreAbril.isEmpty()) {
            errores.add("El campo de actividades para octubre-abril no puede estar vacío.");
        }

        if (noviembreMayo.isEmpty()) {
            errores.add("El campo de actividades para noviembre-mayo no puede estar vacío.");
        }

        return errores;
    }

    public boolean validarTextoCronogramaActividades(String texto) {

        String[] caracteres = texto.trim().split("\\s+");
        return caracteres.length > 3 && texto.length() <= 255;
    }

    public List<String> validarCamposCronograma(String agostoFebrero, String septiembreMarzo,
                                                String octubreAbril, String noviembreMayo) {

        List<String> errores = new ArrayList<>();

        if (!validarTextoCronogramaActividades(agostoFebrero)) {
            errores.add("El campo de actividades para agosto-febrero " +
                    "debe tener más de 3 palabras y no exceder 255 caracteres.");
        }
        if (!validarTextoCronogramaActividades(septiembreMarzo)) {
            errores.add("El campo de actividades para septiembre-marzo " +
                    "debe tener más de 3 palabras y no exceder 255 caracteres.");
        }
        if (!validarTextoCronogramaActividades(octubreAbril)) {
            errores.add("El campo de actividades para octubre-abril " +
                    "debe tener más de 3 palabras y no exceder 255 caracteres.");
        }
        if (!validarTextoCronogramaActividades(noviembreMayo)) {
            errores.add("El campo de actividades para noviembre-mayo debe " +
                    "tener más de 3 palabras y no exceder 255 caracteres.");
        }

        return errores;
    }
}
