package logica.utilidadesproyecto;

import logica.DTOs.OrganizacionVinculadaDTO;
import logica.DTOs.ProyectoDTO;
import logica.DTOs.RepresentanteDTO;

public class AsociacionProyecto {

    private RepresentanteDTO representante;
    private OrganizacionVinculadaDTO organizacion;
    private ProyectoDTO proyecto;

    public AsociacionProyecto(RepresentanteDTO representante,
                              OrganizacionVinculadaDTO organizacion, ProyectoDTO proyecto) {

        this.representante = representante;
        this.organizacion = organizacion;
        this.proyecto = proyecto;
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

    public ProyectoDTO getProyecto() {

        return proyecto;
    }

    public void setProyecto(ProyectoDTO proyecto) {

        this.proyecto = proyecto;
    }
}
