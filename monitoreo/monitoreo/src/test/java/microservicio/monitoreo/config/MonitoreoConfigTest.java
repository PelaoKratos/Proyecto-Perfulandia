package microservicio.monitoreo.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.http.HttpClient;

import org.junit.jupiter.api.Test;

class MonitoreoConfigTest {

	@Test
	void httpClientTieneTimeoutConfigurado() {
		HttpClient httpClient = new MonitoreoConfig().httpClient();

		assertThat(httpClient.connectTimeout()).isPresent();
		assertThat(httpClient.connectTimeout().get().getSeconds()).isEqualTo(2);
	}
}
