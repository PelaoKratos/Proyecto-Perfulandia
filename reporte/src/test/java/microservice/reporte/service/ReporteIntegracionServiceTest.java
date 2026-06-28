package microservice.reporte.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import microservice.reporte.dto.ResumenMicroservicios;

@ExtendWith(MockitoExtension.class)
class ReporteIntegracionServiceTest {

    @Mock
    private MicroservicioExternoService microservicioExternoService;

    private ReporteIntegracionService reporteIntegracionService;

    @BeforeEach
    void setUp() {
        reporteIntegracionService = new ReporteIntegracionService(microservicioExternoService);
    }

    @Test
    void obtieneResumenDesdeMicroservicios() {
        List<Map<String, Object>> ventas = List.of(Map.of("idVenta", 1));
        List<Map<String, Object>> inventario = List.of(Map.of("idProducto", 501));
        List<Map<String, Object>> sucursales = List.of(Map.of("idSucursal", 1));
        when(microservicioExternoService.obtenerVentas()).thenReturn(ventas);
        when(microservicioExternoService.obtenerInventario()).thenReturn(inventario);
        when(microservicioExternoService.obtenerSucursales()).thenReturn(sucursales);

        ResumenMicroservicios resumen = reporteIntegracionService.obtenerResumenMicroservicios();

        assertEquals(ventas, resumen.ventas());
        assertEquals(inventario, resumen.inventario());
        assertEquals(sucursales, resumen.sucursales());
    }
}
