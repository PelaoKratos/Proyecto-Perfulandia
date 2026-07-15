package microservice.ventas.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.ventas.model.Venta;

@Data
@NoArgsConstructor
public class VentaDetalleResponse {

    private Venta venta;
    private Map<String, Object> perfume;
    private Map<String, Object> producto;
    private Map<String, Object> disponibilidadProducto;
    private Map<String, Object> sucursal;
    private List<VentaDetalleItemResponse> detalles;

    public VentaDetalleResponse(
            Venta venta,
            Map<String, Object> perfume,
            Map<String, Object> producto,
            Map<String, Object> disponibilidadProducto,
            Map<String, Object> sucursal) {
        this(venta, perfume, producto, disponibilidadProducto, sucursal, List.of());
    }

    public VentaDetalleResponse(
            Venta venta,
            Map<String, Object> perfume,
            Map<String, Object> producto,
            Map<String, Object> disponibilidadProducto,
            Map<String, Object> sucursal,
            List<VentaDetalleItemResponse> detalles) {
        this.venta = venta;
        this.perfume = perfume;
        this.producto = producto;
        this.disponibilidadProducto = disponibilidadProducto;
        this.sucursal = sucursal;
        this.detalles = detalles;
    }
}
