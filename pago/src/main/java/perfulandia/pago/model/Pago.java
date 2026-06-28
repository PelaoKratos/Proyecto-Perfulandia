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
@Table(name = "pagos")
public class Pago {

    public static final String ESTADO_PENDIENTE = "PENDIENTE";
    public static final String ESTADO_CONFIRMADO = "CONFIRMADO";
    public static final String ESTADO_RECHAZADO = "RECHAZADO";
    public static final String ESTADO_ANULADO = "ANULADO";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;

    @NotNull(message = "La venta es obligatoria")
    @Column(nullable = false)
    private Long idVenta;

    @NotNull(message = "El cliente es obligatorio")
    @Column(nullable = false)
    private Long idCliente;

    @NotNull(message = "El empleado es obligatorio")
    @Column(nullable = false)
    private Long idEmpleado;

    @NotNull(message = "El metodo de pago es obligatorio")
    @Column(nullable = false)
    private Long idMetodoPago;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    @Column(nullable = false)
    private Double monto;

    @Column(length = 50)
    private String metodoPago;

    @NotBlank(message = "El estado es obligatorio")
    @Column(nullable = false, length = 30)
    private String estado;

    @Column(nullable = false)
    private LocalDateTime fechaPago;

    @Column(length = 100)
    private String codigoTransaccion;

    @Column(length = 200)
    private String mensaje;

    @PrePersist
    void prePersist() {
        if (fechaPago == null) {
            fechaPago = LocalDateTime.now();
        }
        if (estado == null || estado.isBlank()) {
            estado = ESTADO_PENDIENTE;
        }
    }

    public boolean validarPago() {
        return idVenta != null
                && idCliente != null
                && idEmpleado != null
                && idMetodoPago != null
                && monto != null
                && monto > 0;
    }

    public void realizarPago() {
        procesar();
    }

    public void procesar() {
        if (!validarPago()) {
            rechazar("Datos de pago invalidos");
            return;
        }
        estado = ESTADO_PENDIENTE;
        mensaje = "Pago en proceso";
    }

    public void confirmar(String codigoTransaccion) {
        estado = ESTADO_CONFIRMADO;
        this.codigoTransaccion = codigoTransaccion;
        mensaje = "Pago confirmado";
    }

    public void confirmarPago() {
        confirmar(codigoTransaccion);
    }

    public void rechazar(String motivo) {
        estado = ESTADO_RECHAZADO;
        mensaje = motivo;
    }

    public void rechazarPago() {
        rechazar(mensaje);
    }

    public void anular(String motivo) {
        estado = ESTADO_ANULADO;
        mensaje = motivo;
    }
}
