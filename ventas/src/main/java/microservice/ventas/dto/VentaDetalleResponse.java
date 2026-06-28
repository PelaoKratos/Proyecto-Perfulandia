package microservice.ventas.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.ventas.model.Venta;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaDetalleResponse {

    private Venta venta;
    private Map<String, Object> perfume;
    private Map<String, Object> producto;
    private Map<String, Object> disponibilidadProducto;
    private Map<String, Object> sucursal;
}
