package ar.com.almacen_peliculas.notificaciones.domain.services;

import ar.com.almacen_peliculas.notificaciones.domain.models.CompraEvent;
import ar.com.almacen_peliculas.notificaciones.domain.models.Destinatario;

import java.util.List;

public interface NotificationService {
    void sendPurchaseEmail(CompraEvent event);
    List<Destinatario> obtenerTodosLosDestinatarios();
}
