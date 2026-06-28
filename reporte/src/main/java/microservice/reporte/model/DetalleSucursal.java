package microservice.reporte.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DetalleSucursal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    @Column(name = "id_reporte", insertable = false, updatable = false)
    private Long idReporte;

    @NotNull(message = "El id de sucursal es obligatorio")
    private Long idSucursal;

    private LocalDate fechaRegistro;

    @PositiveOrZero(message = "Las ventas no pueden ser negativas")
    private double ventas;

    @PositiveOrZero(message = "El rendimiento no puede ser negativo")
    private double rendimiento;

    @ManyToOne
    @JoinColumn(name = "id_reporte")
    private ReporteSucursal reporteSucursal;

    public void agregarDetalle() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDate.now();
        }
    }

    public void setReporteSucursal(ReporteSucursal reporteSucursal) {
        this.reporteSucursal = reporteSucursal;
        this.idReporte = reporteSucursal != null ? reporteSucursal.getIdReporte() : null;
    }
}
