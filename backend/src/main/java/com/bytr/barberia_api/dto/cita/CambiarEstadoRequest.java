package com.bytr.barberia_api.dto.cita;

import com.bytr.barberia_api.enums.EstadoCita;
import jakarta.validation.constraints.NotNull;

public record CambiarEstadoRequest(

        @NotNull(message = "El nuevo estado es obligatorio")
        EstadoCita nuevoEstado
) {}