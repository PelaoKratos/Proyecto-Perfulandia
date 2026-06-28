package microservice.sucursales1.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import microservice.sucursales1.model.Sucursal;
import microservice.sucursales1.service.sucursalService;

@ExtendWith(MockitoExtension.class)
public class SucursalControllerTest {
    @Mock
    private sucursalService sucursalService;

    @InjectMocks
    private sucursalController sucursalController;

    @Test
    public void postSucursalRetornaSucursalCreada() {
        Sucursal sucursal = crearSucursal();
        when(sucursalService.postSucursal(sucursal)).thenReturn(sucursal);

        Sucursal resultado = sucursalController.postSucursal(sucursal);

        assertSame(sucursal, resultado);
        verify(sucursalService).postSucursal(sucursal);
        verifyNoMoreInteractions(sucursalService);
    }

    @Test
    public void getSucursalesRetornaListaDelServicio() {
        List<Sucursal> sucursales = List.of(crearSucursal());
        when(sucursalService.getSucursales()).thenReturn(sucursales);

        List<Sucursal> resultado = sucursalController.getSucursales();

        assertEquals(1, resultado.size());
        assertSame(sucursales, resultado);
        verify(sucursalService).getSucursales();
        verifyNoMoreInteractions(sucursalService);
    }

    @Test
    public void getSucursalRetornaSucursalDelServicio() {
        Sucursal sucursal = crearSucursal();
        when(sucursalService.getSucursal(1L)).thenReturn(sucursal);

        Sucursal resultado = sucursalController.getSucursal(1L);

        assertSame(sucursal, resultado);
        verify(sucursalService).getSucursal(1L);
        verifyNoMoreInteractions(sucursalService);
    }

    @Test
    public void updateSucursalRetornaSucursalActualizada() {
        Sucursal sucursal = crearSucursal();
        when(sucursalService.updateSucursal(1L, sucursal)).thenReturn(sucursal);

        Sucursal resultado = sucursalController.updateSucursal(1L, sucursal);

        assertSame(sucursal, resultado);
        verify(sucursalService).updateSucursal(1L, sucursal);
        verifyNoMoreInteractions(sucursalService);
    }

    @Test
    public void deleteSucursalLlamaAlServicio() {
        sucursalController.deleteSucursal(1L);

        verify(sucursalService).deleteSucursal(1L);
        verifyNoMoreInteractions(sucursalService);
    }

    @Test
    public void updateSucursalPropagaErrorDelServicio() {
        Sucursal sucursal = crearSucursal();
        when(sucursalService.updateSucursal(99L, sucursal))
                .thenThrow(new IllegalArgumentException("La sucursal no existe"));

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> sucursalController.updateSucursal(99L, sucursal));

        assertEquals("La sucursal no existe", error.getMessage());
        verify(sucursalService).updateSucursal(99L, sucursal);
        verifyNoMoreInteractions(sucursalService);
    }

    @Test
    public void getSucursalPropagaErrorDelServicio() {
        when(sucursalService.getSucursal(99L))
                .thenThrow(new IllegalArgumentException("La sucursal no existe"));

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> sucursalController.getSucursal(99L));

        assertEquals("La sucursal no existe", error.getMessage());
        verify(sucursalService).getSucursal(99L);
        verifyNoMoreInteractions(sucursalService);
    }

    @Test
    public void deleteSucursalPropagaErrorDelServicio() {
        doThrow(new IllegalArgumentException("La sucursal no existe"))
                .when(sucursalService).deleteSucursal(99L);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> sucursalController.deleteSucursal(99L));

        assertEquals("La sucursal no existe", error.getMessage());
        verify(sucursalService).deleteSucursal(99L);
        verifyNoMoreInteractions(sucursalService);
    }

    private Sucursal crearSucursal() {
        return new Sucursal(1L, "Sucursal Centro", "Av Centro 123", "123456789", "Santiago",
                "ACTIVA", LocalDate.of(2026, 6, 12), null, List.of());
    }
}
