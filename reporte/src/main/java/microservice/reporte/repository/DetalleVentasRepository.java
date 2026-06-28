package microservice.reporte.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import microservice.reporte.model.DetalleVentas;

@Repository
public interface DetalleVentasRepository extends JpaRepository<DetalleVentas, Long> {
    List<DetalleVentas> findByIdReporte(Long idReporte);
}
