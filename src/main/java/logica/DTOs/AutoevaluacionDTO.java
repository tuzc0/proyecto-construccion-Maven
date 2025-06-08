package logica.DTOs;

import java.sql.Timestamp;
import java.util.Objects;

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

    public void setCalificacionFinal(float calificacionFinal) {

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

    @Override
    public boolean equals(Object objetoAComparar) {

        if (this == objetoAComparar) {
            return true;
        }
        if (objetoAComparar == null || getClass() != objetoAComparar.getClass()) {
            return false;
        }

        AutoevaluacionDTO autoevaluacionComparada = (AutoevaluacionDTO) objetoAComparar;

        return IDAutoevaluacion == autoevaluacionComparada.IDAutoevaluacion &&
                Float.compare(autoevaluacionComparada.calificacionFinal, calificacionFinal) == 0 &&
                estadoActivo == autoevaluacionComparada.estadoActivo &&
                Objects.equals(fecha, autoevaluacionComparada.fecha) &&
                Objects.equals(lugar, autoevaluacionComparada.lugar) &&
                Objects.equals(idEstudiante, autoevaluacionComparada.idEstudiante);
    }

    @Override
    public int hashCode() {

        return Objects.hash(IDAutoevaluacion, fecha, lugar, calificacionFinal, idEstudiante, estadoActivo);
    }
}
