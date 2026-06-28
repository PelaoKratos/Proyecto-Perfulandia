package microservice.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import microservice.ventas.model.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    
}
