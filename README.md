# Perfulandia SPA - Microservicios

Sistema de microservicios para gestionar el flujo operativo de Perfulandia SPA: usuarios, clientes, sucursales, inventario, abastecimiento, pedidos, ventas, pagos, despachos, soporte, reportes y monitoreo.

## Arquitectura

El proyecto usa Spring Boot con separacion por capas:

- `Controller`: expone endpoints REST.
- `Service`: contiene reglas de negocio y validaciones.
- `Repository`: accede a la base de datos con Spring Data JPA.
- `Model`: representa entidades del dominio.

El acceso unificado a las APIs se realiza mediante API Gateway en `localhost:8080`.

## Microservicios

| Microservicio | Carpeta | Puerto | Base de datos | Swagger |
| --- | --- | ---: | --- | --- |
| Gateway | `getawayspring-master` | 8080 | No aplica | No aplica |
| Usuario | `usuario/usuario` | 8081 | `usuario_bd` | `http://localhost:8081/swagger-ui/index.html` |
| Cliente | `cliente` | 8082 | `cliente_bd` | `http://localhost:8082/swagger-ui/index.html` |
| Sucursales | `sucursales1` | 8083 | `sucursal_bd` | `http://localhost:8083/swagger-ui/index.html` |
| Inventario | `perfume` | 8084 | `inventario_bd` | `http://localhost:8084/swagger-ui/index.html` |
| Abastecimiento | `abastecimiento` | 8085 | `abastecimiento_bd` | `http://localhost:8085/swagger-ui/index.html` |
| Pedido | `pedido` | 8086 | `pedido_bd` | `http://localhost:8086/swagger-ui/index.html` |
| Pago | `pago` | 8087 | `pago_bd` | `http://localhost:8087/swagger-ui/index.html` |
| Ventas | `ventas` | 8088 | `venta_db` | `http://localhost:8088/swagger-ui/index.html` |
| Despacho | `despacho` | 8089 | `despacho_bd` | `http://localhost:8089/swagger-ui/index.html` |
| Soporte | `soporte` | 8090 | `soporte_bd` | `http://localhost:8090/swagger-ui/index.html` |
| Reporte | `reporte` | 8091 | `reporte_bd` | `http://localhost:8091/swagger-ui/index.html` |
| Monitoreo | `monitoreo/monitoreo` | 8092 | `monitoreo_bd` | `http://localhost:8092/swagger-ui/index.html` |

La carpeta `perfume` cumple el rol de inventario del sistema.

## Requisitos

- Java compatible con el proyecto.
- Maven o Maven Wrapper.
- MySQL levantado localmente.
- Bases de datos creadas en phpMyAdmin o MySQL.
- API Gateway y microservicios ejecutandose en sus puertos correspondientes.

## Orden recomendado de ejecucion

1. Levantar MySQL.
2. Crear o verificar las bases de datos.
3. Levantar servicios base: Usuario, Cliente, Sucursales e Inventario.
4. Levantar servicios de negocio: Abastecimiento, Pedido, Ventas, Pago y Despacho.
5. Levantar servicios de soporte: Soporte, Reporte y Monitoreo.
6. Levantar el Gateway.

## Gateway

Base URL:

```text
http://localhost:8080
```

Rutas principales por Gateway:

| Servicio | Ruta |
| --- | --- |
| Usuario | `/api/v1/usuarios` |
| Cliente | `/api/v1/clientes` |
| Sucursales | `/api/v1/sucursales` |
| Inventario | `/api/v1/productos`, `/api/v1/perfumes`, `/api/v1/inventarios` |
| Abastecimiento | `/api/proveedores`, `/api/ordenes-compra`, `/api/recepciones` |
| Pedido | `/api/pedidos`, `/api/carritos`, `/api/cupones` |
| Pago | `/api/v1/pagos` |
| Ventas | `/api/v1/venta` |
| Despacho | `/api/despachos` |
| Soporte | `/api/v1/soporte/tickets`, `/api/v1/soporte/categorias` |
| Reporte | `/api/v1/reportes` |
| Monitoreo | `/api/monitoreo/servicios`, `/api/registros`, `/api/alertas`, `/api/respaldos` |

## Flujo final de prueba

Consumir todo desde `http://localhost:8080`:

1. Crear usuario.
2. Crear cliente.
3. Crear sucursal.
4. Crear producto, perfume o inventario.
5. Crear pedido con detalles.
6. Crear venta.
7. Crear pago.
8. Crear despacho.
9. Crear ticket de soporte.
10. Generar reporte.
11. Revisar monitoreo con `GET /api/monitoreo/servicios`.

Si el flujo responde desde `localhost:8080`, el Gateway esta funcionando y los microservicios estan integrados.

## Monitoreo

El endpoint principal es:

```text
GET http://localhost:8080/api/monitoreo/servicios
```

Monitoreo consulta endpoints reales de cada microservicio. Si un servicio aparece como `CAIDO`, revisar que el microservicio este levantado, que el puerto sea correcto y que la base de datos este disponible.

## Swagger

Cada microservicio expone su documentacion Swagger en su propio puerto:

```text
http://localhost:PUERTO/swagger-ui/index.html
```

Ejemplo:

```text
http://localhost:8087/swagger-ui/index.html
```

## Pruebas

Para ejecutar pruebas desde la raiz:

```powershell
mvn test
```

Para ejecutar pruebas de un microservicio especifico:

```powershell
cd pago
mvn test
```

## Notas de entrega

- `bodega` y `comprobante` no forman parte de la entrega final.
- El microservicio `perfume` representa inventario.
- El proyecto se prueba localmente con MySQL y API Gateway.
- El monitoreo valida disponibilidad guardando registros en `monitoreo_bd`.

## Puntos rapidos para defensa

- Patron CSR: cada microservicio separa controller, service, repository y model.
- Comunicacion REST: pedido, pago, ventas, despacho, soporte, reporte y monitoreo consultan otros servicios con clientes HTTP.
- Gateway: centraliza las rutas en `getawayspring-master/src/main/resources/application.yml`.
- Pruebas: se ejecutan con `mvn test` o `mvn verify`; JaCoCo genera reportes de cobertura en `target/site/jacoco/index.html`.
- Swagger: cada microservicio documenta sus endpoints en `/swagger-ui/index.html`.
- Base de datos: cada servicio tiene su propia BD MySQL en `localhost:3307`.
