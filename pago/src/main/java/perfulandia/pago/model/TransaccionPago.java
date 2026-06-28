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
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transacciones_pago")
public class TransaccionPago {

    public static final String ESTADO_REGISTRADA = "REGISTRADA";
    public static final String ESTADO_ANULADA = "ANULADA";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTransaccion;

    @NotNull(message = "El pago es obligatorio")
    @Column(nullable = false)
    private Long idPago;

    @NotBlank(message = "El codigo de operacion es obligatorio")
    @Column(nullable = false, length = 100)
    private String codigoOperacion;

    @Column(nullable = false)
    private LocalDateTime fechaTransaccion;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    @Column(nullable = false)
    private Double monto;

    @NotBlank(message = "El estado es obligatorio")
    @Column(nullable = false, length = 30)
    private String estado;

    @PrePersist
    void prePersist() {
        if (fechaTransaccion == null) {
            fechaTransaccion = LocalDateTime.now();
        }
        if (estado == null || estado.isBlank()) {
            estado = ESTADO_REGISTRADA;
        }
    }

    public void registrarTransaccion() {
        if (fechaTransaccion == null) {
            fechaTransaccion = LocalDateTime.now();
        }
        estado = ESTADO_REGISTRADA;
    }

    public String consultarEstado() {
        return estado;
    }

    public void anularTransaccion() {
        estado = ESTADO_ANULADA;
    }
}
