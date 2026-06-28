package microservice.ventas.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import microservice.ventas.dto.VentaDetalleResponse;
import microservice.ventas.model.Venta;
import microservice.ventas.service.VentaService;

class VentaControllerTest {

    private final VentaService ventaService = org.mockito.Mockito.mock(VentaService.class);
    private final VentaController ventaController = new VentaController();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(ventaController, "ventaService", ventaService);
    }

    @Test
    void crearVentaRetornaCreated() {
        Venta venta = ventaValida();
        when(ventaService.crearVenta(venta)).thenReturn(venta);

        ResponseEntity<Venta> respuesta = ventaController.crearVenta(venta);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertSame(venta, respuesta.getBody());
    }

    @Test
    void crearVentaConDetalleRetornaCreated() {
        Venta venta = ventaValida();
        VentaDetalleResponse detalle = detalleValido(venta);
        when(ventaService.crearVentaConDetalle(venta)).thenReturn(detalle);

        ResponseEntity<VentaDetalleResponse> respuesta = ventaController.crearVentaConDetalle(venta);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertSame(detalle, respuesta.getBody());
    }

    @Test
    void obtenerVentasRetornaLista() {
        Venta venta = ventaValida();
        when(ventaService.obtenerVenta()).thenReturn(List.of(venta));

        ResponseEntity<List<Venta>> respuesta = ventaController.obtenerVentas();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
    }

    @Test
    void obtenerVentaPorIdRetornaVenta() {
        Venta venta = ventaValida();
        when(ventaService.obtenerVentaPorId(1L)).thenReturn(venta);

        ResponseEntity<Venta> respuesta = ventaController.obtenerVentaPorId(1L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertSame(venta, respuesta.getBody());
    }

    @Test
    void obtenerVentaDetalleRetornaDetalle() {
        Venta venta = ventaValida();
        VentaDetalleResponse detalle = detalleValido(venta);
        when(ventaService.obtenerVentaDetalle(1L)).thenReturn(detalle);

        ResponseEntity<VentaDetalleResponse> respuesta = ventaController.obtenerVentaDetalle(1L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertSame(detalle, respuesta.getBody());
    }

    @Test
    void actualizarVentaRetornaVentaActualizada() {
        Venta venta = ventaValida();
        when(ventaService.updateVenta(1L, venta)).thenReturn(venta);

        ResponseEntity<Venta> respuesta = ventaController.actualizarVenta(1L, venta);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertSame(venta, respuesta.getBody());
    }

    @Test
    void eliminarVentaRetornaMensaje() {
        ResponseEntity<String> respuesta = ventaController.eliminarVenta(1L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("Venta eliminada correctamente", respuesta.getBody());
        verify(ventaService).eliminarVenta(1L);
    }

    private Venta ventaValida() {
        Venta venta = new Venta();
        venta.setIdVenta(1L);
        venta.setFechaVenta(LocalDate.of(2026, 6, 12));
        venta.setTotalVenta(59990.0);
        venta.setDescuentoVenta(0.0);
        venta.setEstadoVenta("PAGADA");
        venta.setIdPerfume(10L);
        venta.setIdSucursal(20L);
        venta.setCantidad(2L);
        return venta;
    }

    private VentaDetalleResponse detalleValido(Venta venta) {
        return new VentaDetalleResponse(
                venta,
                Map.of("id", venta.getIdPerfume()),
                Map.of("id", venta.getIdPerfume()),
                Map.of("stock", 5),
                Map.of("id", venta.getIdSucursal()));
    }
}
