package microservicio.monitoreo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import microservicio.monitoreo.model.RegistroMonitoreo;
import microservicio.monitoreo.repository.RegistroMonitoreoRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RegistroMonitoreoRepositoryH2Test {

	@Autowired
	private RegistroMonitoreoRepository repository;

	@Test
	void guardaRegistroEnH2() {
		RegistroMonitoreo registro = RegistroMonitoreo.builder()
				.nombreServicio("pagos")
				.estadoServicio("DISPONIBLE")
				.tiempoRespuesta(180)
				.mensaje("Servicio operativo")
				.build();

		RegistroMonitoreo guardado = repository.save(registro);

		assertThat(guardado.getIdRegistro()).isNotNull();
		assertThat(repository.findById(guardado.getIdRegistro())).isPresent();
		assertThat(repository.findAll()).contains(guardado);

		repository.deleteById(guardado.getIdRegistro());

		assertThat(repository.findById(guardado.getIdRegistro())).isEmpty();
	}
}
