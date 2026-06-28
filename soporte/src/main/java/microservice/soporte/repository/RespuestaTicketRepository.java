package microservice.soporte.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import microservice.soporte.model.RespuestaTicket;

public interface RespuestaTicketRepository extends JpaRepository<RespuestaTicket, Long> {
    List<RespuestaTicket> findByIdTicket(Long idTicket);
    List<RespuestaTicket> findByIdUsuario(Long idUsuario);
}
