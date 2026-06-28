package microservice.perfume.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import microservice.perfume.Model.CategoriaProducto;
import microservice.perfume.Repository.CategoriaProductoRepository;

@Service
@Transactional
public class CategoriaProductoService {

    private final CategoriaProductoRepository categoriaRepository;

    public CategoriaProductoService(CategoriaProductoRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public CategoriaProducto crearCategoria(CategoriaProducto categoria) {
        if (categoria.getEstado() == null || categoria.getEstado().isBlank()) {
            categoria.setEstado("ACTIVA");
        }
        return categoriaRepository.save(categoria);
    }

    public List<CategoriaProducto> listarCategorias() {
        return categoriaRepository.findAll();
    }

    public List<CategoriaProducto> buscarPorEstado(String estado) {
        return categoriaRepository.findByEstado(estado);
    }

    public CategoriaProducto obtenerCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada con id " + id));
    }

    public CategoriaProducto modificarCategoria(Long id, CategoriaProducto categoria) {
        CategoriaProducto existente = obtenerCategoria(id);
        existente.setNombre(categoria.getNombre());
        existente.setDescripcion(categoria.getDescripcion());
        existente.setEstado(categoria.getEstado());
        return categoriaRepository.save(existente);
    }

    public CategoriaProducto activarCategoria(Long id) {
        CategoriaProducto categoria = obtenerCategoria(id);
        categoria.setEstado("ACTIVA");
        return categoriaRepository.save(categoria);
    }

    public CategoriaProducto desactivarCategoria(Long id) {
        CategoriaProducto categoria = obtenerCategoria(id);
        categoria.setEstado("INACTIVA");
        return categoriaRepository.save(categoria);
    }
}
