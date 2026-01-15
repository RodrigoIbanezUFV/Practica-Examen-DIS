package com.ufv.back.controller;

import com.ufv.back.model.Libro;
import com.ufv.back.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * ════════════════════════════════════════════════════════════════════════════════
 *  CONTROLLER - CAPA DE PRESENTACIÓN (API REST)
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * QUÉ HACE EL CONTROLLER:
 * -----------------------
 * 1. Define los endpoints REST (URLs de la API)
 * 2. Recibe peticiones HTTP (GET, POST, PUT, DELETE)
 * 3. Llama al Service para ejecutar la lógica
 * 4. Devuelve respuestas HTTP con códigos de estado
 *
 * ANOTACIONES CLAVE:
 * ------------------
 * @RestController: Combina @Controller + @ResponseBody
 *   → Los métodos devuelven datos JSON directamente
 *
 * @RequestMapping: Define la ruta base de todos los endpoints
 *   Ejemplo: "/api/libros" → http://localhost:8080/api/libros
 *
 * @CrossOrigin: ¡MUY IMPORTANTE! Permite que el frontend acceda a la API
 *   Sin esto, el navegador bloquea las peticiones (error CORS)
 *
 * ResponseEntity<T>: Envuelve la respuesta con código HTTP
 *   Permite devolver 200, 201, 404, etc.
 *
 * ADAPTACIÓN EN EL EXAMEN:
 * -------------------------
 * 1. Cambiar "Libros" por tu entidad en plural (Characters, Usuarios, etc.)
 * 2. Cambiar "/api/libros" por tu ruta
 * 3. Cambiar tipos Libro/LibroService según tu entidad
 * 4. Los 5 métodos CRUD SIEMPRE son iguales, solo cambian los nombres
 *
 * ════════════════════════════════════════════════════════════════════════════════
 */

@RestController                    // ← Marca esta clase como controlador REST
@RequestMapping("/api/libros")     // ← CAMBIAR: ruta base de tu API
@CrossOrigin(origins = "*")        // ← ¡CRÍTICO! Permite acceso desde frontend
public class LibroController {

    // ════════════════════════════════════════════════════════════════════════════
    //  INYECCIÓN DE DEPENDENCIAS
    // ════════════════════════════════════════════════════════════════════════════
    /**
     * @Autowired: Spring automáticamente inyecta el servicio
     *
     * NO necesitas hacer: new LibroService()
     * Spring lo gestiona automáticamente (Inversión de Control)
     *
     * ← CAMBIAR: tipo según tu servicio
     */
    @Autowired
    private LibroService service;

    // ════════════════════════════════════════════════════════════════════════════
    //  ENDPOINT 1 de 5: GET /api/libros (READ ALL)
    // ════════════════════════════════════════════════════════════════════════════
    /**
     * Obtiene todos los libros
     *
     * ENDPOINT: GET http://localhost:8080/api/libros
     *
     * @GetMapping: Maneja peticiones GET a la ruta base
     * Sin parámetros → devuelve la lista completa
     *
     * ← CAMBIAR: tipos según tu entidad
     */
    @GetMapping
    public ResponseEntity<List<Libro>> getAll() {
        List<Libro> libros = service.getAll();
        return ResponseEntity.ok(libros);  // Devuelve 200 + lista
    }

    // ════════════════════════════════════════════════════════════════════════════
    //  ENDPOINT 2 de 5: GET /api/libros/{id} (READ ONE)
    // ════════════════════════════════════════════════════════════════════════════
    /**
     * Obtiene un libro específico por su ID
     *
     * ENDPOINT: GET http://localhost:8080/api/libros/1
     *
     * @GetMapping("/{id}"): Captura el ID de la URL
     * @PathVariable: Mapea {id} de la URL al parámetro del método
     *
     * ← CAMBIAR: tipo según tu entidad
     */
    @GetMapping("/{id}")
    public ResponseEntity<Libro> getById(@PathVariable Long id) {
        Optional<Libro> libro = service.getById(id);

        // Si existe → 200 OK con el libro
        // Si no existe → 404 NOT FOUND
        return libro.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ════════════════════════════════════════════════════════════════════════════
    //  ENDPOINT 3 de 5: POST /api/libros (CREATE)
    // ════════════════════════════════════════════════════════════════════════════
    /**
     * Crea un nuevo libro
     *
     * ENDPOINT: POST http://localhost:8080/api/libros
     *
     * @PostMapping: Maneja peticiones POST
     * @RequestBody: Convierte el JSON del body a un objeto Libro
     *
     *
     * ← CAMBIAR: tipos según tu entidad
     */
    @PostMapping
    public ResponseEntity<Libro> create(@RequestBody Libro libro) {
        Libro nuevoLibro = service.create(libro);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoLibro);
    }

    // ════════════════════════════════════════════════════════════════════════════
    //  ENDPOINT 4 de 5: PUT /api/libros/{id} (UPDATE)
    // ════════════════════════════════════════════════════════════════════════════
    /**
     * Actualiza un libro existente
     *
     * ENDPOINT: PUT http://localhost:8080/api/libros/1
     *
     * @PutMapping("/{id}"): Maneja peticiones PUT con ID
     * @PathVariable: Captura el ID de la URL
     * @RequestBody: Convierte el JSON a objeto Libro
     *
     * NOTA: El ID de la URL tiene prioridad sobre cualquier ID en el JSON
     *
     * ← CAMBIAR: tipos según tu entidad
     */
    @PutMapping("/{id}")
    public ResponseEntity<Libro> update(@PathVariable Long id, @RequestBody Libro libro) {
        Optional<Libro> libroActualizado = service.update(id, libro);

        // Si existe → 200 OK con el libro actualizado
        // Si no existe → 404 NOT FOUND
        return libroActualizado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ════════════════════════════════════════════════════════════════════════════
    //  ENDPOINT 5 de 5: DELETE /api/libros/{id} (DELETE)
    // ════════════════════════════════════════════════════════════════════════════
    /**
     * Elimina un libro
     *
     * ENDPOINT: DELETE http://localhost:8080/api/libros/1
     *
     * @DeleteMapping("/{id}"): Maneja peticiones DELETE con ID
     * @PathVariable: Captura el ID de la URL
     *
     * NOTA: Devuelve 204 aunque el ID no exista
     * (Es idempotente: eliminar algo que no existe = OK)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();  // 204 NO CONTENT
    }

    // ════════════════════════════════════════════════════════════════════════════
    //  GUÍA RÁPIDA DE CÓDIGOS HTTP
    // ════════════════════════════════════════════════════════════════════════════
    //
    //  200 OK          → Operación exitosa con contenido (GET, PUT)
    //  201 CREATED     → Recurso creado exitosamente (POST)
    //  204 NO CONTENT  → Operación exitosa sin contenido (DELETE)
    //  404 NOT FOUND   → Recurso no encontrado (GET/PUT con ID inexistente)
    //  400 BAD REQUEST → Petición incorrecta (datos inválidos)
    //  500 SERVER ERROR→ Error interno del servidor
    //
    // ════════════════════════════════════════════════════════════════════════════

    // ════════════════════════════════════════════════════════════════════════════
    //  RESUMEN DE ENDPOINTS
    // ════════════════════════════════════════════════════════════════════════════
    //
    //  GET    /api/libros      → Lista todos los libros
    //  GET    /api/libros/1    → Obtiene el libro con id=1
    //  POST   /api/libros      → Crea un nuevo libro
    //  PUT    /api/libros/1    → Actualiza el libro con id=1
    //  DELETE /api/libros/1    → Elimina el libro con id=1
    //
    // ════════════════════════════════════════════════════════════════════════════
}
