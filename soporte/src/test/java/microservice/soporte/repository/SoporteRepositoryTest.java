package microservice.soporte.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import microservice.soporte.model.CategoriaSoporte;
import microservice.soporte.model.RespuestaTicket;
import microservice.soporte.model.TicketSoporte;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SoporteRepositoryTest {
    @Autowired
    private TicketSoporteRepository ticketRepository;
    @Autowired
    private CategoriaSoporteRepository categoriaRepository;
    @Autowired
    private RespuestaTicketRepository respuestaRepository;

    @Test
    void repositoriosPersistenDominioSoporte() {
        CategoriaSoporte categoria = categoriaRepository.save(
                new CategoriaSoporte(null, "General", "Atencion general", "ACTIVA", null));
        TicketSoporte ticket = ticket();
        ticket.setCategoria(categoria);
        TicketSoporte guardado = ticketRepository.save(ticket);
        RespuestaTicket respuesta = respuesta();
        respuesta.setTicket(guardado);
        respuestaRepository.save(respuesta);

        assertThat(categoriaRepository.findByEstado("ACTIVA")).hasSize(1);
        assertThat(ticketRepository.findByIdCliente(10L)).hasSize(1);
        assertThat(ticketRepository.findByIdCategoria(categoria.getIdCategoria())).hasSize(1);
        assertThat(ticketRepository.findByIdUsuarioAsignado(2L)).hasSize(1);
        assertThat(ticketRepository.findByEstado("ABIERTO")).hasSize(1);
        assertThat(ticketRepository.findByPrioridad("MEDIA")).hasSize(1);
        assertThat(respuestaRepository.findByIdTicket(guardado.getIdTicket())).hasSize(1);
        assertThat(respuestaRepository.findByIdUsuario(5L)).hasSize(1);
    }

    private TicketSoporte ticket() {
        TicketSoporte ticket = new TicketSoporte();
        ticket.setIdCliente(10L);
        ticket.setIdUsuarioAsignado(2L);
        ticket.setAsunto("Problema");
        ticket.setDescripcion("Detalle");
        ticket.setCanal("WEB");
        ticket.setPrioridad("MEDIA");
        ticket.setEstado("ABIERTO");
        return ticket;
    }

    private RespuestaTicket respuesta() {
        RespuestaTicket respuesta = new RespuestaTicket();
        respuesta.setIdUsuario(5L);
        respuesta.setMensaje("Respuesta");
        respuesta.setTipoRespuesta("PUBLICA");
        return respuesta;
    }
}
