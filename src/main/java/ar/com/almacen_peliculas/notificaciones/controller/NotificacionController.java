package ar.com.almacen_peliculas.notificaciones.controller;

import ar.com.almacen_peliculas.notificaciones.domain.models.CompraEvent;
import ar.com.almacen_peliculas.notificaciones.domain.models.Destinatario;
import ar.com.almacen_peliculas.notificaciones.domain.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificationService notificationService;

    public NotificacionController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/enviar")
    public ResponseEntity<Map<String, Object>> enviarNotificacion(@RequestBody CompraEvent event) {
        notificationService.sendPurchaseEmail(event);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Notificación enviada exitosamente");
        response.put("idCompra", event.idCompra());
        response.put("emailCliente", event.emailCliente());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/destinatarios")
    public ResponseEntity<List<Destinatario>> obtenerDestinatarios() {
        List<Destinatario> destinatarios = notificationService.obtenerTodosLosDestinatarios();
        return ResponseEntity.ok(destinatarios);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "notificaciones");
        return ResponseEntity.ok(response);
    }
}
