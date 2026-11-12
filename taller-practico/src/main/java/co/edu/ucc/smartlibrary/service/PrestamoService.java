package co.edu.ucc.smartlibrary.service;

import co.edu.ucc.smartlibrary.dto.PrestamoRequest;
import co.edu.ucc.smartlibrary.dto.PrestamoResponse;
import co.edu.ucc.smartlibrary.exception.LibroNoDisponibleException;
import co.edu.ucc.smartlibrary.exception.RecursoNoEncontradoException;
import co.edu.ucc.smartlibrary.model.Estudiante;
import co.edu.ucc.smartlibrary.model.Libro;
import co.edu.ucc.smartlibrary.model.Prestamo;
import co.edu.ucc.smartlibrary.repository.EstudianteRepository;
import co.edu.ucc.smartlibrary.repository.LibroRepository;
import co.edu.ucc.smartlibrary.repository.PrestamoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Capa de negocio. Coordina el caso de uso "Registrar prestamo"
 * (GRASP Controlador de caso de uso, con Experto en las entidades).
 */
@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final EstudianteRepository estudianteRepository;
    private final LibroRepository libroRepository;

    public PrestamoService(PrestamoRepository prestamoRepository,
                           EstudianteRepository estudianteRepository,
                           LibroRepository libroRepository) {
        this.prestamoRepository = prestamoRepository;
        this.estudianteRepository = estudianteRepository;
        this.libroRepository = libroRepository;
    }

    /**
     * Una sola transaccion: si algo falla, ni se crea el prestamo ni cambia la
     * disponibilidad del libro (R4 y consistencia entre R2 y R3).
     */
    @Transactional
    public PrestamoResponse registrarPrestamo(PrestamoRequest request) {
        Estudiante estudiante = estudianteRepository.findById(request.estudianteId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Estudiante no encontrado: " + request.estudianteId()));

        Libro libro = libroRepository.buscarParaPrestar(request.libroId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Libro no encontrado: " + request.libroId()));

        if (!libro.isDisponible()) {                       // R1
            throw new LibroNoDisponibleException(
                    "El libro '" + libro.getTitulo() + "' no esta disponible");
        }

        libro.marcarPrestado();                             // R3
        Prestamo prestamo = prestamoRepository.save(
                new Prestamo(estudiante, libro, LocalDate.now())); // R2
        return PrestamoResponse.desde(prestamo);
    }

    @Transactional(readOnly = true)
    public List<PrestamoResponse> listarPrestamos() {
        return prestamoRepository.findAll().stream()
                .map(PrestamoResponse::desde)
                .toList();
    }
}
