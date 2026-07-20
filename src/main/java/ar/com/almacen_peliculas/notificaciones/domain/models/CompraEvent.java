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
        BigDecimal total
) {
    public record Producto(
            String idProducto,
            String nombre,
            Integer cantidad,
            BigDecimal precioUnitario,
            BigDecimal subtotal
    ) {}
}
