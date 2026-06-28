package microservice.sucursales1.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Sucursal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSucursal;

    @NotBlank(message = "El nombre no puede estar vacio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "La direccion no puede estar vacia")
    @Size(min = 5, max = 200, message = "La direccion debe tener entre 5 y 200 caracteres")
    @Column(nullable = false)
    private String direccion;

    @NotBlank(message = "El telefono no puede estar vacio")
    @Size(min = 8, max = 20, message = "El telefono debe tener entre 8 y 20 caracteres")
    @Column(nullable = false)
    private String telefono;

    @NotBlank(message = "La ciudad no puede estar vacia")
    @Size(min = 2, max = 100, message = "La ciudad debe tener entre 2 y 100 caracteres")
    @Column(nullable = false)
    private String ciudad;

    @NotBlank(message = "El estado no puede estar vacio")
    @Size(min = 3, max = 30, message = "El estado debe tener entre 3 y 30 caracteres")
    @Column(nullable = false)
    private String estado;

    @NotNull(message = "La fecha de creacion es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaCreacion;

    @OneToOne(mappedBy = "sucursal", cascade = CascadeType.ALL)
    private HorarioSucursal horario;

    @OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL)
    private List<AsignacionPersonal> asignaciones = new ArrayList<>();

    public void crearSucursal() {
        estado = "ACTIVA";
        fechaCreacion = LocalDate.now();
    }

    public void modificarSucursal(String nombre, String direccion, String telefono, String ciudad) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.ciudad = ciudad;
    }

    public void eliminarSucursal() {
        estado = "INACTIVA";
    }

    public boolean consultarSucursal() {
        return "ACTIVA".equalsIgnoreCase(estado);
    }
}
