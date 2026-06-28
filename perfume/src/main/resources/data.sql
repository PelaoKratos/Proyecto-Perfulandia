INSERT IGNORE INTO categoria_producto (id_categoria, nombre, descripcion, estado) VALUES
(1001, 'Perfumes', 'Fragancias para uso diario y ocasiones especiales', 'ACTIVA'),
(1002, 'Cuidado Personal', 'Productos complementarios de cuidado y belleza', 'ACTIVA'),
(1003, 'Sets de Regalo', 'Combos promocionales de perfumes y accesorios', 'ACTIVA');

INSERT IGNORE INTO producto (id_producto, id_categoria, nombre, descripcion, precio, estado) VALUES
(1001, 1001, 'Aurora Floral 100ml', 'Perfume floral con notas de jazmin y vainilla', 34990.00, 'ACTIVO'),
(1002, 1001, 'Nocturno Intenso 75ml', 'Fragancia amaderada de alta fijacion', 42990.00, 'ACTIVO'),
(1003, 1002, 'Crema Hidratante Silk 250ml', 'Crema corporal con aroma suave', 12990.00, 'ACTIVO'),
(1004, 1003, 'Set Esencia Citrus', 'Set de perfume citrus y crema corporal', 51990.00, 'ACTIVO');

INSERT IGNORE INTO perfume (id_perfume, nombre_perfume, marca_perfume, descripcion_perfume, precio_perfume) VALUES
(1001, 'Aurora Floral', 'Perfulandia', 'Fragancia floral fresca de larga duracion', 34990.00),
(1002, 'Nocturno Intenso', 'Perfulandia', 'Perfume amaderado intenso para noche', 42990.00),
(1003, 'Brisa Citrus', 'Perfulandia', 'Fragancia citrica ligera y energizante', 29990.00);

INSERT IGNORE INTO sucursal (id_sucursal, nombre, direccion, estado) VALUES
(1001, 'Sucursal Centro', 'Av. Libertador 123, Santiago', 'ACTIVA'),
(1002, 'Sucursal Norte', 'Av. Norte 456, Santiago', 'ACTIVA'),
(1003, 'Sucursal Vina del Mar', 'Calle Marina 321, Vina del Mar', 'ACTIVA');

INSERT IGNORE INTO usuario_inventario (id_usuario, nombre, rol, estado) VALUES
(1001, 'Ana Encargada', 'ADMIN_INVENTARIO', 'ACTIVO'),
(1002, 'Carlos Bodega', 'OPERADOR_BODEGA', 'ACTIVO'),
(1003, 'Maria Supervisora', 'SUPERVISOR', 'ACTIVO');

INSERT IGNORE INTO inventario (id_inventario, id_producto, id_sucursal, stock_actual, stock_minimo, stock_maximo, ubicacion) VALUES
(1001, 1001, 1001, 35, 10, 80, 'Bodega A - Estante 1'),
(1002, 1002, 1001, 8, 10, 60, 'Bodega A - Estante 2'),
(1003, 1003, 1002, 50, 15, 100, 'Bodega Norte - Pasillo 3'),
(1004, 1004, 1003, 12, 8, 40, 'Bodega Vina - Estante 4');

INSERT IGNORE INTO movimiento_inventario (id_movimiento, id_inventario, id_usuario, tipo_movimiento, cantidad, fecha_movimiento, motivo) VALUES
(1001, 1001, 1001, 'ENTRADA', 20, '2026-06-15 09:30:00', 'Reposicion inicial'),
(1002, 1002, 1002, 'SALIDA', 5, '2026-06-16 14:10:00', 'Venta tienda centro'),
(1003, 1003, 1002, 'AJUSTE', 3, '2026-06-17 11:45:00', 'Correccion conteo fisico'),
(1004, 1004, 1003, 'ENTRADA', 10, '2026-06-18 16:20:00', 'Recepcion proveedor');

INSERT IGNORE INTO alerta_stock (id_alerta, id_inventario, fecha_alerta, mensaje, nivel_prioridad, estado) VALUES
(1001, 1002, '2026-06-16', 'Stock bajo para Nocturno Intenso 75ml en Sucursal Centro', 'ALTA', 'ACTIVA'),
(1002, 1004, '2026-06-18', 'Stock cercano al minimo para Set Esencia Citrus', 'MEDIA', 'ACTIVA');

INSERT IGNORE INTO resena_producto (id_resena, id_producto, id_cliente, id_pedido, calificacion, comentario, fecha_resena, estado) VALUES
(1001, 1001, 2001, 3001, 5, 'Aroma elegante y buena duracion.', '2026-06-19 12:00:00', 'ACTIVA'),
(1002, 1002, 2002, 3002, 4, 'Muy intenso, ideal para la noche.', '2026-06-20 15:30:00', 'ACTIVA'),
(1003, 1004, 2003, 3003, 5, 'Excelente opcion para regalo.', '2026-06-21 10:15:00', 'ACTIVA');
