package microservice.perfume.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import microservice.perfume.Model.ResenaProducto;

public interface ResenaProductoRepository extends JpaRepository<ResenaProducto, Long> {

    List<ResenaProducto> findByProductoIdProducto(Long idProducto);

    List<ResenaProducto> findByIdCliente(Long idCliente);
}
