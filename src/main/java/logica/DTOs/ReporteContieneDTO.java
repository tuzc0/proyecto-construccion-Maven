package logica.DTOs;

import java.sql.Timestamp;

public class ReporteContieneDTO {

    private int idReporte;
    private int idActividad;
    private Timestamp fechaInicioReal;
    private Timestamp fechaFinReal;

    public ReporteContieneDTO() {

    }

    public ReporteContieneDTO(int idReporte, int idActividad) {

        this.idReporte = idReporte;
        this.idActividad = idActividad;
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

    }
}
