package logica.DTOs;

import java.util.Objects;

public class GrupoDTO {

    private int NRC;
    private String nombre;
    private int numeroPersonal;
    private int idPeriodo;
    private int estadoActivo;

    public GrupoDTO() {

    }

    public GrupoDTO(int NRC, String nombre, int numeroPersonal, int idPeriodo, int estadoActivo) {

        this.NRC = NRC;
        this.nombre = nombre;
        this.numeroPersonal = numeroPersonal;
        this.idPeriodo = idPeriodo;
        this.estadoActivo = estadoActivo;
    }

    public int getNRC() {

        return NRC;
    }

    public void setNRC(int NRC) {

        this.NRC = NRC;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    public int getNumeroPersonal() {

        return numeroPersonal;
    }

    public void setNumeroPersonal(int numeroPersonal) {

        this.numeroPersonal = numeroPersonal;
    }


    public int getIdPeriodo() {

        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {

        this.idPeriodo = idPeriodo;
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

        GrupoDTO grupoComparado = (GrupoDTO) objetoAComparar;

        return NRC == grupoComparado.NRC &&
                numeroPersonal == grupoComparado.numeroPersonal &&
                idPeriodo == grupoComparado.idPeriodo &&
                estadoActivo == grupoComparado.estadoActivo &&
                Objects.equals(nombre, grupoComparado.nombre);
    }

    @Override
    public int hashCode() {

        return Objects.hash(NRC, nombre, numeroPersonal, idPeriodo, estadoActivo);
    }
}