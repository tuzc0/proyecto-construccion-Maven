package utilidadesPruebas;

import logica.interfaces.IGestorAlertas;

public class UtilidadesConsola implements IGestorAlertas {

    @Override
    public void mostrarAlerta(String titulo, String encabezado, String contenido) {

        System.out.println("ALERTA: " + titulo + " - " + encabezado + ": " + contenido);
    }
}

