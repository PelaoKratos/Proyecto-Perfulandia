package microservicio.monitoreo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import microservicio.monitoreo.model.Respaldo;
import microservicio.monitoreo.repository.RespaldoRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RespaldoRepositoryH2Test {

	@Autowired
	private RespaldoRepository repository;

	@Test
	void buscaRespaldosPorServicioEnH2() {
		Respaldo guardado = repository.save(Respaldo.builder()
				.idUsuario(7L)
				.nombreServicio("inventario")
				.rutaArchivo("/backups/inventario.sql")
				.tipoRespaldo("COMPLETO")
				.estado("CREADO")
				.build());

		assertThat(repository.findByNombreServicio("inventario")).hasSize(1);
		assertThat(repository.findById(guardado.getIdRespaldo())).isPresent();
		assertThat(repository.findAll()).contains(guardado);

		repository.deleteById(guardado.getIdRespaldo());

		assertThat(repository.findById(guardado.getIdRespaldo())).isEmpty();
	}
}
