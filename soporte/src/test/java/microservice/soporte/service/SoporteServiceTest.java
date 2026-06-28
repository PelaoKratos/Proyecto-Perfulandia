package microservice.soporte.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import microservice.soporte.model.CategoriaSoporte;
import microservice.soporte.model.RespuestaTicket;
import microservice.soporte.model.TicketSoporte;
import microservice.soporte.repository.CategoriaSoporteRepository;
import microservice.soporte.repository.RespuestaTicketRepository;
import microservice.soporte.repository.TicketSoporteRepository;

@ExtendWith(MockitoExtension.class)
class SoporteServiceTest {
    @Mock
    private TicketSoporteRepository ticketRepository;
    @Mock
    private CategoriaSoporteRepository categoriaRepository;
    @Mock
    private RespuestaTicketRepository respuestaRepository;

    private SoporteService soporteService;

    @BeforeEach
    void setUp() {
        soporteService = new SoporteService(ticketRepository, categoriaRepository, respuestaRepository);
    }

    @Test
    void crearTicketDefineDatosYVinculaRelaciones() {
        CategoriaSoporte categoria = categoria();
        TicketSoporte ticket = ticket();
        RespuestaTicket respuesta = respuesta();
        ticket.setCategoria(categoria);
        ticket.setRespuestas(List.of(respuesta));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(ticketRepository.save(ticket)).thenReturn(ticket);

        TicketSoporte resultado = soporteService.crearTicket(ticket);

        assertThat(resultado.getEstado()).isEqualTo("ABIERTO");
        assertThat(resultado.getFechaCreacion()).isNotNull();
        assertThat(respuesta.getTicket()).isSameAs(ticket);
        verify(ticketRepository).save(ticket);
    }

    @Test
    void consultasDeleganEnRepositorio() {
        TicketSoporte ticket = ticket();
        when(ticketRepository.findAll()).thenReturn(List.of(ticket));
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.findByIdCliente(10L)).thenReturn(List.of(ticket));
        when(ticketRepository.findByIdCategoria(1L)).thenReturn(List.of(ticket));
        when(ticketRepository.findByIdUsuarioAsignado(2L)).thenReturn(List.of(ticket));
        when(ticketRepository.findByEstado("ABIERTO")).thenReturn(List.of(ticket));

        assertThat(soporteService.obtenerTickets()).containsExactly(ticket);
        assertThat(soporteService.obtenerTicketPorId(1L)).isEqualTo(ticket);
        assertThat(soporteService.obtenerTicketsPorCliente(10L)).containsExactly(ticket);
        assertThat(soporteService.obtenerTicketsPorCategoria(1L)).containsExactly(ticket);
        assertThat(soporteService.obtenerTicketsPorUsuarioAsignado(2L)).containsExactly(ticket);
        assertThat(soporteService.obtenerTicketsPorEstado("ABIERTO")).containsExactly(ticket);
    }

    @Test
    void actualizarYCerrarCambiarEstadoYPrioridad() {
        TicketSoporte existente = ticket();
        TicketSoporte datos = ticket();
        datos.setAsunto("Nuevo asunto");
        datos.setEstado("EN_PROCESO");
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(ticketRepository.save(any(TicketSoporte.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertThat(soporteService.actualizarTicket(1L, datos).getAsunto()).isEqualTo("Nuevo asunto");
        assertThat(soporteService.cambiarEstadoTicket(1L, "CERRADO").getFechaCierre()).isNotNull();
        assertThat(soporteService.asignarPrioridad(1L, "ALTA").getPrioridad()).isEqualTo("ALTA");
        assertThat(soporteService.cerrarTicket(1L).getEstado()).isEqualTo("CERRADO");
    }

    @Test
    void operacionesConTicketInexistenteRetornanNull() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(soporteService.actualizarTicket(99L, ticket())).isNull();
        assertThat(soporteService.cambiarEstadoTicket(99L, "CERRADO")).isNull();
        assertThat(soporteService.asignarPrioridad(99L, "ALTA")).isNull();
        assertThat(soporteService.cerrarTicket(99L)).isNull();
        assertThat(soporteService.registrarRespuesta(99L, respuesta())).isNull();
    }

    @Test
    void eliminarTicketDelegaEnRepositorio() {
        soporteService.eliminarTicket(1L);

        verify(ticketRepository).deleteById(1L);
    }

    @Test
    void categoriasSeGestionanCorrectamente() {
        CategoriaSoporte categoria = categoria();
        when(categoriaRepository.save(any(CategoriaSoporte.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        assertThat(soporteService.crearCategoria(categoria).getEstado()).isEqualTo("ACTIVA");
        assertThat(soporteService.obtenerCategorias()).containsExactly(categoria);
        assertThat(soporteService.obtenerCategoriaPorId(1L)).isEqualTo(categoria);
        assertThat(soporteService.modificarCategoria(1L, categoria).getNombre()).isEqualTo("General");
        assertThat(soporteService.desactivarCategoria(1L).getEstado()).isEqualTo("INACTIVA");
        assertThat(soporteService.activarCategoria(1L).getEstado()).isEqualTo("ACTIVA");
    }

    @Test
    void categoriasInexistentesRetornanNull() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(soporteService.obtenerCategoriaPorId(99L)).isNull();
        assertThat(soporteService.modificarCategoria(99L, categoria())).isNull();
        assertThat(soporteService.activarCategoria(99L)).isNull();
        assertThat(soporteService.desactivarCategoria(99L)).isNull();
    }

    @Test
    void respuestasSeGestionanCorrectamente() {
        TicketSoporte ticket = ticket();
        RespuestaTicket respuesta = respuesta();
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(respuestaRepository.save(any(RespuestaTicket.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(respuestaRepository.findByIdTicket(1L)).thenReturn(List.of(respuesta));
        when(respuestaRepository.findById(2L)).thenReturn(Optional.of(respuesta));

        assertThat(soporteService.registrarRespuesta(1L, respuesta).getFechaRespuesta()).isNotNull();
        assertThat(soporteService.obtenerRespuestasPorTicket(1L)).containsExactly(respuesta);
        assertThat(soporteService.modificarRespuesta(2L, respuesta).getMensaje()).isEqualTo("Respuesta del soporte");

        soporteService.eliminarRespuesta(2L);
        verify(respuestaRepository).deleteById(2L);
    }

    @Test
    void modificarRespuestaInexistenteRetornaNull() {
        when(respuestaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(soporteService.modificarRespuesta(99L, respuesta())).isNull();
    }

    private TicketSoporte ticket() {
        TicketSoporte ticket = new TicketSoporte();
        ticket.setIdTicket(1L);
        ticket.setIdCliente(10L);
        ticket.setIdUsuarioAsignado(2L);
        ticket.setAsunto("Problema");
        ticket.setDescripcion("Detalle del problema");
        ticket.setCanal("WEB");
        ticket.setPrioridad("MEDIA");
        ticket.setEstado("ABIERTO");
        return ticket;
    }

    private CategoriaSoporte categoria() {
        return new CategoriaSoporte(1L, "General", "Atencion general", "ACTIVA", List.of());
    }

    private RespuestaTicket respuesta() {
        RespuestaTicket respuesta = new RespuestaTicket();
        respuesta.setIdRespuesta(2L);
        respuesta.setIdUsuario(5L);
        respuesta.setMensaje("Respuesta del soporte");
        respuesta.setTipoRespuesta("PUBLICA");
        return respuesta;
    }
}
