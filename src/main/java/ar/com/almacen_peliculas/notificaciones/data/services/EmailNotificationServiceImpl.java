package ar.com.almacen_peliculas.notificaciones.data.services;

import ar.com.almacen_peliculas.notificaciones.data.repositories.DestinatarioRepository;
import ar.com.almacen_peliculas.notificaciones.domain.models.CompraEvent;
import ar.com.almacen_peliculas.notificaciones.domain.models.Destinatario;
import ar.com.almacen_peliculas.notificaciones.domain.services.NotificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailNotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationServiceImpl.class);

    private final JavaMailSender javaMailSender;
    private final DestinatarioRepository destinatarioRepository;

    public EmailNotificationServiceImpl(JavaMailSender javaMailSender, DestinatarioRepository destinatarioRepository) {
        this.javaMailSender = javaMailSender;
        this.destinatarioRepository = destinatarioRepository;
    }

    @Override
    public void sendPurchaseEmail(CompraEvent event) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(event.emailCliente());
            helper.setSubject("¡Confirmación de tu compra #" + event.idCompra() + "!");
            
            String htmlContent = buildHtmlContent(event);
            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
            log.info("Email enviado exitosamente a {}", event.emailCliente());

            // Registrar destinatario enviado en la base de datos
            Destinatario destinatario = new Destinatario(
                    event.idCompra(),
                    event.nombreCliente() != null ? event.nombreCliente() : "Cliente",
                    event.emailCliente()
            );
            destinatarioRepository.save(destinatario);

        } catch (MessagingException e) {
            log.error("Fallo al construir o enviar el email para la compra {}: {}", event.idCompra(), e.getMessage(), e);
            throw new RuntimeException("Error al enviar email de notificación", e);
        }
    }

    @Override
    public List<Destinatario> obtenerTodosLosDestinatarios() {
        return destinatarioRepository.findAll();
    }

    private String buildHtmlContent(CompraEvent event) {
        StringBuilder html = new StringBuilder();
        html.append("<html>")
            .append("<body style='font-family: Arial, sans-serif; color: #333;'>")
            .append("<h2 style='color: #2c3e50;'>¡Hola, ").append(event.nombreCliente()).append("!</h2>")
            .append("<p>Gracias por tu compra en Almacén de Películas. A continuación, te detallamos tu pedido (<strong>#")
            .append(event.idCompra()).append("</strong>):</p>")
            .append("<table style='width: 100%; border-collapse: collapse; margin-bottom: 20px;'>")
            .append("<thead>")
            .append("<tr style='background-color: #ecf0f1; text-align: left;'>")
            .append("<th style='padding: 10px; border: 1px solid #ddd;'>Producto</th>")
            .append("<th style='padding: 10px; border: 1px solid #ddd;'>Cantidad</th>")
            .append("<th style='padding: 10px; border: 1px solid #ddd;'>Precio Unitario</th>")
            .append("<th style='padding: 10px; border: 1px solid #ddd;'>Subtotal</th>")
            .append("</tr>")
            .append("</thead>")
            .append("<tbody>");

        for (CompraEvent.Producto prod : event.productos()) {
            html.append("<tr>")
                .append("<td style='padding: 10px; border: 1px solid #ddd;'>").append(prod.nombre()).append("</td>")
                .append("<td style='padding: 10px; border: 1px solid #ddd;'>").append(prod.cantidad()).append("</td>")
                .append("<td style='padding: 10px; border: 1px solid #ddd;'>$").append(prod.precioUnitario()).append("</td>")
                .append("<td style='padding: 10px; border: 1px solid #ddd;'>$").append(prod.subtotal()).append("</td>")
                .append("</tr>");
        }

        html.append("</tbody>")
            .append("</table>");

        java.math.BigDecimal descMonto = event.descuentoMonto();
        java.math.BigDecimal subtotal = event.subtotal();

        if (descMonto != null && descMonto.compareTo(java.math.BigDecimal.ZERO) > 0) {
            if (subtotal == null || subtotal.compareTo(event.total()) == 0) {
                subtotal = event.total().add(descMonto);
            }
            html.append("<p style='margin: 5px 0; font-size: 1.1em;'>Subtotal: <strong>$").append(subtotal).append("</strong></p>")
                .append("<p style='margin: 5px 0; font-size: 1.1em; color: #e74c3c;'>Descuento Aplicado: <strong>-$").append(descMonto).append("</strong></p>");
        }

        html.append("<h3 style='margin-top: 10px;'>Total Pagado: <span style='color: #27ae60;'>$").append(event.total()).append("</span></h3>")
            .append("<p>Si tienes alguna consulta, no dudes en responder a este correo.</p>")
            .append("<p>¡Que disfrutes tu película!</p>")
            .append("</body>")
            .append("</html>");

        return html.toString();
    }
}
