package perfulandia.pago.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import perfulandia.pago.client.MicroserviceValidationClient;
import perfulandia.pago.exception.ResourceNotFoundException;
import perfulandia.pago.model.Pago;
import perfulandia.pago.repository.PagoRepository;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private MicroserviceValidationClient microserviceValidationClient;

    @InjectMocks
    private PagoService pagoService;

    @Test
    void crearPagoProcesaYGuardaPago() {
        Pago pago = pagoValido();
        when(pagoRepository.save(pago)).thenReturn(pago);

        Pago resultado = pagoService.crearPago(pago);

        assertSame(pago, resultado);
        assertEquals(Pago.ESTADO_PENDIENTE, resultado.getEstado());
        verify(microserviceValidationClient).validarCliente(20L);
        verify(microserviceValidationClient).validarVenta(10L);
        verify(microserviceValidationClient).validarUsuario(30L);
        verify(pagoRepository).save(pago);
    }

    @Test
    void crearPagoLanzaErrorSiClienteNoExiste() {
        Pago pago = pagoValido();
        doThrow(new ResourceNotFoundException("Cliente no encontrado con id: 20"))
                .when(microserviceValidationClient).validarCliente(20L);

        ResourceNotFoundException error = assertThrows(ResourceNotFoundException.class,
                () -> pagoService.crearPago(pago));

        assertEquals("Cliente no encontrado con id: 20", error.getMessage());
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    void crearPagoLanzaErrorConDatosInvalidos() {
        Pago pago = pagoValido();
        pago.setMonto(0.0);

        assertThrows(IllegalArgumentException.class, () -> pagoService.crearPago(pago));
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    void crearPagoLanzaErrorConPagoNulo() {
        assertThrows(IllegalArgumentException.class, () -> pagoService.crearPago(null));
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    void obtenerPagosRetornaLista() {
        Pago pago = pagoValido();
        when(pagoRepository.findAll()).thenReturn(List.of(pago));

        List<Pago> resultado = pagoService.obtenerPagos();

        assertEquals(1, resultado.size());
        assertSame(pago, resultado.get(0));
    }

    @Test
    void obtenerPagoPorIdRetornaPagoExistente() {
        Pago pago = pagoValido();
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        Pago resultado = pagoService.obtenerPagoPorId(1L);

        assertSame(pago, resultado);
    }

    @Test
    void obtenerPagoPorIdLanzaSiNoExiste() {
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException error = assertThrows(ResourceNotFoundException.class,
                () -> pagoService.obtenerPagoPorId(99L));

        assertEquals("Pago no encontrado con id: 99", error.getMessage());
    }

    @Test
    void actualizarPagoCopiaCamposYGuarda() {
        Pago existente = pagoValido();
        Pago cambios = pagoValido();
        cambios.setMonto(29990.0);
        cambios.setMetodoPago("DEBITO");
        cambios.setEstado(Pago.ESTADO_CONFIRMADO);
        cambios.setIdVenta(11L);
        cambios.setIdCliente(21L);
        cambios.setIdEmpleado(31L);
        cambios.setIdMetodoPago(41L);
        cambios.setFechaPago(java.time.LocalDateTime.of(2026, 6, 16, 10, 30));
        cambios.setCodigoTransaccion("TX-MANUAL");
        cambios.setMensaje("Pago confirmado manualmente");
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(pagoRepository.save(existente)).thenReturn(existente);

        Pago resultado = pagoService.actualizarPago(1L, cambios);

        verify(microserviceValidationClient).validarCliente(21L);
        verify(microserviceValidationClient).validarVenta(11L);
        verify(microserviceValidationClient).validarUsuario(31L);
        assertEquals(11L, resultado.getIdVenta());
        assertEquals(21L, resultado.getIdCliente());
        assertEquals(31L, resultado.getIdEmpleado());
        assertEquals(41L, resultado.getIdMetodoPago());
        assertEquals(29990.0, resultado.getMonto());
        assertEquals("DEBITO", resultado.getMetodoPago());
        assertEquals(Pago.ESTADO_CONFIRMADO, resultado.getEstado());
        assertEquals("TX-MANUAL", resultado.getCodigoTransaccion());
        assertEquals("Pago confirmado manualmente", resultado.getMensaje());
        verify(pagoRepository).save(existente);
    }

    @Test
    void actualizarPagoLanzaErrorConDatosInvalidos() {
        Pago cambios = pagoValido();
        cambios.setIdMetodoPago(null);

        assertThrows(IllegalArgumentException.class, () -> pagoService.actualizarPago(1L, cambios));
        verify(pagoRepository, never()).findById(1L);
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    void eliminarPagoEliminaSiExiste() {
        when(pagoRepository.existsById(1L)).thenReturn(true);

        pagoService.eliminarPago(1L);

        verify(pagoRepository).deleteById(1L);
    }

    @Test
    void eliminarPagoLanzaSiNoExiste() {
        when(pagoRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> pagoService.eliminarPago(99L));
        verify(pagoRepository, never()).deleteById(99L);
    }

    @Test
    void confirmarPagoActualizaEstadoYCodigo() {
        Pago pago = pagoValido();
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(pago)).thenReturn(pago);

        Pago resultado = pagoService.confirmarPago(1L);

        assertEquals(Pago.ESTADO_CONFIRMADO, resultado.getEstado());
        org.junit.jupiter.api.Assertions.assertTrue(resultado.getCodigoTransaccion().startsWith("TX-"));
    }

    @Test
    void rechazarPagoNormalizaMotivoVacio() {
        Pago pago = pagoValido();
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(pago)).thenReturn(pago);

        Pago resultado = pagoService.rechazarPago(1L, " ");

        assertEquals(Pago.ESTADO_RECHAZADO, resultado.getEstado());
        assertEquals("Sin motivo informado", resultado.getMensaje());
    }

    @Test
    void anularPagoActualizaEstado() {
        Pago pago = pagoValido();
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(pago)).thenReturn(pago);

        Pago resultado = pagoService.anularPago(1L, "Venta cancelada");

        assertEquals(Pago.ESTADO_ANULADO, resultado.getEstado());
        assertEquals("Venta cancelada", resultado.getMensaje());
    }

    @Test
    void anularPagoNormalizaMotivoNulo() {
        Pago pago = pagoValido();
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(pago)).thenReturn(pago);

        Pago resultado = pagoService.anularPago(1L, null);

        assertEquals(Pago.ESTADO_ANULADO, resultado.getEstado());
        assertEquals("Sin motivo informado", resultado.getMensaje());
    }

    @Test
    void consultasDeleganAlRepositorio() {
        Pago pago = pagoValido();
        when(pagoRepository.findByIdVenta(10L)).thenReturn(List.of(pago));
        when(pagoRepository.findByIdCliente(20L)).thenReturn(List.of(pago));
        when(pagoRepository.findByEstado(Pago.ESTADO_CONFIRMADO)).thenReturn(List.of(pago));

        assertEquals(1, pagoService.obtenerPagosPorVenta(10L).size());
        assertEquals(1, pagoService.obtenerPagosPorCliente(20L).size());
        assertEquals(1, pagoService.obtenerPagosPorEstado(Pago.ESTADO_CONFIRMADO).size());
    }

    private Pago pagoValido() {
        Pago pago = new Pago();
        pago.setIdPago(1L);
        pago.setIdVenta(10L);
        pago.setIdCliente(20L);
        pago.setIdEmpleado(30L);
        pago.setIdMetodoPago(40L);
        pago.setMonto(49990.0);
        pago.setMetodoPago("TARJETA");
        pago.setEstado(Pago.ESTADO_PENDIENTE);
        return pago;
    }
}
