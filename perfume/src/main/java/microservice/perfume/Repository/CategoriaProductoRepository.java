package microservice.perfume.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import microservice.perfume.Model.CategoriaProducto;

public interface CategoriaProductoRepository extends JpaRepository<CategoriaProducto, Long> {

    List<CategoriaProducto> findByEstado(String estado);
}
