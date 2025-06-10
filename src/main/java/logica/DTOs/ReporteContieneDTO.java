package logica.DTOs;

import java.sql.Timestamp;

public class ReporteContieneDTO {

    private int idReporte;
    private int idActividad;
    private Timestamp fechaInicioReal;
    private Timestamp fechaFinReal;
    private int estadoActivo;

    public ReporteContieneDTO() {

    }

    public ReporteContieneDTO(int idReporte, int idActividad, Timestamp fechaInicioReal, Timestamp fechaFinReal, int estadoActivo) {

        this.idReporte = idReporte;
        this.idActividad = idActividad;
        this.fechaInicioReal = fechaInicioReal;
        this.fechaFinReal = fechaFinReal;
        this.estadoActivo = estadoActivo;
    }

    public ReporteContieneDTO(int idReporte, int idActividad, Timestamp fechaInicioReal, Timestamp fechaFinReal) {

        this.idReporte = idReporte;
        this.idActividad = idActividad;
        this.fechaInicioReal = fechaInicioReal;
        this.fechaFinReal = fechaFinReal;
    }

    public int getIdReporte() {

        return idReporte;
    }

    public void setIdReporte(int idReporte) {

        this.idReporte = idReporte;
    }

    public int getIdActividad() {

        return idActividad;
    }

    public void setIdActividad(int idActividad) {

        this.idActividad = idActividad;
    }

    public Timestamp getFechaInicioReal() {

        return fechaInicioReal;
    }

    public void setFechaInicioReal(Timestamp fechaInicioReal) {

        this.fechaInicioReal = fechaInicioReal;
    }

    public Timestamp getFechaFinReal() {

        return fechaFinReal;
    }

    public void setFechaFinReal(Timestamp fechaFinReal) {

        this.fechaFinReal = fechaFinReal;

    }

    public int getEstadoActivo() {

        return estadoActivo;
    }

    public void setEstadoActivo(int estadoActivo) {

        this.estadoActivo = estadoActivo;
    }
}
