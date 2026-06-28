package microservice.reporte.model;

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
public class DetalleInventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleInventario;

    @Column(name = "id_reporte", insertable = false, updatable = false)
    private Long idReporte;

    @NotNull(message = "El id de producto es obligatorio")
    private Long idProducto;

    @NotNull(message = "El id de sucursal es obligatorio")
    private Long idSucursal;

    @PositiveOrZero(message = "El stock actual no puede ser negativo")
    private int stockActual;

    @PositiveOrZero(message = "El stock minimo no puede ser negativo")
    private int stockMinimo;

    @PositiveOrZero(message = "La cantidad de movimientos no puede ser negativa")
    private int cantidadMovimientos;

    @ManyToOne
    @JoinColumn(name = "id_reporte")
    private ReporteInventario reporteInventario;

    public void agregarDetalle() {
        verificarStockBajo();
    }

    public boolean verificarStockBajo() {
        return stockActual <= stockMinimo;
    }

    public void setReporteInventario(ReporteInventario reporteInventario) {
        this.reporteInventario = reporteInventario;
        this.idReporte = reporteInventario != null ? reporteInventario.getIdReporte() : null;
    }
}
