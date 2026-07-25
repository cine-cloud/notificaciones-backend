package ar.com.almacen_peliculas.notificaciones.domain.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CompraEvent(
        String idCompra,
        LocalDateTime fecha,
        String emailCliente,
        String nombreCliente,
        List<Producto> productos,
        BigDecimal subtotal,
        BigDecimal descuentoMonto,
        BigDecimal total
) {
    public CompraEvent(
            String idCompra,
            LocalDateTime fecha,
            String emailCliente,
            String nombreCliente,
            List<Producto> productos,
            BigDecimal total
    ) {
        this(idCompra, fecha, emailCliente, nombreCliente, productos, total, BigDecimal.ZERO, total);
    }
    public record Producto(
            String idProducto,
            String nombre,
            Integer cantidad,
            BigDecimal precioUnitario,
            BigDecimal subtotal
    ) {}
}
