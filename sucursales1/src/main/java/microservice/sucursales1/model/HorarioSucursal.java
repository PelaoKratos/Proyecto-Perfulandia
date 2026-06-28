package microservice.sucursales1.model;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HorarioSucursal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHorario;

    @Column(name = "id_sucursal", insertable = false, updatable = false)
    private Long idSucursal;

    @NotBlank(message = "El dia de semana es obligatorio")
    @Column(nullable = false)
    private String diaSemana;

    @NotNull(message = "La hora de apertura es obligatoria")
    @Column(nullable = false)
    private LocalTime horaApertura;

    @NotNull(message = "La hora de cierre es obligatoria")
    @Column(nullable = false)
    private LocalTime horaCierre;

    @Column(nullable = false)
    private boolean activo;

    @OneToOne
    @JoinColumn(name = "id_sucursal")
    private Sucursal sucursal;

    public void configurarHorario(String diaSemana, LocalTime horaApertura, LocalTime horaCierre) {
        this.diaSemana = diaSemana;
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
        this.activo = true;
    }

    public void modificarHorario(LocalTime horaApertura, LocalTime horaCierre) {
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
        this.idSucursal = sucursal != null ? sucursal.getIdSucursal() : null;
    }

    public boolean validarHorario() {
        return activo && horaApertura != null && horaCierre != null && horaApertura.isBefore(horaCierre);
    }
}
