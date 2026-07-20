package ar.com.almacen_peliculas.notificaciones.controller.listeners;

import ar.com.almacen_peliculas.notificaciones.domain.models.CompraEvent;
import ar.com.almacen_peliculas.notificaciones.domain.services.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PurchaseEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(PurchaseEventConsumer.class);

    private final NotificationService notificationService;

    public PurchaseEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void handlePurchaseEvent(CompraEvent event) {
        log.info("Evento de compra recibido desde RabbitMQ: ID Compra -> {}", event.idCompra());
        
        try {
            notificationService.sendPurchaseEmail(event);
            log.info("Evento de compra {} procesado exitosamente", event.idCompra());
        } catch (Exception e) {
            log.error("Error procesando el evento de compra {}: {}", event.idCompra(), e.getMessage());
            throw e; // Lanza la excepción para que RabbitMQ pueda reintentar o enviar a DLQ según configuración
        }
    }
}
