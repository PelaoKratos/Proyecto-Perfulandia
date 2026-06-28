package perfulandia.pago.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class ModelosDiagramaPagoTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void metodoPagoValidaActivaYDesactiva() {
        MetodoPago metodo = new MetodoPago(1L, "TARJETA", "Pago con tarjeta", true);

        assertTrue(metodo.validarMetodo());

        metodo.desactivar();
        assertFalse(metodo.validarMetodo());

        metodo.activar();
        assertTrue(metodo.isActivo());

        metodo.setTipo(" ");
        assertFalse(metodo.validarMetodo());

        metodo.setTipo("TARJETA");
        metodo.setDescripcion("");
        assertFalse(metodo.validarMetodo());
    }

    @Test
    void transaccionRegistraConsultaYAnula() {
        TransaccionPago transaccion = new TransaccionPago();
        transaccion.setIdPago(1L);
        transaccion.setCodigoOperacion("OP-1");
        transaccion.setMonto(49990.0);

        transaccion.registrarTransaccion();

        assertNotNull(transaccion.getFechaTransaccion());
        assertEquals(TransaccionPago.ESTADO_REGISTRADA, transaccion.consultarEstado());

        transaccion.anularTransaccion();
        assertEquals(TransaccionPago.ESTADO_ANULADA, transaccion.getEstado());
    }

    @Test
    void transaccionPrePersistCompletaFechaYEstadoCuandoFaltan() {
        TransaccionPago transaccion = transaccionValida();
        transaccion.setFechaTransaccion(null);
        transaccion.setEstado(" ");

        transaccion.prePersist();

        assertNotNull(transaccion.getFechaTransaccion());
        assertEquals(TransaccionPago.ESTADO_REGISTRADA, transaccion.getEstado());

        TransaccionPago transaccionConEstadoNulo = transaccionValida();
        transaccionConEstadoNulo.setEstado(null);

        transaccionConEstadoNulo.prePersist();

        assertEquals(TransaccionPago.ESTADO_REGISTRADA, transaccionConEstadoNulo.getEstado());
    }

    @Test
    void transaccionPrePersistRespetaFechaYEstadoExistentes() {
        LocalDateTime fecha = LocalDateTime.of(2026, 6, 22, 10, 30);
        TransaccionPago transaccion = transaccionValida();
        transaccion.setFechaTransaccion(fecha);
        transaccion.setEstado(TransaccionPago.ESTADO_ANULADA);

        transaccion.prePersist();
        transaccion.registrarTransaccion();

        assertEquals(fecha, transaccion.getFechaTransaccion());
        assertEquals(TransaccionPago.ESTADO_REGISTRADA, transaccion.getEstado());
    }

    @Test
    void transaccionValidaCamposObligatorios() {
        TransaccionPago transaccion = new TransaccionPago();

        Set<ConstraintViolation<TransaccionPago>> errores = validator.validate(transaccion);

        assertFalse(errores.isEmpty());
    }

    @Test
    void comprobanteGeneraEnviaYAnula() {
        ComprobantePago comprobante = new ComprobantePago();
        comprobante.setIdPago(1L);
        comprobante.setNumero("CP-1");
        comprobante.setTotal(49990.0);

        comprobante.generarComprobante();

        assertNotNull(comprobante.getFechaEmision());
        assertEquals(ComprobantePago.ESTADO_GENERADO, comprobante.getEstado());

        comprobante.enviarComprobante();
        assertEquals(ComprobantePago.ESTADO_ENVIADO, comprobante.getEstado());

        comprobante.anularComprobante();
        assertEquals(ComprobantePago.ESTADO_ANULADO, comprobante.getEstado());
    }

    @Test
    void comprobantePrePersistCompletaFechaYEstadoCuandoFaltan() {
        ComprobantePago comprobante = comprobanteValido();
        comprobante.setFechaEmision(null);
        comprobante.setEstado(null);

        comprobante.prePersist();

        assertNotNull(comprobante.getFechaEmision());
        assertEquals(ComprobantePago.ESTADO_GENERADO, comprobante.getEstado());

        ComprobantePago comprobanteConEstadoEnBlanco = comprobanteValido();
        comprobanteConEstadoEnBlanco.setEstado(" ");

        comprobanteConEstadoEnBlanco.prePersist();

        assertEquals(ComprobantePago.ESTADO_GENERADO, comprobanteConEstadoEnBlanco.getEstado());
    }

    @Test
    void comprobantePrePersistRespetaFechaYEstadoExistentes() {
        LocalDateTime fecha = LocalDateTime.of(2026, 6, 22, 11, 15);
        ComprobantePago comprobante = comprobanteValido();
        comprobante.setFechaEmision(fecha);
        comprobante.setEstado(ComprobantePago.ESTADO_ENVIADO);

        comprobante.prePersist();
        comprobante.generarComprobante();

        assertEquals(fecha, comprobante.getFechaEmision());
        assertEquals(ComprobantePago.ESTADO_GENERADO, comprobante.getEstado());
    }

    @Test
    void comprobanteValidaCamposObligatorios() {
        ComprobantePago comprobante = new ComprobantePago();

        Set<ConstraintViolation<ComprobantePago>> errores = validator.validate(comprobante);

        assertFalse(errores.isEmpty());
    }

    private TransaccionPago transaccionValida() {
        TransaccionPago transaccion = new TransaccionPago();
        transaccion.setIdPago(1L);
        transaccion.setCodigoOperacion("OP-1");
        transaccion.setFechaTransaccion(LocalDateTime.of(2026, 6, 22, 9, 0));
        transaccion.setMonto(49990.0);
        transaccion.setEstado(TransaccionPago.ESTADO_REGISTRADA);
        return transaccion;
    }

    private ComprobantePago comprobanteValido() {
        ComprobantePago comprobante = new ComprobantePago();
        comprobante.setIdPago(1L);
        comprobante.setNumero("CP-1");
        comprobante.setFechaEmision(LocalDateTime.of(2026, 6, 22, 9, 0));
        comprobante.setTotal(49990.0);
        comprobante.setEstado(ComprobantePago.ESTADO_GENERADO);
        return comprobante;
    }
}
