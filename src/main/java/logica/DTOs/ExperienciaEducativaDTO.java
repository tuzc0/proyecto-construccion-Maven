package logica.DTOs;

public class ExperienciaEducativaDTO {

    private int IdEE;
    private String nombre;

    public ExperienciaEducativaDTO() {

    }

    public ExperienciaEducativaDTO(int IdEE, String nombre) {

        this.IdEE = IdEE;
        this.nombre = nombre;
    }

    public int getIdEE() {

        return IdEE;
    }

    public void setIdEE(int IdEE) {

        this.IdEE = IdEE;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }
}