package co.edu.ucc.smartlibrary.repository;

import co.edu.ucc.smartlibrary.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
}
