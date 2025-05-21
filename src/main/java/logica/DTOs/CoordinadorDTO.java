package logica.DTOs;

import java.util.Objects;

public class CoordinadorDTO extends UsuarioDTO {

    private int numeroDePersonal;

    public CoordinadorDTO() {

    }

    public CoordinadorDTO(int numeroDePersonal, int idUsuario, String nombre, String apellido, int estadoActivo) {

        super(idUsuario, nombre, apellido, estadoActivo);
        this.numeroDePersonal = numeroDePersonal;

    }

    public int getNumeroDePersonal() {

        return numeroDePersonal;
    }

    public void setNumeroDePersonal(int numeroDePersonal) {

        this.numeroDePersonal = numeroDePersonal;
    }

    @Override
    public boolean equals(Object objetoAComparar) {

        if (this == objetoAComparar) {
            return true;
        }

        if (objetoAComparar == null || getClass() != objetoAComparar.getClass()) {
            return false;
        }

        CoordinadorDTO coordinadorComparado = (CoordinadorDTO) objetoAComparar;

        return numeroDePersonal == coordinadorComparado.getNumeroDePersonal() &&
                getIdUsuario() == coordinadorComparado.getIdUsuario() &&
                getEstado() == coordinadorComparado.getEstado() &&
                Objects.equals(getNombre(), coordinadorComparado.getNombre()) &&
                Objects.equals(getApellido(), coordinadorComparado.getApellido());
    }

    @Override
    public int hashCode() {

        return Objects.hash(numeroDePersonal, getIdUsuario(), getNombre(),
                getApellido(), getEstado());
    }
}