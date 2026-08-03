package com.bytr.barberia_api.dto.usuario;

public record AuthResponse(
        String token,
        String tipo,
        UsuarioResponse usuario
) {
    public AuthResponse(String token, UsuarioResponse usuario) {
        this(token, "Bearer", usuario);
    }
}