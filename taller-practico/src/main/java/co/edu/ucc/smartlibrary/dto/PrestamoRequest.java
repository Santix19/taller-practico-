package co.edu.ucc.smartlibrary.dto;

import jakarta.validation.constraints.NotNull;

/** Objeto de entrada del POST /api/prestamos (R4: solicitud invalida no crea prestamo). */
public record PrestamoRequest(
        @NotNull(message = "estudianteId es obligatorio") Long estudianteId,
        @NotNull(message = "libroId es obligatorio") Long libroId) {
}
