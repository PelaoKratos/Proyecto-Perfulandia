package microservicio.monitoreo;

import static org.mockito.ArgumentMatchers.any;
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

import microservicio.monitoreo.controller.AlertaSistemaController;
import microservicio.monitoreo.model.AlertaSistema;
import microservicio.monitoreo.service.AlertaSistemaService;

@ExtendWith(MockitoExtension.class)
class AlertaSistemaControllerTest {

	@Mock
	private AlertaSistemaService service;

	@InjectMocks
	private AlertaSistemaController controller;

	@Test
	void generarRetornaCreated() {
		AlertaSistema alerta = AlertaSistema.builder()
				.idAlerta(1L)
				.tipoAlerta("CPU")
				.nivelPrioridad("ALTA")
				.mensaje("Uso elevado")
				.estado("ABIERTA")
				.build();
		when(service.generar(any(AlertaSistema.class))).thenReturn(alerta);

		ResponseEntity<AlertaSistema> respuesta = controller.generar(alerta);

		assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(respuesta.getBody()).isNotNull();
		assertThat(respuesta.getBody().getEstado()).isEqualTo("ABIERTA");
	}

	@Test
	void cerrarRetornaAlertaCerrada() {
		AlertaSistema alerta = AlertaSistema.builder()
				.idAlerta(1L)
				.estado("CERRADA")
				.build();
		when(service.cerrar(1L)).thenReturn(alerta);

		AlertaSistema respuesta = controller.cerrar(1L);

		assertThat(respuesta.getEstado()).isEqualTo("CERRADA");
	}

	@Test
	void listarRetornaAlertas() {
		AlertaSistema alerta = AlertaSistema.builder().idAlerta(1L).build();
		when(service.listar()).thenReturn(List.of(alerta));

		assertThat(controller.listar()).containsExactly(alerta);
	}

	@Test
	void listarPorRegistroRetornaAlertasDelRegistro() {
		AlertaSistema alerta = AlertaSistema.builder().idRegistro(5L).build();
		when(service.listarPorRegistro(5L)).thenReturn(List.of(alerta));

		assertThat(controller.listarPorRegistro(5L)).containsExactly(alerta);
	}

	@Test
	void obtenerRetornaAlerta() {
		AlertaSistema alerta = AlertaSistema.builder().idAlerta(3L).build();
		when(service.obtenerPorId(3L)).thenReturn(alerta);

		assertThat(controller.obtener(3L)).isSameAs(alerta);
	}

	@Test
	void eliminarRetornaNoContent() {
		ResponseEntity<Void> respuesta = controller.eliminar(9L);

		assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		verify(service).eliminar(9L);
	}
}
