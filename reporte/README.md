# Microservicio Reportes

Reportes junta informacion del sistema para generar vistas resumidas de ventas, inventario y sucursales. No reemplaza a los otros microservicios: los consulta cuando necesita armar un resumen y guarda sus propios reportes, metricas y exportaciones.

## Que gestiona

- Reportes generales.
- Reportes de ventas.
- Reportes de inventario.
- Reportes por sucursal.
- Metricas asociadas a un reporte.
- Exportaciones de reportes.
- Detalles usados para respaldar la informacion generada.

## Configuracion local

```properties
spring.application.name=reporte
server.port=8091
spring.datasource.url=jdbc:mysql://localhost:3307/reporte_bd?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
```

Base de datos:

```sql
CREATE DATABASE reporte_bd;
```

## Microservicios que consulta

```properties
microservices.ventas.url=http://localhost:8088/api/v1/venta
microservices.inventario.url=http://localhost:8084/api/v1/inventarios
microservices.sucursales.url=http://localhost:8083/api/v1/sucursales
```

## Endpoints principales

Reportes base:

- `POST /api/v1/reportes`
- `GET /api/v1/reportes`
- `PUT /api/v1/reportes/{id}`
- `DELETE /api/v1/reportes/{id}`

Reportes especificos:

- `POST /api/v1/reportes/ventas`
- `GET /api/v1/reportes/ventas`
- `POST /api/v1/reportes/inventario`
- `GET /api/v1/reportes/inventario`
- `POST /api/v1/reportes/sucursal`
- `GET /api/v1/reportes/sucursal`

Metricas, exportaciones e integraciones:

- `POST /api/v1/reportes/{id}/metricas`
- `GET /api/v1/reportes/{id}/metricas`
- `PUT /api/v1/reportes/metricas/{id}`
- `POST /api/v1/reportes/{id}/exportaciones`
- `GET /api/v1/reportes/{id}/exportaciones`
- `GET /api/v1/reportes/integraciones/resumen`

## Uso dentro del proyecto

Este servicio conviene levantarlo al final, cuando ventas, inventario y sucursales ya esten corriendo. Asi puede consultar informacion real y no solo lo que tenga en su propia base.

## Ejecutar

```powershell
mvn spring-boot:run
```

## Probar

```powershell
mvn test
```
