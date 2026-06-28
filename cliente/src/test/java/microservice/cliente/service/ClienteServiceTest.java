package microservice.cliente.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import microservice.cliente.exception.ResourceNotFoundException;
import microservice.cliente.model.Cliente;
import microservice.cliente.model.DireccionCliente;
import microservice.cliente.repository.ClienteRepository;

@DataJpaTest
@Import(ClienteService.class)
class ClienteServiceTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        clienteRepository.deleteAll();

        cliente = new Cliente();
        cliente.setIdUsuario(10L);
        cliente.setRut("11111111-1");
        cliente.setNombre("Juan");
        cliente.setApellido("Perez");
        cliente.setCorreo("juan.perez@example.com");
        cliente.setTelefono("123456789");
        cliente.setEstado("ACTIVO");
    }

    @Test
    void guardarCliente_deberiaGuardarCliente() {
        Cliente guardado = clienteService.guardarCliente(cliente);

        assertThat(guardado.getIdCliente()).isNotNull();
        assertThat(guardado.getIdUsuario()).isEqualTo(10L);
        assertThat(guardado.getRut()).isEqualTo("11111111-1");
        assertThat(guardado.isActivo()).isTrue();
    }

    @Test
    void guardarCliente_deberiaLanzarCuandoClienteEsNulo() {
        assertThatThrownBy(() -> clienteService.guardarCliente(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El cliente no puede ser nulo");
    }

    @Test
    void guardarCliente_deberiaAsociarDireccionesAntesDeGuardar() {
        DireccionCliente direccion = new DireccionCliente();
        direccion.setCalle("Av. Principal 123");
        direccion.setComuna("Santiago");
        direccion.setCiudad("Santiago");
        direccion.setRegion("Metropolitana");
        direccion.setPrincipal(true);

        cliente.setDirecciones(List.of(direccion));

        Cliente guardado = clienteService.guardarCliente(cliente);

        assertThat(guardado.getDirecciones()).singleElement()
                .satisfies(d -> assertThat(d.getCliente()).isSameAs(guardado));
    }

    @Test
    void obtenerClientes_deberiaRetornarListaVaciaAlInicio() {
        List<Cliente> clientes = clienteService.obtenerClientes();

        assertThat(clientes).isEmpty();
    }

    @Test
    void obtenerClientePorId_deberiaRetornarClienteExistente() {
        Cliente guardado = clienteRepository.save(cliente);

        Optional<Cliente> encontrado = clienteService.obtenerClientePorId(guardado.getIdCliente());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getRut()).isEqualTo("11111111-1");
    }

    @Test
    void actualizarCliente_deberiaActualizarDatos() {
        Cliente guardado = clienteRepository.save(cliente);
        Cliente actualizado = new Cliente();
        actualizado.setNombre("Juan Carlos");
        actualizado.setApellido("Gomez");
        actualizado.setCorreo("juan.gomez@example.com");
        actualizado.setTelefono("987654321");
        actualizado.setEstado("ACTIVO");

        Cliente resultado = clienteService.actualizarCliente(guardado.getIdCliente(), actualizado);

        assertThat(resultado.getNombre()).isEqualTo("Juan Carlos");
        assertThat(resultado.getApellido()).isEqualTo("Gomez");
        assertThat(resultado.getCorreo()).isEqualTo("juan.gomez@example.com");
    }

    @Test
    void actualizarCliente_deberiaLanzarCuandoNoExiste() {
        Cliente actualizado = new Cliente();
        actualizado.setNombre("Test");

        assertThatThrownBy(() -> clienteService.actualizarCliente(999L, actualizado))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void eliminarCliente_deberiaEliminarClienteExistente() {
        Cliente guardado = clienteRepository.save(cliente);

        Cliente eliminado = clienteService.eliminarCliente(guardado.getIdCliente());

        assertThat(eliminado.getIdCliente()).isEqualTo(guardado.getIdCliente());
        assertThat(clienteRepository.findById(guardado.getIdCliente())).isEmpty();
    }

    @Test
    void eliminarCliente_deberiaLanzarCuandoNoExiste() {
        assertThatThrownBy(() -> clienteService.eliminarCliente(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cliente no encontrado con id: 999");
    }

    @Test
    void activarYDesactivarCliente_deberiaCambiarEstadoActivo() {
        Cliente guardado = clienteRepository.save(cliente);

        Cliente desactivado = clienteService.desactivarCliente(guardado.getIdCliente());
        assertThat(desactivado.isActivo()).isFalse();
        assertThat(desactivado.getEstado()).isEqualTo("INACTIVO");

        Cliente activado = clienteService.activarCliente(guardado.getIdCliente());
        assertThat(activado.isActivo()).isTrue();
        assertThat(activado.getEstado()).isEqualTo("ACTIVO");
    }

    @Test
    void activarCliente_deberiaLanzarCuandoNoExiste() {
        assertThatThrownBy(() -> clienteService.activarCliente(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cliente no encontrado con id: 999");
    }

    @Test
    void desactivarCliente_deberiaLanzarCuandoNoExiste() {
        assertThatThrownBy(() -> clienteService.desactivarCliente(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cliente no encontrado con id: 999");
    }
}
