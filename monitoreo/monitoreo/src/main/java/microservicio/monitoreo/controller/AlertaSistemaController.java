package microservicio.monitoreo.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import microservicio.monitoreo.model.AlertaSistema;
import microservicio.monitoreo.service.AlertaSistemaService;

@RestController
@RequestMapping("/api/alertas")
public class AlertaSistemaController {

	private final AlertaSistemaService service;

	public AlertaSistemaController(AlertaSistemaService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<AlertaSistema> generar(@RequestBody AlertaSistema alerta) {
		AlertaSistema creada = service.generar(alerta);
		return ResponseEntity.created(URI.create("/api/alertas/" + creada.getIdAlerta())).body(creada);
	}

	@GetMapping
	public List<AlertaSistema> listar() {
		return service.listar();
	}

	@GetMapping("/registro/{idRegistro}")
	public List<AlertaSistema> listarPorRegistro(@PathVariable Long idRegistro) {
		return service.listarPorRegistro(idRegistro);
	}

	@GetMapping("/{id}")
	public AlertaSistema obtener(@PathVariable Long id) {
		return service.obtenerPorId(id);
	}

	@PatchMapping("/{id}/cerrar")
	public AlertaSistema cerrar(@PathVariable Long id) {
		return service.cerrar(id);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		service.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}
