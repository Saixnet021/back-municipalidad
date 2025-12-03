package com.municipalidad.municipalidad.service;

import com.municipalidad.municipalidad.dto.CreateArbitrioRequest;
import com.municipalidad.municipalidad.dto.PagoArbitriosDTO;
import com.municipalidad.municipalidad.entity.PagoArbitrios;
import com.municipalidad.municipalidad.entity.User;
import com.municipalidad.municipalidad.repository.PagoArbitriosRepository;
import com.municipalidad.municipalidad.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.municipalidad.municipalidad.entity.Role;

import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PagoArbitriosService {

    private final PagoArbitriosRepository repository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PagoArbitriosDTO create(CreateArbitrioRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        // String email = "superadmin@test.com";
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        PagoArbitrios entity = new PagoArbitrios();
        entity.setTipo(request.getTipo());
        entity.setMonto(request.getMonto());
        entity.setEstadoPago(PagoArbitrios.EstadoPago.PENDIENTE);
        entity.setUser(user);

        return mapToDTO(repository.save(entity));
    }

    public List<PagoArbitriosDTO> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<PagoArbitriosDTO> getMyDebts() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return repository.findAll().stream()
                .filter(p -> p.getUser().getId().equals(user.getId()))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PagoArbitriosDTO> getDebtsByDni(String dni) {
        return repository.findAll().stream()
                .filter(p -> p.getUser().getDni().equals(dni) && p.getEstadoPago() == PagoArbitrios.EstadoPago.PENDIENTE)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void pay(Long id) {
        PagoArbitrios entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Arbitrio not found"));
        entity.setEstadoPago(PagoArbitrios.EstadoPago.PAGADO);
        repository.save(entity);
    }

    public void processSimulatedPayment(Long id, com.municipalidad.municipalidad.dto.SimulatedCardPaymentRequest request) {
        PagoArbitrios entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Arbitrio not found"));
        
        // Simulate validation
        if (request.getCardNumber() == null || request.getCardNumber().length() < 16) {
            throw new RuntimeException("Invalid card number");
        }
        
        entity.setEstadoPago(PagoArbitrios.EstadoPago.PAGADO);
        repository.save(entity);
    }

    private PagoArbitriosDTO mapToDTO(PagoArbitrios entity) {
        return new PagoArbitriosDTO(
                entity.getId(),
                entity.getUser().getId(),
                entity.getUser().getFirstname() + " " + entity.getUser().getLastname(),
                entity.getTipo(),
                entity.getMonto(),
                entity.getEstadoPago()
        );
    }

    public void seedAdmin() {
        Optional<User> admin = userRepository.findByEmail("superadmin@test.com");
        if (admin.isEmpty()) {
            User user = new User();
            user.setFirstname("Super");
            user.setLastname("Admin");
            user.setDni("99999999");
            user.setEmail("superadmin@test.com");
            user.setPassword(passwordEncoder.encode("password123"));
            user.setRole(Role.ADMIN);
            userRepository.save(user);
        }
    }
}
