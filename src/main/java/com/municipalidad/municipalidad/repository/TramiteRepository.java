package com.municipalidad.municipalidad.repository;

import com.municipalidad.municipalidad.entity.Tramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TramiteRepository extends JpaRepository<Tramite, Long> {
    List<Tramite> findByUsuario(String usuario);

    List<Tramite> findByEstado(String estado);
    
    Tramite findByExpediente(String expediente);
}
