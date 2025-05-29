package logica.DTOs;

import java.sql.Timestamp;

public class HorarioProyectoDTO {

    private int idHorario;
    private int idProyecto;
    private String diaSemana;
    private Timestamp horaInicio;
    private Timestamp horaFin;

    public HorarioProyectoDTO() {
    }

    public HorarioProyectoDTO(int idHorario, int idProyecto, String diaSemana, Timestamp horaInicio, Timestamp horaFin) {

        this.idHorario = idHorario;
        this.idProyecto = idProyecto;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
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

    public Timestamp getHoraInicio() {

        return horaInicio;
    }

    public void setHoraInicio(Timestamp horaInicio) {

        this.horaInicio = horaInicio;
    }

    public Timestamp getHoraFin() {

        return horaFin;
    }

    public void setHoraFin(Timestamp horaFin) {

        this.horaFin = horaFin;
    }
}
