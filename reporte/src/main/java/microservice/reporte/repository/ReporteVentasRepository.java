package microservice.reporte.repository;

import microservice.reporte.model.ReporteVentas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteVentasRepository extends JpaRepository<ReporteVentas, Long> {
}
