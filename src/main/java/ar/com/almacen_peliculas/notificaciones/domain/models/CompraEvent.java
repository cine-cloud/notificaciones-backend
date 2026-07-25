package ar.com.almacen_peliculas.notificaciones.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CompraEvent(
        @JsonProperty("idCompra") String idCompra,
        @JsonProperty("fecha") LocalDateTime fecha,
        @JsonProperty("emailCliente") String emailCliente,
        @JsonProperty("nombreCliente") String nombreCliente,
        @JsonProperty("productos") List<Producto> productos,
        @JsonProperty("subtotal") BigDecimal subtotal,
        @JsonProperty("descuentoMonto") BigDecimal descuentoMonto,
        @JsonProperty("total") BigDecimal total
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Producto(
            @JsonProperty("idProducto") String idProducto,
            @JsonProperty("nombre") String nombre,
            @JsonProperty("cantidad") Integer cantidad,
            @JsonProperty("precioUnitario") BigDecimal precioUnitario,
            @JsonProperty("subtotal") BigDecimal subtotal
    ) {}
}
