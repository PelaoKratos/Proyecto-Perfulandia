package microservice.ventas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import microservice.ventas.client.DatosExternosClient;
import microservice.ventas.client.InventarioClient;
import microservice.ventas.dto.DescuentoStockRequest;
import microservice.ventas.dto.VentaDetalleItemResponse;
import microservice.ventas.dto.VentaDetalleResponse;
import microservice.ventas.model.Venta;
import microservice.ventas.model.VentaDetalle;
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
        prepararDetalles(venta);
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
        reemplazarDetalles(ventaExistente, venta);

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
        for (VentaDetalle detalle : venta.getDetalles()) {
            DescuentoStockRequest request = new DescuentoStockRequest(
                    null,
                    detalle.getIdPerfume(),
                    venta.getIdSucursal(),
                    null,
                    venta.getIdVenta(),
                    detalle.getCantidad().intValue());

            inventarioClient.descontarStock(request);
        }
    }

    private VentaDetalleResponse construirDetalle(Venta venta) {
        prepararDetalles(venta);
        List<VentaDetalleItemResponse> detalles = venta.getDetalles().stream()
                .map(this::construirDetalleItem)
                .toList();
        VentaDetalleItemResponse primerDetalle = detalles.get(0);

        return new VentaDetalleResponse(
                venta,
                primerDetalle.getPerfume(),
                primerDetalle.getProducto(),
                primerDetalle.getDisponibilidadProducto(),
                datosExternosClient.obtenerSucursal(venta.getIdSucursal()),
                detalles);
    }

    private VentaDetalleItemResponse construirDetalleItem(VentaDetalle detalle) {
        Map<String, Object> perfume = datosExternosClient.obtenerPerfume(detalle.getIdPerfume());
        Map<String, Object> producto = datosExternosClient.obtenerProducto(detalle.getIdPerfume());
        Map<String, Object> disponibilidad = datosExternosClient.obtenerDisponibilidadProducto(detalle.getIdPerfume());

        return new VentaDetalleItemResponse(
                detalle.getIdPerfume(),
                detalle.getCantidad(),
                perfume,
                producto,
                disponibilidad);
    }

    private void prepararDetalles(Venta venta) {
        if (venta.getDetalles() == null) {
            venta.setDetalles(new ArrayList<>());
        }

        if (venta.getDetalles().isEmpty() && venta.getIdPerfume() != null && venta.getCantidad() != null) {
            venta.getDetalles().add(new VentaDetalle(null, venta, venta.getIdPerfume(), venta.getCantidad()));
        }

        if (venta.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar al menos un perfume en el detalle de la venta");
        }

        for (VentaDetalle detalle : venta.getDetalles()) {
            if (detalle.getIdPerfume() == null) {
                throw new IllegalArgumentException("El perfume es obligatorio");
            }
            if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
            }
            detalle.setVenta(venta);
        }

        VentaDetalle primerDetalle = venta.getDetalles().get(0);
        venta.setIdPerfume(primerDetalle.getIdPerfume());
        venta.setCantidad(primerDetalle.getCantidad());
    }

    private void reemplazarDetalles(Venta ventaExistente, Venta ventaNueva) {
        ventaExistente.getDetalles().clear();
        if (ventaNueva.getDetalles() != null) {
            for (VentaDetalle detalle : ventaNueva.getDetalles()) {
                ventaExistente.getDetalles().add(new VentaDetalle(
                        null,
                        ventaExistente,
                        detalle.getIdPerfume(),
                        detalle.getCantidad()));
            }
        }
        prepararDetalles(ventaExistente);
    }

}
