package co.edu.ucc.smartlibrary.dto;

import co.edu.ucc.smartlibrary.model.Prestamo;

import java.time.LocalDate;

/** Objeto de salida: evita exponer las entidades JPA directamente. */
public record PrestamoResponse(
        Long id,
        Long estudianteId,
        String estudiante,
        Long libroId,
        String libro,
        LocalDate fechaPrestamo,
        LocalDate fechaDevolucion) {

    public static PrestamoResponse desde(Prestamo p) {
        return new PrestamoResponse(
                p.getId(),
                p.getEstudiante().getId(),
                p.getEstudiante().getNombre(),
                p.getLibro().getId(),
                p.getLibro().getTitulo(),
                p.getFechaPrestamo(),
                p.getFechaDevolucion());
    }
}
