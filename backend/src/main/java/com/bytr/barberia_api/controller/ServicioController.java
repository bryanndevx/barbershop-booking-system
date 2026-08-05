package com.bytr.barberia_api.controller;

import com.bytr.barberia_api.dto.servicio.ServicioRequest;
import com.bytr.barberia_api.dto.servicio.ServicioResponse;
import com.bytr.barberia_api.service.ServicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
public class ServicioController {

    private final ServicioService servicioService;

    @GetMapping
    public ResponseEntity<List<ServicioResponse>> listarActivos() {
        return ResponseEntity.ok(servicioService.listarActivos());
    }

    @GetMapping("/admin/todos")
    public ResponseEntity<List<ServicioResponse>> listarTodos() {
        return ResponseEntity.ok(servicioService.listarTodos());
    }

    @PostMapping("/admin")
    public ResponseEntity<ServicioResponse> crear(@Valid @RequestBody ServicioRequest request) {
        ServicioResponse response = servicioService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<ServicioResponse> actualizar(@PathVariable Long id,
                                                       @Valid @RequestBody ServicioRequest request) {
        return ResponseEntity.ok(servicioService.actualizar(id, request));
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        servicioService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}