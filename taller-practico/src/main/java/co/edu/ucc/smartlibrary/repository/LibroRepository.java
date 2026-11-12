package co.edu.ucc.smartlibrary.repository;

import co.edu.ucc.smartlibrary.model.Libro;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    /**
     * Busca el libro bloqueandolo hasta el fin de la transaccion, de modo que dos
     * solicitudes simultaneas no puedan prestar el mismo libro (R5, consideracion de concurrencia).
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select l from Libro l where l.id = :id")
    Optional<Libro> buscarParaPrestar(@Param("id") Long id);
}
