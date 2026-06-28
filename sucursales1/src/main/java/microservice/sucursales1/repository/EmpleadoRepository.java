package microservice.sucursales1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import microservice.sucursales1.model.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    default String nombreModulo() {
        return "empleado";
    }
}
