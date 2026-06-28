package microservice.sucursales1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import microservice.sucursales1.model.AsignacionPersonal;

public interface AsignacionPersonalRepository extends JpaRepository<AsignacionPersonal, Long> {
    default String nombreModulo() {
        return "asignacion";
    }
}
