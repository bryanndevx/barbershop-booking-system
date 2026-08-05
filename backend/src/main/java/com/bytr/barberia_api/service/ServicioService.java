package com.bytr.barberia_api.service;

import com.bytr.barberia_api.dto.servicio.ServicioRequest;
import com.bytr.barberia_api.dto.servicio.ServicioResponse;
import com.bytr.barberia_api.exception.RecursoNoEncontradoException;
import com.bytr.barberia_api.model.Servicio;
import com.bytr.barberia_api.repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioService {

    private final ServicioRepository servicioRepository;

    public List<ServicioResponse> listarActivos() {
        return servicioRepository.findByActivoTrue().stream()
                .map(this::mapearAResponse)
                .toList();
    }

    public List<ServicioResponse> listarTodos() {
        return servicioRepository.findAll().stream()
                .map(this::mapearAResponse)
                .toList();
    }

    public ServicioResponse crear(ServicioRequest request) {
        Servicio servicio = Servicio.builder()
                .nombre(request.nombre())
                .descripcion(request.descripcion())
                .precio(request.precio())
                .duracionMinutos(request.duracionMinutos())
                .activo(true)
                .build();

        return mapearAResponse(servicioRepository.save(servicio));
    }

    public ServicioResponse actualizar(Long id, ServicioRequest request) {
        Servicio servicio = obtenerPorId(id);
        servicio.setNombre(request.nombre());
        servicio.setDescripcion(request.descripcion());
        servicio.setPrecio(request.precio());
        servicio.setDuracionMinutos(request.duracionMinutos());

        return mapearAResponse(servicioRepository.save(servicio));
    }

    public void desactivar(Long id) {
        Servicio servicio = obtenerPorId(id);
        servicio.setActivo(false);
        servicioRepository.save(servicio);
    }

    protected Servicio obtenerPorId(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado con id: " + id));
    }

    private ServicioResponse mapearAResponse(Servicio servicio) {
        return new ServicioResponse(
                servicio.getId(),
                servicio.getNombre(),
                servicio.getDescripcion(),
                servicio.getPrecio(),
                servicio.getDuracionMinutos(),
                servicio.getActivo()
        );
    }
}