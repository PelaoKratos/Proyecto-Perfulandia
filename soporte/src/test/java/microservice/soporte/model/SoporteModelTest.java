package microservice.soporte.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class SoporteModelTest {

    @Test
    void categoriaEjecutaOperacionesDelDiagrama() {
        CategoriaSoporte categoria = new CategoriaSoporte();

        categoria.setNombre("Pedidos");
        categoria.setDescripcion("Problemas con pedidos");
        categoria.crearCategoria();
        categoria.modificarCategoria("Despacho", "Problemas con despacho");
        categoria.desactivarCategoria();

        assertThat(categoria.getNombre()).isEqualTo("Despacho");
        assertThat(categoria.getDescripcion()).isEqualTo("Problemas con despacho");
        assertThat(categoria.getEstado()).isEqualTo("INACTIVA");

        categoria.activarCategoria();
        assertThat(categoria.getEstado()).isEqualTo("ACTIVA");
    }

    @Test
    void ticketEjecutaOperacionesDelDiagrama() {
        TicketSoporte ticket = crearTicket();

        ticket.crearTicket();
        ticket.asignarPrioridad("ALTA");
        ticket.cambiarEstado("EN_PROCESO");

        assertThat(ticket.getFechaCreacion()).isNotNull();
        assertThat(ticket.getPrioridad()).isEqualTo("ALTA");
        assertThat(ticket.getEstado()).isEqualTo("EN_PROCESO");

        ticket.cerrarTicket();
        assertThat(ticket.getEstado()).isEqualTo("CERRADO");
        assertThat(ticket.getFechaCierre()).isNotNull();
    }

    @Test
    void ticketCerradoPorCambioEstadoDefineFechaCierre() {
        TicketSoporte ticket = crearTicket();

        ticket.cambiarEstado("CERRADO");

        assertThat(ticket.getFechaCierre()).isNotNull();
    }

    @Test
    void relacionesAsignanIdsYObjetos() {
        CategoriaSoporte categoria = new CategoriaSoporte(7L, "Cuenta", "Cuenta cliente", "ACTIVA", null);
        TicketSoporte ticket = crearTicket();
        ticket.setIdTicket(3L);
        RespuestaTicket respuesta = new RespuestaTicket();

        ticket.setCategoria(categoria);
        ticket.agregarRespuesta(respuesta);

        assertThat(ticket.getCategoria()).isSameAs(categoria);
        assertThat(ticket.getIdCategoria()).isEqualTo(7L);
        assertThat(ticket.getRespuestas()).containsExactly(respuesta);
        assertThat(respuesta.getTicket()).isSameAs(ticket);
        assertThat(respuesta.getIdTicket()).isEqualTo(3L);
    }

    @Test
    void respuestaEjecutaOperacionesDelDiagrama() {
        RespuestaTicket respuesta = new RespuestaTicket();
        respuesta.setIdUsuario(5L);
        respuesta.setMensaje("Mensaje inicial");
        respuesta.setTipoRespuesta("PUBLICA");

        respuesta.registrarRespuesta();
        respuesta.modificarRespuesta("Mensaje editado", "INTERNA");

        assertThat(respuesta.getFechaRespuesta()).isNotNull();
        assertThat(respuesta.getMensaje()).isEqualTo("Mensaje editado");
        assertThat(respuesta.getTipoRespuesta()).isEqualTo("INTERNA");

        respuesta.eliminarRespuesta();
        assertThat(respuesta.getTipoRespuesta()).isEqualTo("ELIMINADA");
    }

    @Test
    void registrarNoReemplazaFechaExistente() {
        RespuestaTicket respuesta = new RespuestaTicket();
        LocalDateTime fecha = LocalDateTime.of(2026, 6, 21, 12, 0);
        respuesta.setFechaRespuesta(fecha);

        respuesta.registrarRespuesta();

        assertThat(respuesta.getFechaRespuesta()).isEqualTo(fecha);
    }

    private TicketSoporte crearTicket() {
        TicketSoporte ticket = new TicketSoporte();
        ticket.setIdCliente(1L);
        ticket.setIdUsuarioAsignado(2L);
        ticket.setAsunto("No puedo entrar");
        ticket.setDescripcion("Error de acceso");
        ticket.setCanal("WEB");
        ticket.setPrioridad("MEDIA");
        ticket.setEstado("ABIERTO");
        return ticket;
    }
}
