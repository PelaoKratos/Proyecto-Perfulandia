# Microservicio Cliente

Este servicio guarda la informacion de los clientes de Perfulandia. Se usa para registrar sus datos personales y sus direcciones, de forma que otros microservicios puedan consultar si un cliente existe antes de crear pedidos, pagos, tickets o despachos.

## Que gestiona

- Datos del cliente.
- Relacion con el usuario del sistema mediante `idUsuario`.
- Direcciones asociadas al cliente.
- Activacion y desactivacion de clientes.

## Configuracion local

```properties
spring.application.name=cliente
server.port=8082
spring.datasource.url=jdbc:mysql://localhost:3307/cliente_bd?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
```

Base de datos:

```sql
CREATE DATABASE cliente_bd;
```

## Endpoints principales

- `GET /api/v1/clientes`
- `POST /api/v1/clientes`
- `GET /api/v1/clientes/{id}`
- `PUT /api/v1/clientes/{id}`
- `DELETE /api/v1/clientes/{id}`
- `POST /api/v1/clientes/{id}/activar`
- `POST /api/v1/clientes/{id}/desactivar`

## Uso dentro del proyecto

Cliente es consultado por varios servicios:

- Pedido valida el cliente antes de registrar un pedido.
- Pago valida el cliente antes de registrar un pago.
- Despacho consulta datos del cliente para preparar el envio.
- Soporte valida el cliente cuando se crea un ticket.

## Ejecutar

```powershell
mvn spring-boot:run
```

## Probar

```powershell
mvn test
```
