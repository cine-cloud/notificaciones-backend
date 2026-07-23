package ar.com.almacen_peliculas.notificaciones.controller.listeners;

import ar.com.almacen_peliculas.notificaciones.domain.models.CompraEvent;
import ar.com.almacen_peliculas.notificaciones.domain.services.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseEventConsumerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private PurchaseEventConsumer purchaseEventConsumer;

    private CompraEvent event;

    @BeforeEach
    void setUp() {
        event = new CompraEvent(
                "compra-1",
                LocalDateTime.now(),
                "test@correo.com",
                "Usuario Test",
                List.of(),
                new BigDecimal("1000.00")
        );
    }

    @Test
    void handlePurchaseEvent_eventoValido_invocaServicioExitosamente() {
        // Ejercitación
        purchaseEventConsumer.handlePurchaseEvent(event);

        // Verificación
        verify(notificationService, times(1)).sendPurchaseEmail(event);
    }

    @Test
    void handlePurchaseEvent_errorEnServicio_reflotaExcepcion() {
        // Setup
        doThrow(new RuntimeException("Error simulado")).when(notificationService).sendPurchaseEmail(event);

        // Ejercitación & Verificación
        assertThrows(RuntimeException.class, () -> purchaseEventConsumer.handlePurchaseEvent(event),
                "Debe relanzar la excepción para el manejo de reintentos en RabbitMQ");
        verify(notificationService, times(1)).sendPurchaseEmail(event);
    }
}
