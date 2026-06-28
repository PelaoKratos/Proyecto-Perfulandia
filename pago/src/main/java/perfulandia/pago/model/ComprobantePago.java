package perfulandia.pago.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comprobantes_pago")
public class ComprobantePago {

    public static final String ESTADO_GENERADO = "GENERADO";
    public static final String ESTADO_ENVIADO = "ENVIADO";
    public static final String ESTADO_ANULADO = "ANULADO";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComprobante;

    @NotNull(message = "El pago es obligatorio")
    @Column(nullable = false)
    private Long idPago;

    @NotBlank(message = "El numero es obligatorio")
    @Column(nullable = false, length = 100)
    private String numero;

    @Column(nullable = false)
    private LocalDateTime fechaEmision;

    @NotNull(message = "El total es obligatorio")
    @PositiveOrZero(message = "El total no puede ser negativo")
    @Column(nullable = false)
    private Double total;

    @NotBlank(message = "El estado es obligatorio")
    @Column(nullable = false, length = 30)
    private String estado;

    @PrePersist
    void prePersist() {
        if (fechaEmision == null) {
            fechaEmision = LocalDateTime.now();
        }
        if (estado == null || estado.isBlank()) {
            estado = ESTADO_GENERADO;
        }
    }

    public void generarComprobante() {
        if (fechaEmision == null) {
            fechaEmision = LocalDateTime.now();
        }
        estado = ESTADO_GENERADO;
    }

    public void enviarComprobante() {
        estado = ESTADO_ENVIADO;
    }

    public void anularComprobante() {
        estado = ESTADO_ANULADO;
    }
}
