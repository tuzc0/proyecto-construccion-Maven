package logica.DTOs;

public class CronogramaContieneDTO {

    private int idCronograma;
    private int idActividad;

    public CronogramaContieneDTO() {

    }

    public CronogramaContieneDTO(int idCronograma, int idActividad) {

        this.idCronograma = idCronograma;
        this.idActividad = idActividad;
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
}
