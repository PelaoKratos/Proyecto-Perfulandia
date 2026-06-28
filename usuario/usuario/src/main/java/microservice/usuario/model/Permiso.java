package microservice.usuario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "permisos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permiso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPermiso;

    @Column(length = 80, nullable = false)
    private String modulo;

    @Column(length = 80, nullable = false)
    private String accion;

    public boolean validarAcceso(String modulo, String accion) {
        return this.modulo != null
                && this.accion != null
                && this.modulo.equalsIgnoreCase(modulo)
                && this.accion.equalsIgnoreCase(accion);
    }

    public void crearPermiso(String modulo, String accion) {
        this.modulo = modulo;
        this.accion = accion;
    }

    public void modificarPermiso(String modulo, String accion) {
        this.modulo = modulo;
        this.accion = accion;
    }

    public void eliminarPermiso() {
        this.modulo = null;
        this.accion = null;
    }
}
