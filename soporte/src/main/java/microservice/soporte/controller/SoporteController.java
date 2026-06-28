package microservice.soporte.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import microservice.soporte.dto.TicketDetalleResponse;
import microservice.soporte.model.CategoriaSoporte;
import microservice.soporte.model.RespuestaTicket;
import microservice.soporte.model.TicketSoporte;
import microservice.soporte.service.SoporteService;

@RestController
@RequestMapping("api/v1/soporte")
public class SoporteController {
    private final SoporteService soporteService;

    public SoporteController(SoporteService soporteService) {
        this.soporteService = soporteService;
    }

    @PostMapping("tickets")
    public TicketSoporte crearTicket(@Valid @RequestBody TicketSoporte ticket) {
        return soporteService.crearTicket(ticket);
    }

    @PostMapping("tickets/detalle")
    public TicketDetalleResponse crearTicketConDetalle(@Valid @RequestBody TicketSoporte ticket) {
        return soporteService.crearTicketConDetalle(ticket);
    }

    @GetMapping("tickets")
    public List<TicketSoporte> obtenerTickets() {
        return soporteService.obtenerTickets();
    }

    @GetMapping("tickets/{id}")
    public TicketSoporte obtenerTicket(@PathVariable Long id) {
        return soporteService.obtenerTicketPorId(id);
    }

    @GetMapping("tickets/{id}/detalle")
    public TicketDetalleResponse obtenerTicketDetalle(@PathVariable Long id) {
        return soporteService.obtenerTicketDetalle(id);
    }

    @GetMapping("tickets/cliente/{idCliente}")
    public List<TicketSoporte> obtenerTicketsPorCliente(@PathVariable Long idCliente) {
        return soporteService.obtenerTicketsPorCliente(idCliente);
    }

    @GetMapping("tickets/categoria/{idCategoria}")
    public List<TicketSoporte> obtenerTicketsPorCategoria(@PathVariable Long idCategoria) {
        return soporteService.obtenerTicketsPorCategoria(idCategoria);
    }

    @GetMapping("tickets/usuario/{idUsuario}")
    public List<TicketSoporte> obtenerTicketsPorUsuarioAsignado(@PathVariable Long idUsuario) {
        return soporteService.obtenerTicketsPorUsuarioAsignado(idUsuario);
    }

    @GetMapping("tickets/estado/{estado}")
    public List<TicketSoporte> obtenerTicketsPorEstado(@PathVariable String estado) {
        return soporteService.obtenerTicketsPorEstado(estado);
    }

    @PutMapping("tickets/{id}")
    public TicketSoporte actualizarTicket(@PathVariable Long id, @Valid @RequestBody TicketSoporte ticket) {
        return soporteService.actualizarTicket(id, ticket);
    }

    @PatchMapping("tickets/{id}/cerrar")
    public TicketSoporte cerrarTicket(@PathVariable Long id) {
        return soporteService.cerrarTicket(id);
    }

    @PatchMapping("tickets/{id}/estado")
    public TicketSoporte cambiarEstadoTicket(@PathVariable Long id, @RequestBody Map<String, String> request) {
        return soporteService.cambiarEstadoTicket(id, request.get("estado"));
    }

    @PatchMapping("tickets/{id}/prioridad")
    public TicketSoporte asignarPrioridad(@PathVariable Long id, @RequestBody Map<String, String> request) {
        return soporteService.asignarPrioridad(id, request.get("prioridad"));
    }

    @DeleteMapping("tickets/{id}")
    public void eliminarTicket(@PathVariable Long id) {
        soporteService.eliminarTicket(id);
    }

    @PostMapping("categorias")
    public CategoriaSoporte crearCategoria(@Valid @RequestBody CategoriaSoporte categoria) {
        return soporteService.crearCategoria(categoria);
    }

    @GetMapping("categorias")
    public List<CategoriaSoporte> obtenerCategorias() {
        return soporteService.obtenerCategorias();
    }

    @PutMapping("categorias/{id}")
    public CategoriaSoporte modificarCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaSoporte categoria) {
        return soporteService.modificarCategoria(id, categoria);
    }

    @PatchMapping("categorias/{id}/activar")
    public CategoriaSoporte activarCategoria(@PathVariable Long id) {
        return soporteService.activarCategoria(id);
    }

    @PatchMapping("categorias/{id}/desactivar")
    public CategoriaSoporte desactivarCategoria(@PathVariable Long id) {
        return soporteService.desactivarCategoria(id);
    }

    @PostMapping("tickets/{id}/respuestas")
    public RespuestaTicket registrarRespuesta(
            @PathVariable Long id,
            @Valid @RequestBody RespuestaTicket respuesta) {
        return soporteService.registrarRespuesta(id, respuesta);
    }

    @GetMapping("tickets/{id}/respuestas")
    public List<RespuestaTicket> obtenerRespuestas(@PathVariable Long id) {
        return soporteService.obtenerRespuestasPorTicket(id);
    }

    @PutMapping("respuestas/{id}")
    public RespuestaTicket modificarRespuesta(@PathVariable Long id, @Valid @RequestBody RespuestaTicket respuesta) {
        return soporteService.modificarRespuesta(id, respuesta);
    }

    @DeleteMapping("respuestas/{id}")
    public void eliminarRespuesta(@PathVariable Long id) {
        soporteService.eliminarRespuesta(id);
    }
}
