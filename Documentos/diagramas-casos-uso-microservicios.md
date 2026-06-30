# Diagramas de Casos de Uso por Microservicio - Perfulandia SPA

Este documento resume los casos de uso principales expuestos por los controladores REST de cada microservicio.

## Gateway

```mermaid
flowchart LR
    actor["Usuario / Sistema externo"]
    subgraph gateway["API Gateway"]
        gw1(("Centralizar acceso a microservicios"))
        gw2(("Redirigir solicitudes por ruta"))
    end
    actor --> gw1
    actor --> gw2
    gw1 --> usuario["Usuario"]
    gw1 --> cliente["Cliente"]
    gw1 --> sucursales["Sucursales"]
    gw1 --> inventario["Inventario"]
    gw1 --> abastecimiento["Abastecimiento"]
    gw1 --> pedido["Pedido"]
    gw1 --> pago["Pago"]
    gw1 --> ventas["Ventas"]
    gw1 --> despacho["Despacho"]
    gw1 --> soporte["Soporte"]
    gw1 --> reporte["Reporte"]
    gw1 --> monitoreo["Monitoreo"]
```

## Usuario

```mermaid
flowchart LR
    admin["Administrador"]
    usuario["Usuario"]
    subgraph ms["Microservicio Usuario"]
        uc1(("Gestionar usuarios"))
        uc2(("Activar usuario"))
        uc3(("Desactivar usuario"))
        uc4(("Gestionar roles"))
        uc5(("Gestionar permisos"))
        uc6(("Iniciar sesion"))
        uc7(("Cerrar sesion"))
        uc8(("Recuperar contrasena"))
        uc9(("Validar token"))
    end
    admin --> uc1
    admin --> uc2
    admin --> uc3
    admin --> uc4
    admin --> uc5
    usuario --> uc6
    usuario --> uc7
    usuario --> uc8
    usuario --> uc9
```

## Cliente

```mermaid
flowchart LR
    admin["Administrador"]
    ejecutivo["Ejecutivo de ventas"]
    subgraph ms["Microservicio Cliente"]
        uc1(("Crear cliente"))
        uc2(("Listar clientes"))
        uc3(("Consultar cliente"))
        uc4(("Actualizar cliente"))
        uc5(("Eliminar cliente"))
        uc6(("Activar cliente"))
        uc7(("Desactivar cliente"))
    end
    ejecutivo --> uc1
    ejecutivo --> uc2
    ejecutivo --> uc3
    ejecutivo --> uc4
    admin --> uc5
    admin --> uc6
    admin --> uc7
```

## Sucursales

```mermaid
flowchart LR
    admin["Administrador"]
    encargado["Encargado de sucursal"]
    subgraph ms["Microservicio Sucursales"]
        uc1(("Crear sucursal"))
        uc2(("Listar sucursales"))
        uc3(("Consultar sucursal"))
        uc4(("Actualizar sucursal"))
        uc5(("Eliminar sucursal"))
    end
    admin --> uc1
    admin --> uc2
    admin --> uc3
    admin --> uc4
    admin --> uc5
    encargado --> uc2
    encargado --> uc3
```

## Inventario / Perfume

```mermaid
flowchart LR
    inventario["Encargado de inventario"]
    cliente["Cliente"]
    ventas["Microservicio Ventas"]
    pedido["Microservicio Pedido"]
    subgraph ms["Microservicio Inventario"]
        uc1(("Gestionar productos"))
        uc2(("Gestionar perfumes"))
        uc3(("Gestionar categorias"))
        uc4(("Gestionar inventarios"))
        uc5(("Ajustar stock"))
        uc6(("Descontar stock por venta"))
        uc7(("Registrar movimientos"))
        uc8(("Consultar historial de inventario"))
        uc9(("Consultar disponibilidad"))
        uc10(("Verificar stock bajo"))
        uc11(("Gestionar alertas de stock"))
        uc12(("Gestionar resenas de producto"))
        uc13(("Validar compra para resena"))
        uc14(("Gestionar usuarios de inventario"))
        uc15(("Consultar inventario por sucursal"))
    end
    inventario --> uc1
    inventario --> uc2
    inventario --> uc3
    inventario --> uc4
    inventario --> uc5
    ventas --> uc6
    inventario --> uc7
    inventario --> uc8
    cliente --> uc9
    inventario --> uc10
    inventario --> uc11
    cliente --> uc12
    pedido --> uc13
    inventario --> uc14
    inventario --> uc15
```

## Abastecimiento

```mermaid
flowchart LR
    bodega["Encargado de bodega"]
    compras["Encargado de compras"]
    proveedor["Proveedor"]
    subgraph ms["Microservicio Abastecimiento"]
        uc1(("Gestionar proveedores"))
        uc2(("Cambiar estado de proveedor"))
        uc3(("Crear orden de compra"))
        uc4(("Consultar ordenes de compra"))
        uc5(("Buscar ordenes por estado"))
        uc6(("Buscar ordenes por proveedor"))
        uc7(("Aprobar orden de compra"))
        uc8(("Cancelar orden de compra"))
        uc9(("Eliminar orden de compra"))
        uc10(("Registrar recepcion de mercancia"))
        uc11(("Consultar recepciones"))
        uc12(("Confirmar recepcion"))
    end
    compras --> uc1
    compras --> uc2
    compras --> uc3
    compras --> uc4
    compras --> uc5
    compras --> uc6
    compras --> uc7
    compras --> uc8
    compras --> uc9
    bodega --> uc10
    bodega --> uc11
    bodega --> uc12
    proveedor --> uc3
```

## Pedido

```mermaid
flowchart LR
    cliente["Cliente"]
    ventas["Ejecutivo de ventas"]
    subgraph ms["Microservicio Pedido"]
        uc1(("Gestionar carritos"))
        uc2(("Agregar producto al carrito"))
        uc3(("Modificar cantidad de producto"))
        uc4(("Aplicar descuento al carrito"))
        uc5(("Confirmar compra"))
        uc6(("Quitar producto del carrito"))
        uc7(("Gestionar pedidos"))
        uc8(("Consultar pedidos por cliente"))
        uc9(("Consultar pedidos por estado"))
        uc10(("Consultar historial de pedido"))
        uc11(("Confirmar pedido"))
        uc12(("Cancelar pedido"))
        uc13(("Aplicar cupon"))
        uc14(("Anular uso de cupon"))
        uc15(("Gestionar cupones"))
        uc16(("Cambiar estado de cupon"))
    end
    cliente --> uc1
    cliente --> uc2
    cliente --> uc3
    cliente --> uc5
    cliente --> uc6
    cliente --> uc8
    cliente --> uc10
    ventas --> uc4
    ventas --> uc7
    ventas --> uc9
    ventas --> uc11
    ventas --> uc12
    ventas --> uc13
    ventas --> uc14
    ventas --> uc15
    ventas --> uc16
```

## Pago

```mermaid
flowchart LR
    cliente["Cliente"]
    cajero["Cajero"]
    ventas["Microservicio Ventas"]
    subgraph ms["Microservicio Pago"]
        uc1(("Crear pago"))
        uc2(("Listar pagos"))
        uc3(("Consultar pago"))
        uc4(("Actualizar pago"))
        uc5(("Eliminar pago"))
        uc6(("Confirmar pago"))
        uc7(("Rechazar pago"))
        uc8(("Anular pago"))
        uc9(("Consultar pagos por venta"))
        uc10(("Consultar pagos por cliente"))
        uc11(("Consultar pagos por estado"))
    end
    cliente --> uc1
    cajero --> uc2
    cajero --> uc3
    cajero --> uc4
    cajero --> uc5
    cajero --> uc6
    cajero --> uc7
    cajero --> uc8
    ventas --> uc9
    cajero --> uc10
    cajero --> uc11
```

## Ventas

```mermaid
flowchart LR
    vendedor["Vendedor"]
    admin["Administrador"]
    subgraph ms["Microservicio Ventas"]
        uc1(("Crear venta"))
        uc2(("Crear venta con detalle"))
        uc3(("Listar ventas"))
        uc4(("Consultar venta"))
        uc5(("Consultar detalle de venta"))
        uc6(("Actualizar venta"))
        uc7(("Eliminar venta"))
    end
    vendedor --> uc1
    vendedor --> uc2
    vendedor --> uc3
    vendedor --> uc4
    vendedor --> uc5
    vendedor --> uc6
    admin --> uc7
```

## Despacho

```mermaid
flowchart LR
    logistica["Encargado de logistica"]
    transportista["Transportista"]
    cliente["Cliente"]
    subgraph ms["Microservicio Despacho"]
        uc1(("Crear despacho"))
        uc2(("Listar despachos"))
        uc3(("Consultar despacho"))
        uc4(("Consultar datos relacionados"))
        uc5(("Buscar despacho por pedido"))
        uc6(("Buscar despacho por cliente"))
        uc7(("Buscar despacho por sucursal"))
        uc8(("Buscar despacho por estado"))
        uc9(("Actualizar despacho"))
        uc10(("Cambiar estado de despacho"))
        uc11(("Asignar ruta"))
        uc12(("Marcar en transito"))
        uc13(("Confirmar entrega"))
        uc14(("Cancelar despacho"))
        uc15(("Eliminar despacho"))
        uc16(("Cargar datos demo"))
    end
    logistica --> uc1
    logistica --> uc2
    logistica --> uc3
    logistica --> uc4
    logistica --> uc5
    logistica --> uc6
    logistica --> uc7
    logistica --> uc8
    logistica --> uc9
    logistica --> uc10
    logistica --> uc11
    transportista --> uc12
    transportista --> uc13
    logistica --> uc14
    logistica --> uc15
    logistica --> uc16
    cliente --> uc6
```

## Soporte

```mermaid
flowchart LR
    cliente["Cliente"]
    agente["Agente de soporte"]
    admin["Administrador"]
    subgraph ms["Microservicio Soporte"]
        uc1(("Crear ticket"))
        uc2(("Crear ticket con detalle"))
        uc3(("Listar tickets"))
        uc4(("Consultar ticket"))
        uc5(("Consultar detalle de ticket"))
        uc6(("Buscar tickets por cliente"))
        uc7(("Buscar tickets por categoria"))
        uc8(("Buscar tickets por usuario asignado"))
        uc9(("Buscar tickets por estado"))
        uc10(("Actualizar ticket"))
        uc11(("Cerrar ticket"))
        uc12(("Cambiar estado de ticket"))
        uc13(("Asignar prioridad"))
        uc14(("Eliminar ticket"))
        uc15(("Gestionar categorias"))
        uc16(("Activar categoria"))
        uc17(("Desactivar categoria"))
        uc18(("Gestionar respuestas de ticket"))
    end
    cliente --> uc1
    cliente --> uc2
    cliente --> uc4
    cliente --> uc5
    cliente --> uc6
    agente --> uc3
    agente --> uc7
    agente --> uc8
    agente --> uc9
    agente --> uc10
    agente --> uc11
    agente --> uc12
    agente --> uc13
    agente --> uc18
    admin --> uc14
    admin --> uc15
    admin --> uc16
    admin --> uc17
```

## Reporte

```mermaid
flowchart LR
    analista["Analista"]
    gerencia["Gerencia"]
    servicios["Otros microservicios"]
    subgraph ms["Microservicio Reporte"]
        uc1(("Gestionar reportes generales"))
        uc2(("Generar reporte de ventas"))
        uc3(("Consultar reportes de ventas"))
        uc4(("Generar reporte de inventario"))
        uc5(("Consultar reportes de inventario"))
        uc6(("Generar reporte de sucursal"))
        uc7(("Consultar reportes de sucursal"))
        uc8(("Gestionar metricas"))
        uc9(("Exportar reporte"))
        uc10(("Consultar exportaciones"))
        uc11(("Gestionar detalles de ventas"))
        uc12(("Gestionar detalles de inventario"))
        uc13(("Gestionar detalles de sucursal"))
        uc14(("Consultar resumen de integraciones"))
    end
    analista --> uc1
    analista --> uc2
    analista --> uc3
    analista --> uc4
    analista --> uc5
    analista --> uc6
    analista --> uc7
    analista --> uc8
    analista --> uc9
    analista --> uc10
    analista --> uc11
    analista --> uc12
    analista --> uc13
    gerencia --> uc3
    gerencia --> uc5
    gerencia --> uc7
    gerencia --> uc10
    servicios --> uc14
```

## Monitoreo

```mermaid
flowchart LR
    admin["Administrador tecnico"]
    sistema["Sistema"]
    subgraph ms["Microservicio Monitoreo"]
        uc1(("Verificar servicios"))
        uc2(("Registrar monitoreo"))
        uc3(("Listar registros"))
        uc4(("Consultar registro"))
        uc5(("Verificar disponibilidad"))
        uc6(("Consultar rendimiento"))
        uc7(("Eliminar registro"))
        uc8(("Generar alerta"))
        uc9(("Listar alertas"))
        uc10(("Cerrar alerta"))
        uc11(("Eliminar alerta"))
        uc12(("Crear respaldo"))
        uc13(("Listar respaldos"))
        uc14(("Restaurar respaldo"))
        uc15(("Eliminar respaldo"))
    end
    sistema --> uc1
    sistema --> uc2
    sistema --> uc8
    admin --> uc3
    admin --> uc4
    admin --> uc5
    admin --> uc6
    admin --> uc7
    admin --> uc9
    admin --> uc10
    admin --> uc11
    admin --> uc12
    admin --> uc13
    admin --> uc14
    admin --> uc15
```
