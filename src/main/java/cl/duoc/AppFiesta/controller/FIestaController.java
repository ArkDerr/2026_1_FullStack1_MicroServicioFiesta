package cl.duoc.AppFiesta.controller;

import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.AppFiesta.assembler.FiestaModelAssembler;
import cl.duoc.AppFiesta.dto.request.FiestaCreateRequest;
import cl.duoc.AppFiesta.dto.request.FiestaUpdateRequest;
import cl.duoc.AppFiesta.dto.response.FiestaResponse;
import cl.duoc.AppFiesta.service.FiestaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/fiestas")
@RequiredArgsConstructor
public class FIestaController {
    // @Autowired
    private final FiestaService fiestaService;
    private final FiestaModelAssembler assembler;

    // Endpoint para buscar una fiesta
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<FiestaResponse>> obtenerUnaFiesta(@PathVariable Integer id) {
        FiestaResponse fiesta = fiestaService.obtenerUnaFiesta(id);
        if (fiesta == null) {
            return ResponseEntity.notFound().build(); // devuelve el error 404 Not Found, sin cuerpo o mensaje
        }
        return ResponseEntity.ok(assembler.toModel(fiesta)); // devuelve un 200 ok y el json guardado
    }

    // Endpoint para listar todas las fiestas
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<FiestaResponse>>> listarTodasFiestas() {
        // List<FiestaResponse> fiestas = fiestaService.obtenerTodasFiestas();
        return ResponseEntity.ok(assembler.toCollectionModel(
                fiestaService.obtenerTodasFiestas())); // Devuelve un 200 ok y la lista (fiestas)
    }

    // Endpoint para guardar o registar una fiesta
    @PostMapping
    public ResponseEntity<EntityModel<FiestaResponse>> guardarFiesta(@Valid @RequestBody FiestaCreateRequest request) {
        FiestaResponse fiestaGuardada = fiestaService.guardarFiesta(request);
        if (fiestaGuardada == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Devuelve un 409 Conflict por ya existir un
                                                                       // registro
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(fiestaGuardada)); // Devuelve un 201
                                                                                                      // Created, con el
                                                                                                      // objeto (Fiesta)
                                                                                                      // guardado
        }
    }

    // Endpoint para actualizar una fiesta por ID de la fiesta
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<FiestaResponse>> actualizarFiesta(
            @PathVariable Integer id,
            @Valid @RequestBody FiestaUpdateRequest request) {

        FiestaResponse fiestaActualizada = fiestaService.actualizarFiesta(id, request);

        if (fiestaActualizada == null) {
            return ResponseEntity.notFound().build(); // Devuelve el error 404 Not Found, sin cuerpo o mensaje
        }

        return ResponseEntity.ok(assembler.toModel(fiestaActualizada)); // Devuelve un 200 ok y el objeto (fiesta)
                                                                        // guardado
    }

    // Endpoint para eliminar una fiesta por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFiesta(@PathVariable Integer id) {
        boolean eliminada = fiestaService.eliminarFiesta(id);

        if (!eliminada) {
            return ResponseEntity.notFound().build(); // Devuelve el error 404 Not Found, sin cuerpo o mensaje
        }

        return ResponseEntity.noContent().build(); // Devuelve un 204 No Content (Operacion exitosa pero sin contenido)
    }

    // Usando query's personalizadas
    // ── BÚSQUEDAS ────────────────────────────────────
    @GetMapping("/buscar")
    public ResponseEntity<CollectionModel<EntityModel<FiestaResponse>>> buscarPorTitulo(
            @RequestParam String nombre) {
        return ResponseEntity.ok(assembler.toCollectionModel(fiestaService.buscarPorTitulo(nombre)));
    }

    @GetMapping("/comuna/{id}")
    public ResponseEntity<CollectionModel<EntityModel<FiestaResponse>>> buscarPorCategoria(
            @PathVariable Long id) {
        return ResponseEntity.ok(assembler.toCollectionModel(fiestaService.buscarPorComuna(id)));
    }

    @GetMapping("/capacidad")
    public ResponseEntity<CollectionModel<EntityModel<FiestaResponse>>> bajoCapacidad(
            @RequestParam Integer max) {
        return ResponseEntity.ok(assembler.toCollectionModel(fiestaService.buscarBajoCapacidad(max)));
    }

}
