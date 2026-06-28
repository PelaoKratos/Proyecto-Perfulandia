package microservice.usuario.service;

import microservice.usuario.model.Role;
import microservice.usuario.model.Usuario;
import microservice.usuario.repository.RoleRepository;
import microservice.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void guardarUsuarioResuelveRolPorId() {
        Role role = new Role(1L, "ADMIN", "Admin");
        Usuario usuario = new Usuario(null, "Ana", "Rojas", "ana@mail.com", "123", role);

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario resultado = usuarioService.guardarUsuario(usuario);

        assertThat(resultado.getRolUsuario()).isEqualTo(role);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void listarYBuscarUsuarios() {
        Usuario usuario = new Usuario(1L, "Ana", "Rojas", "ana@mail.com", "123", null);

        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        assertThat(usuarioService.listarUsuarios()).containsExactly(usuario);
        assertThat(usuarioService.findById(1L)).contains(usuario);
    }

    @Test
    void actualizaUsuarioExistenteYRetornaNullSiNoExiste() {
        Usuario existente = new Usuario(1L, "Ana", "Rojas", "ana@mail.com", "123", null);
        Usuario cambios = new Usuario(null, "Luis", "Perez", "luis@mail.com", "456", null);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());
        when(usuarioRepository.save(existente)).thenReturn(existente);

        Usuario resultado = usuarioService.updateUsuario(1L, cambios);

        assertThat(resultado.getNombreUsuario()).isEqualTo("Luis");
        assertThat(usuarioService.updateUsuario(99L, cambios)).isNull();
    }

    @Test
    void eliminaActivaYDesactivaUsuarios() {
        Usuario usuario = new Usuario(1L, "Ana", "Rojas", "ana@mail.com", "123", null);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        assertThat(usuarioService.desactivarUsuario(1L).isEstado()).isFalse();
        assertThat(usuarioService.activarUsuario(1L).isEstado()).isTrue();
        assertThat(usuarioService.deleteUsuario(1L)).isEqualTo(usuario);
        assertThat(usuarioService.deleteUsuario(99L)).isNull();
        assertThat(usuarioService.activarUsuario(99L)).isNull();

        verify(usuarioRepository).delete(usuario);
    }
}
