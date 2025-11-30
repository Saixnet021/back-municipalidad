package com.municipalidad.municipalidad.controller;

import com.municipalidad.municipalidad.entity.Deuda;
import com.municipalidad.municipalidad.entity.Pago;
import com.municipalidad.municipalidad.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/arbitrios")
@CrossOrigin(origins = "http://localhost:4200") // Permitir peticiones desde Angular
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping("/deudas/{dni}")
    public ResponseEntity<List<Deuda>> consultarDeudas(@PathVariable String dni) {
        List<Deuda> deudas = pagoService.obtenerDeudasPendientes(dni);
        return ResponseEntity.ok(deudas);
    }

    @PostMapping("/pagos/procesar")
    public ResponseEntity<?> procesarPago(@RequestBody Map<String, Object> pagoRequest) {
        try {
            Pago pago = pagoService.procesarPago(pagoRequest);
            return ResponseEntity.ok(Map.of("mensaje", "Pago exitoso", "pago", pago));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
