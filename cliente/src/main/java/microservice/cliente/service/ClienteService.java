package microservice.cliente.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import microservice.cliente.exception.ResourceNotFoundException;
import microservice.cliente.model.Cliente;
import microservice.cliente.repository.ClienteRepository;

@Service
@Transactional
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente guardarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
        if (cliente.getDirecciones() != null) {
            cliente.getDirecciones().forEach(direccion -> direccion.setCliente(cliente));
        }
        return clienteRepository.save(cliente);
    }

    public List<Cliente> obtenerClientes() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        clienteExistente.actualizarDatosPersonales(clienteActualizado.getRut(), clienteActualizado.getNombre(),
                clienteActualizado.getApellido(), clienteActualizado.getCorreo(), clienteActualizado.getTelefono(),
                clienteActualizado.getEstado());

        return clienteRepository.save(clienteExistente);
    }

    public Cliente eliminarCliente(Long id) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
        clienteRepository.delete(clienteExistente);
        return clienteExistente;
    }

    public Cliente activarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
        cliente.activar();
        return clienteRepository.save(cliente);
    }

    public Cliente desactivarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
        cliente.desactivar();
        return clienteRepository.save(cliente);
    }
}
