package logica.DTOs;

import java.sql.Date;

public class PeriodoDTO {

    private int IDPeriodo;
    private String descripcion;
    private int estadoActivo;
    private Date fechaInicio;
    private Date fechaFinal;

    public PeriodoDTO() {

    }

    public PeriodoDTO(int IDPeriodo, String descripcion, int estadoActivo) {

        this.IDPeriodo = IDPeriodo;
        this.descripcion = descripcion;
        this.estadoActivo = estadoActivo;
    }

    public PeriodoDTO(int IDPeriodo, String descripcion, int estadoActivo, Date fechaInicio, Date fechaFinal) {

        this.IDPeriodo = IDPeriodo;
        this.descripcion = descripcion;
        this.estadoActivo = estadoActivo;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
    }

    public int getIDPeriodo() {

        return IDPeriodo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public void setIDPeriodo(int IDPeriodo) {

        this.IDPeriodo = IDPeriodo;
    }

    public String getDescripcion() {

        return descripcion;
    }

    public void setDescripcion(String descripcion) {

        this.descripcion = descripcion;
    }

    public int getEstadoActivo() {

        return estadoActivo;
    }

    public void setEstadoActivo(int estadoActivo) {

        this.estadoActivo = estadoActivo;
    }
}

