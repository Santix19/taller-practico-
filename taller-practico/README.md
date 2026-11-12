# SmartLibrary – Taller práctico bloque 7

Diseño de Software · Caso de uso **Registrar préstamo** como API REST por tres capas
(Controller → Service → Repository) con Spring Boot 3, Java 21, Spring Data JPA, Validation y H2.

## Contenido del repositorio

| Ruta | Qué contiene |
|---|---|
| `src/main/java/.../controller` | `PrestamoController` (entrada y salida HTTP) |
| `src/main/java/.../service` | `PrestamoService` (coordina el caso de uso, transacción) |
| `src/main/java/.../repository` | `PrestamoRepository`, `EstudianteRepository`, `LibroRepository` |
| `src/main/java/.../model` | Entidades de dominio: `Estudiante`, `Libro`, `Prestamo` |
| `src/main/java/.../dto` | `PrestamoRequest`, `PrestamoResponse` |
| `src/main/java/.../exception` | Excepciones de negocio y `GlobalExceptionHandler` (404, 409, 400) |
| `src/test/java/...` | `PrestamoApiTest`: pruebas de los escenarios del taller |
| `docs/Taller_practico_bloque_7_SmartLibrary.docx` | Documento del taller: observaciones, tabla de responsabilidades, diagramas, defensa y explicación |
| `docs/diagramas/` | Diagramas en PNG y su código fuente (`.dot` y `.mmd`) |

## Ejecutar

```bash
mvn spring-boot:run     # API en http://localhost:8080
mvn test                # pruebas de los escenarios
```

Datos iniciales (`data.sql`): estudiantes 1 y 2; libros 1 y 2 disponibles; libro 3 ya prestado.

```bash
# 201 Created
curl -i -X POST localhost:8080/api/prestamos -H "Content-Type: application/json" \
     -d '{"estudianteId":1,"libroId":1}'
# 409 Conflict (libro ya prestado)
curl -i -X POST localhost:8080/api/prestamos -H "Content-Type: application/json" \
     -d '{"estudianteId":1,"libroId":3}'
# 200 OK
curl -i localhost:8080/api/prestamos
```

## Reglas de negocio y dónde se cumplen

| Regla | Dónde |
|---|---|
| R1 Solo un libro disponible puede prestarse | `PrestamoService.registrarPrestamo` + `Libro.isDisponible` |
| R2 El préstamo registra estudiante, libro y fecha | `new Prestamo(estudiante, libro, fecha)` |
| R3 Al prestarlo, el libro deja de estar disponible | `Libro.marcarPrestado` |
| R4 Una solicitud inválida no crea préstamo | `@Valid`, `@Transactional`, `GlobalExceptionHandler` |
| R5 Varios préstamos históricos, solo uno activo | `Libro.disponible` + bloqueo en `LibroRepository.buscarParaPrestar` |

## Escenarios

| Escenario | Resultado | Prueba |
|---|---|---|
| Libro disponible | 201 Created | `prestamoDeLibroDisponible_devuelve201` |
| Libro ya prestado | 409 Conflict, sin nuevo préstamo | `libroYaPrestado_devuelve409_yNoCreaPrestamo` |
| Estudiante inexistente | 404 Not Found | `estudianteInexistente_devuelve404` |
| Libro inexistente | 404 Not Found | `libroInexistente_devuelve404` |
| Consulta de préstamos | 200 OK | `consultaDePrestamos_devuelve200ConRegistros` |
| Solicitud sin campos | 400 Bad Request | `solicitudSinCampos_devuelve400` |
