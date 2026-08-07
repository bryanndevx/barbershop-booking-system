package com.bytr.barberia_api.controller;

import com.bytr.barberia_api.dto.usuario.AuthResponse;
import com.bytr.barberia_api.dto.usuario.LoginRequest;
import com.bytr.barberia_api.dto.usuario.RegistroRequest;
import com.bytr.barberia_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticacion",description = "Endpoints de registro y login")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Registrar un nuevo cliente")
    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        AuthResponse response = authService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Iniciar Sesion")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}