package co.edu.ucc.smartlibrary.exception;

/** Se traduce a 409 Conflict (viola la regla R1). */
public class LibroNoDisponibleException extends RuntimeException {

    public LibroNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
