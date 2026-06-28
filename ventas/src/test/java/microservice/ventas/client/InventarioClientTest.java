package microservice.ventas.client;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import microservice.ventas.dto.DescuentoStockRequest;

class InventarioClientTest {

    private static final String URL = "http://localhost:8084/api/v1/inventarios/ventas/descontar";

    private final RestTemplate restTemplate = org.mockito.Mockito.mock(RestTemplate.class);
    private final InventarioClient inventarioClient = new InventarioClient(restTemplate, URL);

    @Test
    void descontarStockLlamaEndpointConfigurado() {
        DescuentoStockRequest request = new DescuentoStockRequest(null, 1L, 1L, null, null, 2);

        inventarioClient.descontarStock(request);

        verify(restTemplate).postForEntity(URL, request, Void.class);
    }

    @Test
    void descontarStockLanzaExcepcionCuandoInventarioFalla() {
        DescuentoStockRequest request = new DescuentoStockRequest(null, 1L, 1L, null, null, 2);
        doThrow(new RestClientException("error")).when(restTemplate).postForEntity(URL, request, Void.class);

        assertThrows(IllegalStateException.class, () -> inventarioClient.descontarStock(request));
    }
}
