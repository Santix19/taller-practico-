package co.edu.ucc.smartlibrary.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Entidad del dominio: libro del catalogo.
 * Es experta en su propio estado de disponibilidad (GRASP Experto, R1 y R3).
 */
@Entity
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private boolean disponible;

    protected Libro() {
        // requerido por JPA
    }

    public Libro(String titulo, boolean disponible) {
        this.titulo = titulo;
        this.disponible = disponible;
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public boolean isDisponible() { return disponible; }

    /** Regla R3: al prestarlo, el libro deja de estar disponible. */
    public void marcarPrestado() {
        this.disponible = false;
    }

    /** Se usara cuando se implemente la devolucion. */
    public void marcarDisponible() {
        this.disponible = true;
    }
}
