package ar.com.almacen_peliculas.notificaciones.domain.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompraEventTest {

    @Test
    void crearCompraEvent_valoresValidos_creacionExitosa() {
        // Setup
        LocalDateTime fecha = LocalDateTime.now();
        CompraEvent.Producto producto = new CompraEvent.Producto(
                "prod-1",
                "Matrix",
                2,
                new BigDecimal("1500.00"),
                new BigDecimal("3000.00")
        );

        // Ejercitación
        CompraEvent event = new CompraEvent(
                "compra-100",
                fecha,
                "juan@example.com",
                "Juan Perez",
                List.of(producto),
                new BigDecimal("3000.00")
        );

        // Verificación
        assertEquals("compra-100", event.idCompra(), "El ID de la compra debe coincidir");
        assertEquals(fecha, event.fecha(), "La fecha debe coincidir");
        assertEquals("juan@example.com", event.emailCliente(), "El email del cliente debe coincidir");
        assertEquals("Juan Perez", event.nombreCliente(), "El nombre del cliente debe coincidir");
        assertEquals(new BigDecimal("3000.00"), event.total(), "El total debe coincidir");
        assertNotNull(event.productos(), "La lista de productos no debe ser nula");
        assertEquals(1, event.productos().size(), "Debe contener 1 producto");

        CompraEvent.Producto prodExtraido = event.productos().get(0);
        assertEquals("prod-1", prodExtraido.idProducto(), "El ID del producto debe coincidir");
        assertEquals("Matrix", prodExtraido.nombre(), "El nombre del producto debe coincidir");
        assertEquals(2, prodExtraido.cantidad(), "La cantidad debe coincidir");
        assertEquals(new BigDecimal("1500.00"), prodExtraido.precioUnitario(), "El precio unitario debe coincidir");
        assertEquals(new BigDecimal("3000.00"), prodExtraido.subtotal(), "El subtotal debe coincidir");
    }
}
