package microservice.cliente;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

@SpringBootTest
class ClienteApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void main_deberiaIniciarAplicacionSpringBoot() {
		String[] args = { "--spring.profiles.active=test" };

		try (var springApplication = mockStatic(SpringApplication.class)) {
			ClienteApplication.main(args);

			springApplication.verify(() -> SpringApplication.run(ClienteApplication.class, args));
		}
	}

}
