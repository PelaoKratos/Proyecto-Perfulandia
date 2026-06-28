package microservicio.monitoreo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import microservicio.monitoreo.model.AlertaSistema;
import microservicio.monitoreo.repository.AlertaSistemaRepository;
import microservicio.monitoreo.service.AlertaSistemaService;

@ExtendWith(MockitoExtension.class)
class AlertaSistemaServiceTest {

	@Mock
	private AlertaSistemaRepository repository;

	@InjectMocks
	private AlertaSistemaService service;

	@Test
	void generarAsignaEstadoCuandoNoExiste() {
		AlertaSistema alerta = AlertaSistema.builder().mensaje("CPU alta").build();
		when(repository.save(alerta)).thenReturn(alerta);

		AlertaSistema resultado = service.generar(alerta);

		assertThat(resultado.getEstado()).isEqualTo("ABIERTA");
		verify(repository).save(alerta);
	}

	@Test
	void generarConservaEstadoExistente() {
		AlertaSistema alerta = AlertaSistema.builder().estado("EN_REVISION").build();
		when(repository.save(alerta)).thenReturn(alerta);

		assertThat(service.generar(alerta).getEstado()).isEqualTo("EN_REVISION");
	}

	@Test
	void listarRetornaTodasLasAlertas() {
		AlertaSistema alerta = AlertaSistema.builder().idAlerta(1L).build();
		when(repository.findAll()).thenReturn(List.of(alerta));

		assertThat(service.listar()).containsExactly(alerta);
	}

	@Test
	void listarPorRegistroFiltraPorIdRegistro() {
		AlertaSistema alerta = AlertaSistema.builder().idRegistro(7L).build();
		when(repository.findByIdRegistro(7L)).thenReturn(List.of(alerta));

		assertThat(service.listarPorRegistro(7L)).containsExactly(alerta);
	}

	@Test
	void obtenerPorIdRetornaAlertaExistente() {
		AlertaSistema alerta = AlertaSistema.builder().idAlerta(2L).build();
		when(repository.findById(2L)).thenReturn(Optional.of(alerta));

		assertThat(service.obtenerPorId(2L)).isSameAs(alerta);
	}

	@Test
	void obtenerPorIdLanzaErrorSiNoExiste() {
		when(repository.findById(3L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.obtenerPorId(3L))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Alerta no encontrada: 3");
	}

	@Test
	void cerrarCambiaEstadoYGuarda() {
		AlertaSistema alerta = AlertaSistema.builder().idAlerta(4L).estado("ABIERTA").build();
		when(repository.findById(4L)).thenReturn(Optional.of(alerta));
		when(repository.save(alerta)).thenReturn(alerta);

		assertThat(service.cerrar(4L).getEstado()).isEqualTo("CERRADA");
		verify(repository).save(alerta);
	}

	@Test
	void eliminarBorraPorId() {
		service.eliminar(5L);

		verify(repository).deleteById(5L);
	}
}
