package perfulandia.pago.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import perfulandia.pago.model.Pago;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByIdVenta(Long idVenta);

    List<Pago> findByIdCliente(Long idCliente);

    List<Pago> findByEstado(String estado);
}
