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
@Table(name = "reportes_ventas")
public class ReporteVentas extends Reportes {
    @PositiveOrZero(message = "El total de ventas no puede ser negativo")
    private double totalVentas;

    @PositiveOrZero(message = "La cantidad de ventas no puede ser negativa")
    private int cantidadVentas;

    @Column(nullable = false)
    @NotBlank(message = "El periodo es obligatorio")
    private String periodo;

    private Long idVenta;

    @OneToMany(mappedBy = "reporteVentas", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVentas> detalles = new ArrayList<>();

    public void generarReporteVentas() {
        setTipo("Ventas");
        generar();
    }

    public double calcularTotales() {
        if (detalles != null && !detalles.isEmpty()) {
            return detalles.stream().mapToDouble(DetalleVentas::calcularTotal).sum();
        }
        return totalVentas;
    }

    public int getIdSucursal() {
        return idVenta != null ? idVenta.intValue() : 0;
    }

    public void setIdSucursal(int idSucursal) {
        this.idVenta = (long) idSucursal;
    }
}
