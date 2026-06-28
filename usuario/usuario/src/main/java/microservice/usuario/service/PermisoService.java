package microservice.usuario.service;

import microservice.usuario.model.Permiso;
import microservice.usuario.repository.PermisoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermisoService {
    private final PermisoRepository permisoRepository;

    public PermisoService(PermisoRepository permisoRepository) {
        this.permisoRepository = permisoRepository;
    }

    public Permiso guardarPermiso(Permiso permiso) {
        return permisoRepository.save(permiso);
    }

    public List<Permiso> listarPermisos() {
        return permisoRepository.findAll();
    }

    public Optional<Permiso> findById(Long id) {
        return permisoRepository.findById(id);
    }

    public Permiso actualizarPermiso(Long id, Permiso permiso) {
        return permisoRepository.findById(id).map(existing -> {
            existing.modificarPermiso(permiso.getModulo(), permiso.getAccion());
            return permisoRepository.save(existing);
        }).orElse(null);
    }

    public boolean eliminarPermiso(Long id) {
        return permisoRepository.findById(id).map(existing -> {
            permisoRepository.delete(existing);
            return true;
        }).orElse(false);
    }
}
