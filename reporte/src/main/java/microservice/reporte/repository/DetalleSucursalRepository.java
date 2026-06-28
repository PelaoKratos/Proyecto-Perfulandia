package microservice.reporte.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import microservice.reporte.model.DetalleSucursal;

@Repository
public interface DetalleSucursalRepository extends JpaRepository<DetalleSucursal, Long> {
    List<DetalleSucursal> findByIdReporte(Long idReporte);
}
