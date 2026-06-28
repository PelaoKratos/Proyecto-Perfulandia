package microservice.usuario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import microservice.usuario.model.Usuario;
import microservice.usuario.service.UsuarioService;


@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    /*
     * public UsuarioController(UsuarioService service) {
     * this.service = service;
     * }
     */

    @GetMapping
    public ResponseEntity<List<Usuario>> getUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        if (usuarios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(usuarios, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<Usuario> postUsuario(@RequestBody Usuario usuario) {
        Usuario nuevo;
        try {
            nuevo = usuarioService.guardarUsuario(usuario);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 409 si los usuarios están duplicados
        }

    }

    @GetMapping("{id}")
    public ResponseEntity<Usuario> getUsuario(@PathVariable Long id) {
        Usuario buscado=usuarioService.findById(id).orElse(null); //.orElse controla el tipo Optional<Usuario>
        if(buscado==null)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(buscado,HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Usuario> putUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario actualizado = usuarioService.updateUsuario(id, usuario);
        if (actualizado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(actualizado, HttpStatus.OK);
    }
    
    @DeleteMapping("{id}")
    public ResponseEntity<Usuario> deleteUsuario(@PathVariable Long id) {
        Usuario eliminado = usuarioService.deleteUsuario(id);
        if (eliminado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(eliminado, HttpStatus.OK);
    }

    @PutMapping("{id}/activar")
    public ResponseEntity<Usuario> activarUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioService.activarUsuario(id);
        if (usuario == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @PutMapping("{id}/desactivar")
    public ResponseEntity<Usuario> desactivarUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioService.desactivarUsuario(id);
        if (usuario == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }
}
