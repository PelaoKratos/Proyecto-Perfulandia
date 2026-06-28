package perfulandia.pago.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import perfulandia.pago.model.TransaccionPago;

public interface TransaccionPagoRepository extends JpaRepository<TransaccionPago, Long> {

    List<TransaccionPago> findByIdPago(Long idPago);

    List<TransaccionPago> findByEstado(String estado);
}
