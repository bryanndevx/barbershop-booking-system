package com.bytr.barberia_api.dto.servicio;

import java.math.BigDecimal;

public record ServicioResponse(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer duracionMinutos,
        Boolean activo
) {}