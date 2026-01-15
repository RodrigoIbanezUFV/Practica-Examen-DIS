package com.ufv.back;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ════════════════════════════════════════════════════════════════════
 * TESTS TDD - CRUD COMPLETO BACKEND
 * ════════════════════════════════════════════════════════════════════
 * 
 * FASE RED (Actual): Tests fallan porque no existe el código
 * FASE GREEN: Implementar código hasta que pasen
 * FASE REFACTOR: Mejorar código manteniendo tests verdes
 * 
 * PARA ADAPTAR EN EL EXAMEN:
 * --------------------------
 * 1. Cambiar "Libros" → Tu entidad en plural
 * 2. Cambiar "/api/libros" → Tu endpoint
 * 3. Cambiar campos en jsonPath según tu JSON
 * 4. Ajustar IDs según tu archivo JSON
 * 
 * ════════════════════════════════════════════════════════════════════
 */
@SpringBootTest
@AutoConfigureMockMvc
public class LibrosControllerTest { // Cambiar esto por el nombre que sea.

    @Autowired
    private MockMvc mockMvc;

    // TEST 1: GET /api/libros - Lista todos
    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get("/api/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // TEST 2: GET /api/libros/1 - Obtener uno que existe
    @Test
    public void testGetById_Existe() throws Exception {
        mockMvc.perform(get("/api/libros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").exists());
    }

    // TEST 3: GET /api/libros/9999 - Obtener uno que NO existe
    @Test
    public void testGetById_NoExiste() throws Exception {
        mockMvc.perform(get("/api/libros/9999"))
                .andExpect(status().isNotFound());
    }

    // TEST 4: POST /api/libros - Crear nuevo
    @Test
    public void testCreate() throws Exception {
        String nuevoLibro = """
                {
                    "titulo": "1984",
                    "autor": "George Orwell",
                    "paginas": 328
                }
                """;

        mockMvc.perform(post("/api/libros")
                .contentType("application/json")
                .content(nuevoLibro))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    // TEST 5: PUT /api/libros/1 - Actualizar existente
    @Test
    public void testUpdate() throws Exception {
        String libroActualizado = """
                {
                    "titulo": "El Quijote - Edición Nueva",
                    "autor": "Miguel de Cervantes",
                    "paginas": 900
                }
                """;

        mockMvc.perform(put("/api/libros/1")
                .contentType("application/json")
                .content(libroActualizado))
                .andExpect(status().isOk());
    }

    // TEST 6: DELETE /api/libros/1 - Eliminar
    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/libros/1"))
                .andExpect(status().isNoContent());
    }
}
