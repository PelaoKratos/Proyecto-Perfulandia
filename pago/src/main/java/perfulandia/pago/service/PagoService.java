package perfulandia.pago.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import perfulandia.pago.client.MicroserviceValidationClient;
import perfulandia.pago.exception.ResourceNotFoundException;
import perfulandia.pago.model.Pago;
import perfulandia.pago.repository.PagoRepository;

@Service
@Transactional
public class PagoService {

    private final PagoRepository pagoRepository;
    private final MicroserviceValidationClient microserviceValidationClient;

    public PagoService(PagoRepository pagoRepository, MicroserviceValidationClient microserviceValidationClient) {
        this.pagoRepository = pagoRepository;
        this.microserviceValidationClient = microserviceValidationClient;
    }

    public Pago crearPago(Pago pago) {
        validarPagoObligatorio(pago);
        validarReferenciasExternas(pago);
        pago.procesar();
        return pagoRepository.save(pago);
    }

    public List<Pago> obtenerPagos() {
        return pagoRepository.findAll();
    }

    public Pago obtenerPagoPorId(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con id: " + id));
    }

    public Pago actualizarPago(Long id, Pago pago) {
        validarPagoObligatorio(pago);
        validarReferenciasExternas(pago);
        Pago existente = obtenerPagoPorId(id);
        existente.setIdVenta(pago.getIdVenta());
        existente.setIdCliente(pago.getIdCliente());
        existente.setIdEmpleado(pago.getIdEmpleado());
        existente.setIdMetodoPago(pago.getIdMetodoPago());
        existente.setMonto(pago.getMonto());
        existente.setMetodoPago(pago.getMetodoPago());
        existente.setEstado(pago.getEstado());
        existente.setFechaPago(pago.getFechaPago());
        existente.setCodigoTransaccion(pago.getCodigoTransaccion());
        existente.setMensaje(pago.getMensaje());
        return pagoRepository.save(existente);
    }

    public void eliminarPago(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pago no encontrado con id: " + id);
        }
        pagoRepository.deleteById(id);
    }

    public Pago confirmarPago(Long id) {
        Pago pago = obtenerPagoPorId(id);
        pago.confirmar("TX-" + UUID.randomUUID());
        return pagoRepository.save(pago);
    }

    public Pago rechazarPago(Long id, String motivo) {
        Pago pago = obtenerPagoPorId(id);
        pago.rechazar(normalizarMotivo(motivo));
        return pagoRepository.save(pago);
    }

    public Pago anularPago(Long id, String motivo) {
        Pago pago = obtenerPagoPorId(id);
        pago.anular(normalizarMotivo(motivo));
        return pagoRepository.save(pago);
    }

    public List<Pago> obtenerPagosPorVenta(Long idVenta) {
        return pagoRepository.findByIdVenta(idVenta);
    }

    public List<Pago> obtenerPagosPorCliente(Long idCliente) {
        return pagoRepository.findByIdCliente(idCliente);
    }

    public List<Pago> obtenerPagosPorEstado(String estado) {
        return pagoRepository.findByEstado(estado);
    }

    private void validarPagoObligatorio(Pago pago) {
        if (pago == null) {
            throw new IllegalArgumentException("El pago es obligatorio");
        }
        if (!pago.validarPago()) {
            throw new IllegalArgumentException("Los datos del pago son invalidos");
        }
    }

    private void validarReferenciasExternas(Pago pago) {
        microserviceValidationClient.validarCliente(pago.getIdCliente());
        microserviceValidationClient.validarVenta(pago.getIdVenta());
        microserviceValidationClient.validarUsuario(pago.getIdEmpleado());
    }

    private String normalizarMotivo(String motivo) {
        if (motivo == null || motivo.isBlank()) {
            return "Sin motivo informado";
        }
        return motivo;
    }
}
