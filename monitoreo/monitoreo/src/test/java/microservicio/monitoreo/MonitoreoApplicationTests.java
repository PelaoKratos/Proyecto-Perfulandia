package microservicio.monitoreo;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MonitoreoApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainIniciaSpringApplication() {
		String[] args = { "--spring.profiles.active=test" };

		try (MockedStatic<SpringApplication> springApplication = Mockito.mockStatic(SpringApplication.class)) {
			MonitoreoApplication.main(args);

			springApplication.verify(() -> SpringApplication.run(MonitoreoApplication.class, args));
		}
	}
}
