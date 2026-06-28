package microservice.reporte.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reportes")
@Inheritance(strategy = InheritanceType.JOINED)
public class Reportes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReporte;

    @Column(nullable = false)
    @NotBlank(message = "El titulo es obligatorio")
    private String titulo;

    @Column(nullable = false)
    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;

    private LocalDateTime fechaGeneracion;

    private LocalDate periodoInicio;

    private LocalDate periodoFin;

    @Column(nullable = false)
    @NotBlank(message = "El formato es obligatorio")
    private String formato;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    @NotBlank(message = "La fecha es obligatoria")
    private String fechaReporte;

    @Column(nullable = false)
    @NotBlank(message = "La razon es obligatoria")
    private String razonReporte;

    @Column(nullable = false)
    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcionReporte;

    @Column(nullable = false)
    @NotBlank(message = "El estado es obligatorio")
    private String estadoReporte;

    public void generar() {
        generarReporte();
    }

    public void exportar() {
        actualizarEstado("Exportado");
    }

    public String visualizar() {
        return consultarReporte();
    }

    public void generarReporte() {
        if (fechaGeneracion == null) {
            fechaGeneracion = LocalDateTime.now();
        }
        actualizarEstado("Generado");
    }

    public String consultarReporte() {
        return titulo + " - " + tipo + " - " + estado;
    }

    public void actualizarEstado(String estado) {
        this.estado = estado;
        this.estadoReporte = estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
        if (estadoReporte == null || estadoReporte.isBlank()) {
            estadoReporte = estado;
        }
    }

    public void setEstadoReporte(String estadoReporte) {
        this.estadoReporte = estadoReporte;
        if (estado == null || estado.isBlank()) {
            estado = estadoReporte;
        }
    }

    @PrePersist
    public void completarDatosDelDiagrama() {
        if (titulo == null || titulo.isBlank()) {
            titulo = razonReporte;
        }
        if (tipo == null || tipo.isBlank()) {
            tipo = "General";
        }
        if (formato == null || formato.isBlank()) {
            formato = "JSON";
        }
        if (estado == null || estado.isBlank()) {
            estado = estadoReporte;
        }
        if (estadoReporte == null || estadoReporte.isBlank()) {
            estadoReporte = estado;
        }
        if (fechaReporte == null && fechaGeneracion != null) {
            fechaReporte = fechaGeneracion.toLocalDate().toString();
        }
        if (fechaGeneracion == null) {
            fechaGeneracion = LocalDateTime.now();
        }
    }
}
