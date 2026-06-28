package microservice.reporte.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import microservice.reporte.model.Reportes;

@Repository
public interface ReporteRepository extends JpaRepository<Reportes, Long> {
    
}
