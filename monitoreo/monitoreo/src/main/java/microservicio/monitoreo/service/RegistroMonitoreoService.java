package microservicio.monitoreo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import microservicio.monitoreo.model.RegistroMonitoreo;
import microservicio.monitoreo.repository.RegistroMonitoreoRepository;

@Service
public class RegistroMonitoreoService {

	private final RegistroMonitoreoRepository repository;

	public RegistroMonitoreoService(RegistroMonitoreoRepository repository) {
		this.repository = repository;
	}

	public RegistroMonitoreo registrar(RegistroMonitoreo registro) {
		return repository.save(registro);
	}

	public List<RegistroMonitoreo> listar() {
		return repository.findAll();
	}

	public RegistroMonitoreo obtenerPorId(Long id) {
		return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Registro no encontrado: " + id));
	}

	public boolean verificarDisponibilidad(Long id) {
		return obtenerPorId(id).verificarDisponibilidad();
	}

	public String consultarRendimiento(Long id) {
		return obtenerPorId(id).consultarRendimiento();
	}

	public void eliminar(Long id) {
		repository.deleteById(id);
	}
}
