package microservice.ventas.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

class VentaRepositoryTest {

    @Test
    void ventaRepositoryExtiendeJpaRepository() {
        assertTrue(JpaRepository.class.isAssignableFrom(VentaRepository.class));
    }
}
