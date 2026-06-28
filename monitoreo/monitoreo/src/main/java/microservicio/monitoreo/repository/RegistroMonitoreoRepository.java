package microservicio.monitoreo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import microservicio.monitoreo.model.RegistroMonitoreo;

public interface RegistroMonitoreoRepository extends JpaRepository<RegistroMonitoreo, Long> {
}
