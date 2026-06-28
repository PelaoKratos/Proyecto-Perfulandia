# Microservicio Soporte

Soporte permite registrar tickets de clientes y llevar el seguimiento de las respuestas del equipo. Sirve para dejar ordenadas las solicitudes, clasificar los problemas y saber quien esta atendiendo cada caso.

## Que gestiona

- Tickets de soporte.
- Categorias de soporte.
- Respuestas asociadas a tickets.
- Prioridad, estado, canal y usuario asignado.

## Configuracion local

```properties
spring.application.name=soporte
server.port=8090
spring.datasource.url=jdbc:mysql://localhost:3307/soporte_bd?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
```

Base de datos:

```sql
CREATE DATABASE soporte_bd;
```

## Microservicios que consulta

```properties
microservices.cliente.obtener-url=http://localhost:8082/api/v1/clientes/{id}
microservices.usuario.obtener-url=http://localhost:8081/api/v1/usuarios/{id}
```

Con eso el ticket puede validar que el cliente exista y que el usuario asignado sea valido.

## Endpoints principales

Tickets:

- `POST /api/v1/soporte/tickets`
- `POST /api/v1/soporte/tickets/detalle`
- `GET /api/v1/soporte/tickets`
- `GET /api/v1/soporte/tickets/{id}`
- `GET /api/v1/soporte/tickets/{id}/detalle`
- `GET /api/v1/soporte/tickets/cliente/{idCliente}`
- `GET /api/v1/soporte/tickets/categoria/{idCategoria}`
- `GET /api/v1/soporte/tickets/usuario/{idUsuario}`
- `GET /api/v1/soporte/tickets/estado/{estado}`
- `PUT /api/v1/soporte/tickets/{id}`
- `DELETE /api/v1/soporte/tickets/{id}`

Categorias y respuestas:

- `POST /api/v1/soporte/categorias`
- `GET /api/v1/soporte/categorias`
- `PUT /api/v1/soporte/categorias/{id}`
- `POST /api/v1/soporte/tickets/{id}/respuestas`
- `GET /api/v1/soporte/tickets/{id}/respuestas`
- `PUT /api/v1/soporte/respuestas/{id}`
- `DELETE /api/v1/soporte/respuestas/{id}`

## Ejecutar

```powershell
mvn spring-boot:run
```

## Probar

```powershell
mvn test
```
