package microservice.reporte.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class MicroservicioExternoServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private MicroservicioExternoService microservicioExternoService;

    @BeforeEach
    void setUp() {
        microservicioExternoService = new MicroservicioExternoService(
                restTemplate,
                "http://ventas/api",
                "http://inventario/api",
                "http://sucursales/api");
    }

    @Test
    void obtieneDatosDesdeMicroserviciosExternos() {
        List<Map<String, Object>> ventas = List.of(Map.of("idVenta", 1));
        List<Map<String, Object>> inventario = List.of(Map.of("idProducto", 501));
        List<Map<String, Object>> sucursales = List.of(Map.of("idSucursal", 1));
        when(restTemplate.exchange("http://ventas/api", HttpMethod.GET, null, Object.class))
                .thenReturn(ResponseEntity.ok(ventas));
        when(restTemplate.exchange("http://inventario/api", HttpMethod.GET, null, Object.class))
                .thenReturn(ResponseEntity.ok(inventario));
        when(restTemplate.exchange("http://sucursales/api", HttpMethod.GET, null, Object.class))
                .thenReturn(ResponseEntity.ok(sucursales));

        assertEquals(ventas, microservicioExternoService.obtenerVentas());
        assertEquals(inventario, microservicioExternoService.obtenerInventario());
        assertEquals(sucursales, microservicioExternoService.obtenerSucursales());
    }

    @Test
    void retornaMapaDeErrorCuandoMicroservicioNoEstaDisponible() {
        when(restTemplate.exchange("http://ventas/api", HttpMethod.GET, null, Object.class))
                .thenThrow(new RestClientException("No disponible"));

        Object resultado = microservicioExternoService.obtenerVentas();

        assertTrue(resultado instanceof Map);
        assertEquals(false, ((Map<?, ?>) resultado).get("disponible"));
        assertEquals("ventas", ((Map<?, ?>) resultado).get("recurso"));
    }
}
