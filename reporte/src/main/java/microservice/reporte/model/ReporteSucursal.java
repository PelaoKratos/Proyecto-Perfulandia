package microservice.reporte.model;

import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "reportes_sucursal")
public class ReporteSucursal extends Reportes {
    private Long idSucursal;

    @Column(nullable = false)
    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    private String nombreSucursal;

    @PositiveOrZero(message = "Las ventas de la sucursal no pueden ser negativas")
    private double ventasSucursal;

    private double rendimiento;

    @OneToMany(mappedBy = "reporteSucursal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleSucursal> detalles = new ArrayList<>();

    public void generarReporteSucursal() {
        setTipo("Sucursal");
        generar();
    }

    public double compararMetricas(double valorReferencia) {
        return rendimiento - valorReferencia;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = (long) idSucursal;
    }
}
