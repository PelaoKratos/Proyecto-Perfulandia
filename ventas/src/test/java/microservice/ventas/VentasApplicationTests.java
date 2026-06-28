package microservice.ventas;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class VentasApplicationTests {

	@Test
	void aplicacionExiste() {
		assertNotNull(new VentasApplication());
	}

}
