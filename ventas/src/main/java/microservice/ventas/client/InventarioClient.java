package microservice.ventas.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import microservice.ventas.dto.DescuentoStockRequest;

@Component
public class InventarioClient {

    private final RestTemplate restTemplate;
    private final String descontarStockUrl;

    public InventarioClient(
            RestTemplate restTemplate,
            @Value("${microservices.inventario.descontar-stock-url}") String descontarStockUrl) {
        this.restTemplate = restTemplate;
        this.descontarStockUrl = descontarStockUrl;
    }

    public void descontarStock(DescuentoStockRequest request) {
        try {
            restTemplate.postForEntity(descontarStockUrl, request, Void.class);
        } catch (RestClientException ex) {
            throw new IllegalStateException("No se pudo descontar stock en inventario", ex);
        }
    }
}
