package microservice.ventas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import microservice.ventas.client.DatosExternosClient;
import microservice.ventas.client.InventarioClient;
import microservice.ventas.dto.DescuentoStockRequest;
import microservice.ventas.dto.VentaDetalleResponse;
import microservice.ventas.model.Venta;
import microservice.ventas.model.VentaDetalle;
import microservice.ventas.repository.VentaRepository;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private InventarioClient inventarioClient;

    @Mock
    private DatosExternosClient datosExternosClient;

    @InjectMocks
    private VentaService ventaService;

    @Test
    void crearVentaDescuentaStockYGuardaVenta() {
        Venta venta = ventaValida();
        when(ventaRepository.save(venta)).thenReturn(venta);

        Venta resultado = ventaService.crearVenta(venta);

        assertSame(venta, resultado);

        verify(inventarioClient).descontarStock(any(DescuentoStockRequest.class));
        verify(ventaRepository).save(venta);
    }

    @Test
    void crearVentaEnviaDatosCorrectosParaDescontarStock() {
        Venta venta = ventaValida();
        when(ventaRepository.save(venta)).thenReturn(venta);
        ArgumentCaptor<DescuentoStockRequest> captor = ArgumentCaptor.forClass(DescuentoStockRequest.class);

        ventaService.crearVenta(venta);

        verify(inventarioClient).descontarStock(captor.capture());
        DescuentoStockRequest request = captor.getValue();
        assertEquals(10L, request.getProductoId());
        assertEquals(20L, request.getSucursalId());
        assertEquals(2, request.getCantidad());
    }

    @Test
    void crearVentaConVariosPerfumesDescuentaStockPorCadaDetalle() {
        Venta venta = ventaValida();
        venta.setIdPerfume(null);
        venta.setCantidad(null);
        venta.setDetalles(List.of(
                new VentaDetalle(null, null, 10L, 2L),
                new VentaDetalle(null, null, 11L, 1L)));
        when(ventaRepository.save(venta)).thenReturn(venta);
        ArgumentCaptor<DescuentoStockRequest> captor = ArgumentCaptor.forClass(DescuentoStockRequest.class);

        Venta resultado = ventaService.crearVenta(venta);

        assertSame(venta, resultado);
        assertEquals(2, resultado.getDetalles().size());
        assertEquals(10L, resultado.getIdPerfume());
        assertEquals(2L, resultado.getCantidad());
        verify(inventarioClient, times(2)).descontarStock(captor.capture());
        List<DescuentoStockRequest> descuentos = captor.getAllValues();
        assertEquals(10L, descuentos.get(0).getProductoId());
        assertEquals(2, descuentos.get(0).getCantidad());
        assertEquals(11L, descuentos.get(1).getProductoId());
        assertEquals(1, descuentos.get(1).getCantidad());
        verify(ventaRepository).save(venta);
    }

    @Test
    void crearVentaNoGuardaSiInventarioFalla() {
        Venta venta = ventaValida();
        org.mockito.Mockito.doThrow(new IllegalStateException("sin stock"))
                .when(inventarioClient)
                .descontarStock(any(DescuentoStockRequest.class));

        assertThrows(IllegalStateException.class, () -> ventaService.crearVenta(venta));

        verify(ventaRepository, never()).save(any(Venta.class));
    }

    @Test
    void crearVentaConDetalleGuardaYRetornaDatosExternos() {
        Venta venta = ventaValida();
        Map<String, Object> perfume = Map.of("id", 10L, "nombre", "Ambar");
        Map<String, Object> producto = Map.of("id", 10L, "precio", 59990);
        Map<String, Object> disponibilidad = Map.of("stock", 8);
        Map<String, Object> sucursal = Map.of("id", 20L, "nombre", "Centro");
        when(ventaRepository.save(venta)).thenReturn(venta);
        when(datosExternosClient.obtenerPerfume(10L)).thenReturn(perfume);
        when(datosExternosClient.obtenerProducto(10L)).thenReturn(producto);
        when(datosExternosClient.obtenerDisponibilidadProducto(10L)).thenReturn(disponibilidad);
        when(datosExternosClient.obtenerSucursal(20L)).thenReturn(sucursal);

        VentaDetalleResponse resultado = ventaService.crearVentaConDetalle(venta);

        assertSame(venta, resultado.getVenta());
        assertSame(perfume, resultado.getPerfume());
        assertSame(producto, resultado.getProducto());
        assertSame(disponibilidad, resultado.getDisponibilidadProducto());
        assertSame(sucursal, resultado.getSucursal());
        verify(inventarioClient).descontarStock(any(DescuentoStockRequest.class));
        verify(ventaRepository).save(venta);
    }

    @Test
    void crearVentaConDetalleYVariosPerfumesRetornaTodosLosDetalles() {
        Venta venta = ventaValida();
        venta.setIdPerfume(null);
        venta.setCantidad(null);
        venta.setDetalles(List.of(
                new VentaDetalle(null, null, 10L, 2L),
                new VentaDetalle(null, null, 11L, 1L)));
        Map<String, Object> sucursal = Map.of("id", 20L, "nombre", "Centro");
        when(ventaRepository.save(venta)).thenReturn(venta);
        when(datosExternosClient.obtenerPerfume(10L)).thenReturn(Map.of("id", 10L, "nombre", "Ambar"));
        when(datosExternosClient.obtenerProducto(10L)).thenReturn(Map.of("id", 10L, "precio", 59990));
        when(datosExternosClient.obtenerDisponibilidadProducto(10L)).thenReturn(Map.of("stock", 8));
        when(datosExternosClient.obtenerPerfume(11L)).thenReturn(Map.of("id", 11L, "nombre", "Citrico"));
        when(datosExternosClient.obtenerProducto(11L)).thenReturn(Map.of("id", 11L, "precio", 29990));
        when(datosExternosClient.obtenerDisponibilidadProducto(11L)).thenReturn(Map.of("stock", 4));
        when(datosExternosClient.obtenerSucursal(20L)).thenReturn(sucursal);

        VentaDetalleResponse resultado = ventaService.crearVentaConDetalle(venta);

        assertSame(venta, resultado.getVenta());
        assertEquals(2, resultado.getDetalles().size());
        assertEquals(10L, resultado.getDetalles().get(0).getIdPerfume());
        assertEquals(11L, resultado.getDetalles().get(1).getIdPerfume());
        assertSame(sucursal, resultado.getSucursal());
        verify(inventarioClient, times(2)).descontarStock(any(DescuentoStockRequest.class));
        verify(ventaRepository).save(venta);
    }

    @Test
    void obtenerVentaRetornaTodasLasVentas() {
        Venta venta = ventaValida();
        when(ventaRepository.findAll()).thenReturn(List.of(venta));

        List<Venta> resultado = ventaService.obtenerVenta();

        assertEquals(1, resultado.size());
        assertSame(venta, resultado.get(0));
    }

    @Test
    void obtenerVentaRetornaListaVacia() {
        when(ventaRepository.findAll()).thenReturn(Collections.emptyList());

        List<Venta> resultado = ventaService.obtenerVenta();

        assertEquals(0, resultado.size());
    }

    @Test
    void obtenerventaPorIdRetornaVentaExistente() {
        Venta venta = ventaValida();
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));

        Venta resultado = ventaService.obtenerventaPorId(1L);

        assertSame(venta, resultado);
    }

    @Test
    void obtenerventaPorIdRetornaNullSiNoExiste() {
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());

        Venta resultado = ventaService.obtenerventaPorId(99L);

        assertNull(resultado);
    }

    @Test
    void obtenerVentaPorIdRetornaVentaExistente() {
        Venta venta = ventaValida();
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));

        Venta resultado = ventaService.obtenerVentaPorId(1L);

        assertSame(venta, resultado);
    }

    @Test
    void obtenerVentaPorIdLanzaExcepcionSiNoExiste() {
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> ventaService.obtenerVentaPorId(99L));

        assertEquals("Venta no encontrada", exception.getMessage());
    }

    @Test
    void obtenerVentaDetalleRetornaVentaConDatosExternos() {
        Venta venta = ventaValida();
        Map<String, Object> perfume = Map.of("id", 10L);
        Map<String, Object> producto = Map.of("id", 10L);
        Map<String, Object> disponibilidad = Map.of("disponible", true);
        Map<String, Object> sucursal = Map.of("id", 20L);
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));
        when(datosExternosClient.obtenerPerfume(10L)).thenReturn(perfume);
        when(datosExternosClient.obtenerProducto(10L)).thenReturn(producto);
        when(datosExternosClient.obtenerDisponibilidadProducto(10L)).thenReturn(disponibilidad);
        when(datosExternosClient.obtenerSucursal(20L)).thenReturn(sucursal);

        VentaDetalleResponse resultado = ventaService.obtenerVentaDetalle(1L);

        assertSame(venta, resultado.getVenta());
        assertSame(perfume, resultado.getPerfume());
        assertSame(producto, resultado.getProducto());
        assertSame(disponibilidad, resultado.getDisponibilidadProducto());
        assertSame(sucursal, resultado.getSucursal());
    }

    @Test
    void updateVentaActualizaCampos() {
        Venta existente = ventaValida();
        Venta nueva = ventaValida();
        nueva.setEstadoVenta("ANULADA");
        nueva.setCantidad(5L);
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(ventaRepository.save(existente)).thenReturn(existente);

        Venta resultado = ventaService.updateVenta(1L, nueva);

        assertEquals("ANULADA", resultado.getEstadoVenta());
        assertEquals(5, resultado.getCantidad());
        verify(ventaRepository).save(existente);
    }

    @Test
    void updateVentaLanzaExcepcionSiNoExiste() {
        Venta venta = ventaValida();
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> ventaService.updateVenta(99L, venta));

        assertEquals("Venta no encontrada", exception.getMessage());
        verify(ventaRepository, never()).save(any(Venta.class));
    }

    @Test
    void eliminarVentaEliminaPorId() {
        ventaService.eliminarVenta(1L);

        verify(ventaRepository).deleteById(1L);
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
}
