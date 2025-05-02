package logica.DTOs;

import java.sql.Timestamp;

public class AutoevaluacionDTO {

    private int IDAutoevaluacion;
    private Timestamp fecha;
    private String lugar;
    private float calificacionFinal;
    private String idEstudiante;
    private int estadoActivo;

    public AutoevaluacionDTO() {

    }

    public AutoevaluacionDTO(int IDAutoevaluacion, Timestamp fecha, String lugar, float calificacionFinal, String idEstudiante, int estadoActivo) {

        this.IDAutoevaluacion = IDAutoevaluacion;
        this.fecha = fecha;
        this.lugar = lugar;
        this.calificacionFinal = calificacionFinal;
        this.idEstudiante = idEstudiante;
        this.estadoActivo = estadoActivo;
    }

    public int getIDAutoevaluacion() {

        return IDAutoevaluacion;
    }

    public void setIDAutoevaluacion(int IDAutoevaluacion) {

        this.IDAutoevaluacion = IDAutoevaluacion;
    }

    public Timestamp getFecha() {

        return fecha;
    }

    public void setFecha(Timestamp fecha) {

        this.fecha = fecha;
    }

    public String getLugar() {

        return lugar;
    }

    public void setLugar(String lugar) {

        this.lugar = lugar;
    }

    public float getCalificacionFinal() {

        return calificacionFinal;
    }

    public void setCalificacionFinal(int calificacionFinal) {

        this.calificacionFinal = calificacionFinal;
    }

    public String getIdEstudiante() {

        return idEstudiante;
    }

    public void setidEstudiante(String idEstudiante) {

        this.idEstudiante = idEstudiante;
    }

    public int getEstadoActivo() {

        return estadoActivo;
    }

    public void setEstadoActivo(int estadoActivo) {

        this.estadoActivo = estadoActivo;
    }
}
