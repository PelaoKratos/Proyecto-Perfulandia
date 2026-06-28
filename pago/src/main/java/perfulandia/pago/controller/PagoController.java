package perfulandia.pago.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import perfulandia.pago.exception.ResourceNotFoundException;
import perfulandia.pago.model.Pago;
import perfulandia.pago.service.PagoService;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    public ResponseEntity<Pago> crearPago(@Valid @RequestBody Pago pago) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.crearPago(pago));
    }

    @GetMapping
    public ResponseEntity<List<Pago>> obtenerPagos() {
        return ResponseEntity.ok(pagoService.obtenerPagos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPagoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.obtenerPagoPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pago> actualizarPago(@PathVariable Long id, @Valid @RequestBody Pago pago) {
        return ResponseEntity.ok(pagoService.actualizarPago(id, pago));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPago(@PathVariable Long id) {
        pagoService.eliminarPago(id);
        return ResponseEntity.ok("Pago eliminado correctamente");
    }

    @PostMapping("/{id}/confirmar")
    public ResponseEntity<Pago> confirmarPago(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.confirmarPago(id));
    }

    @PostMapping("/{id}/rechazar")
    public ResponseEntity<Pago> rechazarPago(@PathVariable Long id, @RequestBody(required = false) EstadoPagoRequest request) {
        return ResponseEntity.ok(pagoService.rechazarPago(id, request == null ? null : request.motivo()));
    }

    @PostMapping("/{id}/anular")
    public ResponseEntity<Pago> anularPago(@PathVariable Long id, @RequestBody(required = false) EstadoPagoRequest request) {
        return ResponseEntity.ok(pagoService.anularPago(id, request == null ? null : request.motivo()));
    }

    @GetMapping("/venta/{idVenta}")
    public ResponseEntity<List<Pago>> obtenerPagosPorVenta(@PathVariable Long idVenta) {
        return ResponseEntity.ok(pagoService.obtenerPagosPorVenta(idVenta));
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Pago>> obtenerPagosPorCliente(@PathVariable Long idCliente) {
        return ResponseEntity.ok(pagoService.obtenerPagosPorCliente(idCliente));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pago>> obtenerPagosPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(pagoService.obtenerPagosPorEstado(estado));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> manejarNoEncontrado(ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> manejarSolicitudInvalida(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    public record EstadoPagoRequest(String motivo) {
    }
}
