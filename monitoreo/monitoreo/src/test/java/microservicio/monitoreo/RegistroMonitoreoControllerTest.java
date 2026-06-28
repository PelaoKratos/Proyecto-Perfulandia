package microservicio.monitoreo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import microservicio.monitoreo.controller.RegistroMonitoreoController;
import microservicio.monitoreo.model.RegistroMonitoreo;
import microservicio.monitoreo.service.RegistroMonitoreoService;

@ExtendWith(MockitoExtension.class)
class RegistroMonitoreoControllerTest {

	@Mock
	private RegistroMonitoreoService service;

	@InjectMocks
	private RegistroMonitoreoController controller;

	@Test
	void registrarRetornaCreated() {
		RegistroMonitoreo registro = RegistroMonitoreo.builder().idRegistro(1L).build();
		when(service.registrar(registro)).thenReturn(registro);

		ResponseEntity<RegistroMonitoreo> respuesta = controller.registrar(registro);

		assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(respuesta.getHeaders().getLocation()).hasToString("/api/registros/1");
		assertThat(respuesta.getBody()).isSameAs(registro);
	}

	@Test
	void listarRetornaRegistros() {
		RegistroMonitoreo registro = RegistroMonitoreo.builder().idRegistro(2L).build();
		when(service.listar()).thenReturn(List.of(registro));

		assertThat(controller.listar()).containsExactly(registro);
	}

	@Test
	void obtenerRetornaRegistro() {
		RegistroMonitoreo registro = RegistroMonitoreo.builder().idRegistro(3L).build();
		when(service.obtenerPorId(3L)).thenReturn(registro);

		assertThat(controller.obtener(3L)).isSameAs(registro);
	}

	@Test
	void verificarDisponibilidadRetornaResultadoDelServicio() {
		when(service.verificarDisponibilidad(4L)).thenReturn(true);

		assertThat(controller.verificarDisponibilidad(4L)).isTrue();
	}

	@Test
	void consultarRendimientoRetornaResultadoDelServicio() {
		when(service.consultarRendimiento(5L)).thenReturn("NORMAL");

		assertThat(controller.consultarRendimiento(5L)).isEqualTo("NORMAL");
	}

	@Test
	void eliminarRetornaNoContent() {
		ResponseEntity<Void> respuesta = controller.eliminar(6L);

		assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		verify(service).eliminar(6L);
	}
}
