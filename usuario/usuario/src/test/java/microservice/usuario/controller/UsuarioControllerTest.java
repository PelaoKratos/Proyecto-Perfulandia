package microservice.usuario.controller;

import microservice.usuario.model.Usuario;
import microservice.usuario.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UsuarioControllerTest {
    private UsuarioService usuarioService;
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        usuarioService = mock(UsuarioService.class);
        usuarioController = new UsuarioController();
        ReflectionTestUtils.setField(usuarioController, "usuarioService", usuarioService);
    }

    @Test
    void getUsuariosRetornaOkCuandoHayDatosYNoContentCuandoEstaVacio() {
        Usuario usuario = usuario();

        when(usuarioService.listarUsuarios()).thenReturn(List.of(usuario), List.of());

        ResponseEntity<List<Usuario>> conDatos = usuarioController.getUsuarios();
        ResponseEntity<List<Usuario>> sinDatos = usuarioController.getUsuarios();

        assertThat(conDatos.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(conDatos.getBody()).containsExactly(usuario);
        assertThat(sinDatos.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void postUsuarioRetornaCreatedOConflict() {
        Usuario usuario = usuario();

        when(usuarioService.guardarUsuario(usuario)).thenReturn(usuario).thenThrow(new RuntimeException("duplicado"));

        ResponseEntity<Usuario> creado = usuarioController.postUsuario(usuario);
        ResponseEntity<Usuario> conflicto = usuarioController.postUsuario(usuario);

        assertThat(creado.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(creado.getBody()).isEqualTo(usuario);
        assertThat(conflicto.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void getPutYDeleteUsuarioCubrenEncontradoYNoEncontrado() {
        Usuario usuario = usuario();

        when(usuarioService.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioService.findById(99L)).thenReturn(Optional.empty());
        when(usuarioService.updateUsuario(1L, usuario)).thenReturn(usuario);
        when(usuarioService.updateUsuario(99L, usuario)).thenReturn(null);
        when(usuarioService.deleteUsuario(1L)).thenReturn(usuario);
        when(usuarioService.deleteUsuario(99L)).thenReturn(null);

        assertThat(usuarioController.getUsuario(1L).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(usuarioController.getUsuario(99L).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(usuarioController.putUsuario(1L, usuario).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(usuarioController.putUsuario(99L, usuario).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(usuarioController.deleteUsuario(1L).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(usuarioController.deleteUsuario(99L).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void activarYDesactivarUsuarioCubrenEncontradoYNoEncontrado() {
        Usuario usuario = usuario();

        when(usuarioService.activarUsuario(1L)).thenReturn(usuario);
        when(usuarioService.activarUsuario(99L)).thenReturn(null);
        when(usuarioService.desactivarUsuario(1L)).thenReturn(usuario);
        when(usuarioService.desactivarUsuario(99L)).thenReturn(null);

        assertThat(usuarioController.activarUsuario(1L).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(usuarioController.activarUsuario(99L).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(usuarioController.desactivarUsuario(1L).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(usuarioController.desactivarUsuario(99L).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private Usuario usuario() {
        return new Usuario(1L, "Ana", "Rojas", "ana@mail.com", "123", null);
    }
}
