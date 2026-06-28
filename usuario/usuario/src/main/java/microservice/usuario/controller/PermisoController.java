package microservice.usuario.controller;

import microservice.usuario.model.Permiso;
import microservice.usuario.service.PermisoService;
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

import java.util.List;

@RestController
@RequestMapping("api/v1/permisos")
public class PermisoController {
    private final PermisoService permisoService;

    public PermisoController(PermisoService permisoService) {
        this.permisoService = permisoService;
    }

    @GetMapping
    public ResponseEntity<List<Permiso>> getPermisos() {
        List<Permiso> permisos = permisoService.listarPermisos();
        if (permisos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(permisos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Permiso> postPermiso(@RequestBody Permiso permiso) {
        return new ResponseEntity<>(permisoService.guardarPermiso(permiso), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Permiso> getPermiso(@PathVariable Long id) {
        return permisoService.findById(id)
                .map(permiso -> new ResponseEntity<>(permiso, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("{id}")
    public ResponseEntity<Permiso> putPermiso(@PathVariable Long id, @RequestBody Permiso permiso) {
        Permiso actualizado = permisoService.actualizarPermiso(id, permiso);
        if (actualizado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(actualizado, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePermiso(@PathVariable Long id) {
        if (permisoService.eliminarPermiso(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
