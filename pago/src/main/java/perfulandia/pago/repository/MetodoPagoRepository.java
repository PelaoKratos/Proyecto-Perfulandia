package perfulandia.pago.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import perfulandia.pago.model.MetodoPago;

public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Long> {

    List<MetodoPago> findByActivo(boolean activo);
}
