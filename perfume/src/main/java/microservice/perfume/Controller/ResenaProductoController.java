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
import microservice.perfume.Model.ResenaProducto;
import microservice.perfume.Service.ResenaProductoService;

@RestController
@RequestMapping("api/v1/resenas-producto")
public class ResenaProductoController {

    private final ResenaProductoService resenaService;

    public ResenaProductoController(ResenaProductoService resenaService) {
        this.resenaService = resenaService;
    }

    @PostMapping
    public ResenaProducto crearResena(@Valid @RequestBody ResenaProducto resena) {
        return resenaService.crearResena(resena);
    }

    @GetMapping
    public List<ResenaProducto> listarResenas() {
        return resenaService.listarResenas();
    }

    @GetMapping("{id}")
    public ResenaProducto obtenerResena(@PathVariable Long id) {
        return resenaService.obtenerResena(id);
    }

    @GetMapping("producto/{idProducto}")
    public List<ResenaProducto> listarPorProducto(@PathVariable Long idProducto) {
        return resenaService.listarPorProducto(idProducto);
    }

    @GetMapping("cliente/{idCliente}")
    public List<ResenaProducto> listarPorCliente(@PathVariable Long idCliente) {
        return resenaService.listarPorCliente(idCliente);
    }

    @PutMapping("{id}")
    public ResenaProducto modificarResena(@PathVariable Long id, @Valid @RequestBody ResenaProducto resena) {
        return resenaService.modificarResena(id, resena);
    }

    @PatchMapping("{id}/eliminar")
    public ResenaProducto eliminarResena(@PathVariable Long id) {
        return resenaService.eliminarResena(id);
    }

    @GetMapping("validar-compra/cliente/{idCliente}/pedido/{idPedido}/producto/{idProducto}")
    public boolean validarCompra(@PathVariable Long idCliente, @PathVariable Long idPedido,
            @PathVariable Long idProducto) {
        return resenaService.validarCompra(idCliente, idPedido, idProducto);
    }
}
