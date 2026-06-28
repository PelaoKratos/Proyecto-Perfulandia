package microservice.cliente.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

class ClienteModelTest {

    @Test
    void clienteConstructorVacio_deberiaInicializarDireccionesYActivoPorDefecto() {
        Cliente cliente = new Cliente();

        assertThat(cliente.isActivo()).isTrue();
        assertThat(cliente.getDirecciones()).isEmpty();
    }

    @Test
    void clienteConstructorConParametros_deberiaAsignarDatosPrincipales() {
        Cliente cliente = new Cliente(1L, 10L, "11111111-1", "Juan", "Perez", "juan@example.com",
                "123456789", "ACTIVO", true);

        assertThat(cliente.getIdCliente()).isEqualTo(1L);
        assertThat(cliente.getIdUsuario()).isEqualTo(10L);
        assertThat(cliente.getRut()).isEqualTo("11111111-1");
        assertThat(cliente.getNombre()).isEqualTo("Juan");
        assertThat(cliente.getApellido()).isEqualTo("Perez");
        assertThat(cliente.getCorreo()).isEqualTo("juan@example.com");
        assertThat(cliente.getTelefono()).isEqualTo("123456789");
        assertThat(cliente.getEstado()).isEqualTo("ACTIVO");
        assertThat(cliente.isActivo()).isTrue();
    }

    @Test
    void clienteSettersBasicos_deberianAsignarDatos() {
        Cliente cliente = new Cliente();

        cliente.setIdCliente(5L);
        cliente.setIdUsuario(8L);
        cliente.setRut("22222222-2");
        cliente.setNombre("Ana");
        cliente.setApellido("Gomez");
        cliente.setCorreo("ana@example.com");
        cliente.setTelefono("987654321");
        cliente.setEstado("INACTIVO");
        cliente.setActivo(false);

        assertThat(cliente.getIdCliente()).isEqualTo(5L);
        assertThat(cliente.getIdUsuario()).isEqualTo(8L);
        assertThat(cliente.getRut()).isEqualTo("22222222-2");
        assertThat(cliente.getNombre()).isEqualTo("Ana");
        assertThat(cliente.getApellido()).isEqualTo("Gomez");
        assertThat(cliente.getCorreo()).isEqualTo("ana@example.com");
        assertThat(cliente.getTelefono()).isEqualTo("987654321");
        assertThat(cliente.getEstado()).isEqualTo("INACTIVO");
        assertThat(cliente.isActivo()).isFalse();
    }

    @Test
    void activarYDesactivar_deberianActualizarEstadoYFlagActivo() {
        Cliente cliente = new Cliente();

        cliente.desactivar();
        assertThat(cliente.isActivo()).isFalse();
        assertThat(cliente.getEstado()).isEqualTo("INACTIVO");

        cliente.activar();
        assertThat(cliente.isActivo()).isTrue();
        assertThat(cliente.getEstado()).isEqualTo("ACTIVO");
    }

    @Test
    void actualizarDatosPersonales_deberiaActualizarSoloValoresNoNulos() {
        Cliente cliente = new Cliente(1L, 10L, "11111111-1", "Juan", "Perez", "juan@example.com",
                "123456789", "ACTIVO", true);

        cliente.actualizarDatosPersonales(null, "Carlos", null, "carlos@example.com", null, "INACTIVO");

        assertThat(cliente.getRut()).isEqualTo("11111111-1");
        assertThat(cliente.getNombre()).isEqualTo("Carlos");
        assertThat(cliente.getApellido()).isEqualTo("Perez");
        assertThat(cliente.getCorreo()).isEqualTo("carlos@example.com");
        assertThat(cliente.getTelefono()).isEqualTo("123456789");
        assertThat(cliente.getEstado()).isEqualTo("INACTIVO");
    }

    @Test
    void direcciones_deberianAsignarRelacionConCliente() {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);
        DireccionCliente direccion = new DireccionCliente();

        direccion.setIdDireccion(10L);
        direccion.setIdCliente(1L);
        direccion.setCalle("Av. Siempre Viva 123");
        direccion.setComuna("Santiago");
        direccion.setCiudad("Santiago");
        direccion.setRegion("Metropolitana");
        direccion.setPrincipal(true);

        cliente.setDirecciones(List.of(direccion));

        assertThat(cliente.getDirecciones()).containsExactly(direccion);
        assertThat(direccion.getCliente()).isSameAs(cliente);
        assertThat(direccion.getIdCliente()).isEqualTo(1L);
        assertThat(direccion.getIdDireccion()).isEqualTo(10L);
        assertThat(direccion.getCalle()).isEqualTo("Av. Siempre Viva 123");
        assertThat(direccion.getComuna()).isEqualTo("Santiago");
        assertThat(direccion.getCiudad()).isEqualTo("Santiago");
        assertThat(direccion.getRegion()).isEqualTo("Metropolitana");
        assertThat(direccion.isPrincipal()).isTrue();
    }

    @Test
    void setDireccionesConNulo_deberiaLimpiarColeccion() {
        Cliente cliente = new Cliente();

        cliente.agregarDireccion(new DireccionCliente());
        cliente.setDirecciones(null);

        assertThat(cliente.getDirecciones()).isEmpty();
    }

    @Test
    void agregarDireccionNula_noDeberiaModificarColeccion() {
        Cliente cliente = new Cliente();

        cliente.agregarDireccion(null);

        assertThat(cliente.getDirecciones()).isEmpty();
    }
}
