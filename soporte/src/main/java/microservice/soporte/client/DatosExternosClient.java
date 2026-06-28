package microservice.soporte.client;

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
    private final String clienteUrl;
    private final String usuarioUrl;

    public DatosExternosClient(
            RestTemplate restTemplate,
            @Value("${microservices.cliente.obtener-url}") String clienteUrl,
            @Value("${microservices.usuario.obtener-url}") String usuarioUrl) {
        this.restTemplate = restTemplate;
        this.clienteUrl = clienteUrl;
        this.usuarioUrl = usuarioUrl;
    }

    public Map<String, Object> obtenerCliente(Long idCliente) {
        return obtenerMapa(clienteUrl, idCliente, "No se pudo obtener el cliente");
    }

    public Map<String, Object> obtenerUsuario(Long idUsuario) {
        if (idUsuario == null) {
            return Collections.emptyMap();
        }
        return obtenerMapa(usuarioUrl, idUsuario, "No se pudo obtener el usuario");
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
