package microservicio.monitoreo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import microservicio.monitoreo.model.AlertaSistema;

public interface AlertaSistemaRepository extends JpaRepository<AlertaSistema, Long> {

	List<AlertaSistema> findByIdRegistro(Long idRegistro);
}
