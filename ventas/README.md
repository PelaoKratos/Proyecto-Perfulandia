# Microservicio Ventas

Ventas registra las ventas realizadas y coordina el descuento de stock con Inventario. Tambien consulta Producto, Perfume y Sucursales para validar que los datos usados en la venta existan.

## Que gestiona

- Ventas.
- Detalles de venta.
- Calculo de total, descuento e impuestos.
- Consulta de datos externos para completar informacion de la venta.
- Descuento de stock al registrar una venta.

## Configuracion local

```properties
spring.application.name=venta
server.port=8088
spring.datasource.url=jdbc:mysql://localhost:3307/venta_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
```

La base usada actualmente es `venta_db`, porque esa es la que ya tiene datos cargados en phpMyAdmin.

## Microservicios que consulta

```properties
microservices.inventario.descontar-stock-url=http://localhost:8084/api/v1/inventarios/ventas/descontar
microservices.perfume.obtener-url=http://localhost:8084/api/v1/perfumes/{id}
microservices.producto.obtener-url=http://localhost:8084/api/v1/productos/{id}
microservices.producto.disponibilidad-url=http://localhost:8084/api/v1/productos/{id}/disponibilidad
microservices.sucursal.obtener-url=http://localhost:8083/api/v1/sucursales/{id}
```

## Endpoints principales

- `POST /api/v1/venta`
- `POST /api/v1/venta/detalle`
- `GET /api/v1/venta`
- `GET /api/v1/venta/{id}`
- `GET /api/v1/venta/{id}/detalle`
- `PUT /api/v1/venta/{id}`
- `DELETE /api/v1/venta/{id}`

## Crear una venta con varios perfumes

Use `detalles` para enviar todos los perfumes incluidos en la venta:

```json
{
  "fechaVenta": "2026-07-15",
  "totalVenta": 89980,
  "descuentoVenta": 0,
  "estadoVenta": "PAGADA",
  "idSucursal": 1,
  "detalles": [
    {
      "idPerfume": 1,
      "cantidad": 2
    },
    {
      "idPerfume": 2,
      "cantidad": 1
    }
  ]
}
```

El formato anterior con `idPerfume` y `cantidad` en la venta sigue funcionando para ventas de un solo perfume.

## Flujo al crear una venta

1. Se reciben los datos de la venta.
2. Se valida la informacion relacionada, como producto y sucursal.
3. Se solicita a Inventario descontar el stock de cada perfume del detalle.
4. Si el descuento se realiza bien, la venta queda registrada.

## Ejecutar

```powershell
mvn spring-boot:run
```

## Probar

```powershell
mvn test
```
