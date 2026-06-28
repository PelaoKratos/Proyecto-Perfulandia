package microservice.soporte.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.soporte.model.TicketSoporte;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDetalleResponse {

    private TicketSoporte ticket;
    private Map<String, Object> cliente;
    private Map<String, Object> usuarioAsignado;
}
