package com.municipalidad.municipalidad.service;

import com.municipalidad.municipalidad.entity.*;
import com.municipalidad.municipalidad.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class TramiteService {

    @Autowired
    private TramiteMesaPartesRepository mesaPartesRepository;

    @Autowired
    private TramiteConstanciaRepository constanciaRepository;

    @Autowired
    private TramiteLicenciaRepository licenciaRepository;

    @Autowired
    private TramiteRepository tramiteRepository;

    private final String UPLOAD_DIR = "uploads/tramites/";

    @Transactional
    public TramiteMesaPartes crearTramiteMesaPartes(TramiteMesaPartes tramite, MultipartFile archivo) {
        if (archivo != null && !archivo.isEmpty()) {
            String rutaArchivo = guardarArchivo(archivo);
            tramite.setRutaArchivo(rutaArchivo);
        }
        return mesaPartesRepository.save(tramite);
    }

    @Transactional
    public TramiteConstancia crearTramiteConstancia(TramiteConstancia tramite, MultipartFile recibo) {
        if (recibo != null && !recibo.isEmpty()) {
            String rutaRecibo = guardarArchivo(recibo);
            tramite.setReciboServicioUrl(rutaRecibo);
        }
        return constanciaRepository.save(tramite);
    }

    @Transactional
    public TramiteLicencia crearTramiteLicencia(TramiteLicencia tramite) {
        return licenciaRepository.save(tramite);
    }

    public List<Tramite> obtenerTodosTramites() {
        return tramiteRepository.findAll();
    }

    public List<Tramite> obtenerTramitesPorUsuario(String usuario) {
        return tramiteRepository.findByUsuario(usuario);
    }

    public Tramite buscarPorExpediente(String expediente) {
        return tramiteRepository.findByExpediente(expediente);
    }

    @Transactional
    public void actualizarEstado(Long id, String nuevoEstado) {
        Tramite tramite = tramiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trámite no encontrado"));
        tramite.setEstado(nuevoEstado);
        tramiteRepository.save(tramite);
    }

    private String guardarArchivo(MultipartFile archivo) {
        try {
            // Crear directorio si no existe
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generar nombre único
            String nombreOriginal = archivo.getOriginalFilename();
            String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
            String nombreUnico = UUID.randomUUID().toString() + extension;

            // Guardar archivo
            Path rutaDestino = uploadPath.resolve(nombreUnico);
            Files.copy(archivo.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

            return UPLOAD_DIR + nombreUnico;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar archivo: " + e.getMessage());
        }
    }

    public void seedTramites() {
        if (tramiteRepository.count() == 0) {
            TramiteMesaPartes t1 = new TramiteMesaPartes();
            t1.setUsuario("Juan Perez");
            t1.setAsunto("Solicitud de Limpieza");
            t1.setDescripcion("Solicito limpieza en la calle 5");
            mesaPartesRepository.save(t1);

            TramiteConstancia t2 = new TramiteConstancia();
            t2.setUsuario("Maria Lopez");
            t2.setDireccion("Av. Principal 123");
            t2.setTiempoResidencia(12);
            constanciaRepository.save(t2);

            TramiteLicencia t3 = new TramiteLicencia();
            t3.setUsuario("Carlos Ramos");
            t3.setNombreNegocio("Bodega Carlos");
            t3.setGiro("Venta de abarrotes");
            t3.setArea(new java.math.BigDecimal("50.00"));
            t3.setZonificacion("COMERCIAL");
            licenciaRepository.save(t3);
        }
    }
}
