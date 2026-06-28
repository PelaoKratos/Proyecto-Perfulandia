# Microservicio Sucursales

Este microservicio guarda la informacion de las sucursales y parte de su operacion interna, como horarios, empleados y asignaciones de personal. Otros servicios lo consultan cuando necesitan validar desde que sucursal se vende, se despacha o se genera un reporte.

## Que gestiona

- Sucursales.
- Horarios de atencion.
- Empleados.
- Asignaciones de personal por sucursal y horario.

## Configuracion local

```properties
spring.application.name=sucursales1
server.port=8083
spring.datasource.url=jdbc:mysql://localhost:3307/sucursal_bd?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
```

Base de datos:

```sql
CREATE DATABASE sucursal_bd;
```

## Endpoints principales

- `POST /api/v1/sucursales`
- `GET /api/v1/sucursales`
- `GET /api/v1/sucursales/{id}`
- `PUT /api/v1/sucursales/{id}`
- `DELETE /api/v1/sucursales/{id}`

## Uso dentro del proyecto

Este servicio es consultado por Pedido, Ventas, Despacho y Reportes. Por eso es recomendable levantarlo antes de esos microservicios.

## Ejecutar

```powershell
mvn spring-boot:run
```

## Probar

```powershell
mvn test
```
