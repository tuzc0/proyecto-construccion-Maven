package logica.DTOs;

public class CronogramaContieneDTO {

    private int idCronograma;
    private int idActividad;
    private int estadoActivo;

    public CronogramaContieneDTO() {

    }

    public CronogramaContieneDTO(int idCronograma, int idActividad, int estadoActivo) {

        this.idCronograma = idCronograma;
        this.idActividad = idActividad;
        this.estadoActivo = estadoActivo;
    }

    public int getIdCronograma() {

        return idCronograma;
    }

    public void setIdCronograma(int idCronograma) {

        this.idCronograma = idCronograma;
    }

    public int getIdActividad() {

        return idActividad;
    }

    public void setIdActividad(int idActividad) {

        this.idActividad = idActividad;
    }

    public int getEstadoActivo() {

        return estadoActivo;
    }

    public void setEstadoActivo(int estadoActivo) {

        this.estadoActivo = estadoActivo;
    }
}
