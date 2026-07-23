package ar.com.almacen_peliculas.notificaciones.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ar.com.almacen_peliculas.notificaciones.domain.models.CompraEvent;
import ar.com.almacen_peliculas.notificaciones.domain.models.Destinatario;
import ar.com.almacen_peliculas.notificaciones.domain.services.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificacionController.class)
class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testEnviarNotificacion_exitoso() throws Exception {
        // Setup
        CompraEvent event = new CompraEvent(
                "compra-88",
                LocalDateTime.now(),
                "ana@test.com",
                "Ana Lopez",
                List.of(),
                new BigDecimal("1200.00")
        );

        doNothing().when(notificationService).sendPurchaseEmail(any(CompraEvent.class));

        // Act & Assert
        mockMvc.perform(post("/api/notificaciones/enviar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Notificación enviada exitosamente"))
                .andExpect(jsonPath("$.idCompra").value("compra-88"))
                .andExpect(jsonPath("$.emailCliente").value("ana@test.com"));
    }

    @Test
    void testObtenerDestinatarios_exitoso() throws Exception {
        // Setup
        Destinatario dest1 = new Destinatario("c-1", "Ana Lopez", "ana@test.com");
        Destinatario dest2 = new Destinatario("c-2", "Pedro Martinez", "pedro@test.com");

        when(notificationService.obtenerTodosLosDestinatarios()).thenReturn(List.of(dest1, dest2));

        // Act & Assert
        mockMvc.perform(get("/api/notificaciones/destinatarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].usuario").value("Ana Lopez"))
                .andExpect(jsonPath("$[1].usuario").value("Pedro Martinez"));
    }

    @Test
    void testHealthCheck_exitoso() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/notificaciones/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("notificaciones"));
    }
}
