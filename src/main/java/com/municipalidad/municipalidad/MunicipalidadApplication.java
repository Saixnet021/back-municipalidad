package com.municipalidad.municipalidad;

import com.municipalidad.municipalidad.service.PagoArbitriosService;
import com.municipalidad.municipalidad.service.TramiteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MunicipalidadApplication {

	public static void main(String[] args) {
		SpringApplication.run(MunicipalidadApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(PagoArbitriosService pagoService, TramiteService tramiteService) {
		return args -> {
			pagoService.seedAdmin();
			tramiteService.seedTramites();
		};
	}
}
