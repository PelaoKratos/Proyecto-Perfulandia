package microservice.reporte.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import microservice.reporte.dto.ResumenMicroservicios;
import microservice.reporte.model.DetalleInventario;
import microservice.reporte.model.DetalleSucursal;
import microservice.reporte.model.DetalleVentas;
import microservice.reporte.model.ExportacionReporte;
import microservice.reporte.model.Metrica;
import microservice.reporte.model.ReporteInventario;
import microservice.reporte.model.ReporteSucursal;
import microservice.reporte.model.ReporteVentas;
import microservice.reporte.model.Reportes;
import microservice.reporte.service.ReporteIntegracionService;
import microservice.reporte.service.ReporteService;

@RestController
@RequestMapping("api/v1/reportes")
public class ReporteController {
    @Autowired
    private ReporteService reporteService;

    @Autowired
    private ReporteIntegracionService reporteIntegracionService;


    @PostMapping
    public Reportes postReporte(@Valid @RequestBody Reportes reportes) {
        return reporteService.crearReportes(reportes);
    }

    @GetMapping
    public List<Reportes> getReportes() {
        return reporteService.obtenerReportes();
    }

    @PutMapping("{id}")
    public Reportes putReportes(@PathVariable Long id, @Valid @RequestBody Reportes reportes) {
        return reporteService.updateReportes(id, reportes);
    }

    @DeleteMapping("{id}")
    public void eliminarReporte(@PathVariable Long id) {
        reporteService.eliminarReporte(id);
    }

    @PostMapping("ventas")
    public ReporteVentas postReporteVentas(@Valid @RequestBody ReporteVentas reporteVentas) {
        return reporteService.generarReporteVentas(reporteVentas);
    }

    @GetMapping("ventas")
    public List<ReporteVentas> getReportesVentas() {
        return reporteService.obtenerReportesVentas();
    }

    @PostMapping("inventario")
    public ReporteInventario postReporteInventario(@Valid @RequestBody ReporteInventario reporteInventario) {
        return reporteService.generarReporteInventario(reporteInventario);
    }

    @GetMapping("inventario")
    public List<ReporteInventario> getReportesInventario() {
        return reporteService.obtenerReportesInventario();
    }

    @PostMapping("sucursal")
    public ReporteSucursal postReporteSucursal(@Valid @RequestBody ReporteSucursal reporteSucursal) {
        return reporteService.generarReporteSucursal(reporteSucursal);
    }

    @GetMapping("sucursal")
    public List<ReporteSucursal> getReportesSucursal() {
        return reporteService.obtenerReportesSucursal();
    }

    @PostMapping("{id}/metricas")
    public Metrica postMetrica(@PathVariable Long id, @Valid @RequestBody Metrica metrica) {
        return reporteService.agregarMetrica(id, metrica);
    }

    @GetMapping("{id}/metricas")
    public List<Metrica> getMetricas(@PathVariable Long id) {
        return reporteService.obtenerMetricasPorReporte(id);
    }

    @PutMapping("metricas/{id}")
    public Metrica putMetrica(@PathVariable Long id, @Valid @RequestBody Metrica metrica) {
        return reporteService.actualizarMetrica(id, metrica);
    }

    @PostMapping("{id}/exportaciones")
    public ExportacionReporte postExportacion(
            @PathVariable Long id,
            @Valid @RequestBody ExportacionReporte exportacionReporte) {
        return reporteService.exportarReporte(id, exportacionReporte);
    }

    @GetMapping("{id}/exportaciones")
    public List<ExportacionReporte> getExportaciones(@PathVariable Long id) {
        return reporteService.obtenerExportacionesPorReporte(id);
    }

    @PostMapping("ventas/{id}/detalles")
    public DetalleVentas postDetalleVentas(@PathVariable Long id, @Valid @RequestBody DetalleVentas detalle) {
        return reporteService.agregarDetalleVentas(id, detalle);
    }

    @GetMapping("ventas/{id}/detalles")
    public List<DetalleVentas> getDetallesVentas(@PathVariable Long id) {
        return reporteService.obtenerDetallesVentas(id);
    }

    @PostMapping("inventario/{id}/detalles")
    public DetalleInventario postDetalleInventario(@PathVariable Long id, @Valid @RequestBody DetalleInventario detalle) {
        return reporteService.agregarDetalleInventario(id, detalle);
    }

    @GetMapping("inventario/{id}/detalles")
    public List<DetalleInventario> getDetallesInventario(@PathVariable Long id) {
        return reporteService.obtenerDetallesInventario(id);
    }

    @PostMapping("sucursal/{id}/detalles")
    public DetalleSucursal postDetalleSucursal(@PathVariable Long id, @Valid @RequestBody DetalleSucursal detalle) {
        return reporteService.agregarDetalleSucursal(id, detalle);
    }

    @GetMapping("sucursal/{id}/detalles")
    public List<DetalleSucursal> getDetallesSucursal(@PathVariable Long id) {
        return reporteService.obtenerDetallesSucursal(id);
    }

    @GetMapping("integraciones/resumen")
    public ResumenMicroservicios getResumenMicroservicios() {
        return reporteIntegracionService.obtenerResumenMicroservicios();
    }
}
