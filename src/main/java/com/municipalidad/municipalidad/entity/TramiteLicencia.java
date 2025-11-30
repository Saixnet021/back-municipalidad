package com.municipalidad.municipalidad.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tramite_licencia")
public class TramiteLicencia extends Tramite {

    @Column(name = "nombre_negocio", nullable = false)
    private String nombreNegocio;

    @Column(nullable = false)
    private String giro;

    @Column(nullable = false)
    private BigDecimal area; // en m²

    @Column(nullable = false)
    private String zonificacion; // COMERCIAL, RESIDENCIAL, INDUSTRIAL
}
