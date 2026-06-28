package microservice.perfume.Model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ResenaProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResena;

    @Column(name = "id_producto", insertable = false, updatable = false)
    private Long idProducto;

    @NotNull(message = "El id de cliente es obligatorio")
    private Long idCliente;

    @NotNull(message = "El id de pedido es obligatorio")
    private Long idPedido;

    @NotNull(message = "La calificacion es obligatoria")
    @Min(value = 1, message = "La calificacion minima es 1")
    @Max(value = 5, message = "La calificacion maxima es 5")
    private Integer calificacion;

    @NotBlank(message = "El comentario no puede estar vacio")
    @Column(nullable = false)
    private String comentario;

    @Column(nullable = false)
    private LocalDateTime fechaResena;

    @NotBlank(message = "El estado de la resena no puede estar vacio")
    @Column(nullable = false)
    private String estado;

    @NotNull(message = "El producto es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producto", nullable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Producto producto;

    public void crearResena() {
        if (fechaResena == null) {
            fechaResena = LocalDateTime.now();
        }
        if (estado == null || estado.isBlank()) {
            estado = "ACTIVA";
        }
    }

    public void modificarResena(String comentario, Integer calificacion) {
        this.comentario = comentario;
        this.calificacion = calificacion;
    }

    public void eliminarResena() {
        estado = "INACTIVA";
    }

    public boolean validarCompra() {
        return idCliente != null && idPedido != null && producto != null;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        this.idProducto = producto != null ? producto.getIdProducto() : null;
    }
}
