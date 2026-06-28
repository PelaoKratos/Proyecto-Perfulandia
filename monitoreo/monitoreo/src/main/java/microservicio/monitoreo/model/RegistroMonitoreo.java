package microservicio.monitoreo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroMonitoreo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idRegistro;

	private String nombreServicio;
	private String estadoServicio;
	private double tiempoRespuesta;
	private LocalDateTime fechaRegistro;
	private String mensaje;

	@PrePersist
	void asignarFechaRegistro() {
		if (fechaRegistro == null) {
			fechaRegistro = LocalDateTime.now();
		}
	}

	public void registrarEstado(String estadoServicio, String mensaje) {
		this.estadoServicio = estadoServicio;
		this.mensaje = mensaje;
	}

	public boolean verificarDisponibilidad() {
		return "DISPONIBLE".equalsIgnoreCase(estadoServicio) || "ACTIVO".equalsIgnoreCase(estadoServicio);
	}

	public String consultarRendimiento() {
		return tiempoRespuesta <= 500 ? "NORMAL" : "LENTO";
	}
}
