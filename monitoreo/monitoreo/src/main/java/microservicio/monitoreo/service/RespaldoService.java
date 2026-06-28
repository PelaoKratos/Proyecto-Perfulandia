package microservicio.monitoreo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import microservicio.monitoreo.model.Respaldo;
import microservicio.monitoreo.repository.RespaldoRepository;

@Service
public class RespaldoService {

	private final RespaldoRepository repository;

	public RespaldoService(RespaldoRepository repository) {
		this.repository = repository;
	}

	public Respaldo crear(Respaldo respaldo) {
		if (!respaldo.validarRespaldo()) {
			throw new IllegalArgumentException("La ruta del archivo de respaldo es obligatoria");
		}
		return repository.save(respaldo);
	}

	public List<Respaldo> listar() {
		return repository.findAll();
	}

	public List<Respaldo> listarPorServicio(String nombreServicio) {
		return repository.findByNombreServicio(nombreServicio);
	}

	public Respaldo obtenerPorId(Long id) {
		return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Respaldo no encontrado: " + id));
	}

	public Respaldo restaurar(Long id) {
		Respaldo respaldo = obtenerPorId(id);
		respaldo.restaurarRespaldo();
		return repository.save(respaldo);
	}

	public void eliminar(Long id) {
		Respaldo respaldo = obtenerPorId(id);
		respaldo.eliminarRespaldo();
		repository.save(respaldo);
	}
}
