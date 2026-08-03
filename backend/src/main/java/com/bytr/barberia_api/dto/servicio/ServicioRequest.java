package com.bytr.barberia_api.dto.servicio;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ServicioRequest(

        @NotBlank(message = "El nombre del servicio es obligatorio")
        String nombre,

        String descripcion,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
        BigDecimal precio,

        @NotNull(message = "La duracion es obligatoria")
        @Min(value = 5, message = "La duracion minima es de 5 minutos")
        Integer duracionMinutos
) {}