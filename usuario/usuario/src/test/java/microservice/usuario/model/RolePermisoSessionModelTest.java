package microservice.usuario.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class RolePermisoSessionModelTest {

    @Test
    void roleGestionaDatosYPermisos() {
        Role role = new Role();
        Permiso permiso = new Permiso(1L, "usuarios", "crear");

        role.crearRol("ADMIN", "Administrador");
        role.asignarPermiso(permiso);
        role.modificarRol("SUPER_ADMIN", "Super administrador");

        assertThat(role.getNombre()).isEqualTo("SUPER_ADMIN");
        assertThat(role.getDescripcion()).isEqualTo("Super administrador");
        assertThat(role.getPermisos()).containsExactly(permiso);

        role.eliminarRol();

        assertThat(role.getNombre()).isNull();
        assertThat(role.getPermisos()).isEmpty();
    }

    @Test
    void permisoValidaAccesoYGestionaDatos() {
        Permiso permiso = new Permiso();

        permiso.crearPermiso("roles", "leer");

        assertThat(permiso.validarAcceso("ROLES", "LEER")).isTrue();
        assertThat(permiso.validarAcceso("usuarios", "leer")).isFalse();

        permiso.modificarPermiso("usuarios", "editar");
        permiso.eliminarPermiso();

        assertThat(permiso.getModulo()).isNull();
        assertThat(permiso.validarAcceso("usuarios", "editar")).isFalse();
    }

    @Test
    void sessionGeneraCierraYValidaToken() {
        Session session = new Session();

        String token = session.generarToken();

        assertThat(token).isNotBlank();
        assertThat(session.validarSesion()).isTrue();

        session.cerrarSesion();

        assertThat(session.validarSesion()).isFalse();

        session.setActiva(true);
        session.setFechaExpiracion(LocalDateTime.now().minusMinutes(1));

        assertThat(session.validarSesion()).isFalse();
    }

    @Test
    void sessionGestionaIdAliasYValidaSinFecha() {
        Session session = new Session();

        session.setIdSession(10L);
        session.setActiva(true);
        session.setFechaExpiracion(null);

        assertThat(session.getIdSession()).isEqualTo(10L);
        assertThat(session.validarSesion()).isFalse();
    }
}
