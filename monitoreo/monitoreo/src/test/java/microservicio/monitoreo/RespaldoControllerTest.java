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

import microservicio.monitoreo.controller.RespaldoController;
import microservicio.monitoreo.model.Respaldo;
import microservicio.monitoreo.service.RespaldoService;

@ExtendWith(MockitoExtension.class)
class RespaldoControllerTest {

	@Mock
	private RespaldoService service;

	@InjectMocks
	private RespaldoController controller;

	@Test
	void crearRetornaCreated() {
		Respaldo respaldo = Respaldo.builder().idRespaldo(1L).rutaArchivo("/tmp/respaldo.sql").build();
		when(service.crear(respaldo)).thenReturn(respaldo);

		ResponseEntity<Respaldo> respuesta = controller.crear(respaldo);

		assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(respuesta.getHeaders().getLocation()).hasToString("/api/respaldos/1");
		assertThat(respuesta.getBody()).isSameAs(respaldo);
	}

	@Test
	void listarRetornaRespaldos() {
		Respaldo respaldo = Respaldo.builder().idRespaldo(2L).build();
		when(service.listar()).thenReturn(List.of(respaldo));

		assertThat(controller.listar()).containsExactly(respaldo);
	}

	@Test
	void listarPorServicioRetornaRespaldosDelServicio() {
		Respaldo respaldo = Respaldo.builder().nombreServicio("pagos").build();
		when(service.listarPorServicio("pagos")).thenReturn(List.of(respaldo));

		assertThat(controller.listarPorServicio("pagos")).containsExactly(respaldo);
	}

	@Test
	void obtenerRetornaRespaldo() {
		Respaldo respaldo = Respaldo.builder().idRespaldo(3L).build();
		when(service.obtenerPorId(3L)).thenReturn(respaldo);

		assertThat(controller.obtener(3L)).isSameAs(respaldo);
	}

	@Test
	void restaurarRetornaRespaldoRestaurado() {
		Respaldo respaldo = Respaldo.builder().idRespaldo(4L).estado("RESTAURADO").build();
		when(service.restaurar(4L)).thenReturn(respaldo);

		assertThat(controller.restaurar(4L).getEstado()).isEqualTo("RESTAURADO");
	}

	@Test
	void eliminarRetornaNoContent() {
		ResponseEntity<Void> respuesta = controller.eliminar(5L);

		assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		verify(service).eliminar(5L);
	}
}
