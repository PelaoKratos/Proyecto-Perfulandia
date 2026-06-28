package microservice.reporte.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exportaciones_reportes")
public class ExportacionReporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idExportacion;

    @Column(name = "id_reporte", insertable = false, updatable = false)
    private Long idReporte;

    @Column(nullable = false)
    @NotBlank(message = "El formato es obligatorio")
    private String formato;

    private String rutaArchivo;
    private LocalDateTime fechaExportacion;

    @Column(nullable = false)
    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_reporte")
    private Reportes reporte;

    public void exportarExcel() {
        this.formato = "EXCEL";
        this.estado = "Exportado";
        this.fechaExportacion = LocalDateTime.now();
    }

    public void exportarPDF() {
        this.formato = "PDF";
        this.estado = "Exportado";
        this.fechaExportacion = LocalDateTime.now();
    }

    public String descargar() {
        return rutaArchivo;
    }

    public String descargarArchivo() {
        return rutaArchivo;
    }

    public void setReporte(Reportes reporte) {
        this.reporte = reporte;
        this.idReporte = reporte != null ? reporte.getIdReporte() : null;
    }
}
