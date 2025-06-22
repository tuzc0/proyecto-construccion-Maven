package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class GestorHorarios {

    private final Map<CheckBox, List<ComboBox<String>>> diasConHorarios;

    public GestorHorarios() {

        this.diasConHorarios = new HashMap<>();
    }

    public void agregarDia(CheckBox checkBox, ComboBox<String> horaInicio, ComboBox<String> minutoInicio,
                           ComboBox<String> horaFin, ComboBox<String> minutoFin) {

        diasConHorarios.put(checkBox, List.of(horaInicio, minutoInicio, horaFin, minutoFin));
    }

    public void inicializarCombosHorarios() {

        ObservableList<String> listaHoras = FXCollections.observableArrayList();
        ObservableList<String> listaMinutos = FXCollections.observableArrayList();

        final int HORAS_POR_DIA = 24;
        final int MINUTOS_POR_HORA = 60;

        for (int hora = 0; hora < HORAS_POR_DIA; hora++) {

            listaHoras.add(String.format("%02d", hora));
        }

        for (int minuto = 0; minuto < MINUTOS_POR_HORA; minuto++) {

            listaMinutos.add(String.format("%02d", minuto));
        }

        diasConHorarios.values().forEach(combosHorarios -> {

            combosHorarios.get(0).setItems(listaHoras);
            combosHorarios.get(2).setItems(listaHoras);
            combosHorarios.get(1).setItems(listaMinutos);
            combosHorarios.get(3).setItems(listaMinutos);
        });
    }

    public void habilitarHorariosPorDiaSeleccionado() {

        diasConHorarios.forEach((diaCheckBox, combosHorarios) -> {

            diaCheckBox.selectedProperty().addListener((valorObservado,
                                                        valorAnterior, estadoSeleccionado) -> {

                combosHorarios.forEach(comboHorario -> comboHorario.setDisable(!estadoSeleccionado));
            });
        });
    }

    public boolean validarDiasSeleccionados() {

        return diasConHorarios.keySet().stream().anyMatch(CheckBox::isSelected);
    }

    public List<String> validarHorarios() {

        List<String> listaDeErrores = new ArrayList<>();

        diasConHorarios.forEach((comboDia, listaDeCombosPorDia) -> {

            if (comboDia.isSelected() && !verificarHorarioDia(listaDeCombosPorDia)) {

                listaDeErrores.add("Horario inválido para " + obtenerNombreDia(comboDia));
            }
        });

        return listaDeErrores;
    }

    public boolean verificarHorarioDia(List<ComboBox<String>> horarios) {

        if (!todosLosCombosTienenValor(horarios)) {

            return false;
        }

        int horaInicio = Integer.parseInt(horarios.get(0).getValue());
        int minutoInicio = Integer.parseInt(horarios.get(1).getValue());
        int horaFin = Integer.parseInt(horarios.get(2).getValue());
        int minutoFin = Integer.parseInt(horarios.get(3).getValue());

        return esHorarioCorrecto(horaInicio, minutoInicio, horaFin, minutoFin);
    }

    private boolean todosLosCombosTienenValor(List<ComboBox<String>> horarios) {

        return horarios.stream().allMatch(comboDias -> comboDias.getValue() != null);
    }

    private boolean esHorarioCorrecto(int horaInicio, int minutoInicio, int horaFin, int minutoFin) {

        if (horaInicio < horaFin) {

            return true;

        } else if (horaInicio == horaFin) {

            return minutoInicio < minutoFin;

        } else {

            return false;
        }
    }


    public String obtenerNombreDia(CheckBox checkBox) {

        if (checkBox.getId() == null) {
            return "Día desconocido";
        }

        switch(checkBox.getId()) {

            case "checkLunes":

                return "Lunes";

            case "checkMartes":

                return "Martes";

            case "checkMiercoles":

                return "Miercoles";

            case "checkJueves":

                return "Jueves";

            case "checkViernes":

                return "Viernes";

            default:

                return "Día desconocido";
        }
    }

    public Map<CheckBox, List<ComboBox<String>>> obtenerDiasConHorarios() {

        return diasConHorarios;
    }
}