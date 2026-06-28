package microservice.usuario.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioModelTest {

    @Test
    void iniciarSesionValidaCredencialesYEstado() {
        Usuario usuario = new Usuario(1L, "Ana", "Rojas", "ana@mail.com", "clave", new Role());

        assertThat(usuario.iniciarSesion("ana@mail.com", "clave")).isTrue();

        usuario.desactivar();

        assertThat(usuario.iniciarSesion("ana@mail.com", "clave")).isFalse();
    }

    @Test
    void actualizaActivaYRecuperaContrasena() {
        Usuario usuario = new Usuario();

        usuario.actualizarDatos("Luis", "Perez", "luis@mail.com");
        usuario.recuperarContrasena("nueva");
        usuario.activar();

        assertThat(usuario.getNombreUsuario()).isEqualTo("Luis");
        assertThat(usuario.getApellidoUsuario()).isEqualTo("Perez");
        assertThat(usuario.getCorreoUsuario()).isEqualTo("luis@mail.com");
        assertThat(usuario.getContrasena()).isEqualTo("nueva");
        assertThat(usuario.isEstado()).isTrue();
    }

    @Test
    void prePersistDefineFechaCreacionSiNoExiste() {
        Usuario usuario = new Usuario();

        usuario.prePersist();

        assertThat(usuario.getFechaCreacion()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void prePersistMantieneFechaCreacionExistente() {
        LocalDateTime fecha = LocalDateTime.now().minusDays(1);
        Usuario usuario = new Usuario();
        usuario.setFechaCreacion(fecha);

        usuario.prePersist();

        assertThat(usuario.getFechaCreacion()).isEqualTo(fecha);
    }

    @Test
    void iniciarSesionRechazaDatosInvalidos() {
        Usuario usuario = new Usuario();
        usuario.setEstado(true);

        assertThat(usuario.iniciarSesion("correo@mail.com", "clave")).isFalse();

        usuario.setCorreoUsuario("correo@mail.com");

        assertThat(usuario.iniciarSesion("correo@mail.com", "clave")).isFalse();

        usuario.setContrasena("clave");

        assertThat(usuario.iniciarSesion("otro@mail.com", "clave")).isFalse();
        assertThat(usuario.iniciarSesion("correo@mail.com", "otra")).isFalse();
        assertThat(usuario.iniciarSesion("CORREO@mail.com", "clave")).isTrue();
    }

    @Test
    void gestionaEstadoRolYAliasDeDatos() {
        Usuario usuario = new Usuario();
        Role rol = new Role();
        rol.setIdRol(5L);

        usuario.setRolUsuario(rol);
        usuario.setEstado(false);
        usuario.setNombre("Pedro");
        usuario.setEmail("pedro@mail.com");
        usuario.setContrasena("clave");

        assertThat(usuario.getRolUsuario()).isEqualTo(rol);
        assertThat(usuario.getIdRol()).isEqualTo(5L);
        assertThat(usuario.isEstado()).isFalse();
        assertThat(usuario.getNombre()).isEqualTo("Pedro");
        assertThat(usuario.getEmail()).isEqualTo("pedro@mail.com");
        assertThat(usuario.getContrasena()).isEqualTo("clave");

        usuario.setRolUsuario(null);

        assertThat(usuario.getRolUsuario()).isNull();
        assertThat(usuario.getIdRol()).isNull();
    }
}
