# Microservicio Inventario

Este microservicio maneja productos, categorias, stock y movimientos de inventario. Aunque la carpeta se llama `perfume`, dentro del proyecto cumple el rol de Inventario, por eso tambien expone productos y stock para que otros servicios puedan consultar disponibilidad.

## Que gestiona

- Productos y categorias.
- Inventario por producto y sucursal.
- Movimientos de entrada, salida y ajuste.
- Alertas de stock bajo.
- Resenas de productos.
- Datos simples de sucursal y usuario de inventario.

## Configuracion local

```properties
spring.application.name=inventario
server.port=8084
spring.datasource.url=jdbc:mysql://localhost:3307/inventario_bd?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
```

Base de datos:

```sql
CREATE DATABASE inventario_bd;
```

## Endpoints principales

Productos:

- `POST /api/v1/productos`
- `GET /api/v1/productos`
- `GET /api/v1/productos/{id}`
- `PUT /api/v1/productos/{id}`
- `DELETE /api/v1/productos/{id}`
- `GET /api/v1/productos/{id}/disponibilidad`

Inventario:

- `POST /api/v1/inventarios`
- `GET /api/v1/inventarios`
- `GET /api/v1/inventarios/{id}`
- `PUT /api/v1/inventarios/{id}`
- `DELETE /api/v1/inventarios/{id}`
- `PUT /api/v1/inventarios/{id}/ajustar`
- `POST /api/v1/inventarios/ventas/descontar`
- `GET /api/v1/inventarios/{id}/stock-bajo`

Movimientos y alertas:

- `POST /api/v1/movimientos-inventario`
- `GET /api/v1/movimientos-inventario`
- `GET /api/v1/movimientos-inventario/inventarios/{idInventario}`
- `GET /api/v1/alertas-stock`
- `GET /api/v1/alertas-stock/{id}/notificar`

Categorias, perfumes y resenas:

- `GET /api/v1/categorias-producto`
- `GET /api/v1/perfumes`
- `GET /api/v1/resenas-producto`
- `GET /api/v1/resenas-producto/producto/{idProducto}`
- `GET /api/v1/resenas-producto/cliente/{idCliente}`

## Uso dentro del proyecto

Ventas consulta este servicio para validar productos y descontar stock. Pedido tambien lo usa para validar productos antes de registrar una compra.

## Ejecutar

```powershell
mvn spring-boot:run
```

## Probar

```powershell
mvn test
```
