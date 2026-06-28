package microservicio.monitoreo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import microservicio.monitoreo.model.RegistroMonitoreo;
import microservicio.monitoreo.service.MicroservicioExternoService;

@RestController
@RequestMapping("/api/monitoreo")
public class MonitoreoServiciosController {

	private final MicroservicioExternoService service;

	public MonitoreoServiciosController(MicroservicioExternoService service) {
		this.service = service;
	}

	@GetMapping("/servicios")
	public List<RegistroMonitoreo> verificarServicios() {
		return service.verificarServicios();
	}
}
