package microservice.sucursales1.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import microservice.sucursales1.model.Sucursal;
import microservice.sucursales1.repository.sucursalRepository;

@Service
@Transactional
public class sucursalService {
    private final sucursalRepository sucursalRepository;

    public sucursalService(sucursalRepository sucursalRepository) {
        this.sucursalRepository = sucursalRepository;
    }

    public Sucursal postSucursal(Sucursal sucursal) {
        validarSucursal(sucursal);
        return sucursalRepository.save(sucursal);
    }

    public void deleteSucursal(Long id) {
        validarId(id);
        if (!sucursalRepository.existsById(id)) {
            throw new IllegalArgumentException("La sucursal no existe");
        }
        sucursalRepository.deleteById(id);
    }

    public Sucursal updateSucursal(Long id, Sucursal sucursal) {
        validarId(id);
        validarSucursal(sucursal);
        Sucursal sucursalExistente = sucursalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La sucursal no existe"));
        sucursalExistente.setNombre(sucursal.getNombre());
        sucursalExistente.setDireccion(sucursal.getDireccion());
        sucursalExistente.setTelefono(sucursal.getTelefono());
        sucursalExistente.setCiudad(sucursal.getCiudad());
        sucursalExistente.setEstado(sucursal.getEstado());
        sucursalExistente.setFechaCreacion(sucursal.getFechaCreacion());
        return sucursalRepository.save(sucursalExistente);
    }

    public List<Sucursal> getSucursales() {
        return sucursalRepository.findAll();
    }

    public Sucursal getSucursal(Long id) {
        validarId(id);
        return sucursalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La sucursal no existe"));
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El id de la sucursal debe ser mayor a cero");
        }
    }

    private void validarSucursal(Sucursal sucursal) {
        Objects.requireNonNull(sucursal, "La sucursal es obligatoria");
        Objects.requireNonNull(sucursal.getFechaCreacion(), "La fecha de creacion es obligatoria");

        boolean tieneCampoVacio = Stream.of(
                sucursal.getNombre(),
                sucursal.getDireccion(),
                sucursal.getTelefono(),
                sucursal.getCiudad(),
                sucursal.getEstado())
                .anyMatch(campo -> campo == null || campo.trim().isEmpty());

        if (tieneCampoVacio) {
            throw new IllegalArgumentException("Los campos de la sucursal son obligatorios");
        }
    }
}
