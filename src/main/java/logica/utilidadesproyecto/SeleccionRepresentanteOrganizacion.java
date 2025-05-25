package logica.utilidadesproyecto;

import logica.DTOs.OrganizacionVinculadaDTO;
import logica.DTOs.RepresentanteDTO;

public class SeleccionRepresentanteOrganizacion {

    private static RepresentanteDTO representanteSeleccionado;
    private static OrganizacionVinculadaDTO organizacionSeleccionada;

    public static RepresentanteDTO getRepresentanteSeleccionado() {

        return representanteSeleccionado;
    }

    public static void setRepresentanteSeleccionado(RepresentanteDTO representante) {

        representanteSeleccionado = representante;
    }

    public static OrganizacionVinculadaDTO getOrganizacionSeleccionada() {

        return organizacionSeleccionada;
    }

    public static void setOrganizacionSeleccionada(OrganizacionVinculadaDTO organizacion) {

        organizacionSeleccionada = organizacion;
    }
}
