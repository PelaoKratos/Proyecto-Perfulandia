package microservice.perfume.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import microservice.perfume.Model.Producto;
import microservice.perfume.Model.ResenaProducto;
import microservice.perfume.Repository.ProductoRepository;
import microservice.perfume.Repository.ResenaProductoRepository;

@Service
@Transactional
public class ResenaProductoService {

    private final ResenaProductoRepository resenaRepository;
    private final ProductoRepository productoRepository;

    public ResenaProductoService(ResenaProductoRepository resenaRepository, ProductoRepository productoRepository) {
        this.resenaRepository = resenaRepository;
        this.productoRepository = productoRepository;
    }

    public ResenaProducto crearResena(ResenaProducto resena) {
        Producto producto = buscarProducto(resena.getProducto().getIdProducto());
        resena.setProducto(producto);
        if (resena.getFechaResena() == null) {
            resena.setFechaResena(LocalDateTime.now());
        }
        if (resena.getEstado() == null || resena.getEstado().isBlank()) {
            resena.setEstado("ACTIVA");
        }
        return resenaRepository.save(resena);
    }

    public List<ResenaProducto> listarResenas() {
        return resenaRepository.findAll();
    }

    public List<ResenaProducto> listarPorProducto(Long idProducto) {
        return resenaRepository.findByProductoIdProducto(idProducto);
    }

    public List<ResenaProducto> listarPorCliente(Long idCliente) {
        return resenaRepository.findByIdCliente(idCliente);
    }

    public ResenaProducto obtenerResena(Long id) {
        return resenaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resena no encontrada con id " + id));
    }

    public ResenaProducto modificarResena(Long id, ResenaProducto resena) {
        ResenaProducto existente = obtenerResena(id);
        existente.setCalificacion(resena.getCalificacion());
        existente.setComentario(resena.getComentario());
        existente.setEstado(resena.getEstado());
        return resenaRepository.save(existente);
    }

    public ResenaProducto eliminarResena(Long id) {
        ResenaProducto resena = obtenerResena(id);
        resena.setEstado("ELIMINADA");
        return resenaRepository.save(resena);
    }

    public boolean validarCompra(Long idCliente, Long idPedido, Long idProducto) {
        return idCliente != null && idPedido != null && idProducto != null;
    }

    private Producto buscarProducto(Long idProducto) {
        return productoRepository.findById(idProducto)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id " + idProducto));
    }
}
