package microservice.ventas.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVenta;

    @Column(nullable = false)
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fechaVenta;

    @Column(nullable = false)
    @NotNull(message = "El total es obligatorio")
    @PositiveOrZero(message = "El total no puede ser negativo")
    private Double totalVenta;

    @Column(nullable = true)
    @PositiveOrZero(message = "El descuento no puede ser negativo")
    private Double descuentoVenta;

    @Column(nullable = false)
    @NotBlank(message = "El estado es obligatorio")
    private String estadoVenta;

    @Column(nullable = true)
    private Long idPerfume;

    @Column(nullable = false)
    @NotNull(message = "La sucursal es obligatoria")
    private Long idSucursal;

    @Column(nullable = true)
    @Positive(message = "La cantidad debe ser mayor a cero")
    private Long cantidad;

    @Valid
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VentaDetalle> detalles = new ArrayList<>();

    @JsonIgnore
    @AssertTrue(message = "Debe ingresar al menos un perfume en el detalle de la venta")
    public boolean isDetalleVentaValido() {
        boolean tieneDetalleMultiple = detalles != null && !detalles.isEmpty();
        boolean tieneDetalleSimple = idPerfume != null && cantidad != null;
        return tieneDetalleMultiple || tieneDetalleSimple;
    }
}
