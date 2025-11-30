package com.municipalidad.municipalidad.service;

import com.municipalidad.municipalidad.entity.Deuda;
import com.municipalidad.municipalidad.entity.Pago;
import com.municipalidad.municipalidad.repository.DeudaRepository;
import com.municipalidad.municipalidad.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PagoService {

    @Autowired
    private DeudaRepository deudaRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private RestTemplate restTemplate;

    // LLAVE PRIVADA DE PRUEBA (Reemplazar con la real)
    private static final String CULQI_PRIVATE_KEY = "sk_test_YOUR_PRIVATE_KEY_HERE";

    public List<Deuda> obtenerDeudasPendientes(String dni) {
        return deudaRepository.findByDniContribuyenteAndEstado(dni, "PENDIENTE");
    }

    @Transactional
    public Pago procesarPago(Map<String, Object> pagoRequest) {
        // 1. Obtener datos del request
        String token = (String) pagoRequest.get("token");
        Long deudaId = Long.valueOf(pagoRequest.get("deudaId").toString());
        String email = (String) pagoRequest.get("email");

        // Validar deuda
        Deuda deuda = deudaRepository.findById(deudaId)
                .orElseThrow(() -> new RuntimeException("Deuda no encontrada"));

        if (!"PENDIENTE".equals(deuda.getEstado())) {
            throw new RuntimeException("La deuda ya no está pendiente");
        }

        // --- LÓGICA HÍBRIDA ---
        if (token != null && token.startsWith("tkn_test_simulado")) {
            // MODO DEV: Simulación de éxito
            System.out.println("MODO DEV: Simulando pago exitoso para token: " + token);
            return registrarPagoLocal(deuda, "TARJETA_SIMULADA");
        }

        // 2. Crear cargo en Culqi (MODO REAL)
        try {
            String url = "https://api.culqi.com/v2/charges";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(CULQI_PRIVATE_KEY);

            // Monto en céntimos
            long amountInCents = deuda.getMonto().multiply(new BigDecimal(100)).longValue();

            Map<String, Object> body = new HashMap<>();
            body.put("amount", amountInCents);
            body.put("currency_code", "PEN");
            body.put("email", email);
            body.put("source_id", token);
            body.put("description", deuda.getTipo() + " - " + deuda.getPeriodo());

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    org.springframework.http.HttpMethod.POST,
                    request,
                    new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                // Pago exitoso en Culqi
                System.out.println("Cargo creado en Culqi: " + response.getBody().get("id"));
                return registrarPagoLocal(deuda, "TARJETA_CREDITO");
            } else {
                throw new RuntimeException("Error al procesar el pago con Culqi");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error en la pasarela de pagos: " + e.getMessage());
        }
    }

    private Pago registrarPagoLocal(Deuda deuda, String metodoPago) {
        // 3. Registrar el Pago en BD local
        Pago pago = new Pago();
        pago.setIdTramite(deuda.getId());
        pago.setMonto(deuda.getMonto());
        pago.setMetodoPago(metodoPago);
        Pago pagoGuardado = pagoRepository.save(pago);

        // 4. Actualizar estado de la Deuda
        deuda.setEstado("PAGADO");
        deudaRepository.save(deuda);

        return pagoGuardado;
    }
}
