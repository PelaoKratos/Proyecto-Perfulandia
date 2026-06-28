package microservice.ventas.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class DescuentoStockRequestTest {

    @Test
    void creaDtoConDatosDeDescuento() {
        DescuentoStockRequest request = new DescuentoStockRequest(null, 1L, 2L, null, 99L, 3);

        assertEquals(1L, request.getProductoId());
        assertEquals(2L, request.getSucursalId());
        assertEquals(99L, request.getVentaId());
        assertEquals(3, request.getCantidad());
    }

    @Test
    void settersActualizanDatosDelDto() {
        DescuentoStockRequest request = new DescuentoStockRequest();

        request.setProductoId(10L);
        request.setSucursalId(20L);
        request.setVentaId(30L);
        request.setCantidad(5);

        assertEquals(10L, request.getProductoId());
        assertEquals(20L, request.getSucursalId());
        assertEquals(30L, request.getVentaId());
        assertEquals(5, request.getCantidad());
    }

    @Test
    void metodosGeneradosPorLombokFuncionan() {
        DescuentoStockRequest request = new DescuentoStockRequest(null, 1L, 2L, null, 99L, 3);
        DescuentoStockRequest mismoRequest = new DescuentoStockRequest(null, 1L, 2L, null, 99L, 3);
        DescuentoStockRequest otroRequest = new DescuentoStockRequest(null, 4L, 2L, null, 99L, 3);

        assertEquals(request, mismoRequest);
        assertEquals(request.hashCode(), mismoRequest.hashCode());
        assertNotEquals(request, otroRequest);
        assertNotNull(request.toString());
    }
}
