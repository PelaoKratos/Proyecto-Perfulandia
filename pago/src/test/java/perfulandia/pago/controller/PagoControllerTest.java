package perfulandia.pago.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import perfulandia.pago.exception.ResourceNotFoundException;
import perfulandia.pago.model.Pago;
import perfulandia.pago.service.PagoService;

@WebMvcTest(PagoController.class)
class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PagoService pagoService;

    @Test
    void crearPagoRetornaCreated() throws Exception {
        when(pagoService.crearPago(any(Pago.class))).thenReturn(pagoValido());

        mockMvc.perform(post("/api/v1/pagos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPago()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPago").value(1L))
                .andExpect(jsonPath("$.estado").value(Pago.ESTADO_PENDIENTE));
    }

    @Test
    void obtenerPagosRetornaLista() throws Exception {
        when(pagoService.obtenerPagos()).thenReturn(List.of(pagoValido()));

        mockMvc.perform(get("/api/v1/pagos").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPago").value(1L));
    }

    @Test
    void obtenerPagoPorIdRetornaPago() throws Exception {
        when(pagoService.obtenerPagoPorId(1L)).thenReturn(pagoValido());

        mockMvc.perform(get("/api/v1/pagos/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monto").value(49990.0));
    }

    @Test
    void obtenerPagoPorIdRetorna404SiNoExiste() throws Exception {
        when(pagoService.obtenerPagoPorId(99L)).thenThrow(new ResourceNotFoundException("Pago no encontrado con id: 99"));

        mockMvc.perform(get("/api/v1/pagos/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pago no encontrado con id: 99"));
    }

    @Test
    void actualizarPagoRetornaPagoActualizado() throws Exception {
        Pago pago = pagoValido();
        pago.setEstado(Pago.ESTADO_CONFIRMADO);
        when(pagoService.actualizarPago(eq(1L), any(Pago.class))).thenReturn(pago);

        mockMvc.perform(put("/api/v1/pagos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPago()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value(Pago.ESTADO_CONFIRMADO));
    }

    @Test
    void eliminarPagoRetornaMensaje() throws Exception {
        mockMvc.perform(delete("/api/v1/pagos/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Pago eliminado correctamente"));

        verify(pagoService).eliminarPago(1L);
    }

    @Test
    void confirmarPagoRetornaPagoConfirmado() throws Exception {
        Pago pago = pagoValido();
        pago.confirmar("TX-123");
        when(pagoService.confirmarPago(1L)).thenReturn(pago);

        mockMvc.perform(post("/api/v1/pagos/1/confirmar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value(Pago.ESTADO_CONFIRMADO))
                .andExpect(jsonPath("$.codigoTransaccion").value("TX-123"));
    }

    @Test
    void rechazarPagoRetornaPagoRechazado() throws Exception {
        Pago pago = pagoValido();
        pago.rechazar("Fondos insuficientes");
        when(pagoService.rechazarPago(1L, "Fondos insuficientes")).thenReturn(pago);

        mockMvc.perform(post("/api/v1/pagos/1/rechazar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"motivo\":\"Fondos insuficientes\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value(Pago.ESTADO_RECHAZADO));
    }

    @Test
    void rechazarPagoPermiteMotivoVacio() throws Exception {
        Pago pago = pagoValido();
        pago.rechazar("Sin motivo informado");
        when(pagoService.rechazarPago(1L, null)).thenReturn(pago);

        mockMvc.perform(post("/api/v1/pagos/1/rechazar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Sin motivo informado"));
    }

    @Test
    void anularPagoRetornaPagoAnulado() throws Exception {
        Pago pago = pagoValido();
        pago.anular("Venta cancelada");
        when(pagoService.anularPago(1L, "Venta cancelada")).thenReturn(pago);

        mockMvc.perform(post("/api/v1/pagos/1/anular")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"motivo\":\"Venta cancelada\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value(Pago.ESTADO_ANULADO));
    }

    @Test
    void errorDeValidacionDelServicioRetorna400() throws Exception {
        when(pagoService.crearPago(any(Pago.class))).thenThrow(new IllegalArgumentException("Los datos del pago son invalidos"));

        mockMvc.perform(post("/api/v1/pagos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPago()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Los datos del pago son invalidos"));
    }

    @Test
    void filtrosRetornanListas() throws Exception {
        when(pagoService.obtenerPagosPorVenta(10L)).thenReturn(List.of(pagoValido()));
        when(pagoService.obtenerPagosPorCliente(20L)).thenReturn(List.of(pagoValido()));
        when(pagoService.obtenerPagosPorEstado(Pago.ESTADO_PENDIENTE)).thenReturn(List.of(pagoValido()));

        mockMvc.perform(get("/api/v1/pagos/venta/10")).andExpect(status().isOk()).andExpect(jsonPath("$[0].idVenta").value(10L));
        mockMvc.perform(get("/api/v1/pagos/cliente/20")).andExpect(status().isOk()).andExpect(jsonPath("$[0].idCliente").value(20L));
        mockMvc.perform(get("/api/v1/pagos/estado/PENDIENTE")).andExpect(status().isOk()).andExpect(jsonPath("$[0].estado").value(Pago.ESTADO_PENDIENTE));
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

    private String jsonPago() {
        return """
                {
                  "idVenta": 10,
                  "idCliente": 20,
                  "idEmpleado": 30,
                  "idMetodoPago": 40,
                  "monto": 49990.0,
                  "metodoPago": "TARJETA",
                  "estado": "PENDIENTE"
                }
                """;
    }
}
