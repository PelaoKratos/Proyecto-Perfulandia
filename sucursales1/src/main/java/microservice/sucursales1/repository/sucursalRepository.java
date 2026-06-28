package microservice.sucursales1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import microservice.sucursales1.model.Sucursal;

public interface sucursalRepository extends JpaRepository<Sucursal, Long> {
    default String nombreModulo() {
        return "sucursal";
    }
}
