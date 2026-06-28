# Microservicio Pago

Pago registra y controla los pagos hechos por los clientes. Su trabajo es mantener el estado del pago y validar que los datos relacionados existan en otros microservicios antes de guardar informacion importante.

## Que gestiona

- Pagos asociados a clientes, ventas y empleados.
- Metodos de pago disponibles.
- Transacciones generadas por cada pago.
- Comprobantes de pago.
- Estados como confirmado, rechazado o anulado.

## Configuracion local

```properties
spring.application.name=pago
server.port=8087
spring.datasource.url=jdbc:mysql://localhost:3307/pago_bd?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
```

Base de datos:

```sql
CREATE DATABASE pago_bd;
```

## Microservicios que consulta

```properties
microservices.cliente.url=http://localhost:8082/api/v1/clientes
microservices.venta.url=http://localhost:8088/api/v1/venta
microservices.usuario.url=http://localhost:8081/api/v1/usuarios
```

Con esto se evita registrar pagos para clientes, ventas o usuarios que no existan.

## Endpoints principales

- `POST /api/v1/pagos`
- `GET /api/v1/pagos`
- `GET /api/v1/pagos/{id}`
- `PUT /api/v1/pagos/{id}`
- `DELETE /api/v1/pagos/{id}`
- `POST /api/v1/pagos/{id}/confirmar`
- `POST /api/v1/pagos/{id}/rechazar`
- `POST /api/v1/pagos/{id}/anular`
- `GET /api/v1/pagos/venta/{idVenta}`
- `GET /api/v1/pagos/cliente/{idCliente}`
- `GET /api/v1/pagos/estado/{estado}`

## Ejecutar

```powershell
mvn spring-boot:run
```

## Probar

```powershell
mvn test
```
