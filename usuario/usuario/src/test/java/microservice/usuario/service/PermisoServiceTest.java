package microservice.usuario.service;

import microservice.usuario.model.Permiso;
import microservice.usuario.repository.PermisoRepository;
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
class PermisoServiceTest {
    @Mock
    private PermisoRepository permisoRepository;

    @InjectMocks
    private PermisoService permisoService;

    @Test
    void guardaListaBuscaYActualizaPermisos() {
        Permiso permiso = new Permiso(1L, "usuarios", "leer");
        Permiso cambios = new Permiso(null, "roles", "crear");

        when(permisoRepository.save(permiso)).thenReturn(permiso);
        when(permisoRepository.findAll()).thenReturn(List.of(permiso));
        when(permisoRepository.findById(1L)).thenReturn(Optional.of(permiso));
        when(permisoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(permisoService.guardarPermiso(permiso)).isEqualTo(permiso);
        assertThat(permisoService.listarPermisos()).containsExactly(permiso);
        assertThat(permisoService.findById(1L)).contains(permiso);
        assertThat(permisoService.actualizarPermiso(1L, cambios).getModulo()).isEqualTo("roles");
        assertThat(permisoService.actualizarPermiso(99L, cambios)).isNull();
    }

    @Test
    void eliminaPermisoSiExiste() {
        Permiso permiso = new Permiso(1L, "usuarios", "leer");

        when(permisoRepository.findById(1L)).thenReturn(Optional.of(permiso));
        when(permisoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(permisoService.eliminarPermiso(1L)).isTrue();
        assertThat(permisoService.eliminarPermiso(99L)).isFalse();

        verify(permisoRepository).delete(permiso);
    }
}
