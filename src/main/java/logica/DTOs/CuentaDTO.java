package logica.DTOs;

public class CuentaDTO {

    private String correoElectronico;
    private String contrasena;
    private int idUsuario;

    public CuentaDTO() {

    }

    public CuentaDTO(String correoElectronico, String contrasena, int idUsuario) {

        this.correoElectronico = correoElectronico;
        this.contrasena = contrasena;
        this.idUsuario = idUsuario;
    }

    public String getCorreoElectronico() {

        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {

        this.correoElectronico = correoElectronico;
    }

    public String getContrasena() {

        return contrasena;
    }

    public void setContrasena(String contrasena) {

        this.contrasena = contrasena;
    }

    public int getIdUsuario() {

        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {

        this.idUsuario = idUsuario;
    }
}
