package ar.com.almacen_peliculas.notificaciones.domain.services;

import ar.com.almacen_peliculas.notificaciones.domain.models.CompraEvent;

public interface NotificationService {
    void sendPurchaseEmail(CompraEvent event);
}
