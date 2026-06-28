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
public class AlertaSistema {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idAlerta;

	private Long idRegistro;
	private String tipoAlerta;
	private String nivelPrioridad;
	private String mensaje;
	private LocalDateTime fechaAlerta;
	private String estado;

	@PrePersist
	void asignarFechaAlerta() {
		if (fechaAlerta == null) {
			fechaAlerta = LocalDateTime.now();
		}
		if (estado == null) {
			estado = "ABIERTA";
		}
	}

	public void generarAlerta(String tipoAlerta, String nivelPrioridad, String mensaje) {
		this.tipoAlerta = tipoAlerta;
		this.nivelPrioridad = nivelPrioridad;
		this.mensaje = mensaje;
		this.estado = "ABIERTA";
	}

	public String notificarAdministrador() {
		return "Alerta " + nivelPrioridad + ": " + mensaje;
	}

	public void cerrarAlerta() {
		this.estado = "CERRADA";
	}
}
