package microservice.usuario.service;

import microservice.usuario.model.Session;
import microservice.usuario.model.Usuario;
import microservice.usuario.repository.SessionRepository;
import microservice.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutenticacionServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private AutenticacionService autenticacionService;

    @Test
    void loginCreaSesionCuandoCredencialesSonValidas() {
        Usuario usuario = new Usuario(1L, "Ana", "Rojas", "ana@mail.com", "123", null);

        when(usuarioRepository.findByCorreoUsuario("ana@mail.com")).thenReturn(Optional.of(usuario));
        when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Session> resultado = autenticacionService.login("ana@mail.com", "123");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getToken()).isNotBlank();
        assertThat(resultado.get().getIdUsuario()).isEqualTo(usuario.getIdUsuario());
    }

    @Test
    void loginRetornaVacioCuandoCredencialesFallan() {
        Usuario usuario = new Usuario(1L, "Ana", "Rojas", "ana@mail.com", "123", null);

        when(usuarioRepository.findByCorreoUsuario("ana@mail.com")).thenReturn(Optional.of(usuario));

        assertThat(autenticacionService.login("ana@mail.com", "mala")).isEmpty();
    }

    @Test
    void logoutCierraSesionYValidaToken() {
        Session session = new Session(1L, "token", LocalDateTime.now(), LocalDateTime.now().plusHours(1), true, 1L);

        when(sessionRepository.findByToken("token")).thenReturn(Optional.of(session));
        when(sessionRepository.findByToken("otro")).thenReturn(Optional.empty());

        assertThat(autenticacionService.validarToken("token")).isTrue();
        assertThat(autenticacionService.logout("token")).isTrue();
        assertThat(session.isActiva()).isFalse();
        assertThat(autenticacionService.logout("otro")).isFalse();
        assertThat(autenticacionService.validarToken("otro")).isFalse();

        verify(sessionRepository).save(session);
    }

    @Test
    void recuperarContrasenaActualizaUsuario() {
        Usuario usuario = new Usuario(1L, "Ana", "Rojas", "ana@mail.com", "123", null);

        when(usuarioRepository.findByCorreoUsuario("ana@mail.com")).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByCorreoUsuario("nadie@mail.com")).thenReturn(Optional.empty());

        autenticacionService.recuperarContrasena("ana@mail.com", "nueva");

        assertThat(usuario.getContraseñaUsuario()).isEqualTo("nueva");
        verify(usuarioRepository).save(usuario);

        assertThatThrownBy(() -> autenticacionService.recuperarContrasena("nadie@mail.com", "x"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
