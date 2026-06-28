package microservice.sucursales1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import microservice.sucursales1.model.HorarioSucursal;

public interface HorarioSucursalRepository extends JpaRepository<HorarioSucursal, Long> {
    default String nombreModulo() {
        return "horario";
    }
}
