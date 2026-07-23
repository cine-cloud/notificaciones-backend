package ar.com.almacen_peliculas.notificaciones.data.repositories;

import ar.com.almacen_peliculas.notificaciones.domain.models.Destinatario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DestinatarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DestinatarioRepository destinatarioRepository;

    @Test
    void guardarYBuscarPorEmail_retornaDestinatario() {
        // Setup
        Destinatario destinatario = new Destinatario("dest-99", "Lucia Perez", "lucia@test.com");
        entityManager.persistAndFlush(destinatario);

        // Ejercitación
        Optional<Destinatario> encontrado = destinatarioRepository.findByEmail("lucia@test.com");

        // Verificación
        assertTrue(encontrado.isPresent(), "El destinatario debe existir en la base de datos");
        assertEquals("Lucia Perez", encontrado.get().getUsuario());
        assertEquals("dest-99", encontrado.get().getIdDestinatario());
    }

    @Test
    void guardarYBuscarPorIdDestinatario_retornaDestinatario() {
        // Setup
        Destinatario destinatario = new Destinatario("compra-300", "Marcos Gomez", "marcos@test.com");
        entityManager.persistAndFlush(destinatario);

        // Ejercitación
        Optional<Destinatario> encontrado = destinatarioRepository.findByIdDestinatario("compra-300");

        // Verificación
        assertTrue(encontrado.isPresent(), "El destinatario debe ser encontrado por idDestinatario");
        assertEquals("marcos@test.com", encontrado.get().getEmail());
    }
}
