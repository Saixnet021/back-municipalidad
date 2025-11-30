package com.municipalidad.municipalidad.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tramite_mesa_partes")
public class TramiteMesaPartes extends Tramite {

    @Column(nullable = false)
    private String asunto;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "ruta_archivo")
    private String rutaArchivo;
}
