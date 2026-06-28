package microservice.perfume.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CategoriaProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategoria;

    @NotBlank(message = "El nombre de la categoria no puede estar vacio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "La descripcion de la categoria no puede estar vacia")
    @Column(nullable = false)
    private String descripcion;

    @NotBlank(message = "El estado de la categoria no puede estar vacio")
    @Column(nullable = false)
    private String estado;

    public void crearCategoria() {
        if (estado == null || estado.isBlank()) {
            estado = "ACTIVA";
        }
    }

    public void modificarCategoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public void activarCategoria() {
        estado = "ACTIVA";
    }

    public void desactivarCategoria() {
        estado = "INACTIVA";
    }
}
