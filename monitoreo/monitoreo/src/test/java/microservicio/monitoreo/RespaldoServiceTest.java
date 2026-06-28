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

import microservicio.monitoreo.model.Respaldo;
import microservicio.monitoreo.repository.RespaldoRepository;
import microservicio.monitoreo.service.RespaldoService;

@ExtendWith(MockitoExtension.class)
class RespaldoServiceTest {

	@Mock
	private RespaldoRepository repository;

	@InjectMocks
	private RespaldoService service;

	@Test
	void restaurarCambiaEstadoYGuarda() {
		Respaldo respaldo = Respaldo.builder()
				.idRespaldo(2L)
				.rutaArchivo("/backups/usuarios.sql")
				.estado("CREADO")
				.build();
		when(repository.findById(2L)).thenReturn(Optional.of(respaldo));
		when(repository.save(respaldo)).thenReturn(respaldo);

		Respaldo resultado = service.restaurar(2L);

		assertThat(resultado.getEstado()).isEqualTo("RESTAURADO");
		verify(repository).save(respaldo);
	}

	@Test
	void crearGuardaRespaldoValido() {
		Respaldo respaldo = Respaldo.builder().rutaArchivo("/backups/app.sql").build();
		when(repository.save(respaldo)).thenReturn(respaldo);

		assertThat(service.crear(respaldo)).isSameAs(respaldo);
		verify(repository).save(respaldo);
	}

	@Test
	void crearLanzaErrorSiRutaEsInvalida() {
		Respaldo respaldo = Respaldo.builder().rutaArchivo(" ").build();

		assertThatThrownBy(() -> service.crear(respaldo))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("La ruta del archivo de respaldo es obligatoria");
	}

	@Test
	void listarRetornaRespaldos() {
		Respaldo respaldo = Respaldo.builder().idRespaldo(3L).build();
		when(repository.findAll()).thenReturn(List.of(respaldo));

		assertThat(service.listar()).containsExactly(respaldo);
	}

	@Test
	void listarPorServicioFiltraPorNombreServicio() {
		Respaldo respaldo = Respaldo.builder().nombreServicio("usuarios").build();
		when(repository.findByNombreServicio("usuarios")).thenReturn(List.of(respaldo));

		assertThat(service.listarPorServicio("usuarios")).containsExactly(respaldo);
	}

	@Test
	void obtenerPorIdRetornaRespaldoExistente() {
		Respaldo respaldo = Respaldo.builder().idRespaldo(4L).build();
		when(repository.findById(4L)).thenReturn(Optional.of(respaldo));

		assertThat(service.obtenerPorId(4L)).isSameAs(respaldo);
	}

	@Test
	void obtenerPorIdLanzaErrorSiNoExiste() {
		when(repository.findById(5L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.obtenerPorId(5L))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Respaldo no encontrado: 5");
	}

	@Test
	void eliminarMarcaComoEliminadoYGuarda() {
		Respaldo respaldo = Respaldo.builder()
				.idRespaldo(6L)
				.rutaArchivo("/backups/app.sql")
				.estado("CREADO")
				.build();
		when(repository.findById(6L)).thenReturn(Optional.of(respaldo));
		when(repository.save(respaldo)).thenReturn(respaldo);

		service.eliminar(6L);

		assertThat(respaldo.getEstado()).isEqualTo("ELIMINADO");
		verify(repository).save(respaldo);
	}
}
