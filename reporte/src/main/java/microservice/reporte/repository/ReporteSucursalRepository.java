package microservice.reporte.repository;

import microservice.reporte.model.ReporteSucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteSucursalRepository extends JpaRepository<ReporteSucursal, Long> {
}
