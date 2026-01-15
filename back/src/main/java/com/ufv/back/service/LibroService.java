package com.ufv.back.service;

import com.ufv.back.model.Libro;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * ════════════════════════════════════════════════════════════════════════════════
 *  SERVICE - CAPA DE LÓGICA DE NEGOCIO
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * QUÉ HACE EL SERVICE:
 * --------------------
 * 1. Carga el JSON al iniciar la aplicación
 * 2. Guarda los datos en memoria (List<Libro>)
 * 3. Proporciona métodos CRUD para manipular los datos
 * 4. Gestiona IDs automáticamente
 *
 * ADAPTACIÓN EN EL EXAMEN:
 * -------------------------
 * 1. Cambiar "Libro" por tu entidad (Character, Usuario, etc.)
 * 2. Cambiar "libros" por tu entidad en plural y minúscula
 * 3. Cambiar "/libros.json" por el nombre de tu archivo JSON
 * 4. Cambiar "items" en FileContents si tu JSON usa otro nombre
 *    Ejemplos: "characters", "usuarios", "data", etc.
 *
 * IMPORTANTE:
 * -----------
 * - @Service: Marca esta clase como un servicio de Spring
 * - @PostConstruct: Ejecuta loadData() automáticamente al iniciar
 * - Gson: Convierte JSON ↔ Objetos Java
 * - Optional<>: Indica que un valor puede existir o no (usado en getById, update)
 *
 * ════════════════════════════════════════════════════════════════════════════════
 */

@Service  // ← Marca esta clase como servicio de Spring (obligatorio)
public class LibroService {

    // ════════════════════════════════════════════════════════════════════════════
    //  VARIABLES DE INSTANCIA (Estado del servicio)
    // ════════════════════════════════════════════════════════════════════════════

    private List<Libro> libros;
    private Long nextId = 1L;


    /**
     * Método que carga el JSON automáticamente al iniciar
     *
     * @PostConstruct: Se ejecuta UNA VEZ después de crear el servicio
     *
     * PROCESO:
     * 1. Lee el archivo JSON desde resources
     * 2. Convierte el JSON a objetos Java con Gson
     * 3. Guarda la lista en memoria
     * 4. Calcula el siguiente ID disponible
     *
     * ← CAMBIAR: nombre del archivo JSON ("/libros.json")
     */
    @PostConstruct
    public void loadData() {
        // Crear instancia de Gson (conversor JSON ↔ Java)
        Gson gson = new Gson();

        // Leer el archivo JSON desde resources
        // ← CAMBIAR: "/libros.json" por tu archivo
        InputStream inputStream = getClass().getResourceAsStream("/libros.json");
        InputStreamReader reader = new InputStreamReader(inputStream);

        // Convertir el JSON a un objeto FileContents
        FileContents fileContents = gson.fromJson(reader, FileContents.class);

        // Guardar la lista de libros en memoria
        this.libros = new ArrayList<>(fileContents.getItems());

        // Calcular el siguiente ID disponible
        // (Busca el ID más alto y le suma 1)
        if (!libros.isEmpty()) {
            nextId = libros.stream()
                    .mapToLong(Libro::getId)  // ← CAMBIAR si tu método getId tiene otro nombre
                    .max()
                    .orElse(0L) + 1;
        }
    }

    // ════════════════════════════════════════════════════════════════════════════
    //  MÉTODO 1 de 5: CREATE (Crear nuevo elemento)
    // ════════════════════════════════════════════════════════════════════════════
    public Libro create(Libro libro) {
        libro.setId(nextId++);  // Asigna ID y luego incrementa (nextId = nextId + 1)
        libros.add(libro);      // Añade a la lista
        return libro;           // Devuelve el libro con su nuevo ID
    }

    // ════════════════════════════════════════════════════════════════════════════
    //  MÉTODO 2 de 5: READ ALL (Obtener todos los elementos)
    // ════════════════════════════════════════════════════════════════════════════
    public List<Libro> getAll() {
        return libros;
    }

    // ════════════════════════════════════════════════════════════════════════════
    //  MÉTODO 3 de 5: READ ONE (Obtener un elemento por ID)
    // ════════════════════════════════════════════════════════════════════════════
    public Optional<Libro> getById(Long id) {
        return libros.stream()                           // Convierte lista a stream
                .filter(libro -> libro.getId().equals(id))   // Filtra por ID
                .findFirst();                                // Devuelve el primero (o vacío)
    }

    // ════════════════════════════════════════════════════════════════════════════
    //  MÉTODO 4 de 5: UPDATE (Actualizar elemento existente)
    // ════════════════════════════════════════════════════════════════════════════
    public Optional<Libro> update(Long id, Libro libroActualizado) {
        // Recorre la lista buscando el libro
        for (int i = 0; i < libros.size(); i++) {
            if (libros.get(i).getId().equals(id)) {
                // Encontrado: mantiene el ID original
                libroActualizado.setId(id);
                // Reemplaza el libro en la lista
                libros.set(i, libroActualizado);
                return Optional.of(libroActualizado);
            }
        }
        // No encontrado: devuelve vacío
        return Optional.empty();
    }

    // ════════════════════════════════════════════════════════════════════════════
    //  MÉTODO 5 de 5: DELETE (Eliminar elemento)
    // ════════════════════════════════════════════════════════════════════════════
    public void delete(Long id) {
        libros.removeIf(libro -> libro.getId().equals(id));
    }

    // ════════════════════════════════════════════════════════════════════════════
    //  CLASE INTERNA: Wrapper para el JSON
    // ════════════════════════════════════════════════════════════════════════════
    /**
     * Clase auxiliar para leer el JSON
     *
     * POR QUÉ NECESITAMOS ESTO:
     * -------------------------
     * El JSON tiene esta estructura:
     * {
     *   "items": [
     *     { "id": 1, "titulo": "...", ... },
     *     { "id": 2, "titulo": "...", ... }
     *   ]
     * }
     *
     * Gson necesita una clase que coincida con esa estructura.
     * Esta clase "envuelve" (wraps) la lista de libros.
     *
     * ← CAMBIAR:
     * 1. Si tu JSON usa "items" → dejar igual
     * 2. Si usa "characters", "usuarios", etc. → cambiar @SerializedName
     * 3. Cambiar tipo List<Libro> según tu entidad
     */
    private static class FileContents {
        @SerializedName("items")  // ← CAMBIAR según tu JSON
        private List<Libro> items;  // ← CAMBIAR tipo según tu entidad

        /**
         * Getter de la lista
         */
        public List<Libro> getItems() {
            return items;
        }
    }
}
