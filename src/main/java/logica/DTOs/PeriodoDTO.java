package logica.DTOs;

public class PeriodoDTO {

    private int IDPeriodo;
    private String descripcion;
    private int estadoActivo;

    public PeriodoDTO() {

    }

    public PeriodoDTO(int IDPeriodo, String descripcion, int estadoActivo) {

        this.IDPeriodo = IDPeriodo;
        this.descripcion = descripcion;
        this.estadoActivo = estadoActivo;
    }

    public int getIDPeriodo() {

        return IDPeriodo;
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

