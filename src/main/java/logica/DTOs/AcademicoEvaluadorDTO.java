package logica.DTOs;

public class AcademicoEvaluadorDTO extends UsuarioDTO {

    private int numeroDePersonal;

    public AcademicoEvaluadorDTO() {

    }

    public AcademicoEvaluadorDTO(int numeroDePersonal, int idUsuario, String nombre, String apellido, int estadoActivo) {

        super(idUsuario, nombre, apellido, estadoActivo);
        this.numeroDePersonal = numeroDePersonal;
    }

    public int getNumeroDePersonal() {

        return numeroDePersonal;
    }

    public void setNumeroDePersonal(int numeroDePersonal) {

        this.numeroDePersonal = numeroDePersonal;
    }
}