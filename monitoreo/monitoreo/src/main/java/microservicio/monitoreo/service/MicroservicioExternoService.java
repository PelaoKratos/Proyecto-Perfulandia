package microservicio.monitoreo.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import microservicio.monitoreo.model.AlertaSistema;
import microservicio.monitoreo.model.RegistroMonitoreo;

@Service
public class MicroservicioExternoService {

	private final HttpClient httpClient;
	private final RegistroMonitoreoService registroService;
	private final AlertaSistemaService alertaService;
	private final List<String[]> servicios;

	public MicroservicioExternoService(
			HttpClient httpClient,
			RegistroMonitoreoService registroService,
			AlertaSistemaService alertaService,
			@Value("${monitoreo.servicios:}") String serviciosConfigurados) {
		this.httpClient = httpClient;
		this.registroService = registroService;
		this.alertaService = alertaService;
		this.servicios = cargarServicios(serviciosConfigurados);
	}

	public List<RegistroMonitoreo> verificarServicios() {
		List<RegistroMonitoreo> registros = new ArrayList<>();
		for (String[] servicio : servicios) {
			registros.add(verificarServicio(servicio[0], servicio[1]));
		}
		return registros;
	}

	RegistroMonitoreo verificarServicio(String nombreServicio, String url) {
		long inicio = System.nanoTime();
		String estado = "CAIDO";
		String mensaje = "Servicio no disponible";

		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(url))
					.timeout(Duration.ofSeconds(3))
					.GET()
					.build();
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			boolean disponible = response.statusCode() >= 200 && response.statusCode() < 400;
			estado = disponible ? "DISPONIBLE" : "CAIDO";
			mensaje = disponible ? "Servicio disponible" : "Servicio respondio con codigo " + response.statusCode();
		} catch (IOException ex) {
			mensaje = "No se pudo conectar con el servicio";
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
			mensaje = "La revision del servicio fue interrumpida";
		}

		RegistroMonitoreo registro = RegistroMonitoreo.builder()
				.nombreServicio(nombreServicio)
				.estadoServicio(estado)
				.tiempoRespuesta(calcularTiempoRespuesta(inicio))
				.mensaje(mensaje)
				.build();
		RegistroMonitoreo guardado = registroService.registrar(registro);
		if (!guardado.verificarDisponibilidad()) {
			alertaService.generar(crearAlerta(guardado));
		}
		return guardado;
	}

	private double calcularTiempoRespuesta(long inicio) {
		return (System.nanoTime() - inicio) / 1_000_000.0;
	}

	private AlertaSistema crearAlerta(RegistroMonitoreo registro) {
		return AlertaSistema.builder()
				.idRegistro(registro.getIdRegistro())
				.tipoAlerta("DISPONIBILIDAD")
				.nivelPrioridad("ALTA")
				.mensaje("El servicio " + registro.getNombreServicio() + " esta caido")
				.estado("ABIERTA")
				.build();
	}

	private List<String[]> cargarServicios(String serviciosConfigurados) {
		String configuracion = serviciosConfigurados == null ? "" : serviciosConfigurados.trim();
		if (configuracion.isEmpty()) {
			return List.of();
		}
		List<String[]> serviciosCargados = new ArrayList<>();
		for (String entrada : configuracion.split(",")) {
			String[] partes = entrada.trim().split("=", 2);
			serviciosCargados.add(new String[] { partes[0].trim(), partes[1].trim() });
		}
		return serviciosCargados;
	}
}
