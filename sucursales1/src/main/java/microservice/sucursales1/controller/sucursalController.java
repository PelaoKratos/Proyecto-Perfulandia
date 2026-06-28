package microservice.sucursales1.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import microservice.sucursales1.model.Sucursal;
import microservice.sucursales1.service.sucursalService;

@RestController
@RequestMapping("api/v1/sucursales")
public class sucursalController {
    private final sucursalService sucursalService;

    public sucursalController(sucursalService sucursalService) {
        this.sucursalService = sucursalService;
    }

    @PostMapping
    public Sucursal postSucursal(@Valid @RequestBody Sucursal sucursal) {
        return sucursalService.postSucursal(sucursal);
    }

    @GetMapping
    public List<Sucursal> getSucursales() {
        return sucursalService.getSucursales();
    }

    @GetMapping("{id}")
    public Sucursal getSucursal(@PathVariable Long id) {
        return sucursalService.getSucursal(id);
    }

    @PutMapping("{id}")
    public Sucursal updateSucursal(@PathVariable Long id, @Valid @RequestBody Sucursal sucursal) {
        return sucursalService.updateSucursal(id, sucursal);
    }

    @DeleteMapping("{id}")
    public void deleteSucursal(@PathVariable Long id) {
        sucursalService.deleteSucursal(id);
    }
}
