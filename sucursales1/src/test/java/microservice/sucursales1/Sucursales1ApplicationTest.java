package microservice.sucursales1;

import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class Sucursales1ApplicationTest {
    @Test
    public void mainIniciaAplicacion() {
        String[] args = {};

        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            springApplication.when(() -> SpringApplication.run(Sucursales1Application.class, args))
                    .thenReturn(null);

            Sucursales1Application.main(args);

            springApplication.verify(() -> SpringApplication.run(Sucursales1Application.class, args));
        }
    }

    @Test
    public void mainAceptaArgumentos() {
        String[] args = { "--server.port=0" };

        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            springApplication.when(() -> SpringApplication.run(Sucursales1Application.class, args))
                    .thenReturn((ConfigurableApplicationContext) null);

            Sucursales1Application.main(args);

            springApplication.verify(() -> SpringApplication.run(Sucursales1Application.class, args));
        }
    }
}
