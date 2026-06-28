package microservice.reporte.service;

import org.springframework.stereotype.Service;

import microservice.reporte.dto.ResumenMicroservicios;

@Service
public class ReporteIntegracionService {

	private final MicroservicioExternoService microservicioExternoService;

	public ReporteIntegracionService(MicroservicioExternoService microservicioExternoService) {
		this.microservicioExternoService = microservicioExternoService;
	}

	public ResumenMicroservicios obtenerResumenMicroservicios() {
		return new ResumenMicroservicios(
				microservicioExternoService.obtenerVentas(),
				microservicioExternoService.obtenerInventario(),
				microservicioExternoService.obtenerSucursales());
	}
}
