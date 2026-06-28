package microservice.ventas.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

class DatosExternosClientTest {

    private static final String PERFUME_URL = "http://localhost:8084/api/v1/perfumes/{id}";
    private static final String PRODUCTO_URL = "http://localhost:8084/api/v1/productos/{id}";
    private static final String DISPONIBILIDAD_URL = "http://localhost:8084/api/v1/productos/{id}/disponibilidad";
    private static final String SUCURSAL_URL = "http://localhost:8083/api/v1/sucursales/{id}";

    private final RestTemplate restTemplate = org.mockito.Mockito.mock(RestTemplate.class);
    private final DatosExternosClient datosExternosClient = new DatosExternosClient(
            restTemplate,
            PERFUME_URL,
            PRODUCTO_URL,
            DISPONIBILIDAD_URL,
            SUCURSAL_URL);

    @Test
    void obtenerPerfumeRetornaBodyDeEndpointConfigurado() {
        Map<String, Object> body = Map.of("id", 10L, "nombre", "Ambar");
        when(restTemplate.exchange(
                eq(PERFUME_URL),
                eq(HttpMethod.GET),
                isNull(),
                org.mockito.ArgumentMatchers.<ParameterizedTypeReference<Map<String, Object>>>any(),
                eq(10L))).thenReturn(ResponseEntity.ok(body));

        Map<String, Object> resultado = datosExternosClient.obtenerPerfume(10L);

        assertEquals(body, resultado);
        verify(restTemplate).exchange(
                eq(PERFUME_URL),
                eq(HttpMethod.GET),
                isNull(),
                org.mockito.ArgumentMatchers.<ParameterizedTypeReference<Map<String, Object>>>any(),
                eq(10L));
    }

    @Test
    void obtenerProductoRetornaMapaVacioCuandoBodyEsNull() {
        when(restTemplate.exchange(
                eq(PRODUCTO_URL),
                eq(HttpMethod.GET),
                isNull(),
                org.mockito.ArgumentMatchers.<ParameterizedTypeReference<Map<String, Object>>>any(),
                eq(10L))).thenReturn(ResponseEntity.ok(null));

        Map<String, Object> resultado = datosExternosClient.obtenerProducto(10L);

        assertEquals(Map.of(), resultado);
    }

    @Test
    void obtenerDisponibilidadProductoPropagaErrorConMensajeDeDominio() {
        doThrow(new RestClientException("timeout")).when(restTemplate).exchange(
                eq(DISPONIBILIDAD_URL),
                eq(HttpMethod.GET),
                isNull(),
                org.mockito.ArgumentMatchers.<ParameterizedTypeReference<Map<String, Object>>>any(),
                eq(10L));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> datosExternosClient.obtenerDisponibilidadProducto(10L));

        assertEquals("No se pudo obtener la disponibilidad del producto", exception.getMessage());
    }

    @Test
    void obtenerSucursalUsaUrlDeSucursal() {
        Map<String, Object> body = Map.of("id", 20L, "nombre", "Centro");
        when(restTemplate.exchange(
                eq(SUCURSAL_URL),
                eq(HttpMethod.GET),
                isNull(),
                org.mockito.ArgumentMatchers.<ParameterizedTypeReference<Map<String, Object>>>any(),
                eq(20L))).thenReturn(ResponseEntity.ok(body));

        Map<String, Object> resultado = datosExternosClient.obtenerSucursal(20L);

        assertEquals(body, resultado);
    }
}
