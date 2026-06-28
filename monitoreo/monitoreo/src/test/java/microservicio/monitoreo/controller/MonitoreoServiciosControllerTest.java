package microservicio.monitoreo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import microservicio.monitoreo.model.RegistroMonitoreo;
import microservicio.monitoreo.service.MicroservicioExternoService;

@ExtendWith(MockitoExtension.class)
class MonitoreoServiciosControllerTest {

	@Mock
	private MicroservicioExternoService service;

	@InjectMocks
	private MonitoreoServiciosController controller;

	@Test
	void verificarServiciosRetornaRegistrosDelServicio() {
		RegistroMonitoreo registro = RegistroMonitoreo.builder()
				.idRegistro(1L)
				.nombreServicio("usuario")
				.estadoServicio("DISPONIBLE")
				.build();
		when(service.verificarServicios()).thenReturn(List.of(registro));

		assertThat(controller.verificarServicios()).containsExactly(registro);
	}
}
