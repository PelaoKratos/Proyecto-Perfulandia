package microservice.reporte.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import microservice.reporte.model.DetalleInventario;
import microservice.reporte.model.DetalleSucursal;
import microservice.reporte.model.DetalleVentas;
import microservice.reporte.model.ExportacionReporte;
import microservice.reporte.model.Metrica;
import microservice.reporte.model.ReporteInventario;
import microservice.reporte.model.ReporteSucursal;
import microservice.reporte.model.ReporteVentas;
import microservice.reporte.model.Reportes;
import microservice.reporte.repository.ExportacionReporteRepository;
import microservice.reporte.repository.DetalleInventarioRepository;
import microservice.reporte.repository.DetalleSucursalRepository;
import microservice.reporte.repository.DetalleVentasRepository;
import microservice.reporte.repository.MetricaRepository;
import microservice.reporte.repository.ReporteInventarioRepository;
import microservice.reporte.repository.ReporteRepository;
import microservice.reporte.repository.ReporteSucursalRepository;
import microservice.reporte.repository.ReporteVentasRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private ReporteVentasRepository reporteVentasRepository;

    @Mock
    private ReporteInventarioRepository reporteInventarioRepository;

    @Mock
    private ReporteSucursalRepository reporteSucursalRepository;

    @Mock
    private MetricaRepository metricaRepository;

    @Mock
    private ExportacionReporteRepository exportacionReporteRepository;

    @Mock
    private DetalleVentasRepository detalleVentasRepository;

    @Mock
    private DetalleInventarioRepository detalleInventarioRepository;

    @Mock
    private DetalleSucursalRepository detalleSucursalRepository;

    @InjectMocks
    private ReporteService reporteService;

    @Test
    void crearReportesDebeGuardarReporte() {
        Reportes reporte = crearReporte();
        when(reporteRepository.save(reporte)).thenReturn(reporte);

        Reportes resultado = reporteService.crearReportes(reporte);

        assertEquals("Reporte mensual", resultado.getTitulo());
        verify(reporteRepository).save(reporte);
    }

    @Test
    void obtenerReportesDebeRetornarLista() {
        when(reporteRepository.findAll()).thenReturn(List.of(crearReporte()));

        List<Reportes> resultado = reporteService.obtenerReportes();

        assertEquals(1, resultado.size());
    }

    @Test
    void obtenerReportePorIdDebeRetornarReporteSiExiste() {
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(crearReporte()));

        Reportes resultado = reporteService.obtenerReportePorId(1L);

        assertEquals(1L, resultado.getIdReporte());
    }

    @Test
    void obtenerReportePorIdDebeRetornarNullSiNoExiste() {
        when(reporteRepository.findById(99L)).thenReturn(Optional.empty());

        Reportes resultado = reporteService.obtenerReportePorId(99L);

        assertNull(resultado);
    }

    @Test
    void updateReportesDebeActualizarCamposDelDiagrama() {
        Reportes existente = crearReporte();
        Reportes cambios = crearReporte();
        cambios.setTitulo("Reporte actualizado");
        cambios.setTipo("Sucursal");
        cambios.setFormato("PDF");
        cambios.setEstadoReporte("Generado");
        cambios.setEstado("Generado");

        when(reporteRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(reporteRepository.save(existente)).thenReturn(existente);

        Reportes resultado = reporteService.updateReportes(1L, cambios);

        assertEquals("Reporte actualizado", resultado.getTitulo());
        assertEquals("Sucursal", resultado.getTipo());
        assertEquals("PDF", resultado.getFormato());
        assertEquals("Generado", resultado.getEstadoReporte());
        assertEquals("Generado", resultado.getEstado());
    }

    @Test
    void updateReportesDebeRetornarNullSiNoExiste() {
        when(reporteRepository.findById(99L)).thenReturn(Optional.empty());

        Reportes resultado = reporteService.updateReportes(99L, crearReporte());

        assertNull(resultado);
    }

    @Test
    void eliminarReporteDebeEliminarPorId() {
        reporteService.eliminarReporte(1L);

        verify(reporteRepository).deleteById(1L);
    }

    @Test
    void generarReporteVentasDebeMarcarTipoYEstado() {
        ReporteVentas reporte = crearReporteVentas();
        when(reporteVentasRepository.save(reporte)).thenReturn(reporte);

        ReporteVentas resultado = reporteService.generarReporteVentas(reporte);

        assertEquals("Ventas", resultado.getTipo());
        assertEquals("Generado", resultado.getEstadoReporte());
    }

    @Test
    void generarReporteInventarioDebeMarcarTipoYEstado() {
        ReporteInventario reporte = crearReporteInventario();
        when(reporteInventarioRepository.save(reporte)).thenReturn(reporte);

        ReporteInventario resultado = reporteService.generarReporteInventario(reporte);

        assertEquals("Inventario", resultado.getTipo());
        assertEquals("Generado", resultado.getEstadoReporte());
    }

    @Test
    void generarReporteSucursalDebeMarcarTipoYEstado() {
        ReporteSucursal reporte = crearReporteSucursal();
        when(reporteSucursalRepository.save(reporte)).thenReturn(reporte);

        ReporteSucursal resultado = reporteService.generarReporteSucursal(reporte);

        assertEquals("Sucursal", resultado.getTipo());
        assertEquals("Generado", resultado.getEstadoReporte());
    }

    @Test
    void agregarMetricaDebeAsociarlaAlReporte() {
        Metrica metrica = crearMetrica();
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(crearReporte()));
        when(metricaRepository.save(any(Metrica.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Metrica resultado = reporteService.agregarMetrica(1L, metrica);

        assertNotNull(resultado.getReporte());
        assertNotNull(resultado.getFechaRegistro());
        verify(metricaRepository).save(metrica);
    }

    @Test
    void agregarMetricaDebeRetornarNullSiReporteNoExiste() {
        when(reporteRepository.findById(99L)).thenReturn(Optional.empty());

        Metrica resultado = reporteService.agregarMetrica(99L, crearMetrica());

        assertNull(resultado);
    }

    @Test
    void obtenerMetricasPorReporteDebeConsultarRepositorio() {
        when(metricaRepository.findByReporteIdReporte(1L)).thenReturn(List.of(crearMetrica()));

        List<Metrica> resultado = reporteService.obtenerMetricasPorReporte(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void exportarReporteDebeActualizarEstadoYCrearExportacion() {
        ExportacionReporte exportacion = crearExportacion();
        Reportes reporte = crearReporte();
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));
        when(exportacionReporteRepository.save(any(ExportacionReporte.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ExportacionReporte resultado = reporteService.exportarReporte(1L, exportacion);

        assertEquals("Exportado", resultado.getEstado());
        assertEquals("Exportado", reporte.getEstadoReporte());
        assertEquals("reportes/1.pdf", resultado.getRutaArchivo());
        assertNotNull(resultado.getFechaExportacion());
    }

    @Test
    void exportarReporteMantieneRutaExistente() {
        ExportacionReporte exportacion = crearExportacion();
        exportacion.setRutaArchivo("exportados/reporte-final.pdf");
        Reportes reporte = crearReporte();
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));
        when(exportacionReporteRepository.save(any(ExportacionReporte.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ExportacionReporte resultado = reporteService.exportarReporte(1L, exportacion);

        assertEquals("exportados/reporte-final.pdf", resultado.getRutaArchivo());
        verify(reporteRepository).save(reporte);
    }

    @Test
    void exportarReporteDebeRetornarNullSiReporteNoExiste() {
        when(reporteRepository.findById(99L)).thenReturn(Optional.empty());

        ExportacionReporte resultado = reporteService.exportarReporte(99L, crearExportacion());

        assertNull(resultado);
    }

    @Test
    void obtenerExportacionesPorReporteDebeConsultarRepositorio() {
        when(exportacionReporteRepository.findByReporteIdReporte(1L)).thenReturn(List.of(crearExportacion()));

        List<ExportacionReporte> resultado = reporteService.obtenerExportacionesPorReporte(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void actualizarMetricaDebeModificarValores() {
        Metrica existente = crearMetrica();
        Metrica cambios = crearMetrica();
        cambios.setNombre("Margen");
        cambios.setValor(42.0);
        cambios.setUnidad("%");
        when(metricaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(metricaRepository.save(existente)).thenReturn(existente);

        Metrica resultado = reporteService.actualizarMetrica(1L, cambios);

        assertEquals("Margen", resultado.getNombre());
        assertEquals(42.0, resultado.getValor());
        assertEquals("%", resultado.getUnidad());
        assertNotNull(resultado.getFechaRegistro());
    }

    @Test
    void actualizarMetricaDebeRetornarNullSiNoExiste() {
        when(metricaRepository.findById(99L)).thenReturn(Optional.empty());

        assertNull(reporteService.actualizarMetrica(99L, crearMetrica()));
    }

    @Test
    void detallesDeleganYSeAsocianAReporte() {
        ReporteVentas ventas = crearReporteVentas();
        ReporteInventario inventario = crearReporteInventario();
        ReporteSucursal sucursal = crearReporteSucursal();
        DetalleVentas detalleVentas = new DetalleVentas();
        detalleVentas.setMontoNeto(100);
        detalleVentas.setImpuestos(19);
        DetalleInventario detalleInventario = new DetalleInventario();
        detalleInventario.setStockActual(3);
        detalleInventario.setStockMinimo(5);
        DetalleSucursal detalleSucursal = new DetalleSucursal();

        when(reporteVentasRepository.findById(1L)).thenReturn(Optional.of(ventas));
        when(reporteInventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));
        when(reporteSucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        when(detalleVentasRepository.save(detalleVentas)).thenReturn(detalleVentas);
        when(detalleInventarioRepository.save(detalleInventario)).thenReturn(detalleInventario);
        when(detalleSucursalRepository.save(detalleSucursal)).thenReturn(detalleSucursal);
        when(detalleVentasRepository.findByIdReporte(1L)).thenReturn(List.of(detalleVentas));
        when(detalleInventarioRepository.findByIdReporte(1L)).thenReturn(List.of(detalleInventario));
        when(detalleSucursalRepository.findByIdReporte(1L)).thenReturn(List.of(detalleSucursal));

        assertEquals(119.0, reporteService.agregarDetalleVentas(1L, detalleVentas).getTotal());
        assertEquals(ventas, detalleVentas.getReporteVentas());
        assertEquals(true, reporteService.agregarDetalleInventario(1L, detalleInventario).verificarStockBajo());
        assertEquals(inventario, detalleInventario.getReporteInventario());
        assertNotNull(reporteService.agregarDetalleSucursal(1L, detalleSucursal).getFechaRegistro());
        assertEquals(sucursal, detalleSucursal.getReporteSucursal());
        assertEquals(1, reporteService.obtenerDetallesVentas(1L).size());
        assertEquals(1, reporteService.obtenerDetallesInventario(1L).size());
        assertEquals(1, reporteService.obtenerDetallesSucursal(1L).size());
    }

    @Test
    void detallesRetornanNullSiReporteNoExiste() {
        when(reporteVentasRepository.findById(99L)).thenReturn(Optional.empty());
        when(reporteInventarioRepository.findById(99L)).thenReturn(Optional.empty());
        when(reporteSucursalRepository.findById(99L)).thenReturn(Optional.empty());

        assertNull(reporteService.agregarDetalleVentas(99L, new DetalleVentas()));
        assertNull(reporteService.agregarDetalleInventario(99L, new DetalleInventario()));
        assertNull(reporteService.agregarDetalleSucursal(99L, new DetalleSucursal()));
    }

    @Test
    void cargarDatosDemoRetornaExistentesSiYaHayReportes() {
        List<Reportes> existentes = List.of(crearReporte());
        when(reporteRepository.findAll()).thenReturn(existentes);

        List<Reportes> resultado = reporteService.cargarDatosDemo();

        assertEquals(existentes, resultado);
    }

    @Test
    void cargarDatosDemoInsertaReportesRelacionesMetricasYExportacion() {
        Reportes[] generalGuardado = new Reportes[1];
        when(reporteRepository.findAll()).thenReturn(List.of());
        when(reporteRepository.save(any(Reportes.class))).thenAnswer(invocation -> {
            Reportes reporte = invocation.getArgument(0);
            if (reporte.getIdReporte() == null) {
                reporte.setIdReporte(1L);
            }
            generalGuardado[0] = reporte;
            return reporte;
        });
        when(reporteVentasRepository.save(any(ReporteVentas.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(reporteInventarioRepository.save(any(ReporteInventario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(reporteSucursalRepository.save(any(ReporteSucursal.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(reporteRepository.findById(1L)).thenAnswer(invocation -> Optional.of(generalGuardado[0]));
        when(metricaRepository.save(any(Metrica.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(exportacionReporteRepository.save(any(ExportacionReporte.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<Reportes> resultado = reporteService.cargarDatosDemo();

        assertEquals(4, resultado.size());
        assertEquals("Reporte general Perfulandia", resultado.get(0).getTitulo());
        assertEquals("Ventas", resultado.get(1).getTipo());
        assertEquals("Inventario", resultado.get(2).getTipo());
        assertEquals("Sucursal", resultado.get(3).getTipo());
        verify(metricaRepository).save(any(Metrica.class));
        verify(exportacionReporteRepository).save(any(ExportacionReporte.class));
    }

    private Reportes crearReporte() {
        Reportes reporte = new Reportes();
        reporte.setIdReporte(1L);
        reporte.setFechaReporte("2026-06-12");
        reporte.setRazonReporte("Reporte mensual");
        reporte.setDescripcionReporte("Revision de indicadores");
        reporte.setEstadoReporte("Pendiente");
        reporte.setEstado("Pendiente");
        reporte.setTitulo("Reporte mensual");
        reporte.setTipo("General");
        reporte.setFormato("JSON");
        return reporte;
    }

    private ReporteVentas crearReporteVentas() {
        ReporteVentas reporte = new ReporteVentas();
        completarBase(reporte);
        reporte.setTotalVentas(150000);
        reporte.setCantidadVentas(12);
        reporte.setPeriodo("2026-06");
        reporte.setIdSucursal(1);
        return reporte;
    }

    private ReporteInventario crearReporteInventario() {
        ReporteInventario reporte = new ReporteInventario();
        completarBase(reporte);
        reporte.setTotalProductos(100);
        reporte.setStockBajo(8);
        reporte.setMovimientos(20);
        return reporte;
    }

    private ReporteSucursal crearReporteSucursal() {
        ReporteSucursal reporte = new ReporteSucursal();
        completarBase(reporte);
        reporte.setIdSucursal(1);
        reporte.setNombreSucursal("Sucursal Centro");
        reporte.setVentasSucursal(250000);
        reporte.setRendimiento(92.5);
        return reporte;
    }

    private void completarBase(Reportes reporte) {
        reporte.setFechaReporte("2026-06-12");
        reporte.setRazonReporte("Reporte mensual");
        reporte.setDescripcionReporte("Revision de indicadores");
        reporte.setEstadoReporte("Pendiente");
        reporte.setEstado("Pendiente");
        reporte.setTitulo("Reporte mensual");
        reporte.setTipo("General");
        reporte.setFormato("JSON");
    }

    private Metrica crearMetrica() {
        Metrica metrica = new Metrica();
        metrica.setNombre("Ventas");
        metrica.setValor(120.5);
        metrica.setUnidad("CLP");
        return metrica;
    }

    private ExportacionReporte crearExportacion() {
        ExportacionReporte exportacion = new ExportacionReporte();
        exportacion.setFormato("PDF");
        exportacion.setEstado("Pendiente");
        return exportacion;
    }
}
