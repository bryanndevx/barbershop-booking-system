package com.bytr.barberia_api.service;

import com.bytr.barberia_api.dto.servicio.ServicioRequest;
import com.bytr.barberia_api.dto.servicio.ServicioResponse;
import com.bytr.barberia_api.exception.RecursoNoEncontradoException;
import com.bytr.barberia_api.model.Servicio;
import com.bytr.barberia_api.repository.ServicioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicioServiceTest {

    @Mock
    private ServicioRepository servicioRepository;

    @InjectMocks
    private ServicioService servicioService;

    @Test
    void crear_conDatosValidos_creaServicioExitosamente() {
        ServicioRequest request = new ServicioRequest(
                "Corte Clasico", "Descripcion", new BigDecimal("25.00"), 30);

        when(servicioRepository.save(any(Servicio.class))).thenAnswer(invocacion -> {
            Servicio s = invocacion.getArgument(0);
            s.setId(1L);
            return s;
        });

        ServicioResponse response = servicioService.crear(request);

        assertNotNull(response);
        assertEquals("Corte Clasico", response.nombre());
        assertTrue(response.activo());
        verify(servicioRepository, times(1)).save(any(Servicio.class));
    }

    @Test
    void obtenerPorId_conIdInexistente_lanzaExcepcion() {
        when(servicioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> servicioService.obtenerPorId(99L));
    }

    @Test
    void listarActivos_devuelveSoloServiciosActivos() {
        Servicio servicio1 = Servicio.builder()
                .id(1L).nombre("Corte").precio(new BigDecimal("20.00"))
                .duracionMinutos(30).activo(true).build();

        when(servicioRepository.findByActivoTrue()).thenReturn(List.of(servicio1));

        List<ServicioResponse> resultado = servicioService.listarActivos();

        assertEquals(1, resultado.size());
        assertEquals("Corte", resultado.get(0).nombre());
        verify(servicioRepository, times(1)).findByActivoTrue();
    }
}