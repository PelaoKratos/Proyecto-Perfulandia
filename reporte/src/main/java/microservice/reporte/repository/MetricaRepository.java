package microservice.reporte.repository;

import java.util.List;
import microservice.reporte.model.Metrica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricaRepository extends JpaRepository<Metrica, Long> {
    List<Metrica> findByReporteIdReporte(Long idReporte);
}
