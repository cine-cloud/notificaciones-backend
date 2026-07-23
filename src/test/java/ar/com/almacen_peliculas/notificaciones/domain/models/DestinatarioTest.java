package ar.com.almacen_peliculas.notificaciones.domain.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DestinatarioTest {

    @Test
    void crearDestinatario_conParametros_creacionExitosa() {
        // Setup & Ejercitación
        Destinatario destinatario = new Destinatario("dest-1", "maria_gomez", "maria@example.com");

        // Verificación
        assertEquals("dest-1", destinatario.getIdDestinatario(), "El idDestinatario debe coincidir");
        assertEquals("maria_gomez", destinatario.getUsuario(), "El usuario debe coincidir");
        assertEquals("maria@example.com", destinatario.getEmail(), "El email debe coincidir");
        assertNull(destinatario.getId(), "El id JPA debe ser nulo antes de persistir");
    }
}
