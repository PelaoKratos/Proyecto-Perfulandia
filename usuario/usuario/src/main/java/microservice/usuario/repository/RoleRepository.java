package microservice.usuario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import microservice.usuario.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNombre(String nombre);
}
