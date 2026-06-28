package microservice.usuario.controller;

import microservice.usuario.model.Role;
import microservice.usuario.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RoleControllerTest {
    private RoleService roleService;
    private RoleController roleController;

    @BeforeEach
    void setUp() {
        roleService = mock(RoleService.class);
        roleController = new RoleController();
        ReflectionTestUtils.setField(roleController, "roleService", roleService);
    }

    @Test
    void getRolesRetornaOkONoContent() {
        Role role = role();

        when(roleService.listarRoles()).thenReturn(List.of(role), List.of());

        ResponseEntity<List<Role>> conDatos = roleController.getRoles();
        ResponseEntity<List<Role>> sinDatos = roleController.getRoles();

        assertThat(conDatos.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(conDatos.getBody()).containsExactly(role);
        assertThat(sinDatos.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void postGetPutDeleteRoleCubrenRamas() {
        Role role = role();

        when(roleService.guardarRole(role)).thenReturn(role);
        when(roleService.findById(1L)).thenReturn(Optional.of(role));
        when(roleService.findById(99L)).thenReturn(Optional.empty());
        when(roleService.actualizarRole(1L, role)).thenReturn(role);
        when(roleService.actualizarRole(99L, role)).thenReturn(null);

        assertThat(roleController.postRole(role).getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(roleController.getRole(1L).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(roleController.getRole(99L).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(roleController.putRole(1L, role).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(roleController.putRole(99L, role).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(roleController.deleteRole(1L).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(roleController.deleteRole(99L).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(roleService).eliminarRole(1L);
    }

    private Role role() {
        return new Role(1L, "ADMIN", "Administrador");
    }
}
