package com.bytr.barberia_api.service;

import com.bytr.barberia_api.dto.usuario.*;
import com.bytr.barberia_api.enums.Rol;
import com.bytr.barberia_api.exception.EmailDuplicadoException;
import com.bytr.barberia_api.model.Usuario;
import com.bytr.barberia_api.repository.UsuarioRepository;
import com.bytr.barberia_api.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse registrar(RegistroRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new EmailDuplicadoException("Ya existe una cuenta registrada con el email: " + request.email());
        }

        Usuario usuario = Usuario.builder()
                .nombreCompleto(request.nombreCompleto())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .telefono(request.telefono())
                .rol(Rol.ROLE_CLIENTE)
                .build();

        usuarioRepository.save(usuario);

        String token = jwtUtil.generarToken(usuario.getEmail());
        return new AuthResponse(token, mapearAUsuarioResponse(usuario));
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtUtil.generarToken(usuario.getEmail());
        return new AuthResponse(token, mapearAUsuarioResponse(usuario));
    }

    private UsuarioResponse mapearAUsuarioResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombreCompleto(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getRol()
        );
    }
}