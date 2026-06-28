package microservice.cliente.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import microservice.cliente.exception.ResourceNotFoundException;
import microservice.cliente.model.Cliente;
import microservice.cliente.model.DireccionCliente;
import microservice.cliente.service.ClienteService;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private ClienteService clienteService;

    @Test
    void obtenerClientes_deberiaRetornarLista() throws Exception {
        Cliente cliente = clienteBase();
        cliente.setIdCliente(1L);

        given(clienteService.obtenerClientes()).willReturn(List.of(cliente));

        mockMvc.perform(get("/api/v1/clientes").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rut").value("11111111-1"))
                .andExpect(jsonPath("$[0].idUsuario").value(10L));
    }

    @Test
    void crearCliente_deberiaGuardarCliente() throws Exception {
        Cliente cliente = clienteBase();
        DireccionCliente direccion = new DireccionCliente();
        direccion.setCalle("Av. Principal 123");
        cliente.setDirecciones(List.of(direccion));

        Cliente guardado = clienteBase();
        guardado.setIdCliente(1L);
        guardado.setDirecciones(List.of(direccion));

        given(clienteService.guardarCliente(any(Cliente.class))).willReturn(guardado);

        mockMvc.perform(post("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCliente").value(1L))
                .andExpect(jsonPath("$.rut").value("11111111-1"));
    }

    @Test
    void obtenerCliente_deberiaRetornarClienteExistente() throws Exception {
        Cliente cliente = clienteBase();
        cliente.setIdCliente(1L);

        given(clienteService.obtenerClientePorId(1L)).willReturn(Optional.of(cliente));

        mockMvc.perform(get("/api/v1/clientes/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("11111111-1"));
    }

    @Test
    void obtenerCliente_deberiaRetornar404CuandoNoExiste() throws Exception {
        given(clienteService.obtenerClientePorId(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/clientes/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarCliente_deberiaRetornarClienteActualizado() throws Exception {
        Cliente clienteActualizado = clienteBase();
        clienteActualizado.setIdCliente(1L);
        clienteActualizado.setNombre("Juan Carlos");
        clienteActualizado.setApellido("Gomez");
        clienteActualizado.setCorreo("juan.gomez@example.com");
        clienteActualizado.setTelefono("987654321");

        given(clienteService.actualizarCliente(eq(1L), any(Cliente.class))).willReturn(clienteActualizado);

        mockMvc.perform(put("/api/v1/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Carlos"));
    }

    @Test
    void actualizarCliente_deberiaRetornar404CuandoNoExiste() throws Exception {
        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setNombre("Juan Carlos");

        given(clienteService.actualizarCliente(eq(99L), any(Cliente.class)))
                .willThrow(new ResourceNotFoundException("Cliente no encontrado con id: 99"));

        mockMvc.perform(put("/api/v1/clientes/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteActualizado)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cliente no encontrado con id: 99"));
    }

    @Test
    void eliminarCliente_deberiaRetornarNoContent() throws Exception {
        given(clienteService.eliminarCliente(1L)).willReturn(new Cliente());

        mockMvc.perform(delete("/api/v1/clientes/1"))
                .andExpect(status().isNoContent());

        verify(clienteService).eliminarCliente(1L);
    }

    @Test
    void eliminarCliente_deberiaRetornar404CuandoNoExiste() throws Exception {
        given(clienteService.eliminarCliente(99L))
                .willThrow(new ResourceNotFoundException("Cliente no encontrado con id: 99"));

        mockMvc.perform(delete("/api/v1/clientes/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cliente no encontrado con id: 99"));
    }

    @Test
    void activarCliente_deberiaRetornarClienteActivo() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);
        cliente.activar();

        given(clienteService.activarCliente(1L)).willReturn(cliente);

        mockMvc.perform(post("/api/v1/clientes/1/activar").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(true))
                .andExpect(jsonPath("$.estado").value("ACTIVO"));
    }

    @Test
    void activarCliente_deberiaRetornar404CuandoNoExiste() throws Exception {
        given(clienteService.activarCliente(99L))
                .willThrow(new ResourceNotFoundException("Cliente no encontrado con id: 99"));

        mockMvc.perform(post("/api/v1/clientes/99/activar").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cliente no encontrado con id: 99"));
    }

    @Test
    void desactivarCliente_deberiaRetornarClienteInactivo() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);
        cliente.desactivar();

        given(clienteService.desactivarCliente(1L)).willReturn(cliente);

        mockMvc.perform(post("/api/v1/clientes/1/desactivar").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(false))
                .andExpect(jsonPath("$.estado").value("INACTIVO"));
    }

    @Test
    void desactivarCliente_deberiaRetornar404CuandoNoExiste() throws Exception {
        given(clienteService.desactivarCliente(99L))
                .willThrow(new ResourceNotFoundException("Cliente no encontrado con id: 99"));

        mockMvc.perform(post("/api/v1/clientes/99/desactivar").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cliente no encontrado con id: 99"));
    }

    private Cliente clienteBase() {
        Cliente cliente = new Cliente();
        cliente.setIdUsuario(10L);
        cliente.setRut("11111111-1");
        cliente.setNombre("Juan");
        cliente.setApellido("Perez");
        cliente.setCorreo("juan.perez@example.com");
        cliente.setTelefono("123456789");
        cliente.setEstado("ACTIVO");
        return cliente;
    }
}
