package microservice.cliente.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import microservice.cliente.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
}
