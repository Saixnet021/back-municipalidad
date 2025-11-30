package com.municipalidad.municipalidad.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tramite_constancia")
public class TramiteConstancia extends Tramite {

    @Column(nullable = false)
    private String direccion;

    @Column(name = "tiempo_residencia", nullable = false)
    private Integer tiempoResidencia; // en meses

    @Column(name = "recibo_servicio_url")
    private String reciboServicioUrl;
}
