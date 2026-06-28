package microservice.perfume.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import microservice.perfume.Model.CategoriaProducto;
import microservice.perfume.Service.CategoriaProductoService;

@RestController
@RequestMapping("api/v1/categorias-producto")
public class CategoriaProductoController {

    private final CategoriaProductoService categoriaService;

    public CategoriaProductoController(CategoriaProductoService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public CategoriaProducto crearCategoria(@Valid @RequestBody CategoriaProducto categoria) {
        return categoriaService.crearCategoria(categoria);
    }

    @GetMapping
    public List<CategoriaProducto> listarCategorias() {
        return categoriaService.listarCategorias();
    }

    @GetMapping("{id}")
    public CategoriaProducto obtenerCategoria(@PathVariable Long id) {
        return categoriaService.obtenerCategoria(id);
    }

    @GetMapping("estado/{estado}")
    public List<CategoriaProducto> buscarPorEstado(@PathVariable String estado) {
        return categoriaService.buscarPorEstado(estado);
    }

    @PutMapping("{id}")
    public CategoriaProducto modificarCategoria(@PathVariable Long id,
            @Valid @RequestBody CategoriaProducto categoria) {
        return categoriaService.modificarCategoria(id, categoria);
    }

    @PatchMapping("{id}/activar")
    public CategoriaProducto activarCategoria(@PathVariable Long id) {
        return categoriaService.activarCategoria(id);
    }

    @PatchMapping("{id}/desactivar")
    public CategoriaProducto desactivarCategoria(@PathVariable Long id) {
        return categoriaService.desactivarCategoria(id);
    }
}
