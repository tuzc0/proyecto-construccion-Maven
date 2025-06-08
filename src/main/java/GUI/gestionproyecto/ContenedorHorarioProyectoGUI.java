package GUI.gestionproyecto;

import javafx.beans.property.SimpleStringProperty;

public class ContenedorHorarioProyectoGUI {

    private final SimpleStringProperty lunes;
    private final SimpleStringProperty martes;
    private final SimpleStringProperty miercoles;
    private final SimpleStringProperty jueves;
    private final SimpleStringProperty viernes;

    public ContenedorHorarioProyectoGUI(String lunes, String martes, String miercoles,
                                        String jueves, String viernes) {

        this.lunes = new SimpleStringProperty(lunes);
        this.martes = new SimpleStringProperty(martes);
        this.miercoles = new SimpleStringProperty(miercoles);
        this.jueves = new SimpleStringProperty(jueves);
        this.viernes = new SimpleStringProperty(viernes);
    }

    public String getLunes() {

        return lunes.get();
    }

    public String getMartes() {

        return martes.get();
    }

    public String getMiercoles() {

        return miercoles.get();
    }

    public String getJueves() {

        return jueves.get();
    }

    public String getViernes() {
        return viernes.get();
    }
}
