package microservice.soporte.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import microservice.soporte.client.DatosExternosClient;
import microservice.soporte.dto.TicketDetalleResponse;
import microservice.soporte.model.CategoriaSoporte;
import microservice.soporte.model.RespuestaTicket;
import microservice.soporte.model.TicketSoporte;
import microservice.soporte.repository.CategoriaSoporteRepository;
import microservice.soporte.repository.RespuestaTicketRepository;
import microservice.soporte.repository.TicketSoporteRepository;

@Service
@Transactional
public class SoporteService {
    private final TicketSoporteRepository ticketRepository;
    private final CategoriaSoporteRepository categoriaRepository;
    private final RespuestaTicketRepository respuestaRepository;

    @Autowired
    private DatosExternosClient datosExternosClient;

    public SoporteService(
            TicketSoporteRepository ticketRepository,
            CategoriaSoporteRepository categoriaRepository,
            RespuestaTicketRepository respuestaRepository) {
        this.ticketRepository = ticketRepository;
        this.categoriaRepository = categoriaRepository;
        this.respuestaRepository = respuestaRepository;
    }

    public TicketSoporte crearTicket(TicketSoporte ticket) {
        vincularCategoria(ticket);
        vincularRespuestas(ticket);
        ticket.crearTicket();
        return ticketRepository.save(ticket);
    }

    public TicketDetalleResponse crearTicketConDetalle(TicketSoporte ticket) {
        TicketSoporte ticketGuardado = crearTicket(ticket);
        return construirDetalle(ticketGuardado);
    }

    public List<TicketSoporte> obtenerTickets() {
        return ticketRepository.findAll();
    }

    public TicketSoporte obtenerTicketPorId(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }

    public TicketDetalleResponse obtenerTicketDetalle(Long id) {
        TicketSoporte ticket = obtenerTicketPorId(id);
        return ticket == null ? null : construirDetalle(ticket);
    }

    public List<TicketSoporte> obtenerTicketsPorCliente(Long idCliente) {
        return ticketRepository.findByIdCliente(idCliente);
    }

    public List<TicketSoporte> obtenerTicketsPorCategoria(Long idCategoria) {
        return ticketRepository.findByIdCategoria(idCategoria);
    }

    public List<TicketSoporte> obtenerTicketsPorUsuarioAsignado(Long idUsuarioAsignado) {
        return ticketRepository.findByIdUsuarioAsignado(idUsuarioAsignado);
    }

    public List<TicketSoporte> obtenerTicketsPorEstado(String estado) {
        return ticketRepository.findByEstado(estado);
    }

    public TicketSoporte actualizarTicket(Long id, TicketSoporte datos) {
        TicketSoporte existente = obtenerTicketPorId(id);
        if (existente == null) {
            return null;
        }
        existente.setIdCliente(datos.getIdCliente());
        existente.setIdUsuarioAsignado(datos.getIdUsuarioAsignado());
        existente.setAsunto(datos.getAsunto());
        existente.setDescripcion(datos.getDescripcion());
        existente.setCanal(datos.getCanal());
        existente.setPrioridad(datos.getPrioridad());
        existente.setEstado(datos.getEstado());
        existente.setFechaCierre(datos.getFechaCierre());
        existente.setCategoria(datos.getCategoria());
        vincularCategoria(existente);
        return ticketRepository.save(existente);
    }

    public TicketSoporte cerrarTicket(Long id) {
        TicketSoporte ticket = obtenerTicketPorId(id);
        if (ticket == null) {
            return null;
        }
        ticket.cerrarTicket();
        return ticketRepository.save(ticket);
    }

    public TicketSoporte cambiarEstadoTicket(Long id, String estado) {
        TicketSoporte ticket = obtenerTicketPorId(id);
        if (ticket == null) {
            return null;
        }
        ticket.cambiarEstado(estado);
        return ticketRepository.save(ticket);
    }

    public TicketSoporte asignarPrioridad(Long id, String prioridad) {
        TicketSoporte ticket = obtenerTicketPorId(id);
        if (ticket == null) {
            return null;
        }
        ticket.asignarPrioridad(prioridad);
        return ticketRepository.save(ticket);
    }

    public void eliminarTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    public CategoriaSoporte crearCategoria(CategoriaSoporte categoria) {
        categoria.crearCategoria();
        return categoriaRepository.save(categoria);
    }

    public List<CategoriaSoporte> obtenerCategorias() {
        return categoriaRepository.findAll();
    }

    public CategoriaSoporte obtenerCategoriaPorId(Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    public CategoriaSoporte modificarCategoria(Long id, CategoriaSoporte datos) {
        CategoriaSoporte categoria = obtenerCategoriaPorId(id);
        if (categoria == null) {
            return null;
        }
        categoria.modificarCategoria(datos.getNombre(), datos.getDescripcion());
        return categoriaRepository.save(categoria);
    }

    public CategoriaSoporte activarCategoria(Long id) {
        CategoriaSoporte categoria = obtenerCategoriaPorId(id);
        if (categoria == null) {
            return null;
        }
        categoria.activarCategoria();
        return categoriaRepository.save(categoria);
    }

    public CategoriaSoporte desactivarCategoria(Long id) {
        CategoriaSoporte categoria = obtenerCategoriaPorId(id);
        if (categoria == null) {
            return null;
        }
        categoria.desactivarCategoria();
        return categoriaRepository.save(categoria);
    }

    public RespuestaTicket registrarRespuesta(Long idTicket, RespuestaTicket respuesta) {
        TicketSoporte ticket = obtenerTicketPorId(idTicket);
        if (ticket == null) {
            return null;
        }
        respuesta.setTicket(ticket);
        respuesta.registrarRespuesta();
        return respuestaRepository.save(respuesta);
    }

    public List<RespuestaTicket> obtenerRespuestasPorTicket(Long idTicket) {
        return respuestaRepository.findByIdTicket(idTicket);
    }

    public RespuestaTicket modificarRespuesta(Long idRespuesta, RespuestaTicket datos) {
        RespuestaTicket respuesta = respuestaRepository.findById(idRespuesta).orElse(null);
        if (respuesta == null) {
            return null;
        }
        respuesta.modificarRespuesta(datos.getMensaje(), datos.getTipoRespuesta());
        return respuestaRepository.save(respuesta);
    }

    public void eliminarRespuesta(Long idRespuesta) {
        respuestaRepository.deleteById(idRespuesta);
    }

    private void vincularCategoria(TicketSoporte ticket) {
        if (ticket.getCategoria() != null && ticket.getCategoria().getIdCategoria() != null) {
            CategoriaSoporte categoria = categoriaRepository.findById(ticket.getCategoria().getIdCategoria())
                    .orElse(ticket.getCategoria());
            ticket.setCategoria(categoria);
        }
    }

    private void vincularRespuestas(TicketSoporte ticket) {
        if (ticket.getRespuestas() != null) {
            ticket.getRespuestas().forEach(respuesta -> respuesta.setTicket(ticket));
        }
    }

    private TicketDetalleResponse construirDetalle(TicketSoporte ticket) {
        return new TicketDetalleResponse(
                ticket,
                datosExternosClient.obtenerCliente(ticket.getIdCliente()),
                datosExternosClient.obtenerUsuario(ticket.getIdUsuarioAsignado()));
    }
}
