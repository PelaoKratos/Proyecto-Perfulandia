package microservice.perfume.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    @Column(name = "id_categoria", insertable = false, updatable = false)
    private Long idCategoria;

    @NotBlank(message = "El nombre del producto no puede estar vacio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "La descripcion del producto no puede estar vacia")
    @Column(nullable = false)
    private String descripcion;

    @NotNull(message = "El precio del producto no puede ser nulo")
    @PositiveOrZero(message = "El precio del producto no puede ser negativo")
    @Column(nullable = false)
    private Double precio;

    @NotBlank(message = "El estado del producto no puede estar vacio")
    @Column(nullable = false)
    private String estado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria")
    private CategoriaProducto categoria;

    public void agregarProducto() {
        if (estado == null || estado.isBlank()) {
            estado = "ACTIVO";
        }
    }

    public void eliminarProducto() {
        estado = "INACTIVO";
    }

    public boolean consultarDisponibilidad() {
        return "ACTIVO".equalsIgnoreCase(estado);
    }

    public void setCategoria(CategoriaProducto categoria) {
        this.categoria = categoria;
        this.idCategoria = categoria != null ? categoria.getIdCategoria() : null;
    }
}
