package microservice.usuario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import microservice.usuario.model.Role;
import microservice.usuario.repository.RoleRepository;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role guardarRole(Role role) {
        return roleRepository.save(role);
    }

    public List<Role> listarRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    public Optional<Role> findByNombre(String nombre) {
        return roleRepository.findByNombre(nombre);
    }

    public Role actualizarRole(Long id, Role role) {
        return roleRepository.findById(id).map(existing -> {
            existing.setNombre(role.getNombre());
            existing.setDescripcion(role.getDescripcion());
            existing.setPermisos(role.getPermisos());
            return roleRepository.save(existing);
        }).orElse(null);
    }

    public void eliminarRole(Long id) {
        roleRepository.deleteById(id);
    }
}
