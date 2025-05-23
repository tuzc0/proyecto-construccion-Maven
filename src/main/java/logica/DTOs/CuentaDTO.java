package logica.DTOs;

import java.util.Objects;

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

    @Override
    public boolean equals(Object cuentaAComparar) {

        if (this == cuentaAComparar) {
            return true;
        }

        if (cuentaAComparar == null || getClass() != cuentaAComparar.getClass()) {
            return false;
        }

        CuentaDTO cuentaDTO = (CuentaDTO) cuentaAComparar;

        return idUsuario == cuentaDTO.idUsuario &&
                Objects.equals(correoElectronico, cuentaDTO.correoElectronico) &&
                Objects.equals(contrasena, cuentaDTO.contrasena);
    }

    @Override
    public int hashCode() {

        return Objects.hash(correoElectronico, contrasena, idUsuario);
    }
}
