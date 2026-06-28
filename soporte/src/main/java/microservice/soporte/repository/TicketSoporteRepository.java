package microservice.soporte.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import microservice.soporte.model.TicketSoporte;

public interface TicketSoporteRepository extends JpaRepository<TicketSoporte, Long> {
    List<TicketSoporte> findByIdCliente(Long idCliente);
    List<TicketSoporte> findByIdCategoria(Long idCategoria);
    List<TicketSoporte> findByIdUsuarioAsignado(Long idUsuarioAsignado);
    List<TicketSoporte> findByEstado(String estado);
    List<TicketSoporte> findByPrioridad(String prioridad);
}
