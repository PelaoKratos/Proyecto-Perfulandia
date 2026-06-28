package microservice.ventas.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class VentaTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void ventaValidaNoTieneErroresDeValidacion() {
        Venta venta = ventaValida();

        Set<ConstraintViolation<Venta>> errores = validator.validate(venta);

        assertTrue(errores.isEmpty());
    }

    @Test
    void cantidadDebeSerMayorACero() {
        Venta venta = ventaValida();
        venta.setCantidad(0L);

        Set<ConstraintViolation<Venta>> errores = validator.validate(venta);

        assertEquals(1, errores.size());
        assertEquals("cantidad", errores.iterator().next().getPropertyPath().toString());
    }

    @Test
    void camposObligatoriosNoPuedenSerNulosOVacios() {
        Venta venta = new Venta();
        venta.setTotalVenta(-1.0);
        venta.setDescuentoVenta(-1.0);
        venta.setEstadoVenta("");
        venta.setCantidad(0L);

        Set<ConstraintViolation<Venta>> errores = validator.validate(venta);

        assertTrue(errores.stream().anyMatch(error -> error.getPropertyPath().toString().equals("fechaVenta")));
        assertTrue(errores.stream().anyMatch(error -> error.getPropertyPath().toString().equals("totalVenta")));
        assertTrue(errores.stream().anyMatch(error -> error.getPropertyPath().toString().equals("descuentoVenta")));
        assertTrue(errores.stream().anyMatch(error -> error.getPropertyPath().toString().equals("estadoVenta")));
        assertTrue(errores.stream().anyMatch(error -> error.getPropertyPath().toString().equals("idPerfume")));
        assertTrue(errores.stream().anyMatch(error -> error.getPropertyPath().toString().equals("idSucursal")));
        assertTrue(errores.stream().anyMatch(error -> error.getPropertyPath().toString().equals("cantidad")));
    }

    @Test
    void descuentoPuedeSerNuloPorqueEsOpcional() {
        Venta venta = ventaValida();
        venta.setDescuentoVenta(null);

        Set<ConstraintViolation<Venta>> errores = validator.validate(venta);

        assertTrue(errores.isEmpty());
    }

    @Test
    void constructorConArgumentosAsignaTodosLosCampos() {
        Venta venta = new Venta(
                1L,
                LocalDate.of(2026, 6, 12),
                59990.0,
                0.0,
                "PAGADA",
                10L,
                20L,
                2L);

        assertEquals(1L, venta.getIdVenta());
        assertEquals(LocalDate.of(2026, 6, 12), venta.getFechaVenta());
        assertEquals(59990.0, venta.getTotalVenta());
        assertEquals(0.0, venta.getDescuentoVenta());
        assertEquals("PAGADA", venta.getEstadoVenta());
        assertEquals(10L, venta.getIdPerfume());
        assertEquals(20L, venta.getIdSucursal());
        assertEquals(2L, venta.getCantidad());
    }

    @Test
    void settersActualizanTodosLosCampos() {
        Venta venta = new Venta();

        venta.setIdVenta(1L);
        venta.setFechaVenta(LocalDate.of(2026, 6, 12));
        venta.setTotalVenta(59990.0);
        venta.setDescuentoVenta(1000.0);
        venta.setEstadoVenta("PAGADA");
        venta.setIdPerfume(10L);
        venta.setIdSucursal(20L);
        venta.setCantidad(3L);

        assertEquals(1L, venta.getIdVenta());
        assertEquals(LocalDate.of(2026, 6, 12), venta.getFechaVenta());
        assertEquals(59990.0, venta.getTotalVenta());
        assertEquals(1000.0, venta.getDescuentoVenta());
        assertEquals("PAGADA", venta.getEstadoVenta());
        assertEquals(10L, venta.getIdPerfume());
        assertEquals(20L, venta.getIdSucursal());
        assertEquals(3L, venta.getCantidad());
    }

    @Test
    void metodosGeneradosPorLombokFuncionan() {
        Venta venta = ventaValida();
        venta.setIdVenta(1L);
        Venta mismaVenta = ventaValida();
        mismaVenta.setIdVenta(1L);
        Venta otraVenta = ventaValida();
        otraVenta.setIdVenta(2L);

        assertEquals(venta, mismaVenta);
        assertEquals(venta.hashCode(), mismaVenta.hashCode());
        assertNotEquals(venta, otraVenta);
        assertNotNull(venta.toString());
    }

    private Venta ventaValida() {
        Venta venta = new Venta();
        venta.setFechaVenta(LocalDate.of(2026, 6, 12));
        venta.setTotalVenta(59990.0);
        venta.setDescuentoVenta(0.0);
        venta.setEstadoVenta("PAGADA");
        venta.setIdPerfume(1L);
        venta.setIdSucursal(1L);
        venta.setCantidad(2L);
        return venta;
    }
}
