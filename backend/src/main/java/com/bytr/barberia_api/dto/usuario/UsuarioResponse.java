package com.bytr.barberia_api.dto.usuario;

import com.bytr.barberia_api.enums.Rol;

public record UsuarioResponse(
        Long id,
        String nombreCompleto,
        String email,
        String telefono,
        Rol rol
) {}