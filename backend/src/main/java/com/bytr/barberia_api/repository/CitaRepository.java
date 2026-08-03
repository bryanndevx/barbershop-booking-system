package com.bytr.barberia_api.repository;

import com.bytr.barberia_api.model.Cita;
import com.bytr.barberia_api.enums.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByClienteId(Long clienteId);

    List<Cita> findByEstado(EstadoCita estado);

    @Query("""
        SELECT c FROM Cita c
        WHERE c.estado <> 'CANCELADA'
        AND c.fechaHoraInicio < :fin
        AND c.fechaHoraFin > :inicio
        """)
    List<Cita> findCitasQueChocan(@Param("inicio") LocalDateTime inicio,
                                  @Param("fin") LocalDateTime fin);
}