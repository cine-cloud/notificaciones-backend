package ar.com.almacen_peliculas.notificaciones.data.repositories;

import ar.com.almacen_peliculas.notificaciones.domain.models.Destinatario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DestinatarioRepository extends JpaRepository<Destinatario, Long> {
    Optional<Destinatario> findByEmail(String email);
    Optional<Destinatario> findByIdDestinatario(String idDestinatario);
}
