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
public class Respaldo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idRespaldo;

	private Long idUsuario;
	private String nombreServicio;
	private LocalDateTime fechaRespaldo;
	private String rutaArchivo;
	private String tipoRespaldo;
	private String estado;

	@PrePersist
	void asignarFechaRespaldo() {
		if (fechaRespaldo == null) {
			fechaRespaldo = LocalDateTime.now();
		}
		if (estado == null) {
			estado = "CREADO";
		}
	}

	public void crearRespaldo(String rutaArchivo, String tipoRespaldo) {
		this.rutaArchivo = rutaArchivo;
		this.tipoRespaldo = tipoRespaldo;
		this.estado = "CREADO";
	}

	public boolean validarRespaldo() {
		return rutaArchivo != null && !rutaArchivo.isBlank();
	}

	public void restaurarRespaldo() {
		this.estado = "RESTAURADO";
	}

	public void eliminarRespaldo() {
		this.estado = "ELIMINADO";
	}
}
