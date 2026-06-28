package perfulandia.pago.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import perfulandia.pago.exception.ResourceNotFoundException;

@Component
public class MicroserviceValidationClient {

    private final RestTemplate restTemplate;
    private final String clienteUrl;
    private final String ventaUrl;
    private final String usuarioUrl;

    public MicroserviceValidationClient(
            RestTemplate restTemplate,
            @Value("${microservices.cliente.url:http://localhost:8082/api/v1/clientes}") String clienteUrl,
            @Value("${microservices.venta.url:http://localhost:8088/api/v1/venta}") String ventaUrl,
            @Value("${microservices.usuario.url:http://localhost:8081/api/v1/usuarios}") String usuarioUrl) {
        this.restTemplate = restTemplate;
        this.clienteUrl = clienteUrl;
        this.ventaUrl = ventaUrl;
        this.usuarioUrl = usuarioUrl;
    }

    public void validarCliente(Long idCliente) {
        validarExiste(clienteUrl, idCliente, "Cliente");
    }

    public void validarVenta(Long idVenta) {
        validarExiste(ventaUrl, idVenta, "Venta");
    }

    public void validarUsuario(Long idUsuario) {
        validarExiste(usuarioUrl, idUsuario, "Usuario");
    }

    private void validarExiste(String baseUrl, Long id, String recurso) {
        try {
            restTemplate.getForEntity(baseUrl + "/" + id, String.class);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new ResourceNotFoundException(recurso + " no encontrado con id: " + id);
        } catch (RestClientException exception) {
            throw new IllegalStateException("No se pudo validar " + recurso.toLowerCase() + " con id: " + id, exception);
        }
    }
}
