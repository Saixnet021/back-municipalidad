package com.municipalidad.municipalidad.controller;

import com.municipalidad.municipalidad.entity.Tramite;
import com.municipalidad.municipalidad.service.TramiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/tramites")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AdminTramiteController {

    private final TramiteService tramiteService;

    @GetMapping
    // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Tramite>> getAllTramites() {
        return ResponseEntity.ok(tramiteService.obtenerTodosTramites());
    }

    @PostMapping("/{id}/approve")
    // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> approveTramite(@PathVariable Long id) {
        try {
            tramiteService.actualizarEstado(id, "APROBADO");
            return ResponseEntity.ok(Map.of("mensaje", "Trámite aprobado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/reject")
    // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> rejectTramite(@PathVariable Long id) {
        try {
            tramiteService.actualizarEstado(id, "RECHAZADO");
            return ResponseEntity.ok(Map.of("mensaje", "Trámite rechazado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/seed")
    public ResponseEntity<?> seedTramites() {
        tramiteService.seedTramites();
        return ResponseEntity.ok(Map.of("mensaje", "Trámites de prueba creados"));
    }
}
