package logica.verificacion;

import java.util.ArrayList;
import java.util.List;

public class VerificacionDeProyecto {

    public List<String> camposVaciosProyecto( String campoNombre,
                                             String campoDescripcionGeneral,
                                             String campoObjetivosGenerales,
                                             String campoObjetivosInmediatos,
                                             String campoObjetivosMediatos,
                                             String campoMetodologia,
                                             String campoRecursos,
                                             String campoActividades,
                                             String campoResponsabilidades,
                                             String campoDias) {

        List<String> errores = new ArrayList<>();

        if (campoNombre.isEmpty()) {
            errores.add("El campo de nombre no puede estar vacío.");
        }

        if (campoDescripcionGeneral.isEmpty()) {
            errores.add("El campo descripcion general no puede estar vacío.");
        }

        if (campoObjetivosGenerales.isEmpty()) {
            errores.add("El campo de objetivos generales no puede estar vacío.");
        }

        if (campoObjetivosInmediatos.isEmpty()) {
            errores.add("El campo de objetivos inmediatos no puede estar vacío.");
        }

        if (campoObjetivosMediatos.isEmpty()) {
            errores.add("El campo de objetivos mediatos no puede estar vacío.");
        }

        if (campoMetodologia.isEmpty()) {
            errores.add("El campo de metodologia no puede estar vacío.");
        }

        if (campoRecursos.isEmpty()) {
            errores.add("El campo de recursos no puede estar vacío.");
        }

        if (campoActividades.isEmpty()) {
            errores.add("El campo de actividades no puede estar vacío.");
        }

        if (campoResponsabilidades.isEmpty()) {
            errores.add("El campo de responsabilidades no puede estar vacío.");
        }

        if (campoDias.isEmpty()) {
            errores.add("El campo de dias no puede estar vacío.");
        }

        return errores;
    }

    public boolean validarTextoProyecto(String texto) {

        String[] caracteres = texto.trim().split("\\s+");
        return caracteres.length >= 1 && texto.length() <= 255;
    }

    public List<String> validarCamposProyecto( String campoNombre,
                                              String campoDescripcionGeneral,
                                              String campoObjetivosGenerales,
                                              String campoObjetivosInmediatos,
                                              String campoObjetivosMediatos,
                                              String campoMetodologia,
                                              String campoRecursos,
                                              String campoActividades,
                                              String campoResponsabilidades,
                                              String campoDias) {

        List<String> errores = new ArrayList<>();

        if (!validarTextoProyecto(campoNombre)) {
            errores.add("El campo de nombre debe tener una o más palabras " +
                    "y no exceder 150 caracteres.");
        }

        if (!validarTextoProyecto(campoDescripcionGeneral)) {
            errores.add("El campo de descripcion general debe tener una o más palabras " +
                    "y no exceder 255 caracteres.");
        }

        if (!validarTextoProyecto(campoObjetivosGenerales)) {
            errores.add("El campo de objetivos generales debe tener una o más palabras " +
                    "y no exceder 255 caracteres.");
        }

        if (!validarTextoProyecto(campoObjetivosInmediatos)) {
            errores.add("El campo de objetivos inmediatos debe tener una o más palabras " +
                    "y no exceder 255 caracteres.");
        }

        if (!validarTextoProyecto(campoObjetivosMediatos)) {
            errores.add("El campo de objetivos mediatos debe tener una o más palabras " +
                    "y no exceder 255 caracteres.");
        }

        if (!validarTextoProyecto(campoMetodologia)) {
            errores.add("El campo de metodologia debe tener una o más palabras " +
                    "y no exceder 255 caracteres.");
        }

        if (!validarTextoProyecto(campoRecursos)) {
            errores.add("El campo de recursos debe tener una o más palabras " +
                    "y no exceder 255 caracteres.");
        }

        if (!validarTextoProyecto(campoActividades)) {
            errores.add("El campo de actividades debe tener una o más palabras " +
                    "y no exceder 255 caracteres.");
        }

        if (!validarTextoProyecto(campoResponsabilidades)) {
            errores.add("El campo de responsabilidades debe tener una o más palabras " +
                    "y no exceder 255 caracteres.");
        }

        if (!validarTextoProyecto(campoDias)) {
            errores.add("El campo de dias debe tener una o más palabras " +
                    "y no exceder 255 caracteres.");
        }

        return errores;
    }
}
