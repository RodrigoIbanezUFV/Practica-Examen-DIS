package com.ufv.back.model;

import com.google.gson.annotations.SerializedName;

/**
 * ADAPTACIÓN EN EL EXAMEN:
 * -------------------------
 * 1. Cambiar nombre de clase según tu entidad
 *    Ejemplo: Character, Usuario, Producto, etc.
 *
 * 2. Cambiar campos según tu JSON del enunciado
 *    - Mantener siempre el campo "id" (Long)
 *    - Añadir/quitar campos según tu caso
 *
 * 3. Si tu JSON usa snake_case (ej: "page_count") y Java camelCase (ej: "paginas")
 *    Usar: @SerializedName("page_count")
 *
 * 4. Tipos de datos comunes:
 *    - Long → para IDs
 *    - String → para textos
 *    - Integer → para números enteros
 *    - Double → para decimales
 *    - Boolean → para verdadero/falso
 */
public class Libro {

    /**
     * ID único del libro
     * - Tipo: Long (no int, para IDs grandes)
     * - Se genera automáticamente en el Service
     * - SIEMPRE debe existir en cualquier entidad
     */
    private Long id;
    private String titulo;
    private String autor;
    private Integer paginas;

    // ════════════════════════════════════════════════════════════
    //  CONSTRUCTORES
    // ════════════════════════════════════════════════════════════

    /**
     * Constructor vacío
     * ----------------
     * OBLIGATORIO para Gson
     * Gson lo necesita para crear objetos desde JSON
     *
     * ⚠️ NO ELIMINAR aunque no lo uses directamente
     */
    public Libro() {
    }

    /**
     * Constructor completo
     * -------------------
     * Útil para crear objetos fácilmente en tests
     *
     * ← CAMBIAR: parámetros según tus campos
     */
    public Libro(Long id, String titulo, String autor, Integer paginas) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.paginas = paginas;
    }

    /**
     * ¿POR QUÉ NECESITAMOS GETTERS Y SETTERS?
     * ----------------------------------------
     * - Los campos son "private" (no se acceden directamente)
     * - Los getters permiten LEER valores
     * - Los setters permiten ESCRIBIR valores
     * - Spring y Gson los usan automáticamente
     *
     * GENERACIÓN AUTOMÁTICA:
     * ----------------------
     * En VSCode: Click derecho → "Generate Getters and Setters"
     * O usa atajos: Ctrl+. → "Generate..."
     *
     * ← CAMBIAR: generar para TODOS tus campos
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Integer getPaginas() {
        return paginas;
    }

    public void setPaginas(Integer paginas) {
        this.paginas = paginas;
    }

    /**
     * toString() - Para debugging
     * ----------------------------
     * Convierte el objeto a String legible
     * Útil para imprimir en consola: System.out.println(libro)
     *
     * OPCIONAL: Añadir si tienes tiempo en el examen
     */
    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", paginas=" + paginas +
                '}';
    }
}
