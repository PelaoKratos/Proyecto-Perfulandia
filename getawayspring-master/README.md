# API Gateway - Perfulandia SPA

Este proyecto centraliza las rutas de los microservicios de Perfulandia usando Spring Cloud Gateway. Su objetivo es entregar una sola entrada local para consumir el sistema desde Postman.

## Puerto

```text
http://localhost:8080
```

## Configuracion principal

Las rutas estan definidas en:

```text
src/main/resources/application.yml
```

El archivo YAML contiene el nombre del servicio, puerto del gateway y rutas hacia cada microservicio.

## Rutas por microservicio

| Microservicio | Puerto interno | Ruta por gateway |
| --- | ---: | --- |
| Usuario | 8081 | `/api/v1/usuarios`, `/api/v1/auth`, `/api/v1/roles`, `/api/v1/permisos` |
| Cliente | 8082 | `/api/v1/clientes` |
| Sucursales | 8083 | `/api/v1/sucursales` |
| Inventario | 8084 | `/api/v1/productos`, `/api/v1/inventarios`, `/api/v1/perfumes` |
| Abastecimiento | 8085 | `/api/proveedores`, `/api/ordenes-compra`, `/api/recepciones` |
| Pedido | 8086 | `/api/pedidos`, `/api/carritos`, `/api/cupones` |
| Pago | 8087 | `/api/v1/pagos` |
| Ventas | 8088 | `/api/v1/venta` |
| Despacho | 8089 | `/api/despachos` |
| Soporte | 8090 | `/api/v1/soporte` |
| Reporte | 8091 | `/api/v1/reportes` |
| Monitoreo | 8092 | `/api/monitoreo`, `/api/registros`, `/api/alertas`, `/api/respaldos` |

## Ejecucion

Primero se levantan los microservicios y luego el gateway:

```powershell
mvn spring-boot:run
```

Despues se prueba desde Postman usando `http://localhost:8080` como URL base.

## Ejemplo de prueba

```http
GET http://localhost:8080/api/v1/clientes
GET http://localhost:8080/api/v1/productos
GET http://localhost:8080/api/monitoreo/servicios
```

Si estas rutas responden desde el puerto `8080`, el enrutamiento del gateway esta funcionando.
