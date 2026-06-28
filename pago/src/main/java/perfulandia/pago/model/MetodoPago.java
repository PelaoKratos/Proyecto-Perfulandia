package perfulandia.pago.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "metodos_pago")
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMetodoPago;

    @NotBlank(message = "El tipo es obligatorio")
    @Column(nullable = false, length = 50)
    private String tipo;

    @NotBlank(message = "La descripcion es obligatoria")
    @Column(nullable = false, length = 150)
    private String descripcion;

    @Column(nullable = false)
    private boolean activo = true;

    public boolean validarMetodo() {
        return activo
                && tipo != null
                && !tipo.isBlank()
                && descripcion != null
                && !descripcion.isBlank();
    }

    public void activar() {
        activo = true;
    }

    public void desactivar() {
        activo = false;
    }
}
