package microservice.usuario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import microservice.usuario.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    Optional<Usuario> findByCorreoUsuario(String correoUsuario);
}
