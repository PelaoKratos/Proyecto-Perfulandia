package microservice.soporte.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TicketSoporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTicket;

    @NotNull(message = "El id del cliente es obligatorio")
    private Long idCliente;

    @Column(name = "id_categoria", insertable = false, updatable = false)
    private Long idCategoria;

    private Long idUsuarioAsignado;

    @NotBlank(message = "El asunto es obligatorio")
    @Column(nullable = false)
    private String asunto;

    @NotBlank(message = "La descripcion es obligatoria")
    @Column(nullable = false)
    private String descripcion;

    @NotBlank(message = "El canal es obligatorio")
    @Column(nullable = false)
    private String canal;

    @NotBlank(message = "La prioridad es obligatoria")
    @Column(nullable = false)
    private String prioridad;

    @NotBlank(message = "El estado es obligatorio")
    @Column(nullable = false)
    private String estado;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaCierre;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private CategoriaSoporte categoria;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("ticket")
    private List<RespuestaTicket> respuestas = new ArrayList<>();

    public void crearTicket() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (estado == null || estado.isBlank()) {
            estado = "ABIERTO";
        }
    }

    public void cerrarTicket() {
        estado = "CERRADO";
        fechaCierre = LocalDateTime.now();
    }

    public void cambiarEstado(String estado) {
        this.estado = estado;
        if ("CERRADO".equalsIgnoreCase(estado) && fechaCierre == null) {
            fechaCierre = LocalDateTime.now();
        }
    }

    public void asignarPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public void setCategoria(CategoriaSoporte categoria) {
        this.categoria = categoria;
        this.idCategoria = categoria != null ? categoria.getIdCategoria() : null;
    }

    public void agregarRespuesta(RespuestaTicket respuesta) {
        if (respuesta != null) {
            respuesta.setTicket(this);
            respuestas.add(respuesta);
        }
    }
}
