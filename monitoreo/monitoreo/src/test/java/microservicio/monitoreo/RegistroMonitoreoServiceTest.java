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

import microservicio.monitoreo.model.RegistroMonitoreo;
import microservicio.monitoreo.repository.RegistroMonitoreoRepository;
import microservicio.monitoreo.service.RegistroMonitoreoService;

@ExtendWith(MockitoExtension.class)
class RegistroMonitoreoServiceTest {

	@Mock
	private RegistroMonitoreoRepository repository;

	@InjectMocks
	private RegistroMonitoreoService service;

	@Test
	void registrarGuardaRegistro() {
		RegistroMonitoreo registro = RegistroMonitoreo.builder()
				.nombreServicio("usuarios")
				.estadoServicio("DISPONIBLE")
				.tiempoRespuesta(120)
				.mensaje("OK")
				.build();
		when(repository.save(registro)).thenReturn(registro);

		RegistroMonitoreo resultado = service.registrar(registro);

		assertThat(resultado.getNombreServicio()).isEqualTo("usuarios");
		verify(repository).save(registro);
	}

	@Test
	void verificarDisponibilidadRetornaTrueSiServicioEstaDisponible() {
		RegistroMonitoreo registro = RegistroMonitoreo.builder()
				.idRegistro(1L)
				.estadoServicio("DISPONIBLE")
				.build();
		when(repository.findById(1L)).thenReturn(Optional.of(registro));

		assertThat(service.verificarDisponibilidad(1L)).isTrue();
	}

	@Test
	void listarRetornaRegistros() {
		RegistroMonitoreo registro = RegistroMonitoreo.builder().idRegistro(2L).build();
		when(repository.findAll()).thenReturn(List.of(registro));

		assertThat(service.listar()).containsExactly(registro);
	}

	@Test
	void obtenerPorIdRetornaRegistroExistente() {
		RegistroMonitoreo registro = RegistroMonitoreo.builder().idRegistro(3L).build();
		when(repository.findById(3L)).thenReturn(Optional.of(registro));

		assertThat(service.obtenerPorId(3L)).isSameAs(registro);
	}

	@Test
	void obtenerPorIdLanzaErrorSiNoExiste() {
		when(repository.findById(4L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.obtenerPorId(4L))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Registro no encontrado: 4");
	}

	@Test
	void consultarRendimientoRetornaResultadoDelModelo() {
		RegistroMonitoreo registro = RegistroMonitoreo.builder()
				.idRegistro(5L)
				.tiempoRespuesta(800)
				.build();
		when(repository.findById(5L)).thenReturn(Optional.of(registro));

		assertThat(service.consultarRendimiento(5L)).isEqualTo("LENTO");
	}

	@Test
	void eliminarBorraPorId() {
		service.eliminar(6L);

		verify(repository).deleteById(6L);
	}
}
