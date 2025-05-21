package logica.utilidadesproyecto;

import logica.DTOs.OrganizacionVinculadaDTO;
import logica.DTOs.RepresentanteDTO;

public class ContenedoraRepresentanteOrganizacion {

    private RepresentanteDTO representante;
    private OrganizacionVinculadaDTO organizacion;

    public ContenedoraRepresentanteOrganizacion(RepresentanteDTO representante, OrganizacionVinculadaDTO organizacion) {
        this.representante = representante;
        this.organizacion = organizacion;
    }

    public RepresentanteDTO getRepresentante() {
        return representante;
    }

    public void setRepresentante(RepresentanteDTO representante) {
        this.representante = representante;
    }

    public OrganizacionVinculadaDTO getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(OrganizacionVinculadaDTO organizacion) {
        this.organizacion = organizacion;
    }
}
