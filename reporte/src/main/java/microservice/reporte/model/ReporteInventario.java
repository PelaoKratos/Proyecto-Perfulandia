package microservice.reporte.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "reportes_inventario")
public class ReporteInventario extends Reportes {
    @PositiveOrZero(message = "El total de productos no puede ser negativo")
    private int totalProductos;

    @PositiveOrZero(message = "El stock bajo no puede ser negativo")
    private int productosStockBajo;

    @PositiveOrZero(message = "Los movimientos no pueden ser negativos")
    private int totalMovimientos;

    private LocalDate fechaCorte;

    @OneToMany(mappedBy = "reporteInventario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleInventario> detalles = new ArrayList<>();

    public void generarReporteInventario() {
        setTipo("Inventario");
        generar();
    }

    public int calcularStock() {
        return totalProductos - productosStockBajo;
    }

    public int identificarStockBajo() {
        return productosStockBajo;
    }

    public int getStockBajo() {
        return productosStockBajo;
    }

    public void setStockBajo(int stockBajo) {
        this.productosStockBajo = stockBajo;
    }

    public int getMovimientos() {
        return totalMovimientos;
    }

    public void setMovimientos(int movimientos) {
        this.totalMovimientos = movimientos;
    }
}
