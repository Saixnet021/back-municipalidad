package com.municipalidad.municipalidad.repository;

import com.municipalidad.municipalidad.entity.TramiteLicencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TramiteLicenciaRepository extends JpaRepository<TramiteLicencia, Long> {
}
