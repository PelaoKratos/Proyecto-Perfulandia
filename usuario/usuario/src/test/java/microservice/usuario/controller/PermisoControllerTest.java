package microservice.usuario.controller;

import microservice.usuario.model.Permiso;
import microservice.usuario.service.PermisoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PermisoControllerTest {
    private PermisoService permisoService;
    private PermisoController permisoController;

    @BeforeEach
    void setUp() {
        permisoService = mock(PermisoService.class);
        permisoController = new PermisoController(permisoService);
    }

    @Test
    void getPermisosRetornaOkONoContent() {
        Permiso permiso = permiso();

        when(permisoService.listarPermisos()).thenReturn(List.of(permiso), List.of());

        ResponseEntity<List<Permiso>> conDatos = permisoController.getPermisos();
        ResponseEntity<List<Permiso>> sinDatos = permisoController.getPermisos();

        assertThat(conDatos.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(conDatos.getBody()).containsExactly(permiso);
        assertThat(sinDatos.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void postGetPutDeletePermisoCubrenRamas() {
        Permiso permiso = permiso();

        when(permisoService.guardarPermiso(permiso)).thenReturn(permiso);
        when(permisoService.findById(1L)).thenReturn(Optional.of(permiso));
        when(permisoService.findById(99L)).thenReturn(Optional.empty());
        when(permisoService.actualizarPermiso(1L, permiso)).thenReturn(permiso);
        when(permisoService.actualizarPermiso(99L, permiso)).thenReturn(null);
        when(permisoService.eliminarPermiso(1L)).thenReturn(true);
        when(permisoService.eliminarPermiso(99L)).thenReturn(false);

        assertThat(permisoController.postPermiso(permiso).getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(permisoController.getPermiso(1L).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(permisoController.getPermiso(99L).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(permisoController.putPermiso(1L, permiso).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(permisoController.putPermiso(99L, permiso).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(permisoController.deletePermiso(1L).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(permisoController.deletePermiso(99L).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private Permiso permiso() {
        return new Permiso(1L, "usuarios", "crear");
    }
}
