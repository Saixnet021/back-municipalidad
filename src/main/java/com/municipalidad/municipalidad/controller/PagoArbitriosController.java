package com.municipalidad.municipalidad.controller;

import com.municipalidad.municipalidad.dto.CreateArbitrioRequest;
import com.municipalidad.municipalidad.dto.PagoArbitriosDTO;
import com.municipalidad.municipalidad.dto.SimulatedCardPaymentRequest;
import com.municipalidad.municipalidad.service.PagoArbitriosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/arbitrios")
@RequiredArgsConstructor
public class PagoArbitriosController {

    private final PagoArbitriosService service;

    @PostMapping
    // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PagoArbitriosDTO> create(@RequestBody CreateArbitrioRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<PagoArbitriosDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/mis-deudas")
    public ResponseEntity<List<PagoArbitriosDTO>> getMyDebts() {
        return ResponseEntity.ok(service.getMyDebts());
    }

    @GetMapping("/deudas/{dni}")
    public ResponseEntity<List<PagoArbitriosDTO>> getDebtsByDni(@PathVariable String dni) {
        return ResponseEntity.ok(service.getDebtsByDni(dni));
    }

    @PostMapping("/{id}/pagar")
    public ResponseEntity<Void> pay(@PathVariable Long id) {
        service.pay(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/pagar-simulado")
    public ResponseEntity<Void> processSimulatedPayment(@PathVariable Long id, @RequestBody SimulatedCardPaymentRequest request) {
        service.processSimulatedPayment(id, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/seed")
    public ResponseEntity<List<PagoArbitriosDTO>> seedData() {
        List<PagoArbitriosDTO> created = new ArrayList<>();
        created.add(service.create(new CreateArbitrioRequest(null, "Limpieza Publica", new BigDecimal("45.50"))));
        created.add(service.create(new CreateArbitrioRequest(null, "Parques y Jardines", new BigDecimal("30.00"))));
        created.add(service.create(new CreateArbitrioRequest(null, "Serenazgo", new BigDecimal("55.20"))));
        return ResponseEntity.ok(created);
    }

    @GetMapping("/seed-admin")
    public ResponseEntity<String> seedAdmin() {
        service.seedAdmin();
        return ResponseEntity.ok("Admin user created");
    }
}
