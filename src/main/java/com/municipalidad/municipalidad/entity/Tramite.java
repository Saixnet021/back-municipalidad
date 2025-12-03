package com.municipalidad.municipalidad.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tramite")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Tramite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String usuario;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private String estado; // PENDIENTE, EN_PROCESO, APROBADO, RECHAZADO

    @Column(unique = true, nullable = false)
    private String expediente; // Número de expediente único (ej: 2025-001234)

    @Column(nullable = false)
    private String tipo; // Mesa de Partes, Constancia, Licencia

    @PrePersist
    public void prePersist() {
        this.fecha = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = "PENDIENTE";
        }
        if (this.expediente == null) {
            // Generar número de expediente automático
            int year = java.time.LocalDate.now().getYear();
            String randomNumber = String.format("%06d", (int)(Math.random() * 999999));
            this.expediente = year + "-" + randomNumber;
        }
    }
}
