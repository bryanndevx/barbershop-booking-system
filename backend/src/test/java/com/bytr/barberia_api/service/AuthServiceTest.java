package com.bytr.barberia_api.service;

import com.bytr.barberia_api.dto.usuario.AuthResponse;
import com.bytr.barberia_api.dto.usuario.RegistroRequest;
import com.bytr.barberia_api.enums.Rol;
import com.bytr.barberia_api.exception.EmailDuplicadoException;
import com.bytr.barberia_api.model.Usuario;
import com.bytr.barberia_api.repository.UsuarioRepository;
import com.bytr.barberia_api.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void registrar_conEmailNuevo_creaUsuarioExitosamente() {
        RegistroRequest request = new RegistroRequest(
                "Juan Perez", "juan@ejemplo.com", "password123", "987654321");

        when(usuarioRepository.existsByEmail("juan@ejemplo.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hash_encriptado");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocacion -> {
            Usuario u = invocacion.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(jwtUtil.generarToken("juan@ejemplo.com")).thenReturn("token_falso_123");

        AuthResponse response = authService.registrar(request);

        assertNotNull(response);
        assertEquals("token_falso_123", response.token());
        assertEquals("juan@ejemplo.com", response.usuario().email());
        assertEquals(Rol.ROLE_CLIENTE, response.usuario().rol());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void registrar_conEmailYaExistente_lanzaExcepcion() {
        RegistroRequest request = new RegistroRequest(
                "Juan Perez", "juan@ejemplo.com", "password123", "987654321");

        when(usuarioRepository.existsByEmail("juan@ejemplo.com")).thenReturn(true);

        assertThrows(EmailDuplicadoException.class, () -> authService.registrar(request));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}