package microservice.ventas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import microservice.ventas.dto.VentaDetalleResponse;
import microservice.ventas.model.Venta;
import microservice.ventas.service.VentaService;

@RestController
@RequestMapping("/api/v1/venta")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @PostMapping
    public ResponseEntity<Venta> crearVenta(@Valid @RequestBody Venta venta) {
        Venta nuevaVenta = ventaService.crearVenta(venta);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaVenta);
    }

    @PostMapping("/detalle")
    public ResponseEntity<VentaDetalleResponse> crearVentaConDetalle(@Valid @RequestBody Venta venta) {
        VentaDetalleResponse nuevaVenta = ventaService.crearVentaConDetalle(venta);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaVenta);
    }

    @GetMapping
    public ResponseEntity<List<Venta>> obtenerVentas() {
        List<Venta> ventas = ventaService.obtenerVenta();
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerVentaPorId(@PathVariable Long id) {
        Venta venta = ventaService.obtenerVentaPorId(id);

        return ResponseEntity.ok(venta);
    }

    @GetMapping("/{id}/detalle")
    public ResponseEntity<VentaDetalleResponse> obtenerVentaDetalle(@PathVariable Long id) {
        VentaDetalleResponse venta = ventaService.obtenerVentaDetalle(id);
        return ResponseEntity.ok(venta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venta> actualizarVenta(@PathVariable Long id,@Valid @RequestBody Venta venta) {
        Venta ventaActualizada = ventaService.updateVenta(id, venta);
        return ResponseEntity.ok(ventaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarVenta(@PathVariable Long id) {

        ventaService.eliminarVenta(id);

        return ResponseEntity.ok("Venta eliminada correctamente");
    }
}
