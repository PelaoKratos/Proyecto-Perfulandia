package microservice.sucursales1.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AsignacionPersonal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAsignacion;

    @Column(name = "id_sucursal", insertable = false, updatable = false)
    private Long idSucursal;

    @Column(name = "id_empleado", insertable = false, updatable = false)
    private Long idEmpleado;

    @Column(name = "id_horario", insertable = false, updatable = false)
    private Long idHorario;

    @ManyToOne
    @JoinColumn(name = "id_empleado")
    private Empleado empleado;

    @ManyToOne
    @JoinColumn(name = "id_sucursal")
    private Sucursal sucursal;

    @ManyToOne
    @JoinColumn(name = "id_horario")
    private HorarioSucursal horario;

    @NotBlank(message = "El cargo no puede estar vacio")
    @Column(nullable = false)
    private String cargo;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @NotBlank(message = "El estado no puede estar vacio")
    @Column(nullable = false)
    private String estado;

    public void asignarPersonal(Empleado empleado, Sucursal sucursal, String cargo) {
        setEmpleado(empleado);
        setSucursal(sucursal);
        this.cargo = cargo;
        this.fechaInicio = LocalDate.now();
        this.estado = "ACTIVA";
    }

    public void cambiarCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
        this.idEmpleado = empleado != null ? empleado.getIdEmpleado() : null;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
        this.idSucursal = sucursal != null ? sucursal.getIdSucursal() : null;
    }

    public void setHorario(HorarioSucursal horario) {
        this.horario = horario;
        this.idHorario = horario != null ? horario.getIdHorario() : null;
    }

    public void finalizarAsignacion() {
        this.fechaFin = LocalDate.now();
        this.estado = "FINALIZADA";
    }
}
