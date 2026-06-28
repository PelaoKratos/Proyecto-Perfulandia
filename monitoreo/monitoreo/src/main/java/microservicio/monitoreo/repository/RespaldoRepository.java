package microservicio.monitoreo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import microservicio.monitoreo.model.Respaldo;

public interface RespaldoRepository extends JpaRepository<Respaldo, Long> {

	List<Respaldo> findByNombreServicio(String nombreServicio);
}
