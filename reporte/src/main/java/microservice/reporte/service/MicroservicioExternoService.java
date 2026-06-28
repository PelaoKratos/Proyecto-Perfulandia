package microservice.reporte.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class MicroservicioExternoService {

	private final RestTemplate restTemplate;
	private final String ventasUrl;
	private final String inventarioUrl;
	private final String sucursalesUrl;

	public MicroservicioExternoService(
			RestTemplate restTemplate,
			@Value("${microservices.ventas.url}") String ventasUrl,
			@Value("${microservices.inventario.url}") String inventarioUrl,
			@Value("${microservices.sucursales.url}") String sucursalesUrl) {
		this.restTemplate = restTemplate;
		this.ventasUrl = ventasUrl;
		this.inventarioUrl = inventarioUrl;
		this.sucursalesUrl = sucursalesUrl;
	}

	public Object obtenerVentas() {
		return obtenerRecurso(ventasUrl, "ventas");
	}

	public Object obtenerInventario() {
		return obtenerRecurso(inventarioUrl, "inventario");
	}

	public Object obtenerSucursales() {
		return obtenerRecurso(sucursalesUrl, "sucursales");
	}

	private Object obtenerRecurso(String url, String recurso) {
		try {
			ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, null, Object.class);
			return response.getBody();
		} catch (RestClientException exception) {
			return Map.of(
					"recurso", recurso,
					"disponible", false,
					"mensaje", "No se pudo conectar con microservicio de " + recurso);
		}
	}
}
