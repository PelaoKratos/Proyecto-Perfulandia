package microservice.sucursales1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmpleado;

    @NotBlank(message = "El nombre no puede estar vacio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El rut no puede estar vacio")
    @Column(nullable = false, unique = true)
    private String rut;

    @Email(message = "El email debe tener formato valido")
    @NotBlank(message = "El email no puede estar vacio")
    @Column(nullable = false)
    private String email;

    @Size(min = 8, max = 20, message = "El telefono debe tener entre 8 y 20 caracteres")
    private String telefono;

    @NotBlank(message = "El estado no puede estar vacio")
    @Column(nullable = false)
    private String estado;

    public String consultarDatos() {
        return nombre + " - " + rut;
    }

    public void activar() {
        estado = "ACTIVO";
    }

    public void desactivar() {
        estado = "INACTIVO";
    }
}
