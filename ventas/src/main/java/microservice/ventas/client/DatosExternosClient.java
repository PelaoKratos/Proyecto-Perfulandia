package microservice.ventas.client;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class DatosExternosClient {

    private final RestTemplate restTemplate;
    private final String perfumeUrl;
    private final String productoUrl;
    private final String disponibilidadProductoUrl;
    private final String sucursalUrl;

    public DatosExternosClient(
            RestTemplate restTemplate,
            @Value("${microservices.perfume.obtener-url}") String perfumeUrl,
            @Value("${microservices.producto.obtener-url}") String productoUrl,
            @Value("${microservices.producto.disponibilidad-url}") String disponibilidadProductoUrl,
            @Value("${microservices.sucursal.obtener-url}") String sucursalUrl) {
        this.restTemplate = restTemplate;
        this.perfumeUrl = perfumeUrl;
        this.productoUrl = productoUrl;
        this.disponibilidadProductoUrl = disponibilidadProductoUrl;
        this.sucursalUrl = sucursalUrl;
    }

    public Map<String, Object> obtenerPerfume(Long idPerfume) {
        return obtenerMapa(perfumeUrl, idPerfume, "No se pudo obtener el perfume");
    }

    public Map<String, Object> obtenerProducto(Long idProducto) {
        return obtenerMapa(productoUrl, idProducto, "No se pudo obtener el producto");
    }

    public Map<String, Object> obtenerDisponibilidadProducto(Long idProducto) {
        return obtenerMapa(disponibilidadProductoUrl, idProducto, "No se pudo obtener la disponibilidad del producto");
    }

    public Map<String, Object> obtenerSucursal(Long idSucursal) {
        return obtenerMapa(sucursalUrl, idSucursal, "No se pudo obtener la sucursal");
    }

    private Map<String, Object> obtenerMapa(String url, Long id, String mensajeError) {
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    },
                    id);

            return response.getBody() == null ? Collections.emptyMap() : response.getBody();
        } catch (RestClientException ex) {
            throw new IllegalStateException(mensajeError, ex);
        }
    }

}
