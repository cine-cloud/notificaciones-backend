package ar.com.almacen_peliculas.notificaciones.domain.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Destinatario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String idDestinatario;
    private String usuario;
    private String email;

    protected Destinatario() {}

    public Destinatario(String idDestinatario, String usuario, String email) {
        this.idDestinatario = idDestinatario;
        this.usuario = usuario;
        this.email = email;
    }
}
