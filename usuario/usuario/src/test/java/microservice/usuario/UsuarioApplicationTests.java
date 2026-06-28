package microservice.usuario;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UsuarioApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainRunsSpringApplication() {
		String[] args = {};

		try (MockedStatic<SpringApplication> springApplication = Mockito.mockStatic(SpringApplication.class)) {
			UsuarioApplication.main(args);

			springApplication.verify(() -> SpringApplication.run(UsuarioApplication.class, args));
		}
	}

}
