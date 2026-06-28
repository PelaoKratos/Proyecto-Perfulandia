package microservice.reporte.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class ReporteModelTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void reportesCompletaDatosPorDefectoYVisualiza() {
        Reportes reporte = new Reportes();
        reporte.setFechaReporte("2026-06-16");
        reporte.setRazonReporte("Revision mensual");
        reporte.setDescripcionReporte("Indicadores del mes");
        reporte.setEstadoReporte("Pendiente");
        reporte.setEstado("Pendiente");

        reporte.completarDatosDelDiagrama();

        assertEquals("Revision mensual", reporte.getTitulo());
        assertEquals("General", reporte.getTipo());
        assertEquals("JSON", reporte.getFormato());
        assertEquals("Revision mensual - General - Pendiente", reporte.visualizar());
        assertNotNull(reporte.getFechaGeneracion());
    }

    @Test
    void reportesGeneraYExportaEstados() {
        Reportes reporte = reporteBase();

        reporte.generar();
        assertEquals("Generado", reporte.getEstadoReporte());
        assertEquals("Generado", reporte.getEstado());

        reporte.exportar();
        assertEquals("Exportado", reporte.getEstadoReporte());
    }

    @Test
    void reporteVentasCalculaYGenera() {
        ReporteVentas reporte = new ReporteVentas();
        completarBase(reporte);
        reporte.setTotalVentas(150000);
        reporte.setCantidadVentas(12);
        reporte.setPeriodo("2026-06");
        reporte.setIdSucursal(2);

        reporte.generarReporteVentas();

        assertEquals("Ventas", reporte.getTipo());
        assertEquals("Generado", reporte.getEstadoReporte());
        assertEquals(150000, reporte.calcularTotales());
        assertTrue(validator.validate(reporte).isEmpty());
    }

    @Test
    void reporteInventarioCalculaStock() {
        ReporteInventario reporte = new ReporteInventario();
        completarBase(reporte);
        reporte.setTotalProductos(100);
        reporte.setStockBajo(15);
        reporte.setMovimientos(20);
        reporte.setFechaCorte(LocalDate.of(2026, 6, 16));

        reporte.generarReporteInventario();

        assertEquals("Inventario", reporte.getTipo());
        assertEquals(85, reporte.calcularStock());
        assertEquals(LocalDate.of(2026, 6, 16), reporte.getFechaCorte());
    }

    @Test
    void reporteSucursalComparaMetricas() {
        ReporteSucursal reporte = new ReporteSucursal();
        completarBase(reporte);
        reporte.setIdSucursal(1);
        reporte.setNombreSucursal("Sucursal Centro");
        reporte.setVentasSucursal(250000);
        reporte.setRendimiento(92.5);

        reporte.generarReporteSucursal();

        assertEquals("Sucursal", reporte.getTipo());
        assertEquals(12.5, reporte.compararMetricas(80.0));
    }

    @Test
    void metricaCalculaYVisualiza() {
        Metrica metrica = new Metrica(1L, 1L, "Ventas", 120.5, "CLP", LocalDateTime.of(2026, 6, 16, 10, 0), reporteBase());

        assertEquals(120.5, metrica.calcular());
        assertEquals("Ventas: 120.5 CLP", metrica.visualizar());
        metrica.actualizarMetrica(99.0, "%");
        assertEquals(99.0, metrica.getValor());
        assertEquals("%", metrica.getUnidad());
        assertNotNull(metrica.toString());
    }

    @Test
    void exportacionGeneraExcelPdfYDescarga() {
        ExportacionReporte exportacion = new ExportacionReporte();
        exportacion.setIdExportacion(1L);
        exportacion.setRutaArchivo("reportes/1.pdf");
        exportacion.setReporte(reporteBase());

        exportacion.exportarExcel();
        assertEquals("EXCEL", exportacion.getFormato());
        assertEquals("Exportado", exportacion.getEstado());
        assertNotNull(exportacion.getFechaExportacion());

        exportacion.exportarPDF();
        assertEquals("PDF", exportacion.getFormato());
        assertEquals("reportes/1.pdf", exportacion.descargar());
        assertEquals("reportes/1.pdf", exportacion.descargarArchivo());
    }

    @Test
    void detallesCalculanYVerificanDatos() {
        ReporteVentas ventas = new ReporteVentas();
        ventas.setIdReporte(1L);
        DetalleVentas detalleVentas = new DetalleVentas();
        detalleVentas.setMontoNeto(1000);
        detalleVentas.setImpuestos(190);
        detalleVentas.setReporteVentas(ventas);

        assertEquals(1190, detalleVentas.calcularTotal());
        assertEquals(1L, detalleVentas.getIdReporte());

        ReporteInventario inventario = new ReporteInventario();
        inventario.setIdReporte(2L);
        DetalleInventario detalleInventario = new DetalleInventario();
        detalleInventario.setStockActual(4);
        detalleInventario.setStockMinimo(5);
        detalleInventario.setReporteInventario(inventario);

        assertTrue(detalleInventario.verificarStockBajo());
        assertEquals(2L, detalleInventario.getIdReporte());

        ReporteSucursal sucursal = new ReporteSucursal();
        sucursal.setIdReporte(3L);
        DetalleSucursal detalleSucursal = new DetalleSucursal();
        detalleSucursal.setReporteSucursal(sucursal);
        detalleSucursal.agregarDetalle();

        assertNotNull(detalleSucursal.getFechaRegistro());
        assertEquals(3L, detalleSucursal.getIdReporte());
    }

    @Test
    void reportesCompletaEstadosYFechaDesdeCamposDisponibles() {
        Reportes conEstadoReporte = new Reportes(
                1L,
                "Titulo",
                "General",
                LocalDateTime.of(2026, 6, 16, 8, 30),
                null,
                null,
                "JSON",
                null,
                null,
                "Razon base",
                "Descripcion base",
                "Pendiente");

        conEstadoReporte.completarDatosDelDiagrama();

        assertEquals("Pendiente", conEstadoReporte.getEstado());
        assertEquals("2026-06-16", conEstadoReporte.getFechaReporte());

        Reportes conEstado = new Reportes(
                2L,
                "Titulo",
                "General",
                null,
                null,
                null,
                "JSON",
                "Generado",
                "2026-06-16",
                "Razon base",
                "Descripcion base",
                null);
        conEstado.completarDatosDelDiagrama();

        assertEquals("Generado", conEstado.getEstadoReporte());

        Reportes estadoEnBlanco = new Reportes();
        estadoEnBlanco.setEstadoReporte(" ");
        estadoEnBlanco.setEstado("Pendiente");

        assertEquals("Pendiente", estadoEnBlanco.getEstadoReporte());
    }

    @Test
    void reporteVentasCalculaTotalesDesdeDetalles() {
        ReporteVentas reporte = new ReporteVentas();
        DetalleVentas primerDetalle = new DetalleVentas();
        primerDetalle.setMontoNeto(1000);
        primerDetalle.setImpuestos(190);
        DetalleVentas segundoDetalle = new DetalleVentas();
        segundoDetalle.setMontoNeto(2000);
        segundoDetalle.setImpuestos(380);
        reporte.setDetalles(List.of(primerDetalle, segundoDetalle));

        assertEquals(3570, reporte.calcularTotales());

        reporte.setDetalles(null);
        reporte.setTotalVentas(4500);

        assertEquals(4500, reporte.calcularTotales());
    }

    @Test
    void metricaYExportacionSincronizanReporte() {
        Reportes reporte = reporteBase();
        Metrica metrica = new Metrica();
        ExportacionReporte exportacion = new ExportacionReporte();

        metrica.setValor(35.5);
        metrica.setReporte(reporte);
        exportacion.setReporte(reporte);

        assertEquals(35.5, metrica.calcularMetrica());
        assertEquals(1L, metrica.getIdReporte());
        assertEquals(1L, exportacion.getIdReporte());

        metrica.setReporte(null);
        exportacion.setReporte(null);

        assertEquals(null, metrica.getIdReporte());
        assertEquals(null, exportacion.getIdReporte());
    }

    @Test
    void detallesCubrenRamasAlternativas() {
        DetalleInventario inventario = new DetalleInventario();
        inventario.setStockActual(10);
        inventario.setStockMinimo(5);
        inventario.agregarDetalle();
        inventario.setReporteInventario(null);

        DetalleSucursal sucursal = new DetalleSucursal();
        LocalDate fecha = LocalDate.of(2026, 6, 16);
        sucursal.setFechaRegistro(fecha);
        sucursal.agregarDetalle();
        sucursal.setReporteSucursal(null);

        DetalleVentas ventas = new DetalleVentas();
        ventas.agregarDetalle();
        ventas.setReporteVentas(null);

        assertEquals(false, inventario.verificarStockBajo());
        assertEquals(null, inventario.getIdReporte());
        assertEquals(fecha, sucursal.getFechaRegistro());
        assertEquals(null, sucursal.getIdReporte());
        assertEquals(0, ventas.getTotal());
        assertEquals(null, ventas.getIdReporte());
    }

    @Test
    void reporteInventarioIdentificaStockBajoYReporteVentasExponeSucursalPorDefecto() {
        ReporteInventario inventario = new ReporteInventario();
        inventario.setStockBajo(7);
        ReporteVentas ventas = new ReporteVentas();

        assertEquals(7, inventario.identificarStockBajo());
        assertEquals(0, ventas.getIdSucursal());
    }

    @Test
    void validacionesDetectanCamposObligatorios() {
        Reportes reporte = new Reportes();
        Metrica metrica = new Metrica();
        ExportacionReporte exportacion = new ExportacionReporte();

        Set<ConstraintViolation<Reportes>> erroresReporte = validator.validate(reporte);
        Set<ConstraintViolation<Metrica>> erroresMetrica = validator.validate(metrica);
        Set<ConstraintViolation<ExportacionReporte>> erroresExportacion = validator.validate(exportacion);

        assertTrue(erroresReporte.size() >= 4);
        assertTrue(erroresMetrica.size() >= 2);
        assertTrue(erroresExportacion.size() >= 2);
    }

    private Reportes reporteBase() {
        Reportes reporte = new Reportes();
        completarBase(reporte);
        reporte.setIdReporte(1L);
        return reporte;
    }

    private void completarBase(Reportes reporte) {
        reporte.setFechaReporte("2026-06-16");
        reporte.setRazonReporte("Revision mensual");
        reporte.setDescripcionReporte("Indicadores del mes");
        reporte.setEstadoReporte("Pendiente");
        reporte.setEstado("Pendiente");
        reporte.setTitulo("Reporte mensual");
        reporte.setTipo("General");
        reporte.setFormato("JSON");
    }
}
