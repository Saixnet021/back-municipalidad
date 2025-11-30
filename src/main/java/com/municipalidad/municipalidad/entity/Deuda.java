package com.municipalidad.municipalidad.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "deuda")
public class Deuda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dni_contribuyente", nullable = false, length = 8)
    private String dniContribuyente;

    @Column(nullable = false)
    private String periodo; // Ejemplo: "2023-01"

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(nullable = false)
    private String estado; // PENDIENTE, PAGADO

    @Column(nullable = false)
    private String tipo; // Arbitrios, Predial, etc.
}
