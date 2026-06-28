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
@Table(name = "metricas")
public class Metrica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMetrica;

    @Column(name = "id_reporte", insertable = false, updatable = false)
    private Long idReporte;

    @Column(nullable = false)
    @NotBlank(message = "El nombre de la metrica es obligatorio")
    private String nombre;

    private double valor;

    @Column(nullable = false)
    @NotBlank(message = "La unidad es obligatoria")
    private String unidad;

    private LocalDateTime fechaRegistro;

    @ManyToOne
    @JoinColumn(name = "id_reporte")
    private Reportes reporte;

    public double calcular() {
        return valor;
    }

    public double calcularMetrica() {
        return calcular();
    }

    public void actualizarMetrica(double valor, String unidad) {
        this.valor = valor;
        this.unidad = unidad;
        this.fechaRegistro = LocalDateTime.now();
    }

    public String visualizar() {
        return nombre + ": " + valor + " " + unidad;
    }

    public void setReporte(Reportes reporte) {
        this.reporte = reporte;
        this.idReporte = reporte != null ? reporte.getIdReporte() : null;
    }
}
