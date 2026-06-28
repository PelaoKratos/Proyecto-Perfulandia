package microservice.ventas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import microservice.ventas.client.DatosExternosClient;
import microservice.ventas.client.InventarioClient;
import microservice.ventas.dto.DescuentoStockRequest;
import microservice.ventas.dto.VentaDetalleResponse;
import microservice.ventas.model.Venta;
import microservice.ventas.repository.VentaRepository;


@Service
@Transactional
public class VentaService {
    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private InventarioClient inventarioClient;

    @Autowired
    private DatosExternosClient datosExternosClient;

    public Venta crearVenta(Venta venta) {
        descontarStockEnMicroservicios(venta);
        return ventaRepository.save(venta);
    }

    public VentaDetalleResponse crearVentaConDetalle(Venta venta) {
        Venta ventaGuardada = crearVenta(venta);
        return construirDetalle(ventaGuardada);
    }

    public List<Venta> obtenerVenta() {
        return ventaRepository.findAll();
    }
        public Venta obtenerventaPorId(Long id) {
        return ventaRepository.findById(id).orElse(null);
    }

    public Venta updateVenta(Long id, Venta venta) {
        Venta ventaExistente = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        ventaExistente.setFechaVenta(venta.getFechaVenta());
        ventaExistente.setTotalVenta(venta.getTotalVenta());
        ventaExistente.setDescuentoVenta(venta.getDescuentoVenta());
        ventaExistente.setEstadoVenta(venta.getEstadoVenta());
        ventaExistente.setIdPerfume(venta.getIdPerfume());
        ventaExistente.setIdSucursal(venta.getIdSucursal());
        ventaExistente.setCantidad(venta.getCantidad());

        return ventaRepository.save(ventaExistente);
    }


    public void eliminarVenta(Long id) {
        ventaRepository.deleteById(id);
    }

    public Venta obtenerVentaPorId(Long id) {

    return ventaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
}

    public VentaDetalleResponse obtenerVentaDetalle(Long id) {
        Venta venta = obtenerVentaPorId(id);
        return construirDetalle(venta);
    }

    private void descontarStockEnMicroservicios(Venta venta) {
        DescuentoStockRequest request = new DescuentoStockRequest(
                null,
                venta.getIdPerfume(),
                venta.getIdSucursal(),
                null,
                venta.getIdVenta(),
                venta.getCantidad().intValue());

        inventarioClient.descontarStock(request);
    }

    private VentaDetalleResponse construirDetalle(Venta venta) {
        return new VentaDetalleResponse(
                venta,
                datosExternosClient.obtenerPerfume(venta.getIdPerfume()),
                datosExternosClient.obtenerProducto(venta.getIdPerfume()),
                datosExternosClient.obtenerDisponibilidadProducto(venta.getIdPerfume()),
                datosExternosClient.obtenerSucursal(venta.getIdSucursal()));
    }

}
