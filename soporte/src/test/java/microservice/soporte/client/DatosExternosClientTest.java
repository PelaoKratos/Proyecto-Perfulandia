package microservice.soporte.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class DatosExternosClientTest {

    private static final String CLIENTE_URL = "http://cliente/api/clientes/{id}";
    private static final String USUARIO_URL = "http://usuario/api/usuarios/{id}";

    @Mock
    private RestTemplate restTemplate;

    private DatosExternosClient datosExternosClient;

    @BeforeEach
    void setUp() {
        datosExternosClient = new DatosExternosClient(restTemplate, CLIENTE_URL, USUARIO_URL);
    }

    @Test
    void obtenerClienteRetornaBodyDeServicioExterno() {
        Map<String, Object> cliente = Map.of("idCliente", 10L, "nombre", "Cliente Demo");
        when(restTemplate.exchange(
                eq(CLIENTE_URL),
                eq(HttpMethod.GET),
                isNull(),
                typeReference(),
                eq(10L)))
                .thenReturn(ResponseEntity.ok(cliente));

        Map<String, Object> resultado = datosExternosClient.obtenerCliente(10L);

        assertThat(resultado).containsEntry("idCliente", 10L).containsEntry("nombre", "Cliente Demo");
    }

    @Test
    void obtenerUsuarioConIdNuloRetornaMapaVacioSinInvocarServicioExterno() {
        Map<String, Object> resultado = datosExternosClient.obtenerUsuario(null);

        assertThat(resultado).isEmpty();
        verifyNoInteractions(restTemplate);
    }

    @Test
    void obtenerUsuarioRetornaMapaVacioCuandoServicioRespondeSinBody() {
        when(restTemplate.exchange(
                eq(USUARIO_URL),
                eq(HttpMethod.GET),
                isNull(),
                typeReference(),
                eq(2L)))
                .thenReturn(ResponseEntity.ok(null));

        Map<String, Object> resultado = datosExternosClient.obtenerUsuario(2L);

        assertThat(resultado).isEmpty();
    }

    @Test
    void obtenerClienteLanzaIllegalStateExceptionCuandoFallaServicioExterno() {
        RestClientException causa = new RestClientException("timeout");
        when(restTemplate.exchange(
                eq(CLIENTE_URL),
                eq(HttpMethod.GET),
                isNull(),
                typeReference(),
                eq(10L)))
                .thenThrow(causa);

        assertThatThrownBy(() -> datosExternosClient.obtenerCliente(10L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No se pudo obtener el cliente")
                .hasCause(causa);
    }

    @SuppressWarnings("unchecked")
    private ParameterizedTypeReference<Map<String, Object>> typeReference() {
        return org.mockito.ArgumentMatchers.any(ParameterizedTypeReference.class);
    }
}
