package utilidadesPruebas;

import logica.interfaces.GestorAlertas;

public class UtilidadesConsola implements GestorAlertas {

    @Override
    public void mostrarAlerta(String titulo, String encabezado, String contenido) {

        System.out.println("ALERTA: " + titulo + " - " + encabezado + ": " + contenido);
    }
}

