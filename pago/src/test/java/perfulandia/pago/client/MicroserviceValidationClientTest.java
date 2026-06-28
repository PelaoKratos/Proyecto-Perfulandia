package perfulandia.pago.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import perfulandia.pago.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class MicroserviceValidationClientTest {

    private static final String CLIENTE_URL = "http://cliente.test/api/clientes";
    private static final String VENTA_URL = "http://venta.test/api/venta";
    private static final String USUARIO_URL = "http://usuario.test/api/usuarios";

    @Mock
    private RestTemplate restTemplate;

    private MicroserviceValidationClient client;

    @BeforeEach
    void setUp() {
        client = new MicroserviceValidationClient(restTemplate, CLIENTE_URL, VENTA_URL, USUARIO_URL);
    }

    @Test
    void validarClienteConsultaUrlConfigurada() {
        client.validarCliente(20L);

        verify(restTemplate).getForEntity(CLIENTE_URL + "/20", String.class);
    }

    @Test
    void validarVentaConsultaUrlConfigurada() {
        client.validarVenta(10L);

        verify(restTemplate).getForEntity(VENTA_URL + "/10", String.class);
    }

    @Test
    void validarUsuarioConsultaUrlConfigurada() {
        client.validarUsuario(30L);

        verify(restTemplate).getForEntity(USUARIO_URL + "/30", String.class);
    }

    @Test
    void validarClienteLanzaResourceNotFoundCuandoServicioRetorna404() {
        when(restTemplate.getForEntity(CLIENTE_URL + "/99", String.class))
                .thenThrow(notFoundException());

        ResourceNotFoundException error = assertThrows(ResourceNotFoundException.class,
                () -> client.validarCliente(99L));

        assertEquals("Cliente no encontrado con id: 99", error.getMessage());
    }

    @Test
    void validarVentaLanzaIllegalStateExceptionCuandoServicioNoResponde() {
        RestClientException causa = new RestClientException("conexion rechazada");
        when(restTemplate.getForEntity(VENTA_URL + "/11", String.class)).thenThrow(causa);

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> client.validarVenta(11L));

        assertEquals("No se pudo validar venta con id: 11", error.getMessage());
        assertSame(causa, error.getCause());
    }

    private HttpClientErrorException notFoundException() {
        return HttpClientErrorException.create(
                HttpStatus.NOT_FOUND,
                "Not Found",
                HttpHeaders.EMPTY,
                new byte[0],
                StandardCharsets.UTF_8);
    }
}
