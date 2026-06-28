package perfulandia.pago.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class PagoModelTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validarPagoRetornaTrueConDatosValidos() {
        Pago pago = pagoValido();

        assertTrue(pago.validarPago());
    }

    @Test
    void validarPagoRetornaFalseConMontoInvalido() {
        Pago pago = pagoValido();
        pago.setMonto(0.0);

        assertFalse(pago.validarPago());
    }

    @Test
    void validarPagoRetornaFalseConCamposObligatoriosFaltantes() {
        Pago pago = pagoValido();

        pago.setIdVenta(null);
        assertFalse(pago.validarPago());

        pago = pagoValido();
        pago.setIdCliente(null);
        assertFalse(pago.validarPago());

        pago = pagoValido();
        pago.setMonto(null);
        assertFalse(pago.validarPago());

        pago = pagoValido();
        pago.setIdEmpleado(null);
        assertFalse(pago.validarPago());

        pago = pagoValido();
        pago.setIdMetodoPago(null);
        assertFalse(pago.validarPago());
    }

    @Test
    void procesarDejaPagoPendiente() {
        Pago pago = pagoValido();

        pago.procesar();

        assertEquals(Pago.ESTADO_PENDIENTE, pago.getEstado());
        assertEquals("Pago en proceso", pago.getMensaje());
    }

    @Test
    void procesarRechazaPagoInvalido() {
        Pago pago = pagoValido();
        pago.setIdMetodoPago(null);

        pago.procesar();

        assertEquals(Pago.ESTADO_RECHAZADO, pago.getEstado());
        assertEquals("Datos de pago invalidos", pago.getMensaje());
    }

    @Test
    void metodosDelDiagramaActualizanEstado() {
        Pago pago = pagoValido();

        pago.realizarPago();
        assertEquals(Pago.ESTADO_PENDIENTE, pago.getEstado());

        pago.setCodigoTransaccion("TX-123");
        pago.confirmarPago();
        assertEquals(Pago.ESTADO_CONFIRMADO, pago.getEstado());

        pago.setMensaje("Rechazado");
        pago.rechazarPago();
        assertEquals(Pago.ESTADO_RECHAZADO, pago.getEstado());
    }

    @Test
    void confirmarPagoGuardaCodigoYEstado() {
        Pago pago = pagoValido();

        pago.confirmar("TX-123");

        assertEquals(Pago.ESTADO_CONFIRMADO, pago.getEstado());
        assertEquals("TX-123", pago.getCodigoTransaccion());
        assertEquals("Pago confirmado", pago.getMensaje());
    }

    @Test
    void rechazarPagoGuardaMotivo() {
        Pago pago = pagoValido();

        pago.rechazar("Fondos insuficientes");

        assertEquals(Pago.ESTADO_RECHAZADO, pago.getEstado());
        assertEquals("Fondos insuficientes", pago.getMensaje());
    }

    @Test
    void anularPagoGuardaMotivo() {
        Pago pago = pagoValido();

        pago.anular("Solicitud del cliente");

        assertEquals(Pago.ESTADO_ANULADO, pago.getEstado());
        assertEquals("Solicitud del cliente", pago.getMensaje());
    }

    @Test
    void prePersistCompletaFechaYEstado() {
        Pago pago = pagoValido();
        pago.setEstado(null);
        pago.setFechaPago(null);

        pago.prePersist();

        assertEquals(Pago.ESTADO_PENDIENTE, pago.getEstado());
        assertNotNull(pago.getFechaPago());
    }

    @Test
    void validacionesRechazanCamposObligatorios() {
        Pago pago = new Pago();

        Set<ConstraintViolation<Pago>> errores = validator.validate(pago);

        assertFalse(errores.isEmpty());
    }

    private Pago pagoValido() {
        Pago pago = new Pago();
        pago.setIdVenta(1L);
        pago.setIdCliente(2L);
        pago.setIdEmpleado(3L);
        pago.setIdMetodoPago(4L);
        pago.setMonto(49990.0);
        pago.setMetodoPago("TARJETA");
        pago.setEstado(Pago.ESTADO_PENDIENTE);
        return pago;
    }
}
