package com.bytr.barberia_api.dto.cita;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CitaRequest(

        @NotNull(message = "El servicio es obligatorio")
        Long servicioId,

        @NotNull(message = "La fecha y hora de inicio son obligatorias")
        @Future(message = "La fecha de la cita debe ser en el futuro")
        LocalDateTime fechaHoraInicio,

        String notas
) {}