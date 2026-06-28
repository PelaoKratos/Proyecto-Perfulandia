
package microservice.usuario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(length = 50, nullable = false)
    private String nombreUsuario;

    @Column(length = 50, nullable = false)
    private String apellidoUsuario;

    @Column(length = 250, nullable = true, unique = true)
    private String correoUsuario;

    @Column(length = 250, nullable = true, unique = true)
    private String contraseñaUsuario;

    @Column(name = "id_rol", insertable = false, updatable = false)
    private Long idRol;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol")
    private Role rolUsuario;

    @Column(nullable = false)
    private String estado = "ACTIVO";

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    public Usuario(Long idUsuario, String nombreUsuario, String apellidoUsuario, String correoUsuario,
            String contraseñaUsuario, Role rolUsuario) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.correoUsuario = correoUsuario;
        this.contraseñaUsuario = contraseñaUsuario;
        setRolUsuario(rolUsuario);
    }

    @PrePersist
    void prePersist() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }

    public boolean iniciarSesion(String correo, String contraseña) {
        return isEstado()
                && correoUsuario != null
                && contraseñaUsuario != null
                && correoUsuario.equalsIgnoreCase(correo)
                && contraseñaUsuario.equals(contraseña);
    }

    public void actualizarDatos(String nombre, String apellido, String correo) {
        this.nombreUsuario = nombre;
        this.apellidoUsuario = apellido;
        this.correoUsuario = correo;
    }

    public void desactivar() {
        this.estado = "INACTIVO";
    }

    public void activar() {
        this.estado = "ACTIVO";
    }

    public void recuperarContrasena(String nuevaContrasena) {
        this.contraseñaUsuario = nuevaContrasena;
    }

    public boolean isEstado() {
        return "ACTIVO".equalsIgnoreCase(estado);
    }

    public void setEstado(boolean activo) {
        this.estado = activo ? "ACTIVO" : "INACTIVO";
    }

    public void setRolUsuario(Role rolUsuario) {
        this.rolUsuario = rolUsuario;
        this.idRol = rolUsuario != null ? rolUsuario.getIdRol() : null;
    }

    public String getNombre() {
        return nombreUsuario;
    }

    public void setNombre(String nombre) {
        this.nombreUsuario = nombre;
    }

    public String getEmail() {
        return correoUsuario;
    }

    public void setEmail(String email) {
        this.correoUsuario = email;
    }

    public String getContrasena() {
        return contraseñaUsuario;
    }

    public void setContrasena(String contrasena) {
        this.contraseñaUsuario = contrasena;
    }
}
