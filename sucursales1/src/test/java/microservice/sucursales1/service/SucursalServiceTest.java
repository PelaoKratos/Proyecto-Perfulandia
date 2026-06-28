package microservice.sucursales1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import microservice.sucursales1.model.Sucursal;
import microservice.sucursales1.repository.sucursalRepository;

@ExtendWith(MockitoExtension.class)
public class SucursalServiceTest {
    @Mock
    private sucursalRepository sucursalRepository;

    @InjectMocks
    private sucursalService sucursalService;

    @Test
    public void postSucursalGuardaSucursal() {
        Sucursal sucursal = crearSucursal();
        when(sucursalRepository.save(sucursal)).thenReturn(sucursal);

        Sucursal resultado = sucursalService.postSucursal(sucursal);

        assertSame(sucursal, resultado);
        verify(sucursalRepository).save(sucursal);
        verifyNoMoreInteractions(sucursalRepository);
    }

    @Test
    public void getSucursalesRetornaLista() {
        List<Sucursal> sucursales = List.of(crearSucursal());
        when(sucursalRepository.findAll()).thenReturn(sucursales);

        assertSame(sucursales, sucursalService.getSucursales());
        verify(sucursalRepository).findAll();
        verifyNoMoreInteractions(sucursalRepository);
    }

    @Test
    public void updateSucursalModificaExistente() {
        Sucursal existente = crearSucursal();
        Sucursal cambios = new Sucursal();
        cambios.setNombre("Sucursal Norte");
        cambios.setDireccion("Av Norte 456");
        cambios.setTelefono("987654321");
        cambios.setCiudad("Valparaiso");
        cambios.setEstado("ACTIVA");
        cambios.setFechaCreacion(LocalDate.of(2026, 1, 1));
        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(sucursalRepository.save(existente)).thenReturn(existente);

        Sucursal resultado = sucursalService.updateSucursal(1L, cambios);

        assertEquals("Sucursal Norte", resultado.getNombre());
        assertEquals("Av Norte 456", resultado.getDireccion());
        assertEquals("987654321", resultado.getTelefono());
        assertEquals("Valparaiso", resultado.getCiudad());
        assertEquals("ACTIVA", resultado.getEstado());
        assertEquals(LocalDate.of(2026, 1, 1), resultado.getFechaCreacion());
        verify(sucursalRepository).findById(1L);
        verify(sucursalRepository).save(existente);
        verifyNoMoreInteractions(sucursalRepository);
    }

    @Test
    public void updateSucursalLanzaErrorSiNoExiste() {
        when(sucursalRepository.findById(9L)).thenReturn(Optional.empty());

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> sucursalService.updateSucursal(9L, crearSucursal()));

        assertEquals("La sucursal no existe", error.getMessage());
        verify(sucursalRepository).findById(9L);
        verifyNoMoreInteractions(sucursalRepository);
    }

    @Test
    public void deleteSucursalEliminaSiExiste() {
        when(sucursalRepository.existsById(1L)).thenReturn(true);

        sucursalService.deleteSucursal(1L);

        verify(sucursalRepository).deleteById(1L);
        verify(sucursalRepository).existsById(1L);
        verifyNoMoreInteractions(sucursalRepository);
    }

    @Test
    public void deleteSucursalLanzaErrorSiNoExiste() {
        when(sucursalRepository.existsById(7L)).thenReturn(false);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> sucursalService.deleteSucursal(7L));

        assertEquals("La sucursal no existe", error.getMessage());
        verify(sucursalRepository).existsById(7L);
        verifyNoMoreInteractions(sucursalRepository);
    }

    @Test
    public void postSucursalLanzaErrorSiSucursalEsNula() {
        NullPointerException error = assertThrows(NullPointerException.class,
                () -> sucursalService.postSucursal(null));

        assertEquals("La sucursal es obligatoria", error.getMessage());
        verifyNoInteractions(sucursalRepository);
    }

    @Test
    public void postSucursalLanzaErrorSiFechaEsNula() {
        Sucursal sucursal = crearSucursal();
        sucursal.setFechaCreacion(null);

        NullPointerException error = assertThrows(NullPointerException.class,
                () -> sucursalService.postSucursal(sucursal));

        assertEquals("La fecha de creacion es obligatoria", error.getMessage());
        verifyNoInteractions(sucursalRepository);
    }

    @Test
    public void postSucursalLanzaErrorSiCampoEstaVacio() {
        Sucursal sucursal = crearSucursal();
        sucursal.setNombre(" ");

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> sucursalService.postSucursal(sucursal));

        assertEquals("Los campos de la sucursal son obligatorios", error.getMessage());
        verifyNoInteractions(sucursalRepository);
    }

    @Test
    public void postSucursalLanzaErrorSiCampoEsNulo() {
        Sucursal sucursal = crearSucursal();
        sucursal.setDireccion(null);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> sucursalService.postSucursal(sucursal));

        assertEquals("Los campos de la sucursal son obligatorios", error.getMessage());
        verifyNoInteractions(sucursalRepository);
    }

    @Test
    public void updateSucursalLanzaErrorSiIdEsNulo() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> sucursalService.updateSucursal(null, crearSucursal()));

        assertEquals("El id de la sucursal debe ser mayor a cero", error.getMessage());
        verifyNoInteractions(sucursalRepository);
    }

    @Test
    public void deleteSucursalLanzaErrorSiIdEsCero() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> sucursalService.deleteSucursal(0L));

        assertEquals("El id de la sucursal debe ser mayor a cero", error.getMessage());
        verifyNoInteractions(sucursalRepository);
    }

    @Test
    public void updateSucursalLanzaErrorSiSucursalEsInvalida() {
        Sucursal sucursal = crearSucursal();
        sucursal.setTelefono("");

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> sucursalService.updateSucursal(1L, sucursal));

        assertEquals("Los campos de la sucursal son obligatorios", error.getMessage());
        verifyNoInteractions(sucursalRepository);
    }

    @Test
    public void deleteSucursalLanzaErrorSiIdEsNegativo() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> sucursalService.deleteSucursal(-1L));

        assertEquals("El id de la sucursal debe ser mayor a cero", error.getMessage());
        verifyNoInteractions(sucursalRepository);
    }

    private Sucursal crearSucursal() {
        return new Sucursal(1L, "Sucursal Centro", "Av Centro 123", "123456789", "Santiago",
                "ACTIVA", LocalDate.of(2026, 6, 12), null, List.of());
    }
}
