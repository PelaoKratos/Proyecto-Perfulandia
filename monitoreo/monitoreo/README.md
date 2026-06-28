# Microservicio Monitoreo

Repositorio de un microservicio Spring Boot para registrar el estado de servicios, generar alertas del sistema y administrar respaldos. El proyecto sigue un modelo CSR: `Controller`, `Service` y `Repository`, separando la entrada HTTP, la logica de negocio y el acceso a datos.

## Contexto

El microservicio representa un modulo de monitoreo operativo. Su objetivo es dejar trazabilidad del comportamiento de servicios internos, detectar eventos que requieren atencion y mantener registros de respaldos asociados a servicios o usuarios.

El dominio principal esta compuesto por:

- `RegistroMonitoreo`: guarda el estado de un servicio, tiempo de respuesta, fecha de registro y mensaje.
- `AlertaSistema`: representa alertas generadas a partir de eventos o registros de monitoreo.
- `Respaldo`: administra respaldos, rutas de archivo, tipo de respaldo, estado y usuario relacionado.

## Que Conlleva

Este repositorio conlleva una API REST lista para ampliar hacia un sistema de monitoreo real. Actualmente incluye:

- Persistencia con Spring Data JPA.
- Conector MySQL para ejecucion real.
- Base H2 para pruebas automatizadas.
- Lombok para reducir codigo repetitivo en modelos.
- Validacion y estructura de microservicio Spring Boot.
- Pruebas unitarias con Mockito.
- Pruebas de repositorio con H2.
- Cobertura automatizada con JaCoCo al 100% para las clases medidas.

## Arquitectura

La estructura principal es:

```text
src/main/java/microservicio/monitoreo
+-- controller
|   +-- AlertaSistemaController.java
|   +-- RegistroMonitoreoController.java
|   +-- RespaldoController.java
+-- model
|   +-- AlertaSistema.java
|   +-- RegistroMonitoreo.java
|   +-- Respaldo.java
+-- repository
|   +-- AlertaSistemaRepository.java
|   +-- RegistroMonitoreoRepository.java
|   +-- RespaldoRepository.java
+-- service
|   +-- AlertaSistemaService.java
|   +-- RegistroMonitoreoService.java
|   +-- RespaldoService.java
+-- MonitoreoApplication.java
```

## Endpoints Principales

### Registros de monitoreo

- `POST /api/registros`: crea un registro.
- `GET /api/registros`: lista registros.
- `GET /api/registros/{id}`: obtiene un registro por id.
- `GET /api/registros/{id}/disponibilidad`: consulta disponibilidad.
- `GET /api/registros/{id}/rendimiento`: consulta rendimiento.
- `DELETE /api/registros/{id}`: elimina un registro.

### Alertas

- `POST /api/alertas`: crea una alerta.
- `GET /api/alertas`: lista alertas.
- `GET /api/alertas/{id}`: obtiene una alerta por id.
- `GET /api/alertas/registro/{idRegistro}`: lista alertas asociadas a un registro.
- `PATCH /api/alertas/{id}/cerrar`: cierra una alerta.
- `DELETE /api/alertas/{id}`: elimina una alerta.

### Respaldos

- `POST /api/respaldos`: crea un respaldo.
- `GET /api/respaldos`: lista respaldos.
- `GET /api/respaldos/{id}`: obtiene un respaldo por id.
- `GET /api/respaldos/servicio/{nombreServicio}`: lista respaldos por servicio.
- `PATCH /api/respaldos/{id}/restaurar`: marca un respaldo como restaurado.
- `DELETE /api/respaldos/{id}`: marca un respaldo como eliminado.

### Revision de servicios

- `GET /api/monitoreo/servicios`: revisa los microservicios configurados y guarda un registro por cada respuesta.

La lista de servicios se configura en `src/main/resources/application.properties`, en la propiedad `monitoreo.servicios`. Para la prueba final apunta a endpoints reales de cada microservicio, evitando falsos `CAIDO` cuando un servicio no tiene `/actuator/health`.

## Swagger

Con el microservicio levantado, la documentacion se abre en:

```text
http://localhost:8092/swagger-ui.html
```

## Requisitos

- Java 25
- Maven Wrapper incluido (`mvnw.cmd` en Windows)
- MySQL si se configura una base real

## Ejecucion

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Para compilar:

```powershell
.\mvnw.cmd clean package
```

## Pruebas y Cobertura

El proyecto incluye pruebas para controllers, services, modelos y repositories. Las pruebas de repositories usan H2 con el perfil `test`.

Ejecutar suite completa con cobertura:

```powershell
.\mvnw.cmd clean verify
```

El reporte de JaCoCo se genera en:

```text
target/site/jacoco/index.html
```

El build falla si la cobertura baja de 100% en instrucciones o ramas para las clases medidas.

## Configuracion

La configuracion base esta en:

```text
src/main/resources/application.properties
```

La configuracion de pruebas esta en:

```text
src/test/resources/application-test.properties
```

## Alcance Actual

El microservicio entrega una base funcional para monitoreo, alertas y respaldos. Para llevarlo a produccion se recomienda agregar:

- Configuracion real de datasource MySQL.
- Manejo global de excepciones con respuestas HTTP consistentes.
- DTOs para separar API externa de entidades JPA.
- Validaciones de entrada con anotaciones `jakarta.validation`.
- Seguridad para endpoints administrativos.
- Observabilidad con metricas, logs estructurados y health checks.
