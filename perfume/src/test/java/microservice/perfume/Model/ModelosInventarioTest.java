package microservice.perfume.Model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class ModelosInventarioTest {

    @Test
    void categoriaProductoDebeActualizarEstadoYDatos() {
        CategoriaProducto categoria = new CategoriaProducto();
        categoria.setIdCategoria(1L);
        categoria.setNombre("Perfumes");
        categoria.setDescripcion("Fragancias");
        categoria.crearCategoria();

        categoria.modificarCategoria("Cuidado personal", "Productos de cuidado");
        categoria.desactivarCategoria();
        String texto = categoria.toString();

        CategoriaProducto copia = new CategoriaProducto(1L, "Cuidado personal", "Productos de cuidado", "INACTIVA");

        assertAll(
                () -> assertEquals(1L, categoria.getIdCategoria()),
                () -> assertEquals("Cuidado personal", categoria.getNombre()),
                () -> assertEquals("Productos de cuidado", categoria.getDescripcion()),
                () -> assertEquals("INACTIVA", categoria.getEstado()),
                () -> assertTrue(texto.contains("Cuidado personal")),
                () -> assertEquals(copia, categoria),
                () -> assertEquals(copia.hashCode(), categoria.hashCode()));

        categoria.activarCategoria();
        assertEquals("ACTIVA", categoria.getEstado());
    }

    @Test
    void productoDebeSincronizarCategoriaYDisponibilidad() {
        CategoriaProducto categoria = new CategoriaProducto(7L, "Perfumes", "Fragancias", "ACTIVA");
        Producto producto = new Producto();
        producto.setIdProducto(10L);
        producto.setNombre("Citrus");
        producto.setDescripcion("Aroma fresco");
        producto.setPrecio(12990.0);
        producto.setCategoria(categoria);
        producto.agregarProducto();

        assertAll(
                () -> assertEquals(10L, producto.getIdProducto()),
                () -> assertEquals(7L, producto.getIdCategoria()),
                () -> assertEquals(categoria, producto.getCategoria()),
                () -> assertEquals("ACTIVO", producto.getEstado()),
                () -> assertTrue(producto.consultarDisponibilidad()),
                () -> assertTrue(producto.toString().contains("Citrus")));

        Producto copia = new Producto(10L, 7L, "Citrus", "Aroma fresco", 12990.0, "ACTIVO", categoria);
        assertEquals(copia, producto);

        producto.eliminarProducto();
        producto.setCategoria(null);

        assertAll(
                () -> assertEquals("INACTIVO", producto.getEstado()),
                () -> assertFalse(producto.consultarDisponibilidad()),
                () -> assertNull(producto.getIdCategoria()),
                () -> assertNull(producto.getCategoria()));
    }

    @Test
    void inventarioDebeSincronizarProductoSucursalYAjustarStock() {
        Producto producto = new Producto();
        producto.setIdProducto(3L);
        Sucursal sucursal = new Sucursal(4L, "Centro", "Av. Uno", "ACTIVA");

        Inventario inventario = new Inventario();
        inventario.setIdInventario(20L);
        inventario.setProducto(producto);
        inventario.setSucursal(sucursal);
        inventario.setStockActual(8);
        inventario.setStockMinimo(5);
        inventario.setStockMaximo(30);
        inventario.setUbicacion("Bodega A");

        assertAll(
                () -> assertEquals(3L, inventario.getIdProducto()),
                () -> assertEquals(4L, inventario.getIdSucursal()),
                () -> assertFalse(inventario.verificarStockBajo()));

        inventario.actualizarStock(5);
        assertTrue(inventario.verificarStockBajo());

        inventario.ajustarInventario(10);
        Inventario copia = new Inventario(20L, 3L, 4L, producto, sucursal, 15, 5, 30, "Bodega A");

        assertAll(
                () -> assertEquals(15, inventario.getStockActual()),
                () -> assertEquals(copia, inventario),
                () -> assertEquals(copia.hashCode(), inventario.hashCode()),
                () -> assertTrue(inventario.toString().contains("Bodega A")));

        inventario.setProducto(null);
        inventario.setSucursal(null);

        assertAll(
                () -> assertNull(inventario.getIdProducto()),
                () -> assertNull(inventario.getIdSucursal()));
    }

    @Test
    void alertaStockDebeGenerarNotificarYCerrar() {
        Inventario inventario = new Inventario();
        inventario.setIdInventario(30L);
        AlertaStock alerta = new AlertaStock();
        alerta.setIdAlerta(40L);
        alerta.setInventario(inventario);
        alerta.setMensaje("Stock bajo");
        alerta.setNivelPrioridad("ALTA");
        alerta.generarAlerta();

        assertAll(
                () -> assertEquals(30L, alerta.getIdInventario()),
                () -> assertNotNull(alerta.getFechaAlerta()),
                () -> assertEquals(LocalDate.now(), alerta.getFechaAlerta()),
                () -> assertEquals("ACTIVA", alerta.getEstado()),
                () -> assertEquals("Stock bajo", alerta.notificar()));

        AlertaStock copia = new AlertaStock(40L, 30L, alerta.getFechaAlerta(), "Stock bajo", "ALTA", "ACTIVA", inventario);
        assertEquals(copia, alerta);

        alerta.cerrarAlerta();
        alerta.setInventario(null);

        assertAll(
                () -> assertEquals("CERRADA", alerta.getEstado()),
                () -> assertNull(alerta.getIdInventario()),
                () -> assertNull(alerta.getInventario()),
                () -> assertTrue(alerta.toString().contains("Stock bajo")));
    }

    @Test
    void movimientoInventarioDebeRegistrarHistorialYSincronizarIds() {
        Inventario inventario = new Inventario();
        inventario.setIdInventario(50L);
        UsuarioInventario usuario = new UsuarioInventario(60L, "Encargado", "ADMIN", "ACTIVO");

        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setIdMovimiento(70L);
        movimiento.setInventario(inventario);
        movimiento.setUsuario(usuario);
        movimiento.setTipoMovimiento("SALIDA");
        movimiento.setCantidad(2);
        movimiento.setMotivo("Venta");
        movimiento.registrarMovimiento();

        assertAll(
                () -> assertEquals(50L, movimiento.getIdInventario()),
                () -> assertEquals(60L, movimiento.getIdUsuario()),
                () -> assertNotNull(movimiento.getFechaMovimiento()),
                () -> assertEquals("SALIDA - 2 - Venta", movimiento.consultarHistorial()));

        LocalDateTime fecha = movimiento.getFechaMovimiento();
        MovimientoInventario copia = new MovimientoInventario(70L, 50L, 60L, "SALIDA", 2, fecha, "Venta", inventario,
                usuario);
        assertEquals(copia, movimiento);

        movimiento.setInventario(null);
        movimiento.setUsuario(null);

        assertAll(
                () -> assertNull(movimiento.getIdInventario()),
                () -> assertNull(movimiento.getIdUsuario()),
                () -> assertTrue(movimiento.toString().contains("SALIDA")));
    }

    @Test
    void resenaProductoDebeCrearModificarEliminarYValidarCompra() {
        Producto producto = new Producto();
        producto.setIdProducto(80L);
        ResenaProducto resena = new ResenaProducto();
        resena.setIdResena(90L);
        resena.setIdCliente(100L);
        resena.setIdPedido(110L);
        resena.setProducto(producto);
        resena.setCalificacion(4);
        resena.setComentario("Bueno");
        resena.crearResena();

        assertAll(
                () -> assertEquals(80L, resena.getIdProducto()),
                () -> assertNotNull(resena.getFechaResena()),
                () -> assertEquals("ACTIVA", resena.getEstado()),
                () -> assertTrue(resena.validarCompra()));

        resena.modificarResena("Excelente", 5);
        assertAll(
                () -> assertEquals("Excelente", resena.getComentario()),
                () -> assertEquals(5, resena.getCalificacion()));

        LocalDateTime fecha = resena.getFechaResena();
        ResenaProducto copia = new ResenaProducto(90L, 80L, 100L, 110L, 5, "Excelente", fecha, "ACTIVA", producto);
        assertEquals(copia, resena);

        resena.eliminarResena();
        resena.setProducto(null);

        assertAll(
                () -> assertEquals("INACTIVA", resena.getEstado()),
                () -> assertNull(resena.getIdProducto()),
                () -> assertFalse(resena.validarCompra()),
                () -> assertTrue(resena.toString().contains("Excelente")));
    }

    @Test
    void sucursalUsuarioYPerfumeDebenUsarConstructoresYAccesores() {
        Sucursal sucursal = new Sucursal(1L, "Centro", "Av. Principal", "ACTIVA");
        UsuarioInventario usuario = new UsuarioInventario(2L, "Operador", "BODEGA", "ACTIVO");
        Perfume perfume = new Perfume(3L, "Nocturno", "Marca Uno", "Intenso", 25990.0);

        assertAll(
                () -> assertEquals(1L, sucursal.getIdSucursal()),
                () -> assertEquals("Centro", sucursal.getNombre()),
                () -> assertEquals("Av. Principal", sucursal.getDireccion()),
                () -> assertEquals("ACTIVA", sucursal.getEstado()),
                () -> assertTrue(sucursal.toString().contains("Centro")),
                () -> assertEquals(new Sucursal(1L, "Centro", "Av. Principal", "ACTIVA"), sucursal),
                () -> assertEquals(2L, usuario.getIdUsuario()),
                () -> assertEquals("Operador", usuario.getNombre()),
                () -> assertEquals("BODEGA", usuario.getRol()),
                () -> assertEquals("ACTIVO", usuario.getEstado()),
                () -> assertTrue(usuario.toString().contains("Operador")),
                () -> assertEquals(new UsuarioInventario(2L, "Operador", "BODEGA", "ACTIVO"), usuario),
                () -> assertEquals(3L, perfume.getIdPerfume()),
                () -> assertEquals("Nocturno", perfume.getNombrePerfume()),
                () -> assertEquals("Marca Uno", perfume.getMarcaPerfume()),
                () -> assertEquals("Intenso", perfume.getDescripcionPerfume()),
                () -> assertEquals(25990.0, perfume.getPrecioPerfume()),
                () -> assertTrue(perfume.toString().contains("Nocturno")),
                () -> assertEquals(new Perfume(3L, "Nocturno", "Marca Uno", "Intenso", 25990.0), perfume));
    }

    @Test
    void metodosConValoresExistentesDebenMantenerLosDatos() {
        LocalDate fechaAlerta = LocalDate.of(2026, 6, 21);
        AlertaStock alerta = new AlertaStock();
        alerta.setFechaAlerta(fechaAlerta);
        alerta.setEstado("PENDIENTE");
        alerta.generarAlerta();

        AlertaStock alertaSinEstado = new AlertaStock();
        alertaSinEstado.setFechaAlerta(fechaAlerta);
        alertaSinEstado.generarAlerta();

        AlertaStock alertaEstadoEnBlanco = new AlertaStock();
        alertaEstadoEnBlanco.setFechaAlerta(fechaAlerta);
        alertaEstadoEnBlanco.setEstado(" ");
        alertaEstadoEnBlanco.generarAlerta();

        CategoriaProducto categoriaActiva = new CategoriaProducto();
        categoriaActiva.setEstado("ACTIVA");
        categoriaActiva.crearCategoria();

        CategoriaProducto categoriaEnBlanco = new CategoriaProducto();
        categoriaEnBlanco.setEstado(" ");
        categoriaEnBlanco.crearCategoria();

        Producto productoActivo = new Producto();
        productoActivo.setEstado("PUBLICADO");
        productoActivo.agregarProducto();

        Producto productoEnBlanco = new Producto();
        productoEnBlanco.setEstado(" ");
        productoEnBlanco.agregarProducto();

        LocalDateTime fechaResena = LocalDateTime.of(2026, 6, 21, 10, 30);
        ResenaProducto resena = new ResenaProducto();
        resena.setFechaResena(fechaResena);
        resena.setEstado("REVISADA");
        resena.crearResena();

        ResenaProducto resenaSinCompra = new ResenaProducto();
        resenaSinCompra.setIdCliente(1L);

        ResenaProducto resenaSinCliente = new ResenaProducto();
        resenaSinCliente.setIdPedido(1L);
        resenaSinCliente.setProducto(new Producto());

        ResenaProducto resenaSinPedido = new ResenaProducto();
        resenaSinPedido.setIdCliente(1L);
        resenaSinPedido.setProducto(new Producto());

        LocalDateTime fechaMovimiento = LocalDateTime.of(2026, 6, 21, 11, 0);
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setFechaMovimiento(fechaMovimiento);
        movimiento.registrarMovimiento();

        assertAll(
                () -> assertEquals(fechaAlerta, alerta.getFechaAlerta()),
                () -> assertEquals("PENDIENTE", alerta.getEstado()),
                () -> assertEquals(fechaAlerta, alertaSinEstado.getFechaAlerta()),
                () -> assertEquals("ACTIVA", alertaSinEstado.getEstado()),
                () -> assertEquals(fechaAlerta, alertaEstadoEnBlanco.getFechaAlerta()),
                () -> assertEquals("ACTIVA", alertaEstadoEnBlanco.getEstado()),
                () -> assertEquals("ACTIVA", categoriaActiva.getEstado()),
                () -> assertEquals("ACTIVA", categoriaEnBlanco.getEstado()),
                () -> assertEquals("PUBLICADO", productoActivo.getEstado()),
                () -> assertEquals("ACTIVO", productoEnBlanco.getEstado()),
                () -> assertEquals(fechaResena, resena.getFechaResena()),
                () -> assertEquals("REVISADA", resena.getEstado()),
                () -> assertFalse(resenaSinCompra.validarCompra()),
                () -> assertFalse(resenaSinCliente.validarCompra()),
                () -> assertFalse(resenaSinPedido.validarCompra()),
                () -> assertEquals(fechaMovimiento, movimiento.getFechaMovimiento()));
    }
}
