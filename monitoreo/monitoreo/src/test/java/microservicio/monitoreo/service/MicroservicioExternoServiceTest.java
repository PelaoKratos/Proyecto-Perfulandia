package microservicio.monitoreo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import microservicio.monitoreo.model.AlertaSistema;
import microservicio.monitoreo.model.RegistroMonitoreo;

@ExtendWith(MockitoExtension.class)
class MicroservicioExternoServiceTest {

	@Mock
	private HttpClient httpClient;

	@Mock
	private RegistroMonitoreoService registroService;

	@Mock
	private AlertaSistemaService alertaService;

	@Mock
	private HttpResponse<String> respuestaOk;

	@Mock
	private HttpResponse<String> respuestaError;

	@Mock
	private HttpResponse<String> respuestaSinServicio;

	@Test
	void verificarServiciosRegistraEstadosYGeneraAlertasCuandoCorresponde() throws Exception {
		MicroservicioExternoService service = new MicroservicioExternoService(
				httpClient,
				registroService,
				alertaService,
				"usuario=http://localhost:8081/actuator/health,pedido=http://localhost:8086/actuator/health,pago=http://localhost:8087/actuator/health");
		AtomicLong secuencia = new AtomicLong(1);
		when(respuestaOk.statusCode()).thenReturn(200);
		when(respuestaError.statusCode()).thenReturn(500);
		when(respuestaSinServicio.statusCode()).thenReturn(100);
		when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
				.thenReturn(respuestaOk)
				.thenReturn(respuestaError)
				.thenReturn(respuestaSinServicio);
		when(registroService.registrar(any(RegistroMonitoreo.class))).thenAnswer(invocation -> {
			RegistroMonitoreo registro = invocation.getArgument(0);
			registro.setIdRegistro(secuencia.getAndIncrement());
			return registro;
		});

		List<RegistroMonitoreo> registros = service.verificarServicios();

		assertThat(registros).hasSize(3);
		assertThat(registros).extracting(RegistroMonitoreo::getNombreServicio)
				.containsExactly("usuario", "pedido", "pago");
		assertThat(registros).extracting(RegistroMonitoreo::getEstadoServicio)
				.containsExactly("DISPONIBLE", "CAIDO", "CAIDO");
		assertThat(registros).extracting(RegistroMonitoreo::getMensaje)
				.containsExactly(
						"Servicio disponible",
						"Servicio respondio con codigo 500",
						"Servicio respondio con codigo 100");
		verify(alertaService).generar(argThat(alerta -> alerta.getIdRegistro().equals(2L)
				&& alerta.getTipoAlerta().equals("DISPONIBILIDAD")
				&& alerta.getNivelPrioridad().equals("ALTA")
				&& alerta.getEstado().equals("ABIERTA")));
		verify(alertaService).generar(argThat(alerta -> alerta.getIdRegistro().equals(3L)
				&& alerta.getMensaje().equals("El servicio pago esta caido")));
	}

	@Test
	void verificarServiciosManejaErroresDeConexionEInterrupcion() throws Exception {
		MicroservicioExternoService service = new MicroservicioExternoService(
				httpClient,
				registroService,
				alertaService,
				"ventas=http://localhost:8088/actuator/health,despacho=http://localhost:8089/actuator/health");
		when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
				.thenThrow(new IOException("sin conexion"))
				.thenThrow(new InterruptedException("interrumpido"));
		when(registroService.registrar(any(RegistroMonitoreo.class))).thenAnswer(invocation -> {
			RegistroMonitoreo registro = invocation.getArgument(0);
			registro.setIdRegistro(9L);
			return registro;
		});

		List<RegistroMonitoreo> registros = service.verificarServicios();
		Thread.interrupted();

		assertThat(registros).extracting(RegistroMonitoreo::getEstadoServicio)
				.containsExactly("CAIDO", "CAIDO");
		assertThat(registros).extracting(RegistroMonitoreo::getMensaje)
				.containsExactly(
						"No se pudo conectar con el servicio",
						"La revision del servicio fue interrumpida");
		verify(alertaService, times(2)).generar(any(AlertaSistema.class));
	}

	@Test
	void verificarServiciosRetornaVacioSiNoHayServiciosConfigurados() {
		MicroservicioExternoService serviceSinConfig = new MicroservicioExternoService(
				httpClient, registroService, alertaService, null);
		MicroservicioExternoService serviceEnBlanco = new MicroservicioExternoService(
				httpClient, registroService, alertaService, " ");

		assertThat(serviceSinConfig.verificarServicios()).isEmpty();
		assertThat(serviceEnBlanco.verificarServicios()).isEmpty();
		verifyNoInteractions(httpClient, registroService, alertaService);
	}
}
