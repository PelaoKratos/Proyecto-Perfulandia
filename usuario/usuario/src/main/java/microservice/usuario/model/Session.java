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

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sesiones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSesion;

    @Column(length = 100, nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime fechaInicio;

    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(nullable = false)
    private boolean activa;

    @Column(nullable = false)
    private Long idUsuario;

    public String generarToken() {
        this.token = UUID.randomUUID().toString();
        this.fechaInicio = LocalDateTime.now();
        this.fechaExpiracion = fechaInicio.plusHours(2);
        this.activa = true;
        return token;
    }

    public void cerrarSesion() {
        this.activa = false;
    }

    public boolean validarSesion() {
        return activa && fechaExpiracion != null && fechaExpiracion.isAfter(LocalDateTime.now());
    }

    public Long getIdSession() {
        return idSesion;
    }

    public void setIdSession(Long idSession) {
        this.idSesion = idSession;
    }
}
