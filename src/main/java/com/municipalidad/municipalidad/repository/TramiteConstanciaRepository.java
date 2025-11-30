package com.municipalidad.municipalidad.repository;

import com.municipalidad.municipalidad.entity.TramiteConstancia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TramiteConstanciaRepository extends JpaRepository<TramiteConstancia, Long> {
}
