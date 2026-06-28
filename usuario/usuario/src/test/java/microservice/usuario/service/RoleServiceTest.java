package microservice.usuario.service;

import microservice.usuario.model.Role;
import microservice.usuario.repository.RoleRepository;
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
class RoleServiceTest {
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    void guardaListaYBuscaRoles() {
        Role role = new Role(1L, "ADMIN", "Admin");

        when(roleRepository.save(role)).thenReturn(role);
        when(roleRepository.findAll()).thenReturn(List.of(role));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(roleRepository.findByNombre("ADMIN")).thenReturn(Optional.of(role));

        assertThat(roleService.guardarRole(role)).isEqualTo(role);
        assertThat(roleService.listarRoles()).containsExactly(role);
        assertThat(roleService.findById(1L)).contains(role);
        assertThat(roleService.findByNombre("ADMIN")).contains(role);
    }

    @Test
    void actualizaYEliminaRole() {
        Role existente = new Role(1L, "USER", "Usuario");
        Role cambios = new Role(1L, "ADMIN", "Administrador");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(roleRepository.findById(99L)).thenReturn(Optional.empty());
        when(roleRepository.save(existente)).thenReturn(existente);

        Role resultado = roleService.actualizarRole(1L, cambios);

        assertThat(resultado.getNombre()).isEqualTo("ADMIN");
        assertThat(roleService.actualizarRole(99L, cambios)).isNull();

        roleService.eliminarRole(1L);

        verify(roleRepository).deleteById(1L);
    }
}
