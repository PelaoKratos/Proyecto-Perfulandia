package microservice.perfume.Controller;

import static microservice.perfume.TestDataFactory.ajusteRequest;
import static microservice.perfume.TestDataFactory.alerta;
import static microservice.perfume.TestDataFactory.categoria;
import static microservice.perfume.TestDataFactory.inventario;
import static microservice.perfume.TestDataFactory.inventarioRequest;
import static microservice.perfume.TestDataFactory.movimiento;
import static microservice.perfume.TestDataFactory.movimientoRequest;
import static microservice.perfume.TestDataFactory.producto;
import static microservice.perfume.TestDataFactory.resena;
import static microservice.perfume.TestDataFactory.sucursal;
import static microservice.perfume.TestDataFactory.usuario;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import microservice.perfume.Dto.AjusteInventarioRequest;
import microservice.perfume.Dto.DisponibilidadProductoResponse;
import microservice.perfume.Dto.InventarioRequest;
import microservice.perfume.Dto.MovimientoInventarioRequest;
import microservice.perfume.Model.AlertaStock;
import microservice.perfume.Model.CategoriaProducto;
import microservice.perfume.Model.Inventario;
import microservice.perfume.Model.MovimientoInventario;
import microservice.perfume.Model.Producto;
import microservice.perfume.Model.ResenaProducto;
import microservice.perfume.Model.Sucursal;
import microservice.perfume.Model.UsuarioInventario;
import microservice.perfume.Service.AlertaStockService;
import microservice.perfume.Service.CategoriaProductoService;
import microservice.perfume.Service.InventarioService;
import microservice.perfume.Service.MovimientoInventarioService;
import microservice.perfume.Service.ProductoService;
import microservice.perfume.Service.ResenaProductoService;
import microservice.perfume.Service.SucursalService;
import microservice.perfume.Service.UsuarioInventarioService;

@ExtendWith(MockitoExtension.class)
class ControllerDelegationTest {

    @Mock
    private ProductoService productoService;

    @Mock
    private SucursalService sucursalService;

    @Mock
    private InventarioService inventarioService;

    @Mock
    private MovimientoInventarioService movimientoInventarioService;

    @Mock
    private AlertaStockService alertaStockService;

    @Mock
    private UsuarioInventarioService usuarioInventarioService;

    @Mock
    private CategoriaProductoService categoriaProductoService;

    @Mock
    private ResenaProductoService resenaProductoService;

    @Test
    void productoControllerDelegaCrudYDisponibilidad() {
        ProductoController controller = new ProductoController(productoService);
        Producto producto = producto(1L);
        DisponibilidadProductoResponse disponibilidad = new DisponibilidadProductoResponse(1L, 15);

        when(productoService.agregarProducto(producto)).thenReturn(producto);
        when(productoService.listarProductos()).thenReturn(List.of(producto));
        when(productoService.obtenerProducto(1L)).thenReturn(producto);
        when(productoService.actualizarProducto(1L, producto)).thenReturn(producto);
        when(productoService.consultarDisponibilidad(1L)).thenReturn(disponibilidad);

        assertEquals(producto, controller.agregarProducto(producto));
        assertEquals(List.of(producto), controller.listarProductos());
        assertEquals(producto, controller.obtenerProducto(1L));
        assertEquals(producto, controller.actualizarProducto(1L, producto));
        assertEquals(disponibilidad, controller.consultarDisponibilidad(1L));
        controller.eliminarProducto(1L);

        verify(productoService).eliminarProducto(1L);
    }

    @Test
    void sucursalControllerDelegaCrudEInventario() {
        SucursalController controller = new SucursalController(sucursalService);
        Sucursal sucursal = sucursal(1L);
        Inventario inventario = inventario(1L, 10, 5, 20);

        when(sucursalService.asignarSucursal(sucursal)).thenReturn(sucursal);
        when(sucursalService.listarSucursales()).thenReturn(List.of(sucursal));
        when(sucursalService.obtenerSucursal(1L)).thenReturn(sucursal);
        when(sucursalService.actualizarSucursal(1L, sucursal)).thenReturn(sucursal);
        when(sucursalService.consultarInventario(1L)).thenReturn(List.of(inventario));

        assertEquals(sucursal, controller.asignarSucursal(sucursal));
        assertEquals(List.of(sucursal), controller.listarSucursales());
        assertEquals(sucursal, controller.obtenerSucursal(1L));
        assertEquals(sucursal, controller.actualizarSucursal(1L, sucursal));
        assertEquals(List.of(inventario), controller.consultarInventario(1L));
        controller.eliminarSucursal(1L);

        verify(sucursalService).eliminarSucursal(1L);
    }

    @Test
    void inventarioControllerDelegaCrudAjusteYStockBajo() {
        InventarioController controller = new InventarioController(inventarioService);
        InventarioRequest request = inventarioRequest(10, 5, 20);
        AjusteInventarioRequest ajuste = ajusteRequest("SALIDA", 2);
        Inventario inventario = inventario(1L, 8, 5, 20);

        when(inventarioService.crearInventario(request)).thenReturn(inventario);
        when(inventarioService.listarInventarios()).thenReturn(List.of(inventario));
        when(inventarioService.obtenerInventario(1L)).thenReturn(inventario);
        when(inventarioService.actualizarInventario(1L, request)).thenReturn(inventario);
        when(inventarioService.ajustarInventario(1L, ajuste)).thenReturn(inventario);
        when(inventarioService.verificarStockBajo(1L)).thenReturn(true);

        assertEquals(inventario, controller.crearInventario(request));
        assertEquals(List.of(inventario), controller.listarInventarios());
        assertEquals(inventario, controller.obtenerInventario(1L));
        assertEquals(inventario, controller.actualizarInventario(1L, request));
        assertEquals(inventario, controller.ajustarInventario(1L, ajuste));
        assertTrue(controller.verificarStockBajo(1L));
        controller.eliminarInventario(1L);

        verify(inventarioService).eliminarInventario(1L);
    }

    @Test
    void movimientoInventarioControllerDelegaRegistroEHistorial() {
        MovimientoInventarioController controller = new MovimientoInventarioController(movimientoInventarioService);
        MovimientoInventarioRequest request = movimientoRequest("SALIDA", 2);
        MovimientoInventario movimiento = movimiento(1L);

        when(movimientoInventarioService.registrarMovimiento(request)).thenReturn(movimiento);
        when(movimientoInventarioService.consultarHistorial()).thenReturn(List.of(movimiento));
        when(movimientoInventarioService.consultarHistorialInventario(1L)).thenReturn(List.of(movimiento));

        assertEquals(movimiento, controller.registrarMovimiento(request));
        assertEquals(List.of(movimiento), controller.consultarHistorial());
        assertEquals(List.of(movimiento), controller.consultarHistorialInventario(1L));
    }

    @Test
    void alertaStockControllerDelegaListadoNotificacionYCierre() {
        AlertaStockController controller = new AlertaStockController(alertaStockService);
        AlertaStock alerta = alerta(1L);

        when(alertaStockService.listarAlertas()).thenReturn(List.of(alerta));
        when(alertaStockService.notificar(1L)).thenReturn(alerta);
        when(alertaStockService.cerrarAlerta(1L)).thenReturn(alerta);

        assertEquals(List.of(alerta), controller.listarAlertas());
        assertEquals(alerta, controller.notificar(1L));
        assertEquals(alerta, controller.cerrarAlerta(1L));
    }

    @Test
    void usuarioInventarioControllerDelegaCrudYAcciones() {
        UsuarioInventarioController controller = new UsuarioInventarioController(usuarioInventarioService);
        UsuarioInventario usuario = usuario(1L);
        Producto producto = producto(1L);
        AjusteInventarioRequest ajuste = ajusteRequest("AJUSTE", 5);
        Inventario inventario = inventario(1L, 5, 5, 20);

        when(usuarioInventarioService.agregarUsuario(usuario)).thenReturn(usuario);
        when(usuarioInventarioService.listarUsuarios()).thenReturn(List.of(usuario));
        when(usuarioInventarioService.obtenerUsuario(1L)).thenReturn(usuario);
        when(usuarioInventarioService.actualizarUsuario(1L, usuario)).thenReturn(usuario);
        when(usuarioInventarioService.agregarProducto(producto)).thenReturn(producto);
        when(usuarioInventarioService.actualizarStock(1L, ajuste)).thenReturn(inventario);
        when(usuarioInventarioService.ajustarInventario(1L, ajuste)).thenReturn(inventario);

        assertEquals(usuario, controller.agregarUsuario(usuario));
        assertEquals(List.of(usuario), controller.listarUsuarios());
        assertEquals(usuario, controller.obtenerUsuario(1L));
        assertEquals(usuario, controller.actualizarUsuario(1L, usuario));
        assertEquals(producto, controller.agregarProducto(producto));
        assertEquals(inventario, controller.actualizarStock(1L, ajuste));
        assertEquals(inventario, controller.ajustarInventario(1L, ajuste));
        controller.eliminarUsuario(1L);

        verify(usuarioInventarioService).eliminarUsuario(1L);
    }

    @Test
    void categoriaProductoControllerDelegaCrudYEstados() {
        CategoriaProductoController controller = new CategoriaProductoController(categoriaProductoService);
        CategoriaProducto categoria = categoria(1L);

        when(categoriaProductoService.crearCategoria(categoria)).thenReturn(categoria);
        when(categoriaProductoService.listarCategorias()).thenReturn(List.of(categoria));
        when(categoriaProductoService.obtenerCategoria(1L)).thenReturn(categoria);
        when(categoriaProductoService.buscarPorEstado("ACTIVA")).thenReturn(List.of(categoria));
        when(categoriaProductoService.modificarCategoria(1L, categoria)).thenReturn(categoria);
        when(categoriaProductoService.activarCategoria(1L)).thenReturn(categoria);
        when(categoriaProductoService.desactivarCategoria(1L)).thenReturn(categoria);

        assertEquals(categoria, controller.crearCategoria(categoria));
        assertEquals(List.of(categoria), controller.listarCategorias());
        assertEquals(categoria, controller.obtenerCategoria(1L));
        assertEquals(List.of(categoria), controller.buscarPorEstado("ACTIVA"));
        assertEquals(categoria, controller.modificarCategoria(1L, categoria));
        assertEquals(categoria, controller.activarCategoria(1L));
        assertEquals(categoria, controller.desactivarCategoria(1L));
    }

    @Test
    void resenaProductoControllerDelegaCrudYConsultas() {
        ResenaProductoController controller = new ResenaProductoController(resenaProductoService);
        ResenaProducto resena = resena(1L);

        when(resenaProductoService.crearResena(resena)).thenReturn(resena);
        when(resenaProductoService.listarResenas()).thenReturn(List.of(resena));
        when(resenaProductoService.obtenerResena(1L)).thenReturn(resena);
        when(resenaProductoService.listarPorProducto(1L)).thenReturn(List.of(resena));
        when(resenaProductoService.listarPorCliente(1L)).thenReturn(List.of(resena));
        when(resenaProductoService.modificarResena(1L, resena)).thenReturn(resena);
        when(resenaProductoService.eliminarResena(1L)).thenReturn(resena);
        when(resenaProductoService.validarCompra(1L, 2L, 3L)).thenReturn(true);

        assertEquals(resena, controller.crearResena(resena));
        assertEquals(List.of(resena), controller.listarResenas());
        assertEquals(resena, controller.obtenerResena(1L));
        assertEquals(List.of(resena), controller.listarPorProducto(1L));
        assertEquals(List.of(resena), controller.listarPorCliente(1L));
        assertEquals(resena, controller.modificarResena(1L, resena));
        assertEquals(resena, controller.eliminarResena(1L));
        assertTrue(controller.validarCompra(1L, 2L, 3L));
    }
}
