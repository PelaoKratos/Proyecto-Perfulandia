package microservice.reporte.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import microservice.reporte.model.ExportacionReporte;
import microservice.reporte.model.Metrica;
import microservice.reporte.model.ReporteInventario;
import microservice.reporte.model.ReporteSucursal;
import microservice.reporte.model.ReporteVentas;
import microservice.reporte.model.Reportes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ReporteRepositoryTest {

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private ReporteVentasRepository reporteVentasRepository;

    @Autowired
    private ReporteInventarioRepository reporteInventarioRepository;

    @Autowired
    private ReporteSucursalRepository reporteSucursalRepository;

    @Autowired
    private MetricaRepository metricaRepository;

    @Autowired
    private ExportacionReporteRepository exportacionReporteRepository;

    @BeforeEach
    void limpiarDatos() {
        exportacionReporteRepository.deleteAll();
        metricaRepository.deleteAll();
        reporteVentasRepository.deleteAll();
        reporteInventarioRepository.deleteAll();
        reporteSucursalRepository.deleteAll();
        reporteRepository.deleteAll();
    }

    @Test
    void reporteRepositoryDebeGuardarYBuscarReporteBase() {
        Reportes guardado = reporteRepository.save(crearReporte());

        assertTrue(reporteRepository.findById(guardado.getIdReporte()).isPresent());
        assertEquals("General", guardado.getTipo());
    }

    @Test
    void reporteVentasRepositoryDebePersistirReporteEspecializado() {
        ReporteVentas guardado = reporteVentasRepository.save(crearReporteVentas());

        assertEquals(150000, guardado.calcularTotales());
        assertTrue(reporteVentasRepository.findById(guardado.getIdReporte()).isPresent());
    }

    @Test
    void reporteInventarioRepositoryDebePersistirReporteEspecializado() {
        ReporteInventario guardado = reporteInventarioRepository.save(crearReporteInventario());

        assertEquals(92, guardado.calcularStock());
        assertTrue(reporteInventarioRepository.findById(guardado.getIdReporte()).isPresent());
    }

    @Test
    void reporteSucursalRepositoryDebePersistirReporteEspecializado() {
        ReporteSucursal guardado = reporteSucursalRepository.save(crearReporteSucursal());

        assertEquals(2.5, guardado.compararMetricas(90));
        assertTrue(reporteSucursalRepository.findById(guardado.getIdReporte()).isPresent());
    }

    @Test
    void metricaRepositoryDebeBuscarPorReporte() {
        Reportes reporte = reporteRepository.save(crearReporte());
        Metrica metrica = crearMetrica();
        metrica.setReporte(reporte);
        metricaRepository.save(metrica);

        List<Metrica> resultado = metricaRepository.findByReporteIdReporte(reporte.getIdReporte());

        assertEquals(1, resultado.size());
        assertEquals("Ventas", resultado.get(0).getNombre());
    }

    @Test
    void exportacionReporteRepositoryDebeBuscarPorReporte() {
        Reportes reporte = reporteRepository.save(crearReporte());
        ExportacionReporte exportacion = crearExportacion();
        exportacion.setReporte(reporte);
        exportacionReporteRepository.save(exportacion);

        List<ExportacionReporte> resultado = exportacionReporteRepository.findByReporteIdReporte(reporte.getIdReporte());

        assertEquals(1, resultado.size());
        assertEquals("PDF", resultado.get(0).getFormato());
    }

    private Reportes crearReporte() {
        Reportes reporte = new Reportes();
        completarBase(reporte, "General");
        return reporte;
    }

    private ReporteVentas crearReporteVentas() {
        ReporteVentas reporte = new ReporteVentas();
        completarBase(reporte, "Ventas");
        reporte.setTotalVentas(150000);
        reporte.setCantidadVentas(12);
        reporte.setPeriodo("2026-06");
        reporte.setIdSucursal(1);
        return reporte;
    }

    private ReporteInventario crearReporteInventario() {
        ReporteInventario reporte = new ReporteInventario();
        completarBase(reporte, "Inventario");
        reporte.setTotalProductos(100);
        reporte.setStockBajo(8);
        reporte.setMovimientos(20);
        return reporte;
    }

    private ReporteSucursal crearReporteSucursal() {
        ReporteSucursal reporte = new ReporteSucursal();
        completarBase(reporte, "Sucursal");
        reporte.setIdSucursal(1);
        reporte.setNombreSucursal("Sucursal Centro");
        reporte.setVentasSucursal(250000);
        reporte.setRendimiento(92.5);
        return reporte;
    }

    private void completarBase(Reportes reporte, String tipo) {
        reporte.setFechaReporte("2026-06-12");
        reporte.setRazonReporte("Reporte mensual");
        reporte.setDescripcionReporte("Revision de indicadores");
        reporte.setEstadoReporte("Pendiente");
        reporte.setTitulo("Reporte mensual");
        reporte.setTipo(tipo);
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
        exportacion.setEstado("Exportado");
        exportacion.setRutaArchivo("reportes/1.pdf");
        return exportacion;
    }
}
