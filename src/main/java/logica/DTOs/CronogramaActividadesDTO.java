package logica.DTOs;

import java.util.Objects;

public class CronogramaActividadesDTO {

    private int IDCronograma;
    private String matriculaEstudiante;
    private int idProyecto;
    private int idPeriodo;
    private int estadoActivo;

    public CronogramaActividadesDTO() {

    }

    public CronogramaActividadesDTO(int IDCronograma, String matriculaEstudiante, int idProyecto, int idPeriodo, int estadoActivo) {

        this.IDCronograma = IDCronograma;
        this.matriculaEstudiante = matriculaEstudiante;
        this.idProyecto = idProyecto;
        this.idPeriodo = idPeriodo;
        this.estadoActivo = estadoActivo;

    }

    public int getIDCronograma() {

        return IDCronograma;
    }

    public void setIDCronograma(int IDCronograma) {

        this.IDCronograma = IDCronograma;
    }

    public String getMatriculaEstudiante() {

        return matriculaEstudiante;
    }

    public void setMatriculaEstudiante(String matriculaEstudiante) {

        this.matriculaEstudiante = matriculaEstudiante;
    }

    public int getIdProyecto() {

        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {

        this.idProyecto = idProyecto;
    }

    public int getIdPeriodo() {

        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {

        this.idPeriodo = idPeriodo;
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

        CronogramaActividadesDTO cronogramaComparado = (CronogramaActividadesDTO) objetoAComparar;

        return IDCronograma == cronogramaComparado.IDCronograma &&
                idProyecto == cronogramaComparado.idProyecto &&
                idPeriodo == cronogramaComparado.idPeriodo &&
                estadoActivo == cronogramaComparado.estadoActivo &&
                Objects.equals(matriculaEstudiante, cronogramaComparado.matriculaEstudiante);
    }

    @Override
    public int hashCode() {

        return Objects.hash(IDCronograma, matriculaEstudiante, idProyecto, idPeriodo, estadoActivo);
    }
}
