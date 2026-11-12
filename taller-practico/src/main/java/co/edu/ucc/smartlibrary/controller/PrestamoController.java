package co.edu.ucc.smartlibrary.controller;

import co.edu.ucc.smartlibrary.dto.PrestamoRequest;
import co.edu.ucc.smartlibrary.dto.PrestamoResponse;
import co.edu.ucc.smartlibrary.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Capa de presentacion: solo entrada y salida HTTP. No contiene reglas de negocio. */
@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PrestamoResponse registrar(@Valid @RequestBody PrestamoRequest request) {
        return prestamoService.registrarPrestamo(request);
    }

    @GetMapping
    public List<PrestamoResponse> listar() {
        return prestamoService.listarPrestamos();
    }
}
