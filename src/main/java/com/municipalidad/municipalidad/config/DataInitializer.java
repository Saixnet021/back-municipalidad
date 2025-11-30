package com.municipalidad.municipalidad.config;

import com.municipalidad.municipalidad.entity.Deuda;
import com.municipalidad.municipalidad.repository.DeudaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(DeudaRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                Deuda d1 = new Deuda();
                d1.setDniContribuyente("12345678");
                d1.setPeriodo("2023-01");
                d1.setMonto(new BigDecimal("150.00"));
                d1.setEstado("PENDIENTE");
                d1.setTipo("Arbitrios Municipales");

                Deuda d2 = new Deuda();
                d2.setDniContribuyente("12345678");
                d2.setPeriodo("2023-02");
                d2.setMonto(new BigDecimal("150.00"));
                d2.setEstado("PENDIENTE");
                d2.setTipo("Arbitrios Municipales");

                Deuda d3 = new Deuda();
                d3.setDniContribuyente("87654321");
                d3.setPeriodo("2023-01");
                d3.setMonto(new BigDecimal("200.00"));
                d3.setEstado("PENDIENTE");
                d3.setTipo("Impuesto Predial");

                repository.save(d1);
                repository.save(d2);
                repository.save(d3);
                System.out.println("Datos de prueba de Deuda inicializados.");
            }
        };
    }
}
