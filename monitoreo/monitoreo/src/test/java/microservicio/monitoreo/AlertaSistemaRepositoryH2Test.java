package microservicio.monitoreo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import microservicio.monitoreo.model.AlertaSistema;
import microservicio.monitoreo.repository.AlertaSistemaRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AlertaSistemaRepositoryH2Test {

	@Autowired
	private AlertaSistemaRepository repository;

	@Test
	void guardaBuscaPorRegistroYEliminaAlertaEnH2() {
		AlertaSistema alerta = AlertaSistema.builder()
				.idRegistro(10L)
				.tipoAlerta("MEMORIA")
				.nivelPrioridad("MEDIA")
				.mensaje("Uso de memoria elevado")
				.estado("ABIERTA")
				.build();

		AlertaSistema guardada = repository.save(alerta);

		assertThat(guardada.getIdAlerta()).isNotNull();
		assertThat(repository.findById(guardada.getIdAlerta())).isPresent();
		assertThat(repository.findByIdRegistro(10L)).containsExactly(guardada);

		repository.deleteById(guardada.getIdAlerta());

		assertThat(repository.findById(guardada.getIdAlerta())).isEmpty();
	}
}
