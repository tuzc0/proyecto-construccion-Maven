package logica.DTOs;

import java.sql.Timestamp;

public class ReporteDTO {

    private int IDReporte;
    private int numeroHoras;
    private String metodologia;
    private String observaciones;
    private Timestamp fecha;
    private String matricula;

    public ReporteDTO() {

    }

    public ReporteDTO(int IDReporte, int numeroHoras, String metodologia, String observaciones, Timestamp fecha, String matricula) {

        this.IDReporte = IDReporte;
        this.numeroHoras = numeroHoras;
        this.metodologia = metodologia;
        this.observaciones = observaciones;
        this.fecha = fecha;
        this.matricula = matricula;
    }

    public int getIDReporte() {

        return IDReporte;
    }

    public void setIDReporte(int IDReporte) {

        this.IDReporte = IDReporte;
    }

    public int getNumeroHoras() {

        return numeroHoras;
    }

    public void setNumeroHoras(int numeroHoras) {

        this.numeroHoras = numeroHoras;
    }

    public String getMetodologia() {

        return metodologia;
    }

    public void setMetodologia(String metodologia) {

        this.metodologia = metodologia;
    }

    public String getObservaciones() {

        return observaciones;
    }

    public void setObservaciones(String observaciones) {

        this.observaciones = observaciones;
    }

    public Timestamp getFecha() {

        return fecha;
    }

    public void setFecha(Timestamp fecha) {

        this.fecha = fecha;
    }

    public String getMatricula() {

        return matricula;
    }

    public void setMatricula(String matricula) {

        this.matricula = matricula;
    }
}
