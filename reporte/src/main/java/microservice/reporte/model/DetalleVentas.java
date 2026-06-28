package microservice.reporte.model;

import java.time.LocalDateTime;

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
public class DetalleVentas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleVentas;

    @Column(name = "id_reporte", insertable = false, updatable = false)
    private Long idReporte;

    @NotNull(message = "El id de venta es obligatorio")
    private Long idVenta;

    private LocalDateTime fechaVenta;

    @PositiveOrZero(message = "El monto neto no puede ser negativo")
    private double montoNeto;

    @PositiveOrZero(message = "Los impuestos no pueden ser negativos")
    private double impuestos;

    @PositiveOrZero(message = "El total no puede ser negativo")
    private double total;

    @ManyToOne
    @JoinColumn(name = "id_reporte")
    private ReporteVentas reporteVentas;

    public void agregarDetalle() {
        calcularTotal();
    }

    public double calcularTotal() {
        total = montoNeto + impuestos;
        return total;
    }

    public void setReporteVentas(ReporteVentas reporteVentas) {
        this.reporteVentas = reporteVentas;
        this.idReporte = reporteVentas != null ? reporteVentas.getIdReporte() : null;
    }
}
