package microservice.usuario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import microservice.usuario.model.Role;
import microservice.usuario.model.Usuario;
import microservice.usuario.repository.RoleRepository;
import microservice.usuario.repository.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    public Usuario guardarUsuario(Usuario usuario) {
        usuario.setRolUsuario(resolveRole(usuario.getRolUsuario()));
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario updateUsuario(Long id, Usuario usuario) {
        Usuario usuarioExistente = usuarioRepository.findById(id).orElse(null);
        if (usuarioExistente == null) {
            return null;
        }
        usuarioExistente.actualizarDatos(usuario.getNombreUsuario(), usuario.getApellidoUsuario(), usuario.getCorreoUsuario());
        usuarioExistente.setContraseñaUsuario(usuario.getContraseñaUsuario());
        usuarioExistente.setRolUsuario(resolveRole(usuario.getRolUsuario()));
        return usuarioRepository.save(usuarioExistente);
    }

    public Usuario deleteUsuario(Long id) {
        Usuario usuarioExistente = usuarioRepository.findById(id).orElse(null);
        if (usuarioExistente != null) {
            usuarioRepository.delete(usuarioExistente);
        }
        return usuarioExistente;
    }

    public Usuario activarUsuario(Long id) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.activar();
            return usuarioRepository.save(usuario);
        }).orElse(null);
    }

    public Usuario desactivarUsuario(Long id) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.desactivar();
            return usuarioRepository.save(usuario);
        }).orElse(null);
    }

    private Role resolveRole(Role role) {
        if (role == null) {
            return null;
        }
        if (role.getIdRol() != null) {
            return roleRepository.findById(role.getIdRol()).orElse(role);
        }
        if (role.getNombre() != null) {
            return roleRepository.findByNombre(role.getNombre()).orElse(role);
        }
        return role;
    }
}
