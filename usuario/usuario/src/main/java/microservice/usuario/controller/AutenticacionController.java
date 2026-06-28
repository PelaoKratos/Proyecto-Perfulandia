package microservice.usuario.controller;

import microservice.usuario.model.Session;
import microservice.usuario.service.AutenticacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AutenticacionController {
    private final AutenticacionService autenticacionService;

    public AutenticacionController(AutenticacionService autenticacionService) {
        this.autenticacionService = autenticacionService;
    }

    @PostMapping("login")
    public ResponseEntity<Session> login(@RequestBody LoginRequest request) {
        return autenticacionService.login(request.email(), request.contrasena())
                .map(session -> new ResponseEntity<>(session, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @PostMapping("logout")
    public ResponseEntity<Void> logout(@RequestBody TokenRequest request) {
        if (autenticacionService.logout(request.token())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("recuperar-contrasena")
    public ResponseEntity<Void> recuperarContrasena(@RequestBody RecuperarContrasenaRequest request) {
        autenticacionService.recuperarContrasena(request.email(), request.nuevaContrasena());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("validar-token")
    public ResponseEntity<Boolean> validarToken(@RequestBody TokenRequest request) {
        return new ResponseEntity<>(autenticacionService.validarToken(request.token()), HttpStatus.OK);
    }

    public record LoginRequest(String email, String contrasena) {
    }

    public record TokenRequest(String token) {
    }

    public record RecuperarContrasenaRequest(String email, String nuevaContrasena) {
    }
}
