package microservice.usuario.service;

import microservice.usuario.model.Session;
import microservice.usuario.model.Usuario;
import microservice.usuario.repository.SessionRepository;
import microservice.usuario.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacionService {
    private final UsuarioRepository usuarioRepository;
    private final SessionRepository sessionRepository;

    public AutenticacionService(UsuarioRepository usuarioRepository, SessionRepository sessionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.sessionRepository = sessionRepository;
    }

    public Optional<Session> login(String email, String contrasena) {
        return usuarioRepository.findByCorreoUsuario(email)
                .filter(usuario -> usuario.iniciarSesion(email, contrasena))
                .map(usuario -> {
                    Session session = new Session();
                    session.setIdUsuario(usuario.getIdUsuario());
                    session.generarToken();
                    return sessionRepository.save(session);
                });
    }

    public boolean logout(String token) {
        return sessionRepository.findByToken(token).map(session -> {
            session.cerrarSesion();
            sessionRepository.save(session);
            return true;
        }).orElse(false);
    }

    public void recuperarContrasena(String email, String nuevaContrasena) {
        Usuario usuario = usuarioRepository.findByCorreoUsuario(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        usuario.recuperarContrasena(nuevaContrasena);
        usuarioRepository.save(usuario);
    }

    public boolean validarToken(String token) {
        return sessionRepository.findByToken(token)
                .map(Session::validarSesion)
                .orElse(false);
    }
}
