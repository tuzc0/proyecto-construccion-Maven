package logica.DTOs;

import java.sql.Date;
import java.sql.Time;

public class HorarioProyectoDTO {

    private int idHorario;
    private int idProyecto;
    private String diaSemana;
    private Time horaInicio;
    private Time horaFin;
    private String idEstudiante;

    public HorarioProyectoDTO() {
    }

    public HorarioProyectoDTO(int idHorario, int idProyecto, String diaSemana,
                              Time horaInicio, Time horaFin, String idEstudiante) {

        this.idHorario = idHorario;
        this.idProyecto = idProyecto;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.idEstudiante = idEstudiante;
    }

    public int getIdHorario() {

        return idHorario;
    }

    public void setIdHorario(int idHorario) {

        this.idHorario = idHorario;
    }

    public int getIdProyecto() {

        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {

        this.idProyecto = idProyecto;
    }

    public String getDiaSemana() {

        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {

        this.diaSemana = diaSemana;
    }

    public Time getHoraInicio() {

        return horaInicio;
    }

    public void setHoraInicio(Time horaInicio) {

        this.horaInicio = horaInicio;
    }

    public Time getHoraFin() {

        return horaFin;
    }

    public void setHoraFin(Time horaFin) {

        this.horaFin = horaFin;
    }

    public String getIdEstudiante() {

        return idEstudiante;
    }

    public void setIdEstudiante(String idEstudiante) {

        this.idEstudiante = idEstudiante;
    }
}
