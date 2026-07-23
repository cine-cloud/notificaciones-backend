package ar.com.almacen_peliculas.notificaciones.data.services;

import ar.com.almacen_peliculas.notificaciones.data.repositories.DestinatarioRepository;
import ar.com.almacen_peliculas.notificaciones.domain.models.CompraEvent;
import ar.com.almacen_peliculas.notificaciones.domain.models.Destinatario;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailNotificationServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private DestinatarioRepository destinatarioRepository;

    @InjectMocks
    private EmailNotificationServiceImpl notificationService;

    private CompraEvent compraEvent;

    @BeforeEach
    void setUp() {
        CompraEvent.Producto prod = new CompraEvent.Producto(
                "prod-1",
                "Avatar",
                1,
                new BigDecimal("2500.00"),
                new BigDecimal("2500.00")
        );

        compraEvent = new CompraEvent(
                "compra-500",
                LocalDateTime.now(),
                "cliente@correo.com",
                "Carlos Tevez",
                List.of(prod),
                new BigDecimal("2500.00")
        );
    }

    @Test
    void sendPurchaseEmail_exitoso_enviaCorreoYGuardaDestinatario() {
        // Setup
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Ejercitación
        notificationService.sendPurchaseEmail(compraEvent);

        // Verificación
        verify(javaMailSender, times(1)).send(mimeMessage);
        verify(destinatarioRepository, times(1)).save(any(Destinatario.class));
    }

    @Test
    void sendPurchaseEmail_errorMailSender_lanzaExcepcion() {
        // Setup
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new MailSendException("Error SMTP")).when(javaMailSender).send(any(MimeMessage.class));

        // Ejercitación & Verificación
        assertThrows(RuntimeException.class, () -> notificationService.sendPurchaseEmail(compraEvent),
                "Debe lanzar una RuntimeException cuando falla el envío de correo");
    }

    @Test
    void obtenerTodosLosDestinatarios_retornaListaDestinatarios() {
        // Setup
        Destinatario dest = new Destinatario("compra-500", "Carlos Tevez", "cliente@correo.com");
        when(destinatarioRepository.findAll()).thenReturn(List.of(dest));

        // Ejercitación
        List<Destinatario> resultado = notificationService.obtenerTodosLosDestinatarios();

        // Verificación
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("cliente@correo.com", resultado.get(0).getEmail());
        verify(destinatarioRepository, times(1)).findAll();
    }
}
