package microservicio.monitoreo.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import microservicio.monitoreo.model.RegistroMonitoreo;
import microservicio.monitoreo.service.RegistroMonitoreoService;

@RestController
@RequestMapping("/api/registros")
public class RegistroMonitoreoController {

	private final RegistroMonitoreoService service;

	public RegistroMonitoreoController(RegistroMonitoreoService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<RegistroMonitoreo> registrar(@RequestBody RegistroMonitoreo registro) {
		RegistroMonitoreo creado = service.registrar(registro);
		return ResponseEntity.created(URI.create("/api/registros/" + creado.getIdRegistro())).body(creado);
	}

	@GetMapping
	public List<RegistroMonitoreo> listar() {
		return service.listar();
	}

	@GetMapping("/{id}")
	public RegistroMonitoreo obtener(@PathVariable Long id) {
		return service.obtenerPorId(id);
	}

	@GetMapping("/{id}/disponibilidad")
	public boolean verificarDisponibilidad(@PathVariable Long id) {
		return service.verificarDisponibilidad(id);
	}

	@GetMapping("/{id}/rendimiento")
	public String consultarRendimiento(@PathVariable Long id) {
		return service.consultarRendimiento(id);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		service.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}
