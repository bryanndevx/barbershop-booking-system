package com.bytr.barberia_api.service;

import com.bytr.barberia_api.dto.cita.CambiarEstadoRequest;
import com.bytr.barberia_api.dto.cita.CitaRequest;
import com.bytr.barberia_api.dto.cita.CitaResponse;
import com.bytr.barberia_api.enums.EstadoCita;
import com.bytr.barberia_api.exception.HorarioNoDisponibleException;
import com.bytr.barberia_api.exception.RecursoNoEncontradoException;
import com.bytr.barberia_api.model.Cita;
import com.bytr.barberia_api.model.Servicio;
import com.bytr.barberia_api.model.Usuario;
import com.bytr.barberia_api.repository.CitaRepository;
import com.bytr.barberia_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ServicioService servicioService;

    public CitaResponse crear(String emailCliente, CitaRequest request) {
        Usuario cliente = usuarioRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        Servicio servicio = servicioService.obtenerPorId(request.servicioId());

        var inicio = request.fechaHoraInicio();
        var fin = inicio.plusMinutes(servicio.getDuracionMinutos());

        List<Cita> choques = citaRepository.findCitasQueChocan(inicio, fin);
        if (!choques.isEmpty()) {
            throw new HorarioNoDisponibleException(
                    "El horario seleccionado ya no esta disponible. Por favor elige otro horario");
        }

        Cita cita = Cita.builder()
                .cliente(cliente)
                .servicio(servicio)
                .fechaHoraInicio(inicio)
                .fechaHoraFin(fin)
                .estado(EstadoCita.PENDIENTE)
                .notas(request.notas())
                .build();

        return mapearAResponse(citaRepository.save(cita));
    }

    public List<CitaResponse> listarPorCliente(String emailCliente) {
        Usuario cliente = usuarioRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        return citaRepository.findByClienteId(cliente.getId()).stream()
                .map(this::mapearAResponse)
                .toList();
    }

    public List<CitaResponse> listarTodas() {
        return citaRepository.findAll().stream()
                .map(this::mapearAResponse)
                .toList();
    }

    public void cancelar(Long citaId, String emailCliente) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cita no encontrada con id: " + citaId));

        if (!cita.getCliente().getEmail().equals(emailCliente)) {
            throw new RecursoNoEncontradoException("Cita no encontrada con id: " + citaId);
        }

        cita.setEstado(EstadoCita.CANCELADA);
        citaRepository.save(cita);
    }

    public CitaResponse cambiarEstado(Long citaId, CambiarEstadoRequest request) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cita no encontrada con id: " + citaId));

        cita.setEstado(request.nuevoEstado());
        return mapearAResponse(citaRepository.save(cita));
    }

    private CitaResponse mapearAResponse(Cita cita) {
        return new CitaResponse(
                cita.getId(),
                cita.getCliente().getNombreCompleto(),
                cita.getServicio().getNombre(),
                cita.getFechaHoraInicio(),
                cita.getFechaHoraFin(),
                cita.getEstado(),
                cita.getNotas()
        );
    }
}