package com.bytr.barberia_api.service;

import com.bytr.barberia_api.dto.cita.CitaRequest;
import com.bytr.barberia_api.dto.cita.CitaResponse;
import com.bytr.barberia_api.enums.EstadoCita;
import com.bytr.barberia_api.enums.Rol;
import com.bytr.barberia_api.exception.HorarioNoDisponibleException;
import com.bytr.barberia_api.model.Cita;
import com.bytr.barberia_api.model.Servicio;
import com.bytr.barberia_api.model.Usuario;
import com.bytr.barberia_api.repository.CitaRepository;
import com.bytr.barberia_api.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ServicioService servicioService;

    @InjectMocks
    private CitaService citaService;

    @Test
    void crear_conHorarioDisponible_creaCitaExitosamente() {
        Usuario cliente = Usuario.builder()
                .id(1L).nombreCompleto("Juan Perez").email("juan@ejemplo.com")
                .rol(Rol.ROLE_CLIENTE).build();

        Servicio servicio = Servicio.builder()
                .id(1L).nombre("Corte Clasico").duracionMinutos(30).activo(true).build();

        CitaRequest request = new CitaRequest(
                1L, LocalDateTime.of(2026, 8, 10, 15, 0), "Primera vez");

        when(usuarioRepository.findByEmail("juan@ejemplo.com")).thenReturn(Optional.of(cliente));
        when(servicioService.obtenerPorId(1L)).thenReturn(servicio);
        when(citaRepository.findCitasQueChocan(any(), any())).thenReturn(List.of());
        when(citaRepository.save(any(Cita.class))).thenAnswer(invocacion -> {
            Cita c = invocacion.getArgument(0);
            c.setId(1L);
            return c;
        });

        CitaResponse response = citaService.crear("juan@ejemplo.com", request);

        assertNotNull(response);
        assertEquals(EstadoCita.PENDIENTE, response.estado());
        assertEquals("Juan Perez", response.clienteNombre());
        verify(citaRepository, times(1)).save(any(Cita.class));
    }

    @Test
    void crear_conHorarioOcupado_lanzaExcepcion() {
        Usuario cliente = Usuario.builder()
                .id(1L).nombreCompleto("Juan Perez").email("juan@ejemplo.com")
                .rol(Rol.ROLE_CLIENTE).build();

        Servicio servicio = Servicio.builder()
                .id(1L).nombre("Corte Clasico").duracionMinutos(30).activo(true).build();

        Cita citaExistente = Cita.builder()
                .id(5L).estado(EstadoCita.CONFIRMADA).build();

        CitaRequest request = new CitaRequest(
                1L, LocalDateTime.of(2026, 8, 10, 15, 0), "Primera vez");

        when(usuarioRepository.findByEmail("juan@ejemplo.com")).thenReturn(Optional.of(cliente));
        when(servicioService.obtenerPorId(1L)).thenReturn(servicio);
        when(citaRepository.findCitasQueChocan(any(), any())).thenReturn(List.of(citaExistente));

        assertThrows(HorarioNoDisponibleException.class,
                () -> citaService.crear("juan@ejemplo.com", request));
        verify(citaRepository, never()).save(any(Cita.class));
    }

    @Test
    void cancelar_conCitaDeOtroCliente_lanzaExcepcion() {
        Usuario dueñoReal = Usuario.builder()
                .id(1L).email("dueño@ejemplo.com").build();

        Cita cita = Cita.builder()
                .id(10L).cliente(dueñoReal).estado(EstadoCita.PENDIENTE).build();

        when(citaRepository.findById(10L)).thenReturn(Optional.of(cita));

        assertThrows(com.bytr.barberia_api.exception.RecursoNoEncontradoException.class,
                () -> citaService.cancelar(10L, "otro@ejemplo.com"));
        verify(citaRepository, never()).save(any(Cita.class));
    }
}