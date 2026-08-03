package com.bytr.barberia_api.dto.cita;

import com.bytr.barberia_api.enums.EstadoCita;

import java.time.LocalDateTime;

public record CitaResponse(
        Long id,
        String clienteNombre,
        String servicioNombre,
        LocalDateTime fechaHoraInicio,
        LocalDateTime fechaHoraFin,
        EstadoCita estado,
        String notas
) {}