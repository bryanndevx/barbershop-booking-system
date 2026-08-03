package com.bytr.barberia_api.dto.error;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String mensaje,
        Map<String, String> errores
) {}