package microservice.sucursales1.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import microservice.sucursales1.model.AsignacionPersonal;
import microservice.sucursales1.model.Empleado;
import microservice.sucursales1.model.HorarioSucursal;
import microservice.sucursales1.model.Sucursal;

@DataJpaTest
public class RepositoryH2Test {
    @Autowired
    private sucursalRepository sucursalRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private HorarioSucursalRepository horarioSucursalRepository;

    @Autowired
    private AsignacionPersonalRepository asignacionPersonalRepository;

    @Test
    public void guardaEntidadesDelDiagramaEnH2() {
        Sucursal sucursal = sucursalRepository.save(new Sucursal(null, "Sucursal Centro", "Av Centro 123",
                "123456789", "Santiago", "ACTIVA", LocalDate.of(2026, 6, 12), null, null));
        Empleado empleado = empleadoRepository.save(new Empleado(null, "Ana Perez", "11111111-1",
                "ana@test.cl", "123456789", "ACTIVO"));
        HorarioSucursal horario = horarioSucursalRepository.save(new HorarioSucursal(null, null, "Lunes",
                LocalTime.of(9, 0), LocalTime.of(18, 0), true, sucursal));
        AsignacionPersonal asignacion = asignacionPersonalRepository.save(new AsignacionPersonal(null, null, null,
                null, empleado, sucursal, horario, "Cajero", LocalDate.of(2026, 6, 12), null, "ACTIVA"));

        assertTrue(sucursalRepository.findById(sucursal.getIdSucursal()).isPresent());
        assertEquals("Ana Perez", empleadoRepository.findById(empleado.getIdEmpleado()).orElseThrow().getNombre());
        assertEquals("Lunes", horarioSucursalRepository.findById(horario.getIdHorario()).orElseThrow().getDiaSemana());
        assertEquals("Cajero", asignacionPersonalRepository.findById(asignacion.getIdAsignacion()).orElseThrow().getCargo());
    }

    @Test
    public void repositoriosRetornanNombreDeModulo() {
        assertEquals("sucursal", sucursalRepository.nombreModulo());
        assertEquals("empleado", empleadoRepository.nombreModulo());
        assertEquals("horario", horarioSucursalRepository.nombreModulo());
        assertEquals("asignacion", asignacionPersonalRepository.nombreModulo());
    }
}
