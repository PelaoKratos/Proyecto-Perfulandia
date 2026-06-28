package perfulandia.pago.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import perfulandia.pago.model.ComprobantePago;

public interface ComprobantePagoRepository extends JpaRepository<ComprobantePago, Long> {

    List<ComprobantePago> findByIdPago(Long idPago);

    List<ComprobantePago> findByEstado(String estado);
}
