package microservice.soporte.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import microservice.soporte.model.CategoriaSoporte;
import microservice.soporte.model.RespuestaTicket;
import microservice.soporte.model.TicketSoporte;
import microservice.soporte.service.SoporteService;

@ExtendWith(MockitoExtension.class)
class SoporteControllerTest {
    @Mock
    private SoporteService soporteService;

    private SoporteController soporteController;

    @BeforeEach
    void setUp() {
        soporteController = new SoporteController(soporteService);
    }

    @Test
    void endpointsDeTicketDeleganEnServicio() {
        TicketSoporte ticket = ticket();
        when(soporteService.crearTicket(ticket)).thenReturn(ticket);
        when(soporteService.obtenerTickets()).thenReturn(List.of(ticket));
        when(soporteService.obtenerTicketPorId(1L)).thenReturn(ticket);
        when(soporteService.obtenerTicketsPorCliente(10L)).thenReturn(List.of(ticket));
        when(soporteService.obtenerTicketsPorCategoria(1L)).thenReturn(List.of(ticket));
        when(soporteService.obtenerTicketsPorUsuarioAsignado(2L)).thenReturn(List.of(ticket));
        when(soporteService.obtenerTicketsPorEstado("ABIERTO")).thenReturn(List.of(ticket));
        when(soporteService.actualizarTicket(1L, ticket)).thenReturn(ticket);
        when(soporteService.cerrarTicket(1L)).thenReturn(ticket);
        when(soporteService.cambiarEstadoTicket(1L, "CERRADO")).thenReturn(ticket);
        when(soporteService.asignarPrioridad(1L, "ALTA")).thenReturn(ticket);

        assertThat(soporteController.crearTicket(ticket)).isEqualTo(ticket);
        assertThat(soporteController.obtenerTickets()).containsExactly(ticket);
        assertThat(soporteController.obtenerTicket(1L)).isEqualTo(ticket);
        assertThat(soporteController.obtenerTicketsPorCliente(10L)).containsExactly(ticket);
        assertThat(soporteController.obtenerTicketsPorCategoria(1L)).containsExactly(ticket);
        assertThat(soporteController.obtenerTicketsPorUsuarioAsignado(2L)).containsExactly(ticket);
        assertThat(soporteController.obtenerTicketsPorEstado("ABIERTO")).containsExactly(ticket);
        assertThat(soporteController.actualizarTicket(1L, ticket)).isEqualTo(ticket);
        assertThat(soporteController.cerrarTicket(1L)).isEqualTo(ticket);
        assertThat(soporteController.cambiarEstadoTicket(1L, Map.of("estado", "CERRADO"))).isEqualTo(ticket);
        assertThat(soporteController.asignarPrioridad(1L, Map.of("prioridad", "ALTA"))).isEqualTo(ticket);

        soporteController.eliminarTicket(1L);
        verify(soporteService).eliminarTicket(1L);
    }

    @Test
    void endpointsDeCategoriaDeleganEnServicio() {
        CategoriaSoporte categoria = new CategoriaSoporte(1L, "General", "Atencion", "ACTIVA", List.of());
        when(soporteService.crearCategoria(categoria)).thenReturn(categoria);
        when(soporteService.obtenerCategorias()).thenReturn(List.of(categoria));
        when(soporteService.modificarCategoria(1L, categoria)).thenReturn(categoria);
        when(soporteService.activarCategoria(1L)).thenReturn(categoria);
        when(soporteService.desactivarCategoria(1L)).thenReturn(categoria);

        assertThat(soporteController.crearCategoria(categoria)).isEqualTo(categoria);
        assertThat(soporteController.obtenerCategorias()).containsExactly(categoria);
        assertThat(soporteController.modificarCategoria(1L, categoria)).isEqualTo(categoria);
        assertThat(soporteController.activarCategoria(1L)).isEqualTo(categoria);
        assertThat(soporteController.desactivarCategoria(1L)).isEqualTo(categoria);
    }

    @Test
    void endpointsDeRespuestaDeleganEnServicio() {
        RespuestaTicket respuesta = respuesta();
        when(soporteService.registrarRespuesta(1L, respuesta)).thenReturn(respuesta);
        when(soporteService.obtenerRespuestasPorTicket(1L)).thenReturn(List.of(respuesta));
        when(soporteService.modificarRespuesta(2L, respuesta)).thenReturn(respuesta);

        assertThat(soporteController.registrarRespuesta(1L, respuesta)).isEqualTo(respuesta);
        assertThat(soporteController.obtenerRespuestas(1L)).containsExactly(respuesta);
        assertThat(soporteController.modificarRespuesta(2L, respuesta)).isEqualTo(respuesta);

        soporteController.eliminarRespuesta(2L);
        verify(soporteService).eliminarRespuesta(2L);
    }

    private TicketSoporte ticket() {
        TicketSoporte ticket = new TicketSoporte();
        ticket.setIdTicket(1L);
        ticket.setIdCliente(10L);
        ticket.setAsunto("Problema");
        ticket.setDescripcion("Detalle");
        ticket.setCanal("WEB");
        ticket.setPrioridad("MEDIA");
        ticket.setEstado("ABIERTO");
        return ticket;
    }

    private RespuestaTicket respuesta() {
        RespuestaTicket respuesta = new RespuestaTicket();
        respuesta.setIdRespuesta(2L);
        respuesta.setIdUsuario(5L);
        respuesta.setMensaje("Respuesta");
        respuesta.setTipoRespuesta("PUBLICA");
        return respuesta;
    }
}
