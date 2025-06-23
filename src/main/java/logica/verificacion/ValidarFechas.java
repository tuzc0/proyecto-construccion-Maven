package logica.verificacion;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import logica.DAOs.PeriodoDAO;
import logica.DTOs.PeriodoDTO;

public class ValidarFechas {

    public List<String> validarFechas(LocalDate fechaInicio, LocalDate fechaFin) throws SQLException, IOException {

        List<String> errores = new ArrayList<>();
        PeriodoDAO periodoDAO = new PeriodoDAO();

        PeriodoDTO periodoDTO = periodoDAO.mostrarPeriodoActual();
        LocalDate fechaInicioPeriodoActual = periodoDTO.getFechaInicio().toLocalDate();
        LocalDate fechaFinPeriodoActual = periodoDTO.getFechaFinal().toLocalDate();

        if (fechaInicio == null || fechaFin == null) {
            errores.add("Debe seleccionar ambas fechas.");
        } else {
            if (fechaInicio.isBefore(fechaInicioPeriodoActual) || fechaFin.isAfter(fechaFinPeriodoActual)) {
                errores.add("Las fechas seleccionadas deben estar dentro del periodo actual.");
            }

            if (fechaInicio.isAfter(fechaFin)) {
                errores.add("La fecha de inicio no puede ser posterior a la fecha de fin.");
            }
        }

        return errores;
    }
}