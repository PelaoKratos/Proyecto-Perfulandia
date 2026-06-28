package perfulandia.pago.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

class PagoRepositoryTest {

    @Test
    void pagoRepositoryExtiendeJpaRepository() {
        assertTrue(JpaRepository.class.isAssignableFrom(PagoRepository.class));
    }

    @Test
    void nombreDeConsultasDebeMantenerContratoDelDominio() throws NoSuchMethodException {
        assertEquals("findByIdVenta", PagoRepository.class.getMethod("findByIdVenta", Long.class).getName());
        assertEquals("findByIdCliente", PagoRepository.class.getMethod("findByIdCliente", Long.class).getName());
        assertEquals("findByEstado", PagoRepository.class.getMethod("findByEstado", String.class).getName());
    }
}
