package logica.DTOs;

public class CronogramaContieneDTO {

    private int idCronograma;
    private int idActividad;
    private String mes;
    private int estadoActivo;

    public CronogramaContieneDTO() {

    }

    public CronogramaContieneDTO(int idCronograma, int idActividad, String mes, int estadoActivo) {

        this.idCronograma = idCronograma;
        this.idActividad = idActividad;
        this.mes = mes;
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

    public String getMes() {

        return mes;
    }

    public void setMes(String mes) {

        this.mes = mes;
    }

    public int getEstadoActivo() {

        return estadoActivo;
    }

    public void setEstadoActivo(int estadoActivo) {

        this.estadoActivo = estadoActivo;
    }
}
