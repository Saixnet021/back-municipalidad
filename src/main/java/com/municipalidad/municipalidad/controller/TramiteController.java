package com.municipalidad.municipalidad.controller;

import com.municipalidad.municipalidad.entity.*;
import com.municipalidad.municipalidad.service.TramiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tramites")
@CrossOrigin(origins = "http://localhost:4200")
public class TramiteController {

    @Autowired
    private TramiteService tramiteService;

    @PostMapping("/mesa-de-partes")
    public ResponseEntity<?> crearTramiteMesaPartes(
            @RequestParam("usuario") String usuario,
            @RequestParam("asunto") String asunto,
            @RequestParam("descripcion") String descripcion,
            @RequestParam(value = "archivo", required = false) MultipartFile archivo) {

        try {
            TramiteMesaPartes tramite = new TramiteMesaPartes();
            tramite.setUsuario(usuario);
            tramite.setAsunto(asunto);
            tramite.setDescripcion(descripcion);

            TramiteMesaPartes tramiteGuardado = tramiteService.crearTramiteMesaPartes(tramite, archivo);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Trámite de Mesa de Partes creado exitosamente",
                    "tramite", tramiteGuardado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/constancia")
    public ResponseEntity<?> crearTramiteConstancia(
            @RequestParam("usuario") String usuario,
            @RequestParam("direccion") String direccion,
            @RequestParam("tiempoResidencia") Integer tiempoResidencia,
            @RequestParam(value = "recibo", required = false) MultipartFile recibo) {

        try {
            TramiteConstancia tramite = new TramiteConstancia();
            tramite.setUsuario(usuario);
            tramite.setDireccion(direccion);
            tramite.setTiempoResidencia(tiempoResidencia);

            TramiteConstancia tramiteGuardado = tramiteService.crearTramiteConstancia(tramite, recibo);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Trámite de Constancia de Residencia creado exitosamente",
                    "tramite", tramiteGuardado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/licencia")
    public ResponseEntity<?> crearTramiteLicencia(@RequestBody Map<String, Object> request) {
        try {
            TramiteLicencia tramite = new TramiteLicencia();
            tramite.setUsuario((String) request.get("usuario"));
            tramite.setNombreNegocio((String) request.get("nombreNegocio"));
            tramite.setGiro((String) request.get("giro"));
            
            Object areaObj = request.get("area");
            if (areaObj != null) {
                tramite.setArea(new BigDecimal(areaObj.toString()));
            }
            
            tramite.setZonificacion((String) request.get("zonificacion"));

            TramiteLicencia tramiteGuardado = tramiteService.crearTramiteLicencia(tramite);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Trámite de Licencia de Funcionamiento creado exitosamente",
                    "tramite", tramiteGuardado));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Error al procesar la solicitud: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Tramite>> obtenerTodosTramites() {
        return ResponseEntity.ok(tramiteService.obtenerTodosTramites());
    }

    @GetMapping("/usuario/{usuario}")
    public ResponseEntity<List<Tramite>> obtenerTramitesPorUsuario(@PathVariable String usuario) {
        return ResponseEntity.ok(tramiteService.obtenerTramitesPorUsuario(usuario));
    }

    @GetMapping("/expediente/{expediente}")
    public ResponseEntity<?> buscarPorExpediente(@PathVariable String expediente) {
        Tramite tramite = tramiteService.buscarPorExpediente(expediente);
        if (tramite == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Trámite no encontrado"));
        }
        return ResponseEntity.ok(tramite);
    }
}
