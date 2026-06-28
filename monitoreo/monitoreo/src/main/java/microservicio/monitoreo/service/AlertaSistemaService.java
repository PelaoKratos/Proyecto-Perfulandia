package microservicio.monitoreo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import microservicio.monitoreo.model.AlertaSistema;
import microservicio.monitoreo.repository.AlertaSistemaRepository;

@Service
public class AlertaSistemaService {

	private final AlertaSistemaRepository repository;

	public AlertaSistemaService(AlertaSistemaRepository repository) {
		this.repository = repository;
	}

	public AlertaSistema generar(AlertaSistema alerta) {
		if (alerta.getEstado() == null) {
			alerta.setEstado("ABIERTA");
		}
		return repository.save(alerta);
	}

	public List<AlertaSistema> listar() {
		return repository.findAll();
	}

	public List<AlertaSistema> listarPorRegistro(Long idRegistro) {
		return repository.findByIdRegistro(idRegistro);
	}

	public AlertaSistema obtenerPorId(Long id) {
		return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Alerta no encontrada: " + id));
	}

	public AlertaSistema cerrar(Long id) {
		AlertaSistema alerta = obtenerPorId(id);
		alerta.cerrarAlerta();
		return repository.save(alerta);
	}

	public void eliminar(Long id) {
		repository.deleteById(id);
	}
}
