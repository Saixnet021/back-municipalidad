package com.municipalidad.municipalidad.repository;

import com.municipalidad.municipalidad.entity.Deuda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeudaRepository extends JpaRepository<Deuda, Long> {
    List<Deuda> findByDniContribuyenteAndEstado(String dniContribuyente, String estado);
}
