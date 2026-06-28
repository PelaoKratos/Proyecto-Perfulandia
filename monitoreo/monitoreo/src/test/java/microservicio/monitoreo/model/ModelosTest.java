package microservicio.monitoreo.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class ModelosTest {

	@Test
	void registroMonitoreoCubreMetodosDelModelo() {
		RegistroMonitoreo registro = new RegistroMonitoreo();

		registro.registrarEstado("ACTIVO", "Servicio operativo");
		assertThat(registro.verificarDisponibilidad()).isTrue();
		assertThat(registro.getMensaje()).isEqualTo("Servicio operativo");

		registro.setEstadoServicio("DISPONIBLE");
		assertThat(registro.verificarDisponibilidad()).isTrue();

		registro.setEstadoServicio("CAIDO");
		assertThat(registro.verificarDisponibilidad()).isFalse();

		registro.setTiempoRespuesta(500);
		assertThat(registro.consultarRendimiento()).isEqualTo("NORMAL");

		registro.setTiempoRespuesta(501);
		assertThat(registro.consultarRendimiento()).isEqualTo("LENTO");
	}

	@Test
	void registroMonitoreoAsignaFechaSoloSiNoExiste() {
		RegistroMonitoreo sinFecha = new RegistroMonitoreo();
		sinFecha.asignarFechaRegistro();
		assertThat(sinFecha.getFechaRegistro()).isNotNull();

		LocalDateTime fecha = LocalDateTime.of(2026, 6, 23, 10, 0);
		RegistroMonitoreo conFecha = RegistroMonitoreo.builder().fechaRegistro(fecha).build();
		conFecha.asignarFechaRegistro();
		assertThat(conFecha.getFechaRegistro()).isEqualTo(fecha);
	}

	@Test
	void alertaSistemaCubreMetodosDelModelo() {
		AlertaSistema alerta = new AlertaSistema();

		alerta.generarAlerta("CPU", "ALTA", "Uso elevado");

		assertThat(alerta.getTipoAlerta()).isEqualTo("CPU");
		assertThat(alerta.getNivelPrioridad()).isEqualTo("ALTA");
		assertThat(alerta.notificarAdministrador()).isEqualTo("Alerta ALTA: Uso elevado");
		assertThat(alerta.getEstado()).isEqualTo("ABIERTA");

		alerta.cerrarAlerta();
		assertThat(alerta.getEstado()).isEqualTo("CERRADA");
	}

	@Test
	void alertaSistemaAsignaFechaYEstadoSoloSiNoExisten() {
		AlertaSistema sinDatos = new AlertaSistema();
		sinDatos.asignarFechaAlerta();
		assertThat(sinDatos.getFechaAlerta()).isNotNull();
		assertThat(sinDatos.getEstado()).isEqualTo("ABIERTA");

		LocalDateTime fecha = LocalDateTime.of(2026, 6, 23, 11, 0);
		AlertaSistema conDatos = AlertaSistema.builder().fechaAlerta(fecha).estado("EN_REVISION").build();
		conDatos.asignarFechaAlerta();
		assertThat(conDatos.getFechaAlerta()).isEqualTo(fecha);
		assertThat(conDatos.getEstado()).isEqualTo("EN_REVISION");
	}

	@Test
	void respaldoCubreMetodosDelModelo() {
		Respaldo respaldo = new Respaldo();

		assertThat(respaldo.validarRespaldo()).isFalse();
		respaldo.crearRespaldo("/backups/servicio.sql", "COMPLETO");

		assertThat(respaldo.validarRespaldo()).isTrue();
		assertThat(respaldo.getTipoRespaldo()).isEqualTo("COMPLETO");
		assertThat(respaldo.getEstado()).isEqualTo("CREADO");

		respaldo.restaurarRespaldo();
		assertThat(respaldo.getEstado()).isEqualTo("RESTAURADO");

		respaldo.eliminarRespaldo();
		assertThat(respaldo.getEstado()).isEqualTo("ELIMINADO");
	}

	@Test
	void respaldoAsignaFechaYEstadoSoloSiNoExisten() {
		Respaldo sinDatos = new Respaldo();
		sinDatos.asignarFechaRespaldo();
		assertThat(sinDatos.getFechaRespaldo()).isNotNull();
		assertThat(sinDatos.getEstado()).isEqualTo("CREADO");

		LocalDateTime fecha = LocalDateTime.of(2026, 6, 23, 12, 0);
		Respaldo conDatos = Respaldo.builder().fechaRespaldo(fecha).estado("VALIDADO").build();
		conDatos.asignarFechaRespaldo();
		assertThat(conDatos.getFechaRespaldo()).isEqualTo(fecha);
		assertThat(conDatos.getEstado()).isEqualTo("VALIDADO");
	}
}
