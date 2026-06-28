package microservice.usuario.controller;

import microservice.usuario.model.Session;
import microservice.usuario.service.AutenticacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AutenticacionControllerTest {
    private AutenticacionService autenticacionService;
    private AutenticacionController autenticacionController;

    @BeforeEach
    void setUp() {
        autenticacionService = mock(AutenticacionService.class);
        autenticacionController = new AutenticacionController(autenticacionService);
    }

    @Test
    void loginRetornaOkOUnauthorized() {
        Session session = new Session(1L, "token", LocalDateTime.now(), LocalDateTime.now().plusHours(1), true, 1L);
        AutenticacionController.LoginRequest request = new AutenticacionController.LoginRequest("ana@mail.com", "123");
        AutenticacionController.LoginRequest requestMalo = new AutenticacionController.LoginRequest("ana@mail.com", "mala");

        when(autenticacionService.login("ana@mail.com", "123")).thenReturn(Optional.of(session));
        when(autenticacionService.login("ana@mail.com", "mala")).thenReturn(Optional.empty());

        assertThat(autenticacionController.login(request).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(autenticacionController.login(request).getBody()).isEqualTo(session);
        assertThat(autenticacionController.login(requestMalo).getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void logoutRetornaOkONotFound() {
        AutenticacionController.TokenRequest request = new AutenticacionController.TokenRequest("token");
        AutenticacionController.TokenRequest requestMalo = new AutenticacionController.TokenRequest("malo");

        when(autenticacionService.logout("token")).thenReturn(true);
        when(autenticacionService.logout("malo")).thenReturn(false);

        assertThat(autenticacionController.logout(request).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(autenticacionController.logout(requestMalo).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void recuperarContrasenaYValidarToken() {
        AutenticacionController.RecuperarContrasenaRequest recuperar =
                new AutenticacionController.RecuperarContrasenaRequest("ana@mail.com", "nueva");
        AutenticacionController.TokenRequest token = new AutenticacionController.TokenRequest("token");

        when(autenticacionService.validarToken("token")).thenReturn(true);

        assertThat(autenticacionController.recuperarContrasena(recuperar).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(autenticacionController.validarToken(token).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(autenticacionController.validarToken(token).getBody()).isTrue();
        verify(autenticacionService).recuperarContrasena("ana@mail.com", "nueva");
    }
}
