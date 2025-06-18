package logica.DTOs;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActividadDTO {

    private int IDActividad;
    private String nombre;
    private String duracion;
    private String hitos;
    private Date fechaInicio;
    private Date fechaFin;
    private int estadoActivo;

    public ActividadDTO() {

    }

    public ActividadDTO(int IDActividad, String nombre, String duracion, String hitos, Date fechaInicio,
                        Date fechaFin, int estadoActivo) {

        this.IDActividad = IDActividad;
        this.nombre = nombre;
        this.duracion = duracion;
        this.hitos = hitos;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estadoActivo = estadoActivo;
    }

    public int getIDActividad() {

        return IDActividad;
    }

    public void setIDActividad(int IDActividad) {

        this.IDActividad = IDActividad;
    }

    public String getHitos() {

        return hitos;
    }

    public void setHitos(String hitos) {

        this.hitos = hitos;
    }

    public Date getFechaFin() {

        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {

        this.fechaFin = fechaFin;
    }

    public Date getFechaInicio() {

        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {

        this.fechaInicio = fechaInicio;
    }

    public String getDuracion() {

        return duracion;
    }

    public void setDuracion(String duracion) {

        this.duracion = duracion;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    public int getEstadoActivo() {

        return estadoActivo;
    }

    public void setEstadoActivo(int estadoActivo) {

        this.estadoActivo = estadoActivo;
    }

    @Override
    public boolean equals(Object objetoAComparar) {

        if (this == objetoAComparar) {
            return true;
        }
        if (objetoAComparar == null || getClass() != objetoAComparar.getClass()) {
            return false;
        }

        ActividadDTO actividadComparada = (ActividadDTO) objetoAComparar;

        return IDActividad == actividadComparada.IDActividad &&
                estadoActivo == actividadComparada.estadoActivo &&
                Objects.equals(nombre, actividadComparada.nombre) &&
                Objects.equals(duracion, actividadComparada.duracion) &&
                Objects.equals(hitos, actividadComparada.hitos) &&
                Objects.equals(fechaInicio, actividadComparada.fechaInicio) &&
                Objects.equals(fechaFin, actividadComparada.fechaFin);
    }

    @Override
    public int hashCode() {

        return Objects.hash(IDActividad, nombre, duracion, hitos, fechaInicio, fechaFin, estadoActivo);
    }

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

