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

import microservicio.monitoreo.model.Respaldo;
import microservicio.monitoreo.service.RespaldoService;

@RestController
@RequestMapping("/api/respaldos")
public class RespaldoController {

	private final RespaldoService service;

	public RespaldoController(RespaldoService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<Respaldo> crear(@RequestBody Respaldo respaldo) {
		Respaldo creado = service.crear(respaldo);
		return ResponseEntity.created(URI.create("/api/respaldos/" + creado.getIdRespaldo())).body(creado);
	}

	@GetMapping
	public List<Respaldo> listar() {
		return service.listar();
	}

	@GetMapping("/servicio/{nombreServicio}")
	public List<Respaldo> listarPorServicio(@PathVariable String nombreServicio) {
		return service.listarPorServicio(nombreServicio);
	}

	@GetMapping("/{id}")
	public Respaldo obtener(@PathVariable Long id) {
		return service.obtenerPorId(id);
	}

	@PatchMapping("/{id}/restaurar")
	public Respaldo restaurar(@PathVariable Long id) {
		return service.restaurar(id);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		service.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}
