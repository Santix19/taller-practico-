package co.edu.ucc.smartlibrary;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Trazabilidad requisito -> prueba. Datos iniciales (data.sql):
 * estudiantes 1 y 2; libros 1 y 2 disponibles, libro 3 ya prestado.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PrestamoApiTest {

    @Autowired
    private MockMvc mvc;

    private String json(Object estudianteId, Object libroId) {
        return "{\"estudianteId\":" + estudianteId + ",\"libroId\":" + libroId + "}";
    }

    @Test // R1, R2, R3
    void prestamoDeLibroDisponible_devuelve201() throws Exception {
        mvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(json(1, 1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.libro").value("Clean Code"));

        // El libro ya no esta disponible: un segundo intento debe dar conflicto (R3)
        mvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(json(2, 1)))
                .andExpect(status().isConflict());
    }

    @Test // R1, R4, R5
    void libroYaPrestado_devuelve409_yNoCreaPrestamo() throws Exception {
        mvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(json(1, 3)))
                .andExpect(status().isConflict());

        mvc.perform(get("/api/prestamos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1)); // solo el prestamo inicial
    }

    @Test // R4
    void estudianteInexistente_devuelve404() throws Exception {
        mvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(json(99, 1)))
                .andExpect(status().isNotFound());
    }

    @Test // R4
    void libroInexistente_devuelve404() throws Exception {
        mvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(json(1, 99)))
                .andExpect(status().isNotFound());
    }

    @Test // R4: solicitud invalida
    void solicitudSinCampos_devuelve400() throws Exception {
        mvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void consultaDePrestamos_devuelve200ConRegistros() throws Exception {
        mvc.perform(get("/api/prestamos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].libro").value("UML Distilled"));
    }
}
