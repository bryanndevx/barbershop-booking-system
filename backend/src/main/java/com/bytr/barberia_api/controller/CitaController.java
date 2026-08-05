package com.bytr.barberia_api.controller;

import com.bytr.barberia_api.dto.cita.CambiarEstadoRequest;
import com.bytr.barberia_api.dto.cita.CitaRequest;
import com.bytr.barberia_api.dto.cita.CitaResponse;
import com.bytr.barberia_api.service.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    @PostMapping
    public ResponseEntity<CitaResponse> crear(@Valid @RequestBody CitaRequest request,
                                              Authentication authentication) {
        String email = authentication.getName();
        CitaResponse response = citaService.crear(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/mis-citas")
    public ResponseEntity<List<CitaResponse>> misCitas(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(citaService.listarPorCliente(email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        citaService.cancelar(id, email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/todas")
    public ResponseEntity<List<CitaResponse>> listarTodas() {
        return ResponseEntity.ok(citaService.listarTodas());
    }

    @PatchMapping("/admin/{id}/estado")
    public ResponseEntity<CitaResponse> cambiarEstado(@PathVariable Long id,
                                                      @Valid @RequestBody CambiarEstadoRequest request) {
        return ResponseEntity.ok(citaService.cambiarEstado(id, request));
    }
}