package logica.DTOs;

public class CoordinadorDTO extends UsuarioDTO {

    private int NumeroDePersonal;

    public CoordinadorDTO() {

    }

    public CoordinadorDTO(int numeroDePersonal, int idUsuario, String nombre, String apellido, int estadoActivo) {

        super(idUsuario, nombre, apellido, estadoActivo);
        this.NumeroDePersonal = numeroDePersonal;

    }

    public int getNumeroDePersonal() {

        return NumeroDePersonal;
    }

    public void setNumeroDePersonal(int numeroDePersonal) {

        this.NumeroDePersonal = numeroDePersonal;
    }
}