package com.municipalidad.municipalidad.repository;

import com.municipalidad.municipalidad.entity.TramiteMesaPartes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TramiteMesaPartesRepository extends JpaRepository<TramiteMesaPartes, Long> {
}
