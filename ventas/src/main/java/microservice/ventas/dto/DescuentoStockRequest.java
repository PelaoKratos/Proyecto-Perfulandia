package microservice.ventas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DescuentoStockRequest {

    private Long inventarioId;
    private Long productoId;
    private Long sucursalId;
    private Long usuarioId;
    private Long ventaId;
    private Integer cantidad;
}
