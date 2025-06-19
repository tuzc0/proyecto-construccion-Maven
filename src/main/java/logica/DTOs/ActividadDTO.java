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
}

