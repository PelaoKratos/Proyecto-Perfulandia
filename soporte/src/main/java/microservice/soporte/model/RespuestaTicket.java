package microservice.soporte.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RespuestaTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRespuesta;

    @Column(name = "id_ticket", insertable = false, updatable = false)
    private Long idTicket;

    @NotNull(message = "El id del usuario es obligatorio")
    private Long idUsuario;

    @NotBlank(message = "El mensaje es obligatorio")
    @Column(nullable = false)
    private String mensaje;

    private LocalDateTime fechaRespuesta;

    @NotBlank(message = "El tipo de respuesta es obligatorio")
    @Column(nullable = false)
    private String tipoRespuesta;

    @ManyToOne
    @JoinColumn(name = "id_ticket")
    private TicketSoporte ticket;

    public void registrarRespuesta() {
        if (fechaRespuesta == null) {
            fechaRespuesta = LocalDateTime.now();
        }
    }

    public void modificarRespuesta(String mensaje, String tipoRespuesta) {
        this.mensaje = mensaje;
        this.tipoRespuesta = tipoRespuesta;
    }

    public void eliminarRespuesta() {
        tipoRespuesta = "ELIMINADA";
    }

    public void setTicket(TicketSoporte ticket) {
        this.ticket = ticket;
        this.idTicket = ticket != null ? ticket.getIdTicket() : null;
    }
}
