INSERT IGNORE INTO sucursal (id_sucursal, nombre, direccion, telefono, ciudad, estado, fecha_creacion) VALUES
(1001, 'Sucursal Centro', 'Av. Libertador 123', '221234567', 'Santiago', 'ACTIVA', '2026-06-01'),
(1002, 'Sucursal Norte', 'Av. Norte 456', '221234568', 'Santiago', 'ACTIVA', '2026-06-02'),
(1003, 'Sucursal Sur', 'Av. Sur 789', '221234569', 'Concepcion', 'ACTIVA', '2026-06-03'),
(1004, 'Sucursal Vina del Mar', 'Calle Marina 321', '221234570', 'Vina del Mar', 'ACTIVA', '2026-06-04');

INSERT IGNORE INTO empleado (id_empleado, nombre, rut, email, telefono, estado) VALUES
(1001, 'Ana Perez', '22222222-2', 'ana.perez@perfulandia.cl', '912345678', 'ACTIVO'),
(1002, 'Carlos Rojas', '33333333-3', 'carlos.rojas@perfulandia.cl', '923456789', 'ACTIVO'),
(1003, 'Maria Gonzalez', '44444444-4', 'maria.gonzalez@perfulandia.cl', '934567890', 'ACTIVO'),
(1004, 'Diego Morales', '55555555-5', 'diego.morales@perfulandia.cl', '945678901', 'ACTIVO'),
(1005, 'Paula Soto', '66666666-6', 'paula.soto@perfulandia.cl', '956789012', 'ACTIVO');

INSERT IGNORE INTO horario_sucursal (id_horario, id_sucursal, dia_semana, hora_apertura, hora_cierre, activo) VALUES
(1001, 1001, 'Lunes a Viernes', '09:00:00', '18:00:00', true),
(1002, 1002, 'Lunes a Sabado', '10:00:00', '19:00:00', true),
(1003, 1003, 'Lunes a Viernes', '08:30:00', '17:30:00', true),
(1004, 1004, 'Lunes a Domingo', '10:00:00', '20:00:00', true);

INSERT IGNORE INTO asignacion_personal (id_asignacion, id_empleado, id_sucursal, id_horario, cargo, fecha_inicio, fecha_fin, estado) VALUES
(1001, 1001, 1001, 1001, 'Jefa de Sucursal', '2026-06-05', null, 'ACTIVA'),
(1002, 1002, 1001, 1001, 'Cajero', '2026-06-05', null, 'ACTIVA'),
(1003, 1003, 1002, 1002, 'Vendedora', '2026-06-06', null, 'ACTIVA'),
(1004, 1004, 1003, 1003, 'Supervisor', '2026-06-07', null, 'ACTIVA'),
(1005, 1005, 1004, 1004, 'Vendedora', '2026-06-08', null, 'ACTIVA');
