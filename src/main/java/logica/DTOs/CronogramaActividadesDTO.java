package logica.DTOs;

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
}
