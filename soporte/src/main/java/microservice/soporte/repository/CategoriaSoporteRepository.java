package microservice.soporte.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import microservice.soporte.model.CategoriaSoporte;

public interface CategoriaSoporteRepository extends JpaRepository<CategoriaSoporte, Long> {
    List<CategoriaSoporte> findByEstado(String estado);
}
