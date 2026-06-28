package microservice.sucursales1.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Set;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class ModelTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void sucursalEjecutaOperacionesDelDiagrama() {
        Sucursal sucursal = crearSucursal();

        sucursal.crearSucursal();
        assertEquals("ACTIVA", sucursal.getEstado());
        assertNotNull(sucursal.getFechaCreacion());
        assertTrue(sucursal.consultarSucursal());

        sucursal.modificarSucursal("Sucursal Sur", "Av Sur 456", "987654321", "Concepcion");
        assertEquals("Sucursal Sur", sucursal.getNombre());
        assertEquals("Av Sur 456", sucursal.getDireccion());
        assertEquals("987654321", sucursal.getTelefono());
        assertEquals("Concepcion", sucursal.getCiudad());

        sucursal.eliminarSucursal();
        assertEquals("INACTIVA", sucursal.getEstado());
        assertFalse(sucursal.consultarSucursal());
    }

    @Test
    public void horarioConfiguraModificaYValida() {
        HorarioSucursal horario = new HorarioSucursal();

        horario.configurarHorario("Lunes", LocalTime.of(9, 0), LocalTime.of(18, 0));
        assertEquals("Lunes", horario.getDiaSemana());
        assertTrue(horario.isActivo());
        assertTrue(horario.validarHorario());

        horario.modificarHorario(LocalTime.of(10, 0), LocalTime.of(17, 0));
        assertEquals(LocalTime.of(10, 0), horario.getHoraApertura());
        assertEquals(LocalTime.of(17, 0), horario.getHoraCierre());
        assertTrue(horario.validarHorario());
    }

    @Test
    public void horarioInvalidoCubreCasosDeValidacion() {
        HorarioSucursal horario = new HorarioSucursal();
        horario.setActivo(false);
        assertFalse(horario.validarHorario());

        horario.setActivo(true);
        assertFalse(horario.validarHorario());

        horario.setHoraApertura(LocalTime.of(9, 0));
        assertFalse(horario.validarHorario());

        horario.setHoraCierre(LocalTime.of(8, 0));
        assertFalse(horario.validarHorario());
    }

    @Test
    public void horarioSincronizaSucursalEIdSucursal() {
        Sucursal sucursal = crearSucursal();
        HorarioSucursal horario = new HorarioSucursal();

        horario.setSucursal(sucursal);

        assertSame(sucursal, horario.getSucursal());
        assertEquals(sucursal.getIdSucursal(), horario.getIdSucursal());

        horario.setSucursal(null);

        assertNull(horario.getSucursal());
        assertNull(horario.getIdSucursal());
    }

    @Test
    public void empleadoConsultaActivaYDesactiva() {
        Empleado empleado = new Empleado(1L, "Ana Perez", "11111111-1", "ana@test.cl", "123456789", "INACTIVO");

        assertEquals("Ana Perez - 11111111-1", empleado.consultarDatos());
        empleado.activar();
        assertEquals("ACTIVO", empleado.getEstado());
        empleado.desactivar();
        assertEquals("INACTIVO", empleado.getEstado());
    }

    @Test
    public void asignacionAsignaCambiaCargoYFinaliza() {
        Empleado empleado = new Empleado(2L, "Ana Perez", "11111111-1", "ana@test.cl", "123456789", "ACTIVO");
        Sucursal sucursal = crearSucursal();
        AsignacionPersonal asignacion = new AsignacionPersonal();

        asignacion.asignarPersonal(empleado, sucursal, "Cajero");
        assertSame(empleado, asignacion.getEmpleado());
        assertSame(sucursal, asignacion.getSucursal());
        assertEquals(empleado.getIdEmpleado(), asignacion.getIdEmpleado());
        assertEquals(sucursal.getIdSucursal(), asignacion.getIdSucursal());
        assertEquals("Cajero", asignacion.getCargo());
        assertNotNull(asignacion.getFechaInicio());
        assertEquals("ACTIVA", asignacion.getEstado());

        asignacion.cambiarCargo("Supervisor");
        assertEquals("Supervisor", asignacion.getCargo());

        asignacion.finalizarAsignacion();
        assertEquals("FINALIZADA", asignacion.getEstado());
        assertNotNull(asignacion.getFechaFin());
    }

    @Test
    public void asignacionSincronizaRelacionesEIds() {
        Empleado empleado = new Empleado(2L, "Ana Perez", "11111111-1", "ana@test.cl", "123456789", "ACTIVO");
        Sucursal sucursal = crearSucursal();
        HorarioSucursal horario = new HorarioSucursal(3L, 1L, "Lunes", LocalTime.of(9, 0),
                LocalTime.of(18, 0), true, sucursal);
        AsignacionPersonal asignacion = new AsignacionPersonal();

        asignacion.setEmpleado(empleado);
        asignacion.setSucursal(sucursal);
        asignacion.setHorario(horario);

        assertSame(empleado, asignacion.getEmpleado());
        assertSame(sucursal, asignacion.getSucursal());
        assertSame(horario, asignacion.getHorario());
        assertEquals(empleado.getIdEmpleado(), asignacion.getIdEmpleado());
        assertEquals(sucursal.getIdSucursal(), asignacion.getIdSucursal());
        assertEquals(horario.getIdHorario(), asignacion.getIdHorario());

        asignacion.setEmpleado(null);
        asignacion.setSucursal(null);
        asignacion.setHorario(null);

        assertNull(asignacion.getEmpleado());
        assertNull(asignacion.getSucursal());
        assertNull(asignacion.getHorario());
        assertNull(asignacion.getIdEmpleado());
        assertNull(asignacion.getIdSucursal());
        assertNull(asignacion.getIdHorario());
    }

    @Test
    public void validacionesDetectanCamposInvalidos() {
        Sucursal sucursal = new Sucursal();

        Set<ConstraintViolation<Sucursal>> errores = validator.validate(sucursal);

        assertFalse(errores.isEmpty());
    }

    @Test
    public void validacionesAceptanSucursalValida() {
        Sucursal sucursal = crearSucursal();

        Set<ConstraintViolation<Sucursal>> errores = validator.validate(sucursal);

        assertTrue(errores.isEmpty());
    }

    @Test
    public void validacionesDetectanEmpleadoInvalido() {
        Empleado empleado = new Empleado(1L, "", "", "correo-invalido", "123", "");

        Set<ConstraintViolation<Empleado>> errores = validator.validate(empleado);

        assertFalse(errores.isEmpty());
    }

    @Test
    public void validacionesAceptanEmpleadoValido() {
        Empleado empleado = new Empleado(1L, "Ana Perez", "11111111-1", "ana@test.cl", "123456789", "ACTIVO");

        Set<ConstraintViolation<Empleado>> errores = validator.validate(empleado);

        assertTrue(errores.isEmpty());
    }

    @Test
    public void validacionesDetectanHorarioInvalido() {
        HorarioSucursal horario = new HorarioSucursal();

        Set<ConstraintViolation<HorarioSucursal>> errores = validator.validate(horario);

        assertFalse(errores.isEmpty());
    }

    @Test
    public void validacionesAceptanHorarioValido() {
        HorarioSucursal horario = new HorarioSucursal(1L, 1L, "Lunes", LocalTime.of(9, 0),
                LocalTime.of(18, 0), true, crearSucursal());

        Set<ConstraintViolation<HorarioSucursal>> errores = validator.validate(horario);

        assertTrue(errores.isEmpty());
    }

    @Test
    public void validacionesDetectanAsignacionInvalida() {
        AsignacionPersonal asignacion = new AsignacionPersonal();

        Set<ConstraintViolation<AsignacionPersonal>> errores = validator.validate(asignacion);

        assertFalse(errores.isEmpty());
    }

    @Test
    public void validacionesAceptanAsignacionValida() {
        AsignacionPersonal asignacion = new AsignacionPersonal(1L, 1L, 1L, 1L, new Empleado(),
                crearSucursal(), new HorarioSucursal(), "Cajero", LocalDate.of(2026, 6, 12), null, "ACTIVA");

        Set<ConstraintViolation<AsignacionPersonal>> errores = validator.validate(asignacion);

        assertTrue(errores.isEmpty());
    }

    private Sucursal crearSucursal() {
        return new Sucursal(1L, "Sucursal Centro", "Av Centro 123", "123456789", "Santiago",
                "ACTIVA", LocalDate.of(2026, 6, 12), null, new ArrayList<>());
    }
}
