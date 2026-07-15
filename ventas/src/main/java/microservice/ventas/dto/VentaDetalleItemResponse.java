package microservice.ventas.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaDetalleItemResponse {

    private Long idPerfume;
    private Long cantidad;
    private Map<String, Object> perfume;
    private Map<String, Object> producto;
    private Map<String, Object> disponibilidadProducto;
}
